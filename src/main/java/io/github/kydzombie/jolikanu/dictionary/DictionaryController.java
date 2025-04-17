package io.github.kydzombie.jolikanu.dictionary;

import io.github.kydzombie.jolikanu.JolikanuApp;
import io.github.kydzombie.jolikanu.settings.Settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.URISyntaxException;

public class DictionaryController {
    @FXML
    TextField kokanuSearchBox;

    @FXML
    TextField englishSearchBox;

    @FXML
    ListView<Word> wordList;

    Dictionary dictionary;

    FilteredList<Word> filteredData;

    @FXML
    Label wordLabel;

    @FXML
    Label typeLabel;

    @FXML
    Text definitionText;

    @FXML
    WebView dictionaryWebView;

    @FXML
    private void initialize() {
        try {
            File file = new File(JolikanuApp.class.getResource("dictionary.csv").toURI());
            System.out.println(file.exists());
            dictionary = new Dictionary(file);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Word> data = FXCollections.observableArrayList();
        data.addAll(dictionary.words);

        filteredData = new FilteredList<>(data, _ -> true);

        wordList.setCellFactory(wordListview -> new ListCell<>() {
            @Override
            protected void updateItem(Word word, boolean empty) {
                super.updateItem(word, empty);
                if (empty || word == null) {
                    setGraphic(null);
                } else {
                    VBox wordBox = new VBox();
                    Label label = new Label(word.getLatin() + " / " + word.getLikanu() + " (" + word.getType() + ")");
                    Text description = new Text(word.getDefinition());
                    description.setWrappingWidth(250);
                    wordBox.getChildren().addAll(label, description);
                    setGraphic(wordBox);
                }
            }
        });

        wordList.setItems(filteredData);
        // TODO: Disable following links
//        dictionaryWebView.getEngine().getLoadWorker().stateProperty()

        /*
        TODO: Should I disable javascript?
            Pretty much all it does is nerf search
            (which is supposed to be disabled anyway),
            and disable the night mode and home buttons
         */
//        dictionaryWebView.getEngine().javaScriptEnabledProperty().set(false);
    }

    @FXML
    private void updateSearch() {
        filteredData.setPredicate(word -> (word.getLatin().contains(kokanuSearchBox.getCharacters()) || word.getLikanu().contains(kokanuSearchBox.getCharacters())) && word.getDefinition().contains(englishSearchBox.getCharacters()));
    }

    @FXML
    private void selectionUpdated() {
        Word selectedWord = wordList.getSelectionModel().getSelectedItem();
        if (selectedWord == null) return;

        wordLabel.setText(switch (Settings.writingSystemPreference) {
            case LATIN_FIRST -> selectedWord.getLatin() + " / " + selectedWord.getLikanu();
            case LIKANU_FIRST -> selectedWord.getLikanu() + " / " + selectedWord.getLatin();
            case ONLY_LATIN -> selectedWord.getLatin();
            case ONLY_LIKANU -> selectedWord.getLikanu();
        });
        typeLabel.setText("(" + selectedWord.getType() + ")");
        definitionText.setText(selectedWord.getDefinition());

        dictionaryWebView.getEngine().load("https://dictionary.kokanu.com/" + selectedWord.getLatin());
    }
}
