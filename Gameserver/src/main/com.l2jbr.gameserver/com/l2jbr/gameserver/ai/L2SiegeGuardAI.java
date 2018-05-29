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

import com.l2jbr.commons.Config;
import com.l2jbr.commons.util.Rnd;
import com.l2jbr.gameserver.GameTimeController;
import com.l2jbr.gameserver.GeoData;
import com.l2jbr.gameserver.model.*;
import com.l2jbr.gameserver.model.actor.instance.*;

import static com.l2jbr.gameserver.ai.Intention.AI_INTENTION_ACTIVE;
import static com.l2jbr.gameserver.ai.Intention.AI_INTENTION_IDLE;

public class L2SiegeGuardAI extends L2AttackableAI<L2SiegeGuardInstance.AIAccessor> implements Runnable {
	private static final int MAX_ATTACK_TIMEOUT = 300;

	private final int _attackRange;

	public L2SiegeGuardAI(L2SiegeGuardInstance.AIAccessor accessor) {
		super(accessor);
		_attackRange = accessor.getActor().getPhysicalAttackRange();
	}
	
	@Override
	public void run() {
		onEvtThink();
		
	}

	private boolean autoAttackCondition(L2Character target) {
		if ((target == null) || (target instanceof L2SiegeGuardInstance) || (target instanceof L2FolkInstance)  || (target instanceof L2DoorInstance)
                || target.isAlikeDead() || target.isInvul()) {
			return false;
		}

        L2SiegeGuardInstance actor = getActor();
		if (target instanceof L2Summon) {
			L2PcInstance owner = ((L2Summon) target).getOwner();
			if (actor.isInsideRadius(owner, 1000, true, false))
			{
				target = owner;
			}
		}

		if (target instanceof L2PcInstance) {
			if (((L2PcInstance) target).isSilentMoving() && !actor.isInsideRadius(target, 250, false, false)) {
				return false;
			}
		}
		return (actor.isAutoAttackable(target) && GeoData.getInstance().canSeeTarget(actor, target));
		
	}

    private L2SiegeGuardInstance getActor() {
        return getAccessor().getActor();
    }

	@Override
	synchronized void changeIntention(Intention intention, Object arg0, Object arg1) {
	    _log.debug("L2SiegeAI.changeIntention( {}, {}, {})", intention , arg0, arg1);
		getActor().setisReturningToSpawnPoint(false);
		super.changeIntention(intention, arg0, arg1);
	}
	
	/**
	 * Manage AI standard thinks of a L2Attackable (called by onEvtThink).<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Update every 1s the _globalAggro counter to come close to 0</li>
     * <li>If the actor is Aggressive and can attack, add all autoAttackable L2Character in its Aggro Range to its _aggroList, chose a target and order to attack it</li>
     * <li>If the actor can't attack, order to it to return to its home location</li>
	 */
	private void thinkActive() {
        L2SiegeGuardInstance actor = getActor();

		if (_globalAggro != 0) {
			if (_globalAggro < 0) {
				_globalAggro++;
			} else {
				_globalAggro--;
			}
		}

		if (_globalAggro >= 0) {
			for (L2Character target : actor.getKnownList().getKnownCharactersInRadius(_attackRange)) {
				if (target == null) {
					continue;
				}
				if (autoAttackCondition(target)) {
					int hating = actor.getHating(target);
					if (hating == 0) {
						actor.addDamageHate(target, 0, 1);
					}
				}
			}

			L2Character hated;
			if (actor.isConfused()) {
				hated = _attackTarget;
			} else {
				hated = actor.getMostHated();
			}

			if (hated != null) {
				int aggro = actor.getHating(hated);
				
				if ((aggro + _globalAggro) > 0) {
					if (!actor.isRunning()) {
						actor.setRunning();
					}

					setIntention(Intention.AI_INTENTION_ATTACK, hated, null);
				}
				
				return;
			}
			
		}
		actor.returnHome();
	}
	
