import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Search {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("No directories specified, exit application");
            System.exit(0);
        }

        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        List<Path> pathsList = new ArrayList<>();
        Arrays.stream(args).forEach(p -> {
            Path path = FileSystems.getDefault().getPath(p);
            if (Files.exists(path, LinkOption.NOFOLLOW_LINKS) && Files.isDirectory(path)) {
                try {
                    Files.newDirectoryStream(path).forEach(f -> {
                        String type = fileNameMap.getContentTypeFor(f.toUri().toString());
                        if ((!Files.isDirectory(f)) && type != null && type.contains("text/plain")) {
                            pathsList.add(f);
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

        final long LIMIT = 10;
        System.out.println("Files found for processing:");
        pathsList.forEach(p -> System.out.println(p.getFileName()));
        new Application(new SimpleMemoriser(pathsList, LIMIT)).start();
    }
}
