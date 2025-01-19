package panificadora.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;  // Adicione esta linha
import panificadora.utilities.SceneManager;

public class GerenciamentoController {

    @FXML
    private void abrirClientes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ClienteView.fxml"));
            Parent root = loader.load();

            // Criar um novo Stage (nova janela/aba)
            Stage newStage = new Stage();
            Scene scene = new Scene(root);

            // Configurar a nova cena no novo Stage
            newStage.setScene(scene);
            newStage.setTitle("Gerenciar Clientes");

            // Exibir o novo Stage
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirProdutos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProdutoView.fxml"));
            Parent root = loader.load();

            // Criar um novo Stage (nova janela/aba)
            Stage newStage = new Stage();
            Scene scene = new Scene(root);

            // Configurar a nova cena no novo Stage
            newStage.setScene(scene);
            newStage.setTitle("Gerenciar Vendas");

            // Exibir o novo Stage
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirVendas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/VendaView.fxml"));
            Parent root = loader.load();

            // Criar um novo Stage (nova janela/aba)
            Stage newStage = new Stage();
            Scene scene = new Scene(root);

            // Configurar a nova cena no novo Stage
            newStage.setScene(scene);
            newStage.setTitle("Gerenciar Vendas");

            // Exibir o novo Stage
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void voltarParaTelaPrincipal() {
        try {
            // Recuperar a última cena do SceneManager
            Scene previousScene = SceneManager.popScene();

            if (previousScene != null) {
                SceneManager.getCurrentStage().setScene(previousScene);
                SceneManager.getCurrentStage().setTitle("Tela Principal");
            } else {
                System.out.println("Nenhuma cena anterior encontrada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
