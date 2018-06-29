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
import com.l2jbr.gameserver.Territory;
import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.instancemanager.DimensionalRiftManager;
import com.l2jbr.gameserver.model.*;
import com.l2jbr.gameserver.model.actor.instance.*;
import com.l2jbr.gameserver.model.entity.database.Weapon;
import com.l2jbr.gameserver.templates.ItemType;

import java.util.concurrent.Future;

import static com.l2jbr.gameserver.ai.Intention.*;

/**
 * This class manages AI of L2Attackable.
 */
public class L2AttackableAI<T extends L2Attackable.AIAccessor> extends MovableAI<T> implements Runnable
{
	// protected static final Logger _log = LoggerFactory.getLogger(L2AttackableAI.class.getName());
	
	private static final int RANDOM_WALK_RATE = 30; // confirmed
	// private static final int MAX_DRIFT_RANGE = 300;
	private static final int MAX_ATTACK_TIMEOUT = 300; // int ticks, i.e. 30 seconds
	
	/** The L2Attackable AI task executed every 1s (call onEvtThink method) */
	private Future<?> _aiTask;
	
	/** The delay after wich the attacked is stopped */
	protected int _attackTimeout;
	
	/** The L2Attackable aggro counter */
	protected int _globalAggro;
	
	/** The flag used to indicate that a thinking action is in progress */
	protected boolean _thinking; // to prevent recursive thinking
	
	/**
	 * Constructor of L2AttackableAI.<BR>
	 * <BR>
	 * @param accessor The AI accessor of the L2Character
	 */
	public L2AttackableAI(T accessor) {
		super(accessor);
		
		_attackTimeout = Integer.MAX_VALUE;
		_globalAggro = -10; // 10 seconds timeout of ATTACK after respawn
	}
	
	@Override
	public void run()
	{
		// Launch actions corresponding to the Event Think
		onEvtThink();
		
	}
	
