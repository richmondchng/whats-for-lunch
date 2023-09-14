-- add selected_restaurant column

ALTER TABLE `sessions` ADD COLUMN `selected_restaurant` INT NOT NULL DEFAULT 0 COMMENT 'Selected restaurant';