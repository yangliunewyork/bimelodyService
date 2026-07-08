CREATE TABLE product_category_map (
  product_category_map_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  product_id INT UNSIGNED NOT NULL,
  product_category_id INT UNSIGNED NOT NULL,
  creation_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
  modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (product_category_map_id),
  CONSTRAINT FK_product_category_map_RT_product_product_id_RC_product_id
  FOREIGN KEY (product_id) REFERENCES product(product_id),
  CONSTRAINT FK_product_category_map_T_product_category_C_product_category_id
  FOREIGN KEY (product_category_id) REFERENCES product_category(product_category_id)
);
