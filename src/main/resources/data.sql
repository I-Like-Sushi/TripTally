INSERT INTO users (
    id,
    username,
    first_name,
    last_name,
    email,
    password,
    date_of_birth,
    enabled,
    gender,
    bio
) VALUES
      (100000000, 'jdoe',    'John',  'Doe',       'john.doe@example.com',     'hashedpassword', '1990-05-15', true,  'Male',   'Traveler and foodie'),
      (200000000, 'jennytty','Jen',   'Topare',    'jen.topare@example.com',   'hashedpassword', '1995-08-08', true,  'Female', 'I like things that excite me'),
      (300000000, 'alicew',  'Alice', 'Wonderland','alice.wonderland@example.com','hashedpassword','1988-12-01', true,'Female','Adventure seeker'),
      (400000000, 'bwayne',  'Bruce', 'Wayne',     'bruce.wayne@example.com',  'hashedpassword', '1980-02-19', true,  'Male',   'Dark Knight');

INSERT INTO user_roles (user_id, role) VALUES
                                           (100000000, 'ROLE_SUPERADMIN'),
                                           (200000000, 'ROLE_ADMIN'),
                                           (300000000, 'ROLE_USER'),
                                           (300000000, 'ROLE_ADMIN'),
                                           (400000000, 'ROLE_USER'),
                                           (400000000, 'ROLE_SUPERADMIN');

INSERT INTO user_allowed_access (user_id, allowed_username) VALUES
                                                                (100000000, 'alicew'),
                                                                (100000000, 'jennytty'),
                                                                (200000000, 'bwayne'),
                                                                (300000000, 'jdoe'),
                                                                (400000000, 'alicew');

INSERT INTO trip (
    id,
    user_id,
    destination,
    description,
    start_date,
    end_date,
    budget_home_currency,
    budget_local_currency,
    home_currency_code,
    local_currency_code
) VALUES
      ('TOKYO_20253010_5d873c', 100000000, 'Tokyo',    'Sightseeing',      '2025-10-30', '2026-01-25', 2500.00, 435012, 'EUR', 'JPY'),
      ('PARIS_20251105_abcd1234',200000000, 'Paris',    'Romantic getaway', '2025-11-05', '2025-11-12', 3000.00,   3000, 'EUR', 'EUR'),
      ('NYC_20260115_xYz7890Q',  300000000, 'New York','Business trip',     '2026-01-15', '2026-01-20', 5000.00,   5500, 'EUR', 'USD'),
      ('SYDNEY_20251210_qWeRtY12',400000000,'Sydney',  'Beach holiday',     '2025-12-10', '2026-01-02', 4500.00,   8000, 'EUR', 'AUD');

INSERT INTO expense (
    id,
    trip_id,
    description,
    amount_local,
    amount_home,
    date,
    time_stamp,
    category
) VALUES
      (101, 'TOKYO_20253010_5d873c', 'Bought some nice sushi', 4400,  25.29, '2025-02-11', '2025-02-11', 'Food & Dining'),
      (102, 'PARIS_20251105_abcd1234','Eiffel Tower tickets',    250,   2.95, '2025-11-06', '2025-11-06', 'Entertainment'),
      (103, 'TOKYO_20253010_5d873c', 'Train pass',              12000, 69.00,'2025-10-31', '2025-10-31','Transport'),
      (104, 'PARIS_20251105_abcd1234','Croissant breakfast',      15,    0.18, '2025-11-06', '2025-11-06','Food & Dining'),
      (105, 'NYC_20260115_xYz7890Q',  'Broadway show',           180,  200.00,'2026-01-17', '2026-01-17','Entertainment'),
      (106, 'SYDNEY_20251210_qWeRtY12','Harbour Bridge climb',    300,  310.00,'2025-12-11', '2025-12-11','Adventure');

INSERT INTO wishlist_item (
    id,
    trip_id,
    description,
    amount_local,
    amount_home,
    purchased,
    category
) VALUES
      ('aZ8kLmP2Q', 'TOKYO_20253010_5d873c', 'Football jersey',       20980, 12.57, false, 'Shopping'),
      ('bX7yZa01', 'PARIS_20251105_abcd1234','Louvre Museum tour',     50,    0.58, false, 'Entertainment'),
      ('cD9fGh23', 'TOKYO_20253010_5d873c', 'Kimono rental',          6000,  34.50, true,  'Shopping'),
      ('dE4kLm56', 'NYC_20260115_xYz7890Q',   'Statue of Liberty',     25,    27.50, false, 'Tour'),
      ('eR5tYu78', 'SYDNEY_20251210_qWeRtY12','Bondi Beach surf class',120,   125.00, false, 'Adventure'),
      ('fG1hIj90', 'PARIS_20251105_abcd1234','Seine river cruise',     45,    0.53, false, 'Entertainment');

