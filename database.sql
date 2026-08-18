-- ==========================================================
-- Student Management System - Database Script
-- Database: MySQL
-- ==========================================================

-- 1. Create Database if it does not already exist
CREATE DATABASE IF NOT EXISTS student_management;

-- 2. Select and use the database
USE student_management;

-- 3. Drop table if it exists (for clean re-runs)
DROP TABLE IF EXISTS students;

-- 4. Create 'students' Table
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    department VARCHAR(50) NOT NULL,
    year INT NOT NULL CHECK (year BETWEEN 1 AND 4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Insert Sample Student Records for Initial Testing
INSERT INTO students (student_name, email, phone, department, year) VALUES
('Aarav Sharma', 'aarav.sharma@example.com', '9876543210', 'Computer Science', 3),
('Diya Patel', 'diya.patel@example.com', '9812345678', 'Information Technology', 2),
('Rohan Verma', 'rohan.verma@example.com', '9765432109', 'Electronics & Comm', 4),
('Sneha Rao', 'sneha.rao@example.com', '9654321098', 'Mechanical Engineering', 1),
('Kavya Nair', 'kavya.nair@example.com', '9543210987', 'Computer Science', 2),
('Vikram Singh', 'vikram.singh@example.com', '9432109876', 'Civil Engineering', 3);

-- 6. Verify inserted records
SELECT * FROM students;
