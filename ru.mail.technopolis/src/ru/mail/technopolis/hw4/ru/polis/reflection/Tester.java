package ru.mail.technopolis.hw4.ru.polis.reflection;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import ru.mail.technopolis.hw4.ru.polis.reflection.tests.ReflectionTests;

public class Tester {

    public static final String CLASS_PROPERTY = "class";

    private void run(String[] args) {
        if (args.length != 1) {
            printUsage();
        }

        final Class<?> token = ReflectionTests.class;
        System.setProperty(CLASS_PROPERTY, args[0]);
        final Result result = new JUnitCore().run(token);
        if (!result.wasSuccessful()) {
            for (final Failure failure : result.getFailures()) {
                System.err.println("Test " + failure.getDescription().getMethodName() + " failed: " + failure.getMessage());
                if (failure.getException() != null) {
                    failure.getException().printStackTrace();
                }
            }
            System.exit(1);
        } else {
            System.out.println("================================================================================================================");
            System.out.println("Everything OK in tests " + token.getSimpleName() + " for " + args[0]);
        }
    }

    private void printUsage() {
        System.out.println("Usage:");
        System.out.println(String.format("java %s full.class.name", getClass().getName()));
        System.exit(1);
    }

    public static void main(String[] args) {
        new Tester().run(args);
    }


}
