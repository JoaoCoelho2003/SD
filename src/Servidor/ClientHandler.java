package Servidor;

import Cliente.Account;
import Estruturas.Message;
import Estruturas.SafeDataInputStream;
import Estruturas.SafeDataOutputStream;
import Servidor.Server;
import Trabalhador.Worker;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;

    //constructor
    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    //function to close the connection with the client
    public void closeconnectionClient(SafeDataInputStream in, SafeDataOutputStream out, String username) throws IOException {
        // Close the streams and clientSocket
        System.out.println("Client disconnected!");
        if(username != null) server.removeConnectedClient(username);
        in.close();
        out.close();
        this.clientSocket.close();
    }

    //main function of the thread, receives the request from the client and calls the respective function
    public void run(){
        SafeDataInputStream in = null;
        SafeDataOutputStream out = null;
        String username = null;

        try {
            // Create input and output streams for communication
            in = new SafeDataInputStream(this.clientSocket.getInputStream());
            out = new SafeDataOutputStream(this.clientSocket.getOutputStream());

            // Keep the connection open for ongoing communication
            while (true) {
                // Read data from the client
                Message clientMessage = Message.deserialize(in);
                String arguments[] = null;
                if(clientMessage != null){
                    arguments = Message.parsePayload(clientMessage.getPayload());
                    username = arguments[0];
                }

                if (clientMessage == null) {
                    return;
                }
                switch (clientMessage.getType()) {
                    case "LOGIN":
                        this.handleLogin(in, out, arguments);
                        break;
                    case "REGISTER":
                        this.handleRegister(in, out, arguments);
                        break;
                    default:
                        out.writeUTF("Invalid action");
                        out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the streams and clientSocket
                closeconnectionClient(in,out,username);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //function to handle the login of the client
    public void handleLogin(SafeDataInputStream in, SafeDataOutputStream out, String []arguments) throws IOException {

        // Check if the user exists and the password is correct
        if (server.containsAccount(arguments[0]) && server.getAccount(arguments[0]).getPassword().equals(arguments[1]) && !server.containsConnectedClient(arguments[0])) {
            out.writeUTF("Login successful");
            out.flush();
            // Store the connected client's username and output stream
            server.addConnectedClient(arguments[0], out);

            // Keep the connection open for ongoing communication
            while (true) {
                // Read data from the client
                Message message = Message.deserialize(in);

                if (message == null) {
                    return;
                }

                // Handle the client's request
                switch (message.getType()) {
                    case "SEND_PROGRAM":
                        String values[] = Message.parsePayload(message.getPayload());
                        ProgramRequest pr = new ProgramRequest(values);
                        if(server.getMax_memory_workers() >= pr.getMemory()) server.addPendingProgram(pr);
                        break;
                    case "SERVER_AVAILABILITY":
                        new Thread(() -> {
                            try {
                                int max_memory_available = 0;
                                for(Worker w : server.getConnectedWorkers().values()) {
                                    if(w.getMemory_available() > max_memory_available) max_memory_available = w.getMemory_available();
                                }
                                Message.serialize(out, "SERVER_STATUS",max_memory_available + "\t" + server.sizePendingPrograms());
                                out.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                        break;
                    default:
                        out.writeUTF("Invalid action");
                        out.flush();
                }
            }
        } else {
            out.writeUTF("Invalid username or password");
            out.flush();
        }
    }

    public void handleRegister(SafeDataInputStream in, SafeDataOutputStream out, String []arguments) throws IOException {

        // Check if the username already exists
        if (!server.containsAccount(arguments[0])) {

            // Register the new user
            Account newAccount = new Account(arguments[0], arguments[1]);
            server.addAccount(arguments[0],newAccount);
            out.writeUTF("Registration successful");
            out.flush();
        } else {
            out.writeUTF("Username already exists");
            out.flush();
        }
    }
}
