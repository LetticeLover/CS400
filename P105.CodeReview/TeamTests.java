import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamTests {

    @Test
    public void readDataTest() throws IOException {
        // Instantiate the tree and backend as well as the variable to hold expected output
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);
        List<String> expected = new ArrayList<>();
        expected.add("Bad Liar");
        expected.add("Drip (feat. Migos)");
        expected.add("Anaconda");
        expected.add("Come Get It Bae");
        expected.add("Me Too");

        // Attempt to read the data from songs.csv
        try {
            backend.readData("songs.csv");
        } catch (IOException e) {
            Assertions.fail("Reading data failed.", e);
        }
    }

    @Test
    public void fiveMostTest() throws IOException {
        // Instantiate the tree and backend as well as the variable to hold expected output
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);
        List<String> expected = new ArrayList<>();
        expected.add("Bad Liar");
        expected.add("Drip (feat. Migos)");
        expected.add("Anaconda");
        expected.add("Come Get It Bae");
        expected.add("Me Too");

        // Attempt to read the data from songs.csv
        try {
            backend.readData("songs.csv");
        } catch (IOException e) {
            Assertions.fail("Reading data failed.", e);
        }

        List<String> top = backend.fiveMost();

        int index = 0;
        for (String title : top) {
            Assertions.assertEquals(expected.get(index), title);
            index++;
        }
    }

    @Test
    public void getRangeTest() throws IOException {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);
        List<String> expected = new ArrayList<>();
        expected.add("Kills You Slowly");

        try {
            backend.readData("songs.csv");
        } catch (IOException e) {
            Assertions.fail("Reading data failed.", e);
        }

        List<String> top = backend.getRange(2019, 2019);
        Assertions.assertEquals(expected.getFirst(), top.getFirst());
    }
}
