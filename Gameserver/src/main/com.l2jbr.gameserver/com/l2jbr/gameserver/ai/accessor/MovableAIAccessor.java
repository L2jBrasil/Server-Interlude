package com.l2jbr.gameserver.ai.accessor;

import com.l2jbr.gameserver.model.L2Position;

public interface MovableAIAccessor extends AIAccessor {

    void moveTo(int x, int y, int z, int offset);

    void moveTo(int x, int y, int z);

    void stopMove(L2Position pos);

}
