# Online Library Management System

A desktop-based Library Management System built with **Java, Java Swing, JDBC, MySQL, and Maven**. It provides separate portals for librarians and members, with database-backed book and borrowing operations.

## Features

### Librarian Portal
- Manage the book catalogue.
- View and manage member records.
- View borrowing and return transactions.
- Access notifications.
- View inventory reports.

### Member Portal
- Search and browse books.
- Borrow and return books.
- View borrowing history.
- View profile information.
- View notifications.

### Technical Features
- Java Swing desktop interface.
- MySQL connectivity through JDBC.
- Maven dependency management.
- Prepared statements and try-with-resources for database operations.
- Background task support with `SwingWorker` where implemented.
- Task-manager concurrency test.

## Technology Stack

- **Language:** Java
- **UI:** Java Swing
- **Database:** MySQL
- **Connectivity:** JDBC
- **Build tool:** Maven
- **Recommended IDE:** IntelliJ IDEA

## Project Structure

```text
LibraryManagementSystem/
├── src/
│   └── main/
│       ├── java/com/library/
│       │   ├── dao/
│       │   ├── model/
│       │   ├── service/
│       │   ├── AppTheme.java
│       │   ├── DB.java
│       │   ├── LoginFrame.java
│       │   ├── LibrarianFrame.java
│       │   ├── MemberFrame.java
│       │   └── Main.java
│       └── resources/
│           └── schema.sql
├── pom.xml
└── README.md
```

*This is a simplified overview; additional supporting and test classes may be present.*

## Prerequisites

1. A JDK compatible with the Java version configured in `pom.xml`.
2. MySQL Server and a MySQL client such as MySQL Workbench.
3. Maven, if building from the command line. IntelliJ IDEA can import Maven projects.

## Setup and Run

### 1. Download the project

Clone the repository:

```bash
git clone https://github.com/bhavishypourush/Library-Management-System.git
cd Library-Management-System
```

Or download the ZIP using GitHub's **Code → Download ZIP** option and extract it.

### 2. Set up the database

1. Start MySQL Server.
2. Open MySQL Workbench or another MySQL client.
3. Open and execute `src/main/resources/schema.sql`.
4. Confirm that the `library_db` database and the tables defined by the schema have been created.

If the SQL file does not create the database itself, run:

```sql
CREATE DATABASE library_db;
```

Then execute the schema file.

### 3. Configure database credentials

The application should read its database password from the `LIBRARY_DB_PASSWORD` environment variable. Check `src/main/java/com/library/DB.java` to confirm the database URL and username match your local MySQL setup.

**Never commit your personal database password to GitHub or place it in this README.**

#### Windows

Open Command Prompt and run:

```bat
setx LIBRARY_DB_PASSWORD "YOUR_MYSQL_PASSWORD"
```

Replace `YOUR_MYSQL_PASSWORD` with the password for the MySQL account configured in `DB.java`.

After using `setx`:
1. Close and reopen Command Prompt.
2. Restart IntelliJ IDEA so it can read the new environment variable.
3. Run the application again.

To check whether the variable is set, run:

```bat
echo %LIBRARY_DB_PASSWORD%
```

Do not share screenshots or terminal output that reveal your password.

#### macOS / Linux

For the current terminal session:

```bash
export LIBRARY_DB_PASSWORD='YOUR_MYSQL_PASSWORD'
```

Launch the application from that same terminal, or configure the variable in your IDE's run configuration.

### 4. Run the application

**Using IntelliJ IDEA**
1. Open the project folder containing `pom.xml`.
2. Allow IntelliJ to import Maven dependencies.
3. Ensure MySQL is running and the database credentials are configured.
4. Open `src/main/java/com/library/Main.java`.
5. Run `Main.main()`.

**Using Maven**

Compile from the project root:

```bash
mvn clean compile
```

To launch with Maven, the project needs a suitable run configuration or Maven plugin. Otherwise, run `Main.java` from IntelliJ IDEA.

## Testing

The repository includes test/helper classes for checking parts of the application, such as database access and task-manager concurrency. Run them according to their class setup in IntelliJ IDEA. Database-related tests require a running MySQL instance and valid local credentials.

## Troubleshooting

- **Database connection error:** Confirm MySQL Server is running, `library_db` exists, and the configured username and password are correct.
- **Environment variable not detected:** On Windows, open a new terminal and restart IntelliJ after using `setx`.
- **Tables are missing:** Execute `src/main/resources/schema.sql` in MySQL Workbench.
- **Maven dependencies fail:** Check your internet connection and refresh/reimport the Maven project.
- **Login fails:** Confirm the database contains the user records and roles expected by the application.

## Security Notes

- Never commit real database passwords, API keys, or other secrets.
- Each person running the project should configure their own local database credentials.
- If a real password was committed to a public repository, changing the source file alone is not enough; change that database password too.

## Author

Developed as a Java database application project.

## License

No license has been specified. Unless a license is added to this repository, reuse and redistribution are not automatically granted.
