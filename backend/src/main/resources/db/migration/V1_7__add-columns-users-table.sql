-- add columns

ALTER TABLE `users` ADD COLUMN `password` varchar(255) COMMENT 'User password';

UPDATE `users` SET `password`='password' WHERE `password` IS NULL;

ALTER TABLE `users` ADD COLUMN `roles` varchar(255) DEFAULT 'ROLE_USER' COMMENT 'User roles';

UPDATE `users` SET `roles`='ROLE_USER' WHERE `user_name` IS NULL;
UPDATE `users` SET `roles`='ROLE_USER,ROLE_ADMIN' WHERE `user_name`='admin';
