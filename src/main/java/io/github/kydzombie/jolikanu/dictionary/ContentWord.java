package io.github.kydzombie.jolikanu.dictionary;

public record ContentWord(String latin, String type, String meaning, String nounForm, String verbForm,
                          String modifierForm) implements Word {
    @Override
    public String getLatin() {
        return latin;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getDefinition() {
        return meaning;
    }
}
