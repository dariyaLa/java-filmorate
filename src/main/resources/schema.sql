CREATE TABLE IF NOT EXISTS films (
        id int PRIMARY KEY,
        name varchar(100) NOT NULL,
        description varchar(100) NOT NULL,
        releaseDate date,
        duration long,
        mpa_id int
);

CREATE TABLE IF NOT EXISTS users (
        id int PRIMARY KEY,
        username varchar(100) NOT NULL,
        login varchar(100) NOT NULL,
        email varchar(100) NOT NULL,
        birthday date
);

CREATE TABLE IF NOT EXISTS likes_film (
        film_id int NOT NULL,
        user_id int NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
        id int PRIMARY KEY,
        genre varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS films_genres (
        film_id int NOT NULL,
        genre_id int NOT NULL
);

CREATE TABLE IF NOT EXISTS mpa (
        mpa_id int PRIMARY KEY,
        rating varchar(100) NOT NULL,
        description varchar(100)
);

CREATE TABLE IF NOT EXISTS follows (
        following_user_id int NOT NULL,
        followed_user_id int NOT NULL,
        status boolean
);