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
import com.l2jbr.gameserver.GameTimeController;
import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.Universe;
import com.l2jbr.gameserver.model.*;
import com.l2jbr.gameserver.model.actor.instance.*;
import com.l2jbr.gameserver.serverpackets.*;
import com.l2jbr.gameserver.taskmanager.AttackStanceTaskManager;

import java.util.concurrent.Future;

import static com.l2jbr.gameserver.ai.Intention.*;

/**
 * This class manages AI of L2Character.<BR>
 */
public class L2CharacterAI<T extends L2Character.AIAccessor> extends AbstractAI<T> {

    private static final int FOLLOW_INTERVAL = 1000;
    private static final int ATTACK_FOLLOW_INTERVAL = 500;

    protected boolean _clientMoving;
    protected boolean _clientAutoAttacking;
    protected int _clientMovingToPawnOffset;

    private L2Object _target;
    private L2Character _castTarget;
    protected L2Character _attackTarget;
    protected L2Character _followTarget;

    L2Skill _skill;

    private int _moveToPawnTimeout;

    protected Future<?> _followTask = null;


	public L2CharacterAI(T accessor)
	{
		super(accessor);
	}

	@Override
    protected void onEvtAttacked(L2Character attacker) {
        clientStartAutoAttack();
    }

	@Override
	protected void onIntentionIdle() {
		changeIntention(AI_INTENTION_IDLE, null, null);

		setCastTarget(null);
		setAttackTarget(null);
		clientStopMoving(null);
		clientStopAutoAttack();
		
	}
	
	@Override
	protected void onIntentionActive()  {
		if (getIntention() != AI_INTENTION_ACTIVE)  {
			changeIntention(AI_INTENTION_ACTIVE, null, null);

			setCastTarget(null);
			setAttackTarget(null);

			clientStopMoving(null);

			clientStopAutoAttack();

            L2Character actor = getActor();
			if (actor instanceof L2Attackable) {
				((L2NpcInstance) actor).startRandomAnimationTimer();
			}
			onEvtThink();
		}
	}
	

	@Override
	protected void onIntentionRest() {
		setIntention(AI_INTENTION_IDLE);
	}
	

	@Override
	protected void onIntentionAttack(L2Character target) {
		if (target == null) {
			clientActionFailed();
			return;
		}
		
		if (getIntention() == AI_INTENTION_REST) {
			clientActionFailed();
			return;
		}

        L2Character actor = getActor();
		
		if (actor.isAllSkillsDisabled() || actor.isAfraid()) {
			clientActionFailed();
			return;
		}

		if (getIntention() == AI_INTENTION_ATTACK) {
			if (getAttackTarget() != target) {
				setAttackTarget(target);
				stopFollow();
				notifyEvent(Event.EVT_THINK, null);
			} else  {
				clientActionFailed();
			}
		}  else  {
			changeIntention(AI_INTENTION_ATTACK, target, null);
			setAttackTarget(target);
			stopFollow();
			notifyEvent(Event.EVT_THINK, null);
		}
	}

    private L2Character getActor() {
        return getAccessor().getActor();
    }

    @Override
	protected void onIntentionCast(L2Skill skill, L2Object target) {
		if ((getIntention() == AI_INTENTION_REST) && skill.isMagic()) {
			clientActionFailed();
			return;
		}

        L2Character actor = getActor();
		if (actor.isAllSkillsDisabled()) {
			clientActionFailed();
			return;
		}

		if (actor.isMuted() && skill.isMagic()) {
			clientActionFailed();
			return;
		}
		

		setCastTarget((L2Character) target);

		if (skill.getHitTime() > 50) {
			actor.abortAttack();
		}

		_skill = skill;

		changeIntention(AI_INTENTION_CAST, skill, target);
		notifyEvent(Event.EVT_THINK, null);
	}

	@Override
	protected void onIntentionMoveTo(L2Position pos) {
		if (getIntention() == AI_INTENTION_REST) {
			clientActionFailed();
			return;
		}

        L2Character actor = getActor();
		
		if (actor.isAllSkillsDisabled()) {
			clientActionFailed();
			return;
		}

		changeIntention(AI_INTENTION_MOVE_TO, pos, null);

		clientStopAutoAttack();

		actor.abortAttack();

		moveTo(pos.x, pos.y, pos.z);
	}
	

