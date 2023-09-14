INSERT INTO session_restaurants (session_id, added_by_user, restaurant_name, description)
SELECT s.ID, (SELECT id FROM users WHERE user_name='natasha'), 'McTubby Burgers', 'Fresh burgers on a budget'
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-07' AND u.user_name='natasha';

INSERT INTO session_restaurants (session_id, added_by_user, restaurant_name, description)
SELECT s.ID, (SELECT id FROM users WHERE user_name='pam'), 'Java the Hutt Cafe', 'Fresh coffee and sandwiches'
FROM sessions s, users u
WHERE s.owner_id = u.id AND s.session_date = '2023-09-07' AND u.user_name='natasha';