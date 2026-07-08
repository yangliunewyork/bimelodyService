CREATE TABLE customer (
     customer_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
     cognito_user_id VARCHAR(96),
     cognito_user_pool_id VARCHAR(32),
     email VARCHAR(256) NOT NULL,
     phone_number VARCHAR(32) NOT NULL,
     username VARCHAR(32) NOT NULL,
     profile_picture_link VARCHAR(256),
     is_banned BOOLEAN NOT NULL DEFAULT 0,
     creation_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
     modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (customer_id),
     UNIQUE (cognito_user_id),
     UNIQUE (email),
     UNIQUE (username)
);