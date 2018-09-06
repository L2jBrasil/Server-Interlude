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
package com.l2jbr.gameserver.serverpackets;

import com.l2jbr.gameserver.model.L2Manor;
import com.l2jbr.gameserver.model.entity.database.SeedProduction;

import java.util.LinkedList;
import java.util.List;


/**
 * format(packet 0xFE) ch ddd [dddddcdcd] c - id h - sub id d - manor id d d - size [ d - seed id d - left to buy d - started amount d - sell price d - seed level c d - reward 1 id c d - reward 2 id ]
 *
 * @author l3x
 */
public class ExShowSeedInfo extends L2GameServerPacket {
    private static final String _S__FE_1C_EXSHOWSEEDINFO = "[S] FE:1C ExShowSeedInfo";
    private List<SeedProduction> _seeds;
    private final int _manorId;

    public ExShowSeedInfo(int manorId, List<SeedProduction> seeds) {
        _manorId = manorId;
        _seeds = seeds;
        if (_seeds == null) {
            _seeds = new LinkedList<>();
        }
    }

    @Override
    protected void writeImpl() {
        writeByte(0xFE); // Id
        writeShort(0x1C); // SubId
        writeByte(0);
        writeInt(_manorId); // Manor ID
        writeInt(0);
        writeInt(_seeds.size());
        for (SeedProduction seed : _seeds) {
            writeInt(seed.getSeedId()); // Seed id
            writeInt(seed.getAmount()); // Left to buy
            writeInt(seed.getStartAmount()); // Started amount
            writeInt(seed.getPrice()); // Sell Price
            writeInt(L2Manor.getInstance().getSeedLevel(seed.getSeedId())); // Seed Level
            writeByte(1); // reward 1 Type
            writeInt(L2Manor.getInstance().getRewardItemBySeed(seed.getSeedId(), 1)); // Reward 1 Type Item Id
            writeByte(1); // reward 2 Type
            writeInt(L2Manor.getInstance().getRewardItemBySeed(seed.getSeedId(), 2)); // Reward 2 Type Item Id
        }
    }

    @Override
    public String getType() {
        return _S__FE_1C_EXSHOWSEEDINFO;
    }
}
