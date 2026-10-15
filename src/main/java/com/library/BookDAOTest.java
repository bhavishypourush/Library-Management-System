
        package com.library;

import com.library.dao.BookDAO;
import com.library.dao.IBookDAO;
import com.library.model.Book;

import java.sql.SQLException;
import java.util.List;

/**
 * Tests BookDAO database operations.
 *
 * Demonstrates:
 * 1. Interfaces
 * 2. Polymorphism
 * 3. Collections and Generics
 * 4. Exception Handling
 */
public class BookDAOTest {

    public static void main(String[] args) {

        // Interface reference pointing to BookDAO object.
        // This demonstrates polymorphism.
        IBookDAO bookDAO = new BookDAO();

        try {
            // Retrieve books from the MySQL database.
            List<Book> books = bookDAO.getAllBooks();

            System.out.println("================================");
            System.out.println(" LIBRARY BOOK DATABASE TEST");
            System.out.println("================================");

            System.out.println(
                    "Total books retrieved: " + books.size()
            );

            System.out.println("--------------------------------");

            // Display each book using a generic List<Book>.
            for (Book book : books) {
                System.out.println("Book: " + book);
                System.out.println(
                        "Book ID: " + book.getId()
                );
                System.out.println(
                        "ISBN: " + book.getIsbn()
                );
                System.out.println(
                        "Available: " + book.isAvailable()
                );
                System.out.println("--------------------------------");
            }

            System.out.println("Database test completed.");

        } catch (SQLException e) {

            // Handle database errors gracefully.
            System.err.println(
                    "Database error: Could not retrieve books."
            );

            System.err.println(
                    "Reason: " + e.getMessage()
            );
        }
    }
}
