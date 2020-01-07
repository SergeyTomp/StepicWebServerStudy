import org.junit.Test;

import java.io.ByteArrayOutputStream;

public class SearchTest {

    @Test(expected = IllegalArgumentException.class)
    public void testApplication() {
        new Application(null).start();
    }


//    ByteArrayOutputStream err = new ByteArrayOutputStream();
//
//    System.setErr(new PrintStream(err));
//    System.err.println("Out was: " + err.toString());
}