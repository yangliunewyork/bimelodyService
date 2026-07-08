CREATE TABLE product_tag (
     product_tag_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
     tag_content VARCHAR(256),
     creation_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
     modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (product_tag_id)
);