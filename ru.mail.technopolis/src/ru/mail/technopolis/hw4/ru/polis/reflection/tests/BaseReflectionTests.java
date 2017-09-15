package ru.mail.technopolis.hw4.ru.polis.reflection.tests;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import ru.mail.technopolis.hw4.ru.polis.reflection.ReflectionImplementorException;
import ru.mail.technopolis.hw4.ru.polis.reflection.ReflectionImplementor;
import ru.mail.technopolis.hw4.ru.polis.reflection.Tester;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseReflectionTests {

    private String currentMethod;
    @Rule
    public TestWatcher watcher = new TestWatcher() {
        protected void starting(final Description description) {
            currentMethod = description.getMethodName();
            System.out.println("== Running " + description.getMethodName());
        }
    };

    private static Class<?> loadClass() {
        final String className = System.getProperty(Tester.CLASS_PROPERTY);
        Assert.assertTrue("Class name not specified", className != null);

        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T createClass() {
        try {
            return (T) loadClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    void assertConstructor(final Class<?>... ifaces) {
        final Class<?> token = loadClass();
        for (final Class<?> iface : ifaces) {
            Assert.assertTrue(token.getName() + " should implement " + iface.getName() + " interface", iface.isAssignableFrom(token));
        }
        checkConstructor("public default constructor", token);
    }

    private void checkConstructor(final String description, final Class<?> token, final Class<?>... params) {
        try {
            token.getConstructor(params);
        } catch (final NoSuchMethodException e) {
            Assert.fail(token.getName() + " should have " + description);
        }
    }

    void test(final boolean shouldFail, final Class<?>... classes) throws IOException {
        final Path root = getRoot();
        try {
            implement(shouldFail, root, classes);
            if (!shouldFail) {
                compile(root, classes);
                check(root, classes);
            }
        } finally {
            clean(root);
        }
    }

    private void implement(final boolean shouldFail, final Path root, final Class<?>... classes) {
        ReflectionImplementor implementor;
        try {
            implementor = createClass();
        } catch (final Exception e) {
            e.printStackTrace();
            Assert.fail("Instantiation error");
            implementor = null;
        }
        for (final Class<?> clazz : classes) {
            try {
                implement(root, implementor, clazz);

                Assert.assertTrue("You may not implement " + clazz, !shouldFail);
            } catch (final ReflectionImplementorException e) {
                if (shouldFail) {
                    return;
                }
                throw new AssertionError("Error implementing " + clazz, e);
            } catch (final Throwable e) {
                throw new AssertionError("Error implementing " + clazz, e);
            }
            final Path file = getFile(root, clazz);
            Assert.assertTrue("Error implementing clazz: File '" + file + "' not found", Files.exists(file));
        }
    }

    private void implement(final Path root, final ReflectionImplementor implementor, final Class<?> clazz) throws Exception {
        implementor.implement(clazz, root);
    }

    private Path getFile(final Path root, final Class<?> clazz) {
        final String path = clazz.getCanonicalName().replace(".", "/") + "Impl.java";
        return root.resolve(path).toAbsolutePath();
    }

    private void compile(final Path root, final Class<?>... classes) {
        final List<String> files = new ArrayList<>();
        for (final Class<?> token : classes) {
            files.add(getFile(root, token).toString());
        }
        compileFiles(root, files);
    }

    private void compileFiles(final Path root, final List<String> files) {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        Assert.assertNotNull("Could not find java compiler, include tools.jar to classpath", compiler);
        final List<String> args = new ArrayList<>();
        args.addAll(files);
        args.add("-cp");
        args.add(root + File.pathSeparator + System.getProperty("java.class.path"));
        final int exitCode = compiler.run(null, null, null, args.toArray(new String[args.size()]));
        Assert.assertEquals("Compiler exit code", 0, exitCode);
    }

    private void check(final Path root, final Class<?>... classes) {
        final URLClassLoader loader = getClassLoader(root);
        for (final Class<?> token : classes) {
            check(loader, token);
        }
    }

    private static void check(final URLClassLoader loader, final Class<?> token) {
        final String name = token.getCanonicalName() + "Impl";
        try {
            final Class<?> impl = loader.loadClass(name);

            if (token.isInterface()) {
                Assert.assertTrue(name + " should implement " + token, Arrays.asList(impl.getInterfaces()).contains(token));
            } else {
                Assert.assertEquals(name + " should extend " + token, token, impl.getSuperclass());
            }
            Assert.assertFalse(name + " should not be abstract", Modifier.isAbstract(impl.getModifiers()));
            Assert.assertFalse(name + " should not be interface", Modifier.isInterface(impl.getModifiers()));
        } catch (final ClassNotFoundException e) {
            throw new AssertionError("Error loading class " + name, e);
        }
    }

    private static URLClassLoader getClassLoader(final Path root) {
        try {
            return new URLClassLoader(new URL[]{root.toUri().toURL()});
        } catch (final MalformedURLException e) {
            throw new AssertionError(e);
        }
    }

    private Path getRoot() {
        return Paths.get(currentMethod);
    }

    private void clean(final Path root) throws IOException {
        if (!Files.exists(root)) {
            return;
        }
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

}
