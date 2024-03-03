package Servidor;

import Cliente.Account;
import Estruturas.MyPriorityQueue;
import Estruturas.SafeDataOutputStream;
import Trabalhador.Worker;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private Map<String, Account> accounts;
    private Map<String, SafeDataOutputStream> connectedClients;
    private MyPriorityQueue<ProgramRequest> pendingPrograms;
    private Map<Integer, Worker> connectedWorkers;
    private int max_memory_workers;

    private final Lock accountsLock = new ReentrantLock();
    private final Lock connectedClientsLock = new ReentrantLock();
    private final Lock pendingProgramsLock = new ReentrantLock();
    private final Lock connectedWorkersLock = new ReentrantLock();


    public Server(){
        this.accounts = new HashMap<>();
        this.connectedClients = new HashMap<>();
        this.pendingPrograms = new MyPriorityQueue<>(new ProgramRequestComparator());
        this.connectedWorkers = new HashMap<>();
        this.max_memory_workers = 0;
    }

    // getters and setters

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public Map<String, SafeDataOutputStream> getConnectedClients() {
        return connectedClients;
    }

    public MyPriorityQueue<ProgramRequest> getPendingPrograms() {
        return pendingPrograms;
    }

    public Map<Integer, Worker> getConnectedWorkers() {
        return connectedWorkers;
    }

    public int getMax_memory_workers() {
        return max_memory_workers;
    }

    public void setMax_memory_workers(int max_memory_workers) {
        this.max_memory_workers = max_memory_workers;
    }

    public void setAccounts(Map<String, Account> accounts) {
        accountsLock.lock();
        try {
            this.accounts = accounts;
        } finally {
            accountsLock.unlock();
        }
    }

    public void setConnectedClients(Map<String, SafeDataOutputStream> connectedClients) {
        connectedClientsLock.lock();
        try {
            this.connectedClients = connectedClients;
        } finally {
            connectedClientsLock.unlock();
        }
    }

    public void setPendingPrograms(MyPriorityQueue<ProgramRequest> pendingPrograms) {
        pendingProgramsLock.lock();
        try {
            this.pendingPrograms = pendingPrograms;
        } finally {
            pendingProgramsLock.unlock();
        }
    }

    public void setConnectedWorkers(Map<Integer, Worker> connectedWorkers) {
        connectedWorkersLock.lock();
        try {
            this.connectedWorkers = connectedWorkers;
        } finally {
            connectedWorkersLock.unlock();
        }
    }

    // adds and removes from the structures

    public void addAccount(String username, Account account) {
        accountsLock.lock();
        try {
            this.accounts.put(username, account);
        } finally {
            accountsLock.unlock();
        }
    }

    public void removeAccount(String username) {
        accountsLock.lock();
        try {
            this.accounts.remove(username);
        } finally {
            accountsLock.unlock();
        }
    }

    public void addConnectedClient(String username, SafeDataOutputStream out) {
        connectedClientsLock.lock();
        try {
            this.connectedClients.put(username, out);
        } finally {
            connectedClientsLock.unlock();
        }
    }

    public void removeConnectedClient(String username) {
        connectedClientsLock.lock();
        try {
            this.connectedClients.remove(username);
        } finally {
            connectedClientsLock.unlock();
        }
    }

    public void addPendingProgram(ProgramRequest pr) {
        pendingProgramsLock.lock();
        try {
            this.pendingPrograms.offer(pr);
        } finally {
            pendingProgramsLock.unlock();
        }
    }

    public void removePendingProgram() {
        pendingProgramsLock.lock();
        try {
            this.pendingPrograms.poll();
        } finally {
            pendingProgramsLock.unlock();
        }
    }

    public void addConnectedWorker(Worker worker){
        connectedWorkersLock.lock();
        try {
            this.connectedWorkers.put(worker.getWorker_id(), worker);
        } finally {
            connectedWorkersLock.unlock();
        }
    }

    public void removeConnectedWorker(int worker_id){
        connectedWorkersLock.lock();
        try {
            this.connectedWorkers.remove(worker_id);
        } finally {
            connectedWorkersLock.unlock();
        }
    }

    // get a specific element from the structures

    public Account getAccount(String username) {
        accountsLock.lock();
        try {
            return this.accounts.get(username);
        } finally {
            accountsLock.unlock();
        }
    }

    public SafeDataOutputStream getConnectedClient(String username) {
        connectedClientsLock.lock();
        try {
            return this.connectedClients.get(username);
        } finally {
            connectedClientsLock.unlock();
        }
    }

    public ProgramRequest getPendingProgram() {
        pendingProgramsLock.lock();
        try {
            return this.pendingPrograms.peek();
        } finally {
            pendingProgramsLock.unlock();
        }
    }

    public Worker getConnectedWorker(int id) {
        connectedWorkersLock.lock();
        try {
            return this.connectedWorkers.get(id);
        } finally {
            connectedWorkersLock.unlock();
        }
    }

    // Methods for confirming an element exists in the structures with locks

    public boolean containsAccount(String username) {
        accountsLock.lock();
        try {
            return this.accounts.containsKey(username);
        } finally {
            accountsLock.unlock();
        }
    }

    public boolean containsConnectedClient(String username) {
        connectedClientsLock.lock();
        try {
            return this.connectedClients.containsKey(username);
        } finally {
            connectedClientsLock.unlock();
        }
    }

    public boolean containsConnectedWorker(int id) {
        connectedWorkersLock.lock();
        try {
            return this.connectedWorkers.containsKey(id);
        } finally {
            connectedWorkersLock.unlock();
        }
    }

    // Methods for getting the size of the structures with locks

    public int sizeAccounts() {
        accountsLock.lock();
        try {
            return this.accounts.size();
        } finally {
            accountsLock.unlock();
        }
    }

    public int sizeConnectedClients() {
        connectedClientsLock.lock();
        try {
            return this.connectedClients.size();
        } finally {
            connectedClientsLock.unlock();
        }
    }

    public int sizePendingPrograms() {
        pendingProgramsLock.lock();
        try {
            return this.pendingPrograms.size();
        } finally {
            pendingProgramsLock.unlock();
        }
    }

    public int sizeConnectedWorkers() {
        connectedWorkersLock.lock();
        try {
            return this.connectedWorkers.size();
        } finally {
            connectedWorkersLock.unlock();
        }
    }

    // Methods for checking if the structures are empty with locks

    public boolean isEmptyAccounts() {
        accountsLock.lock();
        try {
            return this.accounts.isEmpty();
        } finally {
            accountsLock.unlock();
        }
    }

    public boolean isEmptyConnectedClients() {
        connectedClientsLock.lock();
        try {
            return this.connectedClients.isEmpty();
        } finally {
            connectedClientsLock.unlock();
        }
    }

    public boolean isEmptyPendingPrograms() {
        pendingProgramsLock.lock();
        try {
            return this.pendingPrograms.isEmpty();
        } finally {
            pendingProgramsLock.unlock();
        }
    }

    public boolean isEmptyConnectedWorkers() {
        connectedWorkersLock.lock();
        try {
            return this.connectedWorkers.isEmpty();
        } finally {
            connectedWorkersLock.unlock();
        }
    }

    //function that changes the memory of a worker
    public void changeMemoryWorkerPerId(int worker_id, int memory){
        connectedWorkersLock.lock();
        try {
            Worker w = this.connectedWorkers.get(worker_id);
            w.setMemory_available(w.getMemory_available() + memory);
        } finally {
            connectedWorkersLock.unlock();
        }
    }

    //method that returns the first pending program
    public ProgramRequest pollPendingProgram() {
        pendingProgramsLock.lock();
        try {
            return this.pendingPrograms.poll();
        } finally {
            pendingProgramsLock.unlock();
        }
    }

    // decrement number of jobs of a worker
    public void decrementNumJobsWorker(int worker_id) {
        connectedWorkersLock.lock();
        try {
            Worker w = this.connectedWorkers.get(worker_id);
            w.setNum_jobs(w.getNum_jobs() - 1);
        } finally {
            connectedWorkersLock.unlock();
        }
    }

    // main method of the server, creates the server sockets and the threads for handling clients and workers
    public static void main(String[] args) throws InterruptedException{
        int portClients = 9090;
        int portWorkers = 9091;
        int id_worker = 1;
        Server server = new Server();
        JobScheduler jobs = new JobScheduler(server);
        Thread jobSchedulerThread = new Thread(jobs);
        jobSchedulerThread.start();
        try {
            // Create server sockets
            ServerSocket serverSocketClients = new ServerSocket(portClients);
            System.out.println("Server is listening on port " + portClients + " for clients");

            ServerSocket serverSocketWorkers = new ServerSocket(portWorkers);
            System.out.println("Server is listening on port " + portWorkers + " for workers");

            // Create separate threads for handling clients and workers
            Thread clientAcceptThread = new Thread(() -> acceptClients(serverSocketClients, server));
            Thread workerAcceptThread = new Thread(() -> acceptWorkers(serverSocketWorkers, server, id_worker, jobs));

            // Start the threads
            clientAcceptThread.start();
            workerAcceptThread.start();

            // Wait for threads to finish
            clientAcceptThread.join();
            workerAcceptThread.join();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // methods for accepting clients and workers

    private static void acceptClients(ServerSocket serverSocket, Server server) {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress());
                Thread clientThread = new Thread(new ClientHandler(clientSocket, server));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void acceptWorkers(ServerSocket serverSocket, Server server, int id_worker, JobScheduler jobs) {
        try {
            while (true) {
                Socket workerSocket = serverSocket.accept();
                System.out.println("Worker connected from " + workerSocket.getInetAddress());
                Thread workerThread = new Thread(new WorkerHandler(id_worker, workerSocket, server, jobs));
                workerThread.start();
                id_worker++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
