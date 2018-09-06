package com.l2jbr.gameserver.serverpackets;

import com.l2jbr.gameserver.instancemanager.CastleManager;
import com.l2jbr.gameserver.instancemanager.CastleManorManager;
import com.l2jbr.gameserver.model.entity.Castle;
import com.l2jbr.gameserver.model.entity.database.CropProcure;

import java.util.HashMap;
import java.util.Map;

/**
 * format(packet 0xFE) ch dd [dddc] c - id h - sub id d - crop id d - size [ d - manor name d - buy residual d - buy price c - reward type ]
 *
 * @author l3x
 */
public class ExShowProcureCropDetail extends L2GameServerPacket {
    private static final String _S__FE_22_EXSHOWPROCURECROPDETAIL = "[S] FE:22 ExShowProcureCropDetail";

    private final int _cropId;
    private final Map<Integer, CropProcure> _castleCrops;

    public ExShowProcureCropDetail(int cropId) {
        _cropId = cropId;
        _castleCrops = new HashMap<>();

        for (Castle c : CastleManager.getInstance().getCastles()) {
            CropProcure cropItem = c.getCrop(_cropId, CastleManorManager.PERIOD_CURRENT);
            if ((cropItem != null) && (cropItem.getAmount() > 0)) {
                _castleCrops.put(c.getCastleId(), cropItem);
            }
        }
    }

    @Override
    public void runImpl() {
    }

    @Override
    public void writeImpl() {
        writeByte(0xFE);
        writeShort(0x22);

        writeInt(_cropId); // crop id
        writeInt(_castleCrops.size()); // size

        for (int manorId : _castleCrops.keySet()) {
            CropProcure crop = _castleCrops.get(manorId);
            writeInt(manorId); // manor name
            writeInt(crop.getAmount()); // buy residual
            writeInt(crop.getPrice()); // buy price
            writeByte(crop.getReward()); // reward type
        }
    }

    @Override
    public String getType() {
        return _S__FE_22_EXSHOWPROCURECROPDETAIL;
    }

}
