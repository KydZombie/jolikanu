package io.github.kydzombie.jolikanu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;

public class JolikanuApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Parent parent = fxmlLoader.load();
        parent.setStyle("-fx-font-family: 'Arial Unicode MS'");
        Scene scene = new Scene(parent, 1280, 800);
        stage.setTitle("jolikanu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
