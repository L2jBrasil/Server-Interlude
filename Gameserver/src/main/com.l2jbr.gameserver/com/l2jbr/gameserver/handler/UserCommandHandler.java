/*
 * This program is free software; you can redistribute it and/or modify
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
package com.l2jbr.gameserver.handler;

import com.l2jbr.commons.Config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class ...
 *
 * @version $Revision: 1.1.2.1.2.5 $ $Date: 2005/03/27 15:30:09 $
 */
public class UserCommandHandler {
    private static Logger _log = Logger.getLogger(UserCommandHandler.class.getName());

    private static UserCommandHandler _instance;

    private final Map<Integer, IUserCommandHandler> _datatable;

    public static UserCommandHandler getInstance() {
        if (_instance == null) {
            _instance = new UserCommandHandler();
        }
        return _instance;
    }

    private UserCommandHandler() {
        _datatable = new LinkedHashMap<>();
    }

    public void registerUserCommandHandler(IUserCommandHandler handler) {
        int[] ids = handler.getUserCommandList();
        for (int id : ids) {
            if (Config.DEBUG) {
                _log.fine("Adding handler for user command " + id);
            }
            _datatable.put(id, handler);
        }
    }

    public IUserCommandHandler getUserCommandHandler(int userCommand) {
        if (Config.DEBUG) {
            _log.fine("getting handler for user command: " + userCommand);
        }
        return _datatable.get(userCommand);
    }

    /**
     * @return
     */
    public int size() {
        return _datatable.size();
    }
}