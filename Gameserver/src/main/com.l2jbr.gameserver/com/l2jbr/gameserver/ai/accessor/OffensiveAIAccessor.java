package com.l2jbr.gameserver.ai.accessor;

import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.L2Skill;

public interface OffensiveAIAccessor extends AIAccessor {

    void doAttack(L2Character target);

    void doCast(L2Skill skill);
}
