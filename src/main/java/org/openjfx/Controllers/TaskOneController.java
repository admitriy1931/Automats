package org.openjfx.Controllers;

import algorithms.Adduction;
import automat.Automat;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static org.openjfx.Controllers.Controller.automatonList;

public class TaskOneController {

    @FXML
    private Button returnToStartButton;

    @FXML
    private Button buildFinalisedAutomatons;

    @FXML
    void initialize() {
        initReturnToStartButton();
        initBuildFinalisedAutomatonButtons();
    }

    private void initBuildFinalisedAutomatonButtons() {
        buildFinalisedAutomatons.setOnAction(event -> {
            Automat firstFinalisedAutomaton;
            try {
                firstFinalisedAutomaton = Adduction.buildAdductedAutomat(Controller.automatonList.get(0));
            } catch (CloneNotSupportedException e) {
                return;
            }
            Automat secondFinalisedAutomaton;
            try {
                secondFinalisedAutomaton = Adduction.buildAdductedAutomat(Controller.automatonList.get(1));
            } catch (CloneNotSupportedException e) {
                return;
            }

            buildFinalisedAutomatons.getScene().getWindow().hide();

            var firstAutomatonTableView = createAutomatonJumpTableTableView(firstFinalisedAutomaton);
            var secondAutomatonTableView = createAutomatonJumpTableTableView(secondFinalisedAutomaton);

            var firstAutomatonInfo = new Text("Первый автомат");
            firstAutomatonInfo.setFill(Color.WHITESMOKE);
            firstAutomatonInfo.setFont(Font.font("System", 20));

            var secondAutomatonInfo = new Text("Второй автомат");
            secondAutomatonInfo.setFill(Color.WHITESMOKE);
            secondAutomatonInfo.setFont(Font.font("System", 20));

            var checkIfIsomorphicButton = getCheckIfIsomorphicButton(firstFinalisedAutomaton, secondFinalisedAutomaton);

            var mainPane = getMainPane(firstAutomatonTableView, secondAutomatonTableView, firstAutomatonInfo, secondAutomatonInfo, checkIfIsomorphicButton);

            buildFinalisedAutomatons.getScene().getWindow().hide();

            Loader.showStage(new Scene(mainPane), true);
        });
    }

    private AnchorPane getMainPane(TableView<String[]> firstAutomatonTableView, TableView<String[]> secondAutomatonTableView, Text firstAutomatonInfo, Text secondAutomatonInfo, Button checkIfIsomorphicButton) {
        var mainPane = new AnchorPane(firstAutomatonTableView, secondAutomatonTableView, firstAutomatonInfo, secondAutomatonInfo, checkIfIsomorphicButton);

        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(firstAutomatonTableView, 25.0);
        AnchorPane.setTopAnchor(firstAutomatonTableView, 50.0);

        AnchorPane.setRightAnchor(secondAutomatonTableView, 25.0);
        AnchorPane.setTopAnchor(secondAutomatonTableView, 50.0);

        AnchorPane.setLeftAnchor(firstAutomatonInfo, 25.0);
        AnchorPane.setTopAnchor(firstAutomatonInfo, 10.0);

        AnchorPane.setRightAnchor(secondAutomatonInfo, 25.0);
        AnchorPane.setTopAnchor(secondAutomatonInfo, 10.0);

        AnchorPane.setBottomAnchor(checkIfIsomorphicButton, 35.0);
        AnchorPane.setLeftAnchor(checkIfIsomorphicButton, 800.0);
        AnchorPane.setRightAnchor(checkIfIsomorphicButton, 800.0);
        return mainPane;
    }

    private Button getCheckIfIsomorphicButton(Automat firstFinalisedAutomaton, Automat secondFinalisedAutomaton) {
        var checkIfIsomorphicButton = new Button("Проверить автоматы на изоморфность");
        checkIfIsomorphicButton.setFont(Font.font(Font.getDefault().getName(), 14));
        checkIfIsomorphicButton.setOnAction(event -> {
            //TODO: Доделать, когда будет готов изоморфизм
        });
        return checkIfIsomorphicButton;
    }

    private TableView<String[]> createAutomatonJumpTableTableView(Automat automaton) {
        var jumpTable = new String[automaton.jumpTable.rowMap().size()][automaton.letters.size() + 1];

        var arr = automaton.jumpTable.rowKeySet().toArray(new String[0]);
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < automaton.letters.size() + 1; j++) {
                if (j == 0)
                    jumpTable[i][j] = arr[i];
                else
                    jumpTable[i][j] = automaton.jumpTable.get(arr[i], automaton.letters.get(j - 1));
            }
        }

        ObservableList<String[]> data = FXCollections.observableArrayList(jumpTable);
        var automatonTableView = new TableView<String[]>();

        var stateColumnWidth = 150;
        var regularColumnWidth = 75;
        int width;

        for (int i = 0; i < automaton.letters.size() + 1; i++) {
            TableColumn<String[], String> tableColumn;
            if (i == 0) {
                tableColumn = new TableColumn<>("Название состояния");
                width = stateColumnWidth;
            } else {
                tableColumn = new TableColumn<>(automaton.letters.get(i - 1).strip());
                width = regularColumnWidth;
            }
            tableColumn.setMinWidth(width);
            tableColumn.setPrefWidth(width);
            tableColumn.setMaxWidth(width);
            automatonTableView.getColumns().add(tableColumn);
            final int columnNumber = i;
            tableColumn.setCellValueFactory(p -> new SimpleStringProperty((p.getValue()[columnNumber])));
            tableColumn.setStyle( "-fx-alignment: CENTER;");
        }

        automatonTableView.setItems(data);
        automatonTableView.setFixedCellSize(25);
        automatonTableView.prefHeightProperty().bind(Bindings.size(automatonTableView.getItems()).multiply(automatonTableView.getFixedCellSize()).add(26));
        automatonTableView.maxHeightProperty().bind((new SimpleIntegerProperty(20)).multiply(automatonTableView.getFixedCellSize()).add(26));

        return automatonTableView;
    }


    private void initReturnToStartButton() {
        returnToStartButton.setOnAction(event -> {
            returnToStartButton.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxmlStartupPage();
        });
    }
}

