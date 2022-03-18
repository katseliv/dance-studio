CREATE SCHEMA dancestudio;

CREATE TABLE dancestudio.roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL
);

CREATE TABLE dancestudio.users
(
    id           SERIAL PRIMARY KEY,
    username     VARCHAR(30) UNIQUE                        NOT NULL,
    first_name   VARCHAR(50)                               NOT NULL,
    last_name    VARCHAR(50)                               NOT NULL,
    image        BYTEA                                     NOT NULL,
    email        VARCHAR(100)                              NOT NULL,
    phone_number VARCHAR(11)                               NOT NULL,
    password     VARCHAR(50)                               NOT NULL,
    role_id      INTEGER REFERENCES dancestudio.roles (id) NOT NULL,
    time_zone    VARCHAR(50)                               NOT NULL,
    is_deleted   BOOLEAN DEFAULT FALSE
);

CREATE TABLE dancestudio.trainer_profiles
(
    id                SERIAL PRIMARY KEY,
    user_trainer_id   INTEGER REFERENCES dancestudio.users (id) ON DELETE CASCADE NOT NULL,
    experienced_since TIMESTAMP                                                   NOT NULL,
    description       VARCHAR(500),
    salary            INTEGER                                                     NOT NULL
);

CREATE TABLE dancestudio.dance_styles
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(30) NOT NULL,
    description VARCHAR(200)
);

CREATE TABLE dancestudio.studios
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(30) NOT NULL,
    description VARCHAR(200),
    start_time  TIME        NOT NULL,
    end_time    TIME        NOT NULL
);

CREATE TABLE dancestudio.rooms
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(30)                                                   NOT NULL,
    description VARCHAR(200),
    studio_id   INTEGER REFERENCES dancestudio.studios (id) ON DELETE CASCADE NOT NULL,
    is_deleted  BOOLEAN DEFAULT FALSE
);

CREATE TABLE dancestudio.lessons
(
    id              SERIAL PRIMARY KEY,
    user_trainer_id INTEGER REFERENCES dancestudio.users (id)        NOT NULL,
    dance_style_id  INTEGER REFERENCES dancestudio.dance_styles (id) NOT NULL,
    start_datetime  TIMESTAMP                                        NOT NULL,
    duration        INTEGER                                          NOT NULL,
    room_id         INTEGER REFERENCES dancestudio.rooms (id)        NOT NULL,
    is_deleted      BOOLEAN DEFAULT FALSE
);

CREATE TABLE dancestudio.bookings
(
    id         SERIAL PRIMARY KEY,
    user_id    INTEGER REFERENCES dancestudio.users (id)   NOT NULL,
    lesson_id  INTEGER REFERENCES dancestudio.lessons (id) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE dancestudio.reviews
(
    id                SERIAL PRIMARY KEY,
    user_student_id   INTEGER REFERENCES dancestudio.users (id) NOT NULL,
    user_trainer_id   INTEGER REFERENCES dancestudio.users (id) NOT NULL,
    text              VARCHAR(300)                              NOT NULL,
    creation_datetime TIMESTAMP                                 NOT NULL,
    rate              INTEGER,
    is_deleted        BOOLEAN DEFAULT FALSE
);

CREATE TABLE dancestudio.posts
(
    id                SERIAL PRIMARY KEY,
    user_admin_id     INTEGER REFERENCES dancestudio.users (id) NOT NULL,
    image             BYTEA                                     NOT NULL,
    text              VARCHAR(300)                              NOT NULL,
    creation_datetime TIMESTAMP                                 NOT NULL,
    is_deleted        BOOLEAN DEFAULT FALSE
);

CREATE TABLE dancestudio.likes
(
    post_id    INTEGER REFERENCES dancestudio.posts (id) NOT NULL,
    user_id    INTEGER REFERENCES dancestudio.users (id) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (post_id, user_id)
);
