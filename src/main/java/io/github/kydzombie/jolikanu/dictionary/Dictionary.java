package io.github.kydzombie.jolikanu.dictionary;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    public final List<Word> words = new ArrayList<>();

    public Dictionary(File csvFile) throws FileNotFoundException {
        this(new BufferedReader(new FileReader(csvFile)));
    }

    public Dictionary(URL url) throws IOException {
        this(new BufferedReader(new InputStreamReader(url.openStream())));
    }

    public Dictionary(BufferedReader reader) {
        reader.lines()
                .skip(1)
                .forEachOrdered(line -> words.add(Word.parse(line)));
    }
}