	@Override
	protected void onIntentionMoveToInABoat(L2Position destination, L2Position origin) {
		if (getIntention() == AI_INTENTION_REST) {
			clientActionFailed();
			return;
		}

        L2Character actor = getActor();
		
		if (actor.isAllSkillsDisabled()) {
			clientActionFailed();
			return;
		}
		

		clientStopAutoAttack();

		actor.abortAttack();

		moveToInABoat(destination, origin);
	}
	

	@Override
	protected void onIntentionFollow(L2Character target) {
		if (getIntention() == AI_INTENTION_REST) {
			clientActionFailed();
			return;
		}

        L2Character actor = getActor();
		
		if (actor.isAllSkillsDisabled()) {
			clientActionFailed();
			return;
		}
		
		if (actor.isImobilised() || actor.isRooted()) {
			clientActionFailed();
			return;
		}

		if (actor.isDead()) {
			clientActionFailed();
			return;
		}

		if (actor == target) {
			clientActionFailed();
			return;
		}

		clientStopAutoAttack();

		changeIntention(AI_INTENTION_FOLLOW, target, null);

		startFollow(target);
	}
	

	@Override
	protected void onIntentionPickUp(L2Object object) {
		if (getIntention() == AI_INTENTION_REST) {
			clientActionFailed();
			return;
		}

        L2Character actor = getActor();
		
		if (actor.isAllSkillsDisabled()) {
			clientActionFailed();
			return;
		}

		clientStopAutoAttack();

		changeIntention(AI_INTENTION_PICK_UP, object, null);

		setTarget(object);
		if ((object.getX() == 0) && (object.getY() == 0)) // TODO: Find the drop&spawn bug
		{
			_log.warn("Object in coords 0,0 - using a temporary fix");
			object.setPosition(actor.getX(), actor.getY(), actor.getZ() + 5);
		}

		moveToPawn(object, 20);
	}

	@Override
	protected void onIntentionInteract(L2Object object) {
		if (getIntention() == AI_INTENTION_REST) {
			clientActionFailed();
			return;
		}

        L2Character actor = getActor();
		
		if (actor.isAllSkillsDisabled()) {
			clientActionFailed();
			return;
		}

		clientStopAutoAttack();
		
		if (getIntention() != AI_INTENTION_INTERACT) {
			changeIntention(AI_INTENTION_INTERACT, object, null);
			setTarget(object);
			moveToPawn(object, 60);
		}
	}

	@Override
	protected void onEvtThink() {
		// do nothing
	}

	@Override
	protected void onEvtAggression(L2Character target, int aggro) {
		// do nothing
	}

	@Override
	protected void onEvtStunned(L2Character attacker) {
        L2Character actor = getActor();
		actor.broadcastPacket(new AutoAttackStop(actor.getObjectId()));
		if (AttackStanceTaskManager.getInstance().getAttackStanceTask(actor)) {
			AttackStanceTaskManager.getInstance().removeAttackStanceTask(actor);
		}

		setAutoAttacking(false);

		clientStopMoving(null);

		onEvtAttacked(attacker);
	}

	@Override
	protected void onEvtSleeping(L2Character attacker) {
	    L2Character actor = getActor();
		actor.broadcastPacket(new AutoAttackStop(actor.getObjectId()));
		if (AttackStanceTaskManager.getInstance().getAttackStanceTask(actor)) {
			AttackStanceTaskManager.getInstance().removeAttackStanceTask(actor);
		}

		setAutoAttacking(false);

		clientStopMoving(null);
	}

	@Override
	protected void onEvtRooted(L2Character attacker) {
		clientStopMoving(null);
		onEvtAttacked(attacker);
	}

	@Override
	protected void onEvtConfused(L2Character attacker) {
		clientStopMoving(null);
		onEvtAttacked(attacker);
	}

	@Override
	protected void onEvtMuted(L2Character attacker) {
		//TODO  Break a cast and send Server->Client ActionFailed packet and a System Message to the L2Character
		onEvtAttacked(attacker);
	}

	@Override
	protected void onEvtReadyToAct() {
		onEvtThink();
	}

	@Override
	protected void onEvtUserCmd(Object arg0, Object arg1) {
		// do nothing
	}

	@Override
	protected void onEvtArrived() {
        L2Character actor = getActor();
		if (actor instanceof L2PcInstance) { // TODO move this to PlayerAI
			if (Config.ACTIVATE_POSITION_RECORDER) {
				((L2PcInstance) actor).explore();
			}
			((L2PcInstance) actor).revalidateZone(true);
		} else  {
			actor.revalidateZone();
		}
		
		if (actor.moveToNextRoutePoint()) {
			return;
		}
		
		clientStoppedMoving();

		if (getIntention() == AI_INTENTION_MOVE_TO) {
			setIntention(AI_INTENTION_ACTIVE);
		}

		onEvtThink();
		
		if (actor instanceof L2BoatInstance) {
			((L2BoatInstance) actor).evtArrived();
		}
	}

