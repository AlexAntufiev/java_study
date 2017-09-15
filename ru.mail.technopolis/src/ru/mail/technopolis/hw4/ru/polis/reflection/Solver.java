package ru.mail.technopolis.hw4.ru.polis.reflection;

import org.omg.CORBA_2_3.ORB;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static java.lang.reflect.Modifier.*;

public class Solver implements ReflectionImplementor {

    private static String getModifier(int modifier, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        if (isPublic(modifier)) stringBuilder.append("public ");
        if (isProtected(modifier)) stringBuilder.append("protected ");
        if (isPrivate(modifier)) stringBuilder.append("private ");
        if (isFinal(modifier)) stringBuilder.append("final ");
        if (isStatic(modifier)) stringBuilder.append("static ");
        if (isAbstract(modifier) & i != 1) stringBuilder.append("abstract ");
        if (isStrict(modifier)) stringBuilder.append("strictfp ");
        return stringBuilder.toString();
    }

    private static String getType(Class clazz, Set<String> set) {
        Package pack;
        if (clazz.isArray())
            pack = clazz.getComponentType().getPackage();
        else
            pack = clazz.getPackage();
        StringBuilder stringBuilder = new StringBuilder();
        if (pack != null && !pack.toString().contains("java.lang")) {
            stringBuilder
                    .append("import ")
                    .append(clazz.isArray() ? clazz.getComponentType().getName() : clazz.getName())
                    .append(";\n");
        }
        if (!stringBuilder.toString().equals(""))
            set.add(stringBuilder.toString());
        if (clazz.isArray()) {
            return clazz.getComponentType().getSimpleName().concat("[]");
        }
        return clazz.getSimpleName();
    }

    private static String getReturnType(Class clazz) {
        if (clazz.isPrimitive()) {
            if (clazz.getSimpleName().equals("boolean"))
                return "false";
            if (clazz.getSimpleName().equals("void"))
                return null;
            return "0";
        }
        return "null";
    }

