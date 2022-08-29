package org.openjfx.Controllers;

import algorithms.GlushkovAlgo;
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
import regexp.RegexpExeption;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.openjfx.Controllers.Controller.automatonList;

public class AutomatonAndRegexInputController {

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

    private AnchorPane mainPane;

    private Text regexStatusText;

    private Text inputCorrectnessText;

    private boolean regexStatus = false;

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

                //TODO: Не забыть убрать рандомное заполнение таблицы
                for (int i = 0; i < states.length; i++) {
                    for (int j = 0; j < alphabet.length + 1; j++) {
                        if (j == 0)
                            jumpTable[i][j] = Integer.toString(i + 1);
                        else {
                            jumpTable[i][j] = Integer.toString(ThreadLocalRandom.current().nextInt(1, states.length + 1));
                        }
                    }
                }

                ObservableList<String[]> data = FXCollections.observableArrayList(jumpTable);

                TableView<String[]> automatonTableView = getAutomatonTableView(data, alphabet);
                TextField startVertexTextField = getTextField("Введите начальное состояние", 325);
                TextField finalVerticesTextField = getTextField("Введите через запятую конечные состояния автомата", 325);
                TextField regexTextField = getTextField("Введите регулярное выражение, '*' - итерация, '+' - объединение, две буквы рядом - конкатенация", 570);
                regexTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("[a-z0-1+*]*")) {
                        regexTextField.setText(newValue.replaceAll("[^a-z0-1+*]", ""));
                    }
                });
                Button createAutomatonButton = getCreateAutomatonButton(automatonTableView, startVertexTextField, finalVerticesTextField, states, alphabet, regexTextField);
                Text automatonHintText = getText("Введите таблицу переходов автомата", Color.WHITESMOKE, Font.font("System", FontPosture.ITALIC, 12));
                Text regexHintText = getText("Например, a*b*c* или a+b+c", Color.WHITESMOKE, Font.font("System", FontPosture.ITALIC, 12));
                Button checkRegexCorrectnessButton = getCheckRegexCorrectnessButton(regexTextField);
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

    private TableView<String[]> getAutomatonTableView(ObservableList<String[]> tableData, String[] alphabet) {
        TableView<String[]> automatonTableView = new TableView<String[]>();
        automatonTableView.setEditable(true);

        int stateColumnMinWidth = 150;
        int regularColumnMinWidth = 75;

        for (int i = 0; i < alphabet.length + 1; i++) {
            TableColumn<String[], String> tableColumn;
            if (i == 0) {
                tableColumn = new TableColumn<>("Название состояния");
                tableColumn.setMinWidth(stateColumnMinWidth);
                tableColumn.setPrefWidth(stateColumnMinWidth);
                tableColumn.setMaxWidth(stateColumnMinWidth);
            } else {
                tableColumn = new TableColumn<>(alphabet[i - 1].strip());
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
        automatonTableView.prefWidthProperty().bind(new SimpleIntegerProperty(stateColumnMinWidth + (regularColumnMinWidth + 1) * alphabet.length));
        automatonTableView.maxWidthProperty().bind(new SimpleIntegerProperty(stateColumnMinWidth + (regularColumnMinWidth + 1) * 10));
        return automatonTableView;
    }

    private Button getCheckRegexCorrectnessButton(TextField regexTextField) {
        Button checkRegexCorrectnessButton = new Button("Проверить корректность регулярного выражения");
        checkRegexCorrectnessButton.setPrefWidth(300);
        checkRegexCorrectnessButton.setOnAction(event -> {
            String regex = regexTextField.getText();
            boolean result = RegExprBuild.isCorrect(regex);
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

    private Button getCreateAutomatonButton(TableView<String[]> automatonTableView, TextField startVertexTextField, TextField finalVerticesTextField, String[] states, String[] alphabet, TextField regexTextField) {
        Button createAutomatonButton = new Button("Создать автоматы");
        createAutomatonButton.setOnAction(event2 -> {
            regexStatus = RegExprBuild.isCorrect(regexTextField.getText());
            if (!regexStatus) {
                mainPane.getChildren().remove(regexStatusText);
                regexStatusText = new Text("Некорректное регулярное выражение");
                regexStatusText.setFont(Font.font("System", FontPosture.ITALIC, 14));
                regexStatusText.setFill(Color.RED);
                AnchorPane.setBottomAnchor(regexStatusText, 48.0);
                AnchorPane.setLeftAnchor(regexStatusText, Math.max(regexTextField.getPrefWidth(), regexTextField.getMaxWidth()) + 20.0);
                mainPane.getChildren().add(regexStatusText);
                return;
            }

            String startVertex = startVertexTextField.getText().strip();
            List<String> finalVertices = Arrays.asList(finalVerticesTextField.getText().split(","));
            String[][] firstAutomatonTable = new String[automatonTableView.getItems().size()][automatonTableView.getColumns().size()];

            ObservableList<String[]> items = automatonTableView.getItems();

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

            HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();

            for (int i = 0; i < firstAutomatonTable.length; i++) {
                for (int j = 1; j < firstAutomatonTable[i].length; j++) {
                    if (Objects.equals(firstAutomatonTable[i][j], ""))
                        continue;
                    jumpTable.put(states[i], alphabet[j - 1], firstAutomatonTable[i][j]);
                }
            }

            automatonList.add(new Automat(false, jumpTable, startVertex, finalVertices));
            try {
                automatonList.add(GlushkovAlgo.doGlushkovAlgo(regexTextField.getText()));
            } catch (RegexpExeption e) {
                e.printStackTrace();
            }

            createAutomatonButton.getScene().getWindow().hide();
            Loader.loadFxml("/taskTwo.fxml", true);
        });
        return createAutomatonButton;
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