	/**
	 * Return True if the target is autoattackable (depends on the actor type).<BR>
	 * <BR>
	 * <B><U> Actor is a L2GuardInstance</U> :</B><BR>
	 * <BR>
	 * <li>The target isn't a Folk or a Door</li> <li>The target isn't dead, isn't invulnerable, isn't in silent moving mode AND too far (>100)</li> <li>The target is in the actor Aggro range and is at the same height</li> <li>The L2PcInstance target has karma (=PK)</li> <li>The L2MonsterInstance
	 * target is aggressive</li><BR>
	 * <BR>
	 * <B><U> Actor is a L2SiegeGuardInstance</U> :</B><BR>
	 * <BR>
	 * <li>The target isn't a Folk or a Door</li> <li>The target isn't dead, isn't invulnerable, isn't in silent moving mode AND too far (>100)</li> <li>The target is in the actor Aggro range and is at the same height</li> <li>A siege is in progress</li> <li>The L2PcInstance target isn't a Defender</li>
	 * <BR>
	 * <BR>
	 * <B><U> Actor is a L2FriendlyMobInstance</U> :</B><BR>
	 * <BR>
	 * <li>The target isn't a Folk, a Door or another L2NpcInstance</li> <li>The target isn't dead, isn't invulnerable, isn't in silent moving mode AND too far (>100)</li> <li>The target is in the actor Aggro range and is at the same height</li> <li>The L2PcInstance target has karma (=PK)</li><BR>
	 * <BR>
	 * <B><U> Actor is a L2MonsterInstance</U> :</B><BR>
	 * <BR>
	 * <li>The target isn't a Folk, a Door or another L2NpcInstance</li> <li>The target isn't dead, isn't invulnerable, isn't in silent moving mode AND too far (>100)</li> <li>The target is in the actor Aggro range and is at the same height</li> <li>The actor is Aggressive</li><BR>
	 * <BR>
	 * @param target The targeted L2Object
	 * @return
	 */
	private boolean autoAttackCondition(L2Character target) {
        L2Attackable actor = getActor();
		if ((target == null))
		{
			return false;
		}
		L2Attackable me = (L2Attackable) actor;
		
		// Check if the target isn't invulnerable
		if (target.isInvul())
		{
			// However EffectInvincible requires to check GMs specially
			if ((target instanceof L2PcInstance) && ((L2PcInstance) target).isGM())
			{
				return false;
			}
			if ((target instanceof L2Summon) && ((L2Summon) target).getOwner().isGM())
			{
				return false;
			}
		}
		
		// Check if the target isn't a Folk or a Door
		if ((target instanceof L2FolkInstance) || (target instanceof L2DoorInstance))
		{
			return false;
		}
		
		// Check if the target isn't dead, is in the Aggro range and is at the same height
		if (target.isAlikeDead() || !me.isInsideRadius(target, me.getAggroRange(), false, false) || (Math.abs(actor.getZ() - target.getZ()) > 300))
		{
			return false;
		}
		
		// Check if the target is a L2PcInstance
		if (target instanceof L2PcInstance)
		{
			// Don't take the aggro if the GM has the access level below or equal to GM_DONT_TAKE_AGGRO
			if (((L2PcInstance) target).isGM() && (((L2PcInstance) target).getAccessLevel() <= Config.GM_DONT_TAKE_AGGRO))
			{
				return false;
			}
			
			// Check if the AI isn't a Raid Boss and the target isn't in silent move mode
			if (!(me instanceof L2RaidBossInstance) && ((L2PcInstance) target).isSilentMoving())
			{
				return false;
			}
			
			// Check if player is an ally //TODO! [Nemesiss] it should be rather boolean or smth like that
			// Comparing String isnt good idea!
			if ((me.getFactionId() == "varka") && ((L2PcInstance) target).isAlliedWithVarka())
			{
				return false;
			}
			if ((me.getFactionId() == "ketra") && ((L2PcInstance) target).isAlliedWithKetra())
			{
				return false;
			}
			// check if the target is within the grace period for JUST getting up from fake death
			if (((L2PcInstance) target).isRecentFakeDeath())
			{
				return false;
			}
			
			if (target.isInParty() && target.getParty().isInDimensionalRift())
			{
				byte riftType = target.getParty().getDimensionalRift().getType();
				byte riftRoom = target.getParty().getDimensionalRift().getCurrentRoom();
				
				if ((me instanceof L2RiftInvaderInstance) && !DimensionalRiftManager.getInstance().getRoom(riftType, riftRoom).checkIfInZone(me.getX(), me.getY(), me.getZ()))
				{
					return false;
				}
			}
		}
		
		// Check if the actor is a L2GuardInstance
		if (actor instanceof L2GuardInstance)
		{
			
			// Check if the L2PcInstance target has karma (=PK)
			if ((target instanceof L2PcInstance) && (((L2PcInstance) target).getKarma() > 0))
			{
				// Los Check
				return GeoData.getInstance().canSeeTarget(me, target);
			}
			
			// if (target instanceof L2Summon)
			// return ((L2Summon)target).getKarma() > 0;
			
			// Check if the L2MonsterInstance target is aggressive
			if (target instanceof L2MonsterInstance)
			{
				return (((L2MonsterInstance) target).isAggressive() && GeoData.getInstance().canSeeTarget(me, target));
			}
			
			return false;
		}
		else if (actor instanceof L2FriendlyMobInstance)
		{ // the actor is a L2FriendlyMobInstance
		
			// Check if the target isn't another L2NpcInstance
			if (target instanceof L2NpcInstance)
			{
				return false;
			}
			
			// Check if the L2PcInstance target has karma (=PK)
			if ((target instanceof L2PcInstance) && (((L2PcInstance) target).getKarma() > 0))
			{
				// Los Check
				return GeoData.getInstance().canSeeTarget(me, target);
			}
			return false;
		}
		else
		{ // The actor is a L2MonsterInstance
		
			// Check if the target isn't another L2NpcInstance
			if (target instanceof L2NpcInstance)
			{
				return false;
			}
			
			// depending on config, do not allow mobs to attack _new_ players in peacezones,
			// unless they are already following those players from outside the peacezone.
			if (!Config.ALT_MOB_AGRO_IN_PEACEZONE && target.isInsideZone(L2Character.ZONE_PEACE))
			{
				return false;
			}
			
			// Check if the actor is Aggressive
			return (me.isAggressive() && GeoData.getInstance().canSeeTarget(me, target));
		}
	}

    private L2Attackable getActor() {
        return getAccessor().getActor();
    }

