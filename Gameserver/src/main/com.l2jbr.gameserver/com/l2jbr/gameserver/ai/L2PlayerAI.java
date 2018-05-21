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

import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.L2Object;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance.AIAccessor;
import com.l2jbr.gameserver.model.actor.instance.L2StaticObjectInstance;
import com.l2jbr.gameserver.model.actor.knownlist.ObjectKnownList.KnownListAsynchronousUpdateTask;

import java.util.EmptyStackException;
import java.util.Stack;

import static com.l2jbr.gameserver.ai.Intention.*;

public class L2PlayerAI extends MovableAI<AIAccessor> {
	
	private boolean _thinking;
	
	class IntentionCommand {
		protected Intention intention;
		protected Object _arg0, _arg1;
		
		protected IntentionCommand(Intention pIntention, Object pArg0, Object pArg1) {
			intention = pIntention;
			_arg0 = pArg0;
			_arg1 = pArg1;
		}
	}
	
	private final Stack<IntentionCommand> _interuptedIntentions = new Stack<>();
	
	public L2PlayerAI(AIAccessor accessor)
	{
		super(accessor);
	}

	@Override
	synchronized void changeIntention(Intention intention, Object arg0, Object arg1) {
		if (intention != AI_INTENTION_CAST) {
			super.changeIntention(intention, arg0, arg1);
			return;
		}

		if ((intention == _intention) && (arg0 == _intentionArg0) && (arg1 == _intentionArg1)) {
			super.changeIntention(intention, arg0, arg1);
			return;
		}

		_interuptedIntentions.push(new IntentionCommand(_intention, _intentionArg0, _intentionArg1));
		super.changeIntention(intention, arg0, arg1);
	}

	@Override
	protected void onEvtFinishCasting() {
		// forget interupted actions after offensive skill
		if ((_skill != null) && _skill.isOffensive()) {
			_interuptedIntentions.clear();
		}
		
		if (getIntention() == AI_INTENTION_CAST) {
			if (!_interuptedIntentions.isEmpty()) {
				IntentionCommand cmd = null;
				try {
					cmd = _interuptedIntentions.pop();
				}
				catch (EmptyStackException ese) {

				}
				
				if ((cmd != null) && (cmd.intention != AI_INTENTION_CAST)) {
					setIntention(cmd.intention, cmd._arg0, cmd._arg1);
				}
				else {
					setIntention(AI_INTENTION_IDLE);
				}
			}
			else {
				setIntention(AI_INTENTION_IDLE);
			}
		}
	}
	
	@Override
	protected void onIntentionRest() {
		if (getIntention() != AI_INTENTION_REST) {
			changeIntention(AI_INTENTION_REST, null, null);
			setTarget(null);
			if (getAttackTarget() != null) {
				setAttackTarget(null);
			}
			clientStopMoving(null);
		}
	}
	
	@Override
	protected void onIntentionActive()
	{
		setIntention(AI_INTENTION_IDLE);
	}
	
	@Override
	protected void clientNotifyDead() {
		_clientMovingToPawnOffset = 0;
		_clientMoving = false;
		super.clientNotifyDead();
	}
	
	private void thinkAttack() {
		L2Character target = getAttackTarget();
		if (target == null) {
			return;
		}

		if (checkTargetLostOrDead(target)) {
			setAttackTarget(null);
			return;
		}

        L2PcInstance actor = getActor();
		if (maybeMoveToPawn(target, actor.getPhysicalAttackRange())) {
			return;
		}
		
		getAccessor().doAttack(target);
	}

    private L2PcInstance getActor() {
        return getAccessor().getActor();
    }

    private void thinkCast() {
		
		L2Character target = getCastTarget();
		
		if (checkTargetLost(target)) {
			if (_skill.isOffensive() && (getAttackTarget() != null)) {
				setCastTarget(null);
			}
			return;
		}

		L2PcInstance actor = getActor();
		if (target != null) {
			if (maybeMoveToPawn(target, actor.getMagicalAttackRange(_skill))) {
				return;
			}
		}
		
		if (_skill.getHitTime() > 50) {
			clientStopMoving(null);
		}
		
		L2Object oldTarget = actor.getTarget();
		if (oldTarget != null) {
			if ((target != null) && (oldTarget != target)) {
				actor.setTarget(getCastTarget());
			}

			getAccessor().doCast(_skill);

			if ((target != null) && (oldTarget != target)) {
				actor.setTarget(oldTarget);
			}
		} else {
			getAccessor().doCast(_skill);
		}

	}
	
	private void thinkPickUp() {
	    L2PcInstance actor = getActor();
		if (actor.isAllSkillsDisabled()) {
			return;
		}
		L2Object target = getTarget();

		if (checkTargetLost(target)) {
			return;
		}

		if (maybeMoveToPawn(target, 36)) {
			return;
		}

		setIntention(AI_INTENTION_IDLE);
		getAccessor().doPickupItem(target);
		return;
	}
	
	private void thinkInteract() {
	    L2PcInstance actor = getActor();

		if (actor.isAllSkillsDisabled()) {
			return;
		}

		L2Object target = getTarget();
		if (checkTargetLost(target)) {
			return;
		}

		if (maybeMoveToPawn(target, 36)) {
			return;
		}
		if (!(target instanceof L2StaticObjectInstance)) {
			getAccessor().doInteract((L2Character) target);
		}
		setIntention(AI_INTENTION_IDLE);
		return;
	}
	
	@Override
	protected void onEvtThink() {
		if (_thinking || getActor().isAllSkillsDisabled()) {
			return;
		}
		
		_thinking = true;
		try {
			if (getIntention() == AI_INTENTION_ATTACK) {
				thinkAttack();
			}
			else if (getIntention() == AI_INTENTION_CAST) {
				thinkCast();
			}
			else if (getIntention() == AI_INTENTION_PICK_UP) {
				thinkPickUp();
			}
			else if (getIntention() == AI_INTENTION_INTERACT) {
				thinkInteract();
			}
		}
		finally {
			_thinking = false;
		}
	}
	
	@Override
	protected void onEvtArrivedRevalidate() {
		ThreadPoolManager.getInstance().executeTask(new KnownListAsynchronousUpdateTask(getActor()));
		super.onEvtArrivedRevalidate();
	}
	
}
