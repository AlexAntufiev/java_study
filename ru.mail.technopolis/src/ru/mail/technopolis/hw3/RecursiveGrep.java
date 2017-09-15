package ru.mail.technopolis.hw3;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class RecursiveGrep {

    private void run(String in) throws IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("", "UTF-8"));
        BufferedReader reader = new BufferedReader(new FileReader(in));
        while (reader.ready()) {
            String stringPath = reader.readLine();
            if (stringPath.isEmpty()) {
                continue;
            }
            Path path = Paths.get(stringPath);
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                    FileInputStream fis = new FileInputStream(file.toString());
                    byte[] fileData = new byte[(int) file.toFile().length()];
                    int num = fis.read(fileData);
                    String s = new String(fileData);
                    if (s.contains("Helloworld")) {
                        System.out.println(file + ": " + num + " bytes");
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    public static void main(String[] args) throws IOException {
        String input = args[0];
        new RecursiveGrep().run(input);
    }
}