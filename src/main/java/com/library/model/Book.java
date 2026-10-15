
package com.library.model;

/**
 * Represents a book in the library.
 *
 * Stores book details and controls access to its fields
 * through getters and setters.
 */
public class Book {

    // Private fields demonstrate encapsulation.
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private boolean available;

    /**
     * Creates a book object with the supplied details.
     */
    public Book(int id, String title, String author,
                String isbn, String genre, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.available = available;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isAvailable() {
        return available;
    }

    // Setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Returns a readable representation of the book.
     */
    @Override
    public String toString() {
        return title + " by " + author
                + " (" + genre + ")";
    }
}
