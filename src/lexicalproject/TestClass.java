/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicalproject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static lexicalproject.GeneticP.returnAvailability;
import static lexicalproject.GeneticP.performMutation;
import static lexicalproject.Lexical.buildJSONObject;
import static lexicalproject.Lexical.changeNames;
import static lexicalproject.Lexical.checkIntersectionRWQ;
import static lexicalproject.Lexical.convertIntoJPG;
import static lexicalproject.Lexical.joinSpecifics;
import static lexicalproject.Lexical.makeDBDocument;
import static lexicalproject.Lexical.returnDocuments;
import static lexicalproject.Lexical.returnN;
import org.apache.commons.lang.ArrayUtils;
import org.bson.Document;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Mohtashim's
 */
public class TestClass {
    
    
    public static void main(String[] args) throws IOException, InterruptedException {
            
        ConvertToVoting obj = new ConvertToVoting();
        int maxVPValue[] = {0,0};
        
        List<Document> documents = returnDocuments(null, "pop18");
        List<List<Node>> doubleList = buildJSONObject(documents);         
        Node rootNode;
        int N; 
        int size = doubleList.size();
        //double[] P = {0.8};
        double[] P = {0, 0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.1, 0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19, 0.2, 0.21, 0.22, 0.23, 0.24, 0.25, 0.26, 0.27, 0.28, 0.29, 0.3, 0.31, 0.32, 0.33, 0.34, 0.35, 0.36, 0.37, 0.38, 0.39, 0.4, 0.41, 0.42, 0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5, 0.51, 0.52, 0.53, 0.54, 0.55, 0.56, 0.57, 0.58, 0.59, 0.6, 0.61, 0.62, 0.63, 0.64, 0.65, 0.66, 0.67, 0.68, 0.69, 0.7, 0.71, 0.72, 0.73, 0.74, 0.75, 0.76, 0.77, 0.78, 0.79, 0.8, 0.81, 0.82, 0.83, 0.84, 0.85, 0.86, 0.87, 0.88, 0.89, 0.9, 0.91, 0.92, 0.93, 0.94, 0.95, 0.96, 0.97, 0.98, 0.99, 1 };        
        boolean isIntersection = true;
        
        for(int i=0; i<doubleList.size(); i++){
            rootNode = doubleList.get(i).get(0);
            convertIntoJPG(rootNode, "pop", i+1);
        }
        
        //performMutation(rootNode);
        //convertIntoJPG(rootNode, "sub", 2);
        /*
        List<Node> str1= obj.gridToVoting(0, maxVPValue);
        List <List<Node>> listSrtategies = new ArrayList<>();
        listSrtategies.add(str1);
        convertIntoJPG(listSrtategies.get(0), "sub");  
        */
        
        
        //List<Node> str1= obj.gridToVoting2(15, maxVPValue);
        //List<Node> str1= obj.gridToVoting2(9, maxVPValue);
        //List<Node> str1= obj.mcsToVoting(11, maxVPValue);       
        //List<Node> str1= obj.gridToVoting2(9, 3, 3, maxVPValue);
        //List<Node> str1= obj.mcsToVoting(9, maxVPValue);
        //List<Node> str1= obj.rowaToVoting(3, maxVPValue);
        //List<Node> str1= obj.defineNewVoting(4,4,1);
        //int n1 = returnN(str1.get(0));
        //makeDBDocument(str1.get(0), n1, "zi14");
 
        
        
        //List <List<Node>> listSrtategies = new ArrayList<>();
        //listSrtategies.add(str1);
        //convertIntoJPG(listSrtategies.get(0).get(0), "sub", 0);  

        
 

/*

        //joining stuff
        
        
        // fetch the documents from the database and if the argument is null, fetch all otherwise pass the query as an argument 
        documents = returnDocuments(null, "zi11");
        // parsing of the database graph
        doubleList = buildJSONObject(documents);       
        
        Node fixedRootNode = doubleList.get(1).get(0);
        Node rootNodeStrategy1 = doubleList.get(15).get(0);
        Node rootNodeStrategy2 = doubleList.get(1).get(0);
                
        int[] nodeNumberToReplace = {1};    
        int leafNodeNumber = 0;    
                
        
        joinSpecifics(rootNodeStrategy1, rootNodeStrategy2, fixedRootNode, leafNodeNumber, nodeNumberToReplace);
                
        int maxVPValue1[] = {1,0};        
        // change the names of the rest of the nodes for strategy 2
        changeNames(rootNodeStrategy1, maxVPValue1, new ArrayList<Node>()); 
        
        
        int n = returnN(rootNodeStrategy1);
        System.out.println(n);
        
        
        makeDBDocument(rootNodeStrategy1, n, "zi11"); 
        
        convertIntoJPG(rootNodeStrategy1, "sub", 0); 
        
        System.out.println("isIntersection: "+checkIntersectionRWQ(rootNodeStrategy1));
      


*/



    

        // to calculate and draw availabilites 
        Double[] tempArray;             
        double sumReadAvail;
        double sumWriteAvail;
        double fitness;
        
        List<Double> readFitnessList = new ArrayList<>();;
        List<Double> writeFitnessList = new ArrayList<>();        
        List<Double> fitnessList = new ArrayList<>();

        List <List<Double>> readWriteList; 
        List <double[]> yAxisReadList = new ArrayList<>();
        List <double[]> yAxisWriteList = new ArrayList<>();

                
        XYLineChart lineChartFitness= new XYLineChart(); // draw line graph for the availability analysis
        lineChartFitness.setVisible(true); // to draw the empty graph panel for availability 
        XYSeriesCollection dataset = new XYSeriesCollection(); // availability dataset for graph
        dataset = lineChartFitness.initUI(new ArrayList<Double>(), new ArrayList<Double>(), new ArrayList<Double>(), new Integer (0));
        
        List<List<Node>>selForComparison = new ArrayList<>();
        
        //selForComparison = doubleList;
        selForComparison.add(doubleList.get(6));
        selForComparison.add(doubleList.get(7));
        //selForComparison.add(doubleList.get(1));  
        //selForComparison.add(doubleList.get(2));
        //selForComparison.add(doubleList.get(6));
        //selForComparison.add(doubleList.get(4));
        //selForComparison.add(doubleList.get(9));
        //selForComparison.add(doubleList.get(7));  
        //selForComparison.add(doubleList.get(10));  
        //selForComparison.add(doubleList.get(11));  
        //selForComparison.add(doubleList.get(0));
        //selForComparison.add(doubleList.get(1));
        
        for(int i=0; i<selForComparison.size(); i++){
            
            
            rootNode = selForComparison.get(i).get(0);
            N = returnN(rootNode);
            
            convertIntoJPG(rootNode, "sub", i+1); 
            
            readWriteList = returnAvailability(rootNode, N, P);
            
            sumReadAvail = readWriteList.get(0).stream().mapToDouble(Double::doubleValue).sum();
            sumWriteAvail = readWriteList.get(1).stream().mapToDouble(Double::doubleValue).sum();
            fitness =  sumReadAvail + sumWriteAvail;  
            
            readFitnessList.add(sumReadAvail);
            writeFitnessList.add(sumWriteAvail);
            fitnessList.add(fitness);       
            
            tempArray = new Double[P.length];
            
            yAxisReadList.add(ArrayUtils.toPrimitive(readWriteList.get(0).toArray(tempArray)));
            yAxisWriteList.add(ArrayUtils.toPrimitive(readWriteList.get(1).toArray(tempArray)));
            
            dataset.getSeries(0).add(dataset.getSeries(0).getItemCount()+1, fitness); 
            
             
        }
        
        
        XYLineChartAll lineChartForAvailabiltyAll = new XYLineChartAll(P, yAxisReadList, yAxisWriteList, yAxisReadList.size());
        lineChartForAvailabiltyAll.setVisible(true); 

        
 
        

        // to convert into images 

        for(int i=0; i<size; i++){                     
            rootNode = doubleList.get(i).get(0); 
            convertIntoJPG(rootNode, "sub", i+1); 
        }




        // to check intersection 
        for(int i=0; i<size; i++){         
            rootNode = doubleList.get(i).get(0);
            isIntersection = checkIntersectionRWQ(rootNode);
            System.out.println("Strategy"+(i+1) +":"+ isIntersection);
            
        }
    }
}
