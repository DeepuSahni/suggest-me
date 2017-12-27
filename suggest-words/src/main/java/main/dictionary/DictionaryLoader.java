package main.dictionary;

import main.util.Error;
import main.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sets up user supplied or default dictionary.
 */
public class DictionaryLoader {
    private static final String DEFAULT_DICTIONARY = "/defaultDictionary.txt";
    private static final String DICTIONARY_OPTION = "dictionary";

    private DictionaryLoader() {
    }

    public static Set<String> getDictionary() {
        return new TreeSet(DictionaryLoader.getDictionaryOrEmpty().collect(Collectors.toSet()));
    }

    public static Stream<String> getDictionaryOrEmpty() {
        try {
            Stream<String>  lines;
            if (Optional.ofNullable(System.getProperty(DICTIONARY_OPTION)).isPresent()) {
                lines = Files.lines(Paths.get(Optional.ofNullable(System.getProperty(DICTIONARY_OPTION)).get()));
            }
            else {
                 lines = new BufferedReader(new InputStreamReader(DictionaryLoader.class.getResourceAsStream(DEFAULT_DICTIONARY))).lines();
            }
                   return  lines.map (word -> word.toUpperCase())
                           .distinct()
                           .map(word -> word.replaceAll(Util.NOT_AN_UPPER_CASE_REGEX, ""))
                           .filter(word -> word.length() > 0);
        } catch (IOException ex) {
            System.out.println(Error.DICTIONARY_NOT_FOUND.getText() + ex.getMessage());
            return Stream.empty();
        }
    }

}
