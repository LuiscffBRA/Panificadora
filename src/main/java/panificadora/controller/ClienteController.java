package panificadora.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import panificadora.utilities.ClienteDAO;
import panificadora.model.Cliente;
import panificadora.utilities.SceneManager;  // Importando o SceneManager
import java.sql.Timestamp;
import java.util.Calendar;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ClienteController {

    @FXML
    private TableView<Cliente> tabelaClientes;

    @FXML
    private TableColumn<Cliente, Integer> colIdCliente;

    @FXML
    private TableColumn<Cliente, String> colNome;

    @FXML
    private TableColumn<Cliente, String> colCpf;

    private ObservableList<Cliente> clientes;
    private ClienteDAO clienteDAO;

    private Stage stage;  // Declaração do stage

    @FXML
    public void initialize() {
        clienteDAO = new ClienteDAO();
        clientes = FXCollections.observableArrayList(clienteDAO.listarClientes());
        tabelaClientes.setItems(clientes);

        colIdCliente.setCellValueFactory(data -> data.getValue().idClienteProperty().asObject());
        colNome.setCellValueFactory(data -> data.getValue().nomeProperty());
        colCpf.setCellValueFactory(data -> data.getValue().cpfProperty());
    }

    // Método setStage para permitir configurar o estágio (janela)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Validação do nome (não pode ser vazio ou apenas espaços)
    private boolean validarNome(String nome) {
        return nome != null && !nome.trim().isEmpty();
    }

    // Validação do telefone (apenas números, com no mínimo 8 caracteres)
    private boolean validarTelefone(String telefone) {
        return telefone != null && telefone.matches("\\d{8,11}");
    }

    // Validação do CPF (deve ter exatamente 11 dígitos numéricos)
    private boolean validarCpf(String cpf) {
        return cpf != null && cpf.matches("\\d{11}");
    }

    // Função para permitir apenas números no telefone
    private TextFormatter<String> formatarTelefone() {
        return new TextFormatter<>(change -> {
            // Remover todos os caracteres não numéricos
            String input = change.getControlNewText().replaceAll("[^\\d]", "");

            // Limitar a 11 dígitos
            if (input.length() > 11) {
                input = input.substring(0, 11);
            }

            // Definindo o texto formatado com apenas números
            change.setText(input);
            change.setRange(0, change.getControlText().length());

            return change;
        });
    }

    @FXML
    public void abrirFormularioAdicionar() {
        Dialog<Cliente> dialog = new Dialog<>();
        dialog.setTitle("Adicionar Cliente");
        dialog.setHeaderText("Preencha os dados do novo cliente:");

        // Configurando os campos de entrada
        Label labelNome = new Label("Nome:");
        TextField campoNome = new TextField();
        Label labelEmail = new Label("Email:");
        TextField campoEmail = new TextField();
        Label labelTelefone = new Label("Telefone:");
        TextField campoTelefone = new TextField();
        campoTelefone.setTextFormatter(formatarTelefone()); // Aplicando a formatação ao campo de telefone
        Label labelCpf = new Label("CPF:");
        TextField campoCpf = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(labelNome, 0, 0);
        grid.add(campoNome, 1, 0);
        grid.add(labelEmail, 0, 1);
        grid.add(campoEmail, 1, 1);
        grid.add(labelTelefone, 0, 2);
        grid.add(campoTelefone, 1, 2);
        grid.add(labelCpf, 0, 3);
        grid.add(campoCpf, 1, 3);

        dialog.getDialogPane().setContent(grid);

        ButtonType adicionarButtonType = new ButtonType("Adicionar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(adicionarButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == adicionarButtonType) {
                String nome = campoNome.getText();
                String telefone = campoTelefone.getText();
                String cpf = campoCpf.getText();

                // Validações
                if (!validarNome(nome)) {
                    showAlert("Nome inválido", "O nome não pode ser vazio ou apenas espaços.");
                    return null;
                }

                if (!validarTelefone(telefone)) {
                    showAlert("Telefone inválido", "O telefone deve ter entre 8 e 11 dígitos numéricos.");
                    return null;
                }

                if (!validarCpf(cpf)) {
                    showAlert("CPF inválido", "O CPF deve ter exatamente 11 dígitos.");
                    return null;
                }

                // Criando a data atual como Timestamp
                Timestamp dataCadastro = new Timestamp(Calendar.getInstance().getTimeInMillis());

                return new Cliente(
                        0, // O ID será atribuído após a inserção no banco de dados
                        nome,
                        campoEmail.getText(),
                        telefone,
                        cpf,
                        dataCadastro // Passando a data de cadastro
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(cliente -> {
            // Inserir no banco de dados
            boolean inserido = clienteDAO.inserirCliente(cliente);
            if (inserido) {
                clientes.add(cliente);  // Atualizando a lista com o cliente inserido
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente adicionado com sucesso!", ButtonType.OK);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao adicionar cliente.", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void abrirFormularioEditar() {
        Cliente clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            Dialog<Cliente> dialog = new Dialog<>();
            dialog.setTitle("Editar Cliente");
            dialog.setHeaderText("Edite os dados do cliente selecionado:");

            // Configurando os campos de entrada
            Label labelNome = new Label("Nome:");
            TextField campoNome = new TextField(clienteSelecionado.getNome());
            Label labelEmail = new Label("Email:");
            TextField campoEmail = new TextField(clienteSelecionado.getEmail());
            Label labelTelefone = new Label("Telefone:");
            TextField campoTelefone = new TextField(clienteSelecionado.getTelefone());
            campoTelefone.setTextFormatter(formatarTelefone()); // Aplicando a formatação ao campo de telefone
            Label labelCpf = new Label("CPF:");
            TextField campoCpf = new TextField(clienteSelecionado.getCpf());

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.add(labelNome, 0, 0);
            grid.add(campoNome, 1, 0);
            grid.add(labelEmail, 0, 1);
            grid.add(campoEmail, 1, 1);
            grid.add(labelTelefone, 0, 2);
            grid.add(campoTelefone, 1, 2);
            grid.add(labelCpf, 0, 3);
            grid.add(campoCpf, 1, 3);

            dialog.getDialogPane().setContent(grid);

            ButtonType salvarButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(salvarButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == salvarButtonType) {
                    String nome = campoNome.getText();
                    String telefone = campoTelefone.getText();
                    String cpf = campoCpf.getText();

                    // Validações
                    if (!validarNome(nome)) {
                        showAlert("Nome inválido", "O nome não pode ser vazio ou apenas espaços.");
                        return null;
                    }

                    if (!validarTelefone(telefone)) {
                        showAlert("Telefone inválido", "O telefone deve ter entre 8 e 11 dígitos numéricos.");
                        return null;
                    }

                    if (!validarCpf(cpf)) {
                        showAlert("CPF inválido", "O CPF deve ter exatamente 11 dígitos.");
                        return null;
                    }

                    clienteSelecionado.setNome(nome);
                    clienteSelecionado.setEmail(campoEmail.getText());
                    clienteSelecionado.setTelefone(telefone);
                    clienteSelecionado.setCpf(cpf);
                    return clienteSelecionado;
                }
                return null;
            });

            dialog.showAndWait().ifPresent(cliente -> {
                if (clienteDAO.atualizarCliente(cliente)) {
                    tabelaClientes.refresh();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente atualizado com sucesso!", ButtonType.OK);
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao atualizar cliente.", ButtonType.OK);
                    alert.showAndWait();
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um cliente para editar.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    // Método para exibir alertas
    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING, mensagem, ButtonType.OK);
        alert.setTitle(titulo);
        alert.showAndWait();
    }

    @FXML
    public void excluirCliente() {
        Cliente clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja excluir este cliente?", ButtonType.YES, ButtonType.NO);
            confirmacao.showAndWait().ifPresent(resposta -> {
                if (resposta == ButtonType.YES) {
                    if (clienteDAO.excluirCliente(clienteSelecionado.getIdCliente())) {
                        clientes.remove(clienteSelecionado);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente excluído com sucesso!", ButtonType.OK);
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao excluir cliente.", ButtonType.OK);
                        alert.showAndWait();
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um cliente para excluir.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    // Método para voltar à tela principal
    public void voltarParaTelaPrincipal() {
        try {
            // Usar o caminho correto para o arquivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaPrincipal.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Armazenar a cena atual na pilha de cenas antes de trocar
            SceneManager.pushScene(tabelaClientes.getScene());  // Armazenando a cena atual

            // Obter o estágio atual e configurar a nova cena
            Stage stage = (Stage) tabelaClientes.getScene().getWindow();
            stage.setScene(scene);  // Alterando para a tela principal
            stage.show(); // Exibe a tela principal
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível carregar a tela principal.");
        }
    }



    // Método para voltar à tela anterior
    @FXML
    public void voltarParaCenaAnterior() {
        Stage stage = (Stage) tabelaClientes.getScene().getWindow();
        Scene previousScene = SceneManager.popScene();  // Pega a cena anterior da pilha

        if (previousScene != null) {
            // Verifica se a cena anterior existe e a aplica
            stage.setScene(previousScene);
            stage.show();
        } else {
            // Caso não exista cena anterior, mostra um alerta
            showAlert("Erro", "Não há cena anterior para voltar.");
        }
    }

}
