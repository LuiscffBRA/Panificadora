package panificadora.model;

import java.time.LocalDateTime;

public class Venda {

    private int idVenda;
    private int idCliente;
    private String nomeCliente;  // Novo campo para armazenar o nome do cliente
    private LocalDateTime dataVenda;
    private double valorTotal;

    // Construtor alterado para incluir nomeCliente
    public Venda(int idVenda, int idCliente, String nomeCliente, LocalDateTime dataVenda, double valorTotal) {
        this.idVenda = idVenda;
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente; // Atribuindo nome ao cliente
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
    }

    // Getters e Setters
    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
