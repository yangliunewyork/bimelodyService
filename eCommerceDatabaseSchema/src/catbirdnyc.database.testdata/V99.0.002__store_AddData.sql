LOCK TABLES store Write;

INSERT INTO store
(     
	store_name,
	unique_store_name,
	contact_number,
	contact_email,
	store_website,
    store_description,
    store_cover_image,
    yelp_link,
    facebook_link,
    instagram_link,
    twitter_link
)
VALUES
(
 "movintnyc",
 "movintnyc",
 "9173760958",
 "yl916@nyu.edu",
 "https://movint.com/",
 "Creating the perfect for every girl's dream wardrobe. We believe in the beauty of understated fashion <3",
 "https://cdn.shopify.com/s/files/1/1081/8404/files/Untitled-1_copy.jpg",
 "https://www.yelp.com/biz/movint-new-york-new-york-2",
 "https://www.facebook.com/movintnyc",
 "https://www.instagram.com/movintnyc",
 "https://twitter.com/movintnyc"
 ),
(
 "Doyle & Doyle",
 "DoyleandDoyle",
  "9171111111",
  "test@test.com",
 "https://doyledoyle.com/",
 "At Doyle & Doyle, we love being part of your celebrations, birthdays, new jobs, engagements, or just because..",
 "https://cdn.shopify.com/s/files/1/2130/1141/files/DND_Logo-Blue.png",
 "https://www.yelp.com/biz/doyle-and-doyle-new-york-2",
 "https://www.facebook.com/DoyleandDoyle/",
 "https://www.instagram.com/doyleanddoyle",
 "https://twitter.com/doyleanddoyle"
),
(
 "Sephora",
 "Sephora-new-york-5th-avenue",
  "9172222222",
  "test2@test2.com",
 "https://www.sephora.com/happening/stores/new-york-5th-avenue",
 "Sephora is a French multinational retailer of personal care and beauty products. Featuring nearly 3,000 brands,[3] along with its own private label, Sephora Collection, Sephora offers beauty products including cosmetics, skincare, body, fragrance, nail color, beauty tools, body lotions and haircare. The company was founded in Limoges in 1970 and is currently based in Paris.[4][5] Sephora is owned by luxury conglomerate LVMH as of 1997. The name comes from the Greek spelling of Zipporah (Greek: Σεπφώρα, Sepphōra), wife of Moses.",
 "https://www.sephora.com/img/ufe/logo.svg",
 "https://www.yelp.com/biz/sephora-new-york-11",
 "https://www.facebook.com/sephora",
 "https://www.instagram.com/sephora",
 "https://twitter.com/Sephora"
),
(
 "M.M.LaFleur",
 "M.M.LaFleur-bryant-park",
  "9173333333",
  "test3@test3.com",
 "https://mmlafleur.com/pages/bryant-park-appointments",
 "M.M.LaFleur",
 "https://mma.prnewswire.com/media/1481302/2019_MML_Wordmark_Logo.jpg",
 "https://www.yelp.com/biz/m-m-lafleur-new-york-2",
 "https://www.facebook.com/mmlafleurnyc",
 "https://www.instagram.com/mmlafleur",
 "https://twitter.com/mmlafleur"
);

UNLOCK TABLES;