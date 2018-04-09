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
package com.l2jbr.gameserver;

import com.l2jbr.commons.Config;
import com.l2jbr.commons.L2DatabaseFactory;
import com.l2jbr.commons.Server;
import com.l2jbr.commons.status.Status;
import com.l2jbr.gameserver.cache.CrestCache;
import com.l2jbr.gameserver.cache.HtmCache;
import com.l2jbr.gameserver.communitybbs.Manager.ForumsBBSManager;
import com.l2jbr.gameserver.datatables.*;
import com.l2jbr.gameserver.geoeditorcon.GeoEditorListener;
import com.l2jbr.gameserver.handler.*;
import com.l2jbr.gameserver.handler.admincommandhandlers.*;
import com.l2jbr.gameserver.handler.itemhandlers.*;
import com.l2jbr.gameserver.handler.skillhandlers.*;
import com.l2jbr.gameserver.handler.usercommandhandlers.*;
import com.l2jbr.gameserver.handler.voicedcommandhandlers.Wedding;
import com.l2jbr.gameserver.handler.voicedcommandhandlers.stats;
import com.l2jbr.gameserver.idfactory.IdFactory;
import com.l2jbr.gameserver.instancemanager.*;
import com.l2jbr.gameserver.model.*;
import com.l2jbr.gameserver.model.entity.Hero;
import com.l2jbr.gameserver.model.entity.TvTManager;
import com.l2jbr.gameserver.network.L2GameClient;
import com.l2jbr.gameserver.network.L2GamePacketHandler;
import com.l2jbr.gameserver.pathfinding.geonodes.GeoPathFinding;
import com.l2jbr.gameserver.script.ScriptingManager;
import com.l2jbr.gameserver.script.event.ScriptEventManager;
import com.l2jbr.gameserver.status.GameStatus;
import com.l2jbr.gameserver.taskmanager.TaskManager;
import com.l2jbr.gameserver.util.DynamicExtension;
import com.l2jbr.gameserver.util.FloodProtector;
import com.l2jbr.gameserver.util.IPv4Filter;
import com.l2jbr.mmocore.SelectorConfig;
import com.l2jbr.mmocore.SelectorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Locale;

import static com.l2jbr.gameserver.util.GameserverMessages.getMessage;

public class GameServer {
    private static final String ERROR_EXTRACTED_FILE_NOT_FOUND = "error.extracted.file.not.found";
    private static final String ERROR_INITIALIZE_TABLE = "error.initialize.table";
    private static final String INFO_LOADED_HANDLERS = "info.loaded.handlers";
    private static final String LOG4J_CONFIGURATION_FILE = "log4j.configurationFile";

    private static Logger _log;
    private final SelectorThread<L2GameClient> _selectorThread;
    private final SkillTable _skillTable;
    private final ItemTable _itemTable;
    private final NpcTable _npcTable;
    private final HennaTable _hennaTable;
    private final IdFactory _idFactory;
    public static GameServer gameServer;
    private static ClanHallManager _cHManager;
    private final ItemHandler _itemHandler;
    private final SkillHandler _skillHandler;
    private final AdminCommandHandler _adminCommandHandler;
    private final UserCommandHandler _userCommandHandler;
    private final VoicedCommandHandler _voicedCommandHandler;
    private final DoorTable _doorTable;
    private final SevenSigns _sevenSignsEngine;
    private final AutoChatHandler _autoChatHandler;
    private final AutoSpawnHandler _autoSpawnHandler;
    private final LoginServerThread _loginThread;
    private final HelperBuffTable _helperBuffTable;

    private static Status _statusServer;
    @SuppressWarnings("unused")
    private final ThreadPoolManager _threadpools;

    public static final Calendar dateTimeServerStarted = Calendar.getInstance();
    private final HennaTreeTable _hennaTreeTable;

