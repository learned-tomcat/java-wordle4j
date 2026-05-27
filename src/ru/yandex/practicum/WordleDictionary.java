package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class WordleDictionary {

    public static final int WORD_LENGTH = 5;

    private static final String RUSSIAN_WORD_PATTERN = "[а-я]{" + WORD_LENGTH + "}";

    private final List<String> words;
    private final Set<String> wordSet;

    public WordleDictionary(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("Список слов не должен быть пустым");
        }

        this.words = new ArrayList<>(words);
        this.wordSet = new HashSet<>(words);
    }

    public List<String> getWords() {
        return Collections.unmodifiableList(words);
    }

    public boolean contains(String word) {
        return wordSet.contains(normalize(word));
    }

    public String getRandomWord(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("Random не должен быть null");
        }

        return words.get(random.nextInt(words.size()));
    }

    public static String normalize(String word) {
        if (word == null) {
            return "";
        }

        return word.trim()
                .toLowerCase(Locale.ROOT)
                .replace('ё', 'е');
    }

    public static boolean isValidRussianWord(String word) {
        return word != null && word.matches(RUSSIAN_WORD_PATTERN);
    }

    public static String compareWords(String answer, String guess) {
        if (answer == null || guess == null) {
            throw new IllegalArgumentException("Слова не должны быть null");
        }

        if (answer.length() != WORD_LENGTH || guess.length() != WORD_LENGTH) {
            throw new IllegalArgumentException("Слова должны состоять из 5 букв");
        }

        char[] result = {'-', '-', '-', '-', '-'};
        Map<Character, Integer> letters = countLetters(answer);

        markExactMatches(answer, guess, result, letters);
        markPartialMatches(guess, result, letters);

        return new String(result);
    }

    private static Map<Character, Integer> countLetters(String word) {
        Map<Character, Integer> letters = new HashMap<>();

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            letters.put(letter, letters.getOrDefault(letter, 0) + 1);
        }

        return letters;
    }

    private static void markExactMatches(
            String answer,
            String guess,
            char[] result,
            Map<Character, Integer> letters
    ) {
        for (int i = 0; i < answer.length(); i++) {
            char guessChar = guess.charAt(i);

            if (guessChar == answer.charAt(i)) {
                result[i] = '+';
                letters.put(guessChar, letters.get(guessChar) - 1);
            }
        }
    }

    private static void markPartialMatches(
            String guess,
            char[] result,
            Map<Character, Integer> letters
    ) {
        for (int i = 0; i < guess.length(); i++) {
            if (result[i] == '+') {
                continue;
            }

            char guessChar = guess.charAt(i);
            int count = letters.getOrDefault(guessChar, 0);

            if (count > 0) {
                result[i] = '^';
                letters.put(guessChar, count - 1);
            }
        }
    }
}