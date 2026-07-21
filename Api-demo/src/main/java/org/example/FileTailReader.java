package org.example;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;

public class FileTailReader {

    public static void main(String[] args) throws Exception {

        Path file = Paths.get("C:/temp/logs/my_dagster.log");
        Path directory = file.getParent();

        WatchService watchService = FileSystems.getDefault().newWatchService();
        directory.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        long lastPosition = 0;

        // If you only want new content after program starts
        try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "r")) {
            lastPosition = raf.length();
        }

        System.out.println("Monitoring: " + file);

        while (true) {

            WatchKey key = watchService.take();

            for (WatchEvent<?> event : key.pollEvents()) {

                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.OVERFLOW)
                    continue;

                Path changed = (Path) event.context();

                if (changed.equals(file.getFileName())) {

                    lastPosition = readNewLines(file, lastPosition);
                }
            }

            key.reset();
        }
    }

    private static long readNewLines(Path file, long position) throws IOException {

        try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "r")) {

            raf.seek(position);

            String line;
            while ((line = raf.readLine()) != null) {
                System.out.println("New Line: " + line);
            }

            return raf.getFilePointer();
        }
    }
}

