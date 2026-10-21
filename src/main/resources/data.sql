-- Tyhjennetään taulut ja nollataan ID-numerointi
TRUNCATE TABLE order_table, user_table RESTART IDENTITY;

-- Käyttäjät
INSERT INTO user_table (username, password, email, role)
VALUES
    ('ParcelPaws', '$2y$10$ecz0nh189AUTsYCvAvPk3.O08jicnlzU/9SwxaMyCJSkX5sovYAj6', 'parcel@example.com', 'USER'),
    ('PurrExpress', '$2y$10$1veUPAJwHlAPJ8sK2Ty9H.MmM463UCIQbUy89m1ZzpxmdJZ29kGee', 'express@example.com', 'USER'),
    ('DispatchCat', '$2y$10$lvkOSGG73mD07IgHWjfNeOe4N9MR6Dz4OG9uX4yk4vpqn.FWVN8Wy', 'dispatch@example.com', 'ADMIN');

-- Toimitustilaukset
INSERT INTO order_table (customer, address, delivery_date, status, user_id)
VALUES
    ('Cat Food Express - Musti ja Mirri Kamppi',
     'Urho Kekkosen katu 1, 00100 Helsinki', CURRENT_DATE, 'UNASSIGNED', NULL),

    ('Purrfect Delivery - Musti ja Mirri Hakaniemi',
     'Siltasaarenkatu 12, 00530 Helsinki', CURRENT_DATE, 'UNASSIGNED', NULL),

    ('Meow Supplies - Musti ja Mirri Ruoholahti',
     'Itämerenkatu 21, 00180 Helsinki', CURRENT_DATE, 'UNASSIGNED', NULL);