	@Override
	protected void onEvtArrivedRevalidate() {
		onEvtThink();
	}

	@Override
	protected void onEvtArrivedBlocked(L2Position blocked_at_pos) {
		clientStopMoving(blocked_at_pos);

        L2Character actor = getActor();
		if (Config.ACTIVATE_POSITION_RECORDER && Universe.getInstance().shouldLog(actor.getObjectId())) {
			if (!actor.isFlying()) {
				Universe.getInstance().registerObstacle(blocked_at_pos.x, blocked_at_pos.y, blocked_at_pos.z);
			}

			if (actor instanceof L2PcInstance) { // TODO move this to PlayerAI
				((L2PcInstance) actor).explore();
			}
		}

		if (getIntention() == AI_INTENTION_MOVE_TO) {
			setIntention(AI_INTENTION_ACTIVE);
		}

		onEvtThink();
	}
	

	@Override
	protected void onEvtForgetObject(L2Object object) {
		if (getTarget() == object) {
			setTarget(null);
			
			if (getIntention() == AI_INTENTION_INTERACT) {
				setIntention(AI_INTENTION_ACTIVE);
			} else if (getIntention() == AI_INTENTION_PICK_UP) {
				setIntention(AI_INTENTION_ACTIVE);
			}
		}

		if (getAttackTarget() == object) {
			setAttackTarget(null);
			setIntention(AI_INTENTION_ACTIVE);
		}

		if (getCastTarget() == object) {
			setCastTarget(null);
			setIntention(AI_INTENTION_ACTIVE);
		}

		if (getFollowTarget() == object) {
			clientStopMoving(null);
			stopFollow();
			setIntention(AI_INTENTION_ACTIVE);
		}

        L2Character actor = getActor();
		if (actor == object) {
			setTarget(null);
			setAttackTarget(null);
			setCastTarget(null);

			stopFollow();

			clientStopMoving(null);

			changeIntention(AI_INTENTION_IDLE, null, null);
		}
	}
	

	@Override
	protected void onEvtCancel() {
		stopFollow();

        L2Character actor = getActor();
		if (!AttackStanceTaskManager.getInstance().getAttackStanceTask(actor)) {
			actor.broadcastPacket(new AutoAttackStop(actor.getObjectId()));
		}

		onEvtThink();
	}

	@Override
	protected void onEvtDead() {
		stopFollow();

		clientNotifyDead();

        L2Character actor = getActor();
		if (!(actor instanceof L2PcInstance)) {  // TODO move this to down in the hierarchy
			actor.setWalking();
		}
	}

	@Override
	protected void onEvtFakeDeath() {
		stopFollow();

		clientStopMoving(null);

		_intention = AI_INTENTION_IDLE;
		setTarget(null);
		setCastTarget(null);
		setAttackTarget(null);
	}
	

	@Override
	protected void onEvtFinishCasting() {
		// do nothing
	}

	protected boolean maybeMoveToPawn(L2Object target, int offset) {
		if (target == null) {
			_log.warn("maybeMoveToPawn: target == NULL!");
			return false;
		}

		if (offset < 0) {
			return false;
		}

		L2Character actor = getActor();

		offset += actor.getTemplate().getCollisionRadius();

		if (target instanceof L2Character) {
			offset += ((L2Character) target).getTemplate().getCollisionRadius();
		}
		
		if (!actor.isInsideRadius(target, offset, false, false)) {
			if (getFollowTarget() != null) {
				if ((getAttackTarget() != null) && (actor instanceof L2PlayableInstance) && (target instanceof L2PlayableInstance)) {
					if (getAttackTarget() == getFollowTarget()) {
						// TODO move this to PlayerAI allow GMs to keep following
						boolean isGM = actor instanceof L2PcInstance && ((L2PcInstance) actor).isGM();
						if (actor.isInsidePeaceZone(actor, target) && !isGM) {
							stopFollow();
							setIntention(AI_INTENTION_IDLE);
							return true;
						}
					}
				}

				if (!actor.isInsideRadius(target, 2000, false, false)) {
					stopFollow();
					setIntention(AI_INTENTION_IDLE);
					return true;
				}

				if (!actor.isInsideRadius(target, offset + 100, false, false)) {
					return true;
				}
				stopFollow();
				return false;
			}
			
			if (actor.isMovementDisabled()) {
				return true;
			}

			//TODO move this to down on hierarchy
			if (!actor.isRunning() && !(this instanceof L2PlayerAI)) {
				actor.setRunning();
			}
			
			stopFollow();
			if ((target instanceof L2Character) && !(target instanceof L2DoorInstance)) {
				if (((L2Character) target).isMoving()) {
					offset -= 100;
				}
				if (offset < 5) {
					offset = 5;
				}
				
				startFollow((L2Character) target, offset);
			}
			else {
				moveToPawn(target, offset);
			}
			return true;
		}
		
		if (getFollowTarget() != null) {
			stopFollow();
		}

		return false;
	}
	

