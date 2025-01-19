package panificadora.model;

import javafx.beans.property.*;
import java.sql.Timestamp;

public class Cliente {
    private final IntegerProperty idCliente;
    private final StringProperty nome;
    private final StringProperty email;
    private final StringProperty telefone;
    private final StringProperty cpf;
    private final ObjectProperty<Timestamp> dataCadastro;

    // Construtor da classe Cliente
    public Cliente(int idCliente, String nome, String email, String telefone, String cpf, Timestamp dataCadastro) {
        this.idCliente = new SimpleIntegerProperty(idCliente);
        this.nome = new SimpleStringProperty(nome);
        this.email = new SimpleStringProperty(email);
        this.telefone = new SimpleStringProperty(telefone);
        this.cpf = new SimpleStringProperty(cpf);
        this.dataCadastro = new SimpleObjectProperty<>(dataCadastro);
    }

    // Getters e setters com propriedades
    public int getIdCliente() {
        return idCliente.get();
    }

    public void setIdCliente(int idCliente) {
        this.idCliente.set(idCliente);
    }

    public IntegerProperty idClienteProperty() {
        return idCliente;
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getTelefone() {
        return telefone.get();
    }

    public void setTelefone(String telefone) {
        this.telefone.set(telefone);
    }

    public StringProperty telefoneProperty() {
        return telefone;
    }

    public String getCpf() {
        return cpf.get();
    }

    public void setCpf(String cpf) {
        this.cpf.set(cpf);
    }

    public StringProperty cpfProperty() {
        return cpf;
    }

    public Timestamp getDataCadastro() {
        return dataCadastro.get();
    }

    public void setDataCadastro(Timestamp dataCadastro) {
        this.dataCadastro.set(dataCadastro);
    }

    public ObjectProperty<Timestamp> dataCadastroProperty() {
        return dataCadastro;
    }

    // Sobrescrever o método toString para exibir o nome
    @Override
    public String toString() {
        return nome.get();  // Retorna o nome ao invés do endereço de memória
    }
}
