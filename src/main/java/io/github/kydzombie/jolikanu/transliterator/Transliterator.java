package io.github.kydzombie.jolikanu.transliterator;

import io.github.kydzombie.jolikanu.settings.NasalDiacritic;
import io.github.kydzombie.jolikanu.settings.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

// TODO: null/notnull annotations
public class Transliterator {
    private static final char LIKANU_PROPER_NOUN_LEFT = '‹';
    private static final char LIKANU_PROPER_NOUN_RIGHT = '›';
    private static final char ENDING_NASAL = 'n';

    enum Consonant {
        P('p', 'ʜ'),
        T('t', 'ʌ'),
        K('k', 'x'),
        W('w', 'ɕ'),
        L('l', 'ʋ'),
        J('j', 'ɂ'),
        M('m', 'ɞ'),
        N('n', 'ƨ'),
        S('s', 'ɤ'),
        C('c', 'ɛ'),
        H('h', 'ɵ'),
        NONE('\0', 'o');

        public final char latin;
        public final char likanu;

        private static final HashMap<Character, Consonant> LATIN = new HashMap<>();
        private static final HashMap<Character, Consonant> LIKANU = new HashMap<>();

        Consonant(char latin, char likanu) {
            this.latin = latin;
            this.likanu = likanu;
        }

        static {
            for (Consonant consonant : values()) {
                LATIN.put(consonant.latin, consonant);
                LIKANU.put(consonant.likanu, consonant);
            }
        }

        public static Optional<Consonant> fromLatin(char c) {
            if (LATIN.containsKey(c)) {
                return Optional.of(LATIN.get(c));
            } else {
                Optional<Vowel> vowel = Vowel.fromLatin(c);
                if (vowel.isPresent()) {
                    return Optional.of(NONE);
                } else {
                    return Optional.empty();
                }
            }
        }

        public static Optional<Consonant> fromLikanu(char c) {
            if (LIKANU.containsKey(c)) {
                return Optional.of(LIKANU.get(c));
            } else {
                return Optional.empty();
            }
        }
    }

    enum Vowel {
        A('a', '\0'),
        E('e', 'ȷ'),
        I('i', 'ı'),
        O('o', 'ʃ'),
        U('u', 'ſ');

        public final char latin;
        public final char likanu;

        private static final HashMap<Character, Vowel> LATIN = new HashMap<>();
        private static final HashMap<Character, Vowel> LIKANU = new HashMap<>();

        Vowel(char latin, char likanu) {
            this.latin = latin;
            this.likanu = likanu;
        }

        static {
            for (Vowel vowel : values()) {
                LATIN.put(vowel.latin, vowel);
                LIKANU.put(vowel.likanu, vowel);
            }
        }

        public static Optional<Vowel> fromLatin(char c) {
            return Optional.ofNullable(LATIN.get(c));
        }

        public static Optional<Vowel> fromLikanu(char c) {
            if (LIKANU.containsKey(c)) {
                return Optional.of(LIKANU.get(c));
            } else {
                return Optional.empty();
            }
        }
    }

    interface Token {
        void appendLikanuTo(StringBuilder builder);

        void appendLatinTo(StringBuilder builder);
    }

    // TODO: Are quotes a nested token?

    enum LiteralType {
        SPACE(' ', ' '),
        FULL_STOP('.', ':'),
        BREAK(',', '､'),
        EXCLAMATION('!', 'ʭ'),
        QUESTION('?', '≈');

        public final char latin;
        public final char likanu;

        private static final HashMap<Character, LiteralType> LATIN = new HashMap<>();
        private static final HashMap<Character, LiteralType> LIKANU = new HashMap<>();

        LiteralType(char latin, char likanu) {
            this.latin = latin;
            this.likanu = likanu;
        }


        static {
            for (LiteralType type : values()) {
                LATIN.put(type.latin, type);
                LIKANU.put(type.likanu, type);
            }
        }

        public static Optional<LiteralType> fromLatin(char c) {
            return Optional.ofNullable(LATIN.get(c));
        }

        public static Optional<LiteralType> fromLikanu(char c) {
            return Optional.ofNullable(LIKANU.get(c));
        }
    }

    record LiteralToken(LiteralType literalType) implements Token {
        public void appendLikanuTo(StringBuilder builder) {
            if (literalType == LiteralType.BREAK) {
                if (Settings.convertCommas) {
                    builder.append(literalType.likanu);
                } else {
                    builder.append(literalType.latin);
                }
            } else {
                builder.append(literalType.likanu);
            }
        }

