package io.github.kydzombie.jolikanu.settings;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

public class SettingsController {
    @FXML
    CheckBox convertCommas;

    @FXML
    ComboBox<NasalDiacritic> diacriticComboBox;

    @FXML
    private void initialize() {
        diacriticComboBox.getItems().addAll(NasalDiacritic.values());
        diacriticComboBox.setValue(NasalDiacritic.MACRON);
    }

    @FXML
    private void setConvertCommas() {
        Settings.convertCommas = convertCommas.selectedProperty().getValue();
    }

    @FXML
    private void updateMacronSetting() {
        Settings.diacriticN = diacriticComboBox.getValue();
    }
}
