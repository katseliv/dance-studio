ALTER TABLE dancestudio.users
    ALTER COLUMN username TYPE VARCHAR(100),
    ALTER COLUMN first_name DROP NOT NULL,
    ALTER COLUMN last_name DROP NOT NULL,
    ALTER COLUMN image DROP NOT NULL,
    ALTER COLUMN phone_number DROP NOT NULL,
    ALTER COLUMN time_zone DROP NOT NULL;