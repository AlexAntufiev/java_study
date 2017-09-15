import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Reader r = new Reader("Циклический покоординатный спуск");
        r.setVisible(true);
        r.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        r.setSize(800, 550);
        r.setResizable(false);
        r.setLocationRelativeTo(null);
    }
}
