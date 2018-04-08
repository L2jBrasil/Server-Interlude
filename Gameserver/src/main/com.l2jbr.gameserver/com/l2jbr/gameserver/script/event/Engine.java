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
package com.l2jbr.gameserver.script.event;

import com.l2jbr.gameserver.Announcements;
import com.l2jbr.gameserver.datatables.EventDroplist;
import com.l2jbr.gameserver.model.L2DropData;
import com.l2jbr.commons.util.DateRange;

/**
 * @author Luis Arias
 */
public class Engine {

    private static Engine _instance;

    public static Engine getInstance() {
        if (_instance == null) {
            _instance = new Engine();
        }
        return _instance;
    }

    private Engine() {  }

    public void addEventDrop(int[] items, int[] count, double chance, DateRange range) {
        EventDroplist.getInstance().addGlobalDrop(items, count, (int) (chance * L2DropData.MAX_CHANCE), range);
    }

    public void onPlayerLogin(String[] message, DateRange validDateRange) {
        Announcements.getInstance().addEventAnnouncement(validDateRange, message);
    }

}
