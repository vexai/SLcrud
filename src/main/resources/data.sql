REPLACE INTO `roles` VALUES (1,'ROLE_ADMIN');
REPLACE INTO `roles` VALUES (2,'ROLE_USER');

REPLACE INTO `users` VALUES
(999,1,'s``@s.pl', 's', 's', '$2a$10$TCeuOX8djjhw4EQRF81o7ODdz0dSAY5wa1meWRkfHB4l0ZT9uNN6G','admin');

--Hasło do administratora '111111'. Wyżej jest zaszyfrowane, żeby program mógł je odszyfrować jako właśnie 111111.
