BEGIN;

CREATE TABLE groups (
  id SERIAL PRIMARY KEY,
  group_name VARCHAR(100) UNIQUE
);

CREATE TABLE disciplines (
  id SERIAL PRIMARY KEY,
  discipline_name VARCHAR(100) UNIQUE
);

CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) UNIQUE,
  email VARCHAR(255) UNIQUE,
  password VARCHAR (256),
  role VARCHAR(50),
  group_id BIGINT,
  FOREIGN KEY (group_id) REFERENCES groups(id) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE tokens (
  id SERIAL PRIMARY KEY,
  access_token TEXT,
  refresh_token TEXT,
  is_logged_out BOOLEAN DEFAULT FALSE,
  user_id BIGINT,
  FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE queues (
  id SERIAL PRIMARY KEY,
  is_active BOOLEAN DEFAULT TRUE,
  discipline_id BIGINT,
  FOREIGN KEY (discipline_id) REFERENCES disciplines(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE group_disciplines (
  group_id BIGINT,
  discipline_id BIGINT,
  PRIMARY KEY (group_id, discipline_id),
  FOREIGN KEY (group_id) REFERENCES groups(id) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (discipline_id) REFERENCES disciplines(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE queue_users (
  queue_id BIGINT,
  user_id BIGINT,
  PRIMARY KEY (queue_id, user_id),
  FOREIGN KEY (queue_id) REFERENCES queues(id) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
);

COMMIT;