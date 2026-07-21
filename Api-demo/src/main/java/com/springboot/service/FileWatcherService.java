package com.springboot.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileWatcherService {

    @Value("${watch.folder}")
    private String folder;

    private WatchService watchService;
    private Thread watcherThread;
    private volatile boolean running = true;

    @PostConstruct
    public void start() throws IOException {

        Path path = Paths.get(folder);

        watchService = FileSystems.getDefault().newWatchService();

        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE
        );

        watcherThread = new Thread(this::watchFolder);
        watcherThread.setName("FolderWatcher");
        watcherThread.start();

        System.out.println("Watching folder: " + folder);
    }

    @PreDestroy
    public void stop() throws IOException {

        running = false;

        if (watcherThread != null) {
            watcherThread.interrupt();
        }

        if (watchService != null) {
            watchService.close();
        }
    }

    private void watchFolder() {

        while (running) {

            WatchKey key;

            try {
                key = watchService.take();
            } catch (Exception e) {
                break;
            }

            for (WatchEvent<?> event : key.pollEvents()) {

                if (event.kind() != StandardWatchEventKinds.ENTRY_CREATE)
                    continue;

                Path fileName = (Path) event.context();
                Path fullPath = Paths.get(folder).resolve(fileName);

                System.out.println("\nNew file detected: " + fullPath);

                processFile(fullPath);
            }

            key.reset();
        }
    }

    private void processFile(Path file) {

        try {
            waitUntilCopyFinished(file);
            List<String> lines = Files.readAllLines(file);

            System.out.println("------ File Content ------");

            for (String line : lines) {
                System.out.println(line);
            }
            // Remove line separators and concatenate
            String contentStr = String.join("", lines);

            System.out.println(contentStr);

//            String content = Files.lines(file).collect(Collectors.joining());

        } catch (Exception e) {
            System.err.println("Cannot read " + file + ": " + e.getMessage());
        }
    }

    private void waitUntilCopyFinished(Path file)
            throws IOException, InterruptedException {

        long previousSize = -1;

        while (true) {

            long currentSize = Files.size(file);

            if (currentSize == previousSize) {
                break;
            }

            previousSize = currentSize;
            Thread.sleep(500);
        }
    }
}