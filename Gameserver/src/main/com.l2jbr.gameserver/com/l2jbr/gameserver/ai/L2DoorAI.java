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
import com.l2jbr.gameserver.model.L2Position;
import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.L2Object;
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.model.actor.instance.L2DoorInstance;
import com.l2jbr.gameserver.model.actor.instance.L2DoorInstance.AIAccessor;
import com.l2jbr.gameserver.model.actor.instance.L2SiegeGuardInstance;

public class L2DoorAI extends L2CharacterAI<AIAccessor>
{
	
	public L2DoorAI(AIAccessor accessor)
	{
		super(accessor);
	}

	@Override
	protected void onIntentionIdle() { }
	
	@Override
	protected void onIntentionActive() { }
	
	@Override
	protected void onIntentionRest() { }
	
	@Override
	protected void onIntentionAttack(L2Character target) { }
	
	@Override
	protected void onIntentionCast(L2Skill skill, L2Object target) { }
	
	@Override
	protected void onIntentionMoveTo(L2Position destination) { }
	
	@Override
	protected void onIntentionFollow(L2Character target) { }
	
	@Override
	protected void onIntentionPickUp(L2Object item) { }
	
	@Override
	protected void onIntentionInteract(L2Object object) { }
	
	@Override
	protected void onEvtThink() { }
	
	@Override
	protected void onEvtAttacked(L2Character attacker) {
		ThreadPoolManager.getInstance().executeTask(new onEventAttackedDoorTask(getActor(), attacker));
	}

    private L2DoorInstance getActor() {
        return getAccessor().getActor();
    }

    @Override
	protected void onEvtAggression(L2Character target, int aggro) { }
	
	@Override
	protected void onEvtStunned(L2Character attacker) { }
	
	@Override
	protected void onEvtSleeping(L2Character attacker) { }
	
	@Override
	protected void onEvtRooted(L2Character attacker) { }
	
	@Override
	protected void onEvtReadyToAct() { }
	
	@Override
	protected void onEvtUserCmd(Object arg0, Object arg1) { }
	
	@Override
	protected void onEvtArrived() { }
	
	@Override
	protected void onEvtArrivedRevalidate() { }
	
	@Override
	protected void onEvtArrivedBlocked(L2Position blocked_at_pos) { }
	
	@Override
	protected void onEvtForgetObject(L2Object object) { }
	
	@Override
	protected void onEvtCancel() { }
	
	@Override
	protected void onEvtDead() { }
	
	private class onEventAttackedDoorTask implements Runnable {
		private final L2DoorInstance _door;
		private final L2Character _attacker;
		
		public onEventAttackedDoorTask(L2DoorInstance door, L2Character attacker) {
			_door = door;
			_attacker = attacker;
		}
		
		@Override
		public void run() {
			_door.getKnownList().updateKnownObjects();
			
			for (L2SiegeGuardInstance guard : _door.getKnownSiegeGuards()) {
				if (getActor().isInsideRadius(guard, guard.getFactionRange(), false, true) && (Math.abs(_attacker.getZ() - guard.getZ()) < 200)) {
					guard.getAI().notifyEvent(Event.EVT_AGGRESSION, _attacker, 15);
				}
			}
		}
	}
}
