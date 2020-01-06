import java.nio.file.Path;
import java.util.List;

public class Search {

    public static void main(String[] args) {

        //TODO: constants below could be placed in Properties
        final String FILE_TYPE = "text/plain";
        final long LIMIT = 10;

        if (args.length == 0) {
            System.err.println("No directories specified, please try again, exit application!");
            System.exit(0);
        }

        List<Path> pathsList = new TxtSelector(args, FILE_TYPE).select();

        if (pathsList.size() == 0) {
            System.err.println("No text files found, please try again, exit application!");
            System.exit(0);
        }

        System.out.println("Files found for processing:");
        pathsList.forEach(p -> System.out.println(p.getFileName()));
        new Application(new SimpleMemoriser(pathsList, LIMIT)).start();
    }
}
