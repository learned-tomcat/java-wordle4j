package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    @Test
    void normalizeTest() {

        String word =
                WordleDictionary.normalize(" ЁЖИК ");

        assertEquals("ежик", word);
    }

    @Test
    void compareWordsExactMatch() {

        String result =
                WordleDictionary.compareWords(
                        "герой",
                        "герой"
                );

        assertEquals("+++++", result);
    }

    @Test
    void compareWordsPartialMatch() {

        String result =
                WordleDictionary.compareWords(
                        "герой",
                        "гонец"
                );

        assertEquals("+^-^-", result);
    }

    @Test
    void compareWordsRepeatedLetters() {

        String result =
                WordleDictionary.compareWords(
                        "лампа",
                        "алала"
                );

        assertEquals("^^--+", result);
    }

    @Test
    void validRussianWordTest() {

        assertTrue(
                WordleDictionary
                        .isValidRussianWord("кошка")
        );
    }

    @Test
    void invalidRussianWordTest() {

        assertFalse(
                WordleDictionary
                        .isValidRussianWord("hello")
        );
    }
}