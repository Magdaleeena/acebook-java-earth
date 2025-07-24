DROP TABLE IF EXISTS posts;

CREATE TABLE posts (
  id bigserial PRIMARY KEY,
  content varchar(250) NOT NULL
);

DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id bigserial PRIMARY KEY,
  username varchar(50) NOT NULL UNIQUE,
  enabled boolean NOT NULL
);

ALTER TABLE posts ADD COLUMN like_count INT DEFAULT 0;

CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    content VARCHAR(250) NOT NULL,
    post_id INTEGER NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

ALTER TABLE users
ADD COLUMN display_name varchar(50);

ALTER TABLE posts
ADD COLUMN user_id INTEGER;

ALTER TABLE posts
ADD CONSTRAINT fk_posts_user
FOREIGN KEY (user_id)
REFERENCES users(id);


ALTER TABLE posts
ALTER COLUMN user_id TYPE BIGINT USING user_id::bigint;

ALTER TABLE posts
DROP COLUMN user_id,
ADD COLUMN username VARCHAR(50) NOT NULL UNIQUE;

ALTER TABLE posts
ADD CONSTRAINT fk_username
FOREIGN KEY (username)
REFERENCES users(username);