DROP TABLE IF EXISTS `pledge_skill_trees`;
CREATE TABLE IF NOT EXISTS `pledge_skill_trees` (
  `skill_id` int(11) default NULL,
  `level` int(11) default NULL,
  `name` varchar(25) default NULL,
  `clan_lvl` int(11) default NULL,
  `Description` varchar(255) default NULL,
  `repCost` int(11) default NULL,
  `itemId` int(11) default NULL
);

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `pledge_skill_trees` VALUES ('370', '1', 'Clan Body', '5', 'Clan members Max HP is raised', '500', '8166');
INSERT INTO `pledge_skill_trees` VALUES ('370', '2', 'Clan Body', '5', 'Clan members Max HP is raised', '500', '8166');
INSERT INTO `pledge_skill_trees` VALUES ('370', '3', 'Clan Body', '5', 'Clan members Max HP is raised', '500', '8166');
INSERT INTO `pledge_skill_trees` VALUES ('371', '1', 'Clan Spirit', '6', 'Clan members Max cp is raised', '800', '8169');
INSERT INTO `pledge_skill_trees` VALUES ('371', '2', 'Clan Spirit', '6', 'Clan members Max cp is raised', '800', '8169');
INSERT INTO `pledge_skill_trees` VALUES ('371', '3', 'Clan Spirit', '6', 'Clan members Max cp is raised', '800', '8169');
INSERT INTO `pledge_skill_trees` VALUES ('372', '1', 'Clan Soul', '8', 'Clan members Max MP is raised', '3900', '8172');
INSERT INTO `pledge_skill_trees` VALUES ('372', '2', 'Clan Soul', '8', 'Clan members Max MP is raised', '3900', '8172');
INSERT INTO `pledge_skill_trees` VALUES ('372', '3', 'Clan Soul', '8', 'Clan members Max MP is raised', '3900', '8172');
INSERT INTO `pledge_skill_trees` VALUES ('373', '1', 'Clan Health', '5', 'Clan members HP Regen speed is enhanced', '500', '8166');
INSERT INTO `pledge_skill_trees` VALUES ('373', '2', 'Clan Health', '5', 'Clan members HP Regen speed is enhanced', '500', '8166');
INSERT INTO `pledge_skill_trees` VALUES ('373', '3', 'Clan Health', '5', 'Clan members HP Regen speed is enhanced', '500', '8166');
INSERT INTO `pledge_skill_trees` VALUES ('374', '1', 'Clan Moral', '6', 'Clan members CP Regen speed is enhanced', '900', '8169');
INSERT INTO `pledge_skill_trees` VALUES ('374', '2', 'Clan Moral', '6', 'Clan members CP Regen speed is enhanced', '900', '8169');
INSERT INTO `pledge_skill_trees` VALUES ('374', '3', 'Clan Moral', '6', 'Clan members CP Regen speed is enhanced', '900', '8169');
INSERT INTO `pledge_skill_trees` VALUES ('375', '1', 'Clan Clarity', '8', 'Clan members MP Regen speed is enhanced', '3900', '8172');
INSERT INTO `pledge_skill_trees` VALUES ('375', '2', 'Clan Clarity', '8', 'Clan members MP Regen speed is enhanced', '3900', '8172');
INSERT INTO `pledge_skill_trees` VALUES ('375', '3', 'Clan Clarity', '8', 'Clan members MP Regen speed is enhanced', '3900', '8172');
INSERT INTO `pledge_skill_trees` VALUES ('376', '1', 'Clan Might', '6', 'Clan members P Atk is raised', '1000', '8163');
INSERT INTO `pledge_skill_trees` VALUES ('376', '2', 'Clan Might', '6', 'Clan members P Atk is raised', '1000', '8163');
INSERT INTO `pledge_skill_trees` VALUES ('376', '3', 'Clan Might', '6', 'Clan members P Atk is raised', '1000', '8163');
INSERT INTO `pledge_skill_trees` VALUES ('377', '1', 'Clan Shield', '6', 'Clan members Defence is enhanced', '1000', '8166');
INSERT INTO `pledge_skill_trees` VALUES ('377', '2', 'Clan Shield', '6', 'Clan members Defence is enhanced', '1000', '8166');
INSERT INTO `pledge_skill_trees` VALUES ('377', '3', 'Clan Shield', '6', 'Clan members Defence is enhanced', '1000', '8166');
INSERT INTO `pledge_skill_trees` VALUES ('378', '1', 'Clan Empower', '8', 'Clan members M Atk is raised', '3900', '8172');
INSERT INTO `pledge_skill_trees` VALUES ('378', '2', 'Clan Empower', '8', 'Clan members M Atk is raised', '3900', '8172');
INSERT INTO `pledge_skill_trees` VALUES ('378', '3', 'Clan Empower', '8', 'Clan members M Atk is raised', '3900', '8172');
INSERT INTO `pledge_skill_trees` VALUES ('379', '1', 'Clan Magic Barrier', '5', 'Clan members Magic Resistance is enhanced', '500', '8163');
INSERT INTO `pledge_skill_trees` VALUES ('379', '2', 'Clan Magic Barrier', '5', 'Clan members Magic Resistance is enhanced', '500', '8163');
INSERT INTO `pledge_skill_trees` VALUES ('379', '3', 'Clan Magic Barrier', '5', 'Clan members Magic Resistance is enhanced', '500', '8163');
INSERT INTO `pledge_skill_trees` VALUES ('380', '1', 'Clan Guidance', '7', 'Clan members Accuracy is raised', '1900', '8160');
INSERT INTO `pledge_skill_trees` VALUES ('380', '2', 'Clan Guidance', '7', 'Clan members Accuracy is raised', '1900', '8160');
INSERT INTO `pledge_skill_trees` VALUES ('380', '3', 'Clan Guidance', '7', 'Clan members Accuracy is raised', '1900', '8160');
INSERT INTO `pledge_skill_trees` VALUES ('381', '1', 'Clan Agility', '8', 'Clan members Flee is raised', '4000', '8160');
INSERT INTO `pledge_skill_trees` VALUES ('381', '2', 'Clan Agility', '8', 'Clan members Flee is raised', '4000', '8160');
INSERT INTO `pledge_skill_trees` VALUES ('381', '3', 'Clan Agility', '8', 'Clan members Flee is raised', '4000', '8160');
INSERT INTO `pledge_skill_trees` VALUES ('382', '1', 'Clan Shield Block', '7', 'Clan members Shield Blocking chance enhanced', '800', '8160');
INSERT INTO `pledge_skill_trees` VALUES ('382', '2', 'Clan Shield Block', '7', 'Clan members Shield Blocking chance enhanced', '800', '8160');
INSERT INTO `pledge_skill_trees` VALUES ('382', '3', 'Clan Shield Block', '7', 'Clan members Shield Blocking chance enhanced', '800', '8160');
INSERT INTO `pledge_skill_trees` VALUES ('383', '1', 'Clan Shield Defence', '6', 'Clan members Shield Defence is raised', '800', '8163');
INSERT INTO `pledge_skill_trees` VALUES ('383', '2', 'Clan Shield Defence', '6', 'Clan members Shield Defence is raised', '800', '8163');
INSERT INTO `pledge_skill_trees` VALUES ('383', '3', 'Clan Shield Defence', '6', 'Clan members Shield Defence is raised', '800', '8163');
INSERT INTO `pledge_skill_trees` VALUES ('384', '1', 'Clan Resist Typoon', '7', 'Clan members Resistance against Water / Wind is enhanced', '1800', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('384', '2', 'Clan Resist Typoon', '7', 'Clan members Resistance against Water / Wind is enhanced', '1800', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('384', '3', 'Clan Resist Typoon', '7', 'Clan members Resistance against Water / Wind is enhanced', '1800', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('385', '1', 'Clan Resist Lava', '7', 'Clan members Resistance against Fire / Earth is raised', '1800', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('385', '2', 'Clan Resist Lava', '7', 'Clan members Resistance against Fire / Earth is raised', '1800', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('385', '3', 'Clan Resist Lava', '7', 'Clan members Resistance against Fire / Earth is raised', '1800', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('386', '1', 'Clan Fortitude', '7', 'Clan members Resistance against Shock attack is enhanced', '1000', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('386', '2', 'Clan Fortitude', '7', 'Clan members Resistance against Shock attack is enhanced', '1000', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('386', '3', 'Clan Fortitude', '7', 'Clan members Resistance against Shock attack is enhanced', '1000', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('387', '1', 'Clan Freedom', '7', 'Clan members Resistance against Root magic is raised', '1800', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('387', '2', 'Clan Freedom', '7', 'Clan members Resistance against Root magic is raised', '1800', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('387', '3', 'Clan Freedom', '7', 'Clan members Resistance against Root magic is raised', '1800', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('388', '1', 'Clan Vigilance', '7', 'Clan members Resistance against Sleep magic is raised', '1800', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('388', '2', 'Clan Vigilance', '7', 'Clan members Resistance against Sleep magic is raised', '1800', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('388', '3', 'Clan Vigilance', '7', 'Clan members Resistance against Sleep magic is raised', '1800', '8176');
INSERT INTO `pledge_skill_trees` VALUES ('389', '1', 'Clan Movement', '8', 'Clan members moving speed is raised', '3800', '8175');
INSERT INTO `pledge_skill_trees` VALUES ('389', '2', 'Clan Movement', '8', 'Clan members moving speed is raised', '3800', '8175');
INSERT INTO `pledge_skill_trees` VALUES ('389', '3', 'Clan Movement', '8', 'Clan members moving speed is raised', '3800', '8175');
INSERT INTO `pledge_skill_trees` VALUES ('390', '1', 'Clan Death', '7', 'Clan members exp loss and item drop rate from PK / Monsters is REDUCED', '2200', '8175');
INSERT INTO `pledge_skill_trees` VALUES ('390', '2', 'Clan Death', '7', 'Clan members exp loss and item drop rate from PK / Monsters is REDUCED', '2200', '8175');
INSERT INTO `pledge_skill_trees` VALUES ('390', '3', 'Clan Death', '7', 'Clan members exp loss and item drop rate from PK / Monsters is REDUCED', '2200', '8175');
INSERT INTO `pledge_skill_trees` VALUES ('391', '1', 'Clan Emperium', '5', 'Permission to create Clan leader chat channel.', '0', '8176');
