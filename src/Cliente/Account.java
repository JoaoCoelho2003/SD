package Cliente;

public class Account {
    private String nomeUtilizador;
    private String password;

    //constructors
    public Account() {
        this.nomeUtilizador = null;
        this.password = null;
    }

    public Account(String nomeUtilizador, String password) {
        this.nomeUtilizador = nomeUtilizador;
        this.password = password;
    }

    //getters and setters

    public String getNomeUtilizador() {
        return nomeUtilizador;
    }

    public String getPassword() {
        return password;
    }

    public void setNomeUtilizador(String nomeUtilizador) {
        this.nomeUtilizador = nomeUtilizador;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
