package org.openjfx.Controllers;

import automat.Automat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.openjfx.App;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    public static final ArrayList<Automat> automatonList = new ArrayList<>();

    @FXML
    private Button authorsButton;

    @FXML
    private Button taskOneButton;

    @FXML
    private Button taskTwoButton;

    @FXML
    void initialize() {
        initAuthorsButton();
        initTaskOneButton();
        initTaskTwoButton();
    }

    @FXML
    private void initTaskOneButton() {
        taskOneButton.setOnAction(event -> {
            taskOneButton.getScene().getWindow().hide();
            automatonList.clear();

            var loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/automatonInput.fxml"));
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

    @FXML
    private void initTaskTwoButton() {
        taskTwoButton.setOnAction(event -> {
            taskTwoButton.getScene().getWindow().hide();
            automatonList.clear();

            var loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/automatonAndRegexInput.fxml"));
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
        });;
    }

    @FXML
    private void initAuthorsButton() {
        authorsButton.setOnAction(event -> {
            authorsButton.getScene().getWindow().hide();

            var loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/authors.fxml"));
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


