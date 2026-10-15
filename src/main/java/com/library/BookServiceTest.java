
        package com.library;

import com.library.model.Book;
import com.library.service.BookService;

import java.sql.SQLException;
import java.util.List;

/**
 * Tests book retrieval and search functionality.
 */
public class BookServiceTest {

    public static void main(String[] args) {

        BookService bookService = new BookService();

        try {
            // Test 1: Retrieve all books.
            List<Book> allBooks = bookService.getAllBooks();

            System.out.println("=== ALL BOOKS ===");
            System.out.println("Total: " + allBooks.size());

            for (Book book : allBooks) {
                System.out.println(book);
            }

            // Test 2: Search books by a keyword.
            List<Book> results =
                    bookService.searchBooks("programming");

            System.out.println("\n=== SEARCH RESULTS ===");
            System.out.println("Matches: " + results.size());

            for (Book book : results) {
                System.out.println(book);
            }

            System.out.println("\nService test completed.");

        } catch (SQLException e) {
            System.err.println("Book service test failed.");
            System.err.println("Database error: " + e.getMessage());
        }
    }
}
