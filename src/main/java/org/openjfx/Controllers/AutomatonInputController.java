package org.openjfx.Controllers;

import automat.Automat;
import com.google.common.collect.HashBasedTable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static org.openjfx.Controllers.Controller.automatonList;

public class AutomatonInputController {

    @FXML
    private TextField alphabetSizeField;

    @FXML
    private Button backButton;

    @FXML
    private Button createTableButton;

    @FXML
    private TextField statesCountField;

    private AnchorPane mainPane;

    private Text inputCorrectnessText;

    @FXML
    void initialize() {
        initBackButton();
        initCreateTableButton();
    }

    private void initBackButton() {
        backButton.setOnAction(event -> {
            backButton.getScene().getWindow().hide();
            Loader.loadFxmlStartupPage();
        });
    }

    private void initCreateTableButton() {
        createTableButton.setOnAction(event -> {
            try {
                backButton.getScene().getWindow().hide();
                var states = new String[Integer.parseInt(statesCountField.getText())];
                var letters = alphabetSizeField.getText().split(",");

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

                var automatonTableView = getAutomatonTableView(data, letters);
                var startVertexTextField = getTextField("Введите начальное состояние", 325);
                var finalVerticesTextField = getTextField("Введите через запятую конечные состояния автомата", 325);
                var automatonInfoText = automatonList.size() == 0
                        ? getText("Введите таблицу переходов первого автомата", Color.WHITESMOKE, Font.font("System", 20))
                        : getText("Введите таблицу переходов второго автомата", Color.WHITESMOKE, Font.font("System", 20));
                var createAutomatonButton = getCreateAutomatonButton(
                        startVertexTextField,
                        finalVerticesTextField,
                        automatonTableView,
                        states,
                        letters
                );
                mainPane = getMainPane(automatonTableView, createAutomatonButton, startVertexTextField, finalVerticesTextField, automatonInfoText);
                Loader.showStage(new Scene(mainPane), true);
            } catch (NumberFormatException ignored) {
            }
        });
    }

