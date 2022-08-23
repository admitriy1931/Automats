package org.openjfx.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AuthorsController {

    @FXML
    private Button backButton;

    @FXML
    void initialize() {
        backButton.setOnAction(event -> {
            backButton.getScene().getWindow().hide();
            Loader.loadFxmlStartupPage();
        });
    }

}
