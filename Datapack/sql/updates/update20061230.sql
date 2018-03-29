-- *** DANGER *** - This update must DROP & CREATE the `clanhall` & `auction` tables due to structure changes

ALTER TABLE clan_data ADD `auction_bid_at` INT NOT NULL default '0';
ALTER TABLE auction_bid ADD `time_bid` decimal(20,0) NOT NULL default '0';
ALTER TABLE auction_bid ADD `clan_name` varchar(50) NOT NULL after `bidderName`;

-- removing duplicates spawns with bad IDs
DELETE FROM spawnlist where npc_templateid in (30800,30798,30802,31158,31160,31156,31152,31150,31154,30784,30788,30790,30786,30778,30780,30782,30774,30776,30798);

-- updating auctionners
UPDATE npc SET type = 'L2Auctioneer' where id in (30767,30768,30769,30770,30771);

-- updating doormens
update npc set type = 'L2Doormen' where id in
(30492,30493,30772,30773,30775,30777,30778,30779,30781,30783,30785,30787,30789,30791,30799,30801,30803,31151,31153,31155,31157,31159,31161,31352,31353,31354,31355,31447,31448,31449,31450,31451,35138,35139,35180,35181,35222,35223,35267,35268,35269,35270,35337,35433,35434,35312,35313,35096,35097,35581,35583,35585,35587);

-- updating clan hall managers
update npc set type='L2ClanHallManager', title='Clan Hall Manager' where id in
(35385,35438,35453,35455,35451,35457,35459,35398,35400,35392,35394,35396,35384,35390,35386,35388,35855,35856,35857,35858,35859,35860,35407,35403,35405,35864,35865,35866,35867,35868,35869,35870,35871,35872,35873,35874,35875,35876,35877,35878,35879,35880,35881,35882,35883,35884,35885,35886,35439,35441,35443,35445,35447,35449,35467,35465,35463,35461);

