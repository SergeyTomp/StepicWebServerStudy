import interfaces.Memoriser;
import interfaces.Ranger;
import interfaces.WordsMapper;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleMemoriser implements Memoriser {

    private List<Path> pathList;
    private final long limit;

    public SimpleMemoriser(@NotNull List<Path> pathList, long limit) {
        this.pathList = pathList;
        if(limit < 0) throw new IllegalArgumentException("List LIMIT can not be negative");
        this.limit = limit;
    }

    private Stream<String> getClearedStream(String s) {

        return Arrays.stream(s.toLowerCase()
                .replaceAll("\\p{Punct}", " ")
                .split("\\s+"));
    }

    @Override
    public Ranger memorise(){

        Path lastPath = Paths.get("last");
        int cpus = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(cpus);
        List<Future<Map<String, Set<Path>>>> futures = new ArrayList<>();
        BlockingQueue<Path> pathsQueue = new LinkedBlockingQueue<>(pathList);
        pathsQueue.add(lastPath);

        for (int i = 0; i < cpus; i++) {

            futures.add(pool.submit((WordsMapper<Map<String, Set<Path>>>) () -> {

                Map<String, Set<Path>> wordsMap = new HashMap<>();

                while ((!Thread.currentThread().isInterrupted())) {

                    Path path = pathsQueue.take();

                    if (path == lastPath) {
                        pathsQueue.add(path);
                        break;
                    }

                    try(BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {

                        br.lines().flatMap(this::getClearedStream)
                                .filter(s -> !s.isEmpty())
                                .forEach(s -> {
                                    wordsMap.computeIfAbsent(s, k -> new HashSet<>());
                                    wordsMap.get(s).add(path);
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return wordsMap;
            }));
        }
        Map<String, Set<Path>> wordsMap = new HashMap<>();

        futures.forEach(future -> {
            try {
                future.get().forEach((key, value) -> {
                    wordsMap.computeIfAbsent(key, k -> new HashSet<>());
                    wordsMap.get(key).addAll(value);
                });
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        pool.shutdown();
        pathsQueue.clear();

        return (string) -> {

            Map<String, Integer> rangesMap = new HashMap<>();

            Set<String> wordsSet = getClearedStream(string)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toCollection(HashSet::new));

            int weight = Math.round(100f / wordsSet.size());

            pathList.forEach(path -> rangesMap.put(path.getFileName().toString(), 0));

            wordsSet.forEach(word ->
                    wordsMap.get(word).forEach(path -> {
                        String fileName = path.getFileName().toString();
                        rangesMap.merge(fileName, weight, (int1, int2) -> int1 + weight);
                    }));

//            StringBuilder sb = new StringBuilder();
//
//            rangesMap.entrySet()
//                    .stream()
//                    .sorted((o1, o2) -> o2.getValue() - o1.getValue())
//                    .limit(limit)
//                    .forEach(es ->
//                    sb.append(es.getKey()).append(": ").append(es.getValue()).append("%").append("\n"));
//
//            System.out.println(sb.toString());

            return rangesMap.entrySet()
                    .stream()
                    .sorted((o1, o2) -> o2.getValue() - o1.getValue())
                    .limit(limit)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (i1, i2) -> i2, LinkedHashMap::new));
        };
    }
}
