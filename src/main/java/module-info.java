module io.github.kydzombie.jolikanu {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires jdk.jshell;

    opens io.github.kydzombie.jolikanu to javafx.fxml;
    exports io.github.kydzombie.jolikanu;
    exports io.github.kydzombie.jolikanu.transliterator;
    opens io.github.kydzombie.jolikanu.transliterator to javafx.fxml;
    exports io.github.kydzombie.jolikanu.settings;
    opens io.github.kydzombie.jolikanu.settings to javafx.fxml;
}