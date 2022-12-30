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

    private SynchronizedAutomaton currentGeneratedSyncAutomaton;

    @FXML
    void initialize() {
        currentGeneratedSyncAutomaton = (SynchronizedAutomaton)automatonList.get(0);
        TableView<String[]> automatonTableView = TaskOneController.createAutomatonJumpTableTableView(currentGeneratedSyncAutomaton.originalSyncAutomaton);

        TableView<String[]> syncedAutomatonTableView = TaskOneController.createAutomatonJumpTableTableView(currentGeneratedSyncAutomaton);

        Text automatonInfo = new Text("Сгенерированный автомат");
        automatonInfo.setFill(Color.WHITESMOKE);
        automatonInfo.setFont(Font.font("System", 20));

        Text syncedAutomatonInfo = new Text("Сгенерированный автомат с добавленными вершинами");
        syncedAutomatonInfo.setFill(Color.WHITESMOKE);
        syncedAutomatonInfo.setFont(Font.font("System", 20));

        Text syncWord = new Text("Синхронизирующее слово: " + "'" + currentGeneratedSyncAutomaton.syncWord + "'");
        syncWord.setFill(Color.WHITESMOKE);
        syncWord.setFont(Font.font("System", 16));

        Text shortestSyncWord = new Text("Кратчайшее синхронизирующее слово: " + "'" + currentGeneratedSyncAutomaton.shortestSyncWord + "'");
        shortestSyncWord.setFill(Color.WHITESMOKE);
        shortestSyncWord.setFont(Font.font("System", 16));

        Button generateAnotherOneButton = getGenerateSyncAutomatonButton();
        Button returnToStartButton = new Button("Вернуться в начало");
        AutomatonInputController.setupButtonAsReturnToStart(returnToStartButton);

        TaskOneController.setupMainPaneForAutomatonsTableViewDisplay(mainPane, automatonTableView, syncedAutomatonTableView, automatonInfo, syncedAutomatonInfo, generateAnotherOneButton);

        mainPane.getChildren().add(returnToStartButton);
        AnchorPane.setLeftAnchor(returnToStartButton, 10.0);
        AnchorPane.setBottomAnchor(returnToStartButton, 10.0);

        mainPane.getChildren().add(syncWord);
        AnchorPane.setTopAnchor(syncWord, 50.0);
        AnchorPane.setLeftAnchor(syncWord, Math.min(automatonTableView.getPrefWidth(), automatonTableView.getMaxWidth()) + 40.0);

        mainPane.getChildren().add(shortestSyncWord);
        AnchorPane.setTopAnchor(shortestSyncWord, 80.0);
        AnchorPane.setLeftAnchor(shortestSyncWord, Math.min(automatonTableView.getPrefWidth(), automatonTableView.getMaxWidth()) + 40.0);
    }

    @FXML
    public Button getGenerateSyncAutomatonButton() {
        Button button = new Button("Сгенерировать еще один автомат с такими же параметрами");
        button.setOnAction(event -> {
            Automaton generatedAutomaton = null;
            try {
                generatedAutomaton = SynchronizedAutomatonsGenerator.synchronizedAutomatonsGenerator(1,
                        currentGeneratedSyncAutomaton.originalSyncAutomaton.vertices.size(),
                        currentGeneratedSyncAutomaton.originalSyncAutomaton.letters.size()).get(0);
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
}
