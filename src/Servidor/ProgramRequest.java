package Servidor;

import java.util.Date;

public class ProgramRequest {
    private String clientUsername;
    private int memory;
    private byte[] file;
    private int pedido_id;
    private Date date;
    private int priority;

    //constructors
    public ProgramRequest(){
        this.clientUsername = "";
        this.pedido_id = 0;
        this.memory = 0;
        this.file = new byte[0];
        this.date = new Date();
        this.priority = 0;
    }

    public ProgramRequest(String []arguments) {
        this.clientUsername = arguments[0];
        this.pedido_id = Integer.parseInt(arguments[1]);
        this.memory = Integer.parseInt(arguments[2]);
        this.file = arguments[3].getBytes();
        this.date = new Date();
        this.priority = normalizePriority((double) 1 /((0.9 * this.memory) + (0.1 * this.file.length)) * 1000);
    }

    public ProgramRequest(ProgramRequest pr) {
        this.clientUsername = pr.getClientUsername();
        this.pedido_id = pr.getPedido_id();
        this.memory = pr.getMemory();
        this.file = pr.getFile();
        this.priority = pr.getPriority();
    }

    //getters and setters
    public String getClientUsername() {
        return clientUsername;
    }

    public int getPedido_id() {
        return pedido_id;
    }

    public int getMemory() {
        return memory;
    }

    public byte[] getFile() {
        return file;
    }

    public Date getDate() {
        return date;
    }

    public int getPriority() {
        return priority;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public void setPedido_id(int pedido_id) {
        this.pedido_id = pedido_id;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    //normalize the priority
    private int normalizePriority(double rawPriority) {
        int minPriority = 1;
        int maxPriority = 10;

        return (int) Math.round((rawPriority * (maxPriority - minPriority)) + minPriority);
    }
}
