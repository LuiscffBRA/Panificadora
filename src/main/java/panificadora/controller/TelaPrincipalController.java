package panificadora.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import panificadora.utilities.SceneManager;

import java.io.IOException;

public class TelaPrincipalController {

    private Stage stage;

    // Define o Stage para o controlador
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Ações para abrir as telas de Clientes, Produtos, Vendas
    public void abrirClientes() {
        carregarTela("/view/ClienteView.fxml", "Gerenciar Clientes");
    }

    public void abrirProdutos() {
        carregarTela("/view/ProdutoView.fxml", "Gerenciar Produtos");
    }

    public void abrirVendas() {
        carregarTela("/view/VendaView.fxml", "Gerenciar Vendas");
    }

    // Método para abrir relatórios
    public void abrirGerenciamento() {
        carregarTela("/view/GerenciamentoView.fxml", "Gerenciamento");
    }

    // Método para fechar a aplicação
    public void fecharAplicacao() {
        if (stage != null) {
            stage.close(); // Fecha o Stage (tela atual)
        } else {
            System.err.println("ERRO: O stage não foi configurado antes de fechar a aplicação.");
        }
    }

    // Método para carregar a tela, substituindo a cena atual
    public void carregarTela(String caminhoFXML, String titulo) {
        try {
            // Verifica se o Stage foi configurado corretamente
            if (this.stage == null) {
                throw new IllegalStateException("Stage não foi configurado corretamente. Certifique-se de usar setStage().");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Empurra a cena atual para a pilha antes de carregar a nova tela
            SceneManager.pushScene(stage.getScene());

            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela: " + caminhoFXML);
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }

    // Método para voltar à cena anterior
    public void voltarParaCenaAnterior() {
        try {
            Scene previousScene = SceneManager.popScene();

            if (previousScene != null) {
                stage.setScene(previousScene);
                stage.show();
            } else {
                // Se não houver cena anterior, volte para a tela principal
                carregarTela("/view/TelaPrincipal.fxml", "Tela Principal");
            }
        } catch (Exception e) {
            System.err.println("Erro ao voltar para a cena anterior: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
