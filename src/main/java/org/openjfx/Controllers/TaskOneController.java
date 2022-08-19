package org.openjfx.Controllers;

import algorithms.Adduction;
import automat.Automat;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.openjfx.App;

import java.io.IOException;

import static org.openjfx.Controllers.Controller.automatonList;

public class TaskOneController {

    @FXML
    private Button returnToStartButton;

    @FXML
    private Button buildFinalisedAutomatons;

    private TableView<String[]> firstFinalisedAutomatonTableView;

    private TableView<String[]> secondFinalisedAutomatonTableView;

    @FXML
    void initialize() {
        initReturnToStartButton();
        initBuildFinalisedAutomatonButtons();
    }

    private void initBuildFinalisedAutomatonButtons() {
        buildFinalisedAutomatons.setOnAction(event -> {
            var firstFinalisedAutomaton = Adduction.buildAdductedAutomat(Controller.automatonList.get(0));
            var secondFinalisedAutomaton = Adduction.buildAdductedAutomat(Controller.automatonList.get(1));

            buildFinalisedAutomatons.getScene().getWindow().hide();

            var firstAutomatonTableView = createAutomatonJumpTableTableView(firstFinalisedAutomaton);
            var secondAutomatonTableView = createAutomatonJumpTableTableView(secondFinalisedAutomaton);

            var mainPane = new AnchorPane(firstAutomatonTableView, secondAutomatonTableView);

            var firstAutomatonInfo = new Text("Первый автомат");
            firstAutomatonInfo.setFill(Color.WHITESMOKE);
            firstAutomatonInfo.setFont(Font.font("System", 20));

            var secondAutomatonInfo = new Text("Второй автомат");
            secondAutomatonInfo.setFill(Color.WHITESMOKE);
            secondAutomatonInfo.setFont(Font.font("System", 20));

            mainPane.setStyle("-fx-background-color: #2e3348;");

            AnchorPane.setLeftAnchor(firstAutomatonTableView, 10.0);
            AnchorPane.setTopAnchor(firstAutomatonTableView, 50.0);

            AnchorPane.setRightAnchor(secondAutomatonTableView, 10.0);
            AnchorPane.setTopAnchor(secondAutomatonTableView, 50.0);

            AnchorPane.setLeftAnchor(firstAutomatonInfo, 10.0);
            AnchorPane.setTopAnchor(firstAutomatonInfo, 10.0);

            AnchorPane.setLeftAnchor(secondAutomatonInfo, 10.0);
            AnchorPane.setTopAnchor(secondAutomatonInfo, 10.0);


            buildFinalisedAutomatons.getScene().getWindow().hide();

            var scene = new Scene(mainPane);
            Stage stage = new Stage();
            stage.setMaximized(true);
            App.setUpApplicationWindow(stage);
            stage.setScene(scene);
            stage.show();
        });
    }

    private TableView<String[]> createAutomatonJumpTableTableView(Automat automaton) {
        var jumpTable = new String[automaton.vertexes.size()][automaton.letters.size() + 1];
        for (int i = 0; i < automaton.vertexes.size(); i++) {
            for (int j = 0; j < automaton.letters.size() + 1; j++) {
                if (j == 0)
                    jumpTable[i][j] = automaton.vertexes.get(j);
                else {
                    jumpTable[i][j] = automaton.jumpTable.get(automaton.vertexes.get(i), automaton.letters.get(j));
                }
            }
        }

        ObservableList<String[]> data = FXCollections.observableArrayList(jumpTable);
        var automatonTableView = new TableView<String[]>();

        var stateColumnMinWidth = 150;
        var regularColumnMinWidth = 75;

        for (int i = 0; i < automaton.letters.size() + 1; i++) {
            TableColumn<String[], String> tableColumn;
            if (i == 0) {
                tableColumn = new TableColumn<>("Название состояния");
                tableColumn.setMinWidth(stateColumnMinWidth);
                tableColumn.setPrefWidth(stateColumnMinWidth);
                tableColumn.setMaxWidth(stateColumnMinWidth);
            } else {
                tableColumn = new TableColumn<>(automaton.letters.get(i - 1).strip());
                tableColumn.setMinWidth(regularColumnMinWidth);
                tableColumn.setPrefWidth(regularColumnMinWidth);
                tableColumn.setMaxWidth(regularColumnMinWidth);
            }
            automatonTableView.getColumns().add(tableColumn);
            final int columnNumber = i;
            tableColumn.setCellValueFactory(p -> new SimpleStringProperty((p.getValue()[columnNumber])));
            tableColumn.setStyle( "-fx-alignment: CENTER;");
        }

        automatonTableView.setItems(data);

        return automatonTableView;
    }


    private void initReturnToStartButton() {
        returnToStartButton.setOnAction(event -> {
            returnToStartButton.getScene().getWindow().hide();
            automatonList.clear();
            var loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/start.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            App.setUpApplicationWindow(stage);
            stage.show();
        });
    }
}

