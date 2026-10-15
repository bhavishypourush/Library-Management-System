# Library Management System

A desktop-based Library Management System developed using Java Swing, JDBC, and MySQL. It provides separate interfaces for librarians and library members to manage books, borrowing, returns, and library transactions.

## 1. Features

- Role-based login for Librarians and Members
- Book search and availability tracking
- Add, update, and delete book records
- Book borrowing and returning
- Borrowing history and transaction records
- Member management
- Notifications and inventory reports
- Multithreading demonstration

## 2. Technology Stack

- **Programming Language:** Java
- **User Interface:** Java Swing
- **Database:** MySQL
- **Database Connectivity:** JDBC
- **Build Tool:** Maven
- **IDE:** IntelliJ IDEA

## 3. Requirements

Before running the application, install:

- Java JDK compatible with the project
- MySQL Server
- MySQL Workbench (recommended for database setup)
- IntelliJ IDEA or another Java IDE
- Maven, if it is not managed by your IDE

## 4. Project Structure

```text
Library-Management-System/
├── src/
│   └── main/
│       ├── java/com/library/
│       │   ├── dao/
│       │   ├── model/
│       │   ├── service/
│       │   ├── DB.java
│       │   ├── Main.java
│       │   ├── LoginFrame.java
│       │   ├── LibrarianFrame.java
│       │   └── MemberFrame.java
│       └── resources/
│           └── schema.sql
├── .gitignore
├── pom.xml
└── README.md
```

## 5. Setup Instructions

### Step 1: Clone the repository

```bash
git clone https://github.com/bhavishypourush/Library-Management-System.git
```

Open the cloned folder in IntelliJ IDEA.

### Step 2: Configure MySQL

1. Start your MySQL server.
2. Open `src/main/resources/schema.sql` in MySQL Workbench.
3. Execute the script to create the required database and tables.

### Step 3: Configure the database connection

Open `DB.java` and configure the database URL, username, and password for your local MySQL installation.

Do not commit real passwords or other secrets to GitHub.

### Step 4: Load dependencies

Open the project in IntelliJ IDEA and allow Maven to load the dependencies declared in `pom.xml`.

### Step 5: Run the application

Locate `Main.java` in the `com.library` package and run it after the database connection has been configured.

## 6. Troubleshooting

- **Database connection error:** Check that MySQL is running and the connection details are correct.
- **Missing dependencies:** Reload the Maven project in IntelliJ IDEA.
- **Missing tables:** Execute `schema.sql` and verify that the database was created successfully.

## 7. Author

GitHub: [@bhavishypourush](https://github.com/bhavishypourush)
