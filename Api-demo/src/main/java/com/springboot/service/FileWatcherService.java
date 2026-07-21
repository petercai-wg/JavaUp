package com.springboot.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;

@Service
public class FileWatcherService {

    @Value("${watch.file}")
    private String fileName;

    private volatile boolean running = true;

    private Thread watcherThread;

    @PostConstruct
    public void start() {

        watcherThread = new Thread(this::watchFile);
        watcherThread.setName("FileWatcher");
        watcherThread.start();
    }

    @PreDestroy
    public void stop() {

        running = false;

        if (watcherThread != null) {
            watcherThread.interrupt();
        }
    }

    private void watchFile() {

        try {

            Path file = Paths.get(fileName);
            Path directory = file.getParent();

            WatchService watchService =
                    FileSystems.getDefault().newWatchService();

            directory.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE
            );

            long lastPosition = 0;

            if (Files.exists(file)) {
                lastPosition = Files.size(file);
            }

            System.out.println("Watching " + file);

            while (running) {

                WatchKey key;

                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    break;
                }

                for (WatchEvent<?> event : key.pollEvents()) {

                    Path changed = (Path) event.context();

                    if (!changed.equals(file.getFileName()))
                        continue;

                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_DELETE) {

                        System.out.println("File deleted.");
                        lastPosition = 0;
                        continue;
                    }

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {

                        System.out.println("File created.");
                        lastPosition = 0;
                    }

                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {

                        lastPosition = printNewLines(file, lastPosition);
                    }
                }

                key.reset();
            }

            watchService.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long printNewLines(Path file, long position) throws IOException {

        try (RandomAccessFile raf =
                     new RandomAccessFile(file.toFile(), "r")) {

            raf.seek(position);

            String line;

            while ((line = raf.readLine()) != null) {
                System.out.println("Received: " + line);
            }

            return raf.getFilePointer();
        }
    }

}