package com.l2jbr.gameserver.templates;

import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.actor.instance.*;


public enum NpcType {

    L2Pet(L2PetInstance.class),
    L2Monster(L2MonsterInstance.class),
    L2BabyPet(L2BabyPetInstance.class),
    L2Teleporter(L2TeleporterInstance.class),
    L2ControlTower(L2ControlTowerInstance.class),
    L2Npc(L2NpcInstance.class),
    L2SiegeSummon(L2SiegeSummonInstance.class),
    L2TamedBeast(L2TamedBeastInstance.class),
    L2FestivalMonster(L2FestivalMonsterInstance.class),
    L2Chest(L2ChestInstance.class),
    L2PenaltyMonster(L2PenaltyMonsterInstance.class),
    L2Minion(L2MinionInstance.class),
    L2FeedableBeast(L2FeedableBeastInstance.class),
    L2RiftInvader(L2RiftInvaderInstance.class),
    L2Boss(L2BossInstance.class),
    L2RaidBoss(L2RaidBossInstance.class),
    L2Merchant(L2MerchantInstance.class),
    L2Warehouse(L2WarehouseInstance.class),
    L2Trainer(L2TrainerInstance.class),
    L2VillageMaster(L2VillageMasterInstance.class),
    L2Guard(L2GuardInstance.class),
    L2Auctioneer(L2AuctioneerInstance.class),
    L2Doormen(L2DoormenInstance.class),
    L2Observation(L2ObservationInstance.class),
    L2SymbolMaker(L2SymbolMakerInstance.class),
    L2SignsPriest(L2SignsPriestInstance.class),
    L2CabaleBuffer(L2CabaleBufferInstance.class),
    L2FestivalGuide(L2FestivalGuideInstance.class),
    L2ClassMaster(L2ClassMasterInstance.class),
    L2NpcWalker(L2NpcWalkerInstance.class),
    L2Fisherman(L2FishermanInstance.class),
    L2OlympiadManager(L2OlympiadManagerInstance.class),
    L2Adventurer(L2AdventurerInstance.class),
    L2FriendlyMob(L2FriendlyMobInstance.class),
    L2SiegeGuard(L2SiegeGuardInstance.class),
    L2Artefact(L2ArtefactInstance.class),
    L2CastleTeleporter(L2CastleTeleporterInstance.class),
    L2CastleBlacksmith(L2CastleBlacksmithInstance.class),
    L2CastleWarehouse(L2CastleWarehouseInstance.class),
    L2CastleChamberlain(L2CastleChamberlainInstance.class),
    L2WyvernManager(L2WyvernManagerInstance.class),
    L2MercManager(L2MercManagerInstance.class),
    L2ManorManager(L2ManorManagerInstance.class),
    L2SiegeNpc(L2SiegeNpcInstance.class),
    L2ClanHallManager(L2ClanHallManagerInstance.class),
    L2Deco(L2NpcInstance.class),
    L2WeddingManager(L2WeddingManagerInstance.class),
    L2TvTEventNpc(L2TvTEventNpcInstance.class),
    L2RaceManager(L2RaceManagerInstance.class),
    ;

    private final Class<? extends L2Character> instanceClass;

    NpcType(Class<? extends L2Character> instanceClass) {
        this.instanceClass = instanceClass;
    }


    public Class<? extends L2Character> getInstanceClass() {
        return instanceClass;
    }
}