        @Override
        public void appendLatinTo(StringBuilder builder) {
            builder.append(literalType.latin);
        }
    }

    record UnknownToken(char value) implements Token {
        @Override
        public void appendLikanuTo(StringBuilder builder) {
            builder.append(value);
        }

        @Override
        public void appendLatinTo(StringBuilder builder) {
            builder.append(value);
        }
    }

    interface WordComponent {
    }

    // TODO: Figure out a better way to do this,
    //  and if I can forward the issue to the user
    record InvalidComponent(char value) implements WordComponent {
    }

    record Syllable(Consonant consonant, Vowel vowel, boolean nasal) implements WordComponent {
    }

    static final class UnparsedWordToken {
        private final StringBuilder builder = new StringBuilder();

        WordToken parseLatin() {
            String word = builder.toString();
            boolean properNoun = Character.isUpperCase(word.charAt(0));

            List<WordComponent> components = new ArrayList<>();

            for (int i = 0; i < word.length(); i++) {
                char c = Character.toLowerCase(word.charAt(i));
                Optional<Consonant> consonant = Consonant.fromLatin(c);
                if (consonant.isEmpty()) {
                    components.add(new InvalidComponent(c));
                    continue;
                }
                Optional<Vowel> vowel;

                // If there is no consonant, that means
                //  that the first letter was a vowel.
                if (consonant.get() == Consonant.NONE) {
                    vowel = Vowel.fromLatin(c);
                } else {
                    // Otherwise, we have to check the next letter.
                    if (i + 1 >= word.length()) {
                        components.add(new InvalidComponent(c));
                        continue;
                    }
                    vowel = Vowel.fromLatin(word.charAt(i + 1));
                    if (vowel.isPresent()) i++;
                }

                if (vowel.isEmpty()) {
                    components.add(new InvalidComponent(c));
                    continue;
                }

                boolean nasal = false;

                // Only check for nasal if we aren't at the end.
                if (i + 1 < word.length()) {
                    if (word.charAt(i + 1) == ENDING_NASAL) {
                        // If the next n is the last letter, it has to be correct.
                        if (i + 2 >= word.length()) {
                            nasal = true;
                            i++;
                        } else {
                            // Otherwise, we have to check
                            //  for a following consonant
                            if (Consonant.fromLatin(word.charAt(i + 2)).orElse(null) != Consonant.NONE) {
                                nasal = true;
                                i++;
                            }
                        }
                    }
                }

                components.add(new Syllable(consonant.get(), vowel.get(), nasal));
            }

            return new WordToken(components, properNoun);
        }

        WordToken parseLikanu() {
            String word = builder.toString();
            List<WordComponent> components = new ArrayList<>();
            // TODO: Refine proper noun detection
            boolean properNoun = word.charAt(0) == LIKANU_PROPER_NOUN_LEFT && word.charAt(word.length() - 1) == LIKANU_PROPER_NOUN_RIGHT;

            for (int i = 0; i < word.length(); i++) {
                if ((i == 0 || i == word.length() - 1) && properNoun) { // Skip first and last
                    continue;
                }

                char consonantChar = word.charAt(i);
                Optional<Consonant> consonant = Consonant.fromLikanu(consonantChar);
                if (consonant.isEmpty()) {
                    components.add(new InvalidComponent(consonantChar));
                    continue;
                }

                boolean nasal = false;
                char vowelChar = '\0';
                if (i + 1 < word.length()) {
                    char nasalChar = word.charAt(i + 1);
                    nasal = NasalDiacritic.isDiacritic(nasalChar);
                    vowelChar = word.charAt(i + 1);

                    if (nasal) {
                        i++;
                        if (i + 1 >= word.length()) {
                            vowelChar = '\0';
                        } else {
                            vowelChar = word.charAt(i + 1);
                        }
                    }
                }

                Optional<Vowel> vowel = vowelChar != '\0' ? Vowel.fromLikanu(vowelChar) : Optional.empty();
                if (vowel.isPresent()) {
                    i++;
                }

                components.add(new Syllable(consonant.get(), vowel.orElse(Vowel.A), nasal));
            }

            return new WordToken(components, properNoun);
        }

        @Override
        public String toString() {
            return "UnparsedWord[" + builder + "]";
        }
    }

