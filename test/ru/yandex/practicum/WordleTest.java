package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    @Test
    void normalizeTest() {

        String word =
                WordleDictionary.normalize("ЁЖИК ");

        assertEquals("ежик", word);
    }

    @Test
    void compareWordsTest() {

        String result =
                WordleDictionary.compareWords(
                        "герой",
                        "гонец"
                );

        assertEquals("+^-^-", result);
    }
}
