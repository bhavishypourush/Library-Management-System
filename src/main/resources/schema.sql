CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('LIBRARIAN','MEMBER') NOT NULL,
    name VARCHAR(100),
    email VARCHAR(100),
    membership_id VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(30),
    genre VARCHAR(80),
    available BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    borrow_date DATE NOT NULL,
    return_date DATE NULL,
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (member_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    message VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES users(id)
);

INSERT IGNORE INTO users(username,password,role,name,email,membership_id)
VALUES
('admin','admin123','LIBRARIAN','Library Admin','admin@library.com',NULL),
('member','member123','MEMBER','Demo Member','member@library.com','M001');

INSERT INTO books(title,author,isbn,genre,available)
SELECT 'The Alchemist','Paulo Coelho','9780061122415','Fiction',TRUE
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn='9780061122415');

INSERT INTO books(title,author,isbn,genre,available)
SELECT 'Clean Code','Robert C. Martin','9780132350884','Programming',TRUE
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn='9780132350884');

INSERT INTO books(title,author,isbn,genre,available)
SELECT 'Atomic Habits','James Clear','9780735211292','Self Help',TRUE
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn='9780735211292');
