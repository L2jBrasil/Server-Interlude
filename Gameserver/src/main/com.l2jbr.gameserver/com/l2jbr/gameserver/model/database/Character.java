package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

public class Character extends Entity<Integer> {

    @Id
    private int objId;
    @Column("account_name")
    private String accountName;
    @Column("char_name")
    private String name;
    private Byte level;
    private int maxHp;
    private long curHp;
    private int maxCp;
    private long curCp;
    private int maxMp;
    private long curMp;
    private int acc;
    private int crit;
    private int evasion;
    private int mAtk;
    private int mDef;
    private int mSpd;
    private int pAtk;
    private int pDef;
    private int pSpd;
    private int runSpd;
    private int walkSpd;
    private int str;
    private int con;
    private int dex;
    @Column("_int")
    private int intelligence;
    private int men;
    private int wit;
    private Byte face;
    private Byte hairStyle;
    private Byte hairColor;
    private int sex;
    private int heading;
    private int x;
    private int y;
    private int z;
    @Column("movement_multiplier")
    private int movementMultiplier;
    @Column("attack_speed_multiplier")
    private int attackSpeedMultiplier;
    private int colRad;
    private int colHeight;
    private long exp;
    private long expBeforeDeath;
    private int sp;
    private int karma;
    private int pvpkills;
    private int pkkills;
    private int clanid;
    private int maxload;
    private int race;
    private int classid;
    @Column("base_class")
    private int baseClass;
    private long deletetime;
    private int cancraft;
    private String title;
    @Column("rec_have")
    private int recHave;
    @Column("rec_left")
    private int recLeft;
    private int accesslevel;
    private int online;
    private long onlinetime;
    @Column("char_slot")
    private int slot;
    private int newbie;
    private long lastAccess;
    @Column("clan_privs")
    private int clanPrivs;
    private int wantspeace;
    private int isin7sdungeon;
    @Column("in_jail")
    private int inJail;
    @Column("jailTimer")
    private long jailTimer;
    @Column("power_grade")
    private int powerGrade;
    private int nobless;
    private int subpledge;
    @Column("last_recom_date")
    private long lastRecomDate;
    @Column("lvl_joined_academy")
    private int lvlJoinedAcademy;
    private int apprentice;
    private int sponsor;
    @Column("varka_ketra_ally")
    private int varkaKetraAlly;
    @Column("clan_join_expiry_time")
    private long clanJoinExpiryTime;
    @Column("clan_create_expiry_time")
    private long clanCreateExpiryTime;
    @Column("death_penalty_level")
    private int deathPenaltyLevel;


