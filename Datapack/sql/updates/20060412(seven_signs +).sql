ALTER TABLE `seven_signs`
CHANGE COLUMN `red_stones` `dawn_red_stones` INT(10) NOT NULL DEFAULT 0,
CHANGE COLUMN `green_stones` `dawn_green_stones` INT(10) NOT NULL DEFAULT 0,
CHANGE COLUMN `blue_stones` `dawn_blue_stones` INT(10) NOT NULL DEFAULT 0,
CHANGE COLUMN `ancient_adena_amount` `dawn_ancient_adena_amount` INT(10) NOT NULL DEFAULT 0,
CHANGE COLUMN `contribution_score` `dawn_contribution_score` INT(10) NOT NULL DEFAULT 0,
ADD COLUMN `dusk_red_stones` INT(10) NOT NULL DEFAULT 0,
ADD COLUMN `dusk_green_stones` INT(10) NOT NULL DEFAULT 0,
ADD COLUMN `dusk_blue_stones` INT(10) NOT NULL DEFAULT 0,
ADD COLUMN `dusk_ancient_adena_amount` INT(10) NOT NULL DEFAULT 0,
ADD COLUMN `dusk_contribution_score` INT(10) NOT NULL DEFAULT 0;

UPDATE `seven_signs` SET 
`dusk_red_stones` = `dawn_red_stones`, `dawn_red_stones` = 0,
`dusk_green_stones` = `dawn_green_stones`, `dawn_green_stones` = 0,
`dusk_blue_stones` = `dawn_blue_stones`, `dawn_blue_stones` = 0,
`dusk_ancient_adena_amount` = `dawn_ancient_adena_amount`, `dawn_ancient_adena_amount` = 0,
`dusk_contribution_score` = `dawn_contribution_score`, `dawn_contribution_score` = 0 
WHERE `cabal` = 'dusk';

ALTER TABLE `random_spawn_loc` ADD COLUMN `heading` INTEGER NOT NULL DEFAULT -1,
DROP PRIMARY KEY,
ADD PRIMARY KEY(`groupId`, `x`, `y`, `z`, `heading`);
