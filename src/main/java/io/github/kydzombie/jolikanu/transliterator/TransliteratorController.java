package io.github.kydzombie.jolikanu.transliterator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class TransliteratorController {
    @FXML
    Label errorLabel;

    @FXML
    TextArea latinTextArea;

    @FXML
    TextArea likanuTextArea;

    @FXML
    private void initialize() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    @FXML
    public void updateLatinText() {
        latinTextArea.setText(Transliterator.toLatin(likanuTextArea.getText()));
    }

    @FXML
    public void updateLikanuText() {
        likanuTextArea.setText(Transliterator.toLikanu(latinTextArea.getText()));
    }
}
