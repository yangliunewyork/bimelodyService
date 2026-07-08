LOCK TABLES store_location Write;

INSERT INTO store_location
(
	store_id,
    latitude,
    longitude,
    formatted_address
)
VALUES
(1, 40.733028, -74.004250, "318 Bleecker St. New York, NY 10014, US"),
(2, 40.740690, -74.007030, "412 W 13th St, New York, NY 10014, US"),
(3, 40.757130, -73.979050, "580 5th Ave New York, NY 10036, US"),
(4, 40.754940, -73.985650, "130 W 42nd St, Fl 13, New York, NY 10036, US");

UNLOCK TABLES;