	protected boolean checkTargetLostOrDead(L2Character target) {
		if ((target == null) || target.isAlikeDead()) {
			if ((target != null) && target.isFakeDeath()) {
				target.stopFakeDeath(null);
				return false;
			}

			setIntention(AI_INTENTION_ACTIVE);
			return true;
		}
		return false;
	}
	

	protected boolean checkTargetLost(L2Object target) {
		if (target instanceof L2PcInstance) {
			L2PcInstance target2 = (L2PcInstance) target;
			
			if (target2.isFakeDeath()) {
				target2.stopFakeDeath(null);
				return false;
			}
		}
		if (target == null) {
			setIntention(AI_INTENTION_ACTIVE);
			return true;
		}
		return false;
	}


    // TODO move this to movableAI
    class FollowTask implements Runnable {
        protected int _range = 60;

        public FollowTask() { }

        public FollowTask(int range)
        {
            _range = range;
        }

        @Override
        public void run() {
            try {
                if (_followTask == null) {
                    return;
                }

                if (_followTarget == null) {
                    stopFollow();
                    return;
                }
                if (!getActor().isInsideRadius(_followTarget, _range, true, false)) {
                    moveToPawn(_followTarget, _range);
                }
            }
            catch (Throwable t) {
                _log.warn( "", t);
            }
        }
    }


