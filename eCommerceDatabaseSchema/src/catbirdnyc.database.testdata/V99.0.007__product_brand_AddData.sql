LOCK TABLES product_brand Write;

INSERT INTO product_brand
(     
	brand_name,
	brand_website,
    brand_description
)
VALUES
(
 "lovettjewels",
 "https://lovettjewels.com/",
 "First and foremost we are three sisters: Danielle, Ali & Courtney, and our maiden name is Lovett. We come from different professional backgrounds. A sonographer, a nurse practitioner and last but not least a jewelry designer certified by the Gemological Institute of America. What unites us is our passion and love for jewelry. We are fortunate enough to lead busy professional lives as well as busy personal lives all with children of our own. These past few months have provided us with the opportunity to pursue a lifelong dream of creating our own jewelry line."
 ),
(
 "pippinvintage",
 "https://pippinvintage.com/",
 "On Orchard Street, below Delancey, Pippin Vintage Jewelry opened its doors in 2004, becoming one of the first boutiques to open on the burgeoning Lower East Side."
);

UNLOCK TABLES;