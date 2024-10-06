-- Insert random categories
INSERT INTO categories (name, description)
SELECT
    'Category ' || i AS name,
    'Description for category ' || i AS description
FROM generate_series(1, 10) AS s(i);

-- Insert random products
INSERT INTO products (name, description, price, stock_quantity, category_id)
SELECT
    'Product ' || i AS name,
    'Description for product ' || i AS description,
    round((RANDOM() * 100 + 10)::numeric, 2) AS price, -- random price between 10 and 110
    (RANDOM() * 100)::int AS stock_quantity, -- random stock quantity between 0 and 100
    (SELECT id FROM categories ORDER BY RANDOM() LIMIT 1) AS category_id -- random category
FROM generate_series(1, 20) AS s(i)
ON CONFLICT (name) DO NOTHING;

-- Insert random customers
INSERT INTO customers (username, first_name, last_name, email, password, phone)
SELECT
    'user' || i AS username,
    'FirstName' || i AS first_name,
    'LastName' || i AS last_name,
    'email' || i || '@example.com' AS email,
    'password' || i AS password,
    '123456789' || i AS phone
FROM generate_series(1, 10) AS s(i)
ON CONFLICT (email) DO NOTHING;

-- Insert random orders
INSERT INTO orders (customer_id, status, total)
SELECT
    (SELECT id FROM customers ORDER BY RANDOM() LIMIT 1) AS customer_id,
    CASE
        WHEN RANDOM() < 0.5 THEN 'Completed'
        ELSE 'Pending'
        END AS status,
    round((RANDOM() * 100 + 20)::numeric, 2) AS total -- random total between 20 and 120
FROM generate_series(1, 15) AS s(i);

-- Insert random order items
INSERT INTO order_items (order_id, product_id, quantity, price)
SELECT
    (SELECT id FROM orders ORDER BY RANDOM() LIMIT 1) AS order_id,
    (SELECT id FROM products ORDER BY RANDOM() LIMIT 1) AS product_id,
    (RANDOM() * 10)::int + 1 AS quantity, -- random quantity between 1 and 10
    round((RANDOM() * 100 + 10)::numeric, 2) AS price -- random price between 10 and 110
FROM generate_series(1, 30) AS s(i);

-- Insert random shipping addresses
INSERT INTO shipping_addresses (customer_id, address_line1, address_line2, city, state, postal_code, country)
SELECT
    (SELECT id FROM customers ORDER BY RANDOM() LIMIT 1) AS customer_id,
    'Address Line 1' || i AS address_line1,
    'Address Line 2' || i AS address_line2,
    'City' || i AS city,
    'State' || i AS state,
    'Postal' || i AS postal_code,
    'Country' || i AS country
FROM generate_series(1, 10) AS s(i);

-- Insert random payments
INSERT INTO payments (order_id, amount, payment_method, status)
SELECT
    (SELECT id FROM orders ORDER BY RANDOM() LIMIT 1) AS order_id,
    round((RANDOM() * 100 + 20)::numeric, 2) AS amount, -- random amount between 20 and 120
    CASE
        WHEN RANDOM() < 0.5 THEN 'Credit Card'
        ELSE 'PayPal'
        END AS payment_method,
    CASE
        WHEN RANDOM() < 0.5 THEN 'Completed'
        ELSE 'Pending'
        END AS status
FROM generate_series(1, 15) AS s(i);

-- Insert random roles
INSERT INTO roles (name)
VALUES
    ('ROLE_USER'),
    ('ROLE_ADMIN')
ON CONFLICT (name) DO NOTHING;

-- Insert random tags
INSERT INTO tags (name)
SELECT
    'Tag ' || i AS name
FROM generate_series(1, 10) AS s(i)
ON CONFLICT (name) DO NOTHING;

-- Insert random reviews
INSERT INTO reviews (customer_id, product_id, rating, comment)
SELECT
    (SELECT id FROM customers ORDER BY RANDOM() LIMIT 1) AS customer_id,
    (SELECT id FROM products ORDER BY RANDOM() LIMIT 1) AS product_id,
    ((RANDOM() * 4)::int + 1) AS rating, -- random rating between 1 and 5
    'Review comment ' || i AS comment
FROM generate_series(1, 15) AS s(i);

-- Insert random customer roles
INSERT INTO customer_roles (customer_id, role_id)
SELECT
    (SELECT id FROM customers ORDER BY RANDOM() LIMIT 1) AS customer_id,
    (SELECT id FROM roles ORDER BY RANDOM() LIMIT 1) AS role_id
FROM generate_series(1, 10) AS s(i)
ON CONFLICT (customer_id, role_id) DO NOTHING;

-- Insert random product tags
INSERT INTO product_tags (product_id, tag_id)
SELECT
    (SELECT id FROM products ORDER BY RANDOM() LIMIT 1) AS product_id,
    (SELECT id FROM tags ORDER BY RANDOM() LIMIT 1) AS tag_id
FROM generate_series(1, 20) AS s(i)
ON CONFLICT (product_id, tag_id) DO NOTHING;
