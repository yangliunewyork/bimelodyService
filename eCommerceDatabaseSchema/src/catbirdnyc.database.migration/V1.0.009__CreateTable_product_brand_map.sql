CREATE TABLE product_brand_map (
  product_brand_map_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  product_id INT UNSIGNED NOT NULL,
  product_brand_id INT UNSIGNED NOT NULL,
  creation_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
  modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (product_brand_map_id),
  CONSTRAINT FK_product_brand_map_RT_product_RC_product_id
  FOREIGN KEY (product_id) REFERENCES product(product_id),
  CONSTRAINT FK_product_brand_map_RT_product_brand_RC_product_brand_id
  FOREIGN KEY (product_brand_id) REFERENCES product_brand(product_brand_id)
);
