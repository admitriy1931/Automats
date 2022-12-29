package org.openjfx.Controllers;

import algorithms.SynchronizedAutomatonsGenerator;
import automaton.Automaton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import static org.openjfx.Controllers.Controller.automatonList;

public class AutomatonSyncGenerationInputController extends AutomatonInputController {

    @FXML
    private TextField alphabetField;

    @FXML
    private Button generateSyncAutomatonButton;

    public static int stateCount;

    public static int letterCount;

    @FXML
    protected void initialize() {
        setupButtonAsReturnToStart(returnToStartButton);
        setupStatesCountField(statesCountField);
        this.setupAlphabetField(this.alphabetField);
        setupGenerateSyncAutomatonButton(generateSyncAutomatonButton);
    }

    @FXML
    private void setupGenerateSyncAutomatonButton(Button button) {
        button.setOnAction(event -> {
            if (!checkAlphabetAndStatesCorrectness())
                return;
            Automaton generatedAutomaton = null;
            stateCount = Integer.parseInt(statesCountField.getText());
            letterCount = Integer.parseInt(alphabetField.getText());
            try {
                generatedAutomaton = SynchronizedAutomatonsGenerator.synchronizedAutomatonsGenerator(1, stateCount, letterCount).get(0);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            automatonList.add(generatedAutomaton);

            button.getScene().getWindow().hide();

            Loader.loadFxml("/taskFour.fxml", true);
        });
    }

    protected void setupAlphabetField(TextField alphabetField) {
        alphabetField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]*")) {
                alphabetField.setText(newValue.replaceAll("[^0-9]", ""));
            }
        });
    }

    protected boolean checkAlphabetAndStatesCorrectness() {
        inputWindowMainPane.getChildren().remove(inputCorrectnessText);
        if (statesCountField.getText().isBlank() || alphabetField.getText().isBlank())
            return false;

        int states = Integer.parseInt(statesCountField.getText());
        int lettersCount = Integer.parseInt(alphabetField.getText());

        if (states == 0 || states > 10) {
            if (states == 0) {
                inputCorrectnessText = new Text("В автомате не может быть 0 состояний");
            } else {
                inputCorrectnessText = new Text("В сгенерированном автомате не может быть больше 10 состояний");
            }
            inputCorrectnessText.setFill(Color.RED);
            inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 12));
            AnchorPane.setBottomAnchor(inputCorrectnessText, 10.0);
            AnchorPane.setLeftAnchor(inputCorrectnessText, 10.0);
            inputWindowMainPane.getChildren().add(inputCorrectnessText);
            return false;
        }

        if (lettersCount < 2 || lettersCount > 4) {
            if (lettersCount < 2) {
                inputCorrectnessText = new Text("Алфавит не может содержать меньше двух букв");
            }
            else {
                inputCorrectnessText = new Text("Алфавит не может содержать больше 4-х букв");
            }
            inputCorrectnessText.setFill(Color.RED);
            inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 12));
            AnchorPane.setBottomAnchor(inputCorrectnessText, 10.0);
            AnchorPane.setLeftAnchor(inputCorrectnessText, 10.0);
            inputWindowMainPane.getChildren().add(inputCorrectnessText);
            return false;
        }

        return true;
    }
}
