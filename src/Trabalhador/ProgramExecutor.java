package Trabalhador;

import Estruturas.Message;
import Estruturas.SafeDataOutputStream;
import Servidor.ProgramRequest;
import Trabalhador.WorkerServer;
import sd23.JobFunction;
import sd23.JobFunctionException;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProgramExecutor implements Runnable {
    private ProgramRequest pr;
    private WorkerServer server;
    private SafeDataOutputStream out;
    private Lock l;

    //constructor
    public ProgramExecutor(ProgramRequest pr, WorkerServer server, SafeDataOutputStream out, Lock l) {
        this.pr = pr;
        this.server = server;
        this.out = out;
        this.l = l;
    }

    //main function of the thread, executes the program and sends the result to the server
    public void run() {
        //set the memory used by the worker
        server.setMemory_used(server.getMemory_used() + pr.getMemory());

        try {
            byte[] output = JobFunction.execute(pr.getFile());
            if (out != null) {
                l.lock();
                try{
                    Message.serialize(out,"JOB_DONE", pr.getClientUsername() + "\t" + pr.getMemory() + "\t" + pr.getPedido_id() + "\t" + Arrays.toString(output));
                    out.flush();
                }finally {
                    l.unlock();
                }
                System.err.println("success, returned " + output.length + " bytes");
            }

        //if the program fails, send the error message to the server
        } catch (JobFunctionException e) {
            System.err.println("job failed: code=" + e.getCode() + " message=" + e.getMessage());
            if (out != null) {
                l.lock();
                try{
                    try {
                        Message.serialize(out,"JOB_FAILED",pr.getClientUsername() + "\t" + pr.getMemory() + "\t" + pr.getPedido_id() + "\t" + e.getCode() + "\t" + e.getMessage());
                        out.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }finally {
                    l.unlock();
                }
            }
        } catch (IOException e) {
            System.err.println("Error sending message to server");
        } finally {
            server.setMemory_used(server.getMemory_used() - pr.getMemory());
        }
    }
}
