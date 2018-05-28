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
package com.l2jbr.gameserver.ai;

import com.l2jbr.commons.util.Rnd;
import com.l2jbr.gameserver.model.*;
import com.l2jbr.gameserver.model.actor.instance.*;
import com.l2jbr.gameserver.model.actor.instance.L2ControllableMobInstance.ControllableAIAcessor;
import com.l2jbr.gameserver.util.Util;

import java.util.LinkedList;
import java.util.List;

import static com.l2jbr.gameserver.ai.Intention.AI_INTENTION_ACTIVE;
import static com.l2jbr.gameserver.ai.Intention.AI_INTENTION_ATTACK;

/**
 * @author littlecrow AI for controllable mobs
 */
public class L2ControllableMobAI extends L2AttackableAI<ControllableAIAcessor> {
    public static final int AI_IDLE = 1;
    public static final int AI_NORMAL = 2;
    public static final int AI_FORCEATTACK = 3;
    public static final int AI_FOLLOW = 4;
    public static final int AI_CAST = 5;
    public static final int AI_ATTACK_GROUP = 6;

    private int _alternateAI;

    private boolean _isNotMoving;

    private L2Character _forcedTarget;
    private MobGroup _targetGroup;

    protected void thinkFollow() {
        L2Attackable me = getActor();

        if (!Util.checkIfInRange(MobGroupTable.FOLLOW_RANGE, me, getForcedTarget(), true)) {
            int signX = (Rnd.nextInt(2) == 0) ? -1 : 1;
            int signY = (Rnd.nextInt(2) == 0) ? -1 : 1;
            int randX = Rnd.nextInt(MobGroupTable.FOLLOW_RANGE);
            int randY = Rnd.nextInt(MobGroupTable.FOLLOW_RANGE);

            moveTo(getForcedTarget().getX() + (signX * randX), getForcedTarget().getY() + (signY * randY), getForcedTarget().getZ());
        }
    }

    private L2ControllableMobInstance getActor() {
        return getAccessor().getActor();
    }

    @Override
    protected void onEvtThink() {
        L2ControllableMobInstance actor = getActor();
        if (_thinking || actor.isAllSkillsDisabled()) {
            return;
        }

        _thinking = true;

        try {
            switch (getAlternateAI()) {
                case AI_IDLE:
                    if (getIntention() != Intention.AI_INTENTION_ACTIVE) {
                        setIntention(Intention.AI_INTENTION_ACTIVE);
                    }
                    break;
                case AI_FOLLOW:
                    thinkFollow();
                    break;
                case AI_CAST:
                    thinkCast();
                    break;
                case AI_FORCEATTACK:
                    thinkForceAttack();
                    break;
                case AI_ATTACK_GROUP:
                    thinkAttackGroup();
                    break;
                default:
                    if (getIntention() == AI_INTENTION_ACTIVE) {
                        thinkActive();
                    } else if (getIntention() == AI_INTENTION_ATTACK) {
                        thinkAttack();
                    }
                    break;
            }
        } finally {
            _thinking = false;
        }
    }

    protected void thinkCast() {
        L2Attackable actor = getActor();

        if ((getAttackTarget() == null) || getAttackTarget().isAlikeDead()) {
            setAttackTarget(findNextRndTarget());
            clientStopMoving(null);
        }

        if (getAttackTarget() == null) {
            return;
        }

        actor.setTarget(getAttackTarget());

        L2Skill[] skills = null;
        // double dist2 = 0;

        try {
            skills = actor.getAllSkills();
            // dist2 = _actor.getPlanDistanceSq(getAttackTarget().getX(), getAttackTarget().getY());
        } catch (NullPointerException e) {
            _log.warn("Encountered Null Value.");
            e.printStackTrace();
        }

        if (!actor.isMuted()) {
            int max_range = 0;
            // check distant skills

            for (L2Skill sk : skills) {
                if (Util.checkIfInRange(sk.getCastRange(), actor, getAttackTarget(), true) && !actor.isSkillDisabled(sk.getId()) && (actor.getCurrentMp() > actor.getStat().getMpConsume(sk))) {
                    getAccessor().doCast(sk);
                    return;
                }

                max_range = Math.max(max_range, sk.getCastRange());
            }

            if (!isNotMoving()) {
                moveToPawn(getAttackTarget(), max_range);
            }
        }
    }