    public void startAITask()
	{
		// If not idle - create an AI task (schedule onEvtThink repeatedly)
		if (_aiTask == null)
		{
			_aiTask = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(this, 1000, 1000);
		}
	}
	
	public void stopAITask(boolean interruptIfRunning) {
		if (_aiTask != null) {
			_aiTask.cancel(interruptIfRunning);
			_aiTask = null;
		}
	}
	
	@Override
	protected void onEvtDead()
	{
		stopAITask(false);
		super.onEvtDead();
	}
	
	/**
	 * Set the Intention of this L2CharacterAI and create an AI Task executed every
	 * 1s (call onEvtThink method) for this L2Attackable.<BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> :
	 *
	 * If actor _knowPlayer isn't EMPTY, AI_INTENTION_IDLE will be change in AI_INTENTION_ACTIVE</B></FONT><BR>
	 * <BR>
	 * @param intention The new Intention to set to the AI
	 * @param arg0 The first parameter of the Intention
	 * @param arg1 The second parameter of the Intention
	 */
	@Override
	synchronized void changeIntention(Intention intention, Object arg0, Object arg1) {
        L2Attackable actor = getActor();
		if ((intention == AI_INTENTION_IDLE)) {
			if (!actor.isAlikeDead()) {
				if (actor.getKnownList().getKnownPlayers().size() > 0) {
					intention = AI_INTENTION_ACTIVE;
				}
			}
		}

        super.changeIntention(intention, null, null);

        if (intention == AI_INTENTION_IDLE) {
            stopAITask(true);
            // Cancel the AI
            getAccessor().detachAI();
            return;
        }

		startAITask();
	}

	@Override
	protected void onIntentionAttack(L2Character target) {
		_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getGameTicks();
		super.onIntentionAttack(target);
	}
	
