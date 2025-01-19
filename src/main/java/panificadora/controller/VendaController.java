package panificadora.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import panificadora.model.Cliente;
import panificadora.model.Produto;
import panificadora.model.Venda;
import panificadora.utilities.ClienteDAO;
import panificadora.utilities.ProdutoDAO;
import panificadora.utilities.VendaDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VendaController {

    @FXML
    private ComboBox<Cliente> clienteComboBox;

    @FXML
    private ComboBox<Produto> produtoComboBox;

    @FXML
    private TextField quantidadeField;

    @FXML
    private TableView<Venda> vendaTable;

    @FXML
    private TableColumn<Venda, Integer> idColumn;

    @FXML
    private TableColumn<Venda, String> clienteColumn;

    @FXML
    private TableColumn<Venda, String> dataColumn;

    @FXML
    private TableColumn<Venda, Double> valorTotalColumn;

    private ObservableList<Venda> vendas;
    private Stage stage;
    private final Connection connection;
    private final VendaDAO vendaDAO;
    private final ClienteDAO clienteDAO;  // Declaração do clienteDAO

    public VendaController() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/panificadora", "postgres", "admin");
        this.vendaDAO = new VendaDAO(connection);
        this.clienteDAO = new ClienteDAO();  // Inicialização do clienteDAO
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        vendas = FXCollections.observableArrayList(vendaDAO.obterTodasVendas());
        vendaTable.setItems(vendas);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("idVenda"));
        clienteColumn.setCellValueFactory(data ->
                new SimpleStringProperty(obterNomeCliente(data.getValue().getIdCliente())));

        dataColumn.setCellValueFactory(data -> {
            LocalDateTime dataVenda = data.getValue().getDataVenda();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return new SimpleStringProperty(dataVenda.format(formatter));
        });

        valorTotalColumn.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

        carregarClientes();
        carregarProdutos();
    }

    private void carregarClientes() {
        ObservableList<Cliente> clientes = FXCollections.observableArrayList(clienteDAO.listarClientes());
        clienteComboBox.setItems(clientes);
    }

    private void carregarProdutos() {
        ObservableList<Produto> produtos = FXCollections.observableArrayList(new ProdutoDAO().listarProdutos());
        produtoComboBox.setItems(produtos);
    }

    @FXML
    public void adicionarVenda() {
        Cliente cliente = clienteComboBox.getValue();
        Produto produto = produtoComboBox.getValue();
        int quantidade;

        try {
            quantidade = Integer.parseInt(quantidadeField.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Quantidade inválida!", Alert.AlertType.WARNING);
            return;
        }

        if (cliente != null && produto != null && quantidade > 0) {
            double valorTotal = quantidade * produto.getPreco();
            LocalDateTime dataVenda = LocalDateTime.now();

            // Modificando a criação de Venda para incluir o nome do cliente
            Venda novaVenda = new Venda(0, cliente.getIdCliente(), cliente.getNome(), dataVenda, valorTotal);
            boolean sucesso = vendaDAO.inserirVenda(novaVenda);

            if (sucesso) {
                vendas.add(novaVenda);
                limparCampos();
                mostrarAlerta("Venda adicionada com sucesso!", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Erro ao adicionar venda!", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Preencha todos os campos corretamente!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void editarVenda() {
        Venda vendaSelecionada = vendaTable.getSelectionModel().getSelectedItem();

        if (vendaSelecionada != null) {
            vendaSelecionada.setValorTotal(vendaSelecionada.getValorTotal() + 10.0);
            vendaDAO.atualizarVenda(vendaSelecionada);

            vendaTable.refresh();
            mostrarAlerta("Venda editada com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Selecione uma venda para editar!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void removerVenda() {
        Venda vendaSelecionada = vendaTable.getSelectionModel().getSelectedItem();

        if (vendaSelecionada != null) {
            boolean sucesso = vendaDAO.excluirVenda(vendaSelecionada.getIdVenda());

            if (sucesso) {
                vendas.remove(vendaSelecionada);
                mostrarAlerta("Venda removida com sucesso!", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Erro ao remover venda!", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Selecione uma venda para remover!", Alert.AlertType.WARNING);
        }
    }

    private void mostrarAlerta(String mensagem, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void limparCampos() {
        clienteComboBox.setValue(null);
        produtoComboBox.setValue(null);
        quantidadeField.clear();
    }

    // Método alterado para retornar o nome real do cliente
    private String obterNomeCliente(int idCliente) {
        Cliente cliente = clienteDAO.buscarClientePorId(idCliente);  // Utilizando o clienteDAO
        return cliente != null ? cliente.getNome() : "Cliente desconhecido";
    }
}