-- setting zones
DELETE FROM zone WHERE type = 'Clan Hall';
ALTER TABLE zone ADD `z2` int(11) NOT NULL default '0' AFTER `z`;
INSERT INTO  zone VALUES
 (22, 'Clan Hall', 'Gludio 1', -16400, 123275, -15551, 123850, -3117,0, 1),
 (23, 'Clan Hall', 'Gludio 2', -15100, 125350, -14800, 125800, -3143,0, 1),
 (24, 'Clan Hall', 'Gludio 3', -14050, 125050, -13700, 125700, -3143,0, 1),
 (25, 'Clan Hall', 'Gludio 4', -12950, 123900, -12300, 124250, -3117,0, 1),
 (26, 'Clan Hall', 'Gludin 1', -84700, 151550, -84250, 152350, -3130,0, 1),
 (26, 'Clan Hall', 'Gludin 1', -84350, 151950, -83800, 152350, -3130,0, 1),
 (27, 'Clan Hall', 'Gludin 2', -84400, 153050, -83950, 154050, -3166,0, 1),
 (27, 'Clan Hall', 'Gludin 2', -84200, 153050, -83550, 153600, -3166,0, 1),
 (28, 'Clan Hall', 'Gludin 3', -84500, 154900, -83950, 155700, -3158,0, 1),
 (28, 'Clan Hall', 'Gludin 3', -84100, 155300, -83500, 155700, -3158,0, 1),
 (29, 'Clan Hall', 'Gludin 4', -79700, 149400, -79250, 150300, -3061,0, 1),
 (29, 'Clan Hall', 'Gludin 4', -80100, 149400, -79500, 149850, -3061,0, 1),
 (30, 'Clan Hall', 'Gludin 5', -79700, 151350, -79300, 152250, -3036,0, 1),
 (30, 'Clan Hall', 'Gludin 5', -80100, 151800, -79500, 152250, -3036,0, 1),
 (31, 'Clan Hall', 'Dion 1', 17400, 144800, 18000, 145350, -3043,0, 1),
 (32, 'Clan Hall', 'Dion 2', 18850, 143600, 18600, 143100, -3017,0, 1),
 (33, 'Clan Hall', 'Dion 3', 19950, 146000, 20400, 146300, -3118,0, 1),
 (42, 'Clan Hall', 'Giran 1', 80780, 151063, 81156, 152111, -3518,0, 1),
 (43, 'Clan Hall', 'Giran 2', 82288, 152437, 81912, 151393, -3543,0, 1),
 (44, 'Clan Hall', 'Giran 3', 78077, 148285, 79119, 147911, -3608,0, 1),
 (45, 'Clan Hall', 'Giran 4', 83205, 144788, 83577, 145837, -3396,0, 1),
 (46, 'Clan Hall', 'Giran 5', 82244, 145860, 81870, 144814, -3517,0, 1),
 (36, 'Clan Hall', 'Aden 1', 143712, 27490, 144222, 26713, -2255,0, 1),
 (37, 'Clan Hall', 'Aden 2', 143720, 28607, 144262, 27789, -2247,0, 1),
 (38, 'Clan Hall', 'Aden 3', 151025, 26140, 150512, 26916, -2249,0, 1),
 (39, 'Clan Hall', 'Aden 4', 150396, 24062, 150940, 23243, -2120,0, 1),
 (40, 'Clan Hall', 'Aden 5', 149362, 22756, 148855, 23536, -2132,0, 1),
 (41, 'Clan Hall', 'Aden 6', 145999, 24932, 145455, 25753, -2121,0, 1),
 (47, 'Clan Hall', 'Goddard 1', 149717, -55824, 149063, -55350, -2783,0, 1),
 (48, 'Clan Hall', 'Goddard 2', 148479, -56473, 148479, -57275, -2773,0, 1),
 (49, 'Clan Hall', 'Goddard 3', 147238, -56636, 146564, -57078, -2783,0, 1),
 (50, 'Clan Hall', 'Goddard 4', 146399, -55682, 145652, -55386, -2773,0, 1),
 (35, 'Clan Hall', 'Bandits Stronghold', 80738, -15914, 79627, -15054, -1810,0, 1),
 (21, 'Clan Hall', 'Partisan Hideaway', 43151, 108377, 43648, 109399, -1981,0, 1),
 (62, 'Clan Hall', 'Hot Springs Guild House', 141414, -124508, 140590, -124706, -1896,0, 1);

-- -------------------------------
-- C5 Clan Halls (these are not correct, but just to avoid NPEs)
-- -------------------------------

INSERT INTO zone (id, type, name, x1, y1, x2, y2, z, taxById) VALUES
  (62, "Clan Hall", "Hot Springs Guild House", 141414, -124508, 140590, -124706, -1896, 1),
  (34, "Clan Hall", "Devastated Castle", 0, 0, 0, 0, 0, 0),
  (51, "Clan Hall", "Mont Chamber", 37437, -45872, 38024, -45460, 900, 8),
  (52, "Clan Hall", "Astaire Chamber", 38433, -46322, 39062, -45731, 900, 8),
  (53, "Clan Hall", "Aria Chamber", 39437, -47141, 39760, -46668, 900, 8),
  (54, "Clan Hall", "Yiana Chamber", 39426, -48619, 39820, -47871, 899, 8),
  (55, "Clan Hall", "Roien Chamber", 39173, -50020, 39774, -49340, 900, 8),
  (56, "Clan Hall", "Luna Chamber", 38401, -50516, 39054, -50404, 900, 8),
  (57, "Clan Hall", "Traban Chamber", 37461, -50973, 38006, -50589, 900, 8),
  (58, "Clan Hall", "Eisen Hall", 85426, -143448, 86069, -142769, -1342, 8),
  (59, "Clan Hall", "Heavy Metal Hall", 86162, -142094, 87003, -141727, -1340, 8),
  (60, "Clan Hall", "Molten Ore Hall", 88600, -142111, 87724, -141750, -1341, 8),
  (61, "Clan Hall", "Titan Hall", 88500, -143500, 89500, -142880, -1340, 8),
  (63, "Clan Hall", "Beast Farm", 0, 0, 0, 0, 0, 0),
  (64, "Clan Hall", "Fortress of the Dead", 0, 0, 0, 0, 0, 0);

