package ru.yandex.practicum;

import java.io.Serial;

public class DictionaryLoadingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DictionaryLoadingException(String message) {
        super(message);
    }

    public DictionaryLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
