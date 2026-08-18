# Student Management System (Servlets + JDBC + MySQL)

A simple, clean, professional, and **interview-ready** web application built with **Java Servlets, JDBC, MySQL, HTML5, CSS3, and Vanilla JavaScript** running on **Apache Tomcat**.

---

## Table of Contents
1. [Project Overview](#1-project-overview)
2. [Key Features](#2-key-features)
3. [Technology Stack](#3-technology-stack)
4. [Application Architecture](#4-application-architecture)
5. [Project Folder Structure](#5-project-folder-structure)
6. [Database Setup (MySQL)](#6-database-setup-mysql)
7. [External JAR Setup (No Maven)](#7-external-jar-setup-no-maven)
8. [Apache Tomcat Server Setup](#8-apache-tomcat-server-setup)
9. [How to Run the Project](#9-how-to-run-the-project)
10. [End-to-End CRUD Flow](#10-end-to-end-crud-flow)
11. [Troubleshooting & Common Errors](#11-troubleshooting--common-errors)
12. [Interview Preparation Guide](#12-interview-preparation-guide)
13. [1–2 Minute Project Pitch](#13-12-minute-project-pitch)
14. [25+ Interview Questions & Answers](#14-25-interview-questions--answers)

---

## 1. Project Overview

The **Student Management System** is a full-stack Java web application designed to help educational institutions manage student records. It allows administrators to register students, view a directory of enrolled students, search by ID or Name, update student information, and delete records with confirmation safeguards.

The project strictly follows the **Model-View-Controller (MVC) / DAO design pattern** with clear separation of responsibilities, using **pure standard Java technologies without complex frameworks** (No Spring, No Hibernate, No Maven).

---

## 2. Key Features

- **Dashboard**: Summary metrics showing total enrolled students, quick navigation cards, and recent enrollments.
- **Add Student**: Form to enroll new students with client-side JavaScript validation and server-side constraint checks.
- **View All Students**: Interactive data table displaying all students with department badges, academic year indicators, and action buttons.
- **Search Student**: Query students by either `Student ID` or `Student Name` using parameterized SQL `LIKE` wildcard matching.
- **Update Student**: Pre-filled edit form allowing updates to name, email, phone, department, and academic year while keeping `student_id` immutable.
- **Delete Student**: Safe deletion flow with custom interactive modal dialog ("Are you sure you want to delete this student?").

---

## 3. Technology Stack

| Layer | Technology | Description |
| :--- | :--- | :--- |
| **Backend Language** | Java (JDK 8 / 11 / 17 / 21) | Core business logic and model definitions |
| **Web Layer** | Java Servlets (`javax.servlet` / `jakarta.servlet`) | HTTP request processing, routing, and controller logic |
| **Data Access** | JDBC (`java.sql.*`) | Database connectivity using `PreparedStatement` |
| **Database** | MySQL (8.x / 5.7) | Relational data persistence with constraints |
| **Web Server** | Apache Tomcat (9.x / 10.x) | Servlet container and HTTP web server |
| **Frontend UI** | HTML5, Vanilla CSS3 | Modern responsive UI, cards, tables, badges, modal |
| **Frontend Logic** | Vanilla JavaScript (ES6) | Client-side validation, DOM manipulation, delete modals |
| **Build Tool** | **None (No Maven)** | Pure dynamic web project with manual JAR placement |

---

## 4. Application Architecture

The application implements a 4-tier MVC/DAO architecture:

```text
┌────────────────────────────────────────────────────────┐
│                   Web Browser                          │
│         (HTML5 + Vanilla CSS3 + Vanilla JS)            │
└──────────────────────────┬─────────────────────────────┘
                           │ HTTP Request (GET / POST)
                           ▼
┌────────────────────────────────────────────────────────┐
│              Java Servlet Controller                   │
│  (AddStudentServlet, StudentListServlet, Edit, etc.)   │
└──────────────────────────┬─────────────────────────────┘
                           │ Invokes DAO Methods
                           ▼
┌────────────────────────────────────────────────────────┐
│             Data Access Object (DAO)                   │
│                 (StudentDAO.java)                      │
└──────────────────────────┬─────────────────────────────┘
                           │ JDBC (Connection, PreparedStatement)
                           ▼
┌────────────────────────────────────────────────────────┐
│                   MySQL Database                       │
│        (student_management -> students table)          │
└────────────────────────────────────────────────────────┘
```

### Layer Responsibilities:
1. **Frontend (`WebContent/`)**: Displays UI, captures user input, provides instant client-side validation, and triggers delete confirmation popups.
2. **Servlet Controller (`com.studentmanagement.servlet`)**: Receives HTTP requests, extracts parameters, coordinates with DAO, sets request attributes, and forwards/redirects responses.
3. **DAO Layer (`com.studentmanagement.dao`)**: Contains all SQL statements and JDBC code (`PreparedStatement`, `ResultSet`), encapsulating data access.
4. **Model Layer (`com.studentmanagement.model`)**: Simple POJO (`Student.java`) representing the student entity with private fields and getters/setters.
5. **Utility Layer (`com.studentmanagement.util`)**: Centralized MySQL connection manager (`DatabaseConnection.java`).

---

## 5. Project Folder Structure

```text
student_management_system/
│
├── database.sql                           # MySQL database and table setup script
├── README.md                              # Comprehensive guide & interview prep
│
├── src/
│   └── com/
│       └── studentmanagement/
│           │
│           ├── model/
│           │   └── Student.java            # Student Entity POJO (Encapsulation)
│           │
│           ├── util/
│           │   └── DatabaseConnection.java # Centralized JDBC Connection Manager
│           │
│           ├── dao/
│           │   └── StudentDAO.java         # CRUD & Search database operations
│           │
│           └── servlet/
│               ├── DashboardServlet.java   # Controller for Admin Dashboard
│               ├── AddStudentServlet.java  # Controller for adding new student
│               ├── StudentListServlet.java # Controller for listing all students
│               ├── SearchStudentServlet.java# Controller for searching students
│               ├── EditStudentServlet.java # Controller for loading edit form
│               ├── UpdateStudentServlet.java# Controller for saving edits
│               └── DeleteStudentServlet.java# Controller for deleting student
│
└── WebContent/
    │
    ├── index.jsp                           # Dashboard view (Metrics & recent students)
    ├── add-student.html                    # Add Student registration form
    ├── students.jsp                        # Student table directory & search view
    ├── edit-student.jsp                    # Edit student pre-filled form
    │
    ├── css/
    │   └── style.css                       # Modern responsive CSS styling
    │
    ├── js/
    │   └── script.js                       # Vanilla JS validation & modal handling
    │
    └── WEB-INF/
        ├── web.xml                         # Servlet deployment descriptor
        └── lib/                            # External JAR folder (No Maven)
            └── README.txt                  # Place mysql-connector-j.jar here
```

---

## 6. Database Setup (MySQL)

### Step 1: Open MySQL Workbench or MySQL Command Line Client
Log in with your MySQL root credentials:
```bash
mysql -u root -p
```

### Step 2: Execute `database.sql`
Run the following script to create the database, table, and sample data:

```sql
-- 1. Create Database
CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

-- 2. Create Table
DROP TABLE IF EXISTS students;
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    department VARCHAR(50) NOT NULL,
    year INT NOT NULL CHECK (year BETWEEN 1 AND 4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Insert Sample Records
INSERT INTO students (student_name, email, phone, department, year) VALUES
('Aarav Sharma', 'aarav.sharma@example.com', '9876543210', 'Computer Science', 3),
('Diya Patel', 'diya.patel@example.com', '9812345678', 'Information Technology', 2),
('Rohan Verma', 'rohan.verma@example.com', '9765432109', 'Electronics & Comm', 4),
('Sneha Rao', 'sneha.rao@example.com', '9654321098', 'Mechanical Engineering', 1),
('Kavya Nair', 'kavya.nair@example.com', '9543210987', 'Computer Science', 2),
('Vikram Singh', 'vikram.singh@example.com', '9432109876', 'Civil Engineering', 3);

-- 4. Verify
SELECT * FROM students;
```

---

## 7. External JAR Setup (No Maven)

Since **Maven is not used**, external JAR dependencies are managed manually.

### 1. Download MySQL Connector/J (JDBC Driver)
- **Official Download**: [MySQL Community Downloads](https://dev.mysql.com/downloads/connector/j/) or [Maven Central Direct Download](https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar)
- Look for: `mysql-connector-j-8.3.0.jar` (or `mysql-connector-java-8.0.x.jar`).

### 2. Place JAR in the Project
Copy the downloaded `.jar` file into the project directory:
```text
student_management_system/WebContent/WEB-INF/lib/mysql-connector-j-8.3.0.jar
```

### 3. Add to IDE Build Path (Eclipse / IntelliJ)
- **Eclipse / Spring Tool Suite (STS)**:
  1. Right-click the project folder -> **Properties**.
  2. Navigate to **Java Build Path** -> **Libraries** tab.
  3. Click **Add JARs...** -> Expand `WebContent/WEB-INF/lib/` -> Select `mysql-connector-j-8.3.0.jar` -> Click **Apply and Close**.
- **IntelliJ IDEA**:
  1. Go to **File** -> **Project Structure** (`Ctrl+Alt+Shift+S`) -> **Modules** -> **Dependencies**.
  2. Click `+` -> **JARs or Directories** -> Select `WebContent/WEB-INF/lib/mysql-connector-j-8.3.0.jar`.

---

## 8. Apache Tomcat Server Setup

### What is Apache Tomcat?
Apache Tomcat is an open-source HTTP web server and **Servlet Container** developed by the Apache Software Foundation. It executes Java Servlets and renders JavaServer Pages (JSP).

### Setting up Tomcat in Eclipse:
1. Download **Apache Tomcat 9.0.x** (Zip / Installer) from [tomcat.apache.org](https://tomcat.apache.org/download-90.cgi) and extract it (e.g., `C:\apache-tomcat-9.0.86`).
2. Open Eclipse -> Window -> **Show View** -> **Servers**.
3. Click "No servers are available. Click this link to create a new server...".
4. Select **Apache** -> **Tomcat v9.0 Server** -> Next.
5. Browse to the extracted Tomcat installation directory (`C:\apache-tomcat-9.0.86`) -> Finish.
6. Right-click the configured Tomcat server in the Servers view -> **Add and Remove...** -> Add `student_management_system` -> Finish.

---

## 9. How to Run the Project

### Step 1: Configure Database Credentials
Open [`DatabaseConnection.java`](file:///c:/Users/Latha%20Neeruganti/OneDrive/Desktop/student_management_system/src/com/studentmanagement/util/DatabaseConnection.java) and verify your MySQL username and password:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
private static final String DB_USERNAME = "root";       // Change if needed
private static final String DB_PASSWORD = "root";       // Change to your MySQL password
```

### Step 2: Start Tomcat & Launch
1. In Eclipse / IntelliJ, right-click the project -> **Run As** -> **Run on Server**.
2. Select your Apache Tomcat server and click **Finish**.
3. Open your web browser and visit:
   ```text
   http://localhost:8080/student_management_system/
   ```
   *(or `http://localhost:8080/student_management_system/index.jsp` / `http://localhost:8080/student_management_system/students`)*

---

## 10. End-to-End CRUD Flow

### 1. Add Student Flow:
1. User clicks **"+ Add Student"** in navigation and loads `add-student.html`.
2. User fills out Name, Email, Phone, Department, and Year.
3. JavaScript validates inputs in real time.
4. On submit, an HTTP `POST` request is sent to `/addStudent` (`AddStudentServlet`).
5. `AddStudentServlet` reads parameters, constructs a `Student` object, and calls `StudentDAO.addStudent(student)`.
6. `StudentDAO` executes an `INSERT` statement via `PreparedStatement`.
7. Servlet issues a redirect to `students?message=added`.
8. `students.jsp` displays a success alert banner and lists the new student.

### 2. View Students Flow:
1. User navigates to `/students`.
2. `StudentListServlet` handles the `GET` request and calls `StudentDAO.getAllStudents()`.
3. `StudentDAO` executes `SELECT * FROM students` and maps each row from `ResultSet` into `Student` objects.
4. Servlet attaches `studentList` to request scope (`request.setAttribute`) and forwards to `students.jsp`.
5. `students.jsp` renders the HTML table.

### 3. Search Student Flow:
1. User types an ID or Name into the search bar and clicks **Search**.
2. HTTP `GET` request is sent to `/searchStudent?query=Aarav`.
3. `SearchStudentServlet` calls `StudentDAO.searchStudents(query)`.
4. `StudentDAO` executes `SELECT * FROM students WHERE student_id = ? OR student_name LIKE ?`.
5. Results are forwarded to `students.jsp` showing matching rows and a "Reset" button.

### 4. Update Student Flow:
1. User clicks **"Edit"** next to a student row (`/editStudent?id=2`).
2. `EditStudentServlet` calls `StudentDAO.getStudentById(2)`.
3. Existing student data is placed in request scope and forwarded to `edit-student.jsp`.
4. The form displays pre-filled values. The user modifies the desired fields and submits (`POST /updateStudent`).
5. `UpdateStudentServlet` calls `StudentDAO.updateStudent(student)`.
6. `StudentDAO` executes `UPDATE students SET ... WHERE student_id = ?`.
7. User is redirected to `students?message=updated`.

### 5. Delete Student Flow:
1. User clicks **"Delete"** next to a student record.
2. JavaScript intercepts the action and displays a delete confirmation modal.
3. Upon confirmation, browser calls `/deleteStudent?id=3`.
4. `DeleteStudentServlet` calls `StudentDAO.deleteStudent(3)`.
5. `StudentDAO` executes `DELETE FROM students WHERE student_id = ?`.
6. User is redirected to `students?message=deleted`.

---

## 11. Troubleshooting & Common Errors

| Error | Root Cause | Solution |
| :--- | :--- | :--- |
| **`ClassNotFoundException: com.mysql.cj.jdbc.Driver`** | MySQL Connector JAR is missing from the classpath. | Ensure `mysql-connector-j.jar` is placed in `WebContent/WEB-INF/lib/` and added to Java Build Path. |
| **`CommunicationsException: Communications link failure`** | MySQL Server is stopped or port 3306 is blocked. | Start MySQL service from Windows Services (`services.msc`) or MySQL Notifier. |
| **`Access denied for user 'root'@'localhost'`** | Incorrect MySQL password. | Update `DB_PASSWORD` in [`DatabaseConnection.java`](file:///c:/Users/Latha%20Neeruganti/OneDrive/Desktop/student_management_system/src/com/studentmanagement/util/DatabaseConnection.java). |
| **`HTTP Status 404 - Not Found`** | Wrong URL path or servlet mapping issue. | Verify context path (`/student_management_system/`) and `@WebServlet` URL annotations. |
| **`Port 8080 already in use`** | Another application (or previous Tomcat instance) is using port 8080. | Change Tomcat port to 8081 in `server.xml` or terminate the conflicting process in Task Manager. |

---

## 12. Interview Preparation Guide

### A. Java Core Concepts
- **Encapsulation**: Used in `Student.java` where instance variables (`studentId`, `studentName`, etc.) are `private` and accessed exclusively through `public` getters and setters.
- **Classes & Objects**: `Student` represents the blueprint (class), while rows fetched from the database are instantiated as individual `Student` objects.
- **Exception Handling**: `try-catch-finally` blocks and `try-with-resources` manage `SQLException` and `ClassNotFoundException` gracefully without crashing the server.
- **Collections Framework**: `java.util.List` and `java.util.ArrayList` are used to hold and iterate over dynamic lists of student records.

### B. JDBC (Java Database Connectivity)
- **What is JDBC?** A standard Java API that enables Java programs to interact with relational databases.
- **Key JDBC Interfaces**:
  - `Connection`: Manages the communication session with the database.
  - `PreparedStatement`: Pre-compiled SQL statements that improve performance and prevent SQL Injection attacks.
  - `ResultSet`: A table of data representing a database query result set, navigated with `resultSet.next()`.
- **`executeQuery()` vs `executeUpdate()`**:
  - `executeQuery()`: Used for `SELECT` queries; returns a `ResultSet`.
  - `executeUpdate()`: Used for `INSERT`, `UPDATE`, `DELETE`, or DDL statements; returns an `int` representing affected rows.

### C. Java Servlets
- **What is a Servlet?** A Java class running on a server that handles incoming HTTP client requests and generates dynamic responses.
- **Servlet Lifecycle**:
  1. `init()`: Called once when the servlet is first loaded into memory.
  2. `service()` / `doGet()` / `doPost()`: Called on every incoming HTTP request.
  3. `destroy()`: Called when the server shuts down or the servlet is unloaded.
- **`HttpServletRequest`**: Encapsulates request data (form inputs, query parameters, headers, cookies).
- **`HttpServletResponse`**: Encapsulates response data (status codes, redirects, content types).
- **`@WebServlet`**: Annotation introduced in Servlet 3.0 to declare servlet URL routes without needing verbose XML definitions.

### D. SQL & MySQL
- **Primary Key**: `student_id` uniquely identifies each record and prevents duplicates (`AUTO_INCREMENT`).
- **Data Constraints**: `NOT NULL`, `UNIQUE` on email, and `CHECK (year BETWEEN 1 AND 4)`.
- **Wildcard Search**: `LIKE '%query%'` allows substring searching across student names.

### E. Vanilla JavaScript & DOM
- **`document.getElementById()` / `querySelector()`**: Used to select DOM elements for validation and modal triggers.
- **`addEventListener()`**: Listens to `'submit'`, `'input'`, and `'click'` events without inline HTML handlers.
- **Client-Side Form Validation**: Prevents invalid form submissions before sending network requests, reducing server overhead.

---

## 13. 1–2 Minute Project Pitch

> *"I developed a **Student Management System** using **Java Servlets, JDBC, MySQL, HTML, CSS, and Vanilla JavaScript** running on **Apache Tomcat**.*
>
> *The application follows the **MVC and DAO architectural patterns**. The frontend provides a responsive interface with forms, data tables, live search, and deletion confirmation modals. When a user interacts with the UI, HTTP requests are processed by **Java Servlets**, which act as controllers.*
>
> *The Servlets delegate database operations to a dedicated **StudentDAO** class that uses **JDBC PreparedStatements** to perform CRUD operations on a **MySQL** database securely, preventing SQL injection.*
>
> *To keep the project clean, lightweight, and easy to understand from first principles, I deliberately avoided heavy frameworks like Spring Boot or Hibernate and managed dependencies manually without Maven. This project reinforced my understanding of Java OOP, the Servlet request-response lifecycle, JDBC resource management, and relational database design."*

---

## 14. 25+ Interview Questions & Answers

#### Q1: What is the architecture of your application?
**Ans:** The application follows a 4-tier MVC/DAO pattern:
1. **View**: HTML5/JSP/CSS/JS in the browser.
2. **Controller**: Java Servlets (`AddStudentServlet`, `StudentListServlet`, etc.).
3. **Data Access Layer (DAO)**: `StudentDAO` executing JDBC queries.
4. **Model & Database**: `Student.java` POJO and MySQL database table `students`.

---

#### Q2: Why did you use `PreparedStatement` instead of `Statement`?
**Ans:**
1. **Prevents SQL Injection**: User input is treated strictly as parameter data rather than executable SQL code.
2. **Performance**: Prepared statements are pre-compiled by the database engine, making repeated execution faster.
3. **Type Safety**: Automatically handles data type conversions and escaping of special characters (quotes, slashes).

---

#### Q3: How do you prevent SQL Injection in your search feature?
**Ans:** In `StudentDAO.searchStudents()`, we use placeholders (`?`) for both the student ID and the name wildcard string (`preparedStatement.setString(2, "%" + query + "%")`). No user input is directly concatenated into SQL strings.

---

#### Q4: What is the difference between `doGet()` and `doPost()` in Servlets?
**Ans:**
- `doGet()`: Handles HTTP `GET` requests where parameters are appended to the URL (used for fetching data, e.g., viewing or searching students).
- `doPost()`: Handles HTTP `POST` requests where data is transmitted in the request body (used for sensitive or state-modifying operations like adding or updating student records).

---

#### Q5: What is the difference between `RequestDispatcher.forward()` and `HttpServletResponse.sendRedirect()`?
**Ans:**
- **`forward()`**: Server-side forwarding. The client's browser URL does not change, request attributes are preserved, and only one HTTP round-trip occurs.
- **`sendRedirect()`**: Client-side redirection. The server sends an HTTP 302 response instructing the browser to issue a new `GET` request to the target URL; request attributes from the previous request are lost.

---

#### Q6: Why did you use the DAO (Data Access Object) design pattern?
**Ans:** To achieve **Separation of Concerns**. The DAO pattern isolates database interaction code (SQL, JDBC) from the controller (Servlets) and business logic. If the database schema or provider changes, only the DAO class needs updating.

---

#### Q7: Where are database credentials stored and why?
**Ans:** In [`DatabaseConnection.java`](file:///c:/Users/Latha%20Neeruganti/OneDrive/Desktop/student_management_system/src/com/studentmanagement/util/DatabaseConnection.java). Centralizing credentials prevents duplication, simplifies configuration changes, and avoids exposing database passwords to client-side JavaScript.

---

#### Q8: How are database resources closed to prevent memory leaks?
**Ans:** In `StudentDAO.java`, database resources (`Connection`, `PreparedStatement`, `ResultSet`) are closed inside `finally` blocks using `DatabaseConnection.closeResources()`, ensuring they are released even if a `SQLException` occurs.

---

#### Q9: What is the purpose of `@WebServlet` annotation?
**Ans:** Introduced in Java EE 6 (Servlet 3.0), `@WebServlet` allows declaring a servlet and its URL mapping directly in Java code (e.g., `@WebServlet("/addStudent")`), eliminating the need to write verbose `<servlet>` and `<servlet-mapping>` tags in `web.xml`.

---

#### Q10: How does client-side validation benefit the application?
**Ans:** It validates required fields, email format (regex), phone number digits, and academic year ranges in the browser before the form is submitted. This provides immediate user feedback and reduces unnecessary network requests to the server.

---

#### Q11: What happens if JavaScript is disabled in the client's browser?
**Ans:** The servlets (`AddStudentServlet` and `UpdateStudentServlet`) perform **server-side validation** as a second line of defense. If any parameter is empty or invalid, the servlet rejects the request and redirects with an error code.

---

#### Q12: What is the role of `web.xml` in this project?
**Ans:** `web.xml` is the Web Application Deployment Descriptor. It defines the welcome files (`index.jsp`), application metadata, and acts as a standard configuration reference for the Servlet container.

---

#### Q13: What is the difference between `executeQuery()` and `executeUpdate()`?
**Ans:**
- `executeQuery()`: Executes `SELECT` statements and returns a `ResultSet`.
- `executeUpdate()`: Executes `INSERT`, `UPDATE`, or `DELETE` statements and returns an integer count of modified rows.

---

#### Q14: How does the Delete feature work safely?
**Ans:** When the user clicks "Delete", JavaScript intercepts the event and displays an interactive confirmation modal ("Are you sure?"). The request to `DeleteStudentServlet` is only dispatched if the user explicitly confirms the action.

---

#### Q15: What is `ResultSet` in JDBC?
**Ans:** `ResultSet` is an object that maintains a cursor pointing to the current row of tabular data returned by a `SELECT` query. The cursor is moved row-by-row using `resultSet.next()`.

---

#### Q16: How does `Student.java` demonstrate Encapsulation?
**Ans:** All student fields (`studentId`, `studentName`, `email`, `phone`, `department`, `year`) are declared with `private` access modifiers and exposed only via public getter and setter methods.

---

#### Q17: Why is `studentId` set to `AUTO_INCREMENT` in MySQL?
**Ans:** `AUTO_INCREMENT` automatically generates a unique, sequential integer primary key whenever a new student record is inserted, eliminating manual ID tracking and collisions.

---

#### Q18: What is a Servlet Container?
**Ans:** A Servlet Container (such as Apache Tomcat) is the component of a web server that manages the lifecycle of servlets, maps URLs to servlet classes, handles multithreading, and manages network communication.

---

#### Q19: Why did you not use Maven or Spring Boot?
**Ans:** To master and demonstrate the foundational mechanics of web development: HTTP requests/responses, Servlet lifecycles, JDBC resource management, and raw SQL queries without framework abstraction.

---

#### Q20: How do you handle character encoding for international names?
**Ans:** By calling `request.setCharacterEncoding("UTF-8");` in servlets before reading form parameters and specifying `UTF-8` page encoding in JSP headers (`contentType="text/html; charset=UTF-8"`).

---

#### Q21: What is the difference between a Servlet and a JSP?
**Ans:**
- **Servlet**: A Java class that handles business logic and request dispatching (best for Controller).
- **JSP**: An HTML-centric template that allows embedding Java code for rendering dynamic views (best for View).

---

#### Q22: What is the purpose of `Class.forName("com.mysql.cj.jdbc.Driver")`?
**Ans:** It explicitly loads and registers the MySQL JDBC driver class with Java's `DriverManager` when the class is loaded into memory.

---

#### Q23: How do you pass data from a Servlet to a JSP page?
**Ans:** By setting attributes in the request scope using `request.setAttribute("studentList", list)` in the Servlet, then forwarding to the JSP where data is retrieved using `request.getAttribute("studentList")`.

---

#### Q24: What is the purpose of `session` vs `request` scope?
**Ans:**
- **Request scope**: Exists only for the duration of a single HTTP request-response cycle.
- **Session scope**: Persists across multiple requests from the same client (used for user authentication/login state).

---

#### Q25: How does the live table filter work in JavaScript?
**Ans:** An `input` event listener on the search input box captures the user's keystrokes, iterates over the `<tr>` rows of `#studentsTable`, and toggles `row.style.display = ""` or `"none"` depending on whether `row.textContent` contains the search string.
