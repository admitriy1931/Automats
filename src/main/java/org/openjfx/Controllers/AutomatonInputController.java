package org.openjfx.Controllers;

import automaton.Automaton;
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

import java.util.*;

import static org.openjfx.Controllers.Controller.automatonList;

public class AutomatonInputController {

    @FXML
    private TextField alphabetField;

    @FXML
    private Button backButton;

    @FXML
    private Button createTableButton;

    @FXML
    private AnchorPane inputWindowMainPane;

    @FXML
    private TextField statesCountField;

    private AnchorPane tableWindowMainPane;

    private Text inputCorrectnessText;

    private Text statesInputCorrectnessText;

    private Text alphabetInputCorrectnessText;

    @FXML
    void initialize() {
        initBackButton();
        initCreateTableButton();
        setupStatesCountField();
        setupAlphabetField();
    }

    private void setupStatesCountField() {
        statesCountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                statesCountField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void setupAlphabetField() {
        alphabetField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-1,]*")) {
                alphabetField.setText(newValue.replaceAll("[^a-zA-Z0-1,]", ""));
            }
        });
    }

    private void initBackButton() {
        backButton.setOnAction(event -> {
            backButton.getScene().getWindow().hide();
            Loader.loadFxmlStartupPage();
        });
    }

    private boolean containsDuplicates(String[] array)
    {
        Set<String> set = new HashSet<>();
        for (String item : array)
        {
            if (set.contains(item)) return true;
            set.add(item);
        }
        return false;
    }

    private void initCreateTableButton() {
        createTableButton.setOnAction(event -> {
            try {
                String[] states = new String[Integer.parseInt(statesCountField.getText())];
                String[] alphabet = alphabetField.getText().split(",");

                inputWindowMainPane.getChildren().remove(inputCorrectnessText);
                inputWindowMainPane.getChildren().remove(alphabetInputCorrectnessText);

                if (states.length == 0) {
                    statesInputCorrectnessText = new Text("В автомате не может быть 0 состояний");
                    statesInputCorrectnessText.setFill(Color.RED);
                    statesInputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 12));
                    AnchorPane.setTopAnchor(statesInputCorrectnessText, 80.0);
                    AnchorPane.setLeftAnchor(statesInputCorrectnessText, 130.0);
                    inputWindowMainPane.getChildren().add(statesInputCorrectnessText);
                    return;
                }

                if (alphabetField.getText().equals("") || (alphabetField.getText().trim().length() == 0)  || containsDuplicates(alphabet)) {
                    if (containsDuplicates(alphabet))
                        alphabetInputCorrectnessText = new Text("Алфавит содержит повторяющиеся элементы");
                    else
                        alphabetInputCorrectnessText = new Text("Алфавит не может быть пустым");
                    alphabetInputCorrectnessText.setFill(Color.RED);
                    alphabetInputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 12));
                    AnchorPane.setTopAnchor(alphabetInputCorrectnessText, 80.0);
                    AnchorPane.setLeftAnchor(alphabetInputCorrectnessText, 130.0);
                    inputWindowMainPane.getChildren().add(alphabetInputCorrectnessText);
                    return;
                }

                backButton.getScene().getWindow().hide();

                String[][] jumpTable = new String[states.length][alphabet.length + 1];

                for (int i = 0; i < states.length; i++) {
                    for (int j = 0; j < alphabet.length + 1; j++) {
                        if (j == 0)
                            jumpTable[i][j] = Integer.toString(i + 1);
                        else {
                            jumpTable[i][j] = "";
                        }
                    }
                }

                ObservableList<String[]> data = FXCollections.observableArrayList(jumpTable);

                TableView<String[]> automatonTableView = getAutomatonTableView(data, alphabet);
                TextField startVertexTextField = getTextField("Введите начальное состояние", 325);
                TextField finalVerticesTextField = getTextField("Введите через запятую конечные состояния автомата", 325);
                Text automatonInfoText = automatonList.size() == 0
                        ? getText("Введите таблицу переходов первого автомата", Color.WHITESMOKE, Font.font("System", 20))
                        : getText("Введите таблицу переходов второго автомата", Color.WHITESMOKE, Font.font("System", 20));
                Button createAutomatonButton = getCreateAutomatonButton(
                        startVertexTextField,
                        finalVerticesTextField,
                        automatonTableView,
                        states,
                        alphabet
                );
                tableWindowMainPane = getMainPane(automatonTableView, createAutomatonButton, startVertexTextField, finalVerticesTextField, automatonInfoText);
                Loader.showStage(new Scene(tableWindowMainPane), true);
            } catch (NumberFormatException ignored) {
            }
        });
    }

    private TableView<String[]> getAutomatonTableView(ObservableList<String[]> tableData, String[] alphabet) {
        TableView<String[]> automatonTableView = new TableView<>();
        automatonTableView.setEditable(true);

        int stateColumnWidth = 150;
        int regularColumnWidth = 75;
        int width;

        for (int i = 0; i < alphabet.length + 1; i++) {
            TableColumn<String[], String> tableColumn;
            if (i == 0) {
                tableColumn = new TableColumn<>("Название состояния");
                width = stateColumnWidth;
            } else {
                tableColumn = new TableColumn<>(alphabet[i - 1].strip());
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
                        int row = cell.getTablePosition().getRow();
                        int column = cell.getTablePosition().getColumn();
                        tableData.get(row)[column] = cell.getNewValue();
                        automatonTableView.refresh();
                    }
            );
        }

        automatonTableView.setItems(tableData);
        automatonTableView.setFixedCellSize(25);
        automatonTableView.prefHeightProperty().bind(Bindings.size(automatonTableView.getItems()).multiply(automatonTableView.getFixedCellSize()).add(26));
        automatonTableView.maxHeightProperty().bind((new SimpleIntegerProperty(20)).multiply(automatonTableView.getFixedCellSize()).add(26));
        automatonTableView.prefWidthProperty().bind(new SimpleIntegerProperty(150 + 75 * alphabet.length));
        automatonTableView.maxWidthProperty().bind(new SimpleIntegerProperty(150 + 75 * 10));
        return automatonTableView;
    }

    private Button getCreateAutomatonButton(TextField startVertexTextField, TextField finalVerticesTextField, TableView<String[]> automatonTableView, String[] states, String[] alphabet) {
        Button button;
        if (automatonList.size() == 0)
            button = new Button("Создать автомат");
        else
            button = new Button("Привести ДКА");
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

            if (automatonList.size() < 2)
                Loader.loadFxml("/automatonInput.fxml", false);
            else
                Loader.loadFxml("/taskOne.fxml", true);
        });
        return button;
    }

    private AnchorPane getMainPane(TableView<String[]> automatonTableView,
                                   Button createAutomatonButton,
                                   TextField startVertexTextField,
                                   TextField finalVerticesTextField,
                                   Text automatonInfoText) {
        AnchorPane mainPane = new AnchorPane(automatonTableView, createAutomatonButton, startVertexTextField, finalVerticesTextField, automatonInfoText);
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
        Text text1 = new Text(text);
        text1.setFill(color);
        text1.setFont(font);
        return text1;
    }

    private TextField getTextField(String promptText, int prefWidth) {
        TextField textField = new TextField();
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

        List<String> statesAsList = Arrays.asList(states);
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

        if (containsDuplicates(finalVertices)) {
            setupInputCorrectnessText("В списке конечных вершин некоторые вершины встречаются больше одного раза", automatonTableView);
            return false;
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

