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
package com.l2jbr.gameserver.serverpackets;

import com.l2jbr.gameserver.model.entity.database.PlayerTemplate;

import java.util.LinkedList;
import java.util.List;


/**
 * This class ...
 *
 * @version $Revision: 1.3.2.1.2.7 $ $Date: 2005/03/27 15:29:39 $
 */
public class CharTemplates extends L2GameServerPacket {
    // dddddddddddddddddddd
    private static final String _S__23_CHARTEMPLATES = "[S] 23 CharTemplates";
    private final List<PlayerTemplate> _chars = new LinkedList<>();

    public void addChar(PlayerTemplate template) {
        _chars.add(template);
    }

    @Override
    protected final void writeImpl() {
        writeByte(0x17);
        writeInt(_chars.size());

        for (PlayerTemplate temp : _chars) {
            writeInt(temp.getRace().ordinal());
            writeInt(temp.getPlayerClass().getId());
            writeInt(0x46);
            writeInt(temp.getStrength());
            writeInt(0x0a);
            writeInt(0x46);
            writeInt(temp.getDexterity());
            writeInt(0x0a);
            writeInt(0x46);
            writeInt(temp.getConstitution());
            writeInt(0x0a);
            writeInt(0x46);
            writeInt(temp.getIntellienge());
            writeInt(0x0a);
            writeInt(0x46);
            writeInt(temp.getWitness());
            writeInt(0x0a);
            writeInt(0x46);
            writeInt(temp.getMentality());
            writeInt(0x0a);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.l2jbr.gameserver.serverpackets.ServerBasePacket#getType()
     */
    @Override
    public String getType() {
        return _S__23_CHARTEMPLATES;
    }
}
