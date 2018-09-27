/* This program is free software; you can redistribute it and/or modify
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
import com.l2jbr.gameserver.datatables.MapRegionTable;
import com.l2jbr.gameserver.instancemanager.CastleManager;
import com.l2jbr.gameserver.model.AutoChatHandler;
import com.l2jbr.gameserver.model.AutoSpawnHandler;
import com.l2jbr.gameserver.model.AutoSpawnHandler.AutoSpawnInstance;
import com.l2jbr.gameserver.model.L2World;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.SevenSignsPlayer;
import com.l2jbr.gameserver.model.entity.database.SevenSignsStatus;
import com.l2jbr.gameserver.model.entity.database.repository.SevenSignsRepository;
import com.l2jbr.gameserver.model.entity.database.repository.SevenSignsStatusRepository;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.SignsSky;
import com.l2jbr.gameserver.serverpackets.SystemMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.l2jbr.commons.database.DatabaseAccess.getRepository;
import static java.util.Objects.isNull;

/**
 * Seven Signs Engine
 *
 * @author Tempy
 */
public class SevenSigns {
    protected static final Logger _log = LoggerFactory.getLogger(SevenSigns.class.getName());
    private static SevenSigns _instance;

    // Basic Seven Signs Constants \\
    public static final String SEVEN_SIGNS_DATA_FILE = "config/signs.properties";
    public static final String SEVEN_SIGNS_HTML_PATH = "data/html/seven_signs/";

    public static final int CABAL_NULL = 0;
    public static final int CABAL_DUSK = 1;
    public static final int CABAL_DAWN = 2;

    public static final int SEAL_NULL = 0;
    public static final int SEAL_AVARICE = 1;
    public static final int SEAL_GNOSIS = 2;
    public static final int SEAL_STRIFE = 3;

    public static final int PERIOD_COMP_RECRUITING = 0;
    public static final int PERIOD_COMPETITION = 1;
    public static final int PERIOD_COMP_RESULTS = 2;
    public static final int PERIOD_SEAL_VALIDATION = 3;

    public static final int PERIOD_START_HOUR = 18;
    public static final int PERIOD_START_MINS = 00;
    public static final int PERIOD_START_DAY = Calendar.MONDAY;

    // The quest event and seal validation periods last for approximately one week
    // with a 15 minutes "interval" period sandwiched between them.
    public static final int PERIOD_MINOR_LENGTH = 900000;
    public static final int PERIOD_MAJOR_LENGTH = 604800000 - PERIOD_MINOR_LENGTH;

    public static final int ANCIENT_ADENA_ID = 5575;
    public static final int RECORD_SEVEN_SIGNS_ID = 5707;
    public static final int CERTIFICATE_OF_APPROVAL_ID = 6388;
    public static final int RECORD_SEVEN_SIGNS_COST = 500;
    public static final int ADENA_JOIN_DAWN_COST = 50000;

    // NPC Related Constants \\
    public static final int ORATOR_NPC_ID = 31094;
    public static final int PREACHER_NPC_ID = 31093;
    public static final int MAMMON_MERCHANT_ID = 31113;
    public static final int MAMMON_BLACKSMITH_ID = 31126;
    public static final int MAMMON_MARKETEER_ID = 31092;
    public static final int SPIRIT_IN_ID = 31111;
    public static final int SPIRIT_OUT_ID = 31112;
    public static final int LILITH_NPC_ID = 25283;
    public static final int ANAKIM_NPC_ID = 25286;
    public static final int CREST_OF_DAWN_ID = 31170;
    public static final int CREST_OF_DUSK_ID = 31171;
    // Seal Stone Related Constants \\
    public static final int SEAL_STONE_BLUE_ID = 6360;
    public static final int SEAL_STONE_GREEN_ID = 6361;
    public static final int SEAL_STONE_RED_ID = 6362;

    public static final int SEAL_STONE_BLUE_VALUE = 3;
    public static final int SEAL_STONE_GREEN_VALUE = 5;
    public static final int SEAL_STONE_RED_VALUE = 10;

    public static final int BLUE_CONTRIB_POINTS = 3;
    public static final int GREEN_CONTRIB_POINTS = 5;
    public static final int RED_CONTRIB_POINTS = 10;

    private final Calendar _calendar = Calendar.getInstance();

    protected int _compWinner;

    private final Map<Integer, SevenSignsPlayer> _signsPlayerData;

    private static AutoSpawnInstance _merchantSpawn;
    private static AutoSpawnInstance _blacksmithSpawn;
    private static AutoSpawnInstance _spiritInSpawn;
    private static AutoSpawnInstance _spiritOutSpawn;
    private static AutoSpawnInstance _lilithSpawn;
    private static AutoSpawnInstance _anakimSpawn;
    private static AutoSpawnInstance _crestofdawnspawn;
    private static AutoSpawnInstance _crestofduskspawn;
    private static Map<Integer, AutoSpawnInstance> _oratorSpawns;
    private static Map<Integer, AutoSpawnInstance> _preacherSpawns;
    private static Map<Integer, AutoSpawnInstance> _marketeerSpawns;
    private SevenSignsStatus status;

