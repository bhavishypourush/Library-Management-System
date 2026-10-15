
package com.library.service;

/**
 * Demonstrates synchronization when multiple threads
 * update a shared book-operation counter.
 *
 * This class is a synchronization demonstration.
 * It does not execute database operations itself.
 */
public class LibraryTaskManager {

    private int completedOperations = 0;

    /**
     * Records one completed operation.
     *
     * synchronized prevents multiple threads from
     * incrementing the counter simultaneously.
     */
    public synchronized void recordOperation() {
        completedOperations++;
    }

    /**
     * Returns the number of completed operations.
     *
     * Synchronization ensures that reads are coordinated
     * with updates made by other synchronized methods
     * on the same object.
     *
     * @return completed operation count
     */
    public synchronized int getCompletedOperations() {
        return completedOperations;
    }
}