    public long getUsedMemoryMB() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576; // 1024 * 1024 = 1048576;
    }

    public SelectorThread<L2GameClient> getSelectorThread() {
        return _selectorThread;
    }

    public ClanHallManager getCHManager() {
        return _cHManager;
    }

    public GameServer() throws Exception {
        gameServer = this;
        _log.debug(getMessage("debug.used.memory", getUsedMemoryMB()));

        _idFactory = IdFactory.getInstance();
        if (!_idFactory.isInitialized()) {
            _log.error(getMessage("error.read.object.id"));
            throw new Exception(getMessage("error.initialize.id.factory"));
        }

        _threadpools = ThreadPoolManager.getInstance();

        new File(Config.DATAPACK_ROOT, "data/clans").mkdirs();
        new File(Config.DATAPACK_ROOT, "data/crests").mkdirs();
        new File("pathnode").mkdirs();

        // start game time control early
        GameTimeController.getInstance();

        // keep the references of Singletons to prevent garbage collection
        CharNameTable.getInstance();

        _itemTable = ItemTable.getInstance();
        if (!_itemTable.isInitialized()) {
            _log.error(getMessage(ERROR_EXTRACTED_FILE_NOT_FOUND));
            throw new Exception( getMessage(ERROR_INITIALIZE_TABLE, "Item"));
        }

        ExtractableItemsData.getInstance();
        SummonItemsData.getInstance();

        TradeController.getInstance();
        _skillTable = SkillTable.getInstance();
        if (!_skillTable.isInitialized()) {
            _log.error(getMessage(ERROR_EXTRACTED_FILE_NOT_FOUND));
            throw new Exception(getMessage(ERROR_INITIALIZE_TABLE, "Skill"));
        }

        // L2EMU_ADD by Rayan. L2J - BigBro
        // if(Config.ALLOW_NPC_WALKERS)
        NpcWalkerRoutesTable.getInstance().load();
        // L2EMU_ADD by Rayan. L2J - BigBro

        RecipeController.getInstance();

        SkillTreeTable.getInstance();
        ArmorSetsTable.getInstance();
        FishTable.getInstance();
        SkillSpellbookTable.getInstance();
        CharTemplateTable.getInstance();
        NobleSkillTable.getInstance();
        HeroSkillTable.getInstance();

        // Call to load caches
        HtmCache.getInstance();
        CrestCache.getInstance();
        ClanTable.getInstance();
        _npcTable = NpcTable.getInstance();

        if (!_npcTable.isInitialized()) {
            _log.error(getMessage(ERROR_EXTRACTED_FILE_NOT_FOUND));
            throw new Exception(getMessage(ERROR_INITIALIZE_TABLE, "Npc"));
        }

        _hennaTable = HennaTable.getInstance();

        if (!_hennaTable.isInitialized()) {
            throw new Exception(getMessage(ERROR_INITIALIZE_TABLE, "Henna"));
        }

        _hennaTreeTable = HennaTreeTable.getInstance();

        if (!_hennaTreeTable.isInitialized()) {
            throw new Exception(getMessage(ERROR_INITIALIZE_TABLE, "HennaTree"));
        }

        _helperBuffTable = HelperBuffTable.getInstance();

        if (!_helperBuffTable.isInitialized()) {
            throw new Exception(getMessage(ERROR_INITIALIZE_TABLE, "HelperBuff"));
        }

        GeoData.getInstance();
        if (Config.GEODATA == 2) {
            GeoPathFinding.getInstance();
        }

        // Load clan hall data before zone data
        _cHManager = ClanHallManager.getInstance();
        CastleManager.getInstance();
        SiegeManager.getInstance();

        TeleportLocationTable.getInstance();
        LevelUpData.getInstance();
        L2World.getInstance();
        ZoneData.getInstance();
        SpawnTable.getInstance();
        RaidBossSpawnManager.getInstance();
        DayNightSpawnManager.getInstance().notifyChangeMode();
        DimensionalRiftManager.getInstance();
        Announcements.getInstance();
        MapRegionTable.getInstance();
        EventDroplist.getInstance();

        /** Load Manor data */
        L2Manor.getInstance();

        /** Load Manager */
        AuctionManager.getInstance();
        BoatManager.getInstance();
        CastleManorManager.getInstance();
        MercTicketManager.getInstance();
        // PartyCommandManager.getInstance();
        PetitionManager.getInstance();
        ScriptingManager.getInstance();
        QuestManager.getInstance();
        AugmentationData.getInstance();
        if (Config.SAVE_DROPPED_ITEM) {
            ItemsOnGroundManager.getInstance();
        }

        if ((Config.AUTODESTROY_ITEM_AFTER > 0) || (Config.HERB_AUTO_DESTROY_TIME > 0)) {
            ItemsAutoDestroy.getInstance();
        }

        MonsterRace.getInstance();

        _doorTable = DoorTable.getInstance();
        _doorTable.parseData();
        StaticObjects.getInstance();

        _sevenSignsEngine = SevenSigns.getInstance();
        SevenSignsFestival.getInstance();
        _autoSpawnHandler = AutoSpawnHandler.getInstance();
        _autoChatHandler = AutoChatHandler.getInstance();

        // Spawn the Orators/Preachers if in the Seal Validation period.
        _sevenSignsEngine.spawnSevenSignsNPC();

        Olympiad.getInstance();
        Hero.getInstance();
        ScriptEventManager.getInstance();
        // Init of a cursed weapon manager
        CursedWeaponsManager.getInstance();


        _log.info(getMessage(INFO_LOADED_HANDLERS, _autoChatHandler.size(), "AutoChatHandler"));
        _log.info(getMessage(INFO_LOADED_HANDLERS, _autoSpawnHandler.size(), "AutoSpawnHandler"));

        _itemHandler = ItemHandler.getInstance();
        _itemHandler.registerItemHandler(new ScrollOfEscape());
        _itemHandler.registerItemHandler(new ScrollOfResurrection());
        _itemHandler.registerItemHandler(new SoulShots());
        _itemHandler.registerItemHandler(new SpiritShot());
        _itemHandler.registerItemHandler(new BlessedSpiritShot());
        _itemHandler.registerItemHandler(new BeastSoulShot());
        _itemHandler.registerItemHandler(new BeastSpiritShot());
        _itemHandler.registerItemHandler(new ChestKey());
        _itemHandler.registerItemHandler(new PaganKeys());
        _itemHandler.registerItemHandler(new Maps());
        _itemHandler.registerItemHandler(new Potions());
        _itemHandler.registerItemHandler(new Recipes());
        _itemHandler.registerItemHandler(new RollingDice());
        _itemHandler.registerItemHandler(new MysteryPotion());
        _itemHandler.registerItemHandler(new EnchantScrolls());
        _itemHandler.registerItemHandler(new EnergyStone());
        _itemHandler.registerItemHandler(new Book());
        _itemHandler.registerItemHandler(new Remedy());
        _itemHandler.registerItemHandler(new Scrolls());
        _itemHandler.registerItemHandler(new CrystalCarol());
        _itemHandler.registerItemHandler(new SoulCrystals());
        _itemHandler.registerItemHandler(new SevenSignsRecord());
        _itemHandler.registerItemHandler(new CharChangePotions());
        _itemHandler.registerItemHandler(new Firework());
        _itemHandler.registerItemHandler(new Seed());
        _itemHandler.registerItemHandler(new Harvester());
        _itemHandler.registerItemHandler(new MercTicket());
        _itemHandler.registerItemHandler(new FishShots());
        _itemHandler.registerItemHandler(new ExtractableItems());
        _itemHandler.registerItemHandler(new SpecialXMas());
        _itemHandler.registerItemHandler(new SummonItems());
        _itemHandler.registerItemHandler(new BeastSpice());
        _log.info(getMessage(INFO_LOADED_HANDLERS, _itemHandler.size(), "ItemHandler"));

        _skillHandler = SkillHandler.getInstance();
        _skillHandler.registerSkillHandler(new Blow());
        _skillHandler.registerSkillHandler(new Pdam());
        _skillHandler.registerSkillHandler(new Mdam());
        _skillHandler.registerSkillHandler(new CpDam());
        _skillHandler.registerSkillHandler(new Manadam());
        _skillHandler.registerSkillHandler(new Heal());
        _skillHandler.registerSkillHandler(new CombatPointHeal());
        _skillHandler.registerSkillHandler(new ManaHeal());
        _skillHandler.registerSkillHandler(new BalanceLife());
        _skillHandler.registerSkillHandler(new Charge());
        _skillHandler.registerSkillHandler(new Continuous());
        _skillHandler.registerSkillHandler(new Resurrect());
        _skillHandler.registerSkillHandler(new Spoil());
        _skillHandler.registerSkillHandler(new Sweep());
        _skillHandler.registerSkillHandler(new StrSiegeAssault());
        _skillHandler.registerSkillHandler(new SummonFriend());
        _skillHandler.registerSkillHandler(new SummonTreasureKey());
        _skillHandler.registerSkillHandler(new Disablers());
        _skillHandler.registerSkillHandler(new Recall());
        _skillHandler.registerSkillHandler(new SiegeFlag());
        _skillHandler.registerSkillHandler(new TakeCastle());
        _skillHandler.registerSkillHandler(new Unlock());
        _skillHandler.registerSkillHandler(new DrainSoul());
        _skillHandler.registerSkillHandler(new Craft());
        _skillHandler.registerSkillHandler(new Fishing());
        _skillHandler.registerSkillHandler(new FishingSkill());
        _skillHandler.registerSkillHandler(new BeastFeed());
        _skillHandler.registerSkillHandler(new DeluxeKey());
        _skillHandler.registerSkillHandler(new Sow());
        _skillHandler.registerSkillHandler(new Harvest());
        _skillHandler.registerSkillHandler(new Signets());
        _skillHandler.registerSkillHandler(new GetPlayer());
        _log.info(getMessage(INFO_LOADED_HANDLERS, _skillHandler.size(), "SkillHandler"));

        _adminCommandHandler = AdminCommandHandler.getInstance();
        _adminCommandHandler.registerAdminCommandHandler(new AdminAdmin());
        _adminCommandHandler.registerAdminCommandHandler(new AdminInvul());
        _adminCommandHandler.registerAdminCommandHandler(new AdminDelete());
        _adminCommandHandler.registerAdminCommandHandler(new AdminKill());
        _adminCommandHandler.registerAdminCommandHandler(new AdminTarget());
        _adminCommandHandler.registerAdminCommandHandler(new AdminShop());
        _adminCommandHandler.registerAdminCommandHandler(new AdminAnnouncements());
        _adminCommandHandler.registerAdminCommandHandler(new AdminCreateItem());
        _adminCommandHandler.registerAdminCommandHandler(new AdminHeal());
        _adminCommandHandler.registerAdminCommandHandler(new AdminHelpPage());
        _adminCommandHandler.registerAdminCommandHandler(new AdminShutdown());
        _adminCommandHandler.registerAdminCommandHandler(new AdminSpawn());
        _adminCommandHandler.registerAdminCommandHandler(new AdminSkill());
        _adminCommandHandler.registerAdminCommandHandler(new AdminExpSp());
        _adminCommandHandler.registerAdminCommandHandler(new AdminEventEngine());
        _adminCommandHandler.registerAdminCommandHandler(new AdminGmChat());
        _adminCommandHandler.registerAdminCommandHandler(new AdminEditChar());
        _adminCommandHandler.registerAdminCommandHandler(new AdminGm());
        _adminCommandHandler.registerAdminCommandHandler(new AdminTeleport());
        _adminCommandHandler.registerAdminCommandHandler(new AdminRepairChar());
        _adminCommandHandler.registerAdminCommandHandler(new AdminChangeAccessLevel());
        _adminCommandHandler.registerAdminCommandHandler(new AdminBan());
        _adminCommandHandler.registerAdminCommandHandler(new AdminPolymorph());
        _adminCommandHandler.registerAdminCommandHandler(new AdminBanChat());
        _adminCommandHandler.registerAdminCommandHandler(new AdminKick());
        _adminCommandHandler.registerAdminCommandHandler(new AdminMonsterRace());
        _adminCommandHandler.registerAdminCommandHandler(new AdminEditNpc());
        _adminCommandHandler.registerAdminCommandHandler(new AdminFightCalculator());
        _adminCommandHandler.registerAdminCommandHandler(new AdminMenu());
        _adminCommandHandler.registerAdminCommandHandler(new AdminSiege());
        _adminCommandHandler.registerAdminCommandHandler(new AdminPathNode());
        _adminCommandHandler.registerAdminCommandHandler(new AdminPetition());
        _adminCommandHandler.registerAdminCommandHandler(new AdminPForge());
        _adminCommandHandler.registerAdminCommandHandler(new AdminBBS());
        _adminCommandHandler.registerAdminCommandHandler(new AdminEffects());
        _adminCommandHandler.registerAdminCommandHandler(new AdminDoorControl());
        _adminCommandHandler.registerAdminCommandHandler(new AdminTest());
        _adminCommandHandler.registerAdminCommandHandler(new AdminEnchant());
        _adminCommandHandler.registerAdminCommandHandler(new AdminMobGroup());
        _adminCommandHandler.registerAdminCommandHandler(new AdminRes());
        _adminCommandHandler.registerAdminCommandHandler(new AdminMammon());
        _adminCommandHandler.registerAdminCommandHandler(new AdminUnblockIp());
        _adminCommandHandler.registerAdminCommandHandler(new AdminPledge());
        _adminCommandHandler.registerAdminCommandHandler(new AdminRideWyvern());
        _adminCommandHandler.registerAdminCommandHandler(new AdminLogin());
        _adminCommandHandler.registerAdminCommandHandler(new AdminCache());
        _adminCommandHandler.registerAdminCommandHandler(new AdminLevel());
        _adminCommandHandler.registerAdminCommandHandler(new AdminQuest());
        _adminCommandHandler.registerAdminCommandHandler(new AdminZone());
        _adminCommandHandler.registerAdminCommandHandler(new AdminCursedWeapons());
        _adminCommandHandler.registerAdminCommandHandler(new AdminGeodata());
        _adminCommandHandler.registerAdminCommandHandler(new AdminGeoEditor());
        _adminCommandHandler.registerAdminCommandHandler(new AdminManor());
        _adminCommandHandler.registerAdminCommandHandler(new AdminTvTEvent());
        // _adminCommandHandler.registerAdminCommandHandler(new AdminRadar());
        _log.info(getMessage(INFO_LOADED_HANDLERS, _adminCommandHandler.size(), "AdminCommandHandler"));


        _userCommandHandler = UserCommandHandler.getInstance();
        _userCommandHandler.registerUserCommandHandler(new ClanPenalty());
        _userCommandHandler.registerUserCommandHandler(new ClanWarsList());
        _userCommandHandler.registerUserCommandHandler(new DisMount());
        _userCommandHandler.registerUserCommandHandler(new Escape());
        _userCommandHandler.registerUserCommandHandler(new Loc());
        _userCommandHandler.registerUserCommandHandler(new Mount());
        _userCommandHandler.registerUserCommandHandler(new PartyInfo());
        _userCommandHandler.registerUserCommandHandler(new Time());
        _userCommandHandler.registerUserCommandHandler(new OlympiadStat());
        _userCommandHandler.registerUserCommandHandler(new ChannelLeave());
        _userCommandHandler.registerUserCommandHandler(new ChannelDelete());
        _userCommandHandler.registerUserCommandHandler(new ChannelListUpdate());
        _log.info(getMessage(INFO_LOADED_HANDLERS, _userCommandHandler.size(), "UserCommandHandler"));

        _voicedCommandHandler = VoicedCommandHandler.getInstance();
        _voicedCommandHandler.registerVoicedCommandHandler(new stats());

        if (Config.L2JMOD_ALLOW_WEDDING) {
            _voicedCommandHandler.registerVoicedCommandHandler(new Wedding());
        }

        _log.info(getMessage(INFO_LOADED_HANDLERS, _voicedCommandHandler.size(),  "VoicedCommandHandler"));

        if (Config.L2JMOD_ALLOW_WEDDING) {
            CoupleManager.getInstance();
        }

        TaskManager.getInstance();

        GmListTable.getInstance();

        // read pet stats from db
        L2PetDataTable.getInstance().loadPetsData();

        Universe.getInstance();

        if (Config.ACCEPT_GEOEDITOR_CONN) {
            GeoEditorListener.getInstance();
        }


        Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

        try {
            _doorTable.getDoor(24190001).openMe();
            _doorTable.getDoor(24190002).openMe();
            _doorTable.getDoor(24190003).openMe();
            _doorTable.getDoor(24190004).openMe();
            _doorTable.getDoor(23180001).openMe();
            _doorTable.getDoor(23180002).openMe();
            _doorTable.getDoor(23180003).openMe();
            _doorTable.getDoor(23180004).openMe();
            _doorTable.getDoor(23180005).openMe();
            _doorTable.getDoor(23180006).openMe();

            _doorTable.checkAutoOpen();
        } catch (NullPointerException e) {
            _log.warn(getMessage("error.door.file"));
            if (Config.DEBUG) {
                e.printStackTrace();
            }
        }
        ForumsBBSManager.getInstance();

        _log.info(getMessage("info.object.id.free", IdFactory.getInstance().size()));

        // initialize the dynamic extension loader
        try {
            DynamicExtension.getInstance();
        } catch (Exception ex) {
            _log.warn( getMessage("error.dynamic.extension.not.loaded"), ex);
        }

        FloodProtector.getInstance();
        TvTManager.getInstance();

        // maxMemory is the upper limit the jvm can use, totalMemory the size of the current allocation pool, freeMemory the unused memory in the allocation pool
        long freeMem = ((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) + Runtime.getRuntime().freeMemory()) / 1048576; // 1024 * 1024 = 1048576;
        long totalMem = Runtime.getRuntime().maxMemory() / 1048576;

        _log.info(getMessage("info.free.memory", freeMem, totalMem));

        _loginThread = LoginServerThread.getInstance();
        _loginThread.start();

        // TODO: Unhardcode this configuration options
        final SelectorConfig sc = new SelectorConfig();
        sc.MAX_READ_PER_PASS = 12; // Config.MMO_MAX_READ_PER_PASS;
        sc.MAX_SEND_PER_PASS = 12; // Config.MMO_MAX_SEND_PER_PASS;
        sc.SLEEP_TIME = 20; // Config.MMO_SELECTOR_SLEEP_TIME;
        sc.HELPER_BUFFER_COUNT = 20; // Config.MMO_HELPER_BUFFER_COUNT;
        sc.TCP_NODELAY = false; // Config.MMO_TCP_NODELAY;

        L2GamePacketHandler gph = new L2GamePacketHandler();
        _selectorThread = new SelectorThread<>(sc, gph, gph, gph, new IPv4Filter());
        InetAddress bindAddress = null;
        if (!Config.GAMESERVER_HOSTNAME.equals("*")) {
            try {
                bindAddress = InetAddress.getByName(Config.GAMESERVER_HOSTNAME);
            } catch (UnknownHostException e1) {
                _log.error( getMessage("error.invalid.bind.address",  e1.getMessage()), e1);
            }
        }

        try {
            _selectorThread.openServerSocket(bindAddress, Config.PORT_GAME);
        } catch (IOException e) {
            ;
            _log.error( getMessage("error.open.socket", e.getMessage()), e);
            System.exit(1);
        }

        _selectorThread.start();

        _log.info(getMessage("info.max.connected.players", Config.MAXIMUM_ONLINE_USERS));
    }

    public static void main(String[] args) throws Exception {
        configureLogger();
        Server.serverMode = Server.MODE_GAMESERVER;

        // Initialize config
        Config.load();

        Locale.setDefault(Locale.forLanguageTag(Config.LANGUAGE));

        L2DatabaseFactory.getInstance();
        gameServer = new GameServer();

        if (Config.IS_TELNET_ENABLED) {
            _statusServer = new GameStatus();
            _statusServer.start();
        } else {
            System.out.println(getMessage("info.telnet.disabled"));
        }
    }

    private static void configureLogger() {
        String logConfigurationFile = System.getProperty(LOG4J_CONFIGURATION_FILE);
        if(logConfigurationFile == null || logConfigurationFile.isEmpty()) {
            System.setProperty(LOG4J_CONFIGURATION_FILE, "log4j.xml");
        }
        _log = LoggerFactory.getLogger(GameServer.class);
    }
}
