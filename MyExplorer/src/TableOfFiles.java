import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

public class TableOfFiles extends AbstractTableModel {
    private static final String[] columnNames = {"Имя", "Дата изменения", "Тип", "Размер"};
    private List<File> files;

    TableOfFiles() {
        files = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return files.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    void addFile(File file) {
        files.add(file);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    File getFile(int row) {
        if (row < 0 || row > files.size()) {
            return null;
        } else {
            return files.get(row);
        }
    }

    void clearTable() {
        if (files.size() != 0) {
            fireTableRowsDeleted(0, files.size() - 1);
            files.clear();
        }
    }

    void clearFile(int row) {
        files.remove(row);
        fireTableRowsDeleted(row, row);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        File f1 = files.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return f1.getName();
            case 1:
                Date date = new Date(f1.lastModified());
                Formatter fmt = new Formatter();
                fmt.format("%tY.%tm.%td    %tH:%tM:%tS ", date, date, date, date, date, date);
                return fmt;
            case 2:
                if (f1.isFile()) {
                    return "Файл";
                } else {
                    return "Папка";
                }
            case 3:
                if (f1.isFile()) {
                    long size = f1.length();
                    StringBuilder sb = new StringBuilder();
                    long newSize = size >> 10;
                    if (newSize > 0) {
                        size >>= 10;
                        newSize = size >> 10;
                        if (newSize > 0) {
                            size >>= 10;
                            newSize = size >> 10;
                            if (newSize > 0) {
                                size >>= 10;
                                sb.append(size).append(" ГБ");
                                return sb.toString();
                            } else {
                                sb.append(size).append(" МБ");
                                return sb.toString();
                            }
                        } else {
                            sb.append(size).append(" КБ");
                            return sb.toString();
                        }
                    } else {
                        sb.append(size).append(" Б");
                        return sb.toString();
                    }
                }
                return "";
        }
        return "";
    }
}