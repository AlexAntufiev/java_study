import java.util.ArrayList;
import java.util.List;

public class CPS {
    private static final double EPS = 1e-5;
    private static final int NUMBER_OF_ITERATIONS = 1000;
    private static final int STOP = 2;
    int iterations = 1;
    private List<Double> numbers = new ArrayList<>();
    private List<Double> xPoints = new ArrayList<>();
    private List<Double> yPoints = new ArrayList<>();

    CPS() {
        numbers.add(1.0);
        numbers.add(1.0);
        numbers.add(1.0);
        numbers.add(1.0);
        numbers.add(1.0);
    }

    CPS(List<Double> list) {
        numbers.addAll(list);
    }

    List<Double> getX() {
        return xPoints;
    }

    List<Double> getY() {
        return yPoints;
    }

    double f(List<Double> x) {
        return numbers.get(0) * Math.pow(x.get(0), 2)
                + numbers.get(1) * Math.pow(x.get(1), 2)
                + numbers.get(2) * x.get(0) * x.get(1)
                + numbers.get(3) * x.get(0) + numbers.get(4) * x.get(1);
    }

    //сделать как на паре
    private List<Double> goldenSectionOptimize(List<Double> x, int p, double a, double b) {
        List<Double> tmp = new ArrayList<>(x);
        int i;
        double s1, s2, u1, u2, fu1, fu2;
        s1 = (3 - Math.sqrt(5.0)) / 2;
        s2 = (Math.sqrt(5.0) - 1) / 2;
        u1 = a + s1 * (b - a);
        u2 = a + s2 * (b - a);
        tmp.set(p, u1);
        i = tmp.size();
        fu1 = f(tmp);
        tmp.set(p, u2);
        fu2 = f(tmp);
        for (i = 1; i <= 100; i++) {
            if (fu1 <= fu2) {
                b = u2;
                u2 = u1;
                fu2 = fu1;
                u1 = a + s1 * (b - a);
                tmp.set(p, u1);
                fu1 = f(tmp);
            } else {
                a = u1;
                u1 = u2;
                fu1 = fu2;
                u2 = a + s2 * (b - a);
                tmp.set(p, u2);
                fu2 = f(tmp);
            }
        }
        tmp.set(p, u1);
        fu1 = f(tmp);
        tmp.set(p, u2);
        fu2 = f(tmp);
        if (fu1 < fu2) {
            tmp.set(p, u1);
        } else {
            tmp.set(p, u2);
        }
        return tmp;
    }

    List<Double> coordinateDescent(List<Double> x0) {
        List<Double> cur_x = new ArrayList<>(x0);
        List<Double> old;
        double s;

        for (iterations = 0; iterations < NUMBER_OF_ITERATIONS; iterations++) {
            old = new ArrayList<>(cur_x);
            for (int i = 0; i < 2; i++) {
                //ищем минимум вдоль i-й координаты
                cur_x = goldenSectionOptimize(cur_x, i, -10, 10);
            }
            xPoints.add(cur_x.get(cur_x.size() - 2));
            yPoints.add(cur_x.get(cur_x.size() - 1));
            /*System.out.print("          " + cur_x.get(cur_x.size() - 2));
            System.out.print("          " + cur_x.get(cur_x.size() - 1));
            System.out.println("                  ");*/
            //выбор критерия останова
            if (STOP == 1) {
                //условие останова 1
                s = 0;
                for (int j = 0; j < old.size(); j++) {
                    s += (old.get(j) - cur_x.get(j)) * (old.get(j) - cur_x.get(j));
                }
                s = Math.sqrt(s);
                if (s < EPS) {
                    return cur_x;
                }
            }

            if (STOP == 2) {
                s = Math.abs(f(cur_x) - f(old));
                if (s < EPS) {
                    return cur_x;
                }
            }
        }
        return cur_x;
    }

    public static void main(String[] args) {
        List<Double> x = new ArrayList<>();
        x.add(10.0);
        x.add(10.0);

        CPS cps = new CPS();
        List<Double> result = cps.coordinateDescent(x);

        for (Double d : cps.getX())
            System.out.println(d);

        for (Double d : cps.getY())
            System.out.println(d);

        System.out.println("Value: " + cps.f(result));
        System.out.println("Point: ");
        for (Double aResult : result) System.out.println(aResult + " ");
        System.out.println("Number of iterations:" + cps.iterations);
    }
}
