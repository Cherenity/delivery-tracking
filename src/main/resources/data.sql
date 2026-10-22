-- Tyhjennetään taulut ja nollataan ID-numerointi
TRUNCATE TABLE order_table, user_table RESTART IDENTITY;

-- Käyttäjät
INSERT INTO user_table (username, password, email, role)
VALUES
    ('ParcelPaws', '$2y$10$QuiKN9wE.hWB5NzKPKh9IuCuyVueIh81t1oIlYHP4QrNeEo7Joh.G', 'parcel@example.com', 'USER'),
    ('PurrExpress', '$2y$10$6zsLY.c/UDMUFWGnGqmKCOAGxI8XS.A.iuGDuNAgPYsSyWDT86.NC', 'express@example.com', 'USER'),
    ('DispatchCat', '$2y$10$mimsJG9ynndpUohYFG4.2OH3FjX6CLXE7g6e6FF2lNIwWWseIBP.m', 'dispatch@example.com', 'ADMIN');

-- Toimitustilaukset
INSERT INTO order_table (customer, address, delivery_date, status, user_id)
VALUES
    ('Musti ja Mirri Kamppi',
     'Urho Kekkosen katu 1, 00100 Helsinki', CURRENT_DATE, 'UNASSIGNED', NULL),

    ('Musti ja Mirri Hakaniemi',
     'Siltasaarenkatu 12, 00530 Helsinki', CURRENT_DATE, 'UNASSIGNED', NULL),

    ('Musti ja Mirri Ruoholahti',
     'Itämerenkatu 21, 00180 Helsinki', CURRENT_DATE, 'UNASSIGNED', NULL);