package ru.yandex.practicum;

import java.io.Serial;

public class InvalidWordException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidWordException(String message) {
        super(message);
    }
}
