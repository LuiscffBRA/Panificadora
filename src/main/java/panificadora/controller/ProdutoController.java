package panificadora.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import panificadora.utilities.ProdutoDAO;
import panificadora.model.Produto;

public class ProdutoController {

    @FXML
    private TextField nomeField;

    @FXML
    private TextField precoField;

    @FXML
    private TextArea descricaoField;

    @FXML
    private TextField quantidadeField;

    @FXML
    private TableView<Produto> produtoTable;

    @FXML
    private TableColumn<Produto, String> nomeColumn;

    @FXML
    private TableColumn<Produto, Double> precoColumn;

    @FXML
    private TableColumn<Produto, Integer> quantidadeColumn;

    private ObservableList<Produto> produtos;
    private ProdutoDAO produtoDAO;
    private Stage stage; // Adicionando Stage

    public void setStage(Stage stage) {
        this.stage = stage; // Recebe o Stage
    }

    @FXML
    public void initialize() {
        produtoDAO = new ProdutoDAO();
        produtos = FXCollections.observableArrayList(produtoDAO.listarProdutos());
        produtoTable.setItems(produtos);

        // Configurar colunas
        nomeColumn.setCellValueFactory(data -> data.getValue().nomeProperty());
        precoColumn.setCellValueFactory(data -> data.getValue().precoProperty().asObject());
        quantidadeColumn.setCellValueFactory(data -> data.getValue().quantidadeEstoqueProperty().asObject());

        // Adiciona um listener de seleção de linha na tabela
        produtoTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Preenche os campos de texto com os dados do produto selecionado
                nomeField.setText(newValue.getNome());
                precoField.setText(String.valueOf(newValue.getPreco()));
                descricaoField.setText(newValue.getDescricao());
                quantidadeField.setText(String.valueOf(newValue.getQuantidadeEstoque()));
            }
        });
    }

    @FXML
    public void adicionarProduto() {
        String nome = nomeField.getText();
        String descricao = descricaoField.getText();
        int quantidade;
        double preco;

        try {
            preco = Double.parseDouble(precoField.getText());
            quantidade = Integer.parseInt(quantidadeField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Preço ou quantidade inválidos!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (!nome.isEmpty() && !descricao.isEmpty()) {
            Produto novoProduto = new Produto(0, nome, descricao, preco, quantidade);
            if (produtoDAO.inserirProduto(novoProduto)) {
                produtos.add(novoProduto);
                limparCampos();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao salvar produto!", ButtonType.OK);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha todos os campos!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    public void editarProduto() {
        Produto produtoSelecionado = produtoTable.getSelectionModel().getSelectedItem();

        if (produtoSelecionado != null) {
            String nome = nomeField.getText();
            String descricao = descricaoField.getText();
            int quantidade;
            double preco;

            try {
                preco = Double.parseDouble(precoField.getText());
                quantidade = Integer.parseInt(quantidadeField.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Preço ou quantidade inválidos!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            if (!nome.isEmpty() && !descricao.isEmpty()) {
                produtoSelecionado.setNome(nome);
                produtoSelecionado.setDescricao(descricao);
                produtoSelecionado.setPreco(preco);
                produtoSelecionado.setQuantidadeEstoque(quantidade);

                if (produtoDAO.atualizarProduto(produtoSelecionado)) {
                    produtoTable.refresh(); // Atualiza a tabela para refletir a edição
                    limparCampos();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao editar produto!", ButtonType.OK);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha todos os campos!", ButtonType.OK);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um produto para editar!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    public void removerProduto() {
        Produto produtoSelecionado = produtoTable.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            if (produtoDAO.excluirProduto(produtoSelecionado.getIdProduto())) {
                produtos.remove(produtoSelecionado);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao remover produto!", ButtonType.OK);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um produto para remover!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void limparCampos() {
        nomeField.clear();
        precoField.clear();
        descricaoField.clear();
        quantidadeField.clear();
    }
}
