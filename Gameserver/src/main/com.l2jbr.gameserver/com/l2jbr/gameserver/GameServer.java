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
import com.l2jbr.commons.Server;
import com.l2jbr.commons.status.Status;
import com.l2jbr.gameserver.cache.CrestCache;
import com.l2jbr.gameserver.cache.HtmCache;
import com.l2jbr.gameserver.communitybbs.Manager.ForumsBBSManager;
import com.l2jbr.gameserver.datatables.*;
import com.l2jbr.gameserver.geoeditorcon.GeoEditorListener;
import com.l2jbr.gameserver.handler.*;
import com.l2jbr.gameserver.idfactory.IdFactory;
import com.l2jbr.gameserver.instancemanager.*;
import com.l2jbr.gameserver.model.*;
import com.l2jbr.gameserver.model.entity.Heroes;
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
import org.l2j.mmocore.ConnectionBuilder;
import org.l2j.mmocore.ConnectionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.Locale;

import static com.l2jbr.gameserver.util.GameserverMessages.getMessage;
import static java.util.Objects.nonNull;

public class GameServer {

    private static final String INFO_LOADED_HANDLERS = "info.loaded.handlers";
    private static final String LOG4J_CONFIGURATION_FILE = "log4j.configurationFile";

    private static Logger _log;
    private static GameServer gameServer;

    public static final Calendar dateTimeServerStarted = Calendar.getInstance();
    private final ConnectionHandler<L2GameClient> connectionHandler;

    public static void shutdown() {
        if(nonNull(gameServer)) {
            gameServer.connectionHandler.shutdown();
            gameServer.connectionHandler.setDaemon(true);
        }
    }

    private long getUsedMemoryMB() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576; // 1024 * 1024 = 1048576;
    }

    public GameServer() throws Exception {
        gameServer = this;
        _log.debug(getMessage("debug.used.memory", getUsedMemoryMB()));

        makeDataDirectories();

        ThreadPoolManager.getInstance();
        IdFactory.getInstance();

        // start game time control early
        GameTimeController.getInstance();

        CharTemplateTable.getInstance();

        SkillTable.getInstance();
        SkillTreeTable.getInstance();
        NobleSkillTable.getInstance();
        HeroSkillTable.getInstance();

        ItemTable.getInstance();
        SkillSpellbookTable.getInstance();
        ArmorSetsTable.getInstance();
        RecipeController.getInstance();
        ExtractableItemsData.getInstance();
        SummonItemsData.getInstance();
        HennaTable.getInstance();
        HennaTreeTable.getInstance();
        L2Manor.getInstance();

        NpcTable.getInstance();
        NpcWalkerRoutesTable.getInstance();
        HelperBuffTable.getInstance();
        TradeController.getInstance();
        FishTable.getInstance();

        HtmCache.getInstance();
        CrestCache.getInstance();

        ClanTable.getInstance();

        ClanHallManager.getInstance(); // Load clan hall data before zone data
        ZoneData.getInstance();

        CastleManager.getInstance();
        SiegeManager.getInstance();

        TeleportLocationTable.getInstance();

        L2World.getInstance();

        DayNightSpawnManager.getInstance().notifyChangeMode();
        SpawnTable.getInstance();
        RaidBossSpawnManager.getInstance();
        DimensionalRiftManager.getInstance();

        Announcements.getInstance();

        MapRegionTable.getInstance();

        GeoData.getInstance();
        if (Config.GEODATA == 2) {
            GeoPathFinding.getInstance();
        }

        AuctionManager.getInstance();
        BoatManager.getInstance();
        CastleManorManager.getInstance();
        MercTicketManager.getInstance();
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

        DoorTable.getInstance();
        StaticObjects.getInstance();

        SevenSigns _sevenSignsEngine = SevenSigns.getInstance();
        SevenSignsFestival.getInstance();
        AutoSpawnHandler.getInstance();
        AutoChatHandler.getInstance();

        // Spawn the Orators/Preachers if in the Seal Validation period.
        _sevenSignsEngine.spawnSevenSignsNPC();

        Olympiad.getInstance();
        Heroes.getInstance();
        ScriptEventManager.getInstance();
        // Init of a cursed weapon manager
        CursedWeaponsManager.getInstance();

        ItemHandler _itemHandler = ItemHandler.getInstance();
        _log.info(getMessage(INFO_LOADED_HANDLERS, _itemHandler.size(), "ItemHandler"));

        SkillHandler _skillHandler = SkillHandler.getInstance();
        _log.info(getMessage(INFO_LOADED_HANDLERS, _skillHandler.size(), "SkillHandler"));

        AdminCommandHandler _adminCommandHandler = AdminCommandHandler.getInstance();
        _log.info(getMessage(INFO_LOADED_HANDLERS, _adminCommandHandler.size(), "AdminCommandHandler"));

        UserCommandHandler _userCommandHandler = UserCommandHandler.getInstance();
        _log.info(getMessage(INFO_LOADED_HANDLERS, _userCommandHandler.size(), "UserCommandHandler"));

        VoicedCommandHandler _voicedCommandHandler = VoicedCommandHandler.getInstance();
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

        LoginServerThread _loginThread = LoginServerThread.getInstance();
        _loginThread.start();

        L2GamePacketHandler gph = new L2GamePacketHandler();

        InetSocketAddress bindAddress;
        if (!Config.GAMESERVER_HOSTNAME.equals("*")) {
            bindAddress =  new InetSocketAddress(Config.GAMESERVER_HOSTNAME, Config.PORT_GAME);
        } else {
            bindAddress = new InetSocketAddress(Config.PORT_GAME);
        }

        connectionHandler = ConnectionBuilder.create(bindAddress, gph,gph,gph).filter(new IPv4Filter()).build();
        connectionHandler.start();

        _log.info(getMessage("info.max.connected.players", Config.MAXIMUM_ONLINE_USERS));
    }

    private void makeDataDirectories() {
        new File(Config.DATAPACK_ROOT, "data/clans").mkdirs();
        new File(Config.DATAPACK_ROOT, "data/crests").mkdirs();
        new File("pathnode").mkdirs();
    }

    public static void main(String[] args) throws Exception {
        Server.serverMode = Server.MODE_GAMESERVER;
        configureLogger();
        Config.load();
        Locale.setDefault(Locale.forLanguageTag(Config.LANGUAGE));

        gameServer = new GameServer();

        if (Config.IS_TELNET_ENABLED) {
            Status _statusServer = new GameStatus();
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
