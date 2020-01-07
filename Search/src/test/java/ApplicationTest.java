import org.junit.Test;

public class ApplicationTest {

    @Test (expected = AssertionError.class)
    public void applicationCreation() {

        new Application(null);
        new Application(new SimpleMemoriser(), null, 10);
        new Application(new SimpleMemoriser(), new TxtSelector(new String[10]), 0);
    }
}