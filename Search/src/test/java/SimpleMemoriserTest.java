import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SimpleMemoriserTest {

    @Test (expected = IllegalArgumentException.class)
    public void testPathListNullability() {
        new SimpleMemoriser(null, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRangedListLengthNegation() {
        new SimpleMemoriser(new ArrayList<>(), - 10);
    }

    @Test
    public void rangerTest() {

        String f1 = "tst1.txt";
        String f2 = "tst2.txt";
        String f3 = "tst3.txt";
        String f4 = "tst4.txt";
        String dir = "testFiles";
        String separator = File.separator;
        String pathToDid = "." + separator + dir + separator;

        Path path1 = Paths.get(pathToDid + f1);
        Path path2 = Paths.get(pathToDid + f2);
        Path path3 = Paths.get(pathToDid + f3);
        Path path4 = Paths.get(pathToDid + f4);
        List<Path> pathList = new ArrayList<>(Arrays.asList(path1, path2, path3, path4));

        try {
            Files.createDirectories(path1.getParent());
            Files.createDirectories(path2.getParent());
            Files.createDirectories(path3.getParent());
            Files.createDirectories(path4.getParent());
            Path file1 = Files.createFile(path1);
            Path file2 = Files.createFile(path2);
            Path file3 = Files.createFile(path3);
            Path file4 = Files.createFile(path4);

            try(BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1.toString(), false));
                    BufferedWriter bw2 = new BufferedWriter(new FileWriter(file2.toString(), false));
                    BufferedWriter bw3 = new BufferedWriter(new FileWriter(file3.toString(), false));
                    BufferedWriter bw4 = new BufferedWriter(new FileWriter(file4.toString(), false))){

                bw1.write("to be or not to be");
                bw2.write("to or not");
                bw3.write("or not");
                bw4.write("fsddfhfghjgh fdghgj");
            }

            Map<String, Integer> ranges = new SimpleMemoriser(pathList, 10)
                    .memorise()
                    .range("to be or not to be");

            assert ranges.size() == 4;

            ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(ranges.entrySet());
            assert entries.get(0).getKey().equals(f1);
            assert entries.get(0).getValue() == 100;
            assert entries.get(1).getKey().equals(f2);
            assert entries.get(1).getValue() == 75;
            assert entries.get(2).getKey().equals(f3);
            assert entries.get(2).getValue() == 50;
            assert entries.get(3).getKey().equals(f4);
            assert entries.get(3).getValue() == 0;

            Files.deleteIfExists(file1);
            Files.deleteIfExists(file2);
            Files.deleteIfExists(file3);
            Files.deleteIfExists(file4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}