    record WordToken(List<WordComponent> inner, boolean properNoun) implements Token {
        public void appendLikanuTo(StringBuilder builder) {
            if (properNoun) builder.append(LIKANU_PROPER_NOUN_LEFT);
            for (WordComponent component : inner) {
                if (component instanceof Syllable(Consonant consonant, Vowel vowel, boolean nasal)) {
                    builder.append(consonant.likanu);
                    if (nasal) builder.append(Settings.diacriticN.diacritic);
                    if (vowel.likanu != '\0') {
                        builder.append(vowel.likanu);
                    }
                } else if (component instanceof InvalidComponent(char value)) {
                    builder.append(value);
                }
            }
            if (properNoun) builder.append(LIKANU_PROPER_NOUN_RIGHT);
        }

        @Override
        public void appendLatinTo(StringBuilder builder) {
            for (int i = 0; i < inner.size(); i++) {
                WordComponent component = inner.get(i);
                if (component instanceof Syllable(Consonant consonant, Vowel vowel, boolean nasal)) {
                    if (i == 0 && properNoun) {
                        builder.append(Character.toUpperCase(consonant.latin));
                    } else {
                        builder.append(consonant.latin);
                    }
                    builder.append(vowel.latin);
                    if (nasal) builder.append(ENDING_NASAL);
                } else if (component instanceof InvalidComponent(char value)) {
                    builder.append(value);
                }
            }
        }
    }

    static List<Token> tokenizeLatin(String latinText) {
        List<Token> tokens = new ArrayList<>();

        // TODO: Figure out a quote stack or something
//        Stack<Token> tokenStack = new Stack<>();

        UnparsedWordToken currentWord = null;

        for (int i = 0; i < latinText.length(); i++) {
            char c = latinText.charAt(i);
            Optional<LiteralType> literalType = LiteralType.fromLatin(c);
            if (literalType.isPresent()) {
                if (currentWord != null) {
                    tokens.add(currentWord.parseLatin());
                    currentWord = null;
                }
                tokens.add(new LiteralToken(literalType.get()));
                continue;
            }

            if (Character.isLetter(c)) {
                // Start a new word if there
                //  is no current word
                if (currentWord == null) {
                    currentWord = new UnparsedWordToken();
                }

                currentWord.builder.append(c);
            } else {
                // Immediately end the current word
                //  because something is wrong
                if (currentWord != null) {
                    tokens.add(currentWord.parseLatin());
                    currentWord = null;
                }
                tokens.add(new UnknownToken(c));
            }
        }

        if (currentWord != null) {
            tokens.add(currentWord.parseLatin());
        }

        return tokens;
    }

    public static String toLikanu(String latinText) {
        List<Token> tokens = tokenizeLatin(latinText);

        StringBuilder builder = new StringBuilder();

        for (Token token : tokens) {
            token.appendLikanuTo(builder);
        }

        return builder.toString();
    }

    static List<Token> tokenizeLikanu(String likanuText) {
        List<Token> tokens = new ArrayList<>();

        // TODO: Figure out a quote stack or something
//        Stack<Token> tokenStack = new Stack<>();

        UnparsedWordToken currentWord = null;

        for (int i = 0; i < likanuText.length(); i++) {
            char c = likanuText.charAt(i);
            Optional<LiteralType> literalType = LiteralType.fromLikanu(c);
            if (literalType.isPresent()) {
                if (currentWord != null) {
                    tokens.add(currentWord.parseLikanu());
                    currentWord = null;
                }
                tokens.add(new LiteralToken(literalType.get()));
                continue;
            }

            // If the found symbol wasn't a consonant OR
            //  a vowel, then the word is invalid. Panic.
            if (Consonant.fromLikanu(c).isEmpty() && Vowel.fromLikanu(c).isEmpty() &&
                    c != LIKANU_PROPER_NOUN_LEFT && c != LIKANU_PROPER_NOUN_RIGHT &&
                    !NasalDiacritic.isDiacritic(c)) {
                if (currentWord != null) {
                    tokens.add(currentWord.parseLikanu());
                    currentWord = null;
                }
                tokens.add(new UnknownToken(c));
                continue;
            }

            if (currentWord == null) {
                currentWord = new UnparsedWordToken();
            }

            currentWord.builder.append(c);
        }

        if (currentWord != null) {
            tokens.add(currentWord.parseLikanu());
        }

        return tokens;
    }

    public static String toLatin(String likanuText) {
        List<Token> tokens = tokenizeLikanu(likanuText);

        StringBuilder builder = new StringBuilder();

        for (Token token : tokens) {
            token.appendLatinTo(builder);
        }

        return builder.toString();
    }
}
