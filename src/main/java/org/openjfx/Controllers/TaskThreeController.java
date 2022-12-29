package org.openjfx.Controllers;

import algorithms.GreedySyncWordFinding;
import automaton.Automaton;
import automaton.SynchronizedAutomaton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

import static org.openjfx.Controllers.AutomatonInputController.setupButtonAsReturnToStart;
import static org.openjfx.Controllers.Controller.automatonList;

public class TaskThreeController {

    @FXML
    private AnchorPane mainPane;

    @FXML
    void initialize() {
        Automaton tableBasedAutomaton = automatonList.get(0);
        TableView<String[]> automatonTableView = TaskOneController.createAutomatonJumpTableTableView(tableBasedAutomaton);

        Text automatonInfo = new Text("Автомат, построенный по таблице");
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
            SynchronizedAutomaton syncAutomaton = new SynchronizedAutomaton
                    (automatonList.get(0).jumpTable, new ArrayList<>(), new ArrayList<>(), null, null);
            Text syncWord = new Text();
            Text shortestSyncWord = new Text();
            try {
                if (GreedySyncWordFinding.isAutomatonSynchronized(syncAutomaton)) {
                    GreedySyncWordFinding.addTwoElementVertices(syncAutomaton);
                    GreedySyncWordFinding.greedyWordFindingAlg(syncAutomaton);
                    syncAutomaton = GreedySyncWordFinding.addCombinedElementVertices(syncAutomaton);
                    syncAutomaton = GreedySyncWordFinding.greedyShortestWordFindingAlg(syncAutomaton);
                    syncWord = new Text("Синхронизирующее слово: " + "'" + syncAutomaton.syncWord + "'");
                    shortestSyncWord = new Text("Кратчайшее синхронизирующее слово: " + "'" + syncAutomaton.shortestSyncWord + "'");
                }
                else {
                    syncWord = new Text("Автомат не синхронизируемый");
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            syncWord.setFont(Font.font("System", 20));
            syncWord.setFill(Color.WHITESMOKE);
            shortestSyncWord.setFont(Font.font("System", 20));
            shortestSyncWord.setFill(Color.WHITESMOKE);
            newPane.getChildren().addAll(syncWord, shortestSyncWord);
            AnchorPane.setLeftAnchor(syncWord, 10.0);
            AnchorPane.setTopAnchor(syncWord, 10.0);
            AnchorPane.setLeftAnchor(shortestSyncWord, 10.0);
            AnchorPane.setTopAnchor(shortestSyncWord, 40.0);

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