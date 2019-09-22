/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicalproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;   
import javax.swing.WindowConstants;  
import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;     
import org.jfree.data.xy.DefaultXYZDataset;


/**
 *
 * @author Mohtashim's
 */
public class ScatterPlot2 extends JFrame {   
    public JFreeChart chart;
    public DefaultXYZDataset initUI (List<Double> availabilityList, List<Double> costList, DefaultXYZDataset xyzDataset) {  
        //super("Pareto Front");  

        // Create chart  
        chart = ChartFactory.createScatterPlot(  
            "Pareto front",   
            "Availability", "Cost", xyzDataset);    

        // Create Panel  
        ChartPanel panel = new ChartPanel(chart);  
        setContentPane(panel);  
        
        // Create dataset  
        xyzDataset = createDataset(availabilityList, costList, xyzDataset);  
        
        return xyzDataset;
    }  
  
    private DefaultXYZDataset createDataset(List<Double> availabilityList, List<Double> costList, DefaultXYZDataset xyzDataset) {  
        
        int size = availabilityList.size();
        double x;
        double y;
        double z;
        
         for(int i=0; i<size; i++){
            
            //series1.add(Math.round(availabilityList.get(i)*1000)/1000.0, Math.round(costList.get(i)*1000)/1000.0);
            x = (availabilityList.get(i)*1000)/1000.0;
            y = (costList.get(i)*1000)/1000.0;
            z = (double)i+1;
            
            xyzDataset.addSeries("s"+(i+1), new double[][] {{x}, {y}, {z}}); 
        }
                     
        return xyzDataset;  
    }  
  
    
    public static void main(String[] args) {  
      
        List<Double> availabilityList = new ArrayList<>(Arrays.asList(0.7, 0.6, 0.4, 0.9)); 
        List<Double> costList = new ArrayList<>(Arrays.asList(13.8, 9.3, 8.2, 12.7));
        DefaultXYZDataset xyzDataset = new DefaultXYZDataset();
        
        ScatterPlot2 pareto = new ScatterPlot2();  
        pareto.initUI(availabilityList, costList, xyzDataset);
        pareto.setSize(800, 400);  
        pareto.setLocationRelativeTo(null);  
        pareto.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  
        pareto.setVisible(true);   
    
    } 
}