import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.regex.PatternSyntaxException;

class MyJFrame extends JFrame {

    private final String START_PATH = "C:\\";
    private TableOfFiles tableOfFiles;
    private JTable jTable;
    private JButton next, back, paste, delete, createDir, copy;
    private JLabel path;
    private Path copyPath;

    MyJFrame() {
        super("Explorer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1024, 768);

        tableOfFiles = new TableOfFiles();
        jTable = new JTable(tableOfFiles);
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable.getSelectionModel().addListSelectionListener(e -> tableSelectionChanged());

        TableRowSorter<TableModel> sorter2 = new TableRowSorter<>(jTable.getModel());
        jTable.setRowSorter(sorter2);

        JPanel filesPanel = new JPanel();
        filesPanel.setBorder(BorderFactory.createTitledBorder("Файлы"));
        filesPanel.setLayout(new BorderLayout());
        filesPanel.add(new JScrollPane(jTable), BorderLayout.CENTER);

        JPanel pathPanel = new JPanel();
        path = new JLabel(START_PATH);
        pathPanel.add(path);

        next();
        back();
        paste();
        delete();
        createDir();
        copy();

        TableRowSorter<TableModel> sorter1 = new TableRowSorter<>(jTable.getModel());
        jTable.setRowSorter(sorter1);
        final JTextField filterText = new JTextField(10);
        JButton button = new JButton("Найти");


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(back);
        buttonPanel.add(createDir);
        buttonPanel.add(delete);
        buttonPanel.add(copy);
        buttonPanel.add(paste);
        buttonPanel.add(next);
        buttonPanel.add(filterText);
        buttonPanel.add(button);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(pathPanel, BorderLayout.NORTH);
        getContentPane().add(filesPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        File file = new File(START_PATH);
        String newFiles[] = file.list();
        button.addActionListener(e -> {
            String text = filterText.getText();
            if (text.equals("")) {
                sorter1.setRowFilter(null);
            }
            try {
                sorter1.setRowFilter(RowFilter.regexFilter(text));

            } catch (PatternSyntaxException pse) {
                System.err.println("Bad regex pattern");
            }

        });
        if (newFiles != null) {
            for (String newFile : newFiles) {
                File temp = new File("C:\\".concat(newFile));
                tableOfFiles.addFile(temp);
            }
        }
    }

    private void createDir() {
        createDir = new JButton("Создать папку");
        createDir.addActionListener(e -> {
            String str = JOptionPane.showInputDialog("Введите имя папки:");
            File f1 = new File(path.getText() + "\\" + str);
            if (f1.mkdir())
                tableOfFiles.addFile(f1);
        });
        createDir.setEnabled(true);
    }

    private void next() {
        next = new JButton("Перейти");
        next.addActionListener(e -> {
            if (jTable.getSelectedRow() != -1) {
                StringBuilder target = new StringBuilder(path.getText());
                target.append(tableOfFiles.getFile(jTable.getSelectedRow()).getName());
                String newPath = target.toString();
                path.setText(newPath);
                File f1 = new File(newPath);
                String s[] = f1.list();
                tableOfFiles.clearTable();
                back.setEnabled(true);
                StringBuilder stringBuilder = new StringBuilder();
                if (s != null) {
                    for (String value : s) {
                        stringBuilder.append(target).append("\\").append(value);
                        tableOfFiles.addFile(new File(stringBuilder.toString()));
                        stringBuilder = new StringBuilder();
                    }
                }
            }
            next.setEnabled(false);
            back.setEnabled(true);
        });
        next.setEnabled(false);
    }

    private void back() {
        back = new JButton("Назад");
        back.addActionListener(e -> {
            File file = new File(path.getText());
            String stringPath = file.getParent();
            File parentFile = new File(file.getParent());
            String s[] = parentFile.list();
            tableOfFiles.clearTable();
            path.setText(stringPath);
            StringBuilder stringBuilder = new StringBuilder();
            if (s != null) {
                for (String value : s) {
                    stringBuilder.append(path.getText()).append("\\").append(value);
                    tableOfFiles.addFile(new File(stringBuilder.toString()));
                    stringBuilder = new StringBuilder();
                }
            }
            if (path.getText().equals(START_PATH)) {
                back.setEnabled(false);
            } else {
                back.setEnabled(true);
            }
        });
        back.setEnabled(false);
    }

    private void paste() {
        paste = new JButton("Вставить");
        paste.addActionListener(e -> {
            File f1 = copyPath.toFile();
            File f2 = new File(path.getText() + "\\" + f1.getName());
            try {
                if (f1.equals(f2)) {
                    JOptionPane.showMessageDialog(null, "Файл уже содержится в этой папке");

                } else {
                    Files.copy(f1.toPath(), f2.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    tableOfFiles.addFile(f2);
                }
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "Нельзя скопировать файл в выбранную папку");
            }
        });
        paste.setEnabled(false);
    }

    private void copy() {
        copy = new JButton("Копировать");
        copy.addActionListener(e -> {
            if (jTable.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "Файл не выбран");
            } else {
                copyPath = tableOfFiles.getFile(jTable.getSelectedRow()).toPath();
                paste.setEnabled(true);
            }
        });
        copy.setEnabled(false);
    }

    private void delete() {
        delete = new JButton("Удалить");
        delete.addActionListener(e -> {
            File f1 = tableOfFiles.getFile(jTable.getSelectedRow());
            if (f1.delete()) {
                JOptionPane.showMessageDialog(null, "Файл успешно удалён.");
                tableOfFiles.clearFile(jTable.getSelectedRow());
            } else {
                JOptionPane.showMessageDialog(null, "Удалить файл не удалось.");
            }
        });
        delete.setEnabled(false);
    }

    private void tableSelectionChanged() {
        File file = tableOfFiles.getFile(jTable.getSelectedRow());

        if (file != null) {
            if (file.isDirectory()) {
                next.setEnabled(true);
                delete.setEnabled(false);
                copy.setEnabled(false);
                paste.setEnabled(false);

            }
            if (file.isFile()) {
                next.setEnabled(false);
                delete.setEnabled(true);
                copy.setEnabled(true);
            }
        }
    }

}
