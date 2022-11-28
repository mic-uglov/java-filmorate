CREATE TABLE IF NOT EXISTS genres (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(128));

CREATE TABLE IF NOT EXISTS mpa_rating (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(16) UNIQUE,
    description VARCHAR(256));

CREATE TABLE IF NOT EXISTS users (
    id IDENTITY PRIMARY KEY,
    email VARCHAR(32) NOT NULL,
    login VARCHAR(32) NOT NULL UNIQUE,
    name VARCHAR(256),
    birthday DATE NOT NULL);

CREATE TABLE IF NOT EXISTS films (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    description VARCHAR(1024) NOT NULL,
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    mpa BIGINT REFERENCES mpa_rating (id));

CREATE TABLE IF NOT EXISTS film_genre (
    film_id BIGINT NOT NULL REFERENCES films (id),
    genre_id BIGINT NOT NULL REFERENCES genres (id),
    PRIMARY KEY (film_id, genre_id));

CREATE TABLE IF NOT EXISTS likes (
    film_id BIGINT NOT NULL REFERENCES films (id),
    user_id BIGINT NOT NULL REFERENCES users (id),
    PRIMARY KEY (film_id, user_id));

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT NOT NULL REFERENCES users (id),
    friend_id BIGINT NOT NULL REFERENCES users (id),
    PRIMARY KEY (user_id, friend_id));