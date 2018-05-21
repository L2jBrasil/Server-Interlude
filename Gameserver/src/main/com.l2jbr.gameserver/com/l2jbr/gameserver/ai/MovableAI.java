package com.l2jbr.gameserver.ai;

import com.l2jbr.gameserver.model.L2Character;

public class MovableAI<T extends L2Character.AIAccessor> extends L2CharacterAI<T> {

    public MovableAI(T accessor) {
        super(accessor);
    }
}