	/**
	 * Manage AI standard thinks of a L2Attackable (called by onEvtThink).<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Update every 1s the _globalAggro counter to come close to 0</li> <li>If the actor is Aggressive and can attack, add all autoAttackable L2Character in its Aggro Range to its _aggroList, chose a target and order to attack it</li> <li>If the actor is a L2GuardInstance that can't attack,
	 * order to it to return to its home location</li> <li>If the actor is a L2MonsterInstance that can't attack, order to it to random walk (1/100)</li><BR>
	 * <BR>
	 */
	private void thinkActive()
	{
		L2Attackable actor = getActor();
		
		// Update every 1s the _globalAggro counter to come close to 0
		if (_globalAggro != 0)
		{
			if (_globalAggro < 0)
			{
				_globalAggro++;
			}
			else
			{
				_globalAggro--;
			}
		}
		
		// Add all autoAttackable L2Character in L2Attackable Aggro Range to its _aggroList with 0 damage and 1 hate
		// A L2Attackable isn't aggressive during 10s after its spawn because _globalAggro is set to -10
		if (_globalAggro >= 0)
		{
			// Get all visible objects inside its Aggro Range
			// L2Object[] objects = L2World.getInstance().getVisibleObjects(_actor, ((L2NpcInstance)_actor).getAggroRange());
			// Go through visible objects
			for (L2Object obj : actor.getKnownList().getKnownObjects().values())
			{
				if ((obj == null) || !(obj instanceof L2Character))
				{
					continue;
				}
				L2Character target = (L2Character) obj;
				
				/*
				 * Check to see if this is a festival mob spawn. If it is, then check to see if the aggro trigger is a festival participant...if so, move to attack it.
				 */
				if ((actor instanceof L2FestivalMonsterInstance) && (obj instanceof L2PcInstance))
				{
					L2PcInstance targetPlayer = (L2PcInstance) obj;
					
					if (!(targetPlayer.isFestivalParticipant()))
					{
						continue;
					}
				}
				
				// For each L2Character check if the target is autoattackable
				if (autoAttackCondition(target)) // check aggression
				{
					// Get the hate level of the L2Attackable against this L2Character target contained in _aggroList
					int hating = actor.getHating(target);
					
					// Add the attacker to the L2Attackable _aggroList with 0 damage and 1 hate
					if (hating == 0)
					{
						actor.addDamageHate(target, 0, 1);
					}
				}
			}
			
			// Chose a target from its aggroList
			L2Character hated;
			if (actor.isConfused())
			{
				hated = getAttackTarget(); // Force mobs to attak anybody if confused
			}
			else
			{
				hated = actor.getMostHated();
			}
			
			// Order to the L2Attackable to attack the target
			if (hated != null)
			{
				// Get the hate level of the L2Attackable against this L2Character target contained in _aggroList
				int aggro = actor.getHating(hated);
				
				if ((aggro + _globalAggro) > 0)
				{
					// Set the L2Character movement type to run and send Server->Client packet ChangeMoveType to all others L2PcInstance
					if (!actor.isRunning())
					{
						actor.setRunning();
					}
					
					// Set the AI Intention to AI_INTENTION_ATTACK
					setIntention(Intention.AI_INTENTION_ATTACK, hated);
				}
				
				return;
			}
			
		}
		
		// Check if the actor is a L2GuardInstance
		if (actor instanceof L2GuardInstance)
		{
			// Order to the L2GuardInstance to return to its home location because there's no target to attack
			((L2GuardInstance) actor).returnHome();
		}
		
		// If this is a festival monster, then it remains in the same location.
		if (actor instanceof L2FestivalMonsterInstance)
		{
			return;
		}
		
		// Minions following leader
		if ((actor instanceof L2MinionInstance) && (((L2MinionInstance) actor).getLeader() != null))
		{
			int offset;
			
			if (actor.isRaid())
			{
				offset = 500; // for Raids - need correction
			}
			else
			{
				offset = 200; // for normal minions - need correction :)
			}
			
			if (((L2MinionInstance) actor).getLeader().isRunning())
			{
				actor.setRunning();
			}
			else
			{
				actor.setWalking();
			}
			
			if (actor.getPlanDistanceSq(((L2MinionInstance) actor).getLeader()) > (offset * offset))
			{
				int x1, y1, z1;
				x1 = (((L2MinionInstance) actor).getLeader().getX() + Rnd.nextInt((offset - 30) * 2)) - (offset - 30);
				y1 = (((L2MinionInstance) actor).getLeader().getY() + Rnd.nextInt((offset - 30) * 2)) - (offset - 30);
				z1 = ((L2MinionInstance) actor).getLeader().getZ();
				// Move the actor to Location (x,y,z) server side AND client side by sending Server->Client packet CharMoveToLocation (broadcast)
				moveTo(x1, y1, z1);
				return;
			}
		}
		// Order to the L2MonsterInstance to random walk (1/100)
		else if ((actor.getSpawn() != null) && (Rnd.nextInt(RANDOM_WALK_RATE) == 0))
		{
			int x1, y1, z1;
			
			// If NPC with random coord in territory
			if ((actor.getSpawn().getLocx() == 0) && (actor.getSpawn().getLocy() == 0))
			{
				// If NPC with random fixed coord, don't move
				if (Territory.getInstance().getProcMax(actor.getSpawn().getLocation()) > 0)
				{
					return;
				}
				
				// Calculate a destination point in the spawn area
				int p[] = Territory.getInstance().getRandomPoint(actor.getSpawn().getLocation());
				x1 = p[0];
				y1 = p[1];
				z1 = p[2];
				
				// Calculate the distance between the current position of the L2Character and the target (x,y)
				double distance2 = actor.getPlanDistanceSq(x1, y1);
				
				if (distance2 > (Config.MAX_DRIFT_RANGE * Config.MAX_DRIFT_RANGE))
				{
					actor.setisReturningToSpawnPoint(true);
					float delay = (float) Math.sqrt(distance2) / Config.MAX_DRIFT_RANGE;
					x1 = actor.getX() + (int) ((x1 - actor.getX()) / delay);
					y1 = actor.getY() + (int) ((y1 - actor.getY()) / delay);
				}
				else
				{
					actor.setisReturningToSpawnPoint(false);
				}
				
			}
			else
			{
				// If NPC with fixed coord
				x1 = (actor.getSpawn().getLocx() + Rnd.nextInt(Config.MAX_DRIFT_RANGE * 2)) - Config.MAX_DRIFT_RANGE;
				y1 = (actor.getSpawn().getLocy() + Rnd.nextInt(Config.MAX_DRIFT_RANGE * 2)) - Config.MAX_DRIFT_RANGE;
				z1 = actor.getZ();
			}
			
			// _log.info("Curent pos ("+getX()+", "+getY()+"), moving to ("+x1+", "+y1+").");
			// Move the actor to Location (x,y,z) server side AND client side by sending Server->Client packet CharMoveToLocation (broadcast)
			moveTo(x1, y1, z1);
		}
		
		return;
		
	}
	
