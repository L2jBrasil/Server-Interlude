/* This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jbr.gameserver.taskmanager;

import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.model.entity.database.GlobalTasks;
import com.l2jbr.gameserver.model.entity.database.repository.GlobalTaskRepository;
import com.l2jbr.gameserver.taskmanager.tasks.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

import static com.l2jbr.commons.database.DatabaseAccess.getRepository;
import static com.l2jbr.gameserver.taskmanager.TaskTypes.*;
import static java.util.Objects.isNull;


/**
 * @author Layane
 */
public final class TaskManager {
    protected static final Logger _log = LoggerFactory.getLogger(TaskManager.class.getName());
    private static TaskManager _instance;

    private final LinkedHashMap<Integer, Task> _tasks = new LinkedHashMap<>();
    protected final List<ExecutedTask> _currentTasks = new LinkedList<>();

    public class ExecutedTask implements Runnable {
        int id;
        long lastActivation;
        Task task;
        TaskTypes type;
        String[] params;
        ScheduledFuture<?> scheduled;

        public ExecutedTask(Task ptask, TaskTypes ptype, GlobalTasks globalTask)  {
            task = ptask;
            type = ptype;
            id = globalTask.getId();
            lastActivation = globalTask.getLastActivation();
            params = new String[]   {
                globalTask.getParam1(),
                globalTask.getParam2(),
                globalTask.getParam3()
            };
        }

        @Override
        public void run() {
            task.onTimeElapsed(this);

            lastActivation = System.currentTimeMillis();

            GlobalTaskRepository repository = getRepository(GlobalTaskRepository.class);
            repository.updateLastActivationById(id, lastActivation);

            if ((type == TYPE_SHEDULED) || (type == TYPE_TIME)) {
                stopTask();
            }
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public boolean equals(Object object) {
            return id == ((ExecutedTask) object).id;
        }

        public Task getTask() {
            return task;
        }

        public TaskTypes getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public String[] getParams() {
            return params;
        }

        public long getLastActivation() {
            return lastActivation;
        }

        public void stopTask() {
            task.onDestroy();

            if (scheduled != null) {
                scheduled.cancel(true);
            }

            _currentTasks.remove(this);
        }

    }

    public static TaskManager getInstance() {
        if (isNull(_instance)) {
            _instance = new TaskManager();
        }
        return _instance;
    }

    public TaskManager() {
        initializate();
        startAllTasks();
    }

    private void initializate() {
        registerTask(new TaskJython());
        registerTask(new TaskOlympiadSave());
        registerTask(new TaskRecom());
        registerTask(new TaskRestart());
        registerTask(new TaskSevenSignsUpdate());
        registerTask(new TaskShutdown());
    }

    private void registerTask(Task task) {
        int key = task.getName().trim().toLowerCase().hashCode();
        if (!_tasks.containsKey(key)) {
            _tasks.put(key, task);
            task.initializate();
        }
    }

    private void startAllTasks() {
        getRepository(GlobalTaskRepository.class).findAll().forEach(globalTasks -> {
            Task task = _tasks.get(globalTasks.getTask().trim().toLowerCase().hashCode());

            if (task == null) {
                return;
            }

            TaskTypes type = TaskTypes.valueOf(globalTasks.getType());

            if (type != TYPE_NONE) {
                ExecutedTask current = new ExecutedTask(task, type, globalTasks);
                if (launchTask(current)) {
                    _currentTasks.add(current);
                }
            }
        });

    }

    private boolean launchTask(ExecutedTask task) {
        final ThreadPoolManager scheduler = ThreadPoolManager.getInstance();
        final TaskTypes type = task.getType();

        if (type == TYPE_STARTUP) {
            task.run();
            return false;
        } else if (type == TYPE_SHEDULED) {
            long delay = Long.valueOf(task.getParams()[0]);
            task.scheduled = scheduler.scheduleGeneral(task, delay);
            return true;
        } else if (type == TYPE_FIXED_SHEDULED) {
            long delay = Long.valueOf(task.getParams()[0]);
            long interval = Long.valueOf(task.getParams()[1]);

            task.scheduled = scheduler.scheduleGeneralAtFixedRate(task, delay, interval);
            return true;
        } else if (type == TYPE_TIME) {
            try {
                Date desired = DateFormat.getInstance().parse(task.getParams()[0]);
                long diff = desired.getTime() - System.currentTimeMillis();
                if (diff >= 0) {
                    task.scheduled = scheduler.scheduleGeneral(task, diff);
                    return true;
                }
                _log.info("Task " + task.getId() + " is obsoleted.");
            } catch (Exception e) {
            }
        } else if (type == TYPE_SPECIAL) {
            ScheduledFuture<?> result = task.getTask().launchSpecial(task);
            if (result != null) {
                task.scheduled = result;
                return true;
            }
        } else if (type == TYPE_GLOBAL_TASK) {
            long interval = Long.valueOf(task.getParams()[0]) * 86400000L;
            String[] hour = task.getParams()[1].split(":");

            if (hour.length != 3) {
                _log.warn("Task " + task.getId() + " has incorrect parameters");
                return false;
            }

            Calendar check = Calendar.getInstance();
            check.setTimeInMillis(task.getLastActivation() + interval);

            Calendar min = Calendar.getInstance();
            try {
                min.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour[0]));
                min.set(Calendar.MINUTE, Integer.valueOf(hour[1]));
                min.set(Calendar.SECOND, Integer.valueOf(hour[2]));
            } catch (Exception e) {
                _log.warn("Bad parameter on task " + task.getId() + ": " + e.getMessage());
                return false;
            }

            long delay = min.getTimeInMillis() - System.currentTimeMillis();

            if (check.after(min) || (delay < 0)) {
                delay += interval;
            }

            task.scheduled = scheduler.scheduleGeneralAtFixedRate(task, delay, interval);

            return true;
        }

        return false;
    }

    public static boolean addUniqueTask(String task, TaskTypes type, String param1, String param2, String param3) {
        return addUniqueTask(task, type, param1, param2, param3, 0);
    }

    public static boolean addUniqueTask(String task, TaskTypes type, String param1, String param2, String param3, long lastActivation) {
        GlobalTaskRepository repository = getRepository(GlobalTaskRepository.class);
        if(!repository.existsByTask(task)) {
            GlobalTasks globalTask = new GlobalTasks(task, type.toString(), lastActivation, param1, param2, param3);
            repository.save(globalTask);
            return true;
        }
        return  false;
    }

    public static boolean addTask(String task, TaskTypes type, String param1, String param2, String param3) {
        return addTask(task, type, param1, param2, param3, 0);
    }

    public static boolean addTask(String task, TaskTypes type, String param1, String param2, String param3, long lastActivation) {
        GlobalTasks globalTask = new GlobalTasks(task, type.toString(), lastActivation, param1, param2, param3);
        GlobalTaskRepository repository = getRepository(GlobalTaskRepository.class);
        repository.save(globalTask);
        return true;
    }

}
