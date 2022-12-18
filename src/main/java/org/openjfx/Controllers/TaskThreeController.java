package org.openjfx.Controllers;

import algorithms.Adduction;
import automaton.Automaton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import static org.openjfx.Controllers.AutomatonInputController.setupButtonAsReturnToStart;
import static org.openjfx.Controllers.Controller.automatonList;

public class TaskThreeController {

    @FXML
    private AnchorPane mainPane;

    @FXML
    void initialize() {
        Automaton tableBasedAutomaton = automatonList.get(0);
        TableView<String[]> automatonTableView = TaskOneController.createAutomatonJumpTableTableView(tableBasedAutomaton);

        Text automatonInfo;
        //TODO
        if (true) {
             automatonInfo = new Text("Автомат, построенный по таблице");
        }
        else {
            automatonInfo = new Text("Сгенерированный автомат");
        }
        automatonInfo.setFill(Color.WHITESMOKE);
        automatonInfo.setFont(Font.font("System", 20));

        Button findSyncWordButton = getFindSyncWordButton();



        setupMainPaneForAutomatonTableViewDisplay(mainPane, automatonTableView, automatonInfo, findSyncWordButton);
    }

    private Button getFindSyncWordButton() {
        var button = new Button("Найти синхронизирующее слово");
        button.setOnAction(event ->  {
            AnchorPane newPane = new AnchorPane();
            newPane.setStyle("-fx-background-color: #2e3348;");
            //TODO
            //String word = FindSyncWord();

            String word = "";

            Text text;
            if (word.isBlank()) {
                text = new Text("Синхронизирующего слова не существует");
            }
            else {
                text = new Text("Синхронизирующее слово: " + "'" + word + "'");
            }
            text.setFont(Font.font("System", 16));
            text.setFill(Color.WHITESMOKE);
            newPane.getChildren().add(text);
            AnchorPane.setLeftAnchor(text, 10.0);
            AnchorPane.setTopAnchor(text, 10.0);

            Button returnToStartButton = new Button("Вернуться в начало");
            setupButtonAsReturnToStart(returnToStartButton);
            AnchorPane.setTopAnchor(returnToStartButton, 10.0);
            AnchorPane.setRightAnchor(returnToStartButton, 10.0);

            newPane.getChildren().add(returnToStartButton);

            button.getScene().getWindow().hide();
            Loader.showStage(new Scene(newPane), true);
        });
        return button;
    }

    private void setupMainPaneForAutomatonTableViewDisplay(AnchorPane mainPane, TableView<String[]> firstAutomatonTableView, Text firstAutomatonInfo, Button findSyncWordButton) {
        mainPane.getChildren().addAll(firstAutomatonTableView, firstAutomatonInfo, findSyncWordButton);

        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(firstAutomatonTableView, 25.0);
        AnchorPane.setTopAnchor(firstAutomatonTableView, 50.0);

        AnchorPane.setLeftAnchor(firstAutomatonInfo, 25.0);
        AnchorPane.setTopAnchor(firstAutomatonInfo, 10.0);

        AnchorPane.setRightAnchor(findSyncWordButton, 10.0);
        AnchorPane.setBottomAnchor(findSyncWordButton, 10.0);
    }
}