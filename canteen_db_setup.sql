-- 1. Create the database
CREATE DATABASE IF NOT EXISTS canteen_db;
USE canteen_db;

-- 2. Create 'menu' table
CREATE TABLE IF NOT EXISTS menu (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL UNIQUE,
    price DOUBLE NOT NULL
);

-- 3. Create 'orders' table
CREATE TABLE IF NOT EXISTS orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    FOREIGN KEY (item_name) REFERENCES menu(item_name)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
