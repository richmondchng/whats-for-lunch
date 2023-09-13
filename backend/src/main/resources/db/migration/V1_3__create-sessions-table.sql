-- create user table

CREATE TABLE IF NOT EXISTS `sessions` (
    `id` INT AUTO_INCREMENT COMMENT 'ID',
    `date` DATE NOT NULL COMMENT 'Session date',
    `owner_id` INT NOT NULL COMMENT 'Session owner id',
    `version` INT NOT NULL COMMENT 'version',
    PRIMARY KEY (id),
    CONSTRAINT fk_owner_session FOREIGN KEY (owner_id) REFERENCES users(id)
);