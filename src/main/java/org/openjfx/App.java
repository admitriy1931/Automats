package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class App extends Application {
    public void start(Stage primaryStage) throws IOException {
        URL url = App.class.getResource("/start.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        setUpApplicationWindow(primaryStage);
    }

    public static void setUpApplicationWindow(Stage stage) {
        stage.setTitle("Automaton");
        InputStream iconStream = App.class.getResourceAsStream("/steam.png");
        assert iconStream != null;
        Image image = new Image(iconStream);
        stage.getIcons().add(image);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
