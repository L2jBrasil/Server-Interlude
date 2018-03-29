UPDATE seven_signs SET dawn_red_stones = dawn_red_stones + dusk_red_stones, 
dawn_green_stones = dawn_green_stones + dusk_green_stones, 
dawn_blue_stones = dawn_blue_stones + dusk_blue_stones, 
dawn_ancient_adena_amount = dawn_ancient_adena_amount + dusk_ancient_adena_amount, 
dawn_contribution_score = dawn_contribution_score + dusk_contribution_score;
 
ALTER TABLE seven_signs
CHANGE COLUMN dawn_red_stones red_stones INT(10) NOT NULL DEFAULT 0,
CHANGE COLUMN dawn_green_stones green_stones INT(10) NOT NULL DEFAULT 0,
CHANGE COLUMN dawn_blue_stones blue_stones INT(10) NOT NULL DEFAULT 0,
CHANGE COLUMN dawn_ancient_adena_amount ancient_adena_amount INT(10) NOT NULL DEFAULT 0,
CHANGE COLUMN dawn_contribution_score contribution_score INT(10) NOT NULL DEFAULT 0,
DROP COLUMN dusk_red_stones,
DROP COLUMN dusk_green_stones,
DROP COLUMN dusk_blue_stones,
DROP COLUMN dusk_ancient_adena_amount,
DROP COLUMN dusk_contribution_score; 
