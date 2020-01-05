import interfaces.Ranger;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class Search {

    public static void main(String[] args) {

//        if (args.length == 0) {
//            System.err.println("No directories specified, exit application");
//            System.exit(0);
//        }

        String[] paths = new String[3];
        paths[0] = "D:\\StepicWebServerStudy\\Search";
        paths[1] = "Searching";
        paths[2] = "";
//        paths[2] = ".\\Search";

        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        List<Path> pathsList = new ArrayList<>();
        BlockingQueue<Path> pathsQueue = new LinkedBlockingQueue<>();
        Arrays.stream(paths).forEach(p -> {
            Path path = FileSystems.getDefault().getPath(p);
            if (Files.exists(path, LinkOption.NOFOLLOW_LINKS) && Files.isDirectory(path)) {
                try {
                    Files.newDirectoryStream(path).forEach(f -> {
                        String type = fileNameMap.getContentTypeFor(f.toUri().toString());
                        if ((!Files.isDirectory(f)) && type != null && type.contains("text/plain")) {
                            pathsList.add(f);
                            pathsQueue.add(f);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        if (pathsList.size() == 0) {
            System.err.println("No text files found, exit application");
            System.exit(0);
        }

//        Map<String, Set<Path>> wordsMap = new HashMap<>();
//        Path lastPath = Paths.get("last");
//        pathsQueue.add(lastPath);
//
//        int cpus = Runtime.getRuntime().availableProcessors();
//        ExecutorService pool = Executors.newFixedThreadPool(cpus);
//        List<Future<Map<String, Set<Path>>>> futures = new ArrayList<>();
//
//        for (int i = 0; i < cpus; i++) {
//            futures.add(pool.submit(new WordsMapperTask(pathsQueue, lastPath)));
//        }
//
//        for (Future<Map<String, Set<Path>>> future : futures){
//            try {
//                future.get().forEach((key, value) -> {
//                    wordsMap.computeIfAbsent(key, k -> new HashSet<>());
//                    wordsMap.get(key).addAll(value);
//                });
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
//        pool.shutdown();
//        pathsQueue.clear();
//        wordsMap.forEach((key, value) -> System.out.println(key + " " + Arrays.toString(value.toArray())));
//        System.out.println("Files found for processing:");
//        pathsList.forEach(p -> System.out.println(p.getFileName()));
//        System.out.println("Type the sequence and press Enter for processing or Ctrl+C to exit application");
//        Scanner sc = new Scanner(System.in);
//        System.out.println(" > ");

        System.out.println("Files found for processing:");
        pathsList.forEach(p -> System.out.println(p.getFileName()));
        Ranger ranger = new SimpleMemoriser(pathsList).memorise();
        System.out.println("Type the sequence and press Enter for processing or Ctrl+C to exit application");
        System.out.print(" > ");
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            ranger.range(sc.nextLine());
            System.out.print(" > ");
        }

    }
}
