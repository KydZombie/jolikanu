package io.github.kydzombie.jolikanu.dictionary;

public record GrammarWord(String latin, String type, String definition) implements Word {
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
        return definition;
    }
}
