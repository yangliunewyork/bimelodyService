CREATE TABLE store_image (
     store_image_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
     store_id INT UNSIGNED NOT NULL,
     store_image_link VARCHAR(1024) NOT NULL,
     creation_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
     modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (store_image_id),
     CONSTRAINT FK_store_image_RT_store_RC_store_id
     FOREIGN KEY (store_id) REFERENCES store(store_id)
);