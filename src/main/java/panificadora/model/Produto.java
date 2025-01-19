package panificadora.model;

import javafx.beans.property.*;

public class Produto {
    private IntegerProperty idProduto;
    private StringProperty nome;
    private StringProperty descricao;
    private DoubleProperty preco;
    private IntegerProperty quantidadeEstoque;

    // Construtores
    public Produto() {
        this.idProduto = new SimpleIntegerProperty();
        this.nome = new SimpleStringProperty();
        this.descricao = new SimpleStringProperty();
        this.preco = new SimpleDoubleProperty();
        this.quantidadeEstoque = new SimpleIntegerProperty();
    }

    public Produto(int idProduto, String nome, String descricao, double preco, int quantidadeEstoque) {
        this();
        this.idProduto.set(idProduto);
        this.nome.set(nome);
        this.descricao.set(descricao);
        this.preco.set(preco);
        this.quantidadeEstoque.set(quantidadeEstoque);
    }

    // Getters e Setters com propriedades
    public int getIdProduto() { return idProduto.get(); }
    public void setIdProduto(int idProduto) { this.idProduto.set(idProduto); }
    public IntegerProperty idProdutoProperty() { return idProduto; }

    public String getNome() { return nome.get(); }
    public void setNome(String nome) { this.nome.set(nome); }
    public StringProperty nomeProperty() { return nome; }

    public String getDescricao() { return descricao.get(); }
    public void setDescricao(String descricao) { this.descricao.set(descricao); }
    public StringProperty descricaoProperty() { return descricao; }

    public double getPreco() { return preco.get(); }
    public void setPreco(double preco) { this.preco.set(preco); }
    public DoubleProperty precoProperty() { return preco; }

    public int getQuantidadeEstoque() { return quantidadeEstoque.get(); }
    public void setQuantidadeEstoque(int quantidadeEstoque) { this.quantidadeEstoque.set(quantidadeEstoque); }
    public IntegerProperty quantidadeEstoqueProperty() { return quantidadeEstoque; }

    // Sobrescrever o método toString para exibir o nome
    @Override
    public String toString() {
        return nome.get();  // Retorna o nome ao invés do endereço de memória
    }
}