    public synchronized void startFollow(L2Character target) {
        if (_followTask != null) {
            _followTask.cancel(false);
            _followTask = null;
        }

        _followTarget = target;
        _followTask = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new FollowTask(), 5, FOLLOW_INTERVAL);
    }


    protected void clientNotifyDead() {
        L2Character actor = getActor();
        Die msg = new Die(actor);
        actor.broadcastPacket(msg);

        // Init AI
        _intention = AI_INTENTION_IDLE;
        _target = null;
        _castTarget = null;
        _attackTarget = null;

        stopFollow();
    }

    public void describeStateToPlayer(L2PcInstance player) {
        if (_clientMoving) {

            L2Character actor = getActor();
            if ((_clientMovingToPawnOffset != 0) && (_followTarget != null)) {
                MoveToPawn msg = new MoveToPawn(actor, _followTarget, _clientMovingToPawnOffset);
                player.sendPacket(msg);
            } else  {
                CharMoveToLocation msg = new CharMoveToLocation(actor);
                player.sendPacket(msg);
            }
        }
    }

    public synchronized void startFollow(L2Character target, int range) {
        if (_followTask != null) {
            _followTask.cancel(false);
            _followTask = null;
        }

        _followTarget = target;
        _followTask = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new FollowTask(range), 5, ATTACK_FOLLOW_INTERVAL);
    }

    public synchronized void stopFollow() {
        if (_followTask != null) {
            _followTask.cancel(false);
            _followTask = null;
        }
        _followTarget = null;
    }

	protected L2Character getFollowTarget()
    {
        return _followTarget;
    }


    protected void clientActionFailed() {
        // TODO move to PlayerAI
        L2Character actor = getActor();
        if (actor instanceof L2PcInstance) {
            actor.sendPacket(new ActionFailed());
        }
    }

    protected void moveToPawn(L2Object pawn, int offset) {
        L2Character actor = getActor();
        if (!actor.isMovementDisabled()) {
            if (offset < 10) {
                offset = 10;
            }

            // prevent possible extra calls to this function (there is none?),
            // also don't send movetopawn packets too often
            boolean sendPacket = true;
            if (_clientMoving && (_target == pawn)) {
                if (_clientMovingToPawnOffset == offset) {
                    if (GameTimeController.getGameTicks() < _moveToPawnTimeout) {
                        return;
                    }
                    sendPacket = false;
                } else if (actor.isOnGeodataPath()) {
                    // minimum time to calculate new route is 2 seconds
                    if (GameTimeController.getGameTicks() < (_moveToPawnTimeout + 10)) {
                        return;
                    }
                }
            }

            _clientMoving = true;
            _clientMovingToPawnOffset = offset;
            _target = pawn;
            _moveToPawnTimeout = GameTimeController.getGameTicks();
            _moveToPawnTimeout += 1000 / GameTimeController.MILLIS_IN_TICK;

            if ((pawn == null) || (getAccessor() == null)) {
                return;
            }

            getAccessor().moveTo(pawn.getX(), pawn.getY(), pawn.getZ(), offset);

            if (!actor.isMoving()) {
                actor.sendPacket(new ActionFailed());
                return;
            }

            if (pawn instanceof L2Character) {
                if (actor.isOnGeodataPath()) {
                    actor.broadcastPacket(new CharMoveToLocation(actor));
                    _clientMovingToPawnOffset = 0;
                } else if (sendPacket) {
                    actor.broadcastPacket(new MoveToPawn(actor, (L2Character) pawn, offset));
                }
            } else  {
                actor.broadcastPacket(new CharMoveToLocation(actor));
            }
        } else {
            actor.sendPacket(new ActionFailed());
        }
    }


    protected void moveTo(int x, int y, int z) {

	    L2Character actor = getActor();
        if (!actor.isMovementDisabled()) {
            _clientMoving = true;
            _clientMovingToPawnOffset = 0;

            getAccessor().moveTo(x, y, z);

            CharMoveToLocation msg = new CharMoveToLocation(actor);
            actor.broadcastPacket(msg);
        }  else  {
            actor.sendPacket(new ActionFailed());
        }
    }

    protected void moveToInABoat(L2Position destination, L2Position origin) {
	    L2Character actor = getActor();
        if (!actor.isMovementDisabled()) {
            //TODO move this to PlayerAI
            if (((L2PcInstance) actor).getBoat() != null) {
                MoveToLocationInVehicle msg = new MoveToLocationInVehicle(actor, destination, origin);
                actor.broadcastPacket(msg);
            }
        } else {
            actor.sendPacket(new ActionFailed());
        }
    }

    protected void clientStopMoving(L2Position pos) {
	    L2Character actor = getActor();
        if (actor.isMoving()) {
            getAccessor().stopMove(pos);
        }

        _clientMovingToPawnOffset = 0;

        if (_clientMoving || (pos != null)) {
            _clientMoving = false;

            StopMove msg = new StopMove(actor);
            actor.broadcastPacket(msg);

            if (pos != null) {
                StopRotation sr = new StopRotation(actor, pos.heading);
                actor.sendPacket(sr);
                actor.broadcastPacket(sr);
            }
        }
    }

    protected void clientStoppedMoving() {
        if (_clientMovingToPawnOffset > 0) {
            _clientMovingToPawnOffset = 0;
            L2Character actor = getActor();
            StopMove msg = new StopMove(actor);
            actor.broadcastPacket(msg);
        }
        _clientMoving = false;
    }

    public boolean isAutoAttacking()
    {
        return _clientAutoAttacking;
    }

    public void setAutoAttacking(boolean isAutoAttacking)
    {
        _clientAutoAttacking = isAutoAttacking;
    }

    public void clientStartAutoAttack() {
        L2Character actor = getActor();
        if (!isAutoAttacking()) {
            actor.broadcastPacket(new AutoAttackStart(actor.getObjectId()));
            setAutoAttacking(true);
        }
        AttackStanceTaskManager.getInstance().addAttackStanceTask(actor);
    }

    public void clientStopAutoAttack() {
	    L2Character actor = getActor();
        if (actor instanceof L2PcInstance) {
            if (!AttackStanceTaskManager.getInstance().getAttackStanceTask(actor) && isAutoAttacking()) {
                AttackStanceTaskManager.getInstance().addAttackStanceTask(actor);
            }
        } else if (isAutoAttacking()) {
            actor.broadcastPacket(new AutoAttackStop(actor.getObjectId()));
        }
        setAutoAttacking(false);
    }

    protected L2Object getTarget()
    {
        return _target;
    }

    protected synchronized void setTarget(L2Object target)
    {
        _target = target;
    }

    protected synchronized void setCastTarget(L2Character target)
    {
        _castTarget = target;
    }

    public L2Character getCastTarget()
    {
        return _castTarget;
    }

    protected synchronized void setAttackTarget(L2Character target)
    {
        _attackTarget = target;
    }

    @Override
    public L2Character getAttackTarget()
    {
        return _attackTarget;
    }

}