    public SevenSigns() {
        _signsPlayerData = new HashMap<>();

        try {
            restoreSevenSignsData();
        } catch (Exception e) {
            _log.error("SevenSigns: Failed to load configuration: " + e);
        }

        _log.info("SevenSigns: Currently in the " + getCurrentPeriodName() + " period!");
        initializeSeals();

        if (isSealValidationPeriod()) {
            if (getCabalHighestScore() == CABAL_NULL) {
                _log.info("SevenSigns: The competition ended with a tie last week.");
            } else {
                _log.info("SevenSigns: The " + getCabalName(getCabalHighestScore()) + " were victorious last week.");
            }
        } else if (getCabalHighestScore() == CABAL_NULL) {
            _log.info("SevenSigns: The competition, if the current trend continues, will end in a tie this week.");
        } else {
            _log.info("SevenSigns: The " + getCabalName(getCabalHighestScore()) + " are in the lead this week.");
        }

        synchronized (this) {
            setCalendarForNextPeriodChange();
            long milliToChange = getMilliToPeriodChange();

            // Schedule a time for the next period change.
            SevenSignsPeriodChange sspc = new SevenSignsPeriodChange();
            ThreadPoolManager.getInstance().scheduleGeneral(sspc, milliToChange);

            // Thanks to http://rainbow.arch.scriptmania.com/scripts/timezone_countdown.html for help with this.
            double numSecs = (milliToChange / 1000.0) % 60;
            double countDown = ((milliToChange / 1000.0) - numSecs) / 60;
            int numMins = (int) Math.floor(countDown % 60);
            countDown = (countDown - numMins) / 60;
            int numHours = (int) Math.floor(countDown % 24);
            int numDays = (int) Math.floor((countDown - numHours) / 24);

            _log.info("SevenSigns: Next period begins in " + numDays + " days, " + numHours + " hours and " + numMins + " mins.");
        }
    }

    /**
     * Registers all random spawns and auto-chats for Seven Signs NPCs, along with spawns for the Preachers of Doom and Orators of Revelations at the beginning of the Seal Validation period.
     */
    public void spawnSevenSignsNPC() {
        var autoSpawnHandler = AutoSpawnHandler.getInstance();
        _merchantSpawn = autoSpawnHandler.getAutoSpawnInstance(MAMMON_MERCHANT_ID, false);
        _blacksmithSpawn = autoSpawnHandler.getAutoSpawnInstance(MAMMON_BLACKSMITH_ID, false);
        _marketeerSpawns = autoSpawnHandler.getAutoSpawnInstances(MAMMON_MARKETEER_ID);
        _spiritInSpawn = autoSpawnHandler.getAutoSpawnInstance(SPIRIT_IN_ID, false);
        _spiritOutSpawn = autoSpawnHandler.getAutoSpawnInstance(SPIRIT_OUT_ID, false);
        _lilithSpawn = autoSpawnHandler.getAutoSpawnInstance(LILITH_NPC_ID, false);
        _anakimSpawn = autoSpawnHandler.getAutoSpawnInstance(ANAKIM_NPC_ID, false);
        _crestofdawnspawn = autoSpawnHandler.getAutoSpawnInstance(CREST_OF_DAWN_ID, false);
        _crestofduskspawn = autoSpawnHandler.getAutoSpawnInstance(CREST_OF_DUSK_ID, false);
        _oratorSpawns = autoSpawnHandler.getAutoSpawnInstances(ORATOR_NPC_ID);
        _preacherSpawns = autoSpawnHandler.getAutoSpawnInstances(PREACHER_NPC_ID);

        if (isSealValidationPeriod() || isCompResultsPeriod()) {
            for (AutoSpawnInstance spawnInst : _marketeerSpawns.values()) {
                autoSpawnHandler.setSpawnActive(spawnInst, true);
            }

            if ((getSealOwner(SEAL_GNOSIS) == getCabalHighestScore()) && (getSealOwner(SEAL_GNOSIS) != CABAL_NULL)) {
                if (!Config.ANNOUNCE_MAMMON_SPAWN) {
                    _blacksmithSpawn.setBroadcast(false);
                }

                if (!autoSpawnHandler.getAutoSpawnInstance(_blacksmithSpawn.getObjectId(), true).isSpawnActive()) {
                    autoSpawnHandler.setSpawnActive(_blacksmithSpawn, true);
                }

                for (AutoSpawnInstance spawnInst : _oratorSpawns.values()) {
                    if (!autoSpawnHandler.getAutoSpawnInstance(spawnInst.getObjectId(), true).isSpawnActive()) {
                        autoSpawnHandler.setSpawnActive(spawnInst, true);
                    }
                }

                for (AutoSpawnInstance spawnInst : _preacherSpawns.values()) {
                    if (!autoSpawnHandler.getAutoSpawnInstance(spawnInst.getObjectId(), true).isSpawnActive()) {
                        autoSpawnHandler.setSpawnActive(spawnInst, true);
                    }
                }

                if (!AutoChatHandler.getInstance().getAutoChatInstance(PREACHER_NPC_ID, false).isActive() && !AutoChatHandler.getInstance().getAutoChatInstance(ORATOR_NPC_ID, false).isActive()) {
                    AutoChatHandler.getInstance().setAutoChatActive(true);
                }
            } else {
                autoSpawnHandler.setSpawnActive(_blacksmithSpawn, false);

                for (AutoSpawnInstance spawnInst : _oratorSpawns.values()) {
                    autoSpawnHandler.setSpawnActive(spawnInst, false);
                }

                for (AutoSpawnInstance spawnInst : _preacherSpawns.values()) {
                    autoSpawnHandler.setSpawnActive(spawnInst, false);
                }

                AutoChatHandler.getInstance().setAutoChatActive(false);
            }

            if ((getSealOwner(SEAL_AVARICE) == getCabalHighestScore()) && (getSealOwner(SEAL_AVARICE) != CABAL_NULL)) {
                if (!Config.ANNOUNCE_MAMMON_SPAWN) {
                    _merchantSpawn.setBroadcast(false);
                }

                if (!autoSpawnHandler.getAutoSpawnInstance(_merchantSpawn.getObjectId(), true).isSpawnActive()) {
                    autoSpawnHandler.setSpawnActive(_merchantSpawn, true);
                }

                if (!autoSpawnHandler.getAutoSpawnInstance(_spiritInSpawn.getObjectId(), true).isSpawnActive()) {
                    autoSpawnHandler.setSpawnActive(_spiritInSpawn, true);
                }

                if (!autoSpawnHandler.getAutoSpawnInstance(_spiritOutSpawn.getObjectId(), true).isSpawnActive()) {
                    autoSpawnHandler.setSpawnActive(_spiritOutSpawn, true);
                }

                switch (getCabalHighestScore()) {
                    case CABAL_DAWN:
                        if (!autoSpawnHandler.getAutoSpawnInstance(_lilithSpawn.getObjectId(), true).isSpawnActive()) {
                            autoSpawnHandler.setSpawnActive(_lilithSpawn, true);
                        }

                        autoSpawnHandler.setSpawnActive(_anakimSpawn, false);
                        if (!autoSpawnHandler.getAutoSpawnInstance(_crestofdawnspawn.getObjectId(), true).isSpawnActive()) {
                            autoSpawnHandler.setSpawnActive(_crestofdawnspawn, true);
                        }

                        autoSpawnHandler.setSpawnActive(_crestofduskspawn, false);
                        break;

                    case CABAL_DUSK:
                        if (!autoSpawnHandler.getAutoSpawnInstance(_anakimSpawn.getObjectId(), true).isSpawnActive()) {
                            autoSpawnHandler.setSpawnActive(_anakimSpawn, true);
                        }

                        autoSpawnHandler.setSpawnActive(_lilithSpawn, false);
                        if (!autoSpawnHandler.getAutoSpawnInstance(_crestofduskspawn.getObjectId(), true).isSpawnActive()) {
                            autoSpawnHandler.setSpawnActive(_crestofduskspawn, true);
                        }

                        autoSpawnHandler.setSpawnActive(_crestofdawnspawn, false);
                        break;
                }
            } else {
                autoSpawnHandler.setSpawnActive(_merchantSpawn, false);
                autoSpawnHandler.setSpawnActive(_lilithSpawn, false);
                autoSpawnHandler.setSpawnActive(_anakimSpawn, false);
                autoSpawnHandler.setSpawnActive(_crestofdawnspawn, false);
                autoSpawnHandler.setSpawnActive(_crestofduskspawn, false);
                autoSpawnHandler.setSpawnActive(_spiritInSpawn, false);
                autoSpawnHandler.setSpawnActive(_spiritOutSpawn, false);
            }
        } else {
            autoSpawnHandler.setSpawnActive(_merchantSpawn, false);
            autoSpawnHandler.setSpawnActive(_blacksmithSpawn, false);
            autoSpawnHandler.setSpawnActive(_lilithSpawn, false);
            autoSpawnHandler.setSpawnActive(_anakimSpawn, false);
            autoSpawnHandler.setSpawnActive(_crestofdawnspawn, false);
            autoSpawnHandler.setSpawnActive(_crestofduskspawn, false);
            autoSpawnHandler.setSpawnActive(_spiritInSpawn, false);
            autoSpawnHandler.setSpawnActive(_spiritOutSpawn, false);

            for (AutoSpawnInstance spawnInst : _oratorSpawns.values()) {
                autoSpawnHandler.setSpawnActive(spawnInst, false);
            }

            for (AutoSpawnInstance spawnInst : _preacherSpawns.values()) {
                autoSpawnHandler.setSpawnActive(spawnInst, false);
            }

            for (AutoSpawnInstance spawnInst : _marketeerSpawns.values()) {
                autoSpawnHandler.setSpawnActive(spawnInst, false);
            }

            AutoChatHandler.getInstance().setAutoChatActive(false);
        }
    }

