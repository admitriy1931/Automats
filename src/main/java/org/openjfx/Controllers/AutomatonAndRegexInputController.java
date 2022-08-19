package org.openjfx.Controllers;

import automat.Automat;
import com.google.common.collect.HashBasedTable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.openjfx.App;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static org.openjfx.Controllers.Controller.automatonList;

public class AutomatonAndRegexInputController {

    @FXML
    private TextField alphabetSizeField;

    @FXML
    private Button backButton;

    @FXML
    private Button createTableButton;

    @FXML
    private TextField statesCountField;

    @FXML
    void initialize() {
        initBackButton();
        initCreateTableButton();
    }

    private TableView<String[]> automatonTableView;

    private Button createAutomatonButton;

    private TextField startVertexTextField;

    private TextField finalVerticesTextField;

    private TextField regexTextField;

    private Text automatonInfoText;

    private AnchorPane mainPane;

    private String[] states;

    private String[] letters;

    private void initBackButton() {
        backButton.setOnAction(event -> {
            backButton.getScene().getWindow().hide();
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

    private void initCreateTableButton() {
        createTableButton.setOnAction(event -> {
            try {
                backButton.getScene().getWindow().hide();
                states = new String[Integer.parseInt(statesCountField.getText())];
                letters = alphabetSizeField.getText().split(",");

                var jumpTable = new String[states.length][letters.length + 1];

                //TODO: Не забыть убрать рандомное заполнение таблицы
                for (int i = 0; i < states.length; i++) {
                    for (int j = 0; j < letters.length + 1; j++) {
                        if (j == 0)
                            jumpTable[i][j] = Integer.toString(i + 1);
                        else {
                            jumpTable[i][j] = Integer.toString(ThreadLocalRandom.current().nextInt(1, states.length + 1));
                        }
                    }
                }

                ObservableList<String[]> data = FXCollections.observableArrayList(jumpTable);

                initTableView(data, letters);
                initCreateAutomatonButton();
                initStartVertexTextField();
                initFinalVerticesTextField();
                initRegexTextField();
                initAutomatonInfoText();
                initMainPane();

                var scene = new Scene(mainPane);
                Stage stage = new Stage();
                stage.setMaximized(true);
                App.setUpApplicationWindow(stage);
                stage.setScene(scene);
                stage.show();
            } catch (NumberFormatException ignored) {
            }
        });
    }

    private void initAutomatonInfoText() {
        automatonInfoText = new Text("Введите таблицу переходов автомата");
        automatonInfoText.setFill(Color.WHITESMOKE);
        automatonInfoText.setFont(Font.font("System", 20));
    }

    private void initMainPane() {
        mainPane = new AnchorPane(automatonTableView, createAutomatonButton, startVertexTextField, finalVerticesTextField, automatonInfoText, regexTextField);
        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(automatonTableView, 10.0);
        AnchorPane.setTopAnchor(automatonTableView, 50.0);

        AnchorPane.setTopAnchor(startVertexTextField, 10.0);
        AnchorPane.setRightAnchor(startVertexTextField, 10.0);

        AnchorPane.setTopAnchor(finalVerticesTextField, 40.0);
        AnchorPane.setRightAnchor(finalVerticesTextField, 10.0);

        AnchorPane.setBottomAnchor(createAutomatonButton, 10.0);
        AnchorPane.setRightAnchor(createAutomatonButton, 10.0);

        AnchorPane.setTopAnchor(automatonInfoText, 10.0);
        AnchorPane.setLeftAnchor(automatonInfoText, 10.0);

        AnchorPane.setBottomAnchor(regexTextField, 10.0);
        AnchorPane.setLeftAnchor(regexTextField, 10.0);
    }

    private void initRegexTextField() {
        regexTextField = new TextField();
        regexTextField.setPromptText("Введите регулярное выражение");
        regexTextField.setEditable(true);
        regexTextField.setPrefColumnCount(30);
    }

    private void initStartVertexTextField() {
        startVertexTextField = new TextField();
        startVertexTextField.setPromptText("Введите начальное состояние");
        startVertexTextField.setEditable(true);
        startVertexTextField.setPrefColumnCount(27);
    }

    private void initFinalVerticesTextField() {
        finalVerticesTextField = new TextField();
        finalVerticesTextField.setPromptText("Введите через запятую конечные состояния автомата");
        finalVerticesTextField.setEditable(true);
        finalVerticesTextField.setPrefColumnCount(27);
    }

    private void initTableView(ObservableList<String[]> tableData, String[] letters) {
        automatonTableView = new TableView<>();
        automatonTableView.setEditable(true);

        var stateColumnMinWidth = 150;
        var regularColumnMinWidth = 75;

        for (int i = 0; i < letters.length + 1; i++) {
            TableColumn<String[], String> tableColumn;
            if (i == 0) {
                tableColumn = new TableColumn<>("Название состояния");
                tableColumn.setMinWidth(stateColumnMinWidth);
                tableColumn.setPrefWidth(stateColumnMinWidth);
                tableColumn.setMaxWidth(stateColumnMinWidth);
            } else {
                tableColumn = new TableColumn<>(letters[i - 1].strip());
                tableColumn.setMinWidth(regularColumnMinWidth);
                tableColumn.setPrefWidth(regularColumnMinWidth);
                tableColumn.setMaxWidth(regularColumnMinWidth);
            }
            automatonTableView.getColumns().add(tableColumn);
            final int columnNumber = i;
            tableColumn.setCellValueFactory(p -> new SimpleStringProperty((p.getValue()[columnNumber])));
            tableColumn.setStyle( "-fx-alignment: CENTER;");
            tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            tableColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<String[], String> cell) -> {
                        var row = cell.getTablePosition().getRow();
                        var column = cell.getTablePosition().getColumn();
                        tableData.get(row)[column] = cell.getNewValue();
                        automatonTableView.refresh();
                    }
            );
        }

        automatonTableView.setItems(tableData);
    }

    private void initCreateAutomatonButton() {
        createAutomatonButton = new Button("Создать автомат");
        createAutomatonButton.setOnAction(event2 -> {
            var startVertex = startVertexTextField.getText().strip();
            var finalVertices = Arrays.asList(finalVerticesTextField.getText().split(","));
            var firstAutomatonTable = new String[automatonTableView.getItems().size()][automatonTableView.getColumns().size()];

            var items = automatonTableView.getItems();

            for (int i = 0; i < states.length; i++) {
                states[i] = items.get(i)[0];
            }

            // Проверить существование нужного состояния
            if (startVertex.equals("") || finalVertices.size() == 0 || finalVertices.size() == 1 && Objects.equals(finalVertices.get(0), ""))
                return;

            for (int i = 0; i < items.size(); i++) {
                System.arraycopy(items.get(i), 0, firstAutomatonTable[i], 0, automatonTableView.getColumns().size());
            }

            for (int i = 0; i < firstAutomatonTable.length; i++) {
                for (int j = 0; j < firstAutomatonTable[i].length; j++) {
                    firstAutomatonTable[i][j] = firstAutomatonTable[i][j].strip();
                }
            }

            for (int i = 0; i < finalVertices.size(); i++) {
                finalVertices.set(i, finalVertices.get(i).strip());
            }

            var jumpTable = HashBasedTable.<String,String,String>create();

            for (int i = 0; i < firstAutomatonTable.length; i++) {
                for (int j = 1; j < firstAutomatonTable[i].length; j++) {
                    if (Objects.equals(firstAutomatonTable[i][j], ""))
                        continue;
                    jumpTable.put(states[i], letters[j - 1], firstAutomatonTable[i][j]);
                }
            }

            automatonList.add(new Automat(false, jumpTable, startVertex, finalVertices));

            createAutomatonButton.getScene().getWindow().hide();

            var path = "/taskTwo.fxml";;

            var loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(path));
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