    protected void thinkAttackGroup() {

        L2Character target = getForcedTarget();
        if ((target == null) || target.isAlikeDead()) {
            // try to get next group target
            setForcedTarget(findNextGroupTarget());
            clientStopMoving(null);
        }

        if (target == null) {
            return;
        }

        L2Skill[] skills = null;
        double dist2 = 0;
        int range = 0;
        int max_range = 0;

        L2ControllableMobInstance actor = getActor();

        actor.setTarget(target);
        // as a response, we put the target in a forcedattack mode
        L2ControllableMobInstance theTarget = (L2ControllableMobInstance) target;
        L2ControllableMobAI ctrlAi = (L2ControllableMobAI) theTarget.getAI();
        ctrlAi.forceAttack(actor);

        try {
            skills = actor.getAllSkills();
            dist2 = actor.getPlanDistanceSq(target.getX(), target.getY());
            range = actor.getPhysicalAttackRange() + (int) actor.getTemplate().getCollisionRadius() + (int) target.getTemplate().getCollisionRadius();
            max_range = range;
        } catch (NullPointerException e) {
            _log.warn("Encountered Null Value.");
            e.printStackTrace();
        }

        if (!actor.isMuted() && (dist2 > ((range + 20) * (range + 20)))) {
            // check distant skills
            for (L2Skill sk : skills) {
                int castRange = sk.getCastRange();

                if (((castRange * castRange) >= dist2) && !actor.isSkillDisabled(sk.getId()) && (actor.getCurrentMp() > actor.getStat().getMpConsume(sk))) {
                    getAccessor().doCast(sk);
                    return;
                }

                max_range = Math.max(max_range, castRange);
            }

            if (!isNotMoving()) {
                moveToPawn(target, range);
            }

            return;
        }
        getAccessor().doAttack(target);
    }

    protected void thinkForceAttack() {
        if ((getForcedTarget() == null) || getForcedTarget().isAlikeDead()) {
            clientStopMoving(null);
            setIntention(AI_INTENTION_ACTIVE);
            setAlternateAI(AI_IDLE);
        }

        L2Skill[] skills = null;
        double dist2 = 0;
        int range = 0;
        int max_range = 0;

        L2ControllableMobInstance _actor = getActor();
        try {
            _actor.setTarget(getForcedTarget());
            skills = _actor.getAllSkills();
            dist2 = _actor.getPlanDistanceSq(getForcedTarget().getX(), getForcedTarget().getY());
            range = _actor.getPhysicalAttackRange() + (int) _actor.getTemplate().getCollisionRadius() + (int) getForcedTarget().getTemplate().getCollisionRadius();
            max_range = range;
        } catch (NullPointerException e) {
            _log.warn("Encountered Null Value.");
            e.printStackTrace();
        }

        if (!_actor.isMuted() && (dist2 > ((range + 20) * (range + 20)))) {
            // check distant skills
            for (L2Skill sk : skills) {
                int castRange = sk.getCastRange();

                if (((castRange * castRange) >= dist2) && !_actor.isSkillDisabled(sk.getId()) && (_actor.getCurrentMp() > _actor.getStat().getMpConsume(sk))) {
                    getAccessor().doCast(sk);
                    return;
                }

                max_range = Math.max(max_range, castRange);
            }

            if (!isNotMoving()) {
                moveToPawn(getForcedTarget(), _actor.getPhysicalAttackRange()/* range */);
            }

            return;
        }

        getAccessor().doAttack(getForcedTarget());
    }

    protected void thinkAttack() {
        L2Attackable actor = getActor();
        if ((getAttackTarget() == null) || getAttackTarget().isAlikeDead()) {
            if (getAttackTarget() != null) {
                // stop hating
                actor.stopHating(getAttackTarget());
            }

            setIntention(AI_INTENTION_ACTIVE);
        } else {
            // notify aggression
            if (((L2NpcInstance) actor).getFactionId() != null) {
                String faction_id = ((L2NpcInstance) actor).getFactionId();

                for (L2Object obj : actor.getKnownList().getKnownObjects().values()) {
                    if (!(obj instanceof L2NpcInstance)) {
                        continue;
                    }

                    L2NpcInstance npc = (L2NpcInstance) obj;

                    if (faction_id != npc.getFactionId()) {
                        continue;
                    }

                    if (actor.isInsideRadius(npc, npc.getFactionRange(), false, true) && (Math.abs(getAttackTarget().getZ() - npc.getZ()) < 200)) {
                        npc.getAI().notifyEvent(Event.EVT_AGGRESSION, getAttackTarget(), 1);
                    }
                }
            }

            L2Skill[] skills = null;
            double dist2 = 0;
            int range = 0;
            int max_range = 0;

            try {
                actor.setTarget(getAttackTarget());
                skills = actor.getAllSkills();
                dist2 = actor.getPlanDistanceSq(getAttackTarget().getX(), getAttackTarget().getY());
                range = actor.getPhysicalAttackRange() + (int) actor.getTemplate().getCollisionRadius() + (int) getAttackTarget().getTemplate().getCollisionRadius();
                max_range = range;
            } catch (NullPointerException e) {
                _log.warn("Encountered Null Value.");
                e.printStackTrace();
            }

            if (!actor.isMuted() && (dist2 > ((range + 20) * (range + 20)))) {
                // check distant skills
                for (L2Skill sk : skills) {
                    int castRange = sk.getCastRange();

                    if (((castRange * castRange) >= dist2) && !actor.isSkillDisabled(sk.getId()) && (actor.getCurrentMp() > actor.getStat().getMpConsume(sk))) {
                        getAccessor().doCast(sk);
                        return;
                    }

                    max_range = Math.max(max_range, castRange);
                }

                moveToPawn(getAttackTarget(), range);
                return;
            }

            // Force mobs to attack anybody if confused.
            L2Character hated;

            if (actor.isConfused()) {
                hated = findNextRndTarget();
            } else {
                hated = getAttackTarget();
            }

            if (hated == null) {
                setIntention(AI_INTENTION_ACTIVE);
                return;
            }

            if (hated != getAttackTarget()) {
                setAttackTarget(hated);
            }

            if (!actor.isMuted() && (skills.length > 0) && (Rnd.nextInt(5) == 3)) {
                for (L2Skill sk : skills) {
                    int castRange = sk.getCastRange();

                    if (((castRange * castRange) >= dist2) && !actor.isSkillDisabled(sk.getId()) && (actor.getCurrentMp() < actor.getStat().getMpConsume(sk))) {
                        getAccessor().doCast(sk);
                        return;
                    }
                }
            }

            getAccessor().doAttack(getAttackTarget());
        }
    }