	private void attackPrepare() {
		L2Skill[] skills = null;
		double dist_2 = 0;
		int range = 0;
		L2SiegeGuardInstance actor = getActor();
		
		try {
			actor.setTarget(_attackTarget);
			skills = actor.getAllSkills();
			dist_2 = actor.getPlanDistanceSq(_attackTarget.getX(), _attackTarget.getY());
			range = actor.getPhysicalAttackRange() + (int) actor.getTemplate().getCollisionRadius() + (int) _attackTarget.getTemplate().getCollisionRadius();
		}
		catch (NullPointerException e) {
			_log.warn("AttackableAI: Attack target is NULL.", e);
			actor.setTarget(null);
			setIntention(AI_INTENTION_IDLE, null, null);
			return;
		}

		if ((_attackTarget instanceof L2PcInstance) && actor.getCastle().getSiege().checkIsDefender(((L2PcInstance) _attackTarget).getClan())) {
			actor.stopHating(_attackTarget);
			actor.setTarget(null);
			setIntention(AI_INTENTION_IDLE, null, null);
			return;
		}
		
		if (!GeoData.getInstance().canSeeTarget(actor, _attackTarget)) {
			actor.stopHating(_attackTarget);
			actor.setTarget(null);
			setIntention(AI_INTENTION_IDLE, null, null);
			return;
		}

		if (!actor.isMuted() && (dist_2 > ((range + 20) * (range + 20)))) {
			if (!Config.ALT_GAME_MOB_ATTACK_AI || (Rnd.nextInt(100) <= 5)) {
				for (L2Skill sk : skills) {
					int castRange = sk.getCastRange();
					
					if ((((sk.getSkillType() == L2Skill.SkillType.BUFF) || (sk.getSkillType() == L2Skill.SkillType.HEAL)) ||
                             ((dist_2 >= ((castRange * castRange) / 9)) && (dist_2 <= (castRange * castRange)) && (castRange > 70)))
                            && !actor.isSkillDisabled(sk.getId()) && (actor.getCurrentMp() >= actor.getStat().getMpConsume(sk)) && !sk.isPassive())
					{
						L2Object OldTarget = actor.getTarget();
						if ((sk.getSkillType() == L2Skill.SkillType.BUFF) || (sk.getSkillType() == L2Skill.SkillType.HEAL)) {
							boolean useSkillSelf = true;
							if ((sk.getSkillType() == L2Skill.SkillType.HEAL) && (actor.getCurrentHp() > (int) (actor.getMaxHp() / 1.5))) {
								useSkillSelf = false;
								break;
							}
							if (sk.getSkillType() == L2Skill.SkillType.BUFF) {
								L2Effect[] effects = actor.getAllEffects();
								for (int i = 0; (effects != null) && (i < effects.length); i++) {
									L2Effect effect = effects[i];
									if (effect.getSkill() == sk)  {
										useSkillSelf = false;
										break;
									}
								}
							}
							if (useSkillSelf) {
								actor.setTarget(actor);
							}
						}
						
						clientStopMoving(null);
						getAccessor().doCast(sk);
						actor.setTarget(OldTarget);
						return;
					}
				}
			}

			if (!(actor.isAttackingNow()) && (actor.getRunSpeed() == 0) && (actor.getKnownList().knowsObject(_attackTarget))) {
				actor.getKnownList().removeKnownObject(_attackTarget);
				actor.setTarget(null);
				setIntention(AI_INTENTION_IDLE, null, null);
			} else {
				double dx = actor.getX() - _attackTarget.getX();
				double dy = actor.getY() - _attackTarget.getY();
				double dz = actor.getZ() - _attackTarget.getZ();
				double homeX = _attackTarget.getX() - actor.getHomeX();
				double homeY = _attackTarget.getY() - actor.getHomeY();
				

				if ((((dx * dx) + (dy * dy)) > 10000) && (((homeX * homeX) + (homeY * homeY)) > 3240000)
					&& (actor.getKnownList().knowsObject(_attackTarget))) {
					actor.getKnownList().removeKnownObject(_attackTarget);
					actor.setTarget(null);
					setIntention(AI_INTENTION_IDLE, null, null);
				} else  {
					// Temporary hack for preventing guards jumping off towers,
					// before replacing this with effective geodata checks and AI modification
					if ((dz * dz) < (170 * 170)) {
						moveToPawn(_attackTarget, range);
					}
				}
			}
			return;
			
		} else if (actor.isMuted() && (dist_2 > ((range + 20) * (range + 20)))) {
			// Temporary hack for preventing guards jumping off towers,
			// before replacing this with effective geodata checks and AI modification
			double dz = actor.getZ() - _attackTarget.getZ();
			if ((dz * dz) < (170 * 170)) {
				moveToPawn(_attackTarget, range);
			}
			return;
		} else if (dist_2 <= ((range + 20) * (range + 20))) {
			L2Character hated = null;
			if (actor.isConfused()) {
				hated = _attackTarget;
			} else {
				hated = actor.getMostHated();
			}
			
			if (hated == null) {
				setIntention(AI_INTENTION_ACTIVE, null, null);
				return;
			}
			if (hated != _attackTarget) {
				_attackTarget = hated;
			}
			
			_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getGameTicks();

			if (!actor.isMuted() && (Rnd.nextInt(100) <= 5)) {
				for (L2Skill sk : skills) {
					int castRange = sk.getCastRange();
					
					if (((castRange * castRange) >= dist_2) && (castRange <= 70) && !sk.isPassive() && (actor.getCurrentMp() >= actor.getStat().getMpConsume(sk)) && !actor.isSkillDisabled(sk.getId())) {
						L2Object OldTarget = actor.getTarget();
						if ((sk.getSkillType() == L2Skill.SkillType.BUFF) || (sk.getSkillType() == L2Skill.SkillType.HEAL)) {
							boolean useSkillSelf = true;
							if ((sk.getSkillType() == L2Skill.SkillType.HEAL) && (actor.getCurrentHp() > (int) (actor.getMaxHp() / 1.5))) {
								useSkillSelf = false;
								break;
							}

							if (sk.getSkillType() == L2Skill.SkillType.BUFF) {
								L2Effect[] effects = actor.getAllEffects();
								for (int i = 0; (effects != null) && (i < effects.length); i++) {
									L2Effect effect = effects[i];
									if (effect.getSkill() == sk) {
										useSkillSelf = false;
										break;
									}
								}
							}

							if (useSkillSelf)
							{
								actor.setTarget(actor);
							}
						}
						
						clientStopMoving(null);
						getAccessor().doCast(sk);
						actor.setTarget(OldTarget);
						return;
					}
				}
			}
			getAccessor().doAttack(_attackTarget);
		}
	}
	
