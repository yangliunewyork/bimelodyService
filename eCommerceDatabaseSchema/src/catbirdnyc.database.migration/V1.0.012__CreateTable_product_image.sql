CREATE TABLE product_image (
     product_image_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
     product_id INT UNSIGNED NOT NULL,
     product_image_link VARCHAR(1024) NOT NULL,
     creation_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
     modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (product_image_id),
     FOREIGN KEY (product_id) REFERENCES product(product_id)
);