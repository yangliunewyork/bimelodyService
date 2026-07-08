LOCK TABLES product Write;

INSERT INTO product
(     
     product_name,
     unique_product_name_in_store,
     product_description,
     price_in_dollar,
     store_id
)
VALUES
(
    "HALF CHAIN CHARM RING",
    "HALF-CHAIN-CHARM-RING",
    "14k Gold Band width: 2mm, Chain width: 1.75mm, Opal TTCW: 0.0.16",
    220,
    1
),
(
    "DOT LETTER STUD WITH STONES",
    "DOT-LETTER-STUD-WITH-STONES",
    "14k gold, Enamel, Genuine Stones, Sold as singles ",
    95,
    1
),
(
    "DUAL CHAIN NECKLACE",
    "DUAL-CHAIN-NECKLACE",
    "14k Gold, Lobster clasp closure, Necklace length: 14+2.5 extension",
    190,
    1
),
(
    "Diamond Tennis Bracelet c1980",
    "Diamond-Tennis-Bracelet-c1980",
    "When champion tennis star Chris Evert famously sported a diamond eternity bracelet on the court in the 1980s, these easy-to-wear jewelry staples were forever coined 'tennis bracelets'! Whether you call it an eternity bracelet or a tennis bracelet, the implication is the same– these are forever, get-through-anything, wear-in-any-event jewelry pieces! Diamonds are the hardest material on earth, and gold among the most durable metals– combined with their incredible beauty, it is no wonder this style is so highly valued.",
    4200,
    2
),
(
    "Rare E. Gübelin Pocket Watch c1910",
    "Rare-E.-Gübelin-Pocket-Watch-c1910",
    "Beautiful inside and out! This stunning antique pocket watch is extraordinary in its impeccable make and condition! Most watches of this age have crazing on the face, inscriptions, dents, and chips in the enamel, but this one, at over 110 years old, looks to be in mint condition!",
    2650,
    2
);

UNLOCK TABLES;