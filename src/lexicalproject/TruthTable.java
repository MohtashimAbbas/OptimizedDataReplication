/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicalproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Mohtashim's
 */
public class TruthTable {

    public static ArrayList<ArrayList<Integer>> returnTruthTable(int rows, int cols) {
        
        ArrayList<ArrayList<Integer>> doubleList;
        doubleList = new ArrayList<>();
        ArrayList<Integer> singleList;
              
        //System.out.print("   ");
        for(int i=0; i<cols; i++){
            //System.out.print(i+"   ");
        }
            //System.out.println();
        
        for(int i=0; i<cols; i++){
            //System.out.print("-----");
        }   
        //System.out.println();
        
        
        for (int i=0; i<rows; i++) {
            //System.out.print(""+i+". ");
            singleList = new ArrayList<>();
            
            for (int j=0; j<cols; j++) {
                
                singleList.add((i/(int) Math.pow(2, j))%2);
            }
            
            doubleList.add(singleList);
            //System.out.print(""+doubleList.get(i)+"\n");
          
        }
        
        return doubleList;
    }

    public static double[] selectCases(ArrayList<ArrayList<Integer>> doubleList, int rows, int cols, int givenQuorums[][], double P){
        
        int count1=0;
        int count2=0;
        int count3=0;
        int count4=0;
        int quorumListRowLen = givenQuorums.length;
        ArrayList<Integer> arrayValidCases = new ArrayList<>();
        
        double eqAvailabilityValue = 0.0;
        List<List<Integer>> costData = new ArrayList<>();
        double availCost[] = new double[2];
        
        java.util.Arrays.sort(givenQuorums, new java.util.Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return Double.compare(a.length, b.length);
            }
        });  
        
        
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                
                /*
                if(doubleList.get(i)!=null){
                    if(doubleList.get(i).get(j)==1){
                        count1++;
                    }
                }
                */
            }
            //System.out.println("count1/ row"+i+": "+ count1);
            //count1 = 0;
            
            for(int k=0; k<quorumListRowLen; k++){
                
                for(int l=0; l<givenQuorums[k].length; l++){
                    if(doubleList.get(i)!=null){   
                        if(doubleList.get(i).get(givenQuorums[k][l])==1){

                            count2++;
                        }
                    }
                }  
                
                if (count2==givenQuorums[k].length){                
                    count3++;
                    
                    for(int m=0; m<cols; m++){
                        if(doubleList.get(i)!=null){
                            if(doubleList.get(i).get(m)==1){
                                count4++;
                            }                       
                        }   
                    }
                    
                    doubleList.set(i, null);
                    arrayValidCases.add(count4);   
                    
                    costData.add(new ArrayList<>(Arrays.asList(count2,count4)));
                    
                    //System.out.println("count4: P^"+count4+"(1-P)^"+(cols - count4));
                    //System.out.println(" ");
                    
                    count4=0;                    
                }  
                count2=0;
            }
        }          
        
        
        eqAvailabilityValue = calculateSum(arrayValidCases, cols, P);
        
        
        Collections.sort(costData, new Comparator<List<Integer>>() {
            public int compare(List<Integer> a, List<Integer> b) {
                return Integer.compare(a.get(0), b.get(0));
            }
        });           


        int minCost = 0;
        int availableReplicas = 0;
        double costValue = 0.0;
        int size = costData.size();
        

        for(int i = 0; i<size; i++){
            
            minCost = costData.get(i).get(0); 
            availableReplicas = costData.get(i).get(1);
            
            //System.out.print(sum+"P^"+prevValue+" (1-P)^"+(N - prevValue) +"  +  ");
            costValue += (minCost*Math.pow(P,availableReplicas))* Math.pow((1-P), (cols - availableReplicas));            
        }        
        
        availCost[0] = eqAvailabilityValue;
        availCost[1] = costValue/ eqAvailabilityValue;
                
        return availCost;
        
    }
    
    public static double calculateSum(ArrayList<Integer> arrayValidCases, int N, double P){
        
        int sum = 0;
        int prevValue = -1;
        int newValue = 0;
        int size = arrayValidCases.size();
        double eqAvailabilityValue = 0;
        //List<Integer> listVarForAvailabilityEq= new ArrayList<>();
        
        Collections.sort(arrayValidCases);

        for(int i = 0; i<size; i++){
            
            newValue = arrayValidCases.get(i);            
            
            if (prevValue == -1){
                sum++;
            }
            else if(prevValue != newValue){
                
                //System.out.print(sum+"P^"+prevValue+" (1-P)^"+(N - prevValue) +"  +  ");
                
                eqAvailabilityValue += (sum*Math.pow(P,prevValue))* Math.pow((1-P), (N - prevValue));
                
                //listVarForAvailabilityEq.add(sum);
                //listVarForAvailabilityEq.add(prevValue);
                //listVarForAvailabilityEq.add(N);
        
                sum = 1;
                
            }
            else {
                sum++;

            }
            prevValue = newValue;
            
        }
        //System.out.println(sum+"P^"+newValue);
        
        // how to return the equation and use it in myproject calltruthtable func instead
        eqAvailabilityValue += (sum*Math.pow(P,newValue));
        
        //int sumForLast = sum;         
        //listVarForAvailabilityEq.add(sumForLast);
        //listVarForAvailabilityEq.add(newValue);
        
        return eqAvailabilityValue;
    }

    public void putValuesInEq(int P, int sum, int prevValue, int N, int sumForLast, int newValue ){
        //double eqAvailabilityValue = (sum*Math.pow(P,prevValue))* Math.pow((1-P), (N - prevValue)) + ;
        
    }
    public static void main(String[] args) {
        // TODO code application logic here
        int cols = 0;
        int rows;
        ArrayList<ArrayList<Integer>> doubleList;
        double P = 0;

        int givenQuorums [][] = {{0,1,2}, {3,4,5}, {6,7,8}, 
                                 {0,3,6}, {1,4,7}, {2,5,8}};  
       
        while(cols!=-1){
                System.out.print("\nEnter the number of columns: ");
                Scanner reader = new Scanner(System.in);

                cols = reader.nextInt();
                rows = (int) Math.pow(2,cols);
                    
                long startTime = System.currentTimeMillis();
                doubleList = returnTruthTable(rows, cols);                
                selectCases(doubleList, rows, cols, givenQuorums, P);
                long stopTime = System.currentTimeMillis();
                long millis = (stopTime - startTime);
                System.out.println((millis / 1000) / 60+ " min "+ (millis / 1000) % 60+" sec");         
        }
    }
}
