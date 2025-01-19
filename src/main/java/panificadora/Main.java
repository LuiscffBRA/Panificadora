package panificadora;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import panificadora.controller.TelaPrincipalController;
import panificadora.utilities.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Carregar a tela principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaPrincipal.fxml"));
            Parent root = loader.load();

            // Configurar a cena principal
            Scene mainScene = new Scene(root);

            // Carregar e adicionar o arquivo CSS à cena
            mainScene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

            // Configurar o SceneManager
            SceneManager.setMainScene(mainScene); // Armazena a cena principal
            SceneManager.setMainStage(primaryStage); // Armazena o stage principal

            // Configurar o controlador
            TelaPrincipalController controller = loader.getController();
            controller.setStage(primaryStage);

            // Configurar o stage
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Sistema de Panificadora");
            primaryStage.setResizable(true);

            // Definir largura e altura da janela
            primaryStage.setWidth(800); // Largura
            primaryStage.setHeight(600); // Altura

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
