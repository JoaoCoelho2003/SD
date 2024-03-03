package Cliente;

import Estruturas.Message;
import Estruturas.SafeDataInputStream;
import Estruturas.SafeDataOutputStream;
import sd23.JobFunctionException;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientController {

    private static class ReceiveResponse implements Runnable{
        private Lock l;
        private Account u;
        private SafeDataInputStream in;
        private ClientController clientc;


        // constructor
        public ReceiveResponse(Lock l, Account u, SafeDataInputStream in, ClientController clientc){
            this.l = l;
            this.u = u;
            this.in = in;
            this.clientc = clientc;
        }

        //write the result in the respective file
        public void sendToFile(String username, String result, String id){
            try {
                File folder = new File(getAbsolutePath());
                if (!folder.exists()) {
                    folder.mkdir();
                }
                //create file if it doesn't exist
                File file = new File(getAbsolutePath(),username + ".txt");
                FileWriter writer = new FileWriter(file, true);
                writer.write("Id: " + id + " -> " + result + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //main function of the thread, receives the response from the server and calls the respective function
        public void run() {
            l.lock();
            try {

                try {

                    Message serverMessage = Message.deserialize(in);

                    if (serverMessage == null) {
                        System.out.println("Server disconnected!");
                        return;
                    }

                    String arguments[] = Message.parsePayload(serverMessage.getPayload());

                    //check the type of the message and call the respective function
                    switch (serverMessage.getType()) {
                        case "JOB_DONE":
                            sendToFile(u.getNomeUtilizador(), arguments[3], arguments[2]);
                            break;
                        case "SERVER_STATUS":
                            System.out.println("The server has " + arguments[0] + " MB of memory left and there are currently " + arguments[1] + " jobs waiting to be executed!");
                            break;
                        case "JOB_FAILED":
                            sendToFile(u.getNomeUtilizador(),"Job failed: code=" + arguments[3] + " message=" + arguments[4], arguments[2]);
                            break;
                        default:
                            System.out.println("Server response: " + serverMessage.getType());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                l.unlock();
            }
        }
    }

    private Account u;
    private Socket socket;
    private SafeDataInputStream in;
    private SafeDataOutputStream out;
    private BufferedReader stdin;
    private int pedido_id = 1;
    private Lock l = new ReentrantLock();

    // constructor
    public ClientController(Account u) {
        this.u = u;
    }

    //get the absolute path of the folder "resultados"
    public static String getAbsolutePath(){
        String currentDirectory = System.getProperty("user.dir");
        return currentDirectory + "/resultados";
    }

    //establish the connection with the server
    public void establishConnection() {
        try {
            socket = new Socket("localhost", 9090);
            stdin = new BufferedReader(new InputStreamReader(System.in));
            in = new SafeDataInputStream(socket.getInputStream());
            out = new SafeDataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //close the connection with the server
    public void closeConnection() throws IOException {
        // close the connection to the server
        try {
            in.close();
            out.close();
            stdin.close();
            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        //erase the file with the results
        File file = new File(getAbsolutePath(),u.getNomeUtilizador() + ".txt");
        file.delete();
    }

    //function that sends the login information to the server and receives the response
    public void login() throws IOException {

        // Get login information from the user
        String[] credentials = new String[2];
        System.out.print("Nome de Utilizador :: ");
        credentials[0] = stdin.readLine();
        System.out.print("Palavra-Passe:: ");
        credentials[1] = stdin.readLine();

        // Send login information to the server
        String payload = Message.createPayload(credentials);
        Message.serialize(out,"LOGIN", payload);
        out.flush();

        // Receive a response from the server
        try {
            String response = in.readUTF();
            System.out.println("Server response: " + response);

            // Check if login was successful
            if (response.equals("Login successful")) {
                if(u.getNomeUtilizador() == null){
                    u.setNomeUtilizador(credentials[0]);
                    u.setPassword(credentials[1]);
                }

                // Create a new view for the client
                ClientView view = new ClientView(this);
                view.optionsMenu();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JobFunctionException e) {
            throw new RuntimeException(e);
        }
    }

    //function that sends the register information to the server and receives the response
    public void register() throws IOException {

        // Get registration information from the user
        String[] credentials = new String[2];
        System.out.print("Nome de Utilizador :: ");
        credentials[0] = stdin.readLine();
        this.u.setNomeUtilizador(credentials[0]);
        System.out.print("Palavra-Passe :: ");
        credentials[1] = stdin.readLine();
        this.u.setPassword(credentials[1]);

        // Send registration information to the server without explicit action
        String payload = Message.createPayload(credentials);
        Message.serialize(out, "REGISTER", payload);
        out.flush();

        // Receive a response from the server
        try {
            String response = in.readUTF();
            System.out.println("Server response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //function that sends the program to the server and receives the response
    public void sendProgram() throws IOException {

        // Get program information from the user
        System.out.print("Path do ficheiro :: ");
        String file_name = stdin.readLine();

        // Get memory information from the user
        System.out.print("Memória para o programa (MB):: ");
        String memoria = stdin.readLine();

        //check if memoria is a number
        try{
            Integer.parseInt(memoria);
        }catch(NumberFormatException e){
            System.out.println("Memória inválida!");
            return;
        }

        // Read file into byte array and closing the file input stream to avoid memory leakage
        File file_execute = new File(file_name);
        if(!file_execute.exists()){
            System.out.println("File doesn't exist!");
            return;
        }
        FileInputStream read_file = new FileInputStream(file_execute);
        byte[] array = new byte[(int) file_execute.length()];

        read_file.read(array);
        read_file.close();

        // Send byte array to the server
        Message.serialize(out, "SEND_PROGRAM", u.getNomeUtilizador() + "\t" + this.pedido_id + "\t" + memoria + "\t" + Arrays.toString(array));
        out.flush();
        this.pedido_id++;

        // Receive a response from the server
        Thread t = new Thread(new ReceiveResponse(l,this.u,in,this));
        t.start();
    }

    //function that sends the request for the server status to the server and receives the response
    public void serverAvailability() throws IOException {
        Message.serialize(out,"SERVER_AVAILABILITY", "");
        out.flush();
        // Receive a response from the server
        Thread t = new Thread(new ReceiveResponse(l,this.u,in,this));
        t.start();
    }
}

