
        package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.IBookDAO;
import com.library.model.Book;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Service layer for library book operations.
 *
 * Keeps application-level book logic separate
 * from the user interface and database implementation.
 */
public class BookService {

    // Depend on the interface rather than a concrete implementation.
    private final IBookDAO bookDAO;

    /**
     * Default constructor uses the MySQL-backed DAO.
     */
    public BookService() {
        this(new BookDAO());
    }

    /**
     * Constructor injection makes the service easier to test.
     *
     * @param bookDAO implementation used to retrieve books
     */
    public BookService(IBookDAO bookDAO) {
        if (bookDAO == null) {
            throw new IllegalArgumentException(
                    "BookDAO cannot be null."
            );
        }

        this.bookDAO = bookDAO;
    }

    /**
     * Returns all books retrieved by the DAO.
     *
     * A separate list is returned so callers cannot
     * modify the DAO's list structure.
     *
     * @return a list containing all books
     * @throws SQLException if a database error occurs
     */
    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = bookDAO.getAllBooks();

        // Handle an unexpected null result safely.
        if (books == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(books);
    }

    /**
     * Searches books by title, author, or genre.
     *
     * Search is case-insensitive. A null, empty,
     * or whitespace-only keyword returns all books.
     *
     * @param keyword text to search for
     * @return a list of matching books
     * @throws SQLException if a database error occurs
     */
    public List<Book> searchBooks(String keyword)
            throws SQLException {

        String searchText = keyword == null
                ? ""
                : keyword.trim().toLowerCase(Locale.ROOT);

        List<Book> matchingBooks = new ArrayList<>();

        for (Book book : getAllBooks()) {

            boolean matchesTitle =
                    book.getTitle() != null
                            && book.getTitle()
                            .toLowerCase(Locale.ROOT)
                            .contains(searchText);

            boolean matchesAuthor =
                    book.getAuthor() != null
                            && book.getAuthor()
                            .toLowerCase(Locale.ROOT)
                            .contains(searchText);

            boolean matchesGenre =
                    book.getGenre() != null
                            && book.getGenre()
                            .toLowerCase(Locale.ROOT)
                            .contains(searchText);

            if (matchesTitle || matchesAuthor || matchesGenre) {
                matchingBooks.add(book);
            }
        }

        return matchingBooks;
    }
}
