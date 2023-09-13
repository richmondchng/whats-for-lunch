
-- sessions
INSERT INTO sessions (session_date, owner_id, status, version) 
SELECT '2023-09-04', u.ID, 'CLOSED', 1
FROM users u
WHERE u.user_name = 'john';

INSERT INTO sessions (session_date, owner_id, status, version) 
SELECT '2023-09-05', u.ID, 'DELETED', 1
FROM users u
WHERE u.user_name = 'john';

INSERT INTO sessions (session_date, owner_id, status, version) 
SELECT '2023-09-05', u.ID, 'CLOSED', 1
FROM users u
WHERE u.user_name = 'john';

INSERT INTO sessions (session_date, owner_id, status, version) 
SELECT '2023-09-06', u.ID, 'CLOSED', 1
FROM users u
WHERE u.user_name = 'john';

INSERT INTO sessions (session_date, owner_id, status, version) 
SELECT '2023-09-07', u.ID, 'OPEN', 0
FROM users u
WHERE u.user_name = 'pam';

INSERT INTO sessions (session_date, owner_id, status, version) 
SELECT '2023-09-07', u.ID, 'OPEN', 0
FROM users u
WHERE u.user_name = 'natasha';

INSERT INTO sessions (session_date, owner_id, status, version) 
SELECT '2023-09-07', u.ID, 'OPEN', 0
FROM users u
WHERE u.user_name = 'john';

-- participants

INSERT INTO session_participants (session_id, user_id, status, version) 
SELECT s.ID, (SELECT id FROM users WHERE user_name='john'), 'JOINED', 1
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-04' AND u.user_name='john';

INSERT INTO session_participants (session_id, user_id, status, version) 
SELECT s.ID, (SELECT id FROM users WHERE user_name='pam'), 'JOINED', 1
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-04' AND u.user_name='john';

INSERT INTO session_participants (session_id, user_id, status, version) 
SELECT s.ID, (SELECT id FROM users WHERE user_name='john'), 'JOINED', 1
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-05' AND u.user_name='john' AND s.status='DELETED';

INSERT INTO session_participants (session_id, user_id, status, version) 
SELECT s.ID, (SELECT id FROM users WHERE user_name='pam'), 'PENDING', 1
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-05' AND u.user_name='john' AND s.status='DELETED';

INSERT INTO session_participants (session_id, user_id, status, version) 
SELECT s.ID, (SELECT id FROM users WHERE user_name='john'), 'JOINED', 1
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-05' AND u.user_name='john' AND s.status='CLOSED';

INSERT INTO session_participants (session_id, user_id, status, version) 
SELECT s.ID, (SELECT id FROM users WHERE user_name='pam'), 'JOINED', 1
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-05' AND u.user_name='john' AND s.status='CLOSED';

INSERT INTO session_participants (session_id, user_id, status, version) 
SELECT s.ID, (SELECT id FROM users WHERE user_name='john'), 'JOINED', 1
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-06' AND u.user_name='john';

INSERT INTO session_participants (session_id, user_id, status, version) 
SELECT s.ID, (SELECT id FROM users WHERE user_name='victor'), 'JOINED', 1
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-06' AND u.user_name='john';

INSERT INTO session_participants (session_id, user_id, status, version) 
SELECT s.ID, (SELECT id FROM users WHERE user_name='john'), 'JOINED', 1
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-07' AND u.user_name='john';

INSERT INTO session_participants (session_id, user_id, status, version) 
SELECT s.ID, (SELECT id FROM users WHERE user_name='victor'), 'PENDING', 1
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-07' AND u.user_name='john';

INSERT INTO session_participants (session_id, user_id, status, version) 
SELECT s.ID, (SELECT id FROM users WHERE user_name='pam'), 'JOINED', 1
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-07' AND u.user_name='pam';

INSERT INTO session_participants (session_id, user_id, status, version) 
SELECT s.ID, (SELECT id FROM users WHERE user_name='victor'), 'PENDING', 1
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-07' AND u.user_name='pam';

INSERT INTO session_participants (session_id, user_id, status, version) 
SELECT s.ID, (SELECT id FROM users WHERE user_name='natasha'), 'JOINED', 1
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-07' AND u.user_name='natasha';

INSERT INTO session_participants (session_id, user_id, status, version) 
SELECT s.ID, (SELECT id FROM users WHERE user_name='pam'), 'PENDING', 1
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-07' AND u.user_name='natasha';
