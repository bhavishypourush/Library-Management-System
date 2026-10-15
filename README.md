# Online Library Management System

A desktop Library Management System built with Java, Java Swing, JDBC, Maven, and MySQL. It has separate portals for librarians and members.

## Features

### Librarian portal
- Add, edit, delete, and view books.
- Manage library member accounts.
- View borrowing and return transactions.
- Send notifications to members.
- Generate an inventory report.

### Member portal
- Search books by title, author, or genre.
- Borrow and return books.
- View borrowing history.
- Update name and email.
- View notifications.

## Requirements

- Java JDK 17 or compatible.
- MySQL Server installed and running on the same computer.
- IntelliJ IDEA (recommended) or another Java IDE.
- Internet access on the first Maven import so Maven can download dependencies.

**MySQL Workbench is optional.** The application now attempts to create the database and tables automatically from `schema.sql` the first time it connects. MySQL Server itself must still be installed and running.

## Quick setup

1. Install and start MySQL Server.
2. Extract the ZIP.
3. Open the `LibraryManagementSystem` folder containing `pom.xml` in IntelliJ IDEA.
4. Wait for Maven dependencies to finish importing.
5. Run `src/main/java/com/library/Main.java`.
6. When prompted, enter the password for the local MySQL `root` account.

The password is requested in a dialog if `LIBRARY_DB_PASSWORD` is not already set. It is not written into the source code or saved by this application. If you already set `LIBRARY_DB_PASSWORD`, the application uses that value instead and does not prompt.

On the first successful connection, the application reads `src/main/resources/schema.sql` and creates the `library_db` database, required tables, demo users, and sample books when they are missing.

## Demo login

After the schema has initialized, try:

- **Librarian:** username `admin`, password `admin123`
- **Member:** username `member`, password `member123`

These are demo credentials for the local educational project. Change/remove demo accounts before using real information.

## Project structure

```text
LibraryManagementSystem/
├── src/main/java/com/library/
│   ├── dao/
│   ├── model/
│   ├── service/
│   ├── AppTheme.java
│   ├── DB.java
│   ├── LoginFrame.java
│   ├── LibrarianFrame.java
│   ├── MemberFrame.java
│   └── Main.java
├── src/main/resources/schema.sql
├── pom.xml
└── README.md
```

## Troubleshooting

- **Connection refused / communications link failure:** MySQL Server is not running, is not listening on port `3306`, or uses a different port.
- **Access denied for user `root`:** The password entered is incorrect, or the local MySQL account does not permit this connection. Verify the credentials in MySQL Workbench.
- **Unknown database or missing tables:** The schema normally initializes automatically after a successful server connection. Check that `src/main/resources/schema.sql` is included in the project resources.
- **Maven dependency errors:** Check your internet connection and reimport the Maven project.
- **Password dialog does not appear:** If `LIBRARY_DB_PASSWORD` is set in the environment, the app uses it. Correct or remove that variable if it contains an old value, then restart IntelliJ.

## Security

This is an educational project. Do not use the demo credentials for real library data. Do not commit actual database passwords or other secrets to a public repository. The database is local to each computer; the application does not copy one computer's database to another.

## Academic project

Developed to demonstrate Java, object-oriented programming, Swing GUI development, JDBC, SQL database operations, and basic concurrency concepts.
