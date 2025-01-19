module panificadora {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;  // Importando o módulo java.sql

    // Expondo os pacotes necessários
    exports panificadora;
    exports panificadora.controller;
    exports panificadora.model;
    exports panificadora.utilities;

    // Abrindo pacotes para reflexão (necessário para JavaFX)
    opens panificadora to javafx.fxml;
    opens panificadora.controller to javafx.fxml;
    opens panificadora.model to javafx.fxml;
    opens panificadora.utilities to javafx.fxml;
}
