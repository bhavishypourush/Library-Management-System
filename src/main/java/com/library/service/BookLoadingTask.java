
        package com.library.service;

import com.library.model.Book;

import javax.swing.SwingWorker;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Loads library books in a background thread.
 *
 * SwingWorker runs database loading in the background
 * and handles the result on the Swing Event Dispatch Thread.
 */
public class BookLoadingTask extends SwingWorker<List<Book>, Void> {

    private final BookService bookService;

    /**
     * Creates a task for loading books.
     *
     * @param bookService service used to retrieve books
     */
    public BookLoadingTask(BookService bookService) {
        if (bookService == null) {
            throw new IllegalArgumentException(
                    "BookService cannot be null."
            );
        }

        this.bookService = bookService;
    }

    /**
     * Runs on a background thread.
     * Database loading does not block the Swing UI.
     *
     * @return list of books retrieved from the database
     * @throws Exception if book loading fails
     */
    @Override
    protected List<Book> doInBackground() throws Exception {
        return bookService.getAllBooks();
    }

    /**
     * Runs on the Swing Event Dispatch Thread after
     * the background task completes.
     */
    @Override
    protected void done() {
        try {
            // get() retrieves the result of doInBackground().
            List<Book> books = get();

            // This class does not own a UI component,
            // so the result is reported here for now.
            System.out.println(
                    "Book loading completed. Total books: "
                            + books.size()
            );

        } catch (InterruptedException e) {
            // Restore the interrupted status.
            Thread.currentThread().interrupt();

            System.err.println("Book loading was interrupted.");

        } catch (ExecutionException e) {
            // The actual error is wrapped in ExecutionException.
            Throwable cause = e.getCause();

            System.err.println("Unable to load books: "
                    + (cause != null
                    ? cause.getMessage()
                    : e.getMessage()));
        }
    }
}
