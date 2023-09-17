INSERT INTO session_restaurants (session_id, added_by_user, restaurant_name, description, status, version)
SELECT s.ID, u.id, 'McTubby Burgers', 'Fresh burgers on a budget', 'ACTIVE', 0
FROM sessions s, session_participants p, users u
WHERE s.id = p.session_id AND p.user_id = u.id AND u.user_name='john';

INSERT INTO session_restaurants (session_id, added_by_user, restaurant_name, description, status, version)
SELECT s.ID, u.id, 'Out There Steaks', 'Yummy grills', 'ACTIVE', 0
FROM sessions s, session_participants p, users u
WHERE s.id = p.session_id AND p.user_id = u.id AND u.user_name='john';

INSERT INTO session_restaurants (session_id, added_by_user, restaurant_name, description, status, version)
SELECT s.ID, u.id, 'Java the Hutt Cafe', 'Fresh coffee and sandwiches', 'ACTIVE', 0
FROM sessions s, session_participants p, users u
WHERE s.id = p.session_id AND p.user_id = u.id AND u.user_name='natasha';

INSERT INTO session_restaurants (session_id, added_by_user, restaurant_name, description, status, version)
SELECT s.ID, u.id, 'Ken Tucky Fried Chicken', 'Fried chicken and root beer', 'ACTIVE', 0
FROM sessions s, session_participants p, users u
WHERE s.id = p.session_id AND p.user_id = u.id AND u.user_name='victor';

INSERT INTO session_restaurants (session_id, added_by_user, restaurant_name, description, status, version)
SELECT s.ID, u.id, 'Fresh Salads Today', 'Lets eat healthy', 'ACTIVE', 0
FROM sessions s, session_participants p, users u
WHERE s.id = p.session_id AND p.user_id = u.id AND u.user_name='pam';

INSERT INTO session_restaurants (session_id, added_by_user, restaurant_name, description, status, version)
SELECT s.ID, u.id, 'Astro Pizza', 'Its pizza day', 'ACTIVE', 0
FROM sessions s, session_participants p, users u
WHERE s.id = p.session_id AND p.user_id = u.id AND u.user_name='pam';


