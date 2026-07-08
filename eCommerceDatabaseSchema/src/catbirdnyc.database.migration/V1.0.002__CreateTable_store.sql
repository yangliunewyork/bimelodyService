CREATE TABLE store (
     store_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
     unique_store_name VARCHAR(256) NOT NULL,
     contact_number VARCHAR(256)  NOT NULL,
     contact_email VARCHAR(256) NOT NULL,
     store_name VARCHAR(256) NOT NULL,
     store_website VARCHAR(256) NOT NULL,
     store_description VARCHAR(1024) NOT NULL,
     store_cover_image VARCHAR(256),
     yelp_link VARCHAR(256),
     facebook_link VARCHAR(256),
     instagram_link VARCHAR(256),
     twitter_link VARCHAR(256),
     creation_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
     modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (store_id),
     UNIQUE (unique_store_name)
);
