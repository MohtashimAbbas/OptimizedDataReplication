/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicalproject;

import java.awt.Color;  
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;   
import javax.swing.WindowConstants;  
import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.plot.XYPlot;  
import org.jfree.data.xy.XYSeries;  
import org.jfree.data.xy.XYSeriesCollection;  




/**
 *
 * @author Mohtashim's
 */
public class ScatterPlot extends JFrame {   
  
    public XYSeriesCollection initUI (List<Double> availabilityList, List<Double> costList) {  
        //super("Pareto Front");  
  
        // Create dataset  
        XYSeriesCollection dataset = createDataset(availabilityList, costList);  

        // Create chart  
        JFreeChart chart = ChartFactory.createScatterPlot(  
            "Pareto front",   
            "Availability", "Cost", dataset);  


        //Changes background color  
        XYPlot plot = (XYPlot)chart.getXYPlot();
        
        plot.setBackgroundPaint(new Color(255,228,196));  


        // Create Panel  
        ChartPanel panel = new ChartPanel(chart);  
        setContentPane(panel);  
        
        return dataset;
    }  
  
    private XYSeriesCollection createDataset(List<Double> availabilityList, List<Double> costList) {  
        
        XYSeriesCollection dataset = new XYSeriesCollection();  
        
        //DefaultXYDataset dataset2 = new DefaultXYDataset(); 
       
        XYSeries series1 = new XYSeries("Data replication strategies");
    
        int size = availabilityList.size();
    
        for(int i=0; i<size; i++){
  
            series1.add(Math.round(availabilityList.get(i)*1000)/1000.0, Math.round(costList.get(i)*1000)/1000.0);
            
            //dataset2.;
        }
              
        dataset.addSeries(series1);  
        return dataset;  
    }  
  
   
    public static void main(String[] args) {  
      
        List<Double> availabilityList = new ArrayList<>(Arrays.asList(0.7, 0.6, 0.4, 0.9)); 
        List<Double> costList = new ArrayList<>(Arrays.asList(13.8, 9.3, 8.2, 12.7));
     
        ScatterPlot pareto = new ScatterPlot();  
        pareto.initUI(availabilityList, costList);
        pareto.setSize(800, 400);  
        pareto.setLocationRelativeTo(null);  
        pareto.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  
        pareto.setVisible(true);  
    
    } 
}