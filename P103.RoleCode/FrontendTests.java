import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class FrontendTests {

    // Tests the get songs by year function of the frontend
    @Test
    public void roleTest1() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(tree);

        // Get songs by year with range of 2000-2020
        TextUITester tester = new TextUITester("G\ny\n2000\n2020\nQ\n");
        Scanner input = new Scanner(System.in);
        Frontend frontend = new Frontend(input, backend);
        frontend.runCommandLoop();
        input.close();

        String output = tester.checkOutput();
        Assertions.assertTrue(output.contains("Range set!"));
    }

    // Tests the load file and set filter functions of the frontend
    @Test
    public void roleTest2() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(tree);

        // Load the file named "jerma",
        // attempt to set filter outside range and then set filter within range.
        TextUITester tester = new TextUITester("L\njerma\nF\n10\n5\nQ\n");
        Scanner input = new Scanner(System.in);
        Frontend frontend = new Frontend(input, backend);
        frontend.runCommandLoop();
        input.close();

        String output = tester.checkOutput();
        Assertions.assertTrue(output.contains("File loaded!"));
        Assertions.assertTrue(output.contains("Your desired loudness was not within range."));
        Assertions.assertTrue(output.contains("Filter updated!"));
    }

    // Tests the functionality of loading a song into the tree and then displaying the top five songs
    @Test
    public void roleTest3() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(tree);

        // Display the top five songs after having loaded a song
        TextUITester tester = new TextUITester("L\njerma\nD\nQ\n");
        Scanner input = new Scanner(System.in);
        Frontend frontend = new Frontend(input, backend);
        frontend.runCommandLoop();
        input.close();

        String output = tester.checkOutput();
        Assertions.assertTrue(output.contains("Displaying the top 4 most danceable song(s):"));
        Assertions.assertTrue(output.contains("A L I E N S"));
        Assertions.assertTrue(output.contains("BO$$"));
        Assertions.assertTrue(output.contains("Cake By The Ocean"));
        Assertions.assertTrue(output.contains("DJ Got Us Fallin' In Love (feat. Pitbull)"));
    }

}
