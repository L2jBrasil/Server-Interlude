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

import com.l2jbr.commons.util.DateRange;
import com.l2jbr.gameserver.script.IntList;
import com.l2jbr.gameserver.script.Parser;
import com.l2jbr.gameserver.script.ParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.util.Date;

import static com.l2jbr.gameserver.util.GameserverMessages.getMessage;

public class XmlEventParser extends XmlParser {
    static Logger _log = LoggerFactory.getLogger(XmlEventParser.class);
    private DateRange eventDates = null;

    @Override
    public void parseScript(Node eventNode) {
        String id = attribute(eventNode, "ID");

        _log.debug(getMessage("debug.parsing.event", id));

        eventDates = DateRange.parse(attribute(eventNode, "Active"), DATE_FORMAT);

        Date currentDate = new Date();
        if (eventDates.getEndDate().before(currentDate)) {
            _log.info(getMessage("info.event.passed", id));
            return;
        }

        for (Node node = eventNode.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (isNodeName(node, "DropList")) {
                parseEventDropList(node);
            } else if (isNodeName(node, "Message")) {
                parseEventMessage(node);
            }
        }
    }

    private void parseEventMessage(Node sysMsg) {
        if (DEBUG) {
            _log.debug("Parsing Event Message.");
        }

        try {
            String type = attribute(sysMsg, "Type");
            String[] message = attribute(sysMsg, "Msg").split("\n");

            if (type.equalsIgnoreCase("OnJoin")) {
                _bridge.onPlayerLogin(message, eventDates);
            }
        } catch (Exception e) {
            _log.warn("Error in event parser.");
            e.printStackTrace();
        }
    }

    private void parseEventDropList(Node dropList) {
        if (DEBUG) {
            _log.debug("Parsing Droplist.");
        }

        for (Node node = dropList.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (isNodeName(node, "AllDrop")) {
                parseEventDrop(node);
            }
        }
    }

    private void parseEventDrop(Node drop) {
        if (DEBUG) {
            _log.debug("Parsing Drop.");
        }

        try {
            int[] items = IntList.parse(attribute(drop, "Items"));
            int[] count = IntList.parse(attribute(drop, "Count"));
            double chance = getPercent(attribute(drop, "Chance"));

            _bridge.addEventDrop(items, count, chance, eventDates);
        } catch (Exception e) {
            System.err.println("ERROR(parseEventDrop):" + e.getMessage());
        }
    }

    static class FaenorEventParserFactory implements ParserFactory {
        @Override
        public Parser create() {
            return (new XmlEventParser());
        }
    }

    static {
        ScriptEventManager.parserFactories.put(getParserName("Event"), new FaenorEventParserFactory());
    }
}
