package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    private WordleDictionary dictionary;
    private PrintWriter log;

    @BeforeEach
    void init() {
        dictionary = new WordleDictionary(
                List.of(
                        "герой",
                        "гонец",
                        "кошка",
                        "лампа",
                        "ветка",
                        "домик"
                )
        );

        log = new PrintWriter(System.out);
    }

    @Test
    void moveReducesSteps()
            throws InvalidWordException, WordNotFoundInDictionaryException {
        WordleGame game = new WordleGame(dictionary, 6, log, "герой");

        int before = game.getRemainingSteps();

        game.makeMove("гонец");

        assertEquals(before - 1, game.getRemainingSteps());
    }

    @Test
    void invalidWordThrowsException() {
        WordleGame game = new WordleGame(dictionary, 6, log, "герой");

        assertThrows(InvalidWordException.class, () -> game.makeMove("12345"));
    }

    @Test
    void unknownWordThrowsException() {
        WordleGame game = new WordleGame(dictionary, 6, log, "герой");

        assertThrows(
                WordNotFoundInDictionaryException.class,
                () -> game.makeMove("арбуз")
        );
    }

    @Test
    void hintShouldReturnWord() {
        WordleGame game = new WordleGame(dictionary, 6, log, "герой");

        String hint = game.getHint();

        assertNotNull(hint);
        assertEquals(WordleDictionary.WORD_LENGTH, hint.length());
        assertTrue(dictionary.contains(hint));
    }

    @Test
    void correctLettersShouldBeSaved()
            throws InvalidWordException, WordNotFoundInDictionaryException {
        WordleGame game = new WordleGame(dictionary, 6, log, "герой");

        game.makeMove("гонец");

        assertEquals('г', game.getCorrectPositions().get(0));
    }

    @Test
    void presentLettersShouldBeSaved()
            throws InvalidWordException, WordNotFoundInDictionaryException {
        WordleGame game = new WordleGame(dictionary, 6, log, "герой");

        game.makeMove("гонец");

        assertTrue(game.getPresentLetters().contains('о'));
        assertTrue(game.getPresentLetters().contains('е'));
    }

    @Test
    void absentLettersShouldBeSaved()
            throws InvalidWordException, WordNotFoundInDictionaryException {
        WordleGame game = new WordleGame(dictionary, 6, log, "герой");

        game.makeMove("гонец");

        assertTrue(game.getAbsentLetters().contains('н'));
        assertTrue(game.getAbsentLetters().contains('ц'));
    }

    @Test
    void gameShouldBeWonAfterCorrectAnswer()
            throws InvalidWordException, WordNotFoundInDictionaryException {
        WordleGame game = new WordleGame(dictionary, 6, log, "герой");

        game.makeMove("герой");

        assertTrue(game.isWin());
        assertTrue(game.isGameOver());
    }
}