    public static SevenSigns getInstance() {
        if (isNull(_instance)) {
            _instance = new SevenSigns();
        }
        return _instance;
    }

    public static int calcContributionScore(int blueCount, int greenCount, int redCount) {
        int contrib = blueCount * BLUE_CONTRIB_POINTS;
        contrib += greenCount * GREEN_CONTRIB_POINTS;
        contrib += redCount * RED_CONTRIB_POINTS;

        return contrib;
    }

    public static int calcAncientAdenaReward(int blueCount, int greenCount, int redCount) {
        int reward = blueCount * SEAL_STONE_BLUE_VALUE;
        reward += greenCount * SEAL_STONE_GREEN_VALUE;
        reward += redCount * SEAL_STONE_RED_VALUE;

        return reward;
    }

    public static final String getCabalShortName(int cabal) {
        switch (cabal) {
            case CABAL_DAWN:
                return "dawn";
            case CABAL_DUSK:
                return "dusk";
        }

        return "No Cabal";
    }

    public static final String getCabalName(int cabal) {
        switch (cabal) {
            case CABAL_DAWN:
                return "Lords of Dawn";
            case CABAL_DUSK:
                return "Revolutionaries of Dusk";
        }

        return "No Cabal";
    }

    public static final String getSealName(int seal, boolean shortName) {
        String sealName = (!shortName) ? "Seal of " : "";

        switch (seal) {
            case SEAL_AVARICE:
                sealName += "Avarice";
                break;
            case SEAL_GNOSIS:
                sealName += "Gnosis";
                break;
            case SEAL_STRIFE:
                sealName += "Strife";
                break;
        }

        return sealName;
    }

