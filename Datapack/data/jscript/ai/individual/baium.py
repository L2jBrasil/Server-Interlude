# version 0.1
# by Fulminus

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.serverpackets import SocialAction
from net.sf.l2j.gameserver.serverpackets import Earthquake
from net.sf.l2j.gameserver.serverpackets import PlaySound
from net.sf.l2j.gameserver.ai import CtrlIntention
from net.sf.l2j.util import Rnd
from java.lang import System

STONE_BAIUM = 29025
ANGELIC_VORTEX = 31862
LIVE_BAIUM = 29020

# Boss: Baium
#
# Note1: if the server gets rebooted while players are still fighting Baium, there is no lock, but
#   players also lose their ability to wake baium up.  However, should another person
#   enter the room and wake him up, the players who had stayed inside may join the raid.
#   This can be helpful for players who became victims of a reboot (they only need 1 new player to
#   enter and wake up baium) and is not too exploitable since any player wishing to exploit it
#   would have to suffer 5 days of being parked in an empty room.
# Note2: Neither version of Baium should be a permanent spawn.  This script is fully capable of
#   spawning the statue-version when the lock expires and switching it to the mob version promptly.
#
# Additional notes ( source http://aleenaresron.blogspot.com/2006_08_01_archive.html ):
#   * Baium only first respawns five days after his last death. And from those five days he will
#       respawn within 1-8 hours of his last death. So, you have to know his last time of death.
#   * If by some freak chance you are the only one in Baium's chamber and NO ONE comes in
#       [ha, ha] you or someone else will have to wake Baium. There is a good chance that Baium
#       will automatically kill whoever wakes him. There are some people that have been able to
#       wake him and not die, however if you've already gone through the trouble of getting the
#       bloody fabric and camped him out and researched his spawn time, are you willing to take that 
#       chance that you'll wake him and not be able to finish your quest? Doubtful.
#       [ this powerful attack vs the player who wakes him up is NOT yet implemented here]
#   * once someone starts attacking Baium no one else can port into the chamber where he is.
#       Unlike with the otehr raid bosses, you can just show up at any time as long as you are there
#       when they die. Not true with Baium. Once he gets attacked, the port to Baium closes. byebye,
#       see you in 5 days.  If nobody attacks baium for 30 minutes, he auto-despawns and unlocks the 
#       vortex
class baium (JQuest):

  def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

  def init_LoadGlobalData(self) :
    # initialize
    self.isBaiumAwake = False
    self.playersInside = []
    self.isBaiumLocked = False
    self.lastAttackVsBaiumTime = 0

    # load the unlock date and time for baium from DB
    temp = self.loadGlobalQuestVar("unlockDatetime")
    # if baium is locked until a certain time, mark it so and start the unlock timer
    if temp == "" :
      self.addSpawn(STONE_BAIUM,115213,16623,10080,41740,False,0)
    else :
      # the unlock time has not yet expired.  Mark Baium as currently locked.  Setup a timer
      # to fire at the correct time (calculate the time between now and the unlock time,
      # setup a timer to fire after that many msec)
      temp = long(temp) - System.currentTimeMillis()
      if temp > 0 :
        self.isBaiumLocked = True
        self.startQuestTimer("baium_unlock", temp, None, None)
      else :
        # the time has already expired while the server was offline.  Delete the saved time and
        # immediately spawn the stone-baium.  Also the variable is not changed from "isBaiumLocked = False"
        self.deleteGlobalQuestVar("unlockDatetime")
        self.addSpawn(STONE_BAIUM,115213,16623,10080,41740,False,0)
    return

  def onAdvEvent (self,event,npc,player):
    objId=0
    if event == "baium_unlock" :
      self.isBaiumLocked = False
      self.deleteGlobalQuestVar("unlockDatetime")
      self.addSpawn(STONE_BAIUM,115213,16623,10080,41740,False,0)
    elif event == "baium_wakeup" and npc:
      if npc.getNpcId() == LIVE_BAIUM :
        npc.broadcastPacket(SocialAction(npc.getObjectId(),1))
        npc.broadcastPacket(Earthquake(npc.getX(), npc.getY(), npc.getZ(),40,5))
        # once Baium is awaken, no more people may enter until he dies, the server reboots, or 
        # 30 pass with no attacks made against Baium.
        self.isBaiumLocked = True
        # start monitoring baium's inactivity
        self.lastAttackVsBaiumTime = System.currentTimeMillis()
        self.startQuestTimer("baium_despawn", 60000, npc, None)
        # TODO: the person who woke baium up should be knocked across the room, onto a wall, and
        # lose massive amounts of HP.
    # despawn the live baium after 30 minutes of inactivity  
    elif event == "baium_despawn" and npc:
      if (npc.getNpcId() == LIVE_BAIUM) and (self.lastAttackVsBaiumTime + 1800000 < System.currentTimeMillis()) :
        npc.deleteMe()   # despawn the live-baium
        self.addSpawn(STONE_BAIUM,115213,16623,10080,41740,False,0)  # spawn stone-baium
        self.deleteGlobalQuestVar("unlockDatetime")  # make sure that all locks are deleted from the DB
        self.isBaiumAwake = False       # mark that Baium is not awake any more 
        self.isBaiumLocked = False      # unlock the entrance
        self.playersInside = []
      else :
        # if baium's inactivity is still younger than 30 minutes, just re-add the timer
        self.startQuestTimer("baium_despawn", 60000, npc, None)
    return

  def onTalk (self,npc,player):
    npcId = npc.getNpcId()
    htmltext = ""
    if npcId == STONE_BAIUM :
      if player in self.playersInside :
        if not npc.isBusy():
           npc.setBusy(True)
           npc.setBusyMessage("Attending another player's request")
           npc.deleteMe()
           baium = self.addSpawn(LIVE_BAIUM,npc)
           baium.broadcastPacket(SocialAction(baium.getObjectId(),2))
           self.startQuestTimer("baium_wakeup",15000, baium, None)
           self.playersInside = []
      else:
        htmltext = "Conditions are not right to wake up Baium"
    elif npcId == ANGELIC_VORTEX :
      if not self.isBaiumLocked :
        if player.isFlying() :
          print "Player "+player.getName()+" attempted to enter Baium's layer while flying!"
          htmltext = '<html><body>Angelic Vortex:<br>You may not enter while flying a wyvern</body></html>'
        if player.getQuestState("baium").getQuestItemsCount(4295) : # bloody fabric
          player.getQuestState("baium").takeItems(4295,1)
          player.teleToLocation(113100,14500,10077)
          if not self.isBaiumAwake :
            self.playersInside.append(player)
        else :
          htmltext = '<html><body>Angelic Vortex:<br>You do not have enough items</body></html>'
      else :
        htmltext = '<html><body>Angelic Vortex:<br>You may not enter at this time</body></html>'
    return htmltext
    
  def onAttack(self, npc, player, damage, isPet) :
    # update a variable with the last action against baium
    self.lastAttackVsBaiumTime = System.currentTimeMillis()
    
  def onKill(self,npc,player,isPet):
    objId=npc.getObjectId()
    npc.broadcastPacket(PlaySound(1, "BS02_D", 1, objId, npc.getX(), npc.getY(), npc.getZ()))
    # spawn the "Teleportation Cubic" for 15 minutes (to allow players to exit the lair)
    self.addSpawn(29055,115203,16620,10078,0,False,900000)
    
    # "lock" baium for 5 days and 1 to 8 hours [i.e. 432,000,000 +  1*3,600,000 + random-less-than(8*3,600,000) millisecs]
    respawnTime = 435600000 + Rnd.get(8*3600000)
    self.isBaiumLocked = True
    self.startQuestTimer("baium_unlock", respawnTime, None, None)
    # also save the respawn time so that the info is maintained past reboots
    self.saveGlobalQuestVar("unlockDatetime", str(System.currentTimeMillis() + respawnTime))
    

# Quest class and state definition
QUEST       = baium(-1, "baium", "ai")
CREATED     = State('Start', QUEST)

# Quest initialization
QUEST.setInitialState(CREATED)
# Quest NPC starter initialization
QUEST.addStartNpc(STONE_BAIUM)
QUEST.addStartNpc(ANGELIC_VORTEX)
QUEST.addTalkId(STONE_BAIUM)
QUEST.addTalkId(ANGELIC_VORTEX)

QUEST.addKillId(LIVE_BAIUM)
QUEST.addAttackId(LIVE_BAIUM)