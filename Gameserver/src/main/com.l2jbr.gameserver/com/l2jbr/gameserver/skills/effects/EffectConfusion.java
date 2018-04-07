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
package com.l2jbr.gameserver.skills.effects;

import com.l2jbr.commons.Config;
import com.l2jbr.commons.util.Rnd;
import com.l2jbr.gameserver.ai.CtrlIntention;
import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.L2Effect;
import com.l2jbr.gameserver.model.L2Object;
import com.l2jbr.gameserver.skills.Env;

import java.util.LinkedList;
import java.util.List;


/**
 * @author littlecrow Implementation of the Confusion Effect
 */
final class EffectConfusion extends L2Effect {

    public EffectConfusion(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public EffectType getEffectType() {
        return EffectType.CONFUSION;
    }

    /**
     * Notify started
     */
    @Override
    public void onStart() {
        getEffected().startConfused();
        onActionTime();
    }

    /**
     * Notify exited
     */
    @Override
    public void onExit() {
        getEffected().stopConfused(this);
    }

    @Override
    public boolean onActionTime() {
        if (Config.DEBUG) {
            System.out.println(getEffected());
        }
        List<L2Character> targetList = new LinkedList<>();

        // Getting the possible targets

        for (L2Object obj : getEffected().getKnownList().getKnownObjects().values()) {
            if (obj == null) {
                continue;
            }

            if ((obj instanceof L2Character) && (obj != getEffected())) {
                targetList.add((L2Character) obj);
            }
        }
        // if there is no target, exit function
        if (targetList.size() == 0) {
            return true;
        }

        // Choosing randomly a new target
        int nextTargetIdx = Rnd.nextInt(targetList.size());
        L2Object target = targetList.get(nextTargetIdx);

        // Attacking the target
        // getEffected().setTarget(target);
        getEffected().setTarget(target);
        getEffected().getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);

        return true;
    }
}
