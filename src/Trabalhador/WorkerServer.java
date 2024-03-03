package Trabalhador;

import Estruturas.Message;
import Estruturas.SafeDataInputStream;
import Estruturas.SafeDataOutputStream;
import Servidor.ProgramRequest;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WorkerServer {

    private int max_memory;
    private int memory_used;
    private static Lock l = new ReentrantLock();

    //constructors

    public WorkerServer() {
        this.memory_used = 0;
    }

    public WorkerServer(String max_memory) {
        this.max_memory = Integer.parseInt(max_memory);
        this.memory_used = 0;
    }

    public WorkerServer(int max_memory, int memory_used) {
        this.max_memory = max_memory;
        this.memory_used = memory_used;
    }

    //getters and setters

    public int getMax_memory() {
        return this.max_memory;
    }

    public int getMemory_used() {
        return this.memory_used;
    }

    public void setMax_memory(int max_memory) {
        this.max_memory = max_memory;
    }

    public void setMemory_used(int memory_used) {
        this.memory_used = memory_used;
    }

    // server socket in which the server listens for connections

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 9091;

    //send the memory info to the server
    public void sendMemoryInfo(SafeDataOutputStream out) throws IOException {
        Message.serialize(out,"MEMORY_INFO", this.max_memory + "\t" + this.memory_used);
        out.flush();
    }

    //main function of the worker, connects to the server and waits for requests
    public static void main(String[] args) throws IOException {

        if (args.length != 1 || !args[0].matches("\\d+")) {
            System.out.println("Usage: java WorkerServer <max_memory>");
            return;
        }

        WorkerServer workerServer = new WorkerServer(args[0]);
        try {
            // Connect to the Main Server.Server
            Socket workerSocket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to Main Server: " + workerSocket.getInetAddress().getHostAddress());

            // Set up input and output streams
            SafeDataInputStream in = new SafeDataInputStream(workerSocket.getInputStream());
            SafeDataOutputStream out = new SafeDataOutputStream(workerSocket.getOutputStream());
            workerServer.sendMemoryInfo(out);

            // Keep the connection open for ongoing communication
            while (true) {
                // Read data from the server
                Message message = Message.deserialize(in);
                if (message == null) {
                    System.out.println("Server disconnected!");
                    return;
                }
                if(message.getType().equals("SEND_PROGRAM")) {
                    String arguments[] = Message.parsePayload(message.getPayload());
                    ProgramRequest pr = new ProgramRequest(arguments);
                    Thread t = new Thread(new ProgramExecutor(pr, workerServer,out,l));
                    t.start();
                }
                else {
                    out.writeUTF("Invalid action");
                    out.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
