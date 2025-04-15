package io.github.kydzombie.jolikanu.settings;

public enum WritingSystemPreference {
    LATIN_FIRST("Latin / Likanu"),
    LIKANU_FIRST("Likanu / Latin"),
    ONLY_LATIN("Latin ONLY"),
    ONLY_LIKANU("Likanu ONLY");

    final String description;

    WritingSystemPreference(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
