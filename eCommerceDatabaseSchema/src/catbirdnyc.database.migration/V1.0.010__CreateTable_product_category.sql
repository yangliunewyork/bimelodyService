CREATE TABLE product_category (
     product_category_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
     category_type VARCHAR(256) NOT NULL,
     creation_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
     modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (product_category_id),
     CONSTRAINT UK_product_category_C_category_type
     UNIQUE (category_type)
);
