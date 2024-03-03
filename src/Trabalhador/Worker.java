package Trabalhador;

import Estruturas.SafeDataOutputStream;

public class Worker {
    private int worker_id;
    private SafeDataOutputStream out;
    private int memory_available;
    private int num_jobs;

    //constructors

    public Worker(int worker_id, SafeDataOutputStream out, int memory_available) {
        this.out = out;
        this.worker_id = worker_id;
        this.memory_available = memory_available;
        this.num_jobs = 0;
    }

    public Worker() {
        this.out = null;
        this.worker_id = 0;
        this.memory_available = 0;
        this.num_jobs = 0;
    }

    //getters and setters

    public SafeDataOutputStream getOut() {
        return this.out;
    }

    public int getWorker_id() {
        return this.worker_id;
    }

    public int getMemory_available() {
        return this.memory_available;
    }

    public int getNum_jobs() {
        return this.num_jobs;
    }

    public void setOut(SafeDataOutputStream out) {
        this.out = out;
    }

    public void setWorker_id(int worker_id) {
        this.worker_id = worker_id;
    }

    public void setMemory_available(int memory_available) {
        this.memory_available = memory_available;
    }

    public void setNum_jobs(int num_jobs) {
        this.num_jobs = num_jobs;
    }
}
