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

public class XYLineChartCost extends JFrame {

    public XYSeriesCollection initUI(List<Double> costList, List<Integer> nList) {

        XYSeriesCollection dataset = createDataset(costList, nList);
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

    private XYSeriesCollection createDataset(List<Double> costList, List<Integer> nList) {

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Cost");
        XYSeries series2 = new XYSeries("N");        
        //XYSeries series3 = new XYSeries("writeCost");           
                  

            for(int i=0; i<costList.size(); i++){
                
                series1.add((i+1), Math.round(costList.get(i)*1000)/1000.0);
                series2.add((i+1), Math.round(nList.get(i)*1000)/1000.0);
                //series2.add((i+1), Math.round(writeFitnessList.get(i)*1000)/1000.0);
                
            }
            dataset.addSeries(series1);
            dataset.addSeries(series2);
            //dataset.addSeries(series3);
          
        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Replication Strategies vs. Cost", 
                "Number of Replication Strategies", 
                "Cost", 
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

        chart.setTitle(new TextTitle("Replication Strategies vs. Cost",
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
