package Cliente;

import sd23.JobFunctionException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


public class Menu {

    //interface for the handlers
    public interface Handler {
        public void execute() throws JobFunctionException, IOException;
    }

    //interface for the preconditions
    public interface PreCondition {
        public boolean condition();
    }

    private List<Handler> handlers;
    private List<PreCondition> preConditions;
    private List<String> options;

    //constructor
    public Menu(String[] options) {
        this.options = Arrays.asList(options);
        this.handlers = new ArrayList<>();
        this.preConditions = new ArrayList<>();
        this.options.forEach(s -> {
            this.preConditions.add(() -> true);
            this.handlers.add(() -> System.out.println("\nATENÇÃO: Opção não implementada!"));
        });
    }

    //main function of the menu, shows the menu and reads the choice of the user
    public void run() throws JobFunctionException, IOException {
        int choice;
        do {
            showMenu();
            choice = readChoice();
            if (choice > 0 && !this.preConditions.get(choice - 1).condition()) {
                System.out.println("Opção indisponível! Tente novamente.");
            } else if (choice > 0) {
                    this.handlers.get(choice - 1).execute();
            }
        } while (choice != 0);
    }

    //set the handler and the precondition of the option
    public void setHandler(int i, Handler h) {
        this.handlers.set(i - 1, h);
    }

    public void setPreCondition(int i, PreCondition b) {
        this.preConditions.set(i - 1, b);
    }

    //show the menu
    public void showMenu() {
        System.out.println("-----MENU-----");
        for (int i = 0; i < this.options.size(); i++) {
            System.out.println(i + 1 + " - " + this.options.get(i));
        }
        System.out.println("0 - EXIT");
    }

    //read the choice of the user, if it's invalid, returns -1 and shows a message, if it's valid, returns the choice. If the user disconnects, returns 0
    public int readChoice() {
        int choice = -1;
        Scanner sc = new Scanner(System.in);
        System.out.print("Choice: ");

        try {
            if (!sc.hasNextInt()) {
                choice = -1;
                sc.next();
            }
            else{
                choice = sc.nextInt();
            }
            if (choice < 0 || choice > this.options.size()) {
                choice = -1;
                System.out.println("INVALID CHOICE!");
            }
        } catch (NoSuchElementException e) {
            // Handle end-of-file (Ctrl+D or Ctrl+Z)
            System.out.println("Client disconnecting!");
            choice = 0;
        }
        return choice;
    }

}
