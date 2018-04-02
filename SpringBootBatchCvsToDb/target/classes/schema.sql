DROP TABLE IF EXISTS Feed;

CREATE TABLE Feed  (
    id BIGINT auto_increment NOT NULL PRIMARY KEY,
    feed_name VARCHAR(40),
    description VARCHAR(400)
);