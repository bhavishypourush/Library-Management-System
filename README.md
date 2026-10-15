# 📚 Library Management System

A desktop-based Library Management System built using **Java Swing, JDBC, and MySQL**. It provides separate interfaces for librarians and members to manage books, borrowing, returns, and library transactions.

## ✨ Features

- Role-based login for Librarians and Members
- Search books and check availability
- Add, update, and delete book records
- Borrow and return books
- View borrowing history and transactions
- Manage library members
- Notifications and inventory reports
- Multithreading demonstration

## 🛠️ Technologies Used

- **Language:** Java
- **GUI:** Java Swing
- **Database:** MySQL
- **Connectivity:** JDBC
- **Build Tool:** Maven
- **IDE:** IntelliJ IDEA

## 📋 Requirements

Install the following before running the project:

- Java JDK compatible with the project
- MySQL Server
- MySQL Workbench (recommended)
- IntelliJ IDEA or another Java IDE
- Maven support

## 📥 Download the Project

Clone the repository:

```bash
git clone https://github.com/bhavishypourush/Library-Management-System.git
```

Alternatively, click **Code → Download ZIP** on the GitHub repository page and extract the downloaded file.

Open the extracted project folder in IntelliJ IDEA.

## 🗄️ Database Setup

1. Start MySQL Server.
2. Open `src/main/resources/schema.sql` in MySQL Workbench.
3. Execute the SQL script to create the database and required tables.
4. Verify that the database and tables were created successfully.

## ⚙️ Configure the Database Connection

The connection settings are defined in `src/main/java/com/library/DB.java`.

Configure the database URL, username, and password for your local MySQL installation. Use your own credentials; the application does not share your local database with other users.

Do not put real passwords in source code or commit them to GitHub. Follow the database configuration in this README after setup.

## ▶️ Run the Application

1. Open the project in IntelliJ IDEA.
2. Allow Maven to download the dependencies specified in `pom.xml`.
3. Ensure MySQL is running and the database connection is configured.
4. Open `src/main/java/com/library/Main.java`.
5. Run `Main.java`.

The application should open its login interface if the setup is correct.

## 🧰 Troubleshooting

- **Database connection fails:** Check that MySQL is running and the connection URL and credentials are correct.
- **Tables are missing:** Execute `schema.sql` in MySQL Workbench.
- **Maven dependencies are missing:** Reload the Maven project in IntelliJ IDEA.
- **Application does not start:** Check the console for errors and verify that the configured Java version is compatible.

## 📂 Project Structure

```text
Library-Management-System/
├── src/
│   └── main/
│       ├── java/com/library/
│       │   ├── dao/
│       │   ├── model/
│       │   ├── service/
│       │   ├── DB.java
│       │   ├── LoginFrame.java
│       │   ├── LibrarianFrame.java
│       │   ├── MemberFrame.java
│       │   └── Main.java
│       └── resources/
│           └── schema.sql
├── .gitignore
├── pom.xml
└── README.md
```

## 👨‍💻 Author

GitHub: [@bhavishypourush](https://github.com/bhavishypourush)
