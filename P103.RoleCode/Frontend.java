import java.util.Scanner;

public class Frontend implements FrontendInterface{

    enum COMMANDS {
        L,
        G,
        F,
        D,
        Q
    }

    private Scanner input;
    private BackendInterface backend;

    public Frontend(Scanner in, BackendInterface backend) {
        input = in;
        this.backend = backend;
    }

    @Override
    public void runCommandLoop() {
        String command;
        boolean quit = false;
        while (!quit) {
            command = input.nextLine();
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
            }
        }
    }

    @Override
    public void displayMainMenu() {
        System.out.println("Commands:");
        System.out.println("\t(L)oad a file.");
        System.out.println("\t(G)et songs.");
        System.out.println("\tSet (F)ilter.");
        System.out.println("\t(D)isplay top five.");
        System.out.println("\t(Q)uit.");
    }

    @Override
    public void loadFile() {

    }

    @Override
    public void getSongs() {

    }

    @Override
    public void setFilter() {

    }

    @Override
    public void displayTopFive() {

    }
}
