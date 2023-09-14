-- create session participants table

CREATE TABLE IF NOT EXISTS `session_participants` (
    `session_id` INT NOT NULL COMMENT 'Session id',
    `user_id` INT NOT NULL COMMENT 'User Id',
    `status` VARCHAR(15) NOT NULL DEFAULT 'PENDING' COMMENT 'Record status',
    `version` INT NOT NULL COMMENT 'version',
    PRIMARY KEY (session_id, user_id),
    CONSTRAINT fk_session_session FOREIGN KEY (session_id) REFERENCES sessions(id),
    CONSTRAINT fk_session_participant FOREIGN KEY (user_id) REFERENCES users(id)
);