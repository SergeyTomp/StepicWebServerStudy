import java.io.*;
import java.util.Scanner;

public class Search {

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.err.println("No files specified, exit application");
            System.exit(0);
        }
        for (String arg: args) {
            System.err.println(arg);
        }
        File file;
        file = new File(args[0]);
        if (!file.exists()) {
            System.err.println(String.format("File %s not found, exit application", args[0]));
            System.exit(0);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        line = sb.toString();
        System.out.println("Please type word and press Enter for search or Ctrl+C to exit application.");
        Scanner sc = new Scanner(System.in);
        System.err.print("> ");
        String lineToSearch;
        while (!(lineToSearch = sc.next().trim()).isEmpty()) {
            System.err.println(line.indexOf(lineToSearch));
            System.err.print("> ");
        }
    }
}
