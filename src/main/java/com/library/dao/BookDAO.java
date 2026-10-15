
        package com.library.dao;

import com.library.DB;
import com.library.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides database operations for library books.
 *
 * Uses:
 * - IBookDAO for abstraction
 * - JDBC for database connectivity
 * - List<Book> and ArrayList for collections and generics
 * - Try-with-resources for automatic resource cleanup
 */
public class BookDAO implements IBookDAO {

    /**
     * Retrieves all books from the database,
     * ordered alphabetically by title.
     *
     * Database resources are closed automatically.
     * SQL exceptions are propagated to the caller.
     *
     * @return a list of books, or an empty list if none exist
     * @throws SQLException if a database operation fails
     */
    @Override
    public List<Book> getAllBooks() throws SQLException {

        List<Book> books = new ArrayList<>();

        String sql = """
                SELECT id, title, author, isbn, genre, available
                FROM books
                ORDER BY title
                """;

        try (
                Connection connection = DB.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("isbn"),
                        resultSet.getString("genre"),
                        resultSet.getBoolean("available")
                );

                books.add(book);
            }
        }

        return books;
    }
}
