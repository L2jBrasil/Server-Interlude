-- 
-- Table structure for table `mapregion`
-- 
DROP TABLE IF EXISTS mapregion;
CREATE TABLE `mapregion` (
`region` int(11) NOT NULL default '0',
`sec0` int(2) NOT NULL default '0',
`sec1` int(2) NOT NULL default '0',
`sec2` int(2) NOT NULL default '0',
`sec3` int(2) NOT NULL default '0',
`sec4` int(2) NOT NULL default '0',
`sec5` int(2) NOT NULL default '0',
`sec6` int(2) NOT NULL default '0',
`sec7` int(2) NOT NULL default '0',
`sec8` int(2) NOT NULL default '0',
`sec9` int(2) NOT NULL default '0',
PRIMARY KEY (`region`)
) ENGINE=MyISAM;

--
-- Dumping data for table `mapregion`
-- 

-- 0 = "Talking Island Village"
-- 1 = "Elven Village"
-- 2 = "Dark Elven Village"
-- 3 = "Orc Village"
-- 4 = "Dwarven Village"
-- 5 = "Town of Gludio"
-- 6 = "Gludin Village"
-- 7 = "Town of Dion"
-- 8 = "Town of Giran"
-- 9 = "Town of Oren"
-- 10 = "Town of Aden"
-- 11 = "Hunters Village"
-- 12 = "Giran Harbor"
-- 13 = "Heine"
-- 14 = "Rune Township"
-- 15 = "Town of Goddard"
-- 16 = "Town of Shuttgart"
-- 17 = "Floran Village"
-- default = "Town of Aden"

INSERT INTO mapregion VALUES 
--	16	17	18	19	20	21	22	23	23	25
(0,	3,	3,	3,	3,	3,	4,	4,	4,	4,	4),	-- _8
(1,	3,	3,	3,	3,	3,	4,	4,	4,	4,	4),	-- _9
(2,	3,	3,	7,	9,	11,	4,	4,	4,	4,	4),	-- _10
(3,	3,	3,	3,	3,	3,	4,	4,	4,	4,	4),	-- _11
(4,	3,	3,	3,	3,	3,	4,	4,	4,	4,	4),	-- _12
(5,	3,	3,	3,	3,	3,	16,	16,	16,	15,	15),	-- _13
(6,	3,	3,	3,	3,	3,	16,	16,	16,	15,	15),	-- _14
(7,	3,	3,	3,	3,	3,	14,	14,	15,	15,	15),	-- _15
(8,	3,	3,	3,	14,	14,	14,	14,	15,	15,	15),	-- _16
(9,	2,	2,	2,	2,	2,	2,	9,	9,	10,	10),	-- _17
(10,	2,	2,	2,	2,	2,	9,	9,	10,	10,	10),	-- _18
(11,	2,	2,	2,	2,	1,	1,	9,	11,	10,	10),	-- _19
(12,	6,	6,	2,	5,	1,	1,	9,	11,	11,	11),	-- _20
(13,	6,	6,	5,	5,	7,	7,	8,	8,	8,	8),	-- _21
(14,	6,	6,	6,	5,	7,	7,	8,	8,	8,	8),	-- _22
(15,	0,	6,	6,	5,	17,	12,	13,	13,	13,	13),	-- _23
(16,	0,	0,	6,	6,	12,	12,	13,	13,	13,	13),	-- _24
(17,	0,	0,	0,	0,	0,	0,	13,	13,	13,	13);	-- _25