    public final int getCurrentCycle() {
        return status.getCurrentCycle();
    }

    public final int getCurrentPeriod() {
        return status.getActivePeriod();
    }

    private final int getDaysToPeriodChange() {
        int numDays = _calendar.get(Calendar.DAY_OF_WEEK) - PERIOD_START_DAY;

        if (numDays < 0) {
            return 0 - numDays;
        }

        return 7 - numDays;
    }

    public final long getMilliToPeriodChange() {
        long currTimeMillis = System.currentTimeMillis();
        long changeTimeMillis = _calendar.getTimeInMillis();

        return (changeTimeMillis - currTimeMillis);
    }

    protected void setCalendarForNextPeriodChange() {
        // Calculate the number of days until the next period
        // A period starts at 18:00 pm (local time), like on official servers.
        switch (getCurrentPeriod()) {
            case PERIOD_SEAL_VALIDATION:
            case PERIOD_COMPETITION:
                int daysToChange = getDaysToPeriodChange();

                if (daysToChange == 7) {
                    if (_calendar.get(Calendar.HOUR_OF_DAY) < PERIOD_START_HOUR) {
                        daysToChange = 0;
                    } else if ((_calendar.get(Calendar.HOUR_OF_DAY) == PERIOD_START_HOUR) && (_calendar.get(Calendar.MINUTE) < PERIOD_START_MINS)) {
                        daysToChange = 0;
                    }
                }

                // Otherwise...
                if (daysToChange > 0) {
                    _calendar.add(Calendar.DATE, daysToChange);
                }

                _calendar.set(Calendar.HOUR_OF_DAY, PERIOD_START_HOUR);
                _calendar.set(Calendar.MINUTE, PERIOD_START_MINS);
                break;
            case PERIOD_COMP_RECRUITING:
            case PERIOD_COMP_RESULTS:
                _calendar.add(Calendar.MILLISECOND, PERIOD_MINOR_LENGTH);
                break;
        }
    }

    public final String getCurrentPeriodName() {
        String periodName = null;

        switch (status.getActivePeriod()) {
            case PERIOD_COMP_RECRUITING:
                periodName = "Quest Event Initialization";
                break;
            case PERIOD_COMPETITION:
                periodName = "Competition (Quest Event)";
                break;
            case PERIOD_COMP_RESULTS:
                periodName = "Quest Event Results";
                break;
            case PERIOD_SEAL_VALIDATION:
                periodName = "Seal Validation";
                break;
        }

        return periodName;
    }

    public final boolean isSealValidationPeriod() {
        return (status.getActivePeriod() == PERIOD_SEAL_VALIDATION);
    }

    public final boolean isCompResultsPeriod() {
        return (status.getActivePeriod() == PERIOD_COMP_RESULTS);
    }

    public final int getCurrentScore(int cabal) {
        double totalStoneScore = status.getDawnStoneScore() + status.getDuskStoneScore();

        switch (cabal) {
            case CABAL_NULL:
                return 0;
            case CABAL_DAWN:
                return Math.round((float) (status.getDawnStoneScore() / ((float) totalStoneScore == 0 ? 1 : totalStoneScore)) * 500) + status.getDawnFestivalScore();
            case CABAL_DUSK:
                return Math.round((float) (status.getDuskStoneScore() / ((float) totalStoneScore == 0 ? 1 : totalStoneScore)) * 500) + status.getDuskFestivalScore();
        }

        return 0;
    }

    public final double getCurrentStoneScore(int cabal) {
        switch (cabal) {
            case CABAL_NULL:
                return 0;
            case CABAL_DAWN:
                return status.getDawnStoneScore();
            case CABAL_DUSK:
                return status.getDuskStoneScore();
        }

        return 0;
    }

    public final int getCurrentFestivalScore(int cabal) {
        switch (cabal) {
            case CABAL_NULL:
                return 0;
            case CABAL_DAWN:
                return status.getDawnFestivalScore();
            case CABAL_DUSK:
                return status.getDuskFestivalScore();
        }

        return 0;
    }

    public final int getCabalHighestScore() {
        if (getCurrentScore(CABAL_DUSK) == getCurrentScore(CABAL_DAWN)) {
            return CABAL_NULL;
        } else if (getCurrentScore(CABAL_DUSK) > getCurrentScore(CABAL_DAWN)) {
            return CABAL_DUSK;
        } else {
            return CABAL_DAWN;
        }
    }

    public final int getSealOwner(int seal) {
        var owner = 0;
        switch (seal) {
            case SEAL_AVARICE:
                owner = status.getAvariceOwner();
                break;
            case SEAL_GNOSIS:
                owner = status.getGnosisOwner();
                break;
            case  SEAL_STRIFE:
                owner =  status.getStrifeOwner();
        }
        return owner;
    }

    public final int getSealProportion(int seal, int cabal) {
        if (cabal == CABAL_NULL) {
            return 0;
        } else if (cabal == CABAL_DUSK) {
            return getDuskSealScore(seal);
        } else {
            return getDawnSealScore(seal);
        }
    }

    private int getDuskSealScore(int seal) {
        var score = 0;
        switch (seal) {
            case SEAL_AVARICE:
                score = status.getAvariceDuskScore();
                break;
            case SEAL_GNOSIS:
                score = status.getGnosisDuskScore();
                break;
            case SEAL_STRIFE:
                score = status.getStrifeDuskScore();
        }
        return score;
    }

