package lexicalproject;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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

public class XYLineChartAll extends JFrame {

    public XYLineChartAll(double P[], List <double[]> yAxisReadList, List <double[]> yAxisWriteList, int size) {

        initUI(P, yAxisReadList, yAxisWriteList, size);
    }

    public XYSeriesCollection initUI(double P[], List <double[]> yAxisReadList, List <double[]> yAxisWriteList, int size) {

        XYSeriesCollection dataset = createDataset(P, yAxisReadList, yAxisWriteList, size);
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle("Availability analysis");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        return dataset;
    }

    private XYSeriesCollection createDataset(double P[], List <double[]> yAxisReadList, List <double[]> yAxisWriteList, int size) {

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = null;        
        XYSeries series2 = null;
        
        for (int i=0; i<size; i++){          
            
            series1 = new XYSeries("Strategy"+(i+1) +"-A(r)");
            series2 = new XYSeries("Strategy"+(i+1) +"-A(w)");           

            for(int j=0; j<P.length; j++){
                
                series1.add(P[j], yAxisReadList.get(i)[j]);
                series2.add(P[j], yAxisWriteList.get(i)[j]);                
            }
                      
            dataset.addSeries(series1);
            dataset.addSeries(series2);
        }      
          
        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Operations VS Node Availability", 
                "Node availability", 
                "Availability of access operations", 
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

        chart.setTitle(new TextTitle("Operations VS Node Availability",
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
