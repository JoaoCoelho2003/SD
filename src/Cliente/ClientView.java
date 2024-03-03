package Cliente;

import Cliente.ClientController;
import sd23.JobFunctionException;

import java.io.IOException;

public class ClientView {
    private ClientController client;

    //constructor
    public ClientView(ClientController client) {
        this.client = client;
    }

    //main menu of the client
    public void mainMenu() throws JobFunctionException, IOException {
        Menu menuPrincipal = new Menu(new String[]{
                "Login",
                "Register"
        });

        menuPrincipal.setHandler(1,client :: login);
        menuPrincipal.setHandler(2,client :: register);
        menuPrincipal.run();
    }

    //options menu of the client
    public void optionsMenu() throws JobFunctionException, IOException {

        Menu optionsMenu = new Menu(new String[]{
                "Send Program",
                "Server Availability"
        });

        optionsMenu.setHandler(1,client :: sendProgram);
        optionsMenu.setHandler(2,client :: serverAvailability);
        optionsMenu.run();
    }
}