-- -----------------------------------------
-- C5 town and castle spawns
-- -----------------------------------------
INSERT INTO zone (id, type, name, x1, y1, x2, y2, z, taxById) VALUES
  (17, "Town", "Schuttgart", 83881, -146500, 90908, -139486, 0, 9),
  (17, "Town Spawn", "Schuttgart", 87331, -142842, 0, 0, -1317, 0),
  (9, "Castle Area", "Schuttgart", 73000, -156600, 80740, -147592, 0, 8),
  (9, "Castle HQ", "Schuttgart", 77200, -153000, 77900, -478700, -545, 8),
  (9, "Castle Defender Spawn", "Schuttgart", 77524, -152709, 0, 0, -545, 0),
  (8, "Castle Defender Spawn", "Rune", 11388, -49160, 0, 0, -537, 0),
  (8, "Castle HQ", "Rune", 7000, -52500, 18493, -45900, -547, 0),
  (8, "Castle Area", "Rune", 7000, -55500, 27000, -41716, 0, 0),
  (8, "Siege Battlefield", "Rune", 7000, -55500, 27000, -41716, 0, 0),
  (9, "Siege Battlefield", "Schuttgart", 73000, -156600, 80740, -147592, 0, 0);

-- adding teleporting locations
INSERT INTO teleport VALUES
('Clan Hall -> Execution Grounds',502,51055,141959,-2869,500,0),
-- ('Clan Hall -> Fortress of Resistance',503,51055,141959,-2869,500,0), -- dunno coords !
('Clan Hall -> Cruma Marshlands',504,5106,126916,-3664,500,0),
('Clan Hall -> Cruma Tower Entrance',505,17192,114178,-3439,500,0),
 -- ('Clan Hall -> Mandragora Farm',506,17192,114178,-3439,500,0), -- dunno coords !
('Clan Hall -> Town of Dion',507,15670,142983,-2705,500,0),
('Clan Hall -> Floran Village',508,17838,170274,-3508,500,0),
 -- 509
('Clan Hall -> Tanor Canyon',510,51147,165543,-2829,500,0),
('Clan Hall -> Bee Hive',511,20505,189036,-3344,500,0),
 -- ('Clan Hall -> Dion Hills',512,20505,189036,-3344,500,0), -- dunno coords !
 -- ('Clan Hall -> Floran Agricultural Area',513,20505,189036,-3344,500,0), -- dunno coords !
 -- ('Clan Hall -> Plains of Dion',514,20505,189036,-3344,500,0), -- dunno coords !
 -- 515
 -- 516
('Clan Hall -> Hardin\'s Academy',517,105918,109759,-3207,500,0),
('Clan Hall -> Dragon Valley',518,122824,110836,-3720,500,0),
 -- 519
 -- 520
('Clan Hall -> Death Pass',521,70000,126636,-3804,500,0),
('Clan Hall -> Pirate Tunnel',522,41298,200350,-4583,500,0),
 -- 523
('Clan Hall -> Giran Harbor',524,47942,186764,-3485,500,0),
('Clan Hall -> Giran Castle Town',525,83400,147943,-3404,500,0),
('Clan Hall -> Giran Arena',526,73890,142656,-3778,500,0),
 -- 527
('Clan Hall -> Breka\'s Stronghold',528,79798,130624,-3677,500,0),
 -- ('Clan Hall -> Gorgon Flower Garden',529,79798,130624,-3677,500,0),  -- dunno coords !
 -- -----------------
