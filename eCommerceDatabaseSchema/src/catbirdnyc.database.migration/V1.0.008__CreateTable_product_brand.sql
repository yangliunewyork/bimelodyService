CREATE TABLE product_brand (
     product_brand_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
     brand_name VARCHAR(256) NOT NULL,
     brand_website VARCHAR(256) NOT NULL,
     brand_description VARCHAR(1024) NOT NULL,
     creation_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
     modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (product_brand_id)
);
