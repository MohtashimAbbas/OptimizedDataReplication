package lexicalproject;
import java.awt.Font;
import java.util.List;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

/**
 *
 * @author Mohtashim's
 */

public class BarChartCost extends JFrame {
   
    
    public DefaultCategoryDataset initUI(DefaultCategoryDataset dataset) {
                     
        JFreeChart barChart = ChartFactory.createBarChart(
            "Cost Analysis",           
            "Categories",            
            "Cost Value",            
            dataset,          
            PlotOrientation.VERTICAL,           
            true, true, false);
        barChart.setTitle(new TextTitle("Cost Analysis", new Font("Serif", java.awt.Font.BOLD, 16)));
        ChartPanel chartPanel = new ChartPanel(barChart);        
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));        
        setContentPane(chartPanel); 
        
        return dataset;
        
    }
        
   
    public static void main( String[ ] args ) {

    }   
}