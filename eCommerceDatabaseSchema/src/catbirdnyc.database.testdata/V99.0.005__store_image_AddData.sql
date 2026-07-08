LOCK TABLES store_image Write;

INSERT INTO store_image
(
	store_id,
	store_image_link
)
VALUES
(1, "https://cdn.shopify.com/s/files/1/1081/8404/files/Untitled-1_copy.jpg"),
(2, "https://cdn.shopify.com/s/files/1/2130/1141/files/DND_Logo-Blue.png");

UNLOCK TABLES;