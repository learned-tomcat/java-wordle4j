package ru.yandex.practicum;

import java.io.Serial;

public class EmptyDictionaryException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public EmptyDictionaryException(String message) {
        super(message);
    }
}