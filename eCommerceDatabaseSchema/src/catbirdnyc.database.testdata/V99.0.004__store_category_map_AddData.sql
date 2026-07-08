LOCK TABLES store_category_map Write;

INSERT INTO store_category_map
(     
	  store_id,
      store_category_id
)
VALUES
(1, 2),
(1, 4),
(2, 1),
(3, 3),
(4, 2);

UNLOCK TABLES;