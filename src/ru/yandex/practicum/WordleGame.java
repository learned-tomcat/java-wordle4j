package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WordleGame {

    private final WordleDictionary dictionary;
    private final PrintWriter log;
    private final Random random;
    private final String answer;

    private final Set<Character> absentLetters = new HashSet<>();
    private final Set<Character> presentLetters = new HashSet<>();
    private final List<Character> correctPositions = new ArrayList<>();
    private final Set<String> usedHints = new HashSet<>();

    private int steps;
    private boolean win;

    public WordleGame(WordleDictionary dictionary, int steps, PrintWriter log) {
        this(dictionary, steps, log, new Random());
    }

    public WordleGame(
            WordleDictionary dictionary,
            int steps,
            PrintWriter log,
            Random random
    ) {
        validateConstructorArguments(dictionary, steps, log, random);

        this.dictionary = dictionary;
        this.steps = steps;
        this.log = log;
        this.random = random;
        this.answer = dictionary.getRandomWord(random);

        fillCorrectPositions();
        log.println("Слово загадано");
    }

    public WordleGame(
            WordleDictionary dictionary,
            int steps,
            PrintWriter log,
            String answer
    ) {
        validateConstructorArguments(dictionary, steps, log, new Random());

        String normalizedAnswer = WordleDictionary.normalize(answer);

        if (!dictionary.contains(normalizedAnswer)) {
            throw new IllegalArgumentException("Ответ должен быть словом из словаря");
        }

        this.dictionary = dictionary;
        this.steps = steps;
        this.log = log;
        this.random = new Random();
        this.answer = normalizedAnswer;

        fillCorrectPositions();
        log.println("Слово загадано");
    }

    public String makeMove(String input)
            throws InvalidWordException, WordNotFoundInDictionaryException {
        if (isGameOver()) {
            throw new IllegalStateException("Игра уже завершена");
        }

        String word = WordleDictionary.normalize(input);

        validateWord(word);

        steps--;

        String result = WordleDictionary.compareWords(answer, word);
        updateLetterState(word, result);

        if (word.equals(answer)) {
            win = true;
        }

        log.println("Ход выполнен. Результат: " + result);
        log.println("Осталось попыток: " + steps);

        return result;
    }

    public String getHint() {
        if (isGameOver()) {
            return "Игра уже завершена";
        }

        List<String> possibleWords = findPossibleWords();

        if (possibleWords.isEmpty()) {
            log.println("Подсказка не найдена");
            return "Нет подходящих слов";
        }

        String hint = possibleWords.get(random.nextInt(possibleWords.size()));
        usedHints.add(hint);

        log.println("Подсказка выдана");

        return hint;
    }

    private void validateConstructorArguments(
            WordleDictionary dictionary,
            int steps,
            PrintWriter log,
            Random random
    ) {
        if (dictionary == null) {
            throw new IllegalArgumentException("Словарь не должен быть null");
        }

        if (steps <= 0) {
            throw new IllegalArgumentException("Количество попыток должно быть положительным");
        }

        if (log == null) {
            throw new IllegalArgumentException("Лог не должен быть null");
        }

        if (random == null) {
            throw new IllegalArgumentException("Random не должен быть null");
        }
    }

    private void fillCorrectPositions() {
        for (int i = 0; i < WordleDictionary.WORD_LENGTH; i++) {
            correctPositions.add(null);
        }
    }

    private void validateWord(String word)
            throws InvalidWordException, WordNotFoundInDictionaryException {
        if (word.isBlank()) {
            throw new InvalidWordException("Пустой ввод");
        }

        if (word.length() != WordleDictionary.WORD_LENGTH) {
            throw new InvalidWordException("Слово должно содержать 5 букв");
        }

        if (!WordleDictionary.isValidRussianWord(word)) {
            throw new InvalidWordException("Допустимы только русские буквы");
        }

        if (!dictionary.contains(word)) {
            throw new WordNotFoundInDictionaryException("Слова нет в словаре");
        }
    }

    private void updateLetterState(String word, String result) {
        for (int i = 0; i < WordleDictionary.WORD_LENGTH; i++) {
            char letter = word.charAt(i);
            char mark = result.charAt(i);

            if (mark == '+') {
                correctPositions.set(i, letter);
                presentLetters.add(letter);
                absentLetters.remove(letter);
            } else if (mark == '^') {
                presentLetters.add(letter);
                absentLetters.remove(letter);
            } else if (!presentLetters.contains(letter)) {
                absentLetters.add(letter);
            }
        }
    }

    private List<String> findPossibleWords() {
        List<String> possibleWords = new ArrayList<>();

        for (String candidate : dictionary.getWords()) {
            if (!usedHints.contains(candidate) && matchesKnownLetters(candidate)) {
                possibleWords.add(candidate);
            }
        }

        return possibleWords;
    }

    private boolean matchesKnownLetters(String candidate) {
        return doesNotContainAbsentLetters(candidate)
                && containsPresentLetters(candidate)
                && hasCorrectLettersOnKnownPositions(candidate);
    }

    private boolean doesNotContainAbsentLetters(String candidate) {
        for (char letter : absentLetters) {
            if (candidate.indexOf(letter) >= 0) {
                return false;
            }
        }

        return true;
    }

    private boolean containsPresentLetters(String candidate) {
        for (char letter : presentLetters) {
            if (candidate.indexOf(letter) < 0) {
                return false;
            }
        }

        return true;
    }

    private boolean hasCorrectLettersOnKnownPositions(String candidate) {
        for (int i = 0; i < correctPositions.size(); i++) {
            Character expected = correctPositions.get(i);

            if (expected != null && candidate.charAt(i) != expected) {
                return false;
            }
        }

        return true;
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

    public Set<Character> getAbsentLetters() {
        return Collections.unmodifiableSet(absentLetters);
    }

    public Set<Character> getPresentLetters() {
        return Collections.unmodifiableSet(presentLetters);
    }

    public List<Character> getCorrectPositions() {
        return Collections.unmodifiableList(correctPositions);
    }
}