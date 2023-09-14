-- create session restaurant

CREATE TABLE IF NOT EXISTS `session_restaurants` (
    `id` INT AUTO_INCREMENT COMMENT 'ID',
    `session_id` INT NOT NULL COMMENT 'Session id',
    `added_by_user` INT NOT NULL COMMENT 'Added by user id',
    `restaurant_name` VARCHAR(255) NOT NULL COMMENT 'Restaurant name',
    `description` VARCHAR(255) NOT NULL COMMENT 'Description',
    `status` VARCHAR(15) NOT NULL DEFAULT 'PENDING' COMMENT 'Record status',
    `version` INT NOT NULL COMMENT 'version',
    PRIMARY KEY (id),
    CONSTRAINT fk_restaurant_session FOREIGN KEY (session_id) REFERENCES sessions(id)
);