-- create session table

CREATE TABLE IF NOT EXISTS `sessions` (
    `id` INT AUTO_INCREMENT COMMENT 'ID',
    `session_date` DATE NOT NULL COMMENT 'Session date',
    `owner_id` INT NOT NULL COMMENT 'Session owner id',
    `status` VARCHAR(15) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Record status',
    `version` INT NOT NULL COMMENT 'version',
    PRIMARY KEY (id),
    CONSTRAINT fk_owner_session FOREIGN KEY (owner_id) REFERENCES users(id)
);