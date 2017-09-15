import org.jfree.chart.ChartFactory;

import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;
import java.util.List;


public class LineChart_AWT extends ApplicationFrame {
    private List<Double> xPoints;
    private List<Double> yPoints;
    private JFreeChart lineChart;

    public LineChart_AWT(String applicationTitle, String chartTitle, List<Double> xPoints, List<Double> yPoints) {
        super(applicationTitle);
        this.xPoints = new ArrayList<>(xPoints);
        this.yPoints = new ArrayList<>(yPoints);
        this.lineChart = ChartFactory.createLineChart(
                chartTitle,
                "x", "y",
                createDataSet(),
                PlotOrientation.VERTICAL,
                true, true, false
        );
    }

    JFreeChart getLineChart() {
        return this.lineChart;
    }

    private DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for (int i = 0; i < yPoints.size(); i++) {
            dataSet.addValue(yPoints.get(i), "cps", xPoints.get(i));
        }
        return dataSet;
    }
}