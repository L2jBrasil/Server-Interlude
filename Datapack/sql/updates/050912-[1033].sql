ALTER TABLE `items` ADD KEY `key_item_id` (`item_id`);
ALTER TABLE `items` ADD `time_of_use` INT;
ALTER TABLE `items` ADD KEY `key_time_of_use` (`time_of_use`);
