import org.junit.Test;

import java.nio.file.Path;
import java.util.List;

public class TxtSelectorTest {

    @Test
    public void testSelector() {

        String[] path = {"./"};
        List<Path> select = new TxtSelector(path, Mime.TEXT_PLAIN).select();
        assert select.size() == 2;

        select = new TxtSelector(path, Mime.TEXT_CSV).select();
        assert select.size() == 0;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSelectorNullArray() {

        new TxtSelector(null, Mime.TEXT_PLAIN);
        new TxtSelector(new String[10], null);
        new TxtSelector(new String[0], Mime.TEXT_PLAIN);
        new TxtSelector(new String[0], null);
    }
}