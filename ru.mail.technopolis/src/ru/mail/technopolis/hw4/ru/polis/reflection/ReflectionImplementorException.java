package ru.mail.technopolis.hw4.ru.polis.reflection;

public class ReflectionImplementorException extends Exception {

    public ReflectionImplementorException() {
    }

    public ReflectionImplementorException(String message) {
        super(message);
    }

    public ReflectionImplementorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionImplementorException(Throwable cause) {
        super(cause);
    }

    public ReflectionImplementorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