('Clan Hall -> Ivory Tower',581,85348,16142,-3699,500,0),
('Clan Hall -> Town of Oren',582,85348,16142,-3699,500,0),
 -- 583
('Clan Hall -> Plains of Lizardmen',584,87252,85514,-3056,500,0),
('Clan Hall -> Skyshadow Meadow',585,82764,61145,-3502,500,0),
 -- ('Clan Hall -> Shilen\'s Garden',586,82764,61145,-3502,500,0),  -- dunno coords !
 -- ('Clan Hall -> Black Rock Hill',587,82764,61145,-3502,500,0),  -- dunno coords !
('Clan Hall -> Spider Nest',588,-56532,78321,-2960,500,0),
 -- ('Clan Hall -> Timak Outpost',589,-56532,78321,-2960,500,0), -- dunno coords !
 -- ('Clan Hall -> Ivory Tower Crater',590,-56532,78321,-2960,500,0), -- dunno coords !
 -- ('Clan Hall -> Forest of Evil',591,-56532,78321,-2960,500,0), -- dunno coords !
('Clan Hall -> Outlaw Forest',592,85995,-2433,-3528,500,0),
 -- ('Clan Hall -> Misty Mountains',593,85995,-2433,-3528,500,0), -- dunno coords !
 -- ('Clan Hall -> Starlight Waterfall',594,85995,-2433,-3528,500,0), -- dunno coords !
 -- ('Clan Hall -> Undine Waterfall',595,85995,-2433,-3528,500,0), -- dunno coords !
 -- ('Clan Hall -> The Gods\' Falls',596,85995,-2433,-3528,500,0),  -- dunno coords !
 -- 597
('Clan Hall -> Tower of Insolence',598,121685,15749,-3852,500,0),
('Clan Hall -> The Blazing Swamp',599,146828,-12859,-4455,500,0),
 -- 600
('Clan Hall -> The Forbidden Gateway',601,185395,20359,-3270,500,0),
('Clan Hall -> The Giants Cave',602,174528,52683,-4369,500,0),
('Clan Hall -> Northern Pathway of Enchanted Valley',603,104426,33746,-3800,500,0), -- need also southern?
('Clan Hall -> The Cemetery',604,172136,20325,-3326,500,0),
('Clan Hall -> The Forest of Mirrors',605,150477,85907,-2753,500,0),
('Clan Hall -> Anghel Waterfall',606,165584,85997,-2338,500,0),
('Clan Hall -> Aden Castle Town',607,146331,25762,-2018,500,0),
('Clan Hall -> Hunters Village',608,117110,76883,-2695,500,0),
('Clan Hall -> Border Outpost(Aden Side)',609,109699,-7908,-2902,500,0),
('Clan Hall -> Coliseum',610,150086,46733,-3412,500,0),
-- ('Clan Hall -> Narsell Lake',611,150086,46733,-3412,500,0), -- dunno coords !
 -- 612 
('Clan Hall -> Ancient Battleground',613,127739,-6998,-3869,500,0),
('Clan Hall -> Forsaken Plains',614,167285,37109,-4008,500,0),
('Clan Hall -> Silent Valley',615,177318,48447,-3835,500,0),
-- ('Clan Hall -> Hunters Valley',616,177318,48447,-3835,500,0), -- dunno coords !
-- ('Clan Hall -> Plains of Glory',617,177318,48447,-3835,500,0), -- dunno coords !
('Clan Hall -> Fields of Massacre',618,179718,48447,-7843,500,0),
-- ('Clan Hall -> War-Torn Plains',619,179718,48447,-7843,500,0), --- dunno coords !
('Clan Hall -> Border Outpost(Unknown Side)',620,114172,-18034,-1875,500,0);