    private void thinkActive() {
        setAttackTarget(findNextRndTarget());
        L2Character hated;
        L2ControllableMobInstance actor = getActor();

        if (actor.isConfused()) {
            hated = findNextRndTarget();
        } else {
            hated = getAttackTarget();
        }

        if (hated != null) {
            actor.setRunning();
            setIntention(Intention.AI_INTENTION_ATTACK, hated);
        }

        return;
    }

    private boolean autoAttackCondition(L2Character target) {
        L2ControllableMobInstance actor = getActor();
        if ((target == null)) {
            return false;
        }

        if ((target instanceof L2FolkInstance) || (target instanceof L2DoorInstance)) {
            return false;
        }

        if (target.isAlikeDead() || !actor.isInsideRadius(target, actor.getAggroRange(), false, false) || (Math.abs(actor.getZ() - target.getZ()) > 100)) {
            return false;
        }

        // Check if the target isn't invulnerable
        if (target.isInvul()) {
            return false;
        }

        // Check if the target is a L2PcInstance
        if (target instanceof L2PcInstance) {

            // Check if the target isn't in silent move mode
            if (((L2PcInstance) target).isSilentMoving()) {
                return false;
            }
        }

        if (target instanceof L2NpcInstance) {
            return false;
        }

        return actor.isAggressive();
    }

    private L2Character findNextRndTarget() {
        L2ControllableMobInstance actor = getActor();
        int aggroRange = actor.getAggroRange();

        int npcX, npcY, targetX, targetY;
        double dy, dx;
        double dblAggroRange = aggroRange * aggroRange;

        List<L2Character> potentialTarget = new LinkedList<>();

        for (L2Object obj : actor.getKnownList().getKnownObjects().values()) {
            if (!(obj instanceof L2Character)) {
                continue;
            }

            npcX = actor.getX();
            npcY = actor.getY();
            targetX = obj.getX();
            targetY = obj.getY();

            dx = npcX - targetX;
            dy = npcY - targetY;

            if (((dx * dx) + (dy * dy)) > dblAggroRange) {
                continue;
            }

            L2Character target = (L2Character) obj;

            if (autoAttackCondition(target)) {
                potentialTarget.add(target);
            }
        }

        if (potentialTarget.size() == 0) {
            return null;
        }

        // we choose a random target
        int choice = Rnd.nextInt(potentialTarget.size());
        L2Character target = potentialTarget.get(choice);

        return target;
    }

    private L2ControllableMobInstance findNextGroupTarget() {
        return getGroupTarget().getRandomMob();
    }

    public L2ControllableMobAI(ControllableAIAcessor accessor) {
        super(accessor);
        setAlternateAI(AI_IDLE);
    }

    public int getAlternateAI() {
        return _alternateAI;
    }

    public void setAlternateAI(int _alternateai) {
        _alternateAI = _alternateai;
    }

    public void forceAttack(L2Character target) {
        setAlternateAI(AI_FORCEATTACK);
        setForcedTarget(target);
    }

    public void forceAttackGroup(MobGroup group) {
        setForcedTarget(null);
        setGroupTarget(group);
        setAlternateAI(AI_ATTACK_GROUP);
    }

    public void stop() {
        setAlternateAI(AI_IDLE);
        clientStopMoving(null);
    }

    public void move(int x, int y, int z) {
        moveTo(x, y, z);
    }

    public void follow(L2Character target) {
        setAlternateAI(AI_FOLLOW);
        setForcedTarget(target);
    }


    public boolean isNotMoving() {
        return _isNotMoving;
    }

    public void setNotMoving(boolean isNotMoving) {
        _isNotMoving = isNotMoving;
    }

    private L2Character getForcedTarget() {
        return _forcedTarget;
    }

    private MobGroup getGroupTarget() {
        return _targetGroup;
    }

    private void setForcedTarget(L2Character forcedTarget) {
        _forcedTarget = forcedTarget;
    }

    private void setGroupTarget(MobGroup targetGroup) {
        _targetGroup = targetGroup;
    }
}
