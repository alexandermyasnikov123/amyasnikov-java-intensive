BEGIN;

CREATE TYPE gender AS ENUM ('Male', 'Female', 'Genderfluid battle helicopter Boeing AH-64 Apache');

CREATE TABLE users
(
    id        bigserial    NOT NULL PRIMARY KEY,
    username  varchar(100) NOT NULL UNIQUE,
    firstname varchar(100) NOT NULL,
    name      varchar(100) NOT NULL,
    lastname  varchar(100),
    gender    gender       NOT NULL DEFAULT 'Genderfluid battle helicopter Boeing AH-64 Apache',
    age       int          NOT NULL DEFAULT 18
);

TRUNCATE users RESTART IDENTITY;

INSERT INTO users(username, firstname, name)
VALUES ('iv_1', 'ivanov', 'ahmed'),
       ('an_2', 'andreev', 'rasul'),
       ('zv_200', 'copper', 'bull'),
       ('xx_300', 'petrov', 'mukhtar')
RETURNING (id, username, firstname, name, lastname, gender, age);

COMMIT;