	/**
	 * TODO: Manage casting rules to healer mobs (like Ant Nurses)
	 */
	private void thinkAttack() {
        _log.info("L2SiegeGuardAI.thinkAttack(); timeout={}", _attackTimeout - GameTimeController.getGameTicks());

        L2SiegeGuardInstance actor = getActor();
		
		if (_attackTimeout < GameTimeController.getGameTicks()) {
			if (actor.isRunning()) {
				actor.setWalking();
				_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getGameTicks();
			}
		}

		if ((_attackTarget == null) || _attackTarget.isAlikeDead() || (_attackTimeout < GameTimeController.getGameTicks())) {
			if (_attackTarget != null) {
				actor.stopHating(_attackTarget);
			}

			_attackTimeout = Integer.MAX_VALUE;
			_attackTarget = null;

			setIntention(AI_INTENTION_ACTIVE, null, null);
			actor.setWalking();
			return;
		}
		
		attackPrepare();
		factionNotify();
	}
	
	private final void factionNotify() {
        L2SiegeGuardInstance actor = getActor();
		if ((actor == null) || (actor.getFactionId() == null) || (_attackTarget == null)) {
			return;
		}
		
		if (_attackTarget.isInvul()) {
			return;
		}
		
		String faction_id = actor.getFactionId();

		for (L2Character cha : actor.getKnownList().getKnownCharactersInRadius(1000)) {
			if (cha == null) {
				continue;
			}
			
			if (!(cha instanceof L2NpcInstance)) {
				continue;
			}
			
			L2NpcInstance npc = (L2NpcInstance) cha;
			
			if (faction_id != npc.getFactionId()) {
				continue;
			}

			if (((npc.getAI().getIntention() == Intention.AI_INTENTION_IDLE) || (npc.getAI().getIntention() == Intention.AI_INTENTION_ACTIVE))
                    && actor.isInsideRadius(npc, npc.getFactionRange(), false, true) && (npc.getTarget() == null) &&
                    _attackTarget.isInsideRadius(npc, npc.getFactionRange(), false, true)) {

				if (Config.GEODATA > 0) {
					if (GeoData.getInstance().canSeeTarget(npc, _attackTarget)) {
						npc.getAI().notifyEvent(Event.EVT_AGGRESSION, _attackTarget, 1);
					}
				} else {
					if (Math.abs(_attackTarget.getZ() - npc.getZ()) < 600) {
						npc.getAI().notifyEvent(Event.EVT_AGGRESSION, _attackTarget, 1);
					}
				}
			}
		}
	}

	@Override
	protected void onEvtAggression(L2Character target, int aggro) {
        L2SiegeGuardInstance actor = getActor();

	    if (actor == null) {
			return;
		}
		
		if (target != null) {
			actor.addDamageHate(target, 0, aggro);
			aggro = actor.getHating(target);
			
			if (aggro <= 0) {
				if (actor.getMostHated() == null) {
					_globalAggro = -25;
					actor.clearAggroList();
					setIntention(AI_INTENTION_IDLE, null, null);
				}
				return;
			}

			if (getIntention() != Intention.AI_INTENTION_ATTACK) {
				if (!actor.isRunning()) {
					actor.setRunning();
				}

				double homeX = target.getX() - actor.getHomeX();
				double homeY = target.getY() - actor.getHomeY();

				if (((homeX * homeX) + (homeY * homeY)) < 3240000) {
					setIntention(Intention.AI_INTENTION_ATTACK, target, null);
				}
			}
		} else {
			if (aggro >= 0) {
				return;
			}
			
			L2Character mostHated = actor.getMostHated();
			if (mostHated == null) {
				_globalAggro = -25;
				return;
			}
			
			for (L2Character aggroed : actor.getAggroListRP().keySet()) {
				actor.addDamageHate(aggroed, 0, aggro);
			}
			
			aggro = actor.getHating(mostHated);
			if (aggro <= 0) {
				_globalAggro = -25;
				actor.clearAggroList();
				setIntention(AI_INTENTION_IDLE, null, null);
			}
		}
	}
}
