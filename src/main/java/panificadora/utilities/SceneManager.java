package panificadora.utilities;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Stack;

public class SceneManager {
    private static final Stack<Scene> sceneStack = new Stack<>();
    private static Scene mainScene = null;
    private static Stage mainStage = null; // Referência ao Stage principal

    // Define a cena principal
    public static void setMainScene(Scene scene) {
        mainScene = scene;
    }

    // Define o Stage principal
    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    // Retorna o Stage principal
    public static Stage getCurrentStage() {
        return mainStage;
    }

    // Empilha uma cena para que possamos voltar a ela depois
    public static void pushScene(Scene scene) {
        if (scene != null) {
            sceneStack.push(scene);
        }
    }

    // Retorna a última cena empilhada, ou a cena principal se o stack estiver vazio
    public static Scene popScene() {
        if (!sceneStack.isEmpty()) {
            return sceneStack.pop();
        } else if (mainScene != null) {
            return mainScene;  // Caso não haja cenas no stack, retorna a cena principal
        }
        return null;
    }

    // Limpa o stack de cenas
    public static void clearStack() {
        sceneStack.clear();
    }

    // Verifica se ainda há cenas no stack
    public static boolean hasScenes() {
        return !sceneStack.isEmpty();
    }
}
