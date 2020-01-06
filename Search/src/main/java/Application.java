import interfaces.Memoriser;
import interfaces.Ranger;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class Application {

    private Memoriser memoriser;

    public Application(@NotNull Memoriser memoriser) {
        assert memoriser != null;
        this.memoriser = memoriser;
    }

    public void start() {

        Ranger ranger = memoriser.memorise();
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        System.out.println("Type the sequence and press Enter for searching or Ctrl+C to exit application");
        System.out.print("search > ");

        while (sc.hasNext()) {

            ranger.range(sc.nextLine()).forEach((k, v) -> sb.append(k).append(": ").append(v).append("\n"));
            System.out.println(sb.toString());
            System.out.print("search > ");
            sb.setLength(0);
        }
    }
}
