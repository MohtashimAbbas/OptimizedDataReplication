package lexicalproject;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Mohtashim's
 */

public class XYLineChart extends JFrame {

    public XYSeriesCollection initUI(List<Double> fitnessList, List<Double> readFitnessList, List<Double> writeFitnessList, int size) {

        XYSeriesCollection dataset = createDataset(fitnessList, readFitnessList, writeFitnessList, size);
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle("Replication Strategies Analysis");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        return dataset;
    }

    private XYSeriesCollection createDataset(List<Double> fitnessList, List<Double> readFitnessList, List<Double> writeFitnessList, int size) {

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("total fitness");        
        XYSeries series2 = new XYSeries("read availablity");           
        XYSeries series3 = new XYSeries("write availablity");          

            for(int i=0; i<fitnessList.size(); i++){
                
                series1.add((i+1), Math.round(fitnessList.get(i)*1000)/1000.0);
                series2.add((i+1), Math.round(readFitnessList.get(i)*1000)/1000.0);
                series3.add((i+1), Math.round(writeFitnessList.get(i)*1000)/1000.0);
                
            }
            dataset.addSeries(series1);
            dataset.addSeries(series2);
            dataset.addSeries(series3);
          
        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Replication Strategies vs. Fitness", 
                "Number of Replication Strategies", 
                "Fitness Value", 
                dataset, 
                PlotOrientation.VERTICAL,
                true, 
                true, 
                false 
        );

        XYPlot plot = chart.getXYPlot();
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle("Replication Strategies vs. Fitness",
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );

        return chart;
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {
    
    }

}
