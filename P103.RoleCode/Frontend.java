import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Frontend implements FrontendInterface{


    private final Scanner input;
    private final BackendInterface backend;

    public Frontend(Scanner in, BackendInterface backend) {
        input = in;
        this.backend = backend;
    }

    @Override
    public void runCommandLoop() {
        String command;
        boolean quit = false;
        while (!quit) {
            displayMainMenu();
            System.out.println("Please enter a command:");
            command = input.nextLine();
            while (command.isBlank()) {
                command = input.nextLine();
            }
            System.out.println("Command Entered: " + command);
            command = command.toUpperCase();
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
                case "":
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
                    break;
            }
        }
    }

    @Override
    public void displayMainMenu() {
        System.out.println("Commands:");
        System.out.println("\t[L]oad Song File");
        System.out.println("\t[G]et Songs by Year");
        System.out.println("\t[F]ilter Songs by Loudness");
        System.out.println("\t[D]isplay Five Most Danceable");
        System.out.println("\t[Q]uit.");
    }

    @Override
    public void loadFile() {
        System.out.println("Please enter the name of the file you would like to load:");
        String filename = input.nextLine();
        System.out.println("Loading file...");
        try {
            backend.readData(filename);
            System.out.println("File loaded!");
        } catch (IOException e) {
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

        String answer = input.nextLine().toLowerCase();
        if (answer.equals("y")) {
            System.out.println("Please enter the minimum year:");
            while (!input.hasNextInt()) {
                System.out.println("Your input could not be understood. Please enter a number.");
            }
            yearMin = input.nextInt();
            System.out.println("Please enter the maximum year:");
            while (!input.hasNextInt()) {
                System.out.println("Your input could not be understood. Please enter a number.");
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

    @Override
    public void setFilter() {
        System.out.println("Please enter the desired maximum loudness (0-9):");
        while (!input.hasNextInt()) {
            System.out.println("Your input could not be understood. Please enter a number.");
        }
        int loudnessMax = input.nextInt();
        while (loudnessMax < 0 || loudnessMax > 9) {
            System.out.println("Your desired loudness was not within range.");
            System.out.println("Please enter a number 0-9 for desired maximum loudness:");
            while (!input.hasNextInt()) {
                System.out.println("Your input could not be understood. Please enter a number.");
            }
            loudnessMax = input.nextInt();
        }
        backend.setFilter(loudnessMax);
        System.out.println("Filter updated!");
    }

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