-- creating new tables and replacing old ones
DROP TABLE IF EXISTS `clanhall_functions`;
CREATE TABLE `clanhall_functions` (
  `hall_id` int(2) NOT NULL default '0',
  `type` int(1) NOT NULL default '0',
  `lvl` int(3) NOT NULL default '0',
  `lease` int(10) NOT NULL default '0',
  `rate` decimal(20,0) NOT NULL default '0',
  `endTime` decimal(20,0) NOT NULL default '0',
  `inDebt` int(1) NOT NULL default '0',
  PRIMARY KEY  (`hall_id`,`type`)
);

DROP TABLE IF EXISTS `clanhall`;
CREATE TABLE `clanhall` (
  `id` int(11) NOT NULL default '0',
  `name` varchar(40) NOT NULL default '',
  `ownerId` int(11) NOT NULL default '0',
  `lease` int(10) NOT NULL default '0',
  `desc` text NOT NULL,
  `location` varchar(15) NOT NULL default '',
  `paidUntil` decimal(20,0) NOT NULL default '0',
  `Grade` decimal(1,0) NOT NULL default '0',
  PRIMARY KEY  (`id`,`name`),
  KEY `id` (`id`)
);

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `clanhall` VALUES ('21', 'Fortress of Resistance', '0', '500000', 'Ol Mahum Fortress of Resistance', 'Dion', '0', '0');
INSERT INTO `clanhall` VALUES ('22', 'Moonstone Hall', '0', '500000', 'Clan hall located in the Town of Gludio', 'Gludio', '0', '2');
INSERT INTO `clanhall` VALUES ('23', 'Onyx Hall', '0', '500000', 'Clan hall located in the Town of Gludio', 'Gludio', '0', '2');
INSERT INTO `clanhall` VALUES ('24', 'Topaz Hall', '0', '500000', 'Clan hall located in the Town of Gludio', 'Gludio', '0', '2');
INSERT INTO `clanhall` VALUES ('25', 'Ruby Hall', '0', '500000', 'Clan hall located in the Town of Gludio', 'Gludio', '0', '2');
INSERT INTO `clanhall` VALUES ('26', 'Crystal Hall', '0', '500000', 'Clan hall located in Gludin Village', 'Gludin', '0', '2');
INSERT INTO `clanhall` VALUES ('27', 'Onyx Hall', '0', '500000', 'Clan hall located in Gludin Village', 'Gludin', '0', '2');
INSERT INTO `clanhall` VALUES ('28', 'Sapphire Hall', '0', '500000', 'Clan hall located in Gludin Village', 'Gludin', '0', '2');
INSERT INTO `clanhall` VALUES ('29', 'Moonstone Hall', '0', '500000', 'Clan hall located in Gludin Village', 'Gludin', '0', '2');
INSERT INTO `clanhall` VALUES ('30', 'Emerald Hall', '0', '500000', 'Clan hall located in Gludin Village', 'Gludin', '0', '2');
INSERT INTO `clanhall` VALUES ('31', 'The Atramental Barracks', '0', '500000', 'Clan hall located in the Town of Dion', 'Dion', '0', '1');
INSERT INTO `clanhall` VALUES ('32', 'The Scarlet Barracks', '0', '500000', 'Clan hall located in the Town of Dion', 'Dion', '0', '1');
INSERT INTO `clanhall` VALUES ('33', 'The Viridian Barracks', '0', '500000', 'Clan hall located in the Town of Dion', 'Dion', '0', '1');
INSERT INTO `clanhall` VALUES ('34', 'Devastated Castle', '0', '500000', 'Contestable Clan Hall', 'Aden', '0', '0');
INSERT INTO `clanhall` VALUES ('35', 'Bandit Stronghold', '0', '500000', 'Contestable Clan Hall', 'Oren', '0', '0');
INSERT INTO `clanhall` VALUES ('36', 'The Golden Chamber', '0', '500000', 'Clan hall located in the Town of Aden', 'Aden', '0', '3');
INSERT INTO `clanhall` VALUES ('37', 'The Silver Chamber', '0', '500000', 'Clan hall located in the Town of Aden', 'Aden', '0', '3');
INSERT INTO `clanhall` VALUES ('38', 'The Mithril Chamber', '0', '500000', 'Clan hall located in the Town of Aden', 'Aden', '0', '3');
INSERT INTO `clanhall` VALUES ('39', 'Silver Manor', '0', '500000', 'Clan hall located in the Town of Aden', 'Aden', '0', '3');
INSERT INTO `clanhall` VALUES ('40', 'Gold Manor', '0', '500000', 'Clan hall located in the Town of Aden', 'Aden', '0', '3');
INSERT INTO `clanhall` VALUES ('41', 'The Bronze Chamber', '0', '500000', 'Clan hall located in the Town of Aden', 'Aden', '0', '3');
INSERT INTO `clanhall` VALUES ('42', 'The Golden Chamber', '0', '500000', 'Clan hall located in the Town of Giran', 'Giran', '0', '3');
INSERT INTO `clanhall` VALUES ('43', 'The Silver Chamber', '0', '500000', 'Clan hall located in the Town of Giran', 'Giran', '0', '3');
INSERT INTO `clanhall` VALUES ('44', 'The Mithril Chamber', '0', '500000', 'Clan hall located in the Town of Giran', 'Giran', '0', '3');
INSERT INTO `clanhall` VALUES ('45', 'The Bronze Chamber', '0', '500000', 'Clan hall located in the Town of Giran', 'Giran', '0', '3');
INSERT INTO `clanhall` VALUES ('46', 'Silver Manor', '0', '500000', 'Clan hall located in the Town of Giran', 'Giran', '0', '3');
INSERT INTO `clanhall` VALUES ('47', 'Moonstone Hall', '0', '500000', 'Clan hall located in the Town of Goddard', 'Goddard', '0', '3');
INSERT INTO `clanhall` VALUES ('48', 'Onyx Hall', '0', '500000', 'Clan hall located in the Town of Goddard', 'Goddard', '0', '3');
INSERT INTO `clanhall` VALUES ('49', 'Emerald Hall', '0', '500000', 'Clan hall located in the Town of Goddard', 'Goddard', '0', '3');
INSERT INTO `clanhall` VALUES ('50', 'Sapphire Hall', '0', '500000', 'Clan hall located in the Town of Goddard', 'Goddard', '0', '3');
INSERT INTO `clanhall` VALUES ('51', 'Mont Chamber', '0', '500000', 'An upscale Clan hall located in the Rune Township', 'Rune', '0', '3');
INSERT INTO `clanhall` VALUES ('52', 'Astaire Chamber', '0', '500000', 'An upscale Clan hall located in the Rune Township', 'Rune', '0', '3');
INSERT INTO `clanhall` VALUES ('53', 'Aria Chamber', '0', '500000', 'An upscale Clan hall located in the Rune Township', 'Rune', '0', '3');
INSERT INTO `clanhall` VALUES ('54', 'Yiana Chamber', '0', '500000', 'An upscale Clan hall located in the Rune Township', 'Rune', '0', '3');
INSERT INTO `clanhall` VALUES ('55', 'Roien Chamber', '0', '500000', 'An upscale Clan hall located in the Rune Township', 'Rune', '0', '3');
INSERT INTO `clanhall` VALUES ('56', 'Luna Chamber', '0', '500000', 'An upscale Clan hall located in the Rune Township', 'Rune', '0', '3');
INSERT INTO `clanhall` VALUES ('57', 'Traban Chamber', '0', '500000', 'An upscale Clan hall located in the Rune Township', 'Rune', '0', '3');
INSERT INTO `clanhall` VALUES ('58', 'Eisen Hall', '0', '500000', 'Clan hall located in the Town of Schuttgart', 'Schuttgart', '0', '2');
INSERT INTO `clanhall` VALUES ('59', 'Heavy Metal Hall', '0', '500000', 'Clan hall located in the Town of Schuttgart', 'Schuttgart', '0', '2');
INSERT INTO `clanhall` VALUES ('60', 'Molten Ore Hall', '0', '500000', 'Clan hall located in the Town of Schuttgart', 'Schuttgart', '0', '2');
INSERT INTO `clanhall` VALUES ('61', 'Titan Hall', '0', '500000', 'Clan hall located in the Town of Schuttgart', 'Schuttgart', '0', '2');
INSERT INTO `clanhall` VALUES ('62', 'Rainbow Springs', '0', '500000', '', 'Goddard', '0', '0');
INSERT INTO `clanhall` VALUES ('63', 'Beast Farm', '0', '500000', '', 'Rune', '0', '0');
INSERT INTO `clanhall` VALUES ('64', 'Fortress of the Dead', '0', '500000', '', 'Rune', '0', '0');


