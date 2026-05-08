package ru.yandex.practicum;

import java.util.*;

public class WordleDictionary {

    private final List<String> words;

    public WordleDictionary(List<String> words) {

        this.words = new ArrayList<>(words);
    }

    public List<String> getWords() {

        return Collections.unmodifiableList(words);
    }

    public boolean contains(String word) {

        return words.contains(normalize(word));
    }

    public String getRandomWord() {

        Random random = new Random();

        return words.get(random.nextInt(words.size()));
    }

    public static String normalize(String word) {

        return word.toLowerCase()
                .replace('ё', 'е')
                .trim();
    }

    public static boolean isValidRussianWord(String word) {

        return word.matches("[а-я]{5}");
    }

    public static String compareWords(String answer,
                                      String guess) {

        char[] result = {'-', '-', '-', '-', '-'};

        Map<Character, Integer> letters =
                new HashMap<>();

        for (int i = 0; i < answer.length(); i++) {

            char c = answer.charAt(i);

            letters.put(
                    c,
                    letters.getOrDefault(c, 0) + 1
            );
        }

        for (int i = 0; i < answer.length(); i++) {

            char guessChar = guess.charAt(i);

            if (guessChar == answer.charAt(i)) {

                result[i] = '+';

                letters.put(
                        guessChar,
                        letters.get(guessChar) - 1
                );
            }
        }

        for (int i = 0; i < answer.length(); i++) {

            if (result[i] == '+') {
                continue;
            }

            char guessChar = guess.charAt(i);

            Integer count = letters.get(guessChar);

            if (count != null && count > 0) {

                result[i] = '^';

                letters.put(
                        guessChar,
                        count - 1
                );
            }
        }

        return new String(result);
    }
}
