CREATE TABLE product_tag_map (
     product_tag_map_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
     product_id INT UNSIGNED NOT NULL,
     product_tag_id INT UNSIGNED NOT NULL,
     creation_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
     modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (product_tag_map_id),
     FOREIGN KEY (product_id) REFERENCES product(product_id),
     FOREIGN KEY (product_tag_id) REFERENCES product_tag(product_tag_id)
);