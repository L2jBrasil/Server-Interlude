package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("global_tasks")
public class GlobalTasks  {

    @Id
    private int id;
    private String task;
    private String type;
    @Column("last_activation")
    private long lastActivation;
    private String param1;
    private String param2;
    private String param3;

    public GlobalTasks() {}

    public GlobalTasks(String task, String s, long lastActivation, String param1, String param2, String param3) {
        this.task = task;
        this.type = s;
        this.lastActivation = lastActivation;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }

    public Integer getId() {
        return id;
    }
    public String getTask() {
        return task;
    }

    public String getType() {
        return type;
    }

    public long getLastActivation() {
        return lastActivation;
    }

    public String getParam1() {
        return param1;
    }

    public String getParam2() {
        return param2;
    }

    public String getParam3() {
        return param3;
    }
}
