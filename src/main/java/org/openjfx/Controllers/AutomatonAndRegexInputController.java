package org.openjfx.Controllers;

import algorithms.RegExprBuild;
import automat.Automat;
import com.google.common.collect.HashBasedTable;
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
import javafx.scene.control.TextField;
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

    private AnchorPane mainPane;

    private Text regexStatusText;

    private Text inputCorrectnessText;

    private boolean regexStatus = false;

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
                var createAutomatonButton = getCreateAutomatonButton(automatonTableView, startVertexTextField, finalVerticesTextField, states, letters);
                var regexTextField = getTextField("Введите регулярное выражение, '*' - итерация, '+' - конкатенация, две буквы рядом - умножение", 570);
                var automatonHintText = getText("Введите таблицу переходов автомата", Color.WHITESMOKE, Font.font("System", FontPosture.ITALIC, 12));
                var regexHintText = getText("Например, a*b*c* или a + b + c", Color.WHITESMOKE, Font.font("System", FontPosture.ITALIC, 12));
                var checkRegexCorrectnessButton = getCheckRegexCorrectnessButton(mainPane, regexTextField);
                initMainPane(automatonTableView,
                        createAutomatonButton,
                        startVertexTextField,
                        finalVerticesTextField,
                        regexTextField,
                        regexHintText,
                        automatonHintText,
                        checkRegexCorrectnessButton);
                Loader.showStage(new Scene(mainPane), true);
            } catch (NumberFormatException ignored) {
            }
        });
    }

    private TableView<String[]> getAutomatonTableView(ObservableList<String[]> tableData, String[] letters) {
        var automatonTableView = new TableView<String[]>();
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
        automatonTableView.prefWidthProperty().bind(new SimpleIntegerProperty(stateColumnMinWidth + (regularColumnMinWidth + 1) * letters.length));
        automatonTableView.maxWidthProperty().bind(new SimpleIntegerProperty(stateColumnMinWidth + (regularColumnMinWidth + 1) * 10));
        return automatonTableView;
    }

    private Button getCheckRegexCorrectnessButton(AnchorPane mainPane, TextField regexTextField) {
        var checkRegexCorrectnessButton = new Button("Проверить корректность регулярного выражения");
        checkRegexCorrectnessButton.setPrefWidth(300);
        checkRegexCorrectnessButton.setOnAction(event -> {
            var regex = regexTextField.getText();
            var result = RegExprBuild.isCorrect(regex);
            mainPane.getChildren().remove(regexStatusText);
            regexStatusText = new Text();
            if (result) {
                regexStatusText.setText("✓");
                regexStatusText.setFill(Color.GREEN);
                regexStatusText.setFont(Font.font("System", FontPosture.ITALIC, 24));
                AnchorPane.setBottomAnchor(regexStatusText, 45.0);
                regexStatus = true;
            }
            else {
                regexStatusText.setText("×");
                regexStatusText.setFill(Color.RED);
                regexStatusText.setFont(Font.font("System", FontPosture.ITALIC, 30));
                AnchorPane.setBottomAnchor(regexStatusText, 41.0);
                regexStatus = false;
            }
            AnchorPane.setLeftAnchor(regexStatusText, Math.max(regexTextField.getPrefWidth(), regexTextField.getMaxWidth()) + 15.0);
            mainPane.getChildren().add(regexStatusText);
            mainPane.requestLayout();
        });
        return checkRegexCorrectnessButton;
    }

    private void initMainPane(TableView<String[]> automatonTableView,
                              Button createAutomatonButton,
                              TextField startVertexTextField,
                              TextField finalVerticesTextField,
                              TextField regexTextField,
                              Text regexHintText,
                              Text automatonHintText,
                              Button checkRegexCorrectnessButton) {
        mainPane = new AnchorPane(automatonTableView, createAutomatonButton, startVertexTextField, finalVerticesTextField, automatonHintText, regexTextField, regexHintText, checkRegexCorrectnessButton);
        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(automatonTableView, 10.0);
        AnchorPane.setTopAnchor(automatonTableView, 50.0);

        AnchorPane.setTopAnchor(startVertexTextField, 50.0);
        AnchorPane.setLeftAnchor(startVertexTextField, Math.min(automatonTableView.getPrefWidth(), automatonTableView.getMaxWidth()) + 20.0);

        AnchorPane.setTopAnchor(finalVerticesTextField, 80.0);
        AnchorPane.setLeftAnchor(finalVerticesTextField, Math.min(automatonTableView.getPrefWidth(), automatonTableView.getMaxWidth()) + 20.0);

        AnchorPane.setBottomAnchor(createAutomatonButton, 10.0);
        AnchorPane.setRightAnchor(createAutomatonButton, 10.0);

        AnchorPane.setTopAnchor(automatonHintText, 10.0);
        AnchorPane.setLeftAnchor(automatonHintText, 10.0);

        AnchorPane.setBottomAnchor(regexTextField, 45.0);
        AnchorPane.setLeftAnchor(regexTextField, 10.0);

        AnchorPane.setBottomAnchor(regexHintText, 25.0);
        AnchorPane.setLeftAnchor(regexHintText, 10.0);

        AnchorPane.setBottomAnchor(checkRegexCorrectnessButton, 10.0);
        AnchorPane.setLeftAnchor(checkRegexCorrectnessButton, regexTextField.getPrefWidth() + 10 - checkRegexCorrectnessButton.getPrefWidth());
    }

    private Button getCreateAutomatonButton(TableView<String[]> automatonTableView, TextField startVertexTextField, TextField finalVerticesTextField, String[] states, String[] letters) {
        var createAutomatonButton = new Button("Создать автоматы");
        createAutomatonButton.setOnAction(event2 -> {
            //TODO: Выводить какой-то текст о некорректности регулярки;
            if (!regexStatus) {
                return;
            }

            var startVertex = startVertexTextField.getText().strip();
            var finalVertices = Arrays.asList(finalVerticesTextField.getText().split(","));
            var firstAutomatonTable = new String[automatonTableView.getItems().size()][automatonTableView.getColumns().size()];

            var items = automatonTableView.getItems();

            for (int i = 0; i < states.length; i++) {
                states[i] = items.get(i)[0];
            }

            mainPane.getChildren().remove(inputCorrectnessText);
            if (!checkInputCorrectness(startVertex, finalVerticesTextField.getText().split(","), states, automatonTableView)) {
                mainPane.getChildren().add(inputCorrectnessText);
                mainPane.requestLayout();
                return;
            }

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

            var jumpTable = HashBasedTable.<String, String, String>create();

            for (int i = 0; i < firstAutomatonTable.length; i++) {
                for (int j = 1; j < firstAutomatonTable[i].length; j++) {
                    if (Objects.equals(firstAutomatonTable[i][j], ""))
                        continue;
                    jumpTable.put(states[i], letters[j - 1], firstAutomatonTable[i][j]);
                }
            }

            automatonList.add(new Automat(false, jumpTable, startVertex, finalVertices));

            createAutomatonButton.getScene().getWindow().hide();
            Loader.loadFxml("/taskTwo.fxml");
        });
        return createAutomatonButton;
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

    private boolean checkInputCorrectness(String startVertex, String[] finalVertices, String[] states, TableView<String[]> tableView) {
        if (startVertex.equals("") || finalVertices.length == 0 || finalVertices.length == 1 && Objects.equals(finalVertices[0], "")) {
            setupInputCorrectnessText("Неправильно заданы параметры начальной и конечной вершины", tableView);
            return false;
        }

        var statesAsList = Arrays.asList(states);
        if (!statesAsList.contains(startVertex)) {
            setupInputCorrectnessText("Автомат не содержит вершины '" + startVertex + "'", tableView);
            return false;
        }

        for (String finalVertex : finalVertices) {
            if (!statesAsList.contains(finalVertex)) {
                setupInputCorrectnessText("Автомат не содержит вершины '" + finalVertex + "'", tableView);
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