	/**
	 * Manage AI attack thinks of a L2Attackable (called by onEvtThink).<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Update the attack timeout if actor is running</li> <li>If target is dead or timeout is expired, stop this attack and set the Intention to AI_INTENTION_ACTIVE</li> <li>Call all L2Object of its Faction inside the Faction Range</li> <li>Chose a target and order to attack it with magic skill
	 * or physical attack</li><BR>
	 * <BR>
	 * TODO: Manage casting rules to healer mobs (like Ant Nurses)
	 */
	private void thinkAttack(){
        L2Attackable actor = getActor();
		if (_attackTimeout < GameTimeController.getGameTicks()){
			if (actor.isRunning()){
				actor.setWalking();
				_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getGameTicks();
			}
		}

		if ((getAttackTarget() == null) || getAttackTarget().isAlikeDead() || (_attackTimeout < GameTimeController.getGameTicks())) {
			if (getAttackTarget() != null) {
				actor.stopHating(getAttackTarget());
			}
			
			// Set the AI Intention to AI_INTENTION_ACTIVE
			setIntention(AI_INTENTION_ACTIVE);
			
			actor.setWalking();
		}
		else
		{
			// Call all L2Object of its Faction inside the Faction Range
			if (((L2NpcInstance) actor).getFactionId() != null)
			{
				String faction_id = ((L2NpcInstance) actor).getFactionId();
				
				// Go through all L2Object that belong to its faction
				for (L2Object obj : actor.getKnownList().getKnownObjects().values())
				{
					if (obj instanceof L2NpcInstance)
					{
						L2NpcInstance npc = (L2NpcInstance) obj;
						if ((getAttackTarget() == null) || (faction_id != npc.getFactionId()))
						{
							continue;
						}
						
						// Check if the L2Object is inside the Faction Range of the actor
						if (actor.isInsideRadius(npc, npc.getFactionRange(), true, false) && GeoData.getInstance().canSeeTarget(actor, npc) && (Math.abs(getAttackTarget().getZ() - npc.getZ()) < 600) && (npc.getAI() != null) && actor.getAttackByList().contains(getAttackTarget()) && ((npc.getAI().getIntention() == Intention.AI_INTENTION_IDLE) || (npc.getAI().getIntention() == Intention.AI_INTENTION_ACTIVE)))
						{
							if ((getAttackTarget() instanceof L2PcInstance) && getAttackTarget().isInParty() && getAttackTarget().getParty().isInDimensionalRift())
							{
								byte riftType = getAttackTarget().getParty().getDimensionalRift().getType();
								byte riftRoom = getAttackTarget().getParty().getDimensionalRift().getCurrentRoom();
								
								if ((actor instanceof L2RiftInvaderInstance) && !DimensionalRiftManager.getInstance().getRoom(riftType, riftRoom).checkIfInZone(npc.getX(), npc.getY(), npc.getZ()))
								{
									continue;
								}
							}
							
							// Notify the L2Object AI with EVT_AGGRESSION
							npc.getAI().notifyEvent(Event.EVT_AGGRESSION, getAttackTarget(), 1);
						}
					}
				}
			}
			
			if (actor.isAttackingDisabled())
			{
				return;
			}
			
			// Get all information needed to chose between physical or magical attack
			L2Skill[] skills = null;
			double dist2 = 0;
			int range = 0;
			
			try
			{
				actor.setTarget(getAttackTarget());
				skills = actor.getAllSkills();
				dist2 = actor.getPlanDistanceSq(getAttackTarget().getX(), getAttackTarget().getY());
				range = actor.getPhysicalAttackRange() + (int) actor.getTemplate().getCollisionRadius() + (int) getAttackTarget().getTemplate().getCollisionRadius();
			}
			catch (NullPointerException e)
			{
				// _log.warn("AttackableAI: Attack target is NULL.");
				setIntention(AI_INTENTION_ACTIVE);
				return;
			}
			
			Weapon weapon = actor.getActiveWeaponItem();
			if ((weapon != null) && (weapon.getType() == ItemType.BOW))
			{
				// Micht: kepping this one otherwise we should do 2 sqrt
				double distance2 = actor.getPlanDistanceSq(getAttackTarget().getX(), getAttackTarget().getY());
				if (distance2 <= 10000)
				{
					int chance = 5;
					if (chance >= Rnd.get(100))
					{
						int posX = actor.getX();
						int posY = actor.getY();
						int posZ = actor.getZ();
						double distance = Math.sqrt(distance2); // This way, we only do the sqrt if we need it
						
						int signx = -1;
						int signy = -1;
						if (actor.getX() > getAttackTarget().getX())
						{
							signx = 1;
						}
						if (actor.getY() > getAttackTarget().getY())
						{
							signy = 1;
						}
						posX += Math.round((float) ((signx * ((range / 2) + (Rnd.get(range)))) - distance));
						posY += Math.round((float) ((signy * ((range / 2) + (Rnd.get(range)))) - distance));
						setIntention(Intention.AI_INTENTION_MOVE_TO, new L2Position(posX, posY, posZ, 0));
						return;
					}
				}
			}
			
			// Force mobs to attack anybody if confused
			L2Character hated;
			if (actor.isConfused())
			{
				hated = getAttackTarget();
			}
			else
			{
				hated = ((L2Attackable) actor).getMostHated();
			}
			
			if (hated == null)
			{
				setIntention(AI_INTENTION_ACTIVE);
				return;
			}
			if (hated != getAttackTarget())
			{
				setAttackTarget(hated);
			}
			// We should calculate new distance cuz mob can have changed the target
			dist2 = actor.getPlanDistanceSq(hated.getX(), hated.getY());
			
			if (hated.isMoving())
			{
				range += 50;
			}
			// Check if the actor isn't far from target
			if (dist2 > (range * range))
			{
				// check for long ranged skills and heal/buff skills
				if (!actor.isMuted() && (!Config.ALT_GAME_MOB_ATTACK_AI || ((actor instanceof L2MonsterInstance) && (Rnd.nextInt(100) <= 5))))
				{
					for (L2Skill sk : skills)
					{
						int castRange = sk.getCastRange();
						
						if ((((sk.getSkillType() == L2Skill.SkillType.BUFF) || (sk.getSkillType() == L2Skill.SkillType.HEAL)) || ((dist2 >= ((castRange * castRange) / 9.0)) && (dist2 <= (castRange * castRange)) && (castRange > 70))) && !actor.isSkillDisabled(sk.getId()) && (actor.getCurrentMp() >= actor.getStat().getMpConsume(sk)) && !sk.isPassive() && (Rnd.nextInt(100) <= 5))
						{
							L2Object OldTarget = actor.getTarget();
							if ((sk.getSkillType() == L2Skill.SkillType.BUFF) || (sk.getSkillType() == L2Skill.SkillType.HEAL))
							{
								boolean useSkillSelf = true;
								if ((sk.getSkillType() == L2Skill.SkillType.HEAL) && (actor.getCurrentHp() > (int) (actor.getMaxHp() / 1.5)))
								{
									useSkillSelf = false;
									break;
								}
								if (sk.getSkillType() == L2Skill.SkillType.BUFF)
								{
									L2Effect[] effects = actor.getAllEffects();
									for (int i = 0; (effects != null) && (i < effects.length); i++)
									{
										L2Effect effect = effects[i];
										if (effect.getSkill() == sk)
										{
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
				
				// Move the actor to Pawn server side AND client side by sending Server->Client packet MoveToPawn (broadcast)
				if (hated.isMoving())
				{
					range -= 100;
				}
				if (range < 5)
				{
					range = 5;
				}
				moveToPawn(getAttackTarget(), range);
			}
			// Else, if this is close enough to attack
			else
			{
				_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getGameTicks();
				
				// check for close combat skills && heal/buff skills
				if (!actor.isMuted() /* && _rnd.nextInt(100) <= 5 */)
				{
					boolean useSkillSelf = true;
					for (L2Skill sk : skills)
					{
						if (/* sk.getCastRange() >= dist && sk.getCastRange() <= 70 && */!sk.isPassive() && (actor.getCurrentMp() >= actor.getStat().getMpConsume(sk)) && !actor.isSkillDisabled(sk.getId()) && ((Rnd.nextInt(100) <= 8) || ((actor instanceof L2PenaltyMonsterInstance) && (Rnd.nextInt(100) <= 20))))
						{
							L2Object OldTarget = actor.getTarget();
							if ((sk.getSkillType() == L2Skill.SkillType.BUFF) || (sk.getSkillType() == L2Skill.SkillType.HEAL))
							{
								useSkillSelf = true;
								if ((sk.getSkillType() == L2Skill.SkillType.HEAL) && (actor.getCurrentHp() > (int) (actor.getMaxHp() / 1.5)))
								{
									useSkillSelf = false;
									break;
								}
								if (sk.getSkillType() == L2Skill.SkillType.BUFF)
								{
									L2Effect[] effects = actor.getAllEffects();
									for (int i = 0; (effects != null) && (i < effects.length); i++)
									{
										L2Effect effect = effects[i];
										if (effect.getSkill() == sk)
										{
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
							// GeoData Los Check here
							if (!useSkillSelf && !GeoData.getInstance().canSeeTarget(actor, actor.getTarget()))
							{
								return;
							}
							clientStopMoving(null);
							getAccessor().doCast(sk);
							actor.setTarget(OldTarget);
							return;
						}
					}
				}
				
				// Finally, physical attacks
				clientStopMoving(null);
				getAccessor().doAttack(hated);
			}
		}
	}
	

	@Override
	protected void onEvtThink() {
        L2Attackable actor = getActor();
		if (_thinking || actor.isAllSkillsDisabled()) {
			return;
		}

		_thinking = true;

		try {
			if (getIntention() == AI_INTENTION_ACTIVE) {
				thinkActive();
			} else if (getIntention() == AI_INTENTION_ATTACK) {
				thinkAttack();
			}
		} finally {
			_thinking = false;
		}
	}


	@Override
	protected void onEvtAttacked(L2Character attacker) {
		_attackTimeout = MAX_ATTACK_TIMEOUT + GameTimeController.getGameTicks();

		if (_globalAggro < 0) {
			_globalAggro = 0;
		}

        L2Attackable actor = getActor();
		actor.addDamageHate(attacker, 0, 1);

		if (!actor.isRunning()) {
			actor.setRunning();
		}

		if (getIntention() != AI_INTENTION_ATTACK) {
			setIntention(Intention.AI_INTENTION_ATTACK, attacker);
		} else {
            L2Character mostHated = actor.getMostHated();
		    if (mostHated != getAttackTarget()) {
                setIntention(Intention.AI_INTENTION_ATTACK, mostHated);
            }
        }
		
		super.onEvtAttacked(attacker);
	}
	
	/**
	 * Launch actions corresponding to the Event Aggression.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Add the target to the actor _aggroList or update hate if already present</li> <li>Set the actor Intention to AI_INTENTION_ATTACK (if actor is L2GuardInstance check if it isn't too far from its home location)</li><BR>
	 * <BR>
	 * @param target The L2Character that attacks
	 * @param aggro The value of hate to add to the actor against the target
	 */
	@Override
	protected void onEvtAggression(L2Character target, int aggro)
	{
		L2Attackable _actor = getActor();
		
		if (target != null)
		{
			// Add the target to the actor _aggroList or update hate if already present
			_actor.addDamageHate(target, 0, aggro);
			
			// Set the actor AI Intention to AI_INTENTION_ATTACK
			if (getIntention() != Intention.AI_INTENTION_ATTACK)
			{
				// Set the L2Character movement type to run and send Server->Client packet ChangeMoveType to all others L2PcInstance
				if (!_actor.isRunning())
				{
					_actor.setRunning();
				}
				
				setIntention(Intention.AI_INTENTION_ATTACK, target);
			}
		}
	}
	
	@Override
	protected void onIntentionActive() {
		_attackTimeout = Integer.MAX_VALUE;
		super.onIntentionActive();
	}
	
	public void setGlobalAggro(int value)
	{
		_globalAggro = value;
	}
}
