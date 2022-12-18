package org.openjfx.Controllers;

import automaton.Automaton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class Controller {

    public static final ArrayList<Automaton> automatonList = new ArrayList<>();

    @FXML
    private Button authorsButton;

    @FXML
    private Button taskOneButton;

    @FXML
    private Button taskTwoButton;

    @FXML
    private Button taskThreeButton;

    @FXML
    void initialize() {
        initAuthorsButton();
        setupTaskButton(taskOneButton, "/automatonInput.fxml");
        setupTaskButton(taskTwoButton, "/automatonAndRegexInput.fxml");
        setupTaskButton(taskThreeButton, "/automatonInputWithGeneration.fxml");
    }

    @FXML
    private void setupTaskButton(Button button, String fxmlName) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxml(fxmlName, false);
        });
    }

    @FXML
    private void initAuthorsButton() {
        authorsButton.setOnAction(event -> {
            authorsButton.getScene().getWindow().hide();
            Loader.loadFxml("/authors.fxml", false);
        });
    }
}


