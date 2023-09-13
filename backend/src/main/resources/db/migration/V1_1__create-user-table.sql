-- create user table

CREATE TABLE IF NOT EXISTS `users` (
    `id` INT AUTO_INCREMENT COMMENT 'ID',
    `user_name` VARCHAR(100) NOT NULL COMMENT 'User name',
    `first_name` VARCHAR(100) NOT NULL COMMENT 'First name',
    `last_name` VARCHAR(100) NOT NULL COMMENT 'Last name',
    PRIMARY KEY (id)
);