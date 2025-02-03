BEGIN TRANSACTION;

-- Пользователи
CREATE TABLE IF NOT EXISTS users
(
    id            SERIAL PRIMARY KEY,           -- Идентификатор пользователя
    username      VARCHAR(100) NOT NULL UNIQUE, -- Имя пользователя
    email         VARCHAR(100) NOT NULL UNIQUE, -- Электронная почта
    password      VARCHAR(100) NOT NULL,        -- Пароль
    registered_at TIMESTAMP    NOT NULL         -- Дата регистрации
);

-- Товары
CREATE TABLE IF NOT EXISTS products
(
    id          SERIAL PRIMARY KEY,      -- Идентификатор товара
    name        VARCHAR(100)   NOT NULL, -- Название товара
    description TEXT,                    -- Описание
    price       DECIMAL(10, 2) NOT NULL, -- Цена единицы товара
    stock       INT            NOT NULL  -- Количество оставшихся единиц товара
);

-- Заказы. В одном заказе только один вид товара, но любое его количество.
-- Пример: 5 единиц товара "Карандаш".
CREATE TABLE IF NOT EXISTS orders
(
    id         SERIAL PRIMARY KEY, -- Идентификатор заказа
    user_id    INT       NOT NULL, -- Идентификатор пользователя
    order_date TIMESTAMP NOT NULL, -- Дата заказа
    product_id INT       NOT NULL, -- Идентификатор товара
    quantity   INT       NOT NULL  -- Количество
);

-- Внешние ключи
ALTER TABLE IF EXISTS orders
    ADD CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE IF EXISTS orders
    ADD CONSTRAINT fk_product
        FOREIGN KEY (product_id) REFERENCES products (id);

SELECT *
FROM pg_catalog.pg_tables
WHERE schemaname = 'public';

TRUNCATE users RESTART IDENTITY CASCADE;

TRUNCATE products RESTART IDENTITY CASCADE;

TRUNCATE orders RESTART IDENTITY CASCADE;

INSERT INTO users(username, email, password, registered_at)
VALUES ('john_doe', 'john@doe.org', 'pass1234', '2012-11-23 06:07:26'),
       ('jane_doe', 'jane@doe.com', 'asd123', '2023-11-23 07:08:16'),
       ('steven_ogg', 'steven@ogg.net', 'dawg222', '2023-01-02 07:07:07'),
       ('scott_art', 'scott@art.smtp', 'boss777', '2022-10-24 04:05:06'),
       ('stan_smith', 'stan@smith.uk', 'uk188a', '2021-04-19 09:00:26')
RETURNING (id, username, email);

INSERT INTO products(name, description, price, stock)
VALUES ('Cooking book', 'Simple cooking book for women', 12.24, 5),
       ('PC Keyboard', 'Wooden mechanical keyboard', 94.34, 3),
       ('Samsung TV', '96'' QD-OLED Smart TV', 167.55, 7),
       ('Vivo Smartphone', 'The most famous android phone ever', 77.77, 69),
       ('CD-RW', 'The elder the better', 5.56, 10)
RETURNING (id, name, description);

INSERT INTO orders(user_id, order_date, product_id, quantity)
VALUES (1, '2021-12-23 01:02:03', 1, 2),
       (1, '2021-12-23 01:03:04', 1, 1),
       (1, '2023-12-23 01:03:04', 2, 2),
       (2, '2023-11-21 02:04:03', 3, 3),
       (3, '2023-02-27 01:07:04', 4, 2)
RETURNING (id, user_id, order_date, product_id, quantity);

-- Найти всех пользователей, которые зарегистрировались раньше 1 января 2023 года и сделали хотя бы 1 заказ.
SELECT users.username, users.email, count(orders.quantity) AS orders
FROM orders
         JOIN users ON orders.user_id = users.id
WHERE users.registered_at::date < '2023-01-01'
  AND orders.quantity > 0
GROUP BY users.username, users.email;

-- Найти общий доход магазина за 2023 год.
SELECT 2023 AS period, sum(orders.quantity * products.price) AS total_earned_money
FROM orders
         JOIN products ON products.id = orders.product_id
WHERE date_part('year', orders.order_date) = 2023;

-- Найти 3 товара, для которых было продано наибольшее количество единиц.
SELECT products.name,
       sum(orders.quantity)                  AS total_quantity,
       sum(orders.quantity * products.price) AS total_earned
FROM orders
         JOIN products ON orders.product_id = products.id
GROUP BY products.name
ORDER BY sum(orders.quantity) DESC
LIMIT 3;

-- Оптимизация запроса.
-- Было:
EXPLAIN ANALYSE
SELECT o.id, o.order_date, p.name AS product_name, p.price, u.username
FROM orders o
         JOIN users u ON o.user_id = u.id
         JOIN products p ON o.product_id = p.id
WHERE u.id = 1
  AND o.order_date BETWEEN '2023-01-01' AND '2023-12-31';

-- Пред-фильтрация (перед объединением)
EXPLAIN ANALYSE
SELECT orders.id,
       orders.order_date,
       products.name       AS product_name,
       products.price,
       users_temp.username AS user_name
FROM (SELECT users.id, users.username FROM users WHERE users.id = 1 LIMIT 1) AS users_temp
         JOIN orders ON orders.user_id = 1
         JOIN products ON orders.product_id = products.id
WHERE orders.order_date BETWEEN '2023-01-01' AND '2023-12-31';

-- Заменил первый Join запросом с фильтрацией:
EXPLAIN ANALYSE
SELECT orders_temp.id,
       orders_temp.order_date,
       products.name       AS product_name,
       products.price,
       users_temp.username AS user_name
FROM (SELECT users.id, users.username FROM users WHERE users.id = 1 LIMIT 1) AS users_temp,
     (SELECT orders.id, orders.order_date, orders.product_id FROM orders WHERE orders.user_id = 1) AS orders_temp
         JOIN products ON orders_temp.product_id = products.id
WHERE orders_temp.order_date BETWEEN '2023-01-01' AND '2023-12-31';

/**
 * Шаги для оптимизации:
 * 1) Использование вложенного запроса с фильтрацией перед объединением
 * 2) Выборка только одного значения из пользователей (ограничение 1 кортежа)
 * 3) Замена первого объединения `orders` на под-запрос с фильтрацией
**/

ROLLBACK;
