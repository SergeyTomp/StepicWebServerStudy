import org.junit.Test;

public class SearchTest {

    @Test(expected = IllegalArgumentException.class)
    public void testApplication() {
        new Application(null).start();
    }
}