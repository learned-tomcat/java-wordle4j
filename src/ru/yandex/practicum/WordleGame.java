package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {

    private final String answer;

    private int steps;

    private final WordleDictionary dictionary;

    private final PrintWriter log;

    private final Map<String, String> history =
            new LinkedHashMap<>();

    private final Set<String> usedHints =
            new HashSet<>();

    private boolean win;

    public WordleGame(WordleDictionary dictionary,
                      int steps,
                      PrintWriter log) {

        this.dictionary = dictionary;
        this.steps = steps;
        this.log = log;

        this.answer = dictionary.getRandomWord();

        log.println("Загадано слово: " + answer);
    }

    public String makeMove(String input)
            throws InvalidWordException,
            WordNotFoundInDictionaryException {

        String word =
                WordleDictionary.normalize(input);

        validateWord(word);

        if (isGameOver()) {

            throw new RuntimeException(
                    "Игра уже завершена"
            );
        }

        steps--;

        String result =
                WordleDictionary.compareWords(
                        answer,
                        word
                );

        history.put(word, result);

        if (word.equals(answer)) {

            win = true;
        }

        log.println(
                "Ход: "
                        + word
                        + " -> "
                        + result
        );

        return result;
    }

    private void validateWord(String word)
            throws InvalidWordException,
            WordNotFoundInDictionaryException {

        if (word.isBlank()) {

            throw new InvalidWordException(
                    "Пустой ввод"
            );
        }

        if (word.length() != 5) {

            throw new InvalidWordException(
                    "Слово должно содержать 5 букв"
            );
        }

        if (!WordleDictionary
                .isValidRussianWord(word)) {

            throw new InvalidWordException(
                    "Допустимы только русские буквы"
            );
        }

        if (!dictionary.contains(word)) {

            throw new WordNotFoundInDictionaryException(
                    "Слова нет в словаре"
            );
        }
    }

    /*
        УМНАЯ ПОДСКАЗКА
     */
    public String getHint() {

        List<String> possibleWords =
                new ArrayList<>();

        for (String candidate :
                dictionary.getWords()) {

            boolean matches = true;

            for (Map.Entry<String, String> entry :
                    history.entrySet()) {

                String oldGuess = entry.getKey();

                String expectedPattern =
                        entry.getValue();

                String actualPattern =
                        WordleDictionary.compareWords(
                                candidate,
                                oldGuess
                        );

                if (!expectedPattern.equals(
                        actualPattern
                )) {

                    matches = false;
                    break;
                }
            }

            if (matches
                    && !usedHints.contains(candidate)) {

                possibleWords.add(candidate);
            }
        }

        if (possibleWords.isEmpty()) {

            return "Нет подходящих слов";
        }

        Random random = new Random();

        String hint =
                possibleWords.get(
                        random.nextInt(
                                possibleWords.size()
                        )
                );

        usedHints.add(hint);

        return hint;
    }

    public boolean isGameOver() {

        return win || steps <= 0;
    }

    public boolean isWin() {

        return win;
    }

    public int getRemainingSteps() {

        return steps;
    }

    public String getAnswer() {

        return answer;
    }

    public Map<String, String> getHistory() {

        return Collections.unmodifiableMap(history);
    }
}