    private int getDawnSealScore(int seal) {
        var score = 0;
        switch (seal) {
            case SEAL_AVARICE:
                score = status.getAvariceDawnScore();
                break;
            case SEAL_GNOSIS:
                score = status.getGnosisDawnScore();
                break;
            case SEAL_STRIFE:
                score = status.getStrifeDawnScore();
        }
        return score;
    }

    public final int getTotalMembers(int cabal) {
        String cabalName = getCabalShortName(cabal);
        return (int) _signsPlayerData.values().stream().filter(signsPlayer -> cabalName.equals(signsPlayer.getCabal())).count();
    }

    private Optional<SevenSignsPlayer> getPlayerData(L2PcInstance player) {
        return Optional.ofNullable(_signsPlayerData.get(player.getObjectId()));
    }

    public int getPlayerStoneContrib(L2PcInstance player) {
        return getPlayerData(player).map(SevenSignsPlayer::getStoneContrib).orElse(0);
    }

    public int getPlayerContribScore(L2PcInstance player) {
        return getPlayerData(player).map(SevenSignsPlayer::getContributionScore).orElse(0);
    }

    public int getPlayerAdenaCollect(L2PcInstance player) {
        return  getPlayerData(player).map(SevenSignsPlayer::getAncientAdenaAmount).orElse(0);
    }

    public int getPlayerSeal(L2PcInstance player) {
        return getPlayerData(player).map(SevenSignsPlayer::getSeal).orElse(SEAL_NULL);
    }

    public int getPlayerCabal(L2PcInstance player) {
        return getPlayerData(player).map(playerData -> {
            var cabal = playerData.getCabal();
            if(cabal.equalsIgnoreCase("dawn")) {
                return CABAL_DAWN;
            } else if(cabal.equalsIgnoreCase("dusk")) {
                return CABAL_DUSK;
            }
            return CABAL_NULL;
        }).orElse(CABAL_NULL);
    }

