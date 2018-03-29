# Script for Pagan Temple Teleporters
# Needed for Quests 636 and 637
# v1.1 Done by BiTi

import sys
from net.sf.l2j.gameserver.model.actor.instance import L2PcInstance
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
qn = "1630_PaganTeleporters"
NPCS=[32034,32036,32039,32040]

# Main Quest Code
class Quest (JQuest):

  def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

  def onTalk (self,npc,player):
    st = player.getQuestState(qn)
    npcId = npc.getNpcId()
    htmltext = "You have been teleported."
    if player.getLevel() < 73 :
       htmltext = "<html><body>Teleport available only for characters with Pagans Mark and level 73 or above.</body></html>"
    elif npcId == 32034 and st.getQuestItemsCount(8064) :
          st.takeItems(8064,1)
          htmltext = "FadedMark.htm"
          st.giveItems(8065,1)
          player.teleToLocation(-16324,-37147,-10724)
    elif npcId in [32034,32036]:
       if not st.getQuestItemsCount(8067) :
          htmltext = '<html><body>Teleport available only for characters with Pagans Mark and level 73 or above.</body></html>'
       else:
          if npcId == 32034 :
             player.teleToLocation(-16324,-37147,-10724)
          else :
             player.teleToLocation(-16324,-44638,-10724)
    elif npcId == 32040 and st.getQuestItemsCount(8065) :
       player.teleToLocation(36640,-51218,718)
    elif not st.getQuestItemsCount(8064)+st.getQuestItemsCount(8067) :
       htmltext = '<html>Teleport available only for characters with Pagans Mark or Visitors Mark and level 73 or above.</body></html>'
    else :
       if npcId == 32039 :
          player.teleToLocation(-12241,-35884,-10856)
       elif npcId == 32040 :
          player.teleToLocation(36640,-51218,718)
    st.exitQuest(1)
    return htmltext

# Quest class and state definition
QUEST       = Quest(1630, qn, "Teleporters")
CREATED     = State('Start', QUEST)

# Quest initialization
QUEST.setInitialState(CREATED)
# Quest NPC starter initialization
for npc in NPCS :
    QUEST.addStartNpc(npc)
    QUEST.addTalkId(npc)