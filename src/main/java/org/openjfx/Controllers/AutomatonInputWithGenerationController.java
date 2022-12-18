package org.openjfx.Controllers;

import automaton.Automaton;
import com.google.common.collect.HashBasedTable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Arrays;

import static org.openjfx.Controllers.Controller.automatonList;

public class AutomatonInputWithGenerationController extends AutomatonInputController {

    @FXML
    private Button generateRandomAutomatonButton;

    @FXML
    protected void initialize() {
        super.initialize();
        setupGenerateRandomAutomatonButton(generateRandomAutomatonButton);
    }

    protected void setupGenerateRandomAutomatonButton(Button button) {
        //TODO
        button.setOnAction(event -> {
            //String states = statesCountField.getText();
            //String alphabet = alphabetField.getText();
            // Automaton generatedAutomaton = GenerateRandomSynchronizedAutomaton(states, alphabet);
            //Automaton generatedAutomaton = null;
            //automatonList.add(generatedAutomaton);

            button.getScene().getWindow().hide();

            Loader.loadFxml("/taskThree.fxml", true);

        });
    }

    protected Button getCreateAutomatonButton(TextField startVertexTextField, TextField finalVerticesTextField, TableView<String[]> automatonTableView, String[] states, String[] alphabet) {
        Button button = new Button("Создать автомат");
        button.setOnAction(event2 -> {
            String[][] automatonJumpTable = new String[automatonTableView.getItems().size()][automatonTableView.getColumns().size()];
            ObservableList<String[]> items = automatonTableView.getItems();
            String startVertex = startVertexTextField.getText().strip();
            String[] finalVertices = finalVerticesTextField.getText().split(",");

            for (int i = 0; i < states.length; i++) {
                states[i] = items.get(i)[0];
            }

            tableWindowMainPane.getChildren().remove(inputCorrectnessText);
            if (!checkInputCorrectness(startVertex, finalVertices, states, automatonTableView)) {
                tableWindowMainPane.getChildren().add(inputCorrectnessText);
                tableWindowMainPane.requestLayout();
                return;
            }

            for (int i = 0; i < items.size(); i++) {
                System.arraycopy(items.get(i), 0, automatonJumpTable[i], 0, automatonTableView.getColumns().size());
            }

            for (int i = 0; i < automatonJumpTable.length; i++) {
                for (int j = 0; j < automatonJumpTable[i].length; j++) {
                    automatonJumpTable[i][j] = automatonJumpTable[i][j].strip();
                }
            }

            for (int i = 0; i < finalVertices.length; i++) {
                finalVertices[i] = finalVertices[i].strip();
            }

            HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();

            for (int i = 0; i < automatonJumpTable.length; i++) {
                for (int j = 1; j < automatonJumpTable[i].length; j++) {
                    jumpTable.put(states[i], alphabet[j - 1], automatonJumpTable[i][j]);
                }
            }

            automatonList.add(new Automaton(false, jumpTable, startVertex, Arrays.asList(finalVertices)));

            button.getScene().getWindow().hide();

            Loader.loadFxml("/taskThree.fxml", true);
        });
        return button;
    }
}

