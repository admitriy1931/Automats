package org.openjfx.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import static org.openjfx.Controllers.Controller.automatonList;

public class TaskTwoController {

    @FXML
    private Button returnToStartButton;

    @FXML
    private Button buildAutomatonByRegexButton;

    @FXML
    void initialize() {
        initReturnToStartButton();
    }

    private void initReturnToStartButton() {
        returnToStartButton.setOnAction(event -> {
            returnToStartButton.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxmlStartupPage();
        });
    }
}

