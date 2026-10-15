
package com.library.model;

/**
 * Represents a book borrowing or returning transaction.
 *
 * Stores transaction details for a book and its library member.
 */
public class LibraryTransaction {

    private int id;
    private int bookId;
    private int memberId;
    private String borrowDate;
    private String dueDate;
    private String returnDate;
    private String status;

    /**
     * Initializes transaction details.
     */
    public LibraryTransaction(int id, int bookId, int memberId,
                              String borrowDate, String dueDate,
                              String returnDate, String status) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters

    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }

    // Setters for transaction fields that may change.

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns a readable summary of the transaction.
     */
    @Override
    public String toString() {
        return "Transaction #" + id
                + " | Book ID: " + bookId
                + " | Member ID: " + memberId
                + " | Status: " + status;
    }
}
