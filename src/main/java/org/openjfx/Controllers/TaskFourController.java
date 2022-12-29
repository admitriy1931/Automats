package org.openjfx.Controllers;

import algorithms.SynchronizedAutomatonsGenerator;
import automaton.Automaton;
import automaton.SynchronizedAutomaton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static org.openjfx.Controllers.Controller.automatonList;

public class TaskFourController {

    @FXML
    private AnchorPane mainPane;

    @FXML
    void initialize() {
        //TODO: Не забыть кастануть все эти автоматы к синхронизируемым
        Automaton generatedAutomaton = (SynchronizedAutomaton)automatonList.get(0);
        TableView<String[]> automatonTableView = TaskOneController.createAutomatonJumpTableTableView(generatedAutomaton);

        Text automatonInfo = new Text("Сгенерированный автомат");
        automatonInfo.setFill(Color.WHITESMOKE);
        automatonInfo.setFont(Font.font("System", 20));

        Button generateAnotherOneButton = getGenerateSyncAutomatonButton();
        Button returnToStartButton = new Button("Вернуться в начало");
        AutomatonInputController.setupButtonAsReturnToStart(returnToStartButton);

        setupMainPaneForAutomatonTableViewDisplay(mainPane, automatonTableView, automatonInfo, generateAnotherOneButton, returnToStartButton);
    }

    @FXML
    public Button getGenerateSyncAutomatonButton() {
        Button button = new Button("Сгенерировать еще один автомат с такими же параметрами");
        button.setOnAction(event -> {
            Automaton generatedAutomaton = null;
            //TODO: Тут выводить нормальный автомат, а не преобразованный
            //generatedAutomaton.origin
            try {
                generatedAutomaton = SynchronizedAutomatonsGenerator.synchronizedAutomatonsGenerator(1,
                        AutomatonSyncGenerationInputController.stateCount,
                        AutomatonSyncGenerationInputController.letterCount).get(0);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            automatonList.clear();
            automatonList.add(generatedAutomaton);

            button.getScene().getWindow().hide();

            Loader.loadFxml("/taskFour.fxml", true);
        });
        return button;
    }

    private void setupMainPaneForAutomatonTableViewDisplay(AnchorPane mainPane, TableView<String[]> firstAutomatonTableView, Text firstAutomatonInfo, Button generateAnotherOneButton, Button returnToStartButton) {
        mainPane.getChildren().addAll(firstAutomatonTableView, firstAutomatonInfo, generateAnotherOneButton, returnToStartButton);

        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(firstAutomatonTableView, 25.0);
        AnchorPane.setTopAnchor(firstAutomatonTableView, 50.0);

        AnchorPane.setLeftAnchor(firstAutomatonInfo, 25.0);
        AnchorPane.setTopAnchor(firstAutomatonInfo, 10.0);

        AnchorPane.setRightAnchor(generateAnotherOneButton, 10.0);
        AnchorPane.setBottomAnchor(generateAnotherOneButton, 10.0);

        AnchorPane.setRightAnchor(returnToStartButton, 10.0);
        AnchorPane.setTopAnchor(returnToStartButton, 10.0);
    }
}
