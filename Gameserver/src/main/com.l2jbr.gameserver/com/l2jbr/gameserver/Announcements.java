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
package com.l2jbr.gameserver;

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.cache.HtmCache;
import com.l2jbr.gameserver.clientpackets.Say2;
import com.l2jbr.gameserver.model.L2World;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.commons.util.DateRange;
import com.l2jbr.gameserver.serverpackets.CreatureSay;
import com.l2jbr.gameserver.serverpackets.NpcHtmlMessage;
import com.l2jbr.gameserver.serverpackets.SystemMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * This class ...
 *
 * @version $Revision: 1.5.2.1.2.7 $ $Date: 2005/03/29 23:15:14 $
 */
public class Announcements {
    private static Logger _log = LoggerFactory.getLogger(Announcements.class.getName());

    private static Announcements _instance;
    private final List<String> _announcements = new LinkedList<>();
    private final List<List<Object>> _eventAnnouncements = new LinkedList<>();

    public Announcements() {
        loadAnnouncements();
    }

    public static Announcements getInstance() {
        if (_instance == null) {
            _instance = new Announcements();
        }

        return _instance;
    }

    public void loadAnnouncements() {
        _announcements.clear();
        File file = new File(Config.DATAPACK_ROOT, "data/announcements.txt");
        if (file.exists()) {
            readFromDisk(file);
        } else {
            _log.info("data/announcements.txt doesn't exist");
        }
    }

    public void showAnnouncements(L2PcInstance activeChar) {
        for (int i = 0; i < _announcements.size(); i++) {
            CreatureSay cs = new CreatureSay(0, Say2.ANNOUNCEMENT, activeChar.getName(), _announcements.get(i));
            activeChar.sendPacket(cs);
        }

        for (int i = 0; i < _eventAnnouncements.size(); i++) {
            List<Object> entry = _eventAnnouncements.get(i);

            DateRange validDateRange = (DateRange) entry.get(0);
            String[] msg = (String[]) entry.get(1);
            Date currentDate = new Date();

            if (!validDateRange.isValid() || validDateRange.isWithinRange(currentDate)) {
                SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2);
                for (String element : msg) {
                    sm.addString(element);
                }
                activeChar.sendPacket(sm);
            }

        }
    }

    public void addEventAnnouncement(DateRange validDateRange, String[] msg) {
        List<Object> entry = new LinkedList<>();
        entry.add(validDateRange);
        entry.add(msg);
        _eventAnnouncements.add(entry);
    }

    public void listAnnouncements(L2PcInstance activeChar) {
        String content = HtmCache.getInstance().getHtmForce("data/html/admin/announce.htm");
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        adminReply.setHtml(content);
        StringBuilder replyMSG = new StringBuilder("<br>");
        for (int i = 0; i < _announcements.size(); i++) {
            replyMSG.append("<table width=260><tr><td width=220>" + _announcements.get(i) + "</td><td width=40>");
            replyMSG.append("<button value=\"Delete\" action=\"bypass -h admin_del_announcement " + i + "\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table>");
        }
        adminReply.replace("%announces%", replyMSG.toString());
        activeChar.sendPacket(adminReply);
    }

    public void addAnnouncement(String text) {
        _announcements.add(text);
        saveToDisk();
    }

    public void delAnnouncement(int line) {
        _announcements.remove(line);
        saveToDisk();
    }

    private void readFromDisk(File file) {
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(file))) {
            int i = 0;
            String line = null;
            while ((line = lnr.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "\n\r");
                if (st.hasMoreTokens()) {
                    String announcement = st.nextToken();
                    _announcements.add(announcement);

                    i++;
                }
            }

            _log.info("Announcements: Loaded " + i + " Announcements.");
        } catch (IOException e1) {
            _log.error( "Error reading announcements", e1);
        }
    }

    private void saveToDisk() {
        File file = new File("data/announcements.txt");
        FileWriter save = null;

        try {
            save = new FileWriter(file);
            for (int i = 0; i < _announcements.size(); i++) {
                save.write(_announcements.get(i));
                save.write("\r\n");
            }
            save.flush();
            save.close();
            save = null;
        } catch (IOException e) {
            _log.warn("saving the announcements file has failed: " + e);
        }
    }

    public void announceToAll(String text) {
        CreatureSay cs = new CreatureSay(0, Say2.ANNOUNCEMENT, "", text);

        for (L2PcInstance player : L2World.getInstance().getAllPlayers()) {
            player.sendPacket(cs);
        }
    }

    public void announceToAll(SystemMessage sm) {

        for (L2PcInstance player : L2World.getInstance().getAllPlayers()) {
            player.sendPacket(sm);
        }
    }

    // Method fo handling announcements from admin
    public void handleAnnounce(String command, int lengthToTrim) {
        try {
            // Announce string to everyone on server
            String text = command.substring(lengthToTrim);
            Announcements.getInstance().announceToAll(text);
        }

        // No body cares!
        catch (StringIndexOutOfBoundsException e) {
            // empty message.. ignore
        }
    }
}