    private TableView<String[]> getAutomatonTableView(ObservableList<String[]> tableData, String[] letters) {
        var automatonTableView = new TableView<String[]>();
        automatonTableView.setEditable(true);

        var stateColumnWidth = 150;
        var regularColumnWidth = 75;
        int width;

        for (int i = 0; i < letters.length + 1; i++) {
            TableColumn<String[], String> tableColumn;
            if (i == 0) {
                tableColumn = new TableColumn<>("Название состояния");
                width = stateColumnWidth;
            } else {
                tableColumn = new TableColumn<>(letters[i - 1].strip());
                width = regularColumnWidth;
            }
            tableColumn.setMinWidth(width);
            tableColumn.setPrefWidth(width);
            tableColumn.setMaxWidth(width);
            automatonTableView.getColumns().add(tableColumn);
            final int columnNumber = i;
            tableColumn.setCellValueFactory(p -> new SimpleStringProperty((p.getValue()[columnNumber])));
            tableColumn.setStyle("-fx-alignment: CENTER;");
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
        automatonTableView.setFixedCellSize(25);
        automatonTableView.prefHeightProperty().bind(Bindings.size(automatonTableView.getItems()).multiply(automatonTableView.getFixedCellSize()).add(26));
        automatonTableView.maxHeightProperty().bind((new SimpleIntegerProperty(20)).multiply(automatonTableView.getFixedCellSize()).add(26));
        automatonTableView.prefWidthProperty().bind(new SimpleIntegerProperty(150 + 75 * letters.length));
        automatonTableView.maxWidthProperty().bind(new SimpleIntegerProperty(150 + 75 * 10));
        return automatonTableView;
    }

    private Button getCreateAutomatonButton(TextField startVertexTextField, TextField finalVerticesTextField, TableView<String[]> automatonTableView, String[] states, String[] letters) {
        var createAutomatonButton = new Button("Создать автомат");
        createAutomatonButton.setOnAction(event2 -> {
            var automatonJumpTable = new String[automatonTableView.getItems().size()][automatonTableView.getColumns().size()];
            var items = automatonTableView.getItems();
            var startVertex = startVertexTextField.getText().strip();
            var finalVertices = finalVerticesTextField.getText().split(",");

            for (int i = 0; i < states.length; i++) {
                states[i] = items.get(i)[0];
            }

            mainPane.getChildren().remove(inputCorrectnessText);
            if (!checkInputCorrectness(startVertex, finalVertices, states, automatonTableView)) {
                mainPane.getChildren().add(inputCorrectnessText);
                mainPane.requestLayout();
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

            var jumpTable = HashBasedTable.<String, String, String>create();

            for (int i = 0; i < automatonJumpTable.length; i++) {
                for (int j = 1; j < automatonJumpTable[i].length; j++) {
                    jumpTable.put(states[i], letters[j - 1], automatonJumpTable[i][j]);
                }
            }

            automatonList.add(new Automat(false, jumpTable, startVertex, Arrays.asList(finalVertices)));

            createAutomatonButton.getScene().getWindow().hide();

            if (automatonList.size() < 2)
                Loader.loadFxml("/automatonInput.fxml");
            else
                Loader.loadFxml("/taskOne.fxml");
        });
        return createAutomatonButton;
    }

    private AnchorPane getMainPane(TableView<String[]> automatonTableView,
                                   Button createAutomatonButton,
                                   TextField startVertexTextField,
                                   TextField finalVerticesTextField,
                                   Text automatonInfoText) {
        var mainPane = new AnchorPane(automatonTableView, createAutomatonButton, startVertexTextField, finalVerticesTextField, automatonInfoText);
        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(automatonTableView, 10.0);
        AnchorPane.setTopAnchor(automatonTableView, 50.0);

        AnchorPane.setTopAnchor(startVertexTextField, 50.0);
        AnchorPane.setLeftAnchor(startVertexTextField, Math.min(automatonTableView.getPrefWidth(), automatonTableView.getMaxWidth()) + 20.0);

        AnchorPane.setTopAnchor(finalVerticesTextField, 80.0);
        AnchorPane.setLeftAnchor(finalVerticesTextField, Math.min(automatonTableView.getPrefWidth(), automatonTableView.getMaxWidth()) + 20.0);

        AnchorPane.setBottomAnchor(createAutomatonButton, 10.0);
        AnchorPane.setRightAnchor(createAutomatonButton, 10.0);

        AnchorPane.setTopAnchor(automatonInfoText, 10.0);
        AnchorPane.setLeftAnchor(automatonInfoText, 10.0);

        return mainPane;
    }

    private Text getText(String text, Color color, Font font) {
        var text1 = new Text(text);
        text1.setFill(color);
        text1.setFont(font);
        return text1;
    }

    private TextField getTextField(String promptText, int prefWidth) {
        var textField = new TextField();
        textField.setPromptText(promptText);
        textField.setEditable(true);
        textField.setPrefWidth(prefWidth);
        return textField;
    }

    private boolean checkInputCorrectness(String startVertex, String[] finalVertices, String[] states, TableView<String[]> automatonTableView) {
        if (startVertex.equals("") || finalVertices.length == 0 || finalVertices.length == 1 && Objects.equals(finalVertices[0], "")) {
            setupInputCorrectnessText("Неправильно заданы параметры начальной и конечной вершины", automatonTableView);
            return false;
        }

        var statesAsList = Arrays.asList(states);
        if (!statesAsList.contains(startVertex)) {
            setupInputCorrectnessText("Автомат не содержит вершины '" + startVertex + "'", automatonTableView);
            return false;
        }

        for (String finalVertex : finalVertices) {
            if (!statesAsList.contains(finalVertex)) {
                setupInputCorrectnessText("Автомат не содержит вершины '" + finalVertex + "'", automatonTableView);
                return false;
            }
        }
        return true;
    }

    private void setupInputCorrectnessText(String text, TableView<String[]> tableView) {
        inputCorrectnessText = new Text(text);
        inputCorrectnessText.setFill(Color.RED);
        inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 12));
        AnchorPane.setTopAnchor(inputCorrectnessText, 105.0);
        AnchorPane.setLeftAnchor(inputCorrectnessText, Math.min(tableView.getPrefWidth(), tableView.getMaxWidth()) + 20.0);
    }
}

