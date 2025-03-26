package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class AsyncFuture {
    public static List<String> imgUrls = List.of(
            "https://images.unsplash.com/photo-1516117172878-fd2c41f4a759",
            "https://images.unsplash.com/photo-1532009324734-20a7a5813719",
            "https://images.unsplash.com/photo-1524429656589-6633a470097c"
    );

    public static void mainLoop(){
        for(int i=0; i< 5; i++){
            try {
                System.out.println("Main Thread Looping  " + i );
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public static void main(String[] args) {
        /*
         *   Runnable no return value, Callalbe return a result
         */

        long start = System.nanoTime(); // Start timing
        FutureDemo();
        long duration = System.nanoTime() - start; // Calculate duration
        System.out.println( "CompletableFuturDemo time of : " + (duration / 1_000_000.0) + " ms");

//        long start2 = System.nanoTime(); // Start timing
//        ThreadDemo();
//        long duration2 = System.nanoTime() - start2; // Calculate duration
//        System.out.println( "ThreadDemo time of : " + (duration2 / 1_000_000.0) + " ms");

    }
    public static void CompletableFuturDemo() {

        Collection<CompletableFuture<String>> futures = new LinkedList<CompletableFuture<String>>();

        for (String imageUrl : imgUrls) {
            Executor executor = Executors.newCachedThreadPool();
            CompletableFuture<String> f = CompletableFuture.supplyAsync(() -> {
                String ret = "";
                try{

                    ret =downloadImage(imageUrl);

                } catch (IOException e) {
                    System.err.println(Thread.currentThread().getName() + " Failed to download: " + imageUrl + " -> " + e.getMessage());
                }
                return ret;
            }, executor);

            futures.add(f);
        }

        mainLoop();

        for (CompletableFuture<String> f : futures) {
            try {
                System.out.println("Future return " + f.get() );
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("All tasks finished.");
    }
    public static void ThreadDemo() {

        ArrayList<Thread> tds = new ArrayList<Thread>();
        for (String imageUrl : imgUrls) {
            Thread t = new Thread(() -> {
                try{

                    downloadImage(imageUrl);

                } catch (IOException e) {
                    System.err.println(Thread.currentThread().getName() + " Failed to download: " + imageUrl + " -> " + e.getMessage());
                }
            });
            tds.add(t);
            t.start();

        }
        mainLoop();
        for(Thread t: tds){
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("All tasks finished.");
    }

    public static void FutureDemo() {
        ExecutorService service = Executors.newFixedThreadPool(5); // Pool with 3 threads

        Collection<Future<String>> futures = new LinkedList <Future<String>>();

        for (String imageUrl : imgUrls) {
            Future<String> f = service.submit(() -> {  // callable can return value
                String ret = "";
                try {

                    ret =  downloadImage(imageUrl);


                } catch (IOException e) {
                    System.err.println(Thread.currentThread().getName() + " Failed to download: " + imageUrl + " -> " + e.getMessage());
                }
                return ret;
            });
            futures.add(f);

        }

        mainLoop();

        for (Future<String> f : futures) {
            try {
                System.out.println("Future return " + f.get() );
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
////        service.shutdown(); // Prevent new tasks from being submitted
//        try {
//            if (!service.awaitTermination(10, TimeUnit.SECONDS)) { // Wait for tasks to complete
//                System.out.println("Some tasks didn't finish in time, forcing shutdown...");
//                service.shutdownNow(); // Forcefully shut down unfinished tasks
//            }
//        } catch (InterruptedException e) {
//            service.shutdownNow();
//        }
        service.shutdownNow();
        System.out.println("All tasks finished.");
    }

    public static String downloadImage(String imageUrl) throws IOException {
        long start = System.nanoTime(); // Start timing
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        String photoName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        String fileName = "c:\\temp\\images\\" + photoName + "_" +  System.currentTimeMillis() + ".jpg"; // Unique filename
        Path savePath = Path.of(fileName);

        try (InputStream inputStream = connection.getInputStream()) {
            Files.copy(inputStream, savePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println(Thread.currentThread().getName() + " Downloaded: " + fileName);
        } finally {
            connection.disconnect();
        }

        long duration = System.nanoTime() - start; // Calculate duration
        System.out.println( Thread.currentThread().getName()  + " Execution time of : " + (duration / 1_000_000.0) + " ms");

        return fileName;
    }
}
