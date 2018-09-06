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
import com.l2jbr.gameserver.model.entity.database.CropProcure;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * Format: ch cddd[ddddcdcdcd] c - id (0xFE) h - sub id (0x1D) c d - manor id d d - size [ d - crop id d - residual buy d - start buy d - buy price c - reward type d - seed level c - reward 1 items d - reward 1 item id c - reward 2 items d - reward 2 item id ]
 *
 * @author l3x
 */

public class ExShowCropInfo extends L2GameServerPacket {
    private static final String _S__FE_1C_EXSHOWSEEDINFO = "[S] FE:1D ExShowCropInfo";
    private List<CropProcure> _crops;
    private final int _manorId;

    public ExShowCropInfo(int manorId, List<CropProcure> crops) {
        _manorId = manorId;
        _crops = crops;
        if (isNull(_crops)) {
            _crops = new ArrayList<>();
        }
    }

    @Override
    protected void writeImpl() {
        writeByte(0xFE); // Id
        writeShort(0x1D); // SubId
        writeByte(0);
        writeInt(_manorId); // Manor ID
        writeInt(0);
        writeInt(_crops.size());
        for (CropProcure crop : _crops) {
            writeInt(crop.getCropId()); // Crop id
            writeInt(crop.getAmount()); // Buy residual
            writeInt(crop.getStartAmount()); // Buy
            writeInt(crop.getPrice()); // Buy price
            writeByte(crop.getReward()); // Reward
            writeInt(L2Manor.getInstance().getSeedLevelByCrop(crop.getCropId())); // Seed Level
            writeByte(1); // rewrad 1 Type
            writeInt(L2Manor.getInstance().getRewardItem(crop.getCropId(), 1)); // Rewrad 1 Type Item Id
            writeByte(1); // rewrad 2 Type
            writeInt(L2Manor.getInstance().getRewardItem(crop.getCropId(), 2)); // Rewrad 2 Type Item Id
        }
    }

    @Override
    public String getType() {
        return _S__FE_1C_EXSHOWSEEDINFO;
    }

}
