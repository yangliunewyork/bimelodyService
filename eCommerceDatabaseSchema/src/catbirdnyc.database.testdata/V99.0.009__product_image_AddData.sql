LOCK TABLES product_image Write;

INSERT INTO product_image
(
	product_id,
	product_image_link
)
VALUES
(1, "https://cdn.shopify.com/s/files/1/0466/8784/6560/products/HALFCHAINCHARMRING_1_160x.jpg"),
(1, "https://cdn.shopify.com/s/files/1/0466/8784/6560/products/HALFCHAINCHARMRING_2_1200x.jpg"),
(2, "https://cdn.shopify.com/s/files/1/0466/8784/6560/products/image_e20d98d7-907d-45d5-b654-f38ff7ba7894_900x.jpg"),
(3, "https://cdn.shopify.com/s/files/1/0466/8784/6560/products/DUALCHAINNECKLACE_1_1000x.jpg"),
(3, "https://cdn.shopify.com/s/files/1/0466/8784/6560/products/DUALCHAINNECKLACE_2_1400x.jpg"),
(4, "https://cdn.shopify.com/s/files/1/2130/1141/products/111592R_h_Edwardian_Sapphire_Diamond_Ring_1024x1024@2x.jpg"),
(5, "https://cdn.shopify.com/s/files/1/2130/1141/products/111606R_f_Vintage_Tiffany_Co_Engagement_Ring_1024x1024@2x.jpg");

UNLOCK TABLES;