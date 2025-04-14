package io.github.kydzombie.jolikanu;

import io.github.kydzombie.jolikanu.transliterator.TransliteratorController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

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
    public void initialize() {
        tabPane.getSelectionModel().selectedIndexProperty().addListener(
                (newTab, _, _) -> {
                    if (tabPane.getTabs().get(newTab.getValue().intValue()) == transliteratorTab) {
                        transliteratorController.updateLikanuText();
                    }
                }
        );
    }
}
