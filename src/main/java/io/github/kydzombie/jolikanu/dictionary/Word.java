package io.github.kydzombie.jolikanu.dictionary;

import io.github.kydzombie.jolikanu.transliterator.Transliterator;

public sealed interface Word permits ContentWord, GrammarWord {
    /*
     All that a word uses is its latin, since
      the likanu representation is
      customizable, that makes
      it a bit easier
    */
    String getLatin();
    
    default String getLikanu() {
        return Transliterator.toLikanu(getLatin());
    }

    String getType();

    String getDefinition();

    static Word parse(String line) {
        String[] parts = line.split(",");
        String type = parts[2];
        if (type.equals("Grammar") || type.equals("Connector") || type.equals("Preposition")) {
            return new GrammarWord(parts[0], type, parts[3]);
        } else {
            return new ContentWord(parts[0], type, parts[3], parts[4], parts[5], parts[6]);
        }
    }
}
