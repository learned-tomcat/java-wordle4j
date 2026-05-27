package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordleTest {

    @Test
    void compareWordsTest() {
        String result = WordleDictionary.compareWords("герой", "гонец");

        assertEquals("+^-^-", result);
    }
}