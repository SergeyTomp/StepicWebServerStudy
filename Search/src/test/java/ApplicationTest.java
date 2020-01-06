import org.junit.Test;

public class ApplicationTest {

    @Test (expected = IllegalArgumentException.class)
    public void applicationCreation() {

        new Application(null);
    }
}