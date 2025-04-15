package io.github.kydzombie.jolikanu;

import io.github.kydzombie.jolikanu.transliterator.TransliteratorController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class MainController {
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab transliteratorTab;

    @FXML
    @SuppressWarnings("unused")
    private VBox transliterator;

    @FXML
    private TransliteratorController transliteratorController;

    @FXML
    private WebView bookWebView;

    @FXML
    private WebView dictionaryWebView;

    @FXML
    public void initialize() {
        tabPane.getSelectionModel().selectedIndexProperty().addListener(
                (newTabHolder, _, _) -> {
                    int newTabNumber = newTabHolder.getValue().intValue();
                    Tab newTab = tabPane.getTabs().get(newTabNumber);
                    if (newTab == transliteratorTab) {
                        transliteratorController.updateLikanuText();
                    }
                }
        );

        bookWebView.getEngine().load("https://en.kokanu.com/");
        dictionaryWebView.getEngine().load("https://dictionary.kokanu.com/");
    }
}
