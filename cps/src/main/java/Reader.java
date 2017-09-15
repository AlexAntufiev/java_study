import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Reader extends JFrame {
    private JButton b1, b2;
    private JLabel l9;
    private JLabel l10;
    private JLabel l11;
    private JTextField t1, t2, t3, t4, t5, t6, t7;
    private JPanel p1;
    private List<Double> numbers = new ArrayList<>();
    private List<Double> x = new ArrayList<>();

    Reader(String s) {
        super(s);
        setLayout(new FlowLayout());
        b1 = new JButton("Очистить");
        b2 = new JButton("Посчитать");
        JLabel l1 = new JLabel("Введите значения x1=");
        JLabel l2 = new JLabel("x2=");
        JLabel l3 = new JLabel("Введите функционал f(x1,x2)=");
        JLabel l4 = new JLabel("x1*x1 + ");
        JLabel l5 = new JLabel("x2*x2 + ");
        JLabel l6 = new JLabel("x1*x2 + ");
        JLabel l7 = new JLabel("x1 + ");
        JLabel l8 = new JLabel("x2");
        l9 = new JLabel("");
        l10 = new JLabel("");
        l11 = new JLabel("");
        t1 = new JTextField(2);
        t2 = new JTextField(2);
        t3 = new JTextField(2);
        t4 = new JTextField(2);
        t5 = new JTextField(2);
        t6 = new JTextField(2);
        t7 = new JTextField(2);
        add(l1);
        add(t1);
        add(l2);
        add(t2);
        add(l3);
        add(t3);
        add(l4);
        add(t4);
        add(l5);
        add(t5);
        add(l6);
        add(t6);
        add(l7);
        add(t7);
        add(l8);
        add(b1);
        add(b2);
        add(l9);
        add(l10);
        add(l11);
        eHandler handler = new eHandler();
        b2.addActionListener(handler);
        b1.addActionListener(handler);
    }

    public class eHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getSource() == b2) {
                    x.add(Double.parseDouble(t1.getText()));
                    x.add(Double.parseDouble(t2.getText()));
                    if (!t3.getText().equals(""))
                        numbers.add(Double.parseDouble(t3.getText()));
                    if (!t4.getText().equals(""))
                        numbers.add(Double.parseDouble(t4.getText()));
                    if (!t5.getText().equals(""))
                        numbers.add(Double.parseDouble(t5.getText()));
                    if (!t6.getText().equals(""))
                        numbers.add(Double.parseDouble(t6.getText()));
                    if (!t7.getText().equals(""))
                        numbers.add(Double.parseDouble(t7.getText()));

                    CPS cps;
                    if (numbers.size() == 5)
                        cps = new CPS(numbers);
                    else
                        cps = new CPS();

                    List<Double> result = cps.coordinateDescent(x);

                    l9.setText("Значение: " + String.format("%.6f", cps.f(result)));
                    l10.setText("Точка: (" + String.format("%.6f", result.get(0)) + ", " + String.format("%.4f", result.get(1)) + ")");
                    l11.setText("Количество итераций:" + cps.iterations);
                    LineChart_AWT awt = new LineChart_AWT("CPS", "CPS Points", cps.getX(), cps.getY());
                    p1 = new ChartPanel(awt.getLineChart());
                    add(p1);
                    p1.setVisible(true);
                    x.clear();
                    numbers.clear();
                    result.clear();
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, "Правильно заполните поля!");
                exception.printStackTrace();
            }

            if (e.getSource() == b1) {
                t1.setText(null);
                t2.setText(null);
                t3.setText(null);
                t4.setText(null);
                t5.setText(null);
                t6.setText(null);
                t7.setText(null);
                p1.setVisible(false);
                l9.setText("");
                l10.setText("");
                l11.setText("");
            }
        }
    }
}