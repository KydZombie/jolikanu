package io.github.kydzombie.jolikanu.settings;

import java.util.Arrays;

public enum NasalDiacritic {
    MACRON("Macron (Above)", '̄'),
    DOT_ABOVE("Dot (Above)", '̇'),
    TILDE("Tilde (Above)", '̃');

    public final String name;
    public final char diacritic;

    NasalDiacritic(String name, char diacritic) {
        this.name = name;
        this.diacritic = diacritic;
    }

    @Override
    public String toString() {
        return name + " o" + diacritic;
    }

    public static boolean isDiacritic(char c) {
        return Arrays.stream(NasalDiacritic.values()).anyMatch(diacritic -> c == diacritic.diacritic);
    }
}