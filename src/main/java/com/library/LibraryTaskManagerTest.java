
        package com.library;

import com.library.service.LibraryTaskManager;

/**
 * Tests multithreading and synchronization.
 */
public class LibraryTaskManagerTest {

    public static void main(String[] args)
            throws InterruptedException {

        LibraryTaskManager manager = new LibraryTaskManager();

        int numberOfThreads = 10;
        int operationsPerThread = 1000;

        Thread[] threads = new Thread[numberOfThreads];

        // Create multiple threads.
        for (int i = 0; i < numberOfThreads; i++) {

            threads[i] = new Thread(() -> {

                for (int j = 0; j < operationsPerThread; j++) {
                    manager.recordOperation();
                }

            });
        }

        // Start all threads.
        for (Thread thread : threads) {
            thread.start();
        }

        // Wait until every thread finishes.
        for (Thread thread : threads) {
            thread.join();
        }

        int expected = numberOfThreads * operationsPerThread;
        int actual = manager.getCompletedOperations();

        System.out.println("=== Synchronization Test ===");
        System.out.println("Number of threads: " + numberOfThreads);
        System.out.println("Operations per thread: " + operationsPerThread);
        System.out.println("Expected counter: " + expected);
        System.out.println("Actual counter: " + actual);

        if (expected == actual) {
            System.out.println("SUCCESS: Synchronization works correctly.");
        } else {
            System.out.println("FAILED: Counter values do not match.");
        }
    }
}
