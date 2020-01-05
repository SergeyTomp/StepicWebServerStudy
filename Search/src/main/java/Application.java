import interfaces.Memoriser;
import interfaces.Ranger;

import java.util.Scanner;

public class Application {

    private Memoriser memoriser;

    public Application(Memoriser memoriser) {
        this.memoriser = memoriser;
    }

    public void start() {

        Ranger ranger = memoriser.memorise();
        Scanner sc = new Scanner(System.in);
        System.out.println("Type the sequence and press Enter for processing or Ctrl+C to exit application");
        System.out.print("search > ");
        while (sc.hasNext()) {
            ranger.range(sc.nextLine());
            System.out.print("search > ");
        }
    }
}
