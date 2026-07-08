CREATE TABLE product (
     product_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
     unique_product_name_in_store VARCHAR(256) NOT NULL,
     product_name VARCHAR(256) NOT NULL,
     product_description VARCHAR(1024) NOT NULL,
     price_in_dollar DECIMAL(16,2) NOT NULL,
     quantity INT UNSIGNED NOT NULL DEFAULT 0,
     store_id INT UNSIGNED NOT NULL,
     creation_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
     modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (product_id),
     CONSTRAINT FK_product_RT_store_RC_store_id
     FOREIGN KEY (store_id) REFERENCES store(store_id),
     UNIQUE KEY unique_product_name_in_store (store_id, unique_product_name_in_store)
);
