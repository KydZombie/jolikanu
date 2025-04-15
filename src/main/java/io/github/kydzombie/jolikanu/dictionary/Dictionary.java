package io.github.kydzombie.jolikanu.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

// TODO: Make this connect to the link to get it:
//  https://docs.google.com/spreadsheets/d/e/2PACX-1vTVGXFd17kcvfu__zjshqiV3kW360IclOEfEdWda_K6ZCg4TY6nW2Gwn4_bs1yQeFLwrZI1_xEvSuP0/pub?gid=0&single=true&
public class Dictionary {
    public final List<Word> words = new ArrayList<>();

    public Dictionary(File csvFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            reader.lines()
                    .skip(1)
                    .forEachOrdered(line -> {
                        words.add(Word.parse(line));
                    });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
