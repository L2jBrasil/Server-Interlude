-- accounts: table
CREATE TABLE `accounts` (
  `login`             varchar(45) NOT NULL,
  `password`          varchar(45) NOT NULL,
  `lastActive`        decimal(20, 0)       DEFAULT NULL,
  `access_level`      int(11)     NOT NULL DEFAULT '0',
  `lastIP`            varchar(20)          DEFAULT NULL,
  `lastServer`        int(11)     NOT NULL DEFAULT '1',
  `newbieCharacterId` decimal(11, 0)       DEFAULT NULL,
  PRIMARY KEY (`login`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- armor: table
CREATE TABLE `armor` (
  `item_id`        int(11)        NOT NULL DEFAULT '0',
  `name`           varchar(70)             DEFAULT NULL,
  `bodypart`       varchar(15)    NOT NULL DEFAULT '',
  `crystallizable` varchar(5)     NOT NULL DEFAULT '',
  `armor_type`     varchar(5)     NOT NULL DEFAULT '',
  `weight`         int(5)         NOT NULL DEFAULT '0',
  `material`       varchar(15)    NOT NULL DEFAULT '',
  `crystal_type`   varchar(4)     NOT NULL DEFAULT '',
  `avoid_modify`   int(1)         NOT NULL DEFAULT '0',
  `duration`       int(3)         NOT NULL DEFAULT '0',
  `p_def`          int(3)         NOT NULL DEFAULT '0',
  `m_def`          int(2)         NOT NULL DEFAULT '0',
  `mp_bonus`       int(3)         NOT NULL DEFAULT '0',
  `price`          int(11)        NOT NULL DEFAULT '0',
  `crystal_count`  int(4)                  DEFAULT NULL,
  `sellable`       varchar(5)              DEFAULT NULL,
  `dropable`       varchar(5)              DEFAULT NULL,
  `destroyable`    varchar(5)              DEFAULT NULL,
  `tradeable`      varchar(5)              DEFAULT NULL,
  `item_skill_id`  decimal(11, 0) NOT NULL DEFAULT '0',
  `item_skill_lvl` decimal(11, 0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`item_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- armorsets: table
CREATE TABLE `armorsets` (
  `id`              int(3)         NOT NULL AUTO_INCREMENT,
  `chest`           decimal(11, 0) NOT NULL DEFAULT '0',
  `legs`            decimal(11, 0) NOT NULL DEFAULT '0',
  `head`            decimal(11, 0) NOT NULL DEFAULT '0',
  `gloves`          decimal(11, 0) NOT NULL DEFAULT '0',
  `feet`            decimal(11, 0) NOT NULL DEFAULT '0',
  `skill_id`        decimal(11, 0) NOT NULL DEFAULT '0',
  `shield`          decimal(11, 0) NOT NULL DEFAULT '0',
  `shield_skill_id` decimal(11, 0) NOT NULL DEFAULT '0',
  `enchant6skill`   decimal(11, 0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`, `chest`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 52
  DEFAULT CHARSET = latin1;

-- auction: table
CREATE TABLE `auction` (
  `id`             int(11)        NOT NULL DEFAULT '0',
  `sellerId`       int(11)        NOT NULL DEFAULT '0',
  `sellerName`     varchar(50)    NOT NULL DEFAULT 'NPC',
  `sellerClanName` varchar(50)    NOT NULL DEFAULT '',
  `itemType`       varchar(25)    NOT NULL DEFAULT '',
  `itemId`         int(11)        NOT NULL DEFAULT '0',
  `itemObjectId`   int(11)        NOT NULL DEFAULT '0',
  `itemName`       varchar(40)    NOT NULL DEFAULT '',
  `itemQuantity`   int(11)        NOT NULL DEFAULT '0',
  `startingBid`    int(11)        NOT NULL DEFAULT '0',
  `currentBid`     int(11)        NOT NULL DEFAULT '0',
  `endDate`        decimal(20, 0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`itemType`, `itemId`, `itemObjectId`),
  KEY `id` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- auction_bid: table
CREATE TABLE `auction_bid` (
  `id`         int(11)        NOT NULL DEFAULT '0',
  `auctionId`  int(11)        NOT NULL DEFAULT '0',
  `bidderId`   int(11)        NOT NULL DEFAULT '0',
  `bidderName` varchar(50)    NOT NULL,
  `clan_name`  varchar(50)    NOT NULL,
  `maxBid`     int(11)        NOT NULL DEFAULT '0',
  `time_bid`   decimal(20, 0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`auctionId`, `bidderId`),
  KEY `id` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- augmentations: table
CREATE TABLE `augmentations` (
  `item_id`    int(11) NOT NULL DEFAULT '0',
  `attributes` int(11)          DEFAULT '0',
  `skill`      int(11)          DEFAULT '0',
  `level`      int(11)          DEFAULT '0',
  PRIMARY KEY (`item_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- auto_chat: table
CREATE TABLE `auto_chat` (
  `groupId`   int(11)    NOT NULL DEFAULT '0',
  `npcId`     int(11)    NOT NULL DEFAULT '0',
  `chatDelay` bigint(20) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`groupId`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- auto_chat_text: table
CREATE TABLE `auto_chat_text` (
  `groupId`  int(11)      NOT NULL DEFAULT '0',
  `chatText` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`groupId`, `chatText`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- castle: table
CREATE TABLE `castle` (
  `id`             int(11)        NOT NULL DEFAULT '0',
  `name`           varchar(25)    NOT NULL,
  `taxPercent`     int(11)        NOT NULL DEFAULT '15',
  `treasury`       int(11)        NOT NULL DEFAULT '0',
  `siegeDate`      decimal(20, 0) NOT NULL DEFAULT '0',
  `siegeDayOfWeek` int(11)        NOT NULL DEFAULT '7',
  `siegeHourOfDay` int(11)        NOT NULL DEFAULT '20',
  PRIMARY KEY (`name`),
  KEY `id` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- castle_door: table
CREATE TABLE `castle_door` (
  `castleId`   int(11)     NOT NULL DEFAULT '0',
  `id`         int(11)     NOT NULL DEFAULT '0',
  `name`       varchar(30) NOT NULL,
  `x`          int(11)     NOT NULL DEFAULT '0',
  `y`          int(11)     NOT NULL DEFAULT '0',
  `z`          int(11)     NOT NULL DEFAULT '0',
  `range_xmin` int(11)     NOT NULL DEFAULT '0',
  `range_ymin` int(11)     NOT NULL DEFAULT '0',
  `range_zmin` int(11)     NOT NULL DEFAULT '0',
  `range_xmax` int(11)     NOT NULL DEFAULT '0',
  `range_ymax` int(11)     NOT NULL DEFAULT '0',
  `range_zmax` int(11)     NOT NULL DEFAULT '0',
  `hp`         int(11)     NOT NULL DEFAULT '0',
  `pDef`       int(11)     NOT NULL DEFAULT '0',
  `mDef`       int(11)     NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `id` (`castleId`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- castle_manor_procure: table
CREATE TABLE `castle_manor_procure` (
  `castle_id`   int(11) NOT NULL DEFAULT '0',
  `crop_id`     int(11) NOT NULL DEFAULT '0',
  `can_buy`     int(11) NOT NULL DEFAULT '0',
  `start_buy`   int(11) NOT NULL DEFAULT '0',
  `price`       int(11) NOT NULL DEFAULT '0',
  `reward_type` int(11) NOT NULL DEFAULT '0',
  `period`      int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`castle_id`, `crop_id`, `period`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- castle_manor_production: table
CREATE TABLE `castle_manor_production` (
  `castle_id`     int(11) NOT NULL DEFAULT '0',
  `seed_id`       int(11) NOT NULL DEFAULT '0',
  `can_produce`   int(11) NOT NULL DEFAULT '0',
  `start_produce` int(11) NOT NULL DEFAULT '0',
  `seed_price`    int(11) NOT NULL DEFAULT '0',
  `period`        int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`castle_id`, `seed_id`, `period`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- castle_siege_guards: table
CREATE TABLE `castle_siege_guards` (
  `castleId`     int(11) NOT NULL DEFAULT '0',
  `id`           int(11) NOT NULL AUTO_INCREMENT,
  `npcId`        int(11) NOT NULL DEFAULT '0',
  `x`            int(11) NOT NULL DEFAULT '0',
  `y`            int(11) NOT NULL DEFAULT '0',
  `z`            int(11) NOT NULL DEFAULT '0',
  `heading`      int(11) NOT NULL DEFAULT '0',
  `respawnDelay` int(11) NOT NULL DEFAULT '0',
  `isHired`      int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `id` (`castleId`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 3448
  DEFAULT CHARSET = latin1;

-- char_templates: table
CREATE TABLE `char_templates` (
  `ClassId`       int(11)       NOT NULL DEFAULT '0',
  `ClassName`     varchar(20)   NOT NULL DEFAULT '',
  `classLevel`    int(3)        NOT NULL DEFAULT '0',
  `RaceId`        int(1)        NOT NULL DEFAULT '0',
  `parent_id`     int(11)       NOT NULL DEFAULT '0',
  `STR`           int(2)        NOT NULL DEFAULT '0',
  `CON`           int(2)        NOT NULL DEFAULT '0',
  `DEX`           int(2)        NOT NULL DEFAULT '0',
  `_INT`          int(2)        NOT NULL DEFAULT '0',
  `WIT`           int(2)        NOT NULL DEFAULT '0',
  `MEN`           int(2)        NOT NULL DEFAULT '0',
  `P_ATK`         int(3)        NOT NULL DEFAULT '0',
  `P_DEF`         int(3)        NOT NULL DEFAULT '0',
  `M_ATK`         int(3)        NOT NULL DEFAULT '0',
  `M_DEF`         int(2)        NOT NULL DEFAULT '0',
  `P_SPD`         int(3)        NOT NULL DEFAULT '0',
  `M_SPD`         int(3)        NOT NULL DEFAULT '0',
  `ACC`           int(3)        NOT NULL DEFAULT '0',
  `CRITICAL`      int(3)        NOT NULL DEFAULT '0',
  `EVASION`       int(3)        NOT NULL DEFAULT '0',
  `MOVE_SPD`      int(3)        NOT NULL DEFAULT '0',
  `_LOAD`         int(11)       NOT NULL DEFAULT '0',
  `defaulthpbase` decimal(5, 1) NOT NULL DEFAULT '0.0',
  `defaulthpadd`  decimal(4, 2) NOT NULL DEFAULT '0.00',
  `defaulthpmod`  decimal(4, 2) NOT NULL DEFAULT '0.00',
  `defaultcpbase` decimal(5, 1) NOT NULL DEFAULT '0.0',
  `defaultcpadd`  decimal(4, 2) NOT NULL DEFAULT '0.00',
  `defaultcpmod`  decimal(4, 2) NOT NULL DEFAULT '0.00',
  `defaultmpbase` decimal(5, 1) NOT NULL DEFAULT '0.0',
  `defaultmpadd`  decimal(4, 2) NOT NULL DEFAULT '0.00',
  `defaultmpmod`  decimal(4, 2) NOT NULL DEFAULT '0.00',
  `x`             int(9)        NOT NULL DEFAULT '0',
  `y`             int(9)        NOT NULL DEFAULT '0',
  `z`             int(9)        NOT NULL DEFAULT '0',
  `canCraft`      int(1)        NOT NULL DEFAULT '0',
  `M_UNK1`        decimal(4, 2) NOT NULL DEFAULT '0.00',
  `M_UNK2`        decimal(8, 6) NOT NULL DEFAULT '0.000000',
  `M_COL_R`       decimal(3, 1) NOT NULL DEFAULT '0.0',
  `M_COL_H`       decimal(4, 1) NOT NULL DEFAULT '0.0',
  `F_UNK1`        decimal(4, 2) NOT NULL DEFAULT '0.00',
  `F_UNK2`        decimal(8, 6) NOT NULL DEFAULT '0.000000',
  `F_COL_R`       decimal(3, 1) NOT NULL DEFAULT '0.0',
  `F_COL_H`       decimal(4, 1) NOT NULL DEFAULT '0.0',
  `items1`        int(4)        NOT NULL DEFAULT '0',
  `items2`        int(4)        NOT NULL DEFAULT '0',
  `items3`        int(4)        NOT NULL DEFAULT '0',
  `items4`        int(4)        NOT NULL DEFAULT '0',
  `items5`        int(10)       NOT NULL DEFAULT '0',
  PRIMARY KEY (`ClassId`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- character_friends: table
CREATE TABLE `character_friends` (
  `char_id`     int(11)     NOT NULL DEFAULT '0',
  `friend_id`   int(11)     NOT NULL DEFAULT '0',
  `friend_name` varchar(35) NOT NULL DEFAULT '',
  PRIMARY KEY (`char_id`, `friend_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- character_hennas: table
CREATE TABLE `character_hennas` (
  `char_obj_id` int(11) NOT NULL DEFAULT '0',
  `symbol_id`   int(11)          DEFAULT NULL,
  `slot`        int(11) NOT NULL DEFAULT '0',
  `class_index` int(1)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`char_obj_id`, `slot`, `class_index`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- character_macroses: table
CREATE TABLE `character_macroses` (
  `char_obj_id` int(11) NOT NULL DEFAULT '0',
  `id`          int(11) NOT NULL DEFAULT '0',
  `icon`        int(11)          DEFAULT NULL,
  `name`        varchar(40)      DEFAULT NULL,
  `descr`       varchar(80)      DEFAULT NULL,
  `acronym`     varchar(4)       DEFAULT NULL,
  `commands`    varchar(255)     DEFAULT NULL,
  PRIMARY KEY (`char_obj_id`, `id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- character_quests: table
CREATE TABLE `character_quests` (
  `char_id`     int(11)     NOT NULL DEFAULT '0',
  `name`        varchar(40) NOT NULL DEFAULT '',
  `var`         varchar(20) NOT NULL DEFAULT '',
  `value`       varchar(255)         DEFAULT NULL,
  `class_index` int(1)      NOT NULL DEFAULT '0',
  PRIMARY KEY (`char_id`, `name`, `var`, `class_index`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- character_recipebook: table
CREATE TABLE `character_recipebook` (
  `char_id` decimal(11, 0) NOT NULL DEFAULT '0',
  `id`      decimal(11, 0) NOT NULL DEFAULT '0',
  `type`    int(11)        NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`, `char_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- character_recommends: table
CREATE TABLE `character_recommends` (
  `char_id`   int(11) NOT NULL DEFAULT '0',
  `target_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`char_id`, `target_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- character_shortcuts: table
CREATE TABLE `character_shortcuts` (
  `char_obj_id` decimal(11, 0) NOT NULL DEFAULT '0',
  `slot`        decimal(3, 0)  NOT NULL DEFAULT '0',
  `page`        decimal(3, 0)  NOT NULL DEFAULT '0',
  `type`        decimal(3, 0)           DEFAULT NULL,
  `shortcut_id` decimal(16, 0)          DEFAULT NULL,
  `level`       varchar(4)              DEFAULT NULL,
  `class_index` int(1)         NOT NULL DEFAULT '0',
  PRIMARY KEY (`char_obj_id`, `slot`, `page`, `class_index`),
  KEY `shortcut_id` (`shortcut_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- character_skills: table
CREATE TABLE `character_skills` (
  `char_obj_id` int(11) NOT NULL DEFAULT '0',
  `skill_id`    int(11) NOT NULL DEFAULT '0',
  `skill_level` varchar(5)       DEFAULT NULL,
  `skill_name`  varchar(40)      DEFAULT NULL,
  `class_index` int(1)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`char_obj_id`, `skill_id`, `class_index`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- character_skills_save: table
CREATE TABLE `character_skills_save` (
  `char_obj_id`     int(11) NOT NULL DEFAULT '0',
  `skill_id`        int(11) NOT NULL DEFAULT '0',
  `skill_level`     int(11) NOT NULL DEFAULT '0',
  `effect_count`    int(11) NOT NULL DEFAULT '0',
  `effect_cur_time` int(11) NOT NULL DEFAULT '0',
  `reuse_delay`     int(8)  NOT NULL DEFAULT '0',
  `restore_type`    int(1)  NOT NULL DEFAULT '0',
  `class_index`     int(1)  NOT NULL DEFAULT '0',
  `buff_index`      int(2)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`char_obj_id`, `skill_id`, `class_index`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- character_subclasses: table
CREATE TABLE `character_subclasses` (
  `char_obj_id` decimal(11, 0) NOT NULL DEFAULT '0',
  `class_id`    int(2)         NOT NULL DEFAULT '0',
  `exp`         decimal(20, 0) NOT NULL DEFAULT '0',
  `sp`          decimal(11, 0) NOT NULL DEFAULT '0',
  `level`       int(2)         NOT NULL DEFAULT '40',
  `class_index` int(1)         NOT NULL DEFAULT '0',
  PRIMARY KEY (`char_obj_id`, `class_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- characters: table
CREATE TABLE `characters` (
  `account_name`            varchar(45)    NOT NULL,
  `obj_Id`                  decimal(11, 0) NOT NULL DEFAULT '0',
  `char_name`               varchar(35)    NOT NULL,
  `level`                   decimal(11, 0)          DEFAULT '1',
  `maxHp`                   decimal(11, 0)          DEFAULT NULL,
  `curHp`                   decimal(18, 0)          DEFAULT NULL,
  `maxCp`                   decimal(11, 0)          DEFAULT NULL,
  `curCp`                   decimal(18, 0)          DEFAULT NULL,
  `maxMp`                   decimal(11, 0)          DEFAULT NULL,
  `curMp`                   decimal(18, 0)          DEFAULT NULL,
  `acc`                     decimal(11, 0)          DEFAULT NULL,
  `crit`                    decimal(10, 0)          DEFAULT NULL,
  `evasion`                 decimal(11, 0)          DEFAULT NULL,
  `mAtk`                    decimal(11, 0)          DEFAULT NULL,
  `mDef`                    decimal(11, 0)          DEFAULT NULL,
  `mSpd`                    decimal(11, 0)          DEFAULT NULL,
  `pAtk`                    decimal(11, 0)          DEFAULT NULL,
  `pDef`                    decimal(11, 0)          DEFAULT NULL,
  `pSpd`                    decimal(11, 0)          DEFAULT NULL,
  `runSpd`                  decimal(11, 0)          DEFAULT NULL,
  `walkSpd`                 decimal(11, 0)          DEFAULT NULL,
  `str`                     decimal(11, 0)          DEFAULT NULL,
  `con`                     decimal(11, 0)          DEFAULT NULL,
  `dex`                     decimal(11, 0)          DEFAULT NULL,
  `_int`                    decimal(11, 0)          DEFAULT NULL,
  `men`                     decimal(11, 0)          DEFAULT NULL,
  `wit`                     decimal(11, 0)          DEFAULT NULL,
  `face`                    decimal(11, 0)          DEFAULT NULL,
  `hairStyle`               decimal(11, 0)          DEFAULT NULL,
  `hairColor`               decimal(11, 0)          DEFAULT NULL,
  `sex`                     decimal(11, 0)          DEFAULT NULL,
  `heading`                 decimal(11, 0)          DEFAULT NULL,
  `x`                       decimal(11, 0)          DEFAULT NULL,
  `y`                       decimal(11, 0)          DEFAULT NULL,
  `z`                       decimal(11, 0)          DEFAULT NULL,
  `movement_multiplier`     decimal(9, 8)           DEFAULT NULL,
  `attack_speed_multiplier` decimal(10, 9)          DEFAULT NULL,
  `colRad`                  decimal(10, 3)          DEFAULT NULL,
  `colHeight`               decimal(10, 3)          DEFAULT NULL,
  `exp`                     decimal(20, 0)          DEFAULT NULL,
  `expBeforeDeath`          decimal(20, 0)          DEFAULT '0',
  `sp`                      decimal(11, 0)          DEFAULT NULL,
  `karma`                   decimal(11, 0)          DEFAULT NULL,
  `pvpkills`                decimal(11, 0)          DEFAULT NULL,
  `pkkills`                 decimal(11, 0)          DEFAULT NULL,
  `clanid`                  decimal(11, 0)          DEFAULT NULL,
  `maxload`                 decimal(11, 0)          DEFAULT NULL,
  `race`                    decimal(11, 0)          DEFAULT NULL,
  `classid`                 decimal(11, 0)          DEFAULT NULL,
  `base_class`              int(2)         NOT NULL DEFAULT '0',
  `deletetime`              decimal(20, 0)          DEFAULT NULL,
  `cancraft`                decimal(11, 0)          DEFAULT NULL,
  `title`                   varchar(16)             DEFAULT NULL,
  `rec_have`                int(3)         NOT NULL DEFAULT '0',
  `rec_left`                int(3)         NOT NULL DEFAULT '0',
  `accesslevel`             decimal(4, 0)           DEFAULT NULL,
  `online`                  decimal(1, 0)           DEFAULT NULL,
  `onlinetime`              decimal(20, 0)          DEFAULT NULL,
  `char_slot`               decimal(1, 0)           DEFAULT NULL,
  `newbie`                  decimal(1, 0)           DEFAULT '1',
  `lastAccess`              decimal(20, 0)          DEFAULT NULL,
  `clan_privs`              int(11)                 DEFAULT '0',
  `wantspeace`              decimal(1, 0)           DEFAULT '0',
  `isin7sdungeon`           decimal(1, 0)  NOT NULL DEFAULT '0',
  `in_jail`                 decimal(1, 0)           DEFAULT '0',
  `jail_timer`              decimal(20, 0)          DEFAULT '0',
  `power_grade`             decimal(11, 0)          DEFAULT NULL,
  `nobless`                 decimal(1, 0)  NOT NULL DEFAULT '0',
  `subpledge`               int(1)         NOT NULL DEFAULT '0',
  `last_recom_date`         decimal(20, 0) NOT NULL DEFAULT '0',
  `lvl_joined_academy`      int(1)         NOT NULL DEFAULT '0',
  `apprentice`              int(1)         NOT NULL DEFAULT '0',
  `sponsor`                 int(1)         NOT NULL DEFAULT '0',
  `varka_ketra_ally`        int(1)         NOT NULL DEFAULT '0',
  `clan_join_expiry_time`   decimal(20, 0) NOT NULL DEFAULT '0',
  `clan_create_expiry_time` decimal(20, 0) NOT NULL DEFAULT '0',
  `death_penalty_level`     int(2)         NOT NULL DEFAULT '0',
  PRIMARY KEY (`obj_Id`),
  KEY `clanid` (`clanid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- clan_data: table
CREATE TABLE `clan_data` (
  `clan_id`                  int(11)        NOT NULL DEFAULT '0',
  `clan_name`                varchar(45)             DEFAULT NULL,
  `clan_level`               int(11)                 DEFAULT NULL,
  `reputation_score`         int(11)        NOT NULL DEFAULT '0',
  `hasCastle`                int(11)                 DEFAULT NULL,
  `ally_id`                  int(11)                 DEFAULT NULL,
  `ally_name`                varchar(45)             DEFAULT NULL,
  `leader_id`                int(11)                 DEFAULT NULL,
  `crest_id`                 int(11)                 DEFAULT NULL,
  `crest_large_id`           int(11)                 DEFAULT NULL,
  `ally_crest_id`            int(11)                 DEFAULT NULL,
  `auction_bid_at`           int(11)                 DEFAULT '0',
  `ally_penalty_expiry_time` decimal(20, 0) NOT NULL DEFAULT '0',
  `ally_penalty_type`        decimal(1, 0)  NOT NULL DEFAULT '0',
  `char_penalty_expiry_time` decimal(20, 0) NOT NULL DEFAULT '0',
  `dissolving_expiry_time`   decimal(20, 0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`clan_id`),
  KEY `leader_id` (`leader_id`),
  KEY `ally_id` (`ally_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- clan_privs: table
CREATE TABLE `clan_privs` (
  `clan_id` int(11) NOT NULL DEFAULT '0',
  `rank`    int(11) NOT NULL DEFAULT '0',
  `party`   int(11) NOT NULL DEFAULT '0',
  `privs`   int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`clan_id`, `rank`, `party`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- clan_skills: table
CREATE TABLE `clan_skills` (
  `clan_id`     int(11) NOT NULL DEFAULT '0',
  `skill_id`    int(11) NOT NULL DEFAULT '0',
  `skill_level` int(5)  NOT NULL DEFAULT '0',
  `skill_name`  varchar(26)      DEFAULT NULL,
  PRIMARY KEY (`clan_id`, `skill_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- clan_subpledges: table
CREATE TABLE `clan_subpledges` (
  `clan_id`       int(11) NOT NULL DEFAULT '0',
  `sub_pledge_id` int(11) NOT NULL DEFAULT '0',
  `name`          varchar(45)      DEFAULT NULL,
  `leader_name`   varchar(35)      DEFAULT NULL,
  PRIMARY KEY (`clan_id`, `sub_pledge_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- clan_wars: table
CREATE TABLE `clan_wars` (
  `clan1`       varchar(35)   NOT NULL DEFAULT '',
  `clan2`       varchar(35)   NOT NULL DEFAULT '',
  `wantspeace1` decimal(1, 0) NOT NULL DEFAULT '0',
  `wantspeace2` decimal(1, 0) NOT NULL DEFAULT '0'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- clanhall: table
CREATE TABLE `clanhall` (
  `id`          int(11)        NOT NULL DEFAULT '0',
  `name`        varchar(40)    NOT NULL DEFAULT '',
  `ownerId`     int(11)        NOT NULL DEFAULT '0',
  `lease`       int(10)        NOT NULL DEFAULT '0',
  `description` text           NOT NULL,
  `location`    varchar(15)    NOT NULL DEFAULT '',
  `paidUntil`   decimal(20, 0) NOT NULL DEFAULT '0',
  `Grade`       decimal(1, 0)  NOT NULL DEFAULT '0',
  `paid`        int(1)         NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- clanhall_functions: table
CREATE TABLE `clanhall_functions` (
  `hall_id` int(2)         NOT NULL DEFAULT '0',
  `type`    int(1)         NOT NULL DEFAULT '0',
  `lvl`     int(3)         NOT NULL DEFAULT '0',
  `lease`   int(10)        NOT NULL DEFAULT '0',
  `rate`    decimal(20, 0) NOT NULL DEFAULT '0',
  `endTime` decimal(20, 0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`hall_id`, `type`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- cursed_weapons: table
CREATE TABLE `cursed_weapons` (
  `itemId`        int(11) NOT NULL,
  `playerId`      int(11)        DEFAULT '0',
  `playerKarma`   int(11)        DEFAULT '0',
  `playerPkKills` int(11)        DEFAULT '0',
  `nbKills`       int(11)        DEFAULT '0',
  `endTime`       decimal(20, 0) DEFAULT '0',
  PRIMARY KEY (`itemId`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- dimensional_rift: table
CREATE TABLE `dimensional_rift` (
  `type`    tinyint(2) NOT NULL,
  `room_id` tinyint(2) NOT NULL,
  `xMin`    int(11)    NOT NULL,
  `xMax`    int(11)    NOT NULL,
  `yMin`    int(11)    NOT NULL,
  `yMax`    int(11)    NOT NULL,
  `zMin`    int(11)    NOT NULL,
  `zMax`    int(11)    NOT NULL,
  `xT`      int(11)    NOT NULL,
  `yT`      int(11)    NOT NULL,
  `zT`      int(11)    NOT NULL,
  `boss`    tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`type`, `room_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- droplist: table
CREATE TABLE `droplist` (
  `mobId`    int(11) NOT NULL DEFAULT '0',
  `itemId`   int(11) NOT NULL DEFAULT '0',
  `min`      int(11) NOT NULL DEFAULT '0',
  `max`      int(11) NOT NULL DEFAULT '0',
  `category` int(11) NOT NULL DEFAULT '0',
  `chance`   int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`mobId`, `itemId`, `category`),
  KEY `key_mobId` (`mobId`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- enchant_skill_trees: table
CREATE TABLE `enchant_skill_trees` (
  `skill_id`       int(10)         NOT NULL DEFAULT '0',
  `level`          int(10)         NOT NULL DEFAULT '0',
  `name`           varchar(25)     NOT NULL DEFAULT '',
  `base_lvl`       int(2)          NOT NULL DEFAULT '0',
  `enchant_type`   varchar(25)              DEFAULT NULL,
  `sp`             int(10)         NOT NULL DEFAULT '0',
  `exp`            int(20)                  DEFAULT NULL,
  `min_skill_lvl`  int(2)          NOT NULL DEFAULT '0',
  `success_rate76` int(3) unsigned NOT NULL DEFAULT '0',
  `success_rate77` int(3)          NOT NULL DEFAULT '0',
  `success_rate78` int(3)          NOT NULL DEFAULT '0',
  PRIMARY KEY (`skill_id`, `level`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- etcitem: table
CREATE TABLE `etcitem` (
  `item_id`        decimal(11, 0)       NOT NULL DEFAULT '0',
  `name`           varchar(100)                  DEFAULT NULL,
  `crystallizable` varchar(5)                    DEFAULT NULL,
  `item_type`      varchar(15)                   DEFAULT NULL,
  `weight`         decimal(4, 0)                 DEFAULT NULL,
  `consume_type`   varchar(9)                    DEFAULT NULL,
  `material`       varchar(11)                   DEFAULT NULL,
  `crystal_type`   varchar(4)                    DEFAULT NULL,
  `duration`       decimal(3, 0)                 DEFAULT NULL,
  `price`          decimal(11, 0)                DEFAULT NULL,
  `crystal_count`  int(4)                        DEFAULT NULL,
  `sellable`       varchar(5)                    DEFAULT NULL,
  `dropable`       varchar(5)                    DEFAULT NULL,
  `destroyable`    varchar(5)                    DEFAULT NULL,
  `tradeable`      varchar(5)                    DEFAULT NULL,
  `oldname`        varchar(100)         NOT NULL DEFAULT '',
  `oldtype`        varchar(100)         NOT NULL DEFAULT '',
  `drop_category`  enum ('0', '1', '2') NOT NULL DEFAULT '2',
  PRIMARY KEY (`item_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- fish: table
CREATE TABLE `fish` (
  `id`              int(5)      NOT NULL DEFAULT '0',
  `level`           int(5)      NOT NULL DEFAULT '0',
  `name`            varchar(40) NOT NULL DEFAULT '',
  `hp`              int(5)      NOT NULL DEFAULT '0',
  `hpregen`         int(3)      NOT NULL DEFAULT '5',
  `fish_type`       int(1)      NOT NULL DEFAULT '0',
  `fish_group`      int(1)      NOT NULL DEFAULT '0',
  `fish_guts`       int(4)      NOT NULL DEFAULT '0',
  `guts_check_time` int(4)      NOT NULL DEFAULT '0',
  `wait_time`       int(5)      NOT NULL DEFAULT '0',
  `combat_time`     int(5)      NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`, `level`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- fishing_skill_trees: table
CREATE TABLE `fishing_skill_trees` (
  `skill_id`   int(10)     NOT NULL DEFAULT '0',
  `level`      int(10)     NOT NULL DEFAULT '0',
  `name`       varchar(25) NOT NULL DEFAULT '',
  `sp`         int(10)     NOT NULL DEFAULT '0',
  `min_level`  int(10)     NOT NULL DEFAULT '0',
  `costid`     int(10)     NOT NULL DEFAULT '0',
  `cost`       int(10)     NOT NULL DEFAULT '0',
  `isfordwarf` int(1)      NOT NULL DEFAULT '0',
  PRIMARY KEY (`skill_id`, `level`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- forums: table
CREATE TABLE `forums` (
  `forum_id`       int(8)       NOT NULL DEFAULT '0',
  `forum_name`     varchar(255) NOT NULL DEFAULT '',
  `forum_parent`   int(8)       NOT NULL DEFAULT '0',
  `forum_post`     int(8)       NOT NULL DEFAULT '0',
  `forum_type`     int(8)       NOT NULL DEFAULT '0',
  `forum_perm`     int(8)       NOT NULL DEFAULT '0',
  `forum_owner_id` int(8)       NOT NULL DEFAULT '0',
  UNIQUE KEY `forum_id` (`forum_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- games: table
CREATE TABLE `games` (
  `id`       int(11)        NOT NULL DEFAULT '0',
  `idnr`     int(11)        NOT NULL DEFAULT '0',
  `number1`  int(11)        NOT NULL DEFAULT '0',
  `number2`  int(11)        NOT NULL DEFAULT '0',
  `prize`    int(11)        NOT NULL DEFAULT '0',
  `newprize` int(11)        NOT NULL DEFAULT '0',
  `prize1`   int(11)        NOT NULL DEFAULT '0',
  `prize2`   int(11)        NOT NULL DEFAULT '0',
  `prize3`   int(11)        NOT NULL DEFAULT '0',
  `enddate`  decimal(20, 0) NOT NULL DEFAULT '0',
  `finished` int(11)        NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`, `idnr`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- gameservers: table
CREATE TABLE `gameservers` (
  `server_id` int(11)     NOT NULL DEFAULT '0',
  `hexid`     varchar(50) NOT NULL DEFAULT '',
  `host`      varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`server_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- global_tasks: table
CREATE TABLE `global_tasks` (
  `id`              int(11)        NOT NULL AUTO_INCREMENT,
  `task`            varchar(50)    NOT NULL DEFAULT '',
  `type`            varchar(50)    NOT NULL DEFAULT '',
  `last_activation` decimal(20, 0) NOT NULL DEFAULT '0',
  `param1`          varchar(100)   NOT NULL DEFAULT '',
  `param2`          varchar(100)   NOT NULL DEFAULT '',
  `param3`          varchar(255)   NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- helper_buff_list: table
CREATE TABLE `helper_buff_list` (
  `id`             int(11)          NOT NULL DEFAULT '0',
  `skill_id`       int(10) unsigned NOT NULL DEFAULT '0',
  `name`           varchar(25)      NOT NULL DEFAULT '',
  `skill_level`    int(10) unsigned NOT NULL DEFAULT '0',
  `lower_level`    int(10) unsigned NOT NULL DEFAULT '0',
  `upper_level`    int(10) unsigned NOT NULL DEFAULT '0',
  `is_magic_class` varchar(5)                DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- henna: table
CREATE TABLE `henna` (
  `symbol_id`   int(11) NOT NULL DEFAULT '0',
  `symbol_name` varchar(45)      DEFAULT NULL,
  `dye_id`      int(11)          DEFAULT NULL,
  `dye_amount`  int(11)          DEFAULT NULL,
  `price`       int(11)          DEFAULT NULL,
  `stat_INT`    decimal(11, 0)   DEFAULT NULL,
  `stat_STR`    decimal(11, 0)   DEFAULT NULL,
  `stat_CON`    decimal(11, 0)   DEFAULT NULL,
  `stat_MEM`    decimal(11, 0)   DEFAULT NULL,
  `stat_DEX`    decimal(11, 0)   DEFAULT NULL,
  `stat_WIT`    decimal(11, 0)   DEFAULT NULL,
  PRIMARY KEY (`symbol_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- henna_trees: table
CREATE TABLE `henna_trees` (
  `class_id`  decimal(10, 0) NOT NULL DEFAULT '0',
  `symbol_id` decimal(10, 0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`class_id`, `symbol_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- heroes: table
CREATE TABLE `heroes` (
  `char_id`   decimal(11, 0) NOT NULL DEFAULT '0',
  `char_name` varchar(45)    NOT NULL DEFAULT '',
  `class_id`  decimal(3, 0)  NOT NULL DEFAULT '0',
  `count`     decimal(3, 0)  NOT NULL DEFAULT '0',
  `played`    decimal(1, 0)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`char_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- items: table
CREATE TABLE `items` (
  `owner_id`      int(11) DEFAULT NULL,
  `object_id`     int(11)       NOT NULL                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             DEFAULT '0',
  `item_id`       int(11) DEFAULT NULL,
  `count`         int(11) DEFAULT NULL,
  `enchant_level` int(11) DEFAULT NULL,
  `loc`           varchar(10) DEFAULT NULL,
  `loc_data`      int(11) DEFAULT NULL,
  `price_sell`    int(11) DEFAULT NULL,
  `price_buy`     int(11) DEFAULT NULL,
  `time_of_use`   int(11) DEFAULT NULL,
  `custom_type1`  int(11) DEFAULT '0',
  `custom_type2`  int(11) DEFAULT '0',
  `mana_left`     decimal(3, 0) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`object_id`),
  KEY `key_owner_id` (`owner_id`),
  KEY `key_loc` (`loc`),
  KEY `key_item_id` (`item_id`),
  KEY `key_time_of_use` (`time_of_use`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- itemsonground: table
CREATE TABLE `itemsonground` (
  `object_id`     int(11) NOT NULL DEFAULT '0',
  `item_id`       int(11)          DEFAULT NULL,
  `count`         int(11)          DEFAULT NULL,
  `enchant_level` int(11)          DEFAULT NULL,
  `x`             int(11)          DEFAULT NULL,
  `y`             int(11)          DEFAULT NULL,
  `z`             int(11)          DEFAULT NULL,
  `drop_time`     decimal(20, 0)   DEFAULT NULL,
  `equipable`     int(1)           DEFAULT '0',
  PRIMARY KEY (`object_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- locations: table
CREATE TABLE `locations` (
  `loc_id`   int(9) NOT NULL DEFAULT '0',
  `loc_x`    int(9) NOT NULL DEFAULT '0',
  `loc_y`    int(9) NOT NULL DEFAULT '0',
  `loc_zmin` int(9) NOT NULL DEFAULT '0',
  `loc_zmax` int(9) NOT NULL DEFAULT '0',
  `proc`     int(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`loc_id`, `loc_x`, `loc_y`),
  KEY `proc` (`proc`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- mapregion: table
CREATE TABLE `mapregion` (
  `region` int(11) NOT NULL DEFAULT '0',
  `sec0`   int(2)  NOT NULL DEFAULT '0',
  `sec1`   int(2)  NOT NULL DEFAULT '0',
  `sec2`   int(2)  NOT NULL DEFAULT '0',
  `sec3`   int(2)  NOT NULL DEFAULT '0',
  `sec4`   int(2)  NOT NULL DEFAULT '0',
  `sec5`   int(2)  NOT NULL DEFAULT '0',
  `sec6`   int(2)  NOT NULL DEFAULT '0',
  `sec7`   int(2)  NOT NULL DEFAULT '0',
  `sec8`   int(2)  NOT NULL DEFAULT '0',
  `sec9`   int(2)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`region`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- merchant_areas_list: table
CREATE TABLE `merchant_areas_list` (
  `merchant_area_id`   int(10) unsigned      NOT NULL DEFAULT '0',
  `merchant_area_name` varchar(25)           NOT NULL DEFAULT '',
  `tax`                double(3, 2) unsigned NOT NULL DEFAULT '0.00',
  `Chaotic`            int(11)               NOT NULL DEFAULT '0',
  PRIMARY KEY (`merchant_area_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- merchant_buylists: table
CREATE TABLE `merchant_buylists` (
  `item_id`      decimal(9, 0)  NOT NULL DEFAULT '0',
  `price`        decimal(11, 0) NOT NULL DEFAULT '0',
  `shop_id`      decimal(9, 0)  NOT NULL DEFAULT '0',
  `ordering`     decimal(4, 0)  NOT NULL DEFAULT '0',
  `count`        int(11)        NOT NULL DEFAULT '-1',
  `currentCount` int(11)        NOT NULL DEFAULT '-1',
  `time`         int(11)        NOT NULL DEFAULT '0',
  `savetimer`    decimal(20, 0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`shop_id`, `ordering`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- merchant_lease: table
CREATE TABLE `merchant_lease` (
  `merchant_id` int(11) NOT NULL DEFAULT '0',
  `player_id`   int(11) NOT NULL DEFAULT '0',
  `bid`         int(11)          DEFAULT NULL,
  `type`        int(11) NOT NULL DEFAULT '0',
  `player_name` varchar(35)      DEFAULT NULL,
  PRIMARY KEY (`merchant_id`, `player_id`, `type`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- merchant_shopids: table
CREATE TABLE `merchant_shopids` (
  `shop_id` decimal(9, 0) NOT NULL DEFAULT '0',
  `npc_id`  varchar(9)             DEFAULT NULL,
  PRIMARY KEY (`shop_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- merchants: table
CREATE TABLE `merchants` (
  `npc_id`           int(11) NOT NULL DEFAULT '0',
  `merchant_area_id` tinyint(4)       DEFAULT NULL,
  PRIMARY KEY (`npc_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- minions: table
CREATE TABLE `minions` (
  `boss_id`    int(11) NOT NULL DEFAULT '0',
  `minion_id`  int(11) NOT NULL DEFAULT '0',
  `amount_min` int(4)  NOT NULL DEFAULT '0',
  `amount_max` int(4)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`boss_id`, `minion_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- mods_wedding: table
CREATE TABLE `mods_wedding` (
  `id`           int(11) NOT NULL AUTO_INCREMENT,
  `player1Id`    int(11) NOT NULL DEFAULT '0',
  `player2Id`    int(11) NOT NULL DEFAULT '0',
  `married`      varchar(5)       DEFAULT NULL,
  `affianceDate` decimal(20, 0)   DEFAULT '0',
  `weddingDate`  decimal(20, 0)   DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- npc: table
CREATE TABLE `npc` (
  `id`               decimal(11, 0)                                      NOT NULL DEFAULT '0',
  `idTemplate`       int(11)                                             NOT NULL DEFAULT '0',
  `name`             varchar(200)                                                 DEFAULT NULL,
  `serverSideName`   int(1)                                                       DEFAULT '0',
  `title`            varchar(45)                                                  DEFAULT '',
  `serverSideTitle`  int(1)                                                       DEFAULT '0',
  `collision_radius` decimal(5, 2)                                                DEFAULT NULL,
  `collision_height` decimal(5, 2)                                                DEFAULT NULL,
  `level`            decimal(2, 0)                                                DEFAULT NULL,
  `sex`              varchar(6)                                                   DEFAULT NULL,
  `type`             varchar(20)                                                  DEFAULT NULL,
  `attackrange`      int(11)                                                      DEFAULT NULL,
  `hp`               decimal(8, 0)                                                DEFAULT NULL,
  `mp`               decimal(5, 0)                                                DEFAULT NULL,
  `hpreg`            decimal(8, 2)                                                DEFAULT NULL,
  `mpreg`            decimal(5, 2)                                                DEFAULT NULL,
  `str`              decimal(7, 0)                                                DEFAULT NULL,
  `con`              decimal(7, 0)                                                DEFAULT NULL,
  `dex`              decimal(7, 0)                                                DEFAULT NULL,
  `intelligence`     decimal(7, 0)                                                DEFAULT NULL,
  `wit`              decimal(7, 0)                                                DEFAULT NULL,
  `men`              decimal(7, 0)                                                DEFAULT NULL,
  `exp`              decimal(9, 0)                                                DEFAULT NULL,
  `sp`               decimal(8, 0)                                                DEFAULT NULL,
  `patk`             decimal(5, 0)                                                DEFAULT NULL,
  `pdef`             decimal(5, 0)                                                DEFAULT NULL,
  `matk`             decimal(5, 0)                                                DEFAULT NULL,
  `mdef`             decimal(5, 0)                                                DEFAULT NULL,
  `atkspd`           decimal(3, 0)                                                DEFAULT NULL,
  `aggro`            decimal(6, 0)                                                DEFAULT NULL,
  `matkspd`          decimal(4, 0)                                                DEFAULT NULL,
  `rhand`            decimal(4, 0)                                                DEFAULT NULL,
  `lhand`            decimal(4, 0)                                                DEFAULT NULL,
  `armor`            decimal(1, 0)                                                DEFAULT NULL,
  `walkspd`          decimal(3, 0)                                                DEFAULT NULL,
  `runspd`           decimal(3, 0)                                                DEFAULT NULL,
  `faction_id`       varchar(40)                                                  DEFAULT NULL,
  `faction_range`    decimal(4, 0)                                                DEFAULT NULL,
  `isUndead`         int(11)                                                      DEFAULT '0',
  `absorb_level`     decimal(2, 0)                                                DEFAULT '0',
  `absorb_type`      enum ('FULL_PARTY', 'LAST_HIT', 'PARTY_ONE_RANDOM') NOT NULL DEFAULT 'LAST_HIT',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- npcskills: table
CREATE TABLE `npcskills` (
  `npcid`   int(11) NOT NULL DEFAULT '0',
  `skillid` int(11) NOT NULL DEFAULT '0',
  `level`   int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`npcid`, `skillid`, `level`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- olympiad_nobles: table
CREATE TABLE `olympiad_nobles` (
  `char_id`           decimal(11, 0) NOT NULL DEFAULT '0',
  `class_id`          decimal(3, 0)  NOT NULL DEFAULT '0',
  `char_name`         varchar(45)    NOT NULL DEFAULT '',
  `olympiad_points`   decimal(10, 0) NOT NULL DEFAULT '0',
  `competitions_done` decimal(3, 0)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`char_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- pets: table
CREATE TABLE `pets` (
  `item_obj_id` decimal(11, 0) NOT NULL DEFAULT '0',
  `name`        varchar(16)             DEFAULT NULL,
  `level`       decimal(11, 0)          DEFAULT NULL,
  `curHp`       decimal(18, 0)          DEFAULT NULL,
  `curMp`       decimal(18, 0)          DEFAULT NULL,
  `exp`         decimal(20, 0)          DEFAULT NULL,
  `sp`          decimal(11, 0)          DEFAULT NULL,
  `karma`       decimal(11, 0)          DEFAULT NULL,
  `pkkills`     decimal(11, 0)          DEFAULT NULL,
  `fed`         decimal(11, 0)          DEFAULT NULL,
  PRIMARY KEY (`item_obj_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- pets_stats: table
CREATE TABLE `pets_stats` (
  `type`            varchar(25)   NOT NULL DEFAULT '',
  `typeID`          int(5)        NOT NULL DEFAULT '0',
  `level`           int(11)       NOT NULL DEFAULT '0',
  `expMax`          int(20)       NOT NULL DEFAULT '0',
  `hpMax`           int(11)       NOT NULL DEFAULT '0',
  `mpMax`           int(11)       NOT NULL DEFAULT '0',
  `patk`            int(11)       NOT NULL DEFAULT '0',
  `pdef`            int(11)       NOT NULL DEFAULT '0',
  `matk`            int(11)       NOT NULL DEFAULT '0',
  `mdef`            int(11)       NOT NULL DEFAULT '0',
  `acc`             int(11)       NOT NULL DEFAULT '0',
  `evasion`         int(11)       NOT NULL DEFAULT '0',
  `crit`            int(11)       NOT NULL DEFAULT '0',
  `speed`           int(11)       NOT NULL DEFAULT '0',
  `atk_speed`       int(11)       NOT NULL DEFAULT '0',
  `cast_speed`      int(11)       NOT NULL DEFAULT '0',
  `feedMax`         int(11)       NOT NULL DEFAULT '0',
  `feedbattle`      int(11)       NOT NULL DEFAULT '0',
  `feednormal`      int(11)       NOT NULL DEFAULT '0',
  `loadMax`         int(11)       NOT NULL DEFAULT '0',
  `hpregen`         int(11)       NOT NULL DEFAULT '0',
  `mpregen`         int(11)       NOT NULL DEFAULT '0',
  `owner_exp_taken` decimal(3, 2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`typeID`, `level`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- pledge_skill_trees: table
CREATE TABLE `pledge_skill_trees` (
  `skill_id`    int(11)      DEFAULT NULL,
  `level`       int(11)      DEFAULT NULL,
  `name`        varchar(25)  DEFAULT NULL,
  `clan_lvl`    int(11)      DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `repCost`     int(11)      DEFAULT NULL,
  `itemId`      int(11)      DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- posts: table
CREATE TABLE `posts` (
  `post_id`         int(8)         NOT NULL DEFAULT '0',
  `post_owner_name` varchar(255)   NOT NULL DEFAULT '',
  `post_ownerid`    int(8)         NOT NULL DEFAULT '0',
  `post_date`       decimal(20, 0) NOT NULL DEFAULT '0',
  `post_topic_id`   int(8)         NOT NULL DEFAULT '0',
  `post_forum_id`   int(8)         NOT NULL DEFAULT '0',
  `post_txt`        text           NOT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- quest_global_data: table
CREATE TABLE `quest_global_data` (
  `quest_name` varchar(40) NOT NULL DEFAULT '',
  `var`        varchar(20) NOT NULL DEFAULT '',
  `value`      varchar(255)         DEFAULT NULL,
  PRIMARY KEY (`quest_name`, `var`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- raidboss_spawnlist: table
CREATE TABLE `raidboss_spawnlist` (
  `boss_id`           int(11)    NOT NULL DEFAULT '0',
  `amount`            int(11)    NOT NULL DEFAULT '0',
  `loc_x`             int(11)    NOT NULL DEFAULT '0',
  `loc_y`             int(11)    NOT NULL DEFAULT '0',
  `loc_z`             int(11)    NOT NULL DEFAULT '0',
  `heading`           int(11)    NOT NULL DEFAULT '0',
  `respawn_min_delay` int(11)    NOT NULL DEFAULT '43200',
  `respawn_max_delay` int(11)    NOT NULL DEFAULT '129600',
  `respawn_time`      bigint(20) NOT NULL DEFAULT '0',
  `currentHp`         decimal(8, 0)       DEFAULT NULL,
  `currentMp`         decimal(8, 0)       DEFAULT NULL,
  PRIMARY KEY (`boss_id`, `loc_x`, `loc_y`, `loc_z`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- random_spawn: table
CREATE TABLE `random_spawn` (
  `groupId`        int(11)    NOT NULL DEFAULT '0',
  `npcId`          int(11)    NOT NULL DEFAULT '0',
  `count`          int(11)    NOT NULL DEFAULT '0',
  `initialDelay`   bigint(20) NOT NULL DEFAULT '-1',
  `respawnDelay`   bigint(20) NOT NULL DEFAULT '-1',
  `despawnDelay`   bigint(20) NOT NULL DEFAULT '-1',
  `broadcastSpawn` varchar(5) NOT NULL DEFAULT 'false',
  `randomSpawn`    varchar(5) NOT NULL DEFAULT 'true',
  PRIMARY KEY (`groupId`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- random_spawn_loc: table
CREATE TABLE `random_spawn_loc` (
  `groupId` int(11) NOT NULL DEFAULT '0',
  `x`       int(11) NOT NULL DEFAULT '0',
  `y`       int(11) NOT NULL DEFAULT '0',
  `z`       int(11) NOT NULL DEFAULT '0',
  `heading` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`groupId`, `x`, `y`, `z`, `heading`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- seven_signs: table
CREATE TABLE `seven_signs` (
  `char_obj_id`          int(11)        NOT NULL DEFAULT '0',
  `cabal`                varchar(4)     NOT NULL DEFAULT '',
  `seal`                 int(1)         NOT NULL DEFAULT '0',
  `red_stones`           int(11)        NOT NULL DEFAULT '0',
  `green_stones`         int(11)        NOT NULL DEFAULT '0',
  `blue_stones`          int(11)        NOT NULL DEFAULT '0',
  `ancient_adena_amount` decimal(20, 0) NOT NULL DEFAULT '0',
  `contribution_score`   decimal(20, 0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`char_obj_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- seven_signs_festival: table
CREATE TABLE `seven_signs_festival` (
  `festivalId` int(1)       NOT NULL DEFAULT '0',
  `cabal`      varchar(4)   NOT NULL DEFAULT '',
  `cycle`      int(4)       NOT NULL DEFAULT '0',
  `date`       bigint(50)            DEFAULT '0',
  `score`      int(5)       NOT NULL DEFAULT '0',
  `members`    varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`festivalId`, `cabal`, `cycle`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- seven_signs_status: table
CREATE TABLE `seven_signs_status` (
  `id`                  int(3)         NOT NULL DEFAULT '0',
  `current_cycle`       int(10)        NOT NULL DEFAULT '1',
  `festival_cycle`      int(10)        NOT NULL DEFAULT '1',
  `active_period`       int(10)        NOT NULL DEFAULT '1',
  `date`                int(10)        NOT NULL DEFAULT '1',
  `previous_winner`     int(10)        NOT NULL DEFAULT '0',
  `dawn_stone_score`    decimal(20, 0) NOT NULL DEFAULT '0',
  `dawn_festival_score` int(10)        NOT NULL DEFAULT '0',
  `dusk_stone_score`    decimal(20, 0) NOT NULL DEFAULT '0',
  `dusk_festival_score` int(10)        NOT NULL DEFAULT '0',
  `avarice_owner`       int(10)        NOT NULL DEFAULT '0',
  `gnosis_owner`        int(10)        NOT NULL DEFAULT '0',
  `strife_owner`        int(10)        NOT NULL DEFAULT '0',
  `avarice_dawn_score`  int(10)        NOT NULL DEFAULT '0',
  `gnosis_dawn_score`   int(10)        NOT NULL DEFAULT '0',
  `strife_dawn_score`   int(10)        NOT NULL DEFAULT '0',
  `avarice_dusk_score`  int(10)        NOT NULL DEFAULT '0',
  `gnosis_dusk_score`   int(10)        NOT NULL DEFAULT '0',
  `strife_dusk_score`   int(10)        NOT NULL DEFAULT '0',
  `accumulated_bonus0`  int(10)        NOT NULL DEFAULT '0',
  `accumulated_bonus1`  int(10)        NOT NULL DEFAULT '0',
  `accumulated_bonus2`  int(10)        NOT NULL DEFAULT '0',
  `accumulated_bonus3`  int(10)        NOT NULL DEFAULT '0',
  `accumulated_bonus4`  int(10)        NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- siege_clans: table
CREATE TABLE `siege_clans` (
  `castle_id`    int(1)  NOT NULL DEFAULT '0',
  `clan_id`      int(11) NOT NULL DEFAULT '0',
  `type`         int(1)           DEFAULT NULL,
  `castle_owner` int(1)           DEFAULT NULL,
  PRIMARY KEY (`clan_id`, `castle_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- skill_learn: table
CREATE TABLE `skill_learn` (
  `npc_id`   int(11) NOT NULL DEFAULT '0',
  `class_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`npc_id`, `class_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- skill_spellbooks: table
CREATE TABLE `skill_spellbooks` (
  `skill_id` int(11) NOT NULL DEFAULT '-1',
  `item_id`  int(11) NOT NULL DEFAULT '-1',
  KEY `skill_id` (`skill_id`, `item_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- skill_trees: table
CREATE TABLE `skill_trees` (
  `class_id`  int(10) unsigned NOT NULL DEFAULT '0',
  `skill_id`  int(10) unsigned NOT NULL DEFAULT '0',
  `level`     int(10) unsigned NOT NULL DEFAULT '0',
  `name`      varchar(40)      NOT NULL DEFAULT '',
  `sp`        int(10) unsigned NOT NULL DEFAULT '0',
  `min_level` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`class_id`, `skill_id`, `level`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- spawnlist: table
CREATE TABLE `spawnlist` (
  `id`             int(11)     NOT NULL AUTO_INCREMENT,
  `location`       varchar(40) NOT NULL DEFAULT '',
  `count`          int(9)      NOT NULL DEFAULT '0',
  `npc_templateid` int(9)      NOT NULL DEFAULT '0',
  `locx`           int(9)      NOT NULL DEFAULT '0',
  `locy`           int(9)      NOT NULL DEFAULT '0',
  `locz`           int(9)      NOT NULL DEFAULT '0',
  `randomx`        int(9)      NOT NULL DEFAULT '0',
  `randomy`        int(9)      NOT NULL DEFAULT '0',
  `heading`        int(9)      NOT NULL DEFAULT '0',
  `respawn_delay`  int(9)      NOT NULL DEFAULT '0',
  `loc_id`         int(9)      NOT NULL DEFAULT '0',
  `periodOfDay`    decimal(2, 0)        DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `key_npc_templateid` (`npc_templateid`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 76235
  DEFAULT CHARSET = latin1;

-- teleport: table
CREATE TABLE `teleport` (
  `Description` varchar(75)             DEFAULT NULL,
  `id`          decimal(11, 0) NOT NULL DEFAULT '0',
  `loc_x`       decimal(9, 0)           DEFAULT NULL,
  `loc_y`       decimal(9, 0)           DEFAULT NULL,
  `loc_z`       decimal(9, 0)           DEFAULT NULL,
  `price`       decimal(6, 0)           DEFAULT NULL,
  `fornoble`    int(1)         NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- topic: table
CREATE TABLE `topic` (
  `topic_id`        int(8)         NOT NULL DEFAULT '0',
  `topic_forum_id`  int(8)         NOT NULL DEFAULT '0',
  `topic_name`      varchar(255)   NOT NULL DEFAULT '',
  `topic_date`      decimal(20, 0) NOT NULL DEFAULT '0',
  `topic_ownername` varchar(255)   NOT NULL DEFAULT '0',
  `topic_ownerid`   int(8)         NOT NULL DEFAULT '0',
  `topic_type`      int(8)         NOT NULL DEFAULT '0',
  `topic_reply`     int(8)         NOT NULL DEFAULT '0'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- walker_routes: table
CREATE TABLE `walker_routes` (
  `route_id`   int(11)    NOT NULL DEFAULT '0',
  `npc_id`     int(11)    NOT NULL DEFAULT '0',
  `move_point` int(9)     NOT NULL,
  `chatText`   varchar(255)        DEFAULT NULL,
  `move_x`     int(9)     NOT NULL DEFAULT '0',
  `move_y`     int(9)     NOT NULL DEFAULT '0',
  `move_z`     int(9)     NOT NULL DEFAULT '0',
  `delay`      int(9)     NOT NULL DEFAULT '0',
  `running`    tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`route_id`, `npc_id`, `move_point`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- weapon: table
CREATE TABLE `weapon` (
  `item_id`             decimal(11, 0) NOT NULL DEFAULT '0',
  `name`                varchar(70)             DEFAULT NULL,
  `bodypart`            varchar(15)             DEFAULT NULL,
  `crystallizable`      varchar(5)              DEFAULT NULL,
  `weight`              decimal(4, 0)           DEFAULT NULL,
  `soulshots`           decimal(2, 0)           DEFAULT NULL,
  `spiritshots`         decimal(1, 0)           DEFAULT NULL,
  `material`            varchar(11)             DEFAULT NULL,
  `crystal_type`        varchar(4)              DEFAULT NULL,
  `p_dam`               decimal(5, 0)           DEFAULT NULL,
  `rnd_dam`             decimal(2, 0)           DEFAULT NULL,
  `weaponType`          varchar(8)              DEFAULT NULL,
  `critical`            decimal(2, 0)           DEFAULT NULL,
  `hit_modify`          decimal(6, 5)           DEFAULT NULL,
  `avoid_modify`        decimal(2, 0)           DEFAULT NULL,
  `shield_def`          decimal(3, 0)           DEFAULT NULL,
  `shield_def_rate`     decimal(2, 0)           DEFAULT NULL,
  `atk_speed`           decimal(3, 0)           DEFAULT NULL,
  `mp_consume`          decimal(2, 0)           DEFAULT NULL,
  `m_dam`               decimal(3, 0)           DEFAULT NULL,
  `duration`            decimal(3, 0)           DEFAULT NULL,
  `price`               decimal(11, 0)          DEFAULT NULL,
  `crystal_count`       int(4)                  DEFAULT NULL,
  `sellable`            varchar(5)              DEFAULT NULL,
  `dropable`            varchar(5)              DEFAULT NULL,
  `destroyable`         varchar(5)              DEFAULT NULL,
  `tradeable`           varchar(5)              DEFAULT NULL,
  `item_skill_id`       decimal(11, 0) NOT NULL DEFAULT '0',
  `item_skill_lvl`      decimal(11, 0) NOT NULL DEFAULT '0',
  `enchant4_skill_id`   decimal(11, 0) NOT NULL DEFAULT '0',
  `enchant4_skill_lvl`  decimal(11, 0) NOT NULL DEFAULT '0',
  `onCast_skill_id`     decimal(11, 0) NOT NULL DEFAULT '0',
  `onCast_skill_lvl`    decimal(11, 0) NOT NULL DEFAULT '0',
  `onCast_skill_chance` decimal(11, 0) NOT NULL DEFAULT '0',
  `onCrit_skill_id`     decimal(11, 0) NOT NULL DEFAULT '0',
  `onCrit_skill_lvl`    decimal(11, 0) NOT NULL DEFAULT '0',
  `onCrit_skill_chance` decimal(11, 0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`item_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- zone_vertices: table
CREATE TABLE `zone_vertices` (
  `id`    int(11) NOT NULL,
  `order` int(11) NOT NULL,
  `x`     int(11) NOT NULL,
  `y`     int(11) NOT NULL,
  PRIMARY KEY (`id`, `order`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

