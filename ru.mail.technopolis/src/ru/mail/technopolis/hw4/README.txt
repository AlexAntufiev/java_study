В текущей папке находятся следующие файлы:
- implementorTests.jar - Java-архив всех откомпилированных файлов, необходимых для тестирования Вашей программы
- папка с названием ru/

В папке ru существует следующая иерархия:
ru/
  polis/
    reflection/
      tests/
        ReflectionTests.java - тесты, которые тестируют вашу программу (сюда можно смотреть если ваша программа фэйлится на одном из тестах)
        BaseReflectionTests.java - вспомогательный код
      Tester.java - класс, который запускает тестирование
      ReflectionImplementor.java - интерфейс, который вам нужно реализовать для тестирования
      ReflectionImplementorException.java - исключение, используемое в ReflectionImplementor.java

Итак, для того чтобы протестировать ваше решение необходимо сделать следующие действия:
1) Написать код, удоволетворяющий требованиям ДЗ№4
2) Реализовать интерфейс ReflectionImplementor с одним единственным методом:
void implement(Class<?> token, Path root) throws ReflectionImplementorException
- Class<?> token - Type token того класса для которого вам нужно сгенерировать имплементацию
- Path root - путь до директорию, в которую вам нужно сохранить результат (Note: вам нужно самим создать новый файл с суффиксом Impl)
3) Положить ваш файл в текущую директорию (или если у файла есть package не забудьте перекопировать все директории)
4) Откомпилировать код (например, если файл содержаший решение ДЗ называется Solver.java, то javac Solver.java)
5) Запустите тестирование следующей командой:
Для Windows: java -cp implementorTests.jar;. ru.polis.reflection.Tester Solver
Для Unix.  : java -cp implementorTests.jar:. ru.polis.reflection.Tester Solver
6) Дальше будет запуск множества различных тестов и, если все хорошо, вы увидите строку
Everything OK in tests ReflectionTests for Solver

Удачи.




==========================================
Типичные ошибки
1) Неправильный путь файла
Если вам дается для реализации некий type token класса, то реализацию вам необходимо сохранить по следующему пути:
(директория root, передаваемый в функции implement) + package исходного класса + название исходного класса + "Impl.java" 
Например, если вызвать функцию implement следующим образом:
implement(Element.class, Paths.get("./firstTest")) то результат будет лежать по пути:
полный_путь_до_текущей_папки/firstTest/javax/xml/bind/ElementImpl.java

Кусок кода, который правильно сохраняет:
 try {
 	String packageName = token.getPackage().getName().replace(".", File.separator);
    String pathToFile = root.toString() + File.separator + packageName;
    boolean success = (new File(pathToFile)).mkdirs();
    File resultFile = new File(pathToFile, className + ".java");
    fileName.createNewFile();
} catch (IOException e) {
    throw new ReflectionImplementorException("Class not found", e);
}