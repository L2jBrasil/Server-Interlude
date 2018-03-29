import sys
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.serverpackets import PlaySound

# Boss: Antharas
class antharas(JQuest) :
    def __init__(self,id,name,descr):
        self.antharas = 29019
        JQuest.__init__(self,id,name,descr)

    def onKill(self,npc,player,isPet):
        objId=npc.getObjectId()
        npc.broadcastPacket(PlaySound(1, "BS01_D", 1, objId, npc.getX(), npc.getY(), npc.getZ()))
        #teleport cube antharas.
        self.addSpawn(31859,177615,114941,-7709,0,False,900000)
        return

# now call the constructor (starts up the ai)
QUEST      = antharas(-1,"antharas","ai")

QUEST.addKillId(QUEST.antharas)
QUEST.addAttackId(QUEST.antharas)