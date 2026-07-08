CREATE TABLE store_location (
     store_location_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
     store_id INT UNSIGNED NOT NULL,
     formatted_address VARCHAR(1024) NOT NULL,
     latitude DECIMAL( 10, 6 ) NOT NULL,
     longitude DECIMAL( 10, 6 ) NOT NULL,
     creation_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
     modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (store_location_id),
     CONSTRAINT FK_store_location_RT_store_RC_store_id
     FOREIGN KEY (store_id) REFERENCES store(store_id)
);