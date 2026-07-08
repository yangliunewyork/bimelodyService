CREATE TABLE store_category_map (
  store_category_map_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  store_id INT UNSIGNED NOT NULL,
  store_category_id INT UNSIGNED NOT NULL,
  creation_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
  modification_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (store_category_map_id),
  CONSTRAINT FK_store_category_map_RT_store_RC_store_id
  FOREIGN KEY (store_id) REFERENCES store(store_id),
  CONSTRAINT FK_store_category_map_RT_store_category_RC_store_category_id
  FOREIGN KEY (store_category_id) REFERENCES store_category(store_category_id)
);
