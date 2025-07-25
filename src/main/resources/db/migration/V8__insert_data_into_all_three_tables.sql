INSERT INTO users (username, enabled, display_name) VALUES
('alice@gmail.com', true, 'Alice'),
('bob@hotmail.com', true, 'Bob'),
('charlie@gmail.com', true, 'Charlie'),
('diana@hotmail.com', true, 'Diana'),
('ed@gmail.com', true, 'Ed');

INSERT INTO posts (content, like_count, user_id) VALUES
('Excited to join Acebook!', 3, 1),
('Just had an amazing coffee ☕️', 5, 2),
('Can’t believe it’s already Friday!', 2, 3),
('Anyone want to go hiking this weekend?', 1, 4),
('Trying to learn Spring Boot — send help!', 4, 5),
('Lazy weekend vibes 😴', 8, 1);

INSERT INTO comments (post_id, user_id, content, like_count) VALUES
(1, 2, 'Welcome Alice!', 2),
(1, 3, 'Glad to see you here.', 1);

INSERT INTO comments (post_id, user_id, content, like_count) VALUES
(2, 1, 'Where did you get it?', 1),
(2, 4, 'Sounds delicious!', 5),
(2, 5, 'Love good coffee!', 1);

INSERT INTO comments (post_id, user_id, content, like_count) VALUES
(3, 2, 'I know right?!', 2),
(3, 1, 'Time flies!', 1);

INSERT INTO comments (post_id, user_id, content, like_count) VALUES
(4, 5, 'I’m in! Where to?', 2),
(4, 3, 'Count me in!', 4);

INSERT INTO comments (post_id, user_id, content, like_count) VALUES
(5, 4, 'Same here lol', 2),
(5, 1, 'Spring Boot is 🔥', 1);

INSERT INTO comments (post_id, user_id, content, like_count) VALUES
(6, 3, 'Sundays are the best.', 11),
(6, 2, 'Enjoy and relax!', 4);
