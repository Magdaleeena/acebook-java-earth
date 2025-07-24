DROP TABLE IF EXISTS comments;

CREATE TABLE comments (
  id bigserial PRIMARY KEY,
  post_id bigint NOT NULL,
  user_id bigint,
  content varchar(500) NOT NULL,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_post FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);
