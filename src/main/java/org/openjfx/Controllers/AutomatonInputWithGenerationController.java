package org.openjfx.Controllers;

import automaton.Automaton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import static org.openjfx.Controllers.Controller.automatonList;

public class AutomatonInputWithGenerationController extends AutomatonInputController {

    @FXML
    private Button generateRandomAutomatonButton;

    private AnchorPane tableWindowMainPane;

    @FXML
    protected void initialize() {
        super.initialize();
        setupGenerateRandomAutomatonButton(generateRandomAutomatonButton);
    }

    protected void setupGenerateRandomAutomatonButton(Button button) {
        button.setOnAction(event -> {
            // Automaton generatedAutomaton = GenerateRandomSynchonizedAutomaton();
            Automaton generatedAutomaton = null;
            automatonList.add(generatedAutomaton);
            var automatonTableView = TaskOneController.createAutomatonJumpTableTableView(generatedAutomaton);

            button.getScene().getWindow().hide();



            Loader.loadFxml("/taskThree.fxml", true);
        });
    }
}