    private static String getParameters(Class[] parameters, Set<String> set) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            stringBuilder
                    .append(getType(parameters[i], set))
                    .append(" args")
                    .append(i)
                    .append(i + 1 != parameters.length ? "," : "");
        }
        return stringBuilder.toString();
    }

    private static String getSuperParameters(int count) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            stringBuilder
                    .append("args")
                    .append(i)
                    .append(i + 1 != count ? "," : "");
        }
        return stringBuilder.toString();
    }

    private static String getMethods(Class clazz, Set<String> setImport, Set<String> setMethods) {
        StringBuilder codeImpl;

        for (Method method : clazz.getDeclaredMethods()) {
            if (isPublic(method.getModifiers()) || isProtected(method.getModifiers())) {
                if (!isInterface(clazz.getModifiers()) && (isFinal(method.getModifiers()) || !isAbstract(method.getModifiers())))
                    continue;
                codeImpl = new StringBuilder();
                codeImpl
                        .append(getModifier(method.getModifiers(), 1))
                        .append(getType(method.getReturnType(), setImport)).append(" ")
                        .append(method.getName()).append("(")
                        .append(getParameters(method.getParameterTypes(), setImport)).append(") {\n\n");
                String type = getReturnType(method.getReturnType());
                if (type != null) {
                    codeImpl
                            .append("return ")
                            .append(getReturnType(method.getReturnType()))
                            .append(";\n");
                }
                codeImpl.append("}\n\n");
                setMethods.add(codeImpl.toString());
            }
        }
        Class parent = clazz.getSuperclass();
        if (parent != null && isAbstract(parent.getModifiers()) || isAbstract(clazz.getModifiers())) {
            if (parent != null && !parent.getName().equals("java.lang.Object"))
                getMethods(parent, setImport, setMethods);
            for (Class cls : clazz.getInterfaces()) {
                getMethods(cls, setImport, setMethods);
            }
        }
        codeImpl = new StringBuilder();
        for (String s : setMethods) {
            codeImpl.append(s);
        }

        return codeImpl.toString();
    }

    @Override
    public void implement(Class<?> token, Path root) throws ReflectionImplementorException {

        String clazz = token.getName();

        if (isFinal(token.getModifiers())) {
            throw new ReflectionImplementorException("THIS OBJECT IS FINAL");
        }
        Constructor[] constructors = token.getDeclaredConstructors();
        if (!isInterface(token.getModifiers())) {
            for (int i = 0; i < constructors.length; i++) {
                if (isPublic(constructors[i].getModifiers()) || isProtected(constructors[i].getModifiers()))
                    break;
                if (i + 1 == constructors.length)
                    throw new ReflectionImplementorException("ALL CONSTRUCTORS ARE PRIVATE");
            }
        }
        StringBuilder codeImpl = new StringBuilder();
        Set<String> setImport = new HashSet<>();
        Set<String> setMethods = new HashSet<>();
        StringBuilder importImpl = new StringBuilder();
        StringBuilder nameImpl = new StringBuilder();

        Package p = token.getPackage();
        importImpl.append("package ").append(p.getName()).append(";\n\n");

        String newClass = token.getSimpleName() + "Impl";

        nameImpl
                .append("\n")
                .append("public class ")
                .append(newClass);
        if (token.isInterface()) {
            nameImpl.append(" implements ");
            nameImpl.append(clazz);

            codeImpl
                    .append("\n\npublic ")
                    .append(token.getSimpleName())
                    .append("Impl")
                    .append("(")
                    .append("){\n")
                    .append("}\n\n");

            codeImpl.append(getMethods(token, setImport, setMethods));

            codeImpl.append("}");

        } else {
            nameImpl.append(" extends ");
            nameImpl.append(clazz);

            for (Field field : token.getFields()) {
                codeImpl
                        .append("\n\n")
                        .append(getModifier(field.getModifiers(), 1))
                        .append(" ")
                        .append(getType(field.getType(), setImport))
                        .append(" ")
                        .append(field.getName())
                        .append("=")
                        .append(getReturnType(field.getType()))
                        .append(";\n");
            }
            codeImpl.append("\n");

            if (constructors.length > 0) {
                for (Constructor c : token.getConstructors()) {
                    Class[] exc = c.getExceptionTypes();
                    codeImpl
                            .append(getModifier(c.getModifiers(), 1))
                            .append(" ")
                            .append(token.getSimpleName())
                            .append("Impl")
                            .append("(")
                            .append(getParameters(c.getParameterTypes(), setImport))
                            .append(")");
                    if (exc.length > 0) {
                        codeImpl.append(" throws ");
                        for (int i = 0; i < exc.length; i++) {
                            codeImpl
                                    .append(exc[i].getName())
                                    .append(i + 1 != exc.length ? ", " : "");
                        }
                    }
                    codeImpl
                            .append(" {\n")
                            .append("super(")
                            .append(getSuperParameters(c.getParameterCount()))
                            .append(");\n")
                            .append("}\n ");
                }
            } else {
                codeImpl
                        .append(token.getSimpleName())
                        .append("Impl")
                        .append("(")
                        .append("){\n")
                        .append("}\n ");
            }

            codeImpl.append("\n");

            codeImpl.append(getMethods(token, setImport, setMethods));

            codeImpl.append("}");
        }
        nameImpl.append(" {");
        for (String s : setImport) {
            importImpl.append(s);
        }

        try {
            String packageName = token.getPackage().getName().replace(".", File.separator);
            String pathToFile = root.toString() + File.separator + packageName;
            boolean success1 = (new File(pathToFile)).mkdirs();
            File resultFile = new File(pathToFile, newClass + ".java");
            boolean success2 = resultFile.createNewFile();
            if (success1 && success2)
                try (FileWriter fileWriter = new FileWriter(resultFile)) {
                    fileWriter.write(importImpl.toString());
                    fileWriter.write(nameImpl.toString());
                    fileWriter.write(codeImpl.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } catch (IOException e) {
            throw new ReflectionImplementorException("Class not found", e);
        }

    }

    public static void main(String[] args) throws ReflectionImplementorException {
        Path path = new File("C:\\Users\\alexa\\Documents\\university\\Java\\technopolis\\HW3\\src\\").toPath();
        new Solver().implement(ORB.class, path);
    }
}