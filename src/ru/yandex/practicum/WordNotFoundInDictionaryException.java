package ru.yandex.practicum;

import java.io.Serial;

public class WordNotFoundInDictionaryException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public WordNotFoundInDictionaryException(String message) {
        super(message);
    }
}
