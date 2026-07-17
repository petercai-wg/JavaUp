package org.example;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class AsyncThread {

    public static int counter = 0;

    public static void main(String[] args) throws Exception{
        //threadNoSynCounter();
        //threadSynCounter();
        runCompleteFuture();

    }

    // Using CompletableFuture to run asynchronous tasks, it is not a true single threaded asynchronous model,
    // otherwise result will be 1000 if it is single thread eventloop model
    public static void runCompleteFuture() {
        // Create 1,000 asynchronous tasks to increment the counter
        CompletableFuture<?>[] futures = IntStream.range(0, 1000)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    counter = counter + 1;
                }))
                .toArray(CompletableFuture[]::new);

        // Wait for all tasks to complete
        CompletableFuture.allOf(futures).join();

        System.out.println("Expected: 1000");
        System.out.println("Actual: " + counter);
    }

    public static void threadNoSynCounter() throws InterruptedException {

        int numberOfThreads = 10;
        int incrementsPerThread = 1000;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter = counter + 1;
                }
            });
        }

        service.shutdown();
        // Wait for all threads to complete
        service.awaitTermination(1, TimeUnit.MINUTES);

        int expectedCount = numberOfThreads * incrementsPerThread;
        System.out.println("Expected count: " + expectedCount);
        // The actual count will likely be less than expected due to lost updates
        System.out.println("Actual count:   " + counter);
    }

    public static void threadSynCounter() throws InterruptedException {

        int numberOfThreads = 10;
        int incrementsPerThread = 1000;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    synchronized (AsyncThread.class) {
                        counter = counter + 1;
                    }
                    //counter = counter + 1;
                }
            });
        }

        service.shutdown();
        // Wait for all threads to complete
        service.awaitTermination(1, TimeUnit.MINUTES);

        int expectedCount = numberOfThreads * incrementsPerThread;
        System.out.println("Expected count: " + expectedCount);
        // The actual count will likely be less than expected due to lost updates
        System.out.println("Actual count:   " + counter);
    }
}
