package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    private WordleGame game;

    @BeforeEach
    void init() {

        WordleDictionary dictionary =
                new WordleDictionary(
                        List.of(
                                "герой",
                                "кошка",
                                "лампа",
                                "ветка",
                                "домик"
                        )
                );

        game = new WordleGame(
                dictionary,
                6,
                new PrintWriter(System.out)
        );
    }

    @Test
    void moveReducesSteps()
            throws Exception {

        int before =
                game.getRemainingSteps();

        game.makeMove("герой");

        assertEquals(
                before - 1,
                game.getRemainingSteps()
        );
    }

    @Test
    void invalidWordThrowsException() {

        assertThrows(
                InvalidWordException.class,
                () -> game.makeMove("12345")
        );
    }

    @Test
    void unknownWordThrowsException() {

        assertThrows(
                WordNotFoundInDictionaryException.class,
                () -> game.makeMove("арбуз")
        );
    }

    @Test
    void hintShouldReturnWord() {

        String hint = game.getHint();

        assertNotNull(hint);

        assertEquals(5, hint.length());
    }

    @Test
    void historyShouldContainMove()
            throws Exception {

        game.makeMove("герой");

        assertEquals(
                1,
                game.getHistory().size()
        );
    }
}