    protected void restoreSevenSignsData() {
        getRepository(SevenSignsRepository.class).findAll().forEach(sevenSignsPlayer -> {
            _log.info("SevenSigns: Loaded data from DB for char ID {} ({})", sevenSignsPlayer.getId(), sevenSignsPlayer.getCabal());
            _signsPlayerData.put(sevenSignsPlayer.getId(), sevenSignsPlayer);
        });

        SevenSignsStatusRepository statusRepository = getRepository(SevenSignsStatusRepository.class);
        status = statusRepository.findById(0).orElse(new SevenSignsStatus());
        statusRepository.updateDate(0, Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
    }

    /**
     * Saves all Seven Signs data, both to the database and properties file (if updateSettings = True).
     * Often called to preserve data integrity and synchronization with DB, in case of errors. <BR>
     * If player != null, just that player's data is updated in the database, otherwise all player's data is sequentially updated.
     *
     */
    public void saveSevenSignsData(L2PcInstance player, boolean updateSettings) {
        _log.debug("SevenSigns: Saving data to disk.");

        if(Objects.nonNull(player)) {
            SevenSignsRepository repository = getRepository(SevenSignsRepository.class);
            getPlayerData(player).ifPresent(repository::save);
        }

        if (updateSettings) {
            SevenSignsStatusRepository statusRepository = getRepository(SevenSignsStatusRepository.class);

            SevenSignsFestival festival = SevenSignsFestival.getInstance();
            statusRepository.update(0, status.getCurrentCycle(), status.getActivePeriod(), status.getPreviousWinner(), status.getDawnStoneScore(), status.getDawnFestivalScore(), status.getDuskStoneScore(),
                status.getDuskFestivalScore(), status.getAvariceOwner(), status.getGnosisOwner(), status.getStrifeOwner(),
                status.getAvariceDawnScore(), status.getGnosisDawnScore(), status.getStrifeDawnScore(),
                status.getAvariceDuskScore(), status.getGnosisDuskScore(), status.getStrifeDuskScore(),
                SevenSignsFestival.getInstance().getCurrentFestivalCycle(), festival.getAccumulatedBonus(0), festival.getAccumulatedBonus(1),
                festival.getAccumulatedBonus(2), festival.getAccumulatedBonus(3), festival.getAccumulatedBonus(4),
                Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

            _log.info("SevenSigns: Updated data in database.");
        }
    }

    /**
     * Used to reset the cabal details of all players, and update the database.<BR>
     * Primarily used when beginning a new cycle, and should otherwise never be called.
     */
    protected void resetPlayerData() {
        if (Config.DEBUG) {
            _log.info("SevenSigns: Resetting player data for new event period.");
        }

        // Reset each player's contribution data as well as seal and cabal.
        _signsPlayerData.values().forEach(signsPlayer -> {
            signsPlayer.setCabal("");
            signsPlayer.setSeal(SEAL_NULL);
            signsPlayer.setContribuitionScore(0);
        });
    }

    /**
     * Tests whether the specified player has joined a cabal in the past.
     *
     * @param player
     * @return boolean hasRegistered
     */
    private boolean hasRegisteredBefore(L2PcInstance player) {
        return _signsPlayerData.containsKey(player.getObjectId());
    }

    /**
     * Used to specify cabal-related details for the specified player. This method checks to see if the player has registered before and will update the database if necessary. <BR>
     * Returns the cabal ID the player has joined.
     *
     * @param player
     * @param chosenCabal
     * @param chosenSeal
     * @return int cabal
     */
    public int setPlayerInfo(L2PcInstance player, int chosenCabal, int chosenSeal) {
        getPlayerData(player).ifPresentOrElse(signsPlayer -> {
                signsPlayer.setCabal(getCabalShortName(chosenCabal));
                signsPlayer.setSeal(chosenSeal);
            },
            () -> {
                var signsPlayer = new SevenSignsPlayer(player.getObjectId(), getCabalShortName(chosenCabal), chosenSeal);
                _signsPlayerData.put(signsPlayer.getId(), signsPlayer);
                getRepository(SevenSignsRepository.class).save(signsPlayer);
                _log.debug("SevenSigns: Inserted data in DB for char ID {} ()", signsPlayer.getId(), signsPlayer.getCabal());
            }
        );

        if(chosenCabal == CABAL_DAWN) {
            status.incrementDawnSealScore(chosenSeal);
        } else {
            status.incrementDuskSealScore(chosenSeal);
        }

        saveSevenSignsData(player, true);

        if (Config.DEBUG) {
            _log.info("SevenSigns: {} has joined the {} for the {}!",  player.getName(), getCabalName(chosenCabal),getSealName(chosenSeal, false));
        }

        return chosenCabal;
    }

    public int getAncientAdenaReward(L2PcInstance player, boolean removeReward) {
        var optionalPlayer = getPlayerData(player);

        if(optionalPlayer.isPresent()) {
            var signsPlayer = optionalPlayer.get();
            var rewardAmount = signsPlayer.getAncientAdenaAmount();
            signsPlayer.setRedStones(0);
            signsPlayer.setGreenStones(0);
            signsPlayer.setBlueStones(0);

            if(removeReward) {
                saveSevenSignsData(player, true);
            }

            return rewardAmount;
        }
        return 0;

    }


    public int addPlayerStoneContrib(L2PcInstance player, int blueCount, int greenCount, int redCount) {
        var optionalPlayer = getPlayerData(player);
        if(optionalPlayer.isPresent()) {
            var signsPlayer = optionalPlayer.get();
            var contribScore = calcContributionScore(blueCount, greenCount, redCount);
            var totalAncientAdena = signsPlayer.getAncientAdenaAmount() + calcAncientAdenaReward(blueCount, greenCount, redCount);
            var totalContribScore = signsPlayer.getContributionScore() + contribScore;

            if (totalContribScore > Config.ALT_MAXIMUM_PLAYER_CONTRIB) {
                return -1;
            }

            signsPlayer.setRedStones( signsPlayer.getRedStones() + redCount);
            signsPlayer.setGreenStones( signsPlayer.getGreenStones() + greenCount);
            signsPlayer.setBlueStones( signsPlayer.getBlueStones() + blueCount);
            signsPlayer.setAncientAdenaAmount(totalAncientAdena);
            signsPlayer.setContribuitionScore(totalContribScore);

            switch (getPlayerCabal(player)) {
                case CABAL_DAWN:
                    status.addDawnStoneScore(contribScore);
                    break;
                case CABAL_DUSK:
                    status.addDuskStoneScore(contribScore);
                    break;
            }

            saveSevenSignsData(player, true);

            if (Config.DEBUG) {
                _log.info("SevenSigns: {} contributed {} seal stone point to their cabal.", player.getName(), contribScore);
            }

            return contribScore;
        }
        return 0;
    }

    /**
     * Adds the specified number of festival points to the specified cabal. Remember, the same number of points are <B>deducted from the rival cabal</B> to maintain proportionality.
     *
     * @param cabal
     * @param amount
     */
    public void addFestivalScore(int cabal, int amount) {
        if (cabal == CABAL_DUSK) {
            status.changeDuskFestivalScore(amount);

            // To prevent negative scores!
            if (status.getDawnFestivalScore() >= amount) {
                status.changeDawnFestivalScore(-amount);
            }
        } else {
            status.changeDawnFestivalScore(amount);

            if (status.getDuskFestivalScore() >= amount) {
                status.changeDuskFestivalScore(-amount);
            }
        }
    }

    /**
     * Send info on the current Seven Signs period to the specified player.
     *
     * @param player
     */
    public void sendCurrentPeriodMsg(L2PcInstance player) {
        SystemMessage sm = null;

        switch (getCurrentPeriod()) {
            case PERIOD_COMP_RECRUITING:
                sm = new SystemMessage(SystemMessageId.PREPARATIONS_PERIOD_BEGUN);
                break;
            case PERIOD_COMPETITION:
                sm = new SystemMessage(SystemMessageId.COMPETITION_PERIOD_BEGUN);
                break;
            case PERIOD_COMP_RESULTS:
                sm = new SystemMessage(SystemMessageId.RESULTS_PERIOD_BEGUN);
                break;
            case PERIOD_SEAL_VALIDATION:
                sm = new SystemMessage(SystemMessageId.VALIDATION_PERIOD_BEGUN);
                break;
        }

        player.sendPacket(sm);
    }

    /**
     * Sends the built-in system message specified by sysMsgId to all online players.
     *
     * @param sysMsgId
     */
    public void sendMessageToAll(SystemMessageId sysMsgId) {
        SystemMessage sm = new SystemMessage(sysMsgId);

        for (L2PcInstance player : L2World.getInstance().getAllPlayers()) {
            player.sendPacket(sm);
        }
    }

    /**
     * Used to initialize the seals for each cabal. (Used at startup or at beginning of a new cycle). This method should be called after <B>resetSeals()</B> and <B>calcNewSealOwners()</B> on a new cycle.
     */
    private void initializeSeals() {

        infoInitializeSeal(status.getAvariceOwner(), SEAL_AVARICE);

    }

    private void infoInitializeSeal(int owner, int seal) {
        if(owner != CABAL_NULL) {
            if (isSealValidationPeriod()) {
                _log.info("SevenSigns: The {} have won the {}.", getCabalName(owner), getSealName(seal, false));
            } else {
                _log.info("SevenSigns: The {} is currently owned by {}.", getSealName(seal, false), getCabalName(owner));
            }
        } else {
            _log.info("SevenSigns: The {} remains unclaimed.", getSealName(seal, false));
        }

    }

    /**
     * Only really used at the beginning of a new cycle, this method resets all seal-related data.
     */
    private void resetSeals() {
        status.setAvariceDawnScore(0);
        status.setGnosisDawnScore(0);
        status.setStrifeDawnScore(0);
        status.setAvariceDuskScore(0);
        status.setGnosisDuskScore(0);
        status.setStrifeDuskScore(0);
    }

    /**
     * Calculates the ownership of the three Seals of the Seven Signs, based on various criterion. <BR>
     * <BR>
     * Should only ever called at the beginning of a new cycle.
     */
    private void calcNewSealOwners() {
        if (Config.DEBUG) {
            _log.info("SevenSigns: (Avarice) Dawn = {}, Dusk = {}", status.getAvariceDawnScore(), status.getAvariceDuskScore());
            _log.info("SevenSigns: (Gnosis) Dawn = {}, Dusk = {}", status.getGnosisDawnScore(), status.getGnosisDuskScore());
            _log.info("SevenSigns: (Strife) Dawn = {}, Dusk = {}", status.getStrifeDawnScore(),  status.getStrifeDuskScore());
        }

        for (Integer currSeal : Arrays.asList(SEAL_AVARICE, SEAL_GNOSIS, SEAL_STRIFE)) {
            int prevSealOwner = getSealOwner(currSeal);
            int newSealOwner = CABAL_NULL;
            int dawnProportion = getSealProportion(currSeal, CABAL_DAWN);
            int totalDawnMembers = getTotalMembers(CABAL_DAWN) == 0 ? 1 : getTotalMembers(CABAL_DAWN);
            int dawnPercent = Math.round(((float) dawnProportion / (float) totalDawnMembers) * 100);
            int duskProportion = getSealProportion(currSeal, CABAL_DUSK);
            int totalDuskMembers = getTotalMembers(CABAL_DUSK) == 0 ? 1 : getTotalMembers(CABAL_DUSK);
            int duskPercent = Math.round(((float) duskProportion / (float) totalDuskMembers) * 100);

            /*
             * - If a Seal was already closed or owned by the opponent and the new winner wants to assume ownership of the Seal, 35% or more of the members of the Cabal must have chosen the Seal. If they chose less than 35%, they cannot own the Seal. - If the Seal was owned by the winner in the
             * previous Seven Signs, they can retain that seal if 10% or more members have chosen it. If they want to possess a new Seal, at least 35% of the members of the Cabal must have chosen the new Seal.
             */
            switch (prevSealOwner) {
                case CABAL_NULL:
                    switch (getCabalHighestScore()) {
                        case CABAL_NULL:
                            newSealOwner = CABAL_NULL;
                            break;
                        case CABAL_DAWN:
                            if (dawnPercent >= 35) {
                                newSealOwner = CABAL_DAWN;
                            } else {
                                newSealOwner = CABAL_NULL;
                            }
                            break;
                        case CABAL_DUSK:
                            if (duskPercent >= 35) {
                                newSealOwner = CABAL_DUSK;
                            } else {
                                newSealOwner = CABAL_NULL;
                            }
                            break;
                    }
                    break;
                case CABAL_DAWN:
                    switch (getCabalHighestScore()) {
                        case CABAL_NULL:
                            if (dawnPercent >= 10) {
                                newSealOwner = CABAL_DAWN;
                            } else {
                                newSealOwner = CABAL_NULL;
                            }
                            break;
                        case CABAL_DAWN:
                            if (dawnPercent >= 10) {
                                newSealOwner = CABAL_DAWN;
                            } else {
                                newSealOwner = CABAL_NULL;
                            }
                            break;
                        case CABAL_DUSK:
                            if (duskPercent >= 35) {
                                newSealOwner = CABAL_DUSK;
                            } else if (dawnPercent >= 10) {
                                newSealOwner = CABAL_DAWN;
                            } else {
                                newSealOwner = CABAL_NULL;
                            }
                            break;
                    }
                    break;
                case CABAL_DUSK:
                    switch (getCabalHighestScore()) {
                        case CABAL_NULL:
                            if (duskPercent >= 10) {
                                newSealOwner = CABAL_DUSK;
                            } else {
                                newSealOwner = CABAL_NULL;
                            }
                            break;
                        case CABAL_DAWN:
                            if (dawnPercent >= 35) {
                                newSealOwner = CABAL_DAWN;
                            } else if (duskPercent >= 10) {
                                newSealOwner = CABAL_DUSK;
                            } else {
                                newSealOwner = CABAL_NULL;
                            }
                            break;
                        case CABAL_DUSK:
                            if (duskPercent >= 10) {
                                newSealOwner = CABAL_DUSK;
                            } else {
                                newSealOwner = CABAL_NULL;
                            }
                            break;
                    }
                    break;
            }

            // Alert all online players to new seal status.
            switch (currSeal) {
                case SEAL_AVARICE:
                    status.setAvariceOwner(newSealOwner);
                    if (newSealOwner == CABAL_DAWN) {
                        sendMessageToAll(SystemMessageId.DAWN_OBTAINED_AVARICE);
                    } else if (newSealOwner == CABAL_DUSK) {
                        sendMessageToAll(SystemMessageId.DUSK_OBTAINED_AVARICE);
                    }
                    break;
                case SEAL_GNOSIS:
                    status.setGnosisOwner(newSealOwner);
                    if (newSealOwner == CABAL_DAWN) {
                        sendMessageToAll(SystemMessageId.DAWN_OBTAINED_GNOSIS);
                    } else if (newSealOwner == CABAL_DUSK) {
                        sendMessageToAll(SystemMessageId.DUSK_OBTAINED_GNOSIS);
                    }
                    break;
                case SEAL_STRIFE:
                    status.setStrifeOwner(newSealOwner);
                    if (newSealOwner == CABAL_DAWN) {
                        sendMessageToAll(SystemMessageId.DAWN_OBTAINED_STRIFE);
                    } else if (newSealOwner == CABAL_DUSK) {
                        sendMessageToAll(SystemMessageId.DUSK_OBTAINED_STRIFE);
                    }

                    CastleManager.getInstance().validateTaxes(newSealOwner);
                    break;
            }
        }
    }

    private void teleLosingCabalFromDungeons(String compWinner) {
        String msgValidation = "You have been teleported to the nearest town due to the beginning of the Seal Validation period.";
        String msgNotSigned = "You have been teleported to the nearest town because you have not signed for any cabal.";
        L2World.getInstance().getAllPlayers().parallelStream().filter(p -> !p.isGM() && p.isIn7sDungeon()).forEach(player -> {
            var playerData = getPlayerData(player);
            boolean teleport;
            String msg = "";
            if(isSealValidationPeriod() || isCompResultsPeriod()) {
                teleport = playerData.map(p -> p.getCabal().equalsIgnoreCase(compWinner)).orElse(false);
                msg = msgValidation;
            } else {
                teleport = playerData.map(p -> p.getCabal().equals("")).orElse(false);
                msg = msgNotSigned;
            }
            if(teleport) {
                player.teleToLocation(MapRegionTable.TeleportWhereType.Town);
                player.setIsIn7sDungeon(false);
                player.sendMessage(msg);
            }

        });
    }

    /**
     * The primary controller of period change of the Seven Signs system. This runs all related tasks depending on the period that is about to begin.
     *
     * @author Tempy
     */
    protected class SevenSignsPeriodChange implements Runnable {
        @Override
        public void run() {
            /*
             * Remember the period check here refers to the period just ENDED!
             */
            final int periodEnded = getCurrentPeriod();
            status.incrementActivePeriod();

            switch (periodEnded) {
                case PERIOD_COMP_RECRUITING: // Initialization

                    // Start the Festival of Darkness cycle.
                    SevenSignsFestival.getInstance().startFestivalManager();

                    // Send message that Competition has begun.
                    sendMessageToAll(SystemMessageId.QUEST_EVENT_PERIOD_BEGUN);
                    break;
                case PERIOD_COMPETITION: // Results Calculation

                    // Send message that Competition has ended.
                    sendMessageToAll(SystemMessageId.QUEST_EVENT_PERIOD_ENDED);

                    int compWinner = getCabalHighestScore();

                    // Schedule a stop of the festival engine.
                    SevenSignsFestival.getInstance().getFestivalManagerSchedule().cancel(false);

                    calcNewSealOwners();

                    switch (compWinner) {
                        case CABAL_DAWN:
                            sendMessageToAll(SystemMessageId.DAWN_WON);
                            break;
                        case CABAL_DUSK:
                            sendMessageToAll(SystemMessageId.DUSK_WON);
                            break;
                    }

                    status.setPreviousWinner(compWinner);
                    break;
                case PERIOD_COMP_RESULTS: // Seal Validation

                    // Perform initial Seal Validation set up.
                    initializeSeals();

                    // Send message that Seal Validation has begun.
                    sendMessageToAll(SystemMessageId.SEAL_VALIDATION_PERIOD_BEGUN);

                    _log.info("SevenSigns: The " + getCabalName(status.getPreviousWinner()) + " have won the competition with " + getCurrentScore(status.getPreviousWinner()) + " points!");
                    break;
                case PERIOD_SEAL_VALIDATION: // Reset for New Cycle

                    SevenSignsFestival.getInstance().rewardHighestRanked(); // reward highest ranking members from cycle

                    // Ensure a cycle restart when this period ends.
                    status.setActivePeriod(PERIOD_COMP_RECRUITING);

                    // Send message that Seal Validation has ended.
                    sendMessageToAll(SystemMessageId.SEAL_VALIDATION_PERIOD_ENDED);

                    // Reset all data
                    resetPlayerData();
                    resetSeals();

                    // Reset all Festival-related data and remove any unused blood offerings.
                    // NOTE: A full update of Festival data in the database is also performed.
                    SevenSignsFestival.getInstance().resetFestivalData(false);

                    status.setDawnStoneScore(0);
                    status.setDuskStoneScore(0);

                    status.setDawnFestivalScore(0);
                    status.setDuskFestivalScore(0);

                    status.incrementCurrentCycle();
                    break;
            }

            // Make sure all Seven Signs data is saved for future use.
            saveSevenSignsData(null, true);

            teleLosingCabalFromDungeons(getCabalShortName(getCabalHighestScore()));

            SignsSky ss = new SignsSky();

            for (L2PcInstance player : L2World.getInstance().getAllPlayers()) {
                player.sendPacket(ss);
            }

            spawnSevenSignsNPC();

            _log.info("SevenSigns: The " + getCurrentPeriodName() + " period has begun!");

            setCalendarForNextPeriodChange();

            SevenSignsPeriodChange sspc = new SevenSignsPeriodChange();
            ThreadPoolManager.getInstance().scheduleGeneral(sspc, getMilliToPeriodChange());
        }
    }
}