    @Override
    public Integer getId() {
        return objId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getObjectId() {
        return objId;
    }

    public void setObjId(int objId) {
        this.objId = objId;
    }

    public String getCharName() {
        return name;
    }

    public void setCharName(String char_name) {
        this.name = char_name;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public long getCurrentHp() {
        return curHp;
    }

    public void setCurHp(long curHp) {
        this.curHp = curHp;
    }

    public int getMaxCp() {
        return maxCp;
    }

    public void setMaxCp(int maxCp) {
        this.maxCp = maxCp;
    }

    public long getCurCp() {
        return curCp;
    }

    public void setCurCp(long curCp) {
        this.curCp = curCp;
    }

    public int getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(int maxMp) {
        this.maxMp = maxMp;
    }

    public long getCurrentMp() {
        return curMp;
    }

    public void setCurMp(long curMp) {
        this.curMp = curMp;
    }

    public int getAcc() {
        return acc;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }

    public int getCrit() {
        return crit;
    }

    public void setCrit(int crit) {
        this.crit = crit;
    }

    public int getEvasion() {
        return evasion;
    }

    public void setEvasion(int evasion) {
        this.evasion = evasion;
    }

    public int getmAtk() {
        return mAtk;
    }

    public void setmAtk(int mAtk) {
        this.mAtk = mAtk;
    }

    public int getmDef() {
        return mDef;
    }

    public void setmDef(int mDef) {
        this.mDef = mDef;
    }

    public int getmSpd() {
        return mSpd;
    }

    public void setmSpd(int mSpd) {
        this.mSpd = mSpd;
    }

    public int getpAtk() {
        return pAtk;
    }

    public void setpAtk(int pAtk) {
        this.pAtk = pAtk;
    }

    public int getpDef() {
        return pDef;
    }

    public void setpDef(int pDef) {
        this.pDef = pDef;
    }

    public int getpSpd() {
        return pSpd;
    }

    public void setpSpd(int pSpd) {
        this.pSpd = pSpd;
    }

    public int getRunSpd() {
        return runSpd;
    }

    public void setRunSpd(int runSpd) {
        this.runSpd = runSpd;
    }

    public int getWalkSpd() {
        return walkSpd;
    }

    public void setWalkSpd(int walkSpd) {
        this.walkSpd = walkSpd;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getCon() {
        return con;
    }

    public void setCon(int con) {
        this.con = con;
    }

    public int getDex() {
        return dex;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getMen() {
        return men;
    }

    public void setMen(int men) {
        this.men = men;
    }

    public int getWit() {
        return wit;
    }

    public void setWit(int wit) {
        this.wit = wit;
    }

    public byte getFace() {
        return face;
    }

    public void setFace(byte face) {
        this.face = face;
    }

    public byte getHairStyle() {
        return hairStyle;
    }

    public void setHairStyle(byte hairStyle) {
        this.hairStyle = hairStyle;
    }

    public byte getHairColor() {
        return hairColor;
    }

    public void setHairColor(byte hairColor) {
        this.hairColor = hairColor;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getMovementMultiplier() {
        return movementMultiplier;
    }

    public void setMovementMultiplier(int movementMultiplier) {
        this.movementMultiplier = movementMultiplier;
    }

    public int getAttackSpeedMultiplier() {
        return attackSpeedMultiplier;
    }

    public void setAttackSpeedMultiplier(int attackSpeedMultiplier) {
        this.attackSpeedMultiplier = attackSpeedMultiplier;
    }

    public int getColRad() {
        return colRad;
    }

    public void setColRad(int colRad) {
        this.colRad = colRad;
    }

    public int getColHeight() {
        return colHeight;
    }

    public void setColHeight(int colHeight) {
        this.colHeight = colHeight;
    }

    public long getExperience() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public long getExpBeforeDeath() {
        return expBeforeDeath;
    }

    public void setExpBeforeDeath(long expBeforeDeath) {
        this.expBeforeDeath = expBeforeDeath;
    }

    public int getSp() {
        return sp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public int getPvpkills() {
        return pvpkills;
    }

    public void setPvpkills(int pvpkills) {
        this.pvpkills = pvpkills;
    }

    public int getPkkills() {
        return pkkills;
    }

    public void setPkkills(int pkkills) {
        this.pkkills = pkkills;
    }

    public int getClanId() {
        return clanid;
    }

    public void setClanid(int clanid) {
        this.clanid = clanid;
    }

    public int getMaxload() {
        return maxload;
    }

    public void setMaxload(int maxload) {
        this.maxload = maxload;
    }

    public int getRace() {
        return race;
    }

    public void setRace(int race) {
        this.race = race;
    }

    public int getClassId() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public int getBaseClass() {
        return baseClass;
    }

    public void setBaseClass(int baseClass) {
        this.baseClass = baseClass;
    }

    public long getDeleteTime() {
        return deletetime;
    }

    public void setDeletetime(long deletetime) {
        this.deletetime = deletetime;
    }

    public int getCancraft() {
        return cancraft;
    }

    public void setCancraft(int cancraft) {
        this.cancraft = cancraft;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRecHave() {
        return recHave;
    }

    public void setRecHave(int recHave) {
        this.recHave = recHave;
    }

    public int getRecLeft() {
        return recLeft;
    }

    public void setRecLeft(int recLeft) {
        this.recLeft = recLeft;
    }

    public int getAccesslevel() {
        return accesslevel;
    }

    public void setAccesslevel(int accesslevel) {
        this.accesslevel = accesslevel;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public long getOnlinetime() {
        return onlinetime;
    }

    public void setOnlinetime(long onlinetime) {
        this.onlinetime = onlinetime;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getNewbie() {
        return newbie;
    }

    public void setNewbie(int newbie) {
        this.newbie = newbie;
    }

    public long getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(long lastAccess) {
        this.lastAccess = lastAccess;
    }

    public int getClanPrivs() {
        return clanPrivs;
    }

    public void setClanPrivs(int clanPrivs) {
        this.clanPrivs = clanPrivs;
    }

    public int getWantspeace() {
        return wantspeace;
    }

    public void setWantspeace(int wantspeace) {
        this.wantspeace = wantspeace;
    }

    public int getIsin7sdungeon() {
        return isin7sdungeon;
    }

    public void setIsin7sdungeon(int isin7sdungeon) {
        this.isin7sdungeon = isin7sdungeon;
    }

    public int getInJail() {
        return inJail;
    }

    public void setInJail(int inJail) {
        this.inJail = inJail;
    }

    public long getJailTimer() {
        return jailTimer;
    }

    public void setJailTimer(long jailTimer) {
        this.jailTimer = jailTimer;
    }

    public int getPowerGrade() {
        return powerGrade;
    }

    public void setPowerGrade(int powerGrade) {
        this.powerGrade = powerGrade;
    }

    public int getNobless() {
        return nobless;
    }

    public void setNobless(int nobless) {
        this.nobless = nobless;
    }

    public int getSubpledge() {
        return subpledge;
    }

    public void setSubpledge(int subpledge) {
        this.subpledge = subpledge;
    }

    public long getLastRecomDate() {
        return lastRecomDate;
    }

    public void setLastRecomDate(long lastRecomDate) {
        this.lastRecomDate = lastRecomDate;
    }

    public int getLvlJoinedAcademy() {
        return lvlJoinedAcademy;
    }

    public void setLvlJoinedAcademy(int lvlJoinedAcademy) {
        this.lvlJoinedAcademy = lvlJoinedAcademy;
    }

    public int getApprentice() {
        return apprentice;
    }

    public void setApprentice(int apprentice) {
        this.apprentice = apprentice;
    }

    public int getSponsor() {
        return sponsor;
    }

    public void setSponsor(int sponsor) {
        this.sponsor = sponsor;
    }

    public int getVarkaKetraAlly() {
        return varkaKetraAlly;
    }

    public void setVarkaKetraAlly(int varkaKetraAlly) {
        this.varkaKetraAlly = varkaKetraAlly;
    }

    public long getClanJoinExpiryTime() {
        return clanJoinExpiryTime;
    }

    public void setClanJoinExpiryTime(long clanJoinExpiryTime) {
        this.clanJoinExpiryTime = clanJoinExpiryTime;
    }

    public long getClanCreateExpiryTime() {
        return clanCreateExpiryTime;
    }

    public void setClanCreateExpiryTime(long clanCreateExpiryTime) {
        this.clanCreateExpiryTime = clanCreateExpiryTime;
    }

    public int getDeathPenaltyLevel() {
        return deathPenaltyLevel;
    }

    public void setDeathPenaltyLevel(int deathPenaltyLevel) {
        this.deathPenaltyLevel = deathPenaltyLevel;
    }

    public void updateLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
