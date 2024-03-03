package Servidor;

import Estruturas.Message;
import Estruturas.SafeDataOutputStream;
import Servidor.Server;
import Trabalhador.Worker;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class JobScheduler implements Runnable{

    private Server server;
    private final Lock lock = new ReentrantLock();
    private final Condition conn = lock.newCondition();

    //constructor
    public JobScheduler(Server server) {
        this.server = server;
    }

    //main function of the thread, receives the request from the client and sends it to the best worker
    public void run() {
        while (true) {
            lock.lock();
            try {
                if(!server.getPendingPrograms().isEmpty()) {

                    ProgramRequest programRequest = server.getPendingPrograms().peek();

                    // Find a worker with enough memory, wait if none are available
                    List<Worker> availableWorkers = findAvailableWorker(programRequest.getMemory());
                    while (availableWorkers.isEmpty()) {
                        conn.await();
                        availableWorkers = findAvailableWorker(programRequest.getMemory());
                        programRequest = server.getPendingPrograms().peek();
                    }

                    // Send the program to the best worker
                    sendJobToBestWorker(programRequest, availableWorkers);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    //get the list of workers with enough memory
    private List<Worker> findAvailableWorker(int requiredMemory) {
        return server.getConnectedWorkers().values().stream()
                        .filter(worker -> worker.getMemory_available() >= requiredMemory)
                        .toList();
    }

    //send the program to the best worker
    private void sendJobToBestWorker(ProgramRequest pr, List<Worker> availableWorkers) throws IOException {

        //find the best worker, the one with the least number of jobs and the most memory available
        Worker bestWorker = availableWorkers.get(0);
        for (Worker worker : availableWorkers) {
            if (worker.getNum_jobs() < bestWorker.getNum_jobs()) {
                bestWorker = worker;
            } else if (worker.getNum_jobs() == bestWorker.getNum_jobs()) {
                if (worker.getMemory_available() > bestWorker.getMemory_available()) {
                    bestWorker = worker;
                }
            }
        }

        // send the program to the best worker
        SafeDataOutputStream bestWorkerOut = bestWorker.getOut();
        Message.serialize(bestWorkerOut,"SEND_PROGRAM",pr.getClientUsername() + "\t" + pr.getPedido_id() + "\t" + pr.getMemory() + "\t" + new String(pr.getFile()));
        bestWorkerOut.flush();

        ProgramRequest pr2 = this.server.getPendingPrograms().poll();
        updatePriorityPrograms(pr2);
        bestWorker.setMemory_available(bestWorker.getMemory_available() - pr.getMemory());
        bestWorker.setNum_jobs(bestWorker.getNum_jobs() + 1);

    }

    /*
    update the priority of the programs, it only boosts half the time to ensure that the priority is not too high and the increment is equivalent to a small percentage
    of the priority of the program that was sent.
     */
    private void updatePriorityPrograms(ProgramRequest pr){
        int priority = (int) (pr.getPriority() * 0.25);

        if(new Random().nextInt(2) == 0){
            for (ProgramRequest programRequest : this.server.getPendingPrograms()) {
                programRequest.setPriority(programRequest.getPriority() + priority);
            }
        }
    }

    //set the condition, used to wake up the thread
    public void setCondition() {
        this.lock.lock();
        try {
            this.conn.signal();
        } finally {
            this.lock.unlock();
        }
    }
}

