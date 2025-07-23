ALTER TABLE posts
ALTER COLUMN user_id TYPE BIGINT USING user_id::bigint;