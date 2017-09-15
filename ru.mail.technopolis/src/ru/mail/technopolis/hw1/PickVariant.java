package ru.mail.technopolis.hw1;

public class PickVariant {

    private static final String SURNAME = "ANTUFIEV";
    private static final int VARIANTS_COUNT = 4;

    public static void main(String[] args) {
        System.out.println(new PickVariant().hashCode());
    }

    @Override
    public int hashCode() {
        return SURNAME.hashCode() % VARIANTS_COUNT + 1;
    }
}