package ru.mail.technopolis.hw4.ru.polis.reflection;

import java.nio.file.Path;

public interface ReflectionImplementor {

    void implement(Class<?> token, Path root) throws ru.mail.technopolis.hw4.ru.polis.reflection.ReflectionImplementorException;

}
