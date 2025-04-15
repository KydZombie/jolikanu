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
    ComboBox<WritingSystemPreference> writingSystemPreferenceComboBox;

    @FXML
    private void initialize() {
        convertCommas.setSelected(Settings.convertCommas);

        diacriticComboBox.getItems().addAll(NasalDiacritic.values());
        diacriticComboBox.setValue(Settings.diacriticN);

        writingSystemPreferenceComboBox.getItems().addAll(WritingSystemPreference.values());
        writingSystemPreferenceComboBox.setValue(Settings.writingSystemPreference);
    }

    @FXML
    private void setConvertCommas() {
        Settings.convertCommas = convertCommas.selectedProperty().getValue();
    }

    @FXML
    private void updateMacronSetting() {
        Settings.diacriticN = diacriticComboBox.getValue();
    }

    @FXML
    private void updateWritingSystemSetting() {
        Settings.writingSystemPreference = writingSystemPreferenceComboBox.getValue();
    }
}
