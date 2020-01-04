import interfaces.WordsMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class WordsMapperTask implements WordsMapper {

    Map<String, Set<Path>> wordsMap;
    private final BlockingQueue<Path> pathsQueue;
    private final Path lastPath;

    public WordsMapperTask(BlockingQueue<Path> pathsQueue, Path lastPath) {
        this.wordsMap = new HashMap<>();
        this.pathsQueue = pathsQueue;
        this.lastPath = lastPath;
    }

    @Override
    public Map<String, Set<Path>> call() throws Exception {

        while ((!Thread.currentThread().isInterrupted())) {

            Path path = pathsQueue.take();

            if (path == lastPath) {
                pathsQueue.add(path);
                break;
            }

            try(BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {

                br.lines().flatMap(s -> Arrays.stream(s.toLowerCase()
                        .replaceAll("\\p{Punct}", " ")
                        .split("\\s+")))
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
    }
}
