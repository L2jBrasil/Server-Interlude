ALTER TABLE `npc` ADD `absorb_level` decimal(2,0) default 0;

UPDATE `npc` SET `absorb_level` = 2 WHERE `id` IN (583, 584, 585, 586, 769, 770, 793, 794, 849);
UPDATE `npc` SET `absorb_level` = 3 WHERE `id` IN (587, 588, 638, 767, 768, 798, 799, 800, 838, 839, 848);
UPDATE `npc` SET `absorb_level` = 4 WHERE `id` IN (636, 637, 639, 801, 802, 840, 841, 842, 995);
UPDATE `npc` SET `absorb_level` = 5 WHERE `id` IN (640, 641, 642, 803, 843, 844, 846, 847, 986, 987, 988, 994, 12544);
UPDATE `npc` SET `absorb_level` = 7 WHERE `id` IN (646, 647, 648, 649, 650);
UPDATE `npc` SET `absorb_level` = 9 WHERE `id` IN (1006);
UPDATE `npc` SET `absorb_level` = 10 WHERE `id` IN (625, 626, 627, 628, 629, 674, 761, 762, 821, 823, 826, 827, 828, 829, 830, 831, 1007, 1008, 1063, 1067, 1070);
UPDATE `npc` SET `absorb_level` = 13 WHERE `id` IN (10283, 10286, 12211, 12372, 12374, 12899);