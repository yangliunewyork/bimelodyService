CREATE TABLE store_category (
     store_category_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
     category_type VARCHAR(256) NOT NULL,
     creation_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
     modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (store_category_id),
     UNIQUE (category_type)
);
