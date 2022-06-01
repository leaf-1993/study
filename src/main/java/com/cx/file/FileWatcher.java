package com.cx.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author chenxiang
 * @create 2022-01-04 10:24
 */
public class FileWatcher {
    private static final FileSystem FILE_SYSTEM = FileSystems.getDefault();
    public static void main(String[] args) {
        try {
            String p = "C:\\file";
            final Path path = Paths.get(p);
            WatchService watchService = FILE_SYSTEM.newWatchService();
            path.register(watchService,StandardWatchEventKinds.OVERFLOW, StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);

            ExecutorService callBackExecutor = Executors.newFixedThreadPool(1);

            while (true) {
                try {
                    final WatchKey watchKey = watchService.take();
                    final List<WatchEvent<?>> events = watchKey.pollEvents();
                    watchKey.reset();
                    if (callBackExecutor.isShutdown()) {
                        return;
                    }
                    if (events.isEmpty()) {
                        continue;
                    }
                    callBackExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            for (WatchEvent<?> event : events) {
                                WatchEvent.Kind<?> kind = event.kind();

                                // Since the OS's event cache may be overflow, a backstop is needed
                                if (StandardWatchEventKinds.OVERFLOW.equals(kind)) {
                                    eventOverflow(p);
                                } else {
                                    eventProcess(event.context());
                                }
                            }
                        }
                    });
                } catch (InterruptedException ignore) {
                    Thread.interrupted();
                } catch (Throwable ex) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void eventOverflow(String paths) {
        File dir = Paths.get(paths).toFile();
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            // Subdirectories do not participate in listening
            if (file.isDirectory()) {
                continue;
            }
            eventProcess(file.getName());
        }
    }

    private static void eventProcess(Object context) {
        System.out.println(context);
    }
}
