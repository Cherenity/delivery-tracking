-- Testidata H2-tietokantaan

-- Käyttäjät
INSERT INTO user_table (username, password, email, role)
VALUES
    ('admin', '$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6', 'admin@pawtrack.fi', 'ROLE_ADMIN'),
    ('driver1', '$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6', 'driver1@pawtrack.fi', 'ROLE_USER'),
    ('driver2', '$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6', 'driver2@pawtrack.fi', 'ROLE_USER');

-- Toimitustilaukset
INSERT INTO order_table (customer, address, delivery_date, status, delivery_order, user_id)
VALUES
    ('Musti ja Mirri Hakaniemi', 'Siltasaarenkatu 12, 00530 Helsinki', CURRENT_DATE, 'ASSIGNED', 1, 2),
    ('Musti ja Mirri Iso Omena', 'Piispansilta 11, 02230 Espoo', CURRENT_DATE, 'ASSIGNED', 2, 2),
    ('Musti ja Mirri Ratina', 'Vuolteenkatu 1, 33100 Tampere', CURRENT_DATE, 'ASSIGNED', 1, 3),
    ('Musti ja Mirri Skanssi', 'Skanssinkatu 10, 20730 Turku', CURRENT_DATE, 'UNASSIGNED', 0, NULL),
    ('Musti ja Mirri Kaakkuri', 'Metsokankaantie 3, 90420 Oulu', CURRENT_DATE, 'DELIVERED', 3, 2);
