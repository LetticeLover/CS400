import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Frontend implements FrontendInterface{

    private final Scanner input;
    private final BackendInterface backend;

    /**
     * Public constructor for the Frontend class.
     * @param in is the Scanner object to read user input from.
     * @param backend is the BackendInterface object to interact with and get results from.
     */
    public Frontend(Scanner in, BackendInterface backend) {
        input = in;
        this.backend = backend;
    }

    /**
     * Runs a continuous loop asking the user to input a command until they enter "Q" or "q".
     */
    @Override
    public void runCommandLoop() {
        String command;
        boolean quit = false;
        while (!quit) {
            displayMainMenu();
            System.out.println("Please enter a command:");
            // Extra do-while loop here because for some reason after entering a command
            // the overall command loop runs again with an empty command.
            // I am very confused and I feel like it is something to do with the Scanner input, but
            // I don't even know where to start for debugging it.
            do {
                command = input.nextLine();
            } while (command.isBlank());
            command = command.toUpperCase();
            System.out.println("Command Entered: " + command);
            // Switch statement to handle the command the user entered and call the appropriate function.
            switch (command) {
                case "L":
                    loadFile();
                    break;
                case "G":
                    getSongs();
                    break;
                case "F":
                    setFilter();
                    break;
                case "D":
                    displayTopFive();
                    break;
                case "Q":
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
                    break;
            }
        }
    }

    /**
     * Displays the commands to the user in a clean format.
     */
    @Override
    public void displayMainMenu() {
        System.out.println("Commands:");
        System.out.println("\t[L]oad Song File");
        System.out.println("\t[G]et Songs by Year");
        System.out.println("\t[F]ilter Songs by Loudness");
        System.out.println("\t[D]isplay Five Most Danceable");
        System.out.println("\t[Q]uit.");
    }

    /**
     * Gets a path to a file from the user and attempts to use the backend to read in that data.
     * Will catch an IOException thrown by the readData() function and inform the user of the error
     * and ask them to try again.
     */
    @Override
    public void loadFile() {
        System.out.println("Please enter the path to the file you would like to load:");
        // Get path from user.
        String filename = input.nextLine();
        System.out.println("Loading file...");
        // Load data via backend.
        try {
            backend.readData(filename);
            System.out.println("File loaded!");
        } catch (IOException e) { // If backend fails to load the file, inform user.
            System.out.println("Error loading file: " + filename);
            System.out.println("Please try again.");
        }
    }

    @Override
    public void getSongs() {
        boolean inputUnderstood = false;
        Integer yearMin = null;
        Integer yearMax = null;
        System.out.println("Would you like to filter your results within a range of years?");
        System.out.println("Please enter \"y\" for yes or \"n\" for no.");
        // Get the user's answer.
        String answer = input.nextLine().toLowerCase();
        // If the user wants to filter their songs by year.
        if (answer.equals("y")) {
            System.out.println("Please enter the minimum year:");
            // If input is not an int ask the user to enter a number.
            while (!input.hasNextInt()) {
                System.out.println("Your input could not be understood. Please enter a number.");
                input.nextLine();
            }
            yearMin = input.nextInt();
            System.out.println("Please enter the maximum year:");
            // If the input is not an int ask the user to enter a number.
            while (!input.hasNextInt()) {
                System.out.println("Your input could not be understood. Please enter a number.");
                input.nextLine();
            }
            yearMax = input.nextInt();
            System.out.println("Range set!");
            inputUnderstood = true;
        } else if (answer.equals("n")) {
            System.out.println("Finding results with no range.");
            inputUnderstood = true;
        } else {
            System.out.println("Could not understand your answer, please try again.");
        }
        // If the user entered yes or no.
        if (inputUnderstood) {
            List<String> songs = backend.getRange(yearMin, yearMax);
            if (songs.isEmpty()) {
                System.out.println("No songs found. Please try again with a different range.");
            } else {
                System.out.println("Songs found:");
                for (String song : songs) {
                    System.out.println(song);
                }
            }
        }
    }

    /**
     * Ask the user to enter a desired loudness and set the loudness filter through the backend.
     */
    @Override
    public void setFilter() {
        System.out.println("Please enter the desired maximum loudness (0-9):");
        // If the user doesn't enter a number prompt them to.
        while (!input.hasNextInt()) {
            System.out.println("Your input could not be understood. Please enter a number.");
            input.nextLine();
        }
        int loudnessMax = input.nextInt();
        // While loop to ensure that the user enters a number from 0-9 (inclusive).
        while (loudnessMax < 0 || loudnessMax > 9) {
            System.out.println("Your desired loudness was not within range.");
            System.out.println("Please enter a number 0-9 for desired maximum loudness:");
            while (!input.hasNextInt()) {
                System.out.println("Your input could not be understood. Please enter a number.");
                input.nextLine();
            }
            loudnessMax = input.nextInt();
        }
        backend.setFilter(loudnessMax);
        System.out.println("Filter updated!");
    }

    /**
     * Display the backend's returned top five songs to the user.
     */
    @Override
    public void displayTopFive() {
        List<String> songs = backend.fiveMost();
        if (songs.isEmpty()) {
            System.out.println("No songs found, try changing your year range and/or filter settings.");
        } else {
            System.out.println("Displaying the top " + songs.size() + " most danceable song(s):");
            for (String song : songs) {
                System.out.println(song);
            }
        }
    }
}