DROP TABLE IF EXISTS `auction`;
CREATE TABLE `auction` (
  id int(11) NOT NULL default '0',
  sellerId int(11) NOT NULL default '0',
  sellerName varchar(50) NOT NULL default 'NPC',
  sellerClanName varchar(50) NOT NULL default '',
  itemType varchar(25) NOT NULL default '',
  itemId int(11) NOT NULL default '0',
  itemObjectId int(11) NOT NULL default '0',
  itemName varchar(40) NOT NULL default '',
  itemQuantity int(11) NOT NULL default '0',
  startingBid int(11) NOT NULL default '0',
  currentBid int(11) NOT NULL default '0',
  endDate decimal(20,0) NOT NULL default '0',
  PRIMARY KEY  (`itemType`,`itemId`,`itemObjectId`),
  KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `auction`
-- 

INSERT INTO `auction` VALUES 
(22, 0, 'NPC', 'NPC Clan', 'ClanHall', 22, 0, 'Moonstone Hall', 1, 20000000, 0, 1164841200000),
(23, 0, 'NPC', 'NPC Clan', 'ClanHall', 23, 0, 'Onyx Hall', 1, 20000000, 0, 1164841200000),
(24, 0, 'NPC', 'NPC Clan', 'ClanHall', 24, 0, 'Topaz Hall', 1, 20000000, 0, 1164841200000),
(25, 0, 'NPC', 'NPC Clan', 'ClanHall', 25, 0, 'Ruby Hall', 1, 20000000, 0, 1164841200000),
(26, 0, 'NPC', 'NPC Clan', 'ClanHall', 26, 0, 'Crystal Hall', 1, 20000000, 0, 1164841200000),
(27, 0, 'NPC', 'NPC Clan', 'ClanHall', 27, 0, 'Onyx Hall', 1, 20000000, 0, 1164841200000),
(28, 0, 'NPC', 'NPC Clan', 'ClanHall', 28, 0, 'Sapphire Hall', 1, 20000000, 0, 1164841200000),
(29, 0, 'NPC', 'NPC Clan', 'ClanHall', 29, 0, 'Moonstone Hall', 1, 20000000, 0, 1164841200000),
(30, 0, 'NPC', 'NPC Clan', 'ClanHall', 30, 0, 'Emerald Hall', 1, 20000000, 0, 1164841200000),
(31, 0, 'NPC', 'NPC Clan', 'ClanHall', 31, 0, 'The Atramental Barracks', 1, 8000000, 0, 1164841200000),
(32, 0, 'NPC', 'NPC Clan', 'ClanHall', 32, 0, 'The Scarlet Barracks', 1, 8000000, 0, 1164841200000),
(33, 0, 'NPC', 'NPC Clan', 'ClanHall', 33, 0, 'The Viridian Barracks', 1, 8000000, 0, 1164841200000),
(36, 0, 'NPC', 'NPC Clan', 'ClanHall', 36, 0, 'The Golden Chamber', 1, 50000000, 0, 1164841200000),
(37, 0, 'NPC', 'NPC Clan', 'ClanHall', 37, 0, 'The Silver Chamber', 1, 50000000, 0, 1164841200000),
(38, 0, 'NPC', 'NPC Clan', 'ClanHall', 38, 0, 'The Mithril Chamber', 1, 50000000, 0, 1164841200000),
(39, 0, 'NPC', 'NPC Clan', 'ClanHall', 39, 0, 'Silver Manor', 1, 50000000, 0, 1164841200000),
(40, 0, 'NPC', 'NPC Clan', 'ClanHall', 40, 0, 'Gold Manor', 1, 50000000, 0, 1164841200000),
(41, 0, 'NPC', 'NPC Clan', 'ClanHall', 41, 0, 'The Bronze Chamber', 1, 50000000, 0, 1164841200000),
(42, 0, 'NPC', 'NPC Clan', 'ClanHall', 42, 0, 'The Golden Chamber', 1, 50000000, 0, 1164841200000),
(43, 0, 'NPC', 'NPC Clan', 'ClanHall', 43, 0, 'The Silver Chamber', 1, 50000000, 0, 1164841200000),
(44, 0, 'NPC', 'NPC Clan', 'ClanHall', 44, 0, 'The Mithril Chamber', 1, 50000000, 0, 1164841200000),
(45, 0, 'NPC', 'NPC Clan', 'ClanHall', 45, 0, 'The Bronze Chamber', 1, 50000000, 0, 1164841200000),
(46, 0, 'NPC', 'NPC Clan', 'ClanHall', 46, 0, 'Silver Manor', 1, 50000000, 0, 1164841200000),
(47, 0, 'NPC', 'NPC Clan', 'ClanHall', 47, 0, 'Moonstone Hall', 1, 50000000, 0, 1164841200000),
(48, 0, 'NPC', 'NPC Clan', 'ClanHall', 48, 0, 'Onyx Hall', 1, 50000000, 0, 1164841200000),
(49, 0, 'NPC', 'NPC Clan', 'ClanHall', 49, 0, 'Emerald Hall', 1, 50000000, 0, 1164841200000),
(50, 0, 'NPC', 'NPC Clan', 'ClanHall', 50, 0, 'Sapphire Hall', 1, 50000000, 0, 1164841200000),
(51, 0, 'NPC', 'NPC Clan', 'ClanHall', 51, 0, 'Mont Chamber', 1, 50000000, 0, 1164841200000),
(52, 0, 'NPC', 'NPC Clan', 'ClanHall', 52, 0, 'Astaire Chamber', 1, 50000000, 0, 1164841200000),
(53, 0, 'NPC', 'NPC Clan', 'ClanHall', 53, 0, 'Aria Chamber', 1, 50000000, 0, 1164841200000),
(54, 0, 'NPC', 'NPC Clan', 'ClanHall', 54, 0, 'Yiana Chamber', 1, 50000000, 0, 1164841200000),
(55, 0, 'NPC', 'NPC Clan', 'ClanHall', 55, 0, 'Roien Chamber', 1, 50000000, 0, 1164841200000),
(56, 0, 'NPC', 'NPC Clan', 'ClanHall', 56, 0, 'Luna Chamber', 1, 50000000, 0, 1164841200000),
(57, 0, 'NPC', 'NPC Clan', 'ClanHall', 57, 0, 'Traban Chamber', 1, 50000000, 0, 1164841200000),
(58, 0, 'NPC', 'NPC Clan', 'ClanHall', 58, 0, 'Eisen Hall', 1, 50000000, 0, 1164841200000),
(59, 0, 'NPC', 'NPC Clan', 'ClanHall', 59, 0, 'Heavy Metal Hall', 1, 50000000, 0, 1164841200000),
(60, 0, 'NPC', 'NPC Clan', 'ClanHall', 60, 0, 'Molten Ore Hall', 1, 50000000, 0, 1164841200000),
(61, 0, 'NPC', 'NPC Clan', 'ClanHall', 61, 0, 'Titan Hall', 1, 50000000, 0, 1164841200000);