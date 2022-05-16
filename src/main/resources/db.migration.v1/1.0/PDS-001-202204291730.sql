CREATE TABLE dancestudio.jwt_tokens
(
    id         SERIAL PRIMARY KEY,
    token      VARCHAR(500)                              NOT NULL,
    type       VARCHAR(50)                               NOT NULL,
    user_id    INTEGER REFERENCES dancestudio.users (id) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);