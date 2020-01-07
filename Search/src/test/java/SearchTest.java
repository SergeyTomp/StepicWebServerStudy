import org.junit.Test;

import java.io.ByteArrayOutputStream;

public class SearchTest {

    @Test(expected = AssertionError.class)
    public void testApplication() {
        new Application(null).start();
        new Application(new SimpleMemoriser(), null, 10).start();
        new Application(new SimpleMemoriser(), new TxtSelector(new String[10]), 0).start();
    }


//    ByteArrayOutputStream err = new ByteArrayOutputStream();
//
//    System.setErr(new PrintStream(err));
//    System.err.println("Out was: " + err.toString());
}