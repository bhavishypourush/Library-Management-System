
        package com.library.dao;

import com.library.model.Book;

import java.sql.SQLException;
import java.util.List;

/**
 * Defines the operations available for book data.
 *
 * Implementing classes provide the actual data-access logic.
 */
public interface IBookDAO {

    /**
     * Retrieves all books from the library.
     *
     * @return a list of books
     * @throws SQLException if a database error occurs
     */
    List<Book> getAllBooks() throws SQLException;
}
