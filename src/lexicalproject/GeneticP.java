/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicalproject;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import static lexicalproject.Lexical.changeNames;
import static lexicalproject.Lexical.convertIntoJPG;
import static lexicalproject.Lexical.findPath;
import static lexicalproject.Lexical.formatOutput;
import static lexicalproject.Lexical.returnN;
import static lexicalproject.TruthTable.returnTruthTable;
import static lexicalproject.TruthTable.selectCases;


/**
 *
 * @author Mohtashim's
 */
public class GeneticP {
    
    double reproductionP;
    
    static Node gblRootNodeStrPrt;
    static int gblWeightStrPrt;
    static boolean isLeafStr;

   
    public static class FitnessClass {
        private List <List<Double>> readWriteList = new ArrayList<>();
        private boolean isFit;
        private Node strategy;
        private int N;
        private double sumReadAvail;
        private double sumWriteAvail;
        private double sumReadWriteAvail;
        private double sumReadCost;
        private double sumWriteCost;
        private double sumReadWriteCost;
        private double singleObjFitness;
        
        public void setReadWriteList(List<List<Double>> readWriteList){
            this.readWriteList = readWriteList;
        }
        public void setIsFit(boolean isFit){
            this.isFit = isFit;
        }
        public void setStrategy(Node strategy){
            this.strategy = strategy;
        }
        public void setN(int N){
            this.N = N;
        }   
        public void setSumReadAvail(double sumReadAvail){
            this.sumReadAvail = sumReadAvail;         
        }
        public void setSumWriteAvail(double sumWriteAvail){
            this.sumWriteAvail = sumWriteAvail;         
        } 
        public void setSumReadWriteAvail(double sumReadWriteAvail){
            this.sumReadWriteAvail = sumReadWriteAvail;         
        }         
        public void setSumReadCost(double sumReadCost){
            this.sumReadCost = sumReadCost;         
        }
        public void setSumWriteCost(double sumWriteCost){
            this.sumWriteCost = sumWriteCost;         
        } 
        public void setSumReadWriteCost(double sumReadWriteCost){
            this.sumReadWriteCost = sumReadWriteCost;         
        } 
        public void setSingleObjFitness(double singleObjFitness){
            this.singleObjFitness = singleObjFitness;         
        }         
        
        
        public List<List<Double>> getReadWriteList(){      
            return readWriteList;
        }
        public boolean getIsFit(){      
            return isFit;
        }        
        public Node getStrategy(){      
            return strategy;
        }  
        public int getN(){      
            return N;
        }  
        public double getSumReadAvail(){
            return sumReadAvail;         
        }        
        public double getSumWriteAvail(){
            return sumWriteAvail;         
        } 
        public double getSumReadWriteAvail(){
            return sumReadWriteAvail;         
        }    
        public double getSumReadCost(){
            return sumReadCost;         
        }        
        public double getSumWriteCost(){
            return sumWriteCost;         
        } 
        public double getSumReadWriteCost(){
            return sumReadWriteCost;         
        }            
        public double getSingleObjFitness(){
            return singleObjFitness;         
        }           
        
     
    }

    /**
     * determine whether two numbers are "approximately equal" by seeing if they
     * are within a certain "tolerance percentage," with `tolerancePercentage` given
     * as a percentage (such as 10.0 meaning "10%").
     *
     * @param tolerancePercentage 1 = 1%, 2.5 = 2.5%, etc.
     */
    public static boolean approximatelyEqual(double actualValue, double desiredValue, double tolerancePercentage) {
        double diff = Math.abs(desiredValue - actualValue);          // 1000 - 950  = 50
        double tolerance = tolerancePercentage/100 * desiredValue;  //  20/100*1000 = 200
        return diff < tolerance;                                   //   50<200      = true
    }    
    
    public static FitnessClass checkRelaxedFitness(Node rootNode, double P[], double uSumReadAvail, double uSumWriteAvail, int uN, 
            double fitnessWeightage, double uExpReadCost, double uExpWriteCost, int uMinReadCost, int uMinWriteCost, int uMaxReadCost, int uMaxWriteCost){
       
        boolean isFit = true;
        
        FitnessClass objFitness = new FitnessClass();
        
        // for number of nodes
        int N = returnN(rootNode);       
        
        // actual availability 
        double sumReadAvail = 0.0;
        double sumWriteAvail = 0.0;
        double sumReadCost = 0.0;
        double sumWriteCost = 0.0;       

        // upper tolerance percentage 
        double tolPercentageN = 10;
        double tolPercentageMinReadCost = 20;
        double tolPercentageMaxReadCost = 20;
        double tolPercentageMinWriteCost = 20;
        double tolPercentageMaxWriteCost = 20;
        double tolPercentageSumReadAvail = 50;
        double tolPercentageSumWriteAvail = 50;
        
        
        if(uN !=0){
            /*
            check if N is below the threshold then consider it as fit but for upper threshold 
            it checks the approximation to determine fitness 
            */
            if(N > uN){
                
                //if(approximatelyEqual(N, uN, tolPercentageN)==false){

                    isFit = false;    

                    objFitness.setStrategy(rootNode);        
                    objFitness.setN(N);
                    objFitness.setReadWriteList(null);
                    objFitness.setSumReadAvail(0);
                    objFitness.setSumWriteAvail(0);
                    objFitness.setSumReadWriteAvail(0);
                    objFitness.setSingleObjFitness(0);
                    
                    objFitness.setIsFit(isFit);

                    return objFitness;
                //}
            }
        } 

/*
        // for cost calculation for read and write 
        boolean checkParentEnd = true;
        List<String> pathArrayListRead = findPath(rootNode, checkParentEnd, "read");
        formatOutput(pathArrayListRead);
        int[] minMaxCostRead = calculateCost(pathArrayListRead);
        
        checkParentEnd = true;
        List<String> pathArrayListWrite = findPath(rootNode, checkParentEnd, "write");
        formatOutput(pathArrayListWrite);
        int[] minMaxCostWrite= calculateCost(pathArrayListWrite);
        
        if(uMinReadCost!=0){
            
            if(approximatelyEqual(minMaxCostRead[0], uMinReadCost, tolPercentageMinReadCost)==false){
                
                isFit= false;   
                
                objFitness.setStrategy(rootNode);        
                objFitness.setN(N);
                objFitness.setReadWriteList(null);
                objFitness.setSumReadAvail(0);
                objFitness.setSumWriteAvail(0);
                objFitness.setSumReadWriteAvail(0);
                objFitness.setIsFit(isFit);
                
                return objFitness;                
            }      
        }
        
        if(uMaxReadCost!=0){        
            
            if(approximatelyEqual(minMaxCostRead[1], uMaxReadCost, tolPercentageMaxReadCost)==false){
                
                isFit= false;      
                
                objFitness.setStrategy(rootNode);        
                objFitness.setN(N);
                objFitness.setReadWriteList(null);
                objFitness.setSumReadAvail(0);
                objFitness.setSumWriteAvail(0);
                objFitness.setSumReadWriteAvail(0);                
                objFitness.setIsFit(isFit);
                
                return objFitness;                
            }      
        }
        
        if(uMinWriteCost!=0){ 
            
            if(approximatelyEqual(minMaxCostWrite[0], uMinWriteCost, tolPercentageMinWriteCost)==false){
                
                isFit= false;   
                
                objFitness.setStrategy(rootNode);        
                objFitness.setN(N);
                objFitness.setReadWriteList(null);
                objFitness.setSumReadAvail(0);
                objFitness.setSumWriteAvail(0);  
                objFitness.setSumReadWriteAvail(0);
                objFitness.setIsFit(isFit);
                
                return objFitness;                
            }      
        }
        
        if(uMaxWriteCost!=0){ 
            
            if(approximatelyEqual(minMaxCostWrite[1], uMaxWriteCost, tolPercentageMaxWriteCost)==false){
                
                isFit= false;       
                
                objFitness.setStrategy(rootNode);        
                objFitness.setN(N);
                objFitness.setReadWriteList(null);
                objFitness.setSumReadAvail(0);
                objFitness.setSumWriteAvail(0);
                objFitness.setSumReadWriteAvail(0);
                objFitness.setIsFit(isFit);
                
                return objFitness;                
            }      
        }        
*/        
        // for the availability 
        List <List<Double>> readWriteList = new ArrayList<>();         
        readWriteList = returnAvailability(rootNode, N, P);
        
        // calculated current avilability 
        sumReadAvail = readWriteList.get(0).stream().mapToDouble(Double::doubleValue).sum();
        sumWriteAvail = readWriteList.get(1).stream().mapToDouble(Double::doubleValue).sum();
        sumReadCost = readWriteList.get(2).stream().mapToDouble(Double::doubleValue).sum();
        sumWriteCost = readWriteList.get(3).stream().mapToDouble(Double::doubleValue).sum();
 
/*        
        if (uSumWriteAvail !=0){
            
            if (uSumWriteAvail > sumWriteAvail){
                
                if(approximatelyEqual(sumWriteAvail, uSumWriteAvail, tolPercentageSumWriteAvail)==false){

                    isFit= false;

                    objFitness.setStrategy(rootNode);        
                    objFitness.setN(N);
                    objFitness.setReadWriteList(readWriteList);
                    objFitness.setSumReadAvail(sumReadAvail);
                    objFitness.setSumWriteAvail(sumWriteAvail);
                    objFitness.setSumReadWriteAvail(sumReadAvail + sumWriteAvail);
                    objFitness.setSumReadCost(sumReadCost);
                    objFitness.setSumWriteCost(sumWriteCost);
                    objFitness.setSumReadWriteCost(sumReadCost + sumWriteCost);
                    
                    objFitness.setIsFit(isFit);

                    return objFitness;                
                }
            }
        }
        
        if (uSumReadAvail !=0){
            
            if (uSumReadAvail > sumReadAvail){
                                                
                if(approximatelyEqual(sumReadAvail, uSumReadAvail, tolPercentageSumReadAvail)==false){

                    isFit= false;

                    objFitness.setStrategy(rootNode);        
                    objFitness.setN(N);
                    objFitness.setReadWriteList(readWriteList);
                    objFitness.setSumReadAvail(sumReadAvail);
                    objFitness.setSumWriteAvail(sumWriteAvail);  
                    objFitness.setSumReadWriteAvail(sumReadAvail + sumWriteAvail);
                    objFitness.setSumReadCost(sumReadCost);
                    objFitness.setSumWriteCost(sumWriteCost);
                    objFitness.setSumReadWriteCost(sumReadCost + sumWriteCost);
                    
                    objFitness.setIsFit(isFit);

                    return objFitness;

                }
            }
        }

*/      
        objFitness.setStrategy(rootNode);        
        objFitness.setN(N);   
        objFitness.setReadWriteList(readWriteList);
        objFitness.setSumReadAvail(sumReadAvail);
        objFitness.setSumWriteAvail(sumWriteAvail);     
        objFitness.setSumReadWriteAvail(sumReadAvail + sumWriteAvail);
        objFitness.setSumReadCost(sumReadCost);
        objFitness.setSumWriteCost(sumWriteCost);
        objFitness.setSumReadWriteCost(sumReadCost + sumWriteCost);        
        
        double singleObjFitness = returnSingleObjfitness(sumReadAvail + sumWriteAvail, sumReadCost + sumWriteCost, N, fitnessWeightage);
        objFitness.setSingleObjFitness(singleObjFitness);
        
        objFitness.setIsFit(isFit);
  
        return objFitness;
    }
    
    public static List<FitnessClass> performRelaxedSelection(List<Node> strategiesList, double P[], double uSumReadAvail, double uSumWriteAvail, 
            int uN, int uExpReadCost, int uExpWriteCost, int uMinReadCost, int uMinWriteCost, int uMaxReadCost, int uMaxWriteCost){
       
        Node rootNode;       
        FitnessClass objFitness;
        List<FitnessClass> objFitnessList = new ArrayList<>();
        
        for(int i=0; i<strategiesList.size(); i++){
           
            rootNode = strategiesList.get(i); 
            
            objFitness = new FitnessClass();  
            //objFitness = checkRelaxedFitness(rootNode, P, uSumReadAvail, uSumWriteAvail, uN, uExpReadCost, uExpWriteCost, uMinReadCost, uMinWriteCost, uMaxReadCost, uMaxWriteCost);        
           
            if(objFitness.getIsFit() == true){
               
                objFitnessList.add(objFitness);
            }
        }
        return objFitnessList;
    }

    
    public static List<String> calNumOfSubStrs(Node rootNodeStr, List<String> numOfSub, List<Node> listRepeatedNodes){
               
        // number of children
        int nodeChildren = rootNodeStr.getNodeSize();

        // repeated node check 
        boolean matchName = false;
                        
        // loop: number of children
        for(int i=0; i< nodeChildren; i++){   
            
            // repeated node check 
            matchName = false;
            for(int j=0; j<listRepeatedNodes.size(); j++){
                if(listRepeatedNodes.get(j)==rootNodeStr.getNode(i)){
                    matchName = true;
                }
            }
            
            if(!matchName){
                
                if(rootNodeStr.getNode(i).isSubStrs()==true){

                    // number of sub structures residing in a graph 
                    numOfSub.add(rootNodeStr.getNode(i).getNodeName());
                    
                    listRepeatedNodes.add(rootNodeStr.getNode(i)); 
                }

                // if virtual node
                if(rootNodeStr.getNode(i).isLeaf()==false){
                    
                    calNumOfSubStrs(rootNodeStr.getNode(i), numOfSub, listRepeatedNodes);
                }
            }
        }
        
       return numOfSub;
    }
    

    public static List<String> calNumOfMutables(Node rootNodeStr, List<String> numOfMutables, List<Node> listRepeatedNodes){
               
        // number of children
        int nodeChildren = rootNodeStr.getNodeSize();

        // repeated node check 
        boolean matchName = false;
                        
        // loop: number of children
        for(int i=0; i< nodeChildren; i++){   
            
            // repeated node check 
            matchName = false;
            for(int j=0; j<listRepeatedNodes.size(); j++){
                if(listRepeatedNodes.get(j)==rootNodeStr.getNode(i)){
                    matchName = true;
                }
            }
            
            if(!matchName){
                
                if(rootNodeStr.getNode(i).getMutable()==true){

                    // number of sub structures residing in a graph 
                    numOfMutables.add(rootNodeStr.getNode(i).getNodeName());
                    
                    listRepeatedNodes.add(rootNodeStr.getNode(i)); 
                }

                // if virtual node
                if(rootNodeStr.getNode(i).isLeaf()==false){
                    
                    calNumOfMutables(rootNodeStr.getNode(i), numOfMutables, listRepeatedNodes);
                }
            }
        }
        
       return numOfMutables;
    }
    
    // to check if the crossover point is leaf
    public static int checkLeaf(Node rootNodeStr, int crossOverPoint, int count, List<Node> listRepeatedNodes){
        // number of children
        int nodeChildren = rootNodeStr.getNodeSize();

        // repeated node check 
        boolean matchName = false;
                        
        // loop: number of children
        for(int i=0; i< nodeChildren; i++){   
            
            // repeated node check 
            matchName = false;
            for(int j=0; j<listRepeatedNodes.size(); j++){
                if(listRepeatedNodes.get(j)==rootNodeStr.getNode(i)){
                    matchName = true;
                }
            }
            
            if(!matchName){

                if(rootNodeStr.getNode(i).isSubStrs()==true){

                    // number of sub structures residing in a graph 
                    count++;

                    if(count==crossOverPoint){

                        isLeafStr = rootNodeStr.getNode(i).isLeaf();              
                        return count;

                    }
                    else{
                        listRepeatedNodes.add(rootNodeStr.getNode(i)); 
                    }
                }

                if(rootNodeStr.getNode(i).isLeaf()==false){
                    count = checkLeaf(rootNodeStr.getNode(i), crossOverPoint, count, listRepeatedNodes);
               }
            }                              
        }
        return count;
    }
    
    
    

    public static List<Node> crossoverUpdated(Node strategy1, Node strategy2){
        
        List<Node> crossedOverList = new ArrayList<>();
        int maxVPValue[] = {1,0}; 

        // root node of a structure  
        Node rootNode;
        Node rootNodeStr1;
        Node rootNodeStr2;
             
        rootNodeStr1 = (Node) Node.deepCopy(strategy1);
        rootNodeStr2 = (Node) Node.deepCopy(strategy2);   
       
        int numOfSubStrs1 = 1;
        int numOfSubStrs2 = 1;       

        //numOfSubStrs1 = calNumOfSubStrs(rootNodeStr1, numOfSubStrs1, new ArrayList<Node>());
        //numOfSubStrs2 = calNumOfSubStrs(rootNodeStr2, numOfSubStrs2, new ArrayList<Node>());

        Random rand = new Random();
        int crossOverPointStr1;
        int crossOverPointStr2;
            
        do{
            crossOverPointStr1 = rand.nextInt(numOfSubStrs1-1)+1;
            crossOverPointStr2 = rand.nextInt(numOfSubStrs2-1)+1;
        }
        while(!((crossOverPointStr1>1) && (crossOverPointStr2>1)));
            
        Node rootNodeStr1Prt;
        Node rootNodeStr2Prt;

        //breakStrategy(rootNodeStr1, crossOverPointStr1, 1, new ArrayList<Node>());
        //rootNodeStr1Prt = (Node) Node.deepCopy(gblRootNodeStrPrt);

        //breakStrategy(rootNodeStr2, crossOverPointStr2, 1, new ArrayList<Node>());
        //rootNodeStr2Prt = (Node) Node.deepCopy(gblRootNodeStrPrt);

        //join(rootNodeStr1, crossOverPointStr1, 1, rootNodeStr2Prt, new ArrayList<Node>());
        //join(rootNodeStr2, crossOverPointStr2, 1, rootNodeStr1Prt, new ArrayList<Node>());        
            
        maxVPValue[0] = 1;
        maxVPValue[1] = 0;
        // change the names of the rest of the nodes for strategy 2
        changeNames(rootNodeStr1, maxVPValue, new ArrayList<Node>());
        crossedOverList.add(rootNodeStr1);
            
        maxVPValue[0] = 1;
        maxVPValue[1] = 0;
        // change the names of the rest of the nodes for strategy 2
        changeNames(rootNodeStr2, maxVPValue, new ArrayList<Node>());            
        crossedOverList.add(rootNodeStr2);
        
     
        return crossedOverList;
    }



    public static List<Node> performCrossoverSingle(Node rootNodeStr1, Node rootNodeStr2, List<Double> intraCrossoverProbs) throws IOException, InterruptedException{
        
        List<Node> crossedOverList = new ArrayList<>();
        int maxVPValue[] = {1,0}; 
           
        // start counting from 1 because the couting starts after the rootNode which is by default a subStr              
        List<String> numOfSubStrs1 = new ArrayList();
        List<String> numOfSubStrs2 = new ArrayList();
        
        numOfSubStrs1.add(rootNodeStr1.getNodeName());
        numOfSubStrs2.add(rootNodeStr2.getNodeName());
        
        numOfSubStrs1 = calNumOfSubStrs(rootNodeStr1, numOfSubStrs1, new ArrayList<Node>());
        numOfSubStrs2 = calNumOfSubStrs(rootNodeStr2, numOfSubStrs2, new ArrayList<Node>());
       
        Random rand = new Random();
        String crossOverPointStr1;
        String crossOverPointStr2;
        boolean isLeafStr1;
        boolean isLeafStr2;
        
       
        // crossoverType1: attach two complete strategies horizontally by a new rootNode
        // crossoverType2: attach two sub-strategies or one complete strategy horizontally by a new rootNode
        // crossoverType3: attach a complete strategy to another complete strategy as a child
        // crossoverType4: rest of the crossovers;   replace virtual by virtual, replace virtual by leaf, replace leaf by the virtual, replace virtual by a complete other strategy, replace leaf by a complete other strategy
        List<String> crossoverEvents = new ArrayList<>(Arrays.asList("crossoverType1", "crossoverType2", "crossoverType3", "crossoverType4"));
        double prob = rand.nextDouble(); 
        String event = "";
        
        if(prob <= intraCrossoverProbs.get(0)){
            event = crossoverEvents.get(0);
        }
        else if(prob <= intraCrossoverProbs.get(1)){
            event = crossoverEvents.get(1);;            
        }
        else if(prob <= intraCrossoverProbs.get(2)){
            event = crossoverEvents.get(2);;            
        }        
        else {
            event = crossoverEvents.get(3);   
        }
        
        
        
        
        do{
            isLeafStr1 = false; 
            isLeafStr2 = false; 

            crossOverPointStr1 = numOfSubStrs1.get(rand.nextInt(numOfSubStrs1.size()));       
            crossOverPointStr2 = numOfSubStrs2.get(rand.nextInt(numOfSubStrs2.size()));


            if(crossOverPointStr1.contains("p")){
               isLeafStr1 = true; 
            }
            if(crossOverPointStr2.contains("p")){
               isLeafStr2 = true; 
            }            

        }

        // make crossover with either virtual or physical nodes and to avoid having two 
        // complete strategies' swap or two leaf nodes' swap as it doesn't impact the result    
        while( (crossOverPointStr1.equals("V1") && crossOverPointStr2.equals("V1")) || (isLeafStr1 == true && isLeafStr2 == true)); 

        // swap only virtual nodes such that both should not be complete strategies
        //while ((crossOverPointStr1.equals("V1") && crossOverPointStr2.equals("V1") || !(isLeafStr1 == false && isLeafStr2 == false))); 


        Node rootNodeStr1Prt = new Node();
        Node rootNodeStr2Prt = new Node();

        // to keep the weight/votes in crossover (exchange only quorums)
        int weightStr1Prt = 1;
        int weightStr2Prt = 1; 

        // breaking part
        // check if complete strategy1 or a part of it
        if(!crossOverPointStr1.equals("V1")){            

            breakStrategy(rootNodeStr1, crossOverPointStr1, new ArrayList<Node>());
            rootNodeStr1Prt = (Node) Node.deepCopy(gblRootNodeStrPrt);
            weightStr1Prt = gblWeightStrPrt;
        }
        else{
            rootNodeStr1Prt = (Node) Node.deepCopy(rootNodeStr1);
            weightStr1Prt = rootNodeStr1.getWeight();
        }


        // check if complete strategy2 or a part of it
        if(!crossOverPointStr2.equals("V1")){              

            breakStrategy(rootNodeStr2, crossOverPointStr2, new ArrayList<Node>());
            rootNodeStr2Prt = (Node) Node.deepCopy(gblRootNodeStrPrt);
            weightStr2Prt = gblWeightStrPrt;
        }
        else{
            rootNodeStr2Prt = (Node) Node.deepCopy(rootNodeStr2);
            weightStr2Prt = rootNodeStr2.getWeight();
        }  

        System.out.println("crossOverPointStr1: "+crossOverPointStr1);
        System.out.println("rootNodeStr2Prt: "+rootNodeStr2Prt.getNodeName());
        
        System.out.println("crossOverPointStr2: "+crossOverPointStr2);
        System.out.println("rootNodeStr1Prt: "+rootNodeStr1Prt.getNodeName());
                
        // attach two complete strategies horizontally by a new rootNode
        if(event.equals("crossoverType1")){
            
            Node rootNode = new Node();
            rootNode.setNodeName("V1");
            rootNode.setVotesR(1);
            rootNode.setVotesW(2);
            rootNode.setIsSubStrs(true);
            rootNode.setMutable(true);            
            rootNode.setWeight((int)1);            
            
            rootNode.setNode(rootNodeStr1);
            rootNode.setNode(rootNodeStr2); 
            
            //convertIntoJPG(rootNode, "sub", 1);
            changeNames(rootNode, maxVPValue, new ArrayList<Node>());  // change the names of the rest of the nodes for strategy 2 
            //convertIntoJPG(rootNode, "sub", 2);
            
            crossedOverList.add(rootNode);       
        }
        
        // attach two sub-strategies or one complete strategy horizontally by a new rootNode
        else if(event.equals("crossoverType2")){
            
            Node rootNode = new Node();
            rootNode.setNodeName("V1");
            rootNode.setVotesR(1);
            rootNode.setVotesW(2);
            rootNode.setIsSubStrs(true);
            rootNode.setMutable(true);            
            rootNode.setWeight((int)1);  
            
            rootNode.setNode(rootNodeStr1Prt);
            rootNode.setNode(rootNodeStr2Prt); 
            
            changeNames(rootNode, maxVPValue, new ArrayList<Node>()); 
                        
            double qR = rootNode.getVotesR();
            double qW = rootNode.getVotesW();
            double totalWeight = 0;


            for(int k=0; k<rootNode.getNodeSize(); k++){
                totalWeight += rootNode.getNode(k).getWeight();
            }                        

            while(!(qW > totalWeight/2)){
                qW++; 
            }

            while(!(qR + qW > totalWeight)){
                qR++; 
            }            

            rootNode.setVotesR((int)qR);
            rootNode.setVotesW((int)qW);  
            
            crossedOverList.add(rootNode);            
        }
        
        // attach a complete strategy to another complete strategy as a child
        else if(event.equals("crossoverType3")){          
            
            rootNodeStr1.setNode(rootNodeStr2);           
           
            changeNames(rootNodeStr1, maxVPValue, new ArrayList<Node>());  // change the names of the rest of the nodes for strategy 2 
                       
            double qR = rootNodeStr1.getVotesR();
            double qW = rootNodeStr1.getVotesW();
            double totalWeight = 0;


            for(int k=0; k<rootNodeStr1.getNodeSize(); k++){
                totalWeight += rootNodeStr1.getNode(k).getWeight();
            }                        

            while(!(qW > totalWeight/2)){
                qW++; 
            }

            while(!(qR + qW > totalWeight)){
                qR++; 
            }            

            rootNodeStr1.setVotesR((int)qR);
            rootNodeStr1.setVotesW((int)qW);  
                        
            crossedOverList.add(rootNodeStr1);       
        }
        
        // all other type of crossovers 
            // replace virtual by virtual 
            // replace virtual by leaf  	
            // replace leaf by the virtual		
            // replace virtual by a complete other strategy
            // replace leaf by a complete other strategy
        
        else if(event.equals("crossoverType4")){
            
            System.out.println("statement 1");
                    
            // joining part strategy1
            if(!crossOverPointStr1.equals("V1")){
                
                System.out.println("statement 1.1");
                
                maxVPValue[0] = 1;
                maxVPValue[1] = 0;  
                rootNodeStr2Prt.setWeight(weightStr1Prt);

                join(rootNodeStr1, crossOverPointStr1, rootNodeStr2Prt, new ArrayList<Node>());
                changeNames(rootNodeStr1, maxVPValue, new ArrayList<Node>()); // change the names of the rest of the nodes for strategy 2           
            }
            else{
                System.out.println("statement 2");
                maxVPValue[0] = 1;
                maxVPValue[1] = 0;
                rootNodeStr2Prt.setWeight(weightStr1Prt);

                if(!rootNodeStr2Prt.getNodeName().contains("p")){ // don't replace a complete strategy by just one physical replica and replace compelete strategy only by virtual replica  
                    
                    System.out.println("statement 2.1");
                    rootNodeStr1 = (Node) Node.deepCopy(rootNodeStr2Prt);
                    rootNodeStr1.setNodeName("V1");
                    changeNames(rootNodeStr1, maxVPValue, new ArrayList<Node>()); // change the names of the rest of the nodes for strategy 2                  
                }
            }


            // joining part strategy2
            if(!crossOverPointStr2.equals("V1")){  
                
                System.out.println("statement 3");
                
                maxVPValue[0] = 1;
                maxVPValue[1] = 0;            
                rootNodeStr1Prt.setWeight(weightStr2Prt);
                
                join(rootNodeStr2, crossOverPointStr2, rootNodeStr1Prt, new ArrayList<Node>());        
                changeNames(rootNodeStr2, maxVPValue, new ArrayList<Node>());  // change the names of the rest of the nodes for strategy 2

            } 
            else{
                
                System.out.println("statement 4");
                
                maxVPValue[0] = 1;
                maxVPValue[1] = 0;   
                rootNodeStr1Prt.setWeight(weightStr2Prt);

                if(!rootNodeStr1Prt.getNodeName().contains("p")){
                    
                    System.out.println("statement 4.1");
                    
                    rootNodeStr2 = (Node) Node.deepCopy(rootNodeStr1Prt);   
                    rootNodeStr2.setNodeName("V1");
                    changeNames(rootNodeStr2, maxVPValue, new ArrayList<Node>());  // change the names of the rest of the nodes for strategy 2            
                }
            }






            if(!crossOverPointStr1.equals("V1")){ 
                
                System.out.println("statement 5");
                
                crossedOverList.add(rootNodeStr1);    
            }
            else{
                
                System.out.println("statement 6");
                
                if(!rootNodeStr2Prt.getNodeName().contains("p")){
                   
                   System.out.println("statement 6.1"); 
                   crossedOverList.add(rootNodeStr1); 
                }
            }


            if(!crossOverPointStr2.equals("V1")){ 
                
                System.out.println("statement 7");
                
                crossedOverList.add(rootNodeStr2);
            }
            else{
                
                System.out.println("statement 8");
                
                if(!rootNodeStr1Prt.getNodeName().contains("p")){
                   
                   System.out.println("statement 8.1");
                   crossedOverList.add(rootNodeStr2); 
                }            
            }            
        }
                   
        System.out.println("statement 9");     
        
        for(int i=0; i<crossedOverList.size(); i++){
            convertIntoJPG(crossedOverList.get(i), "sub1", i+1);
        }
        return crossedOverList;
    }


    
    public static void breakStrategy(Node rootNodeStr, String crossOverPoint, List<Node> listRepeatedNodes){
        
        // number of children
        int nodeChildren = rootNodeStr.getNodeSize();

        // repeated node check 
        boolean matchName = false;
                        
        // loop: number of children
        for(int i=0; i< nodeChildren; i++){   
            
            // repeated node check 
            matchName = false;
            for(int j=0; j<listRepeatedNodes.size(); j++){
                if(listRepeatedNodes.get(j)==rootNodeStr.getNode(i)){
                    matchName = true;
                }
            }
            
            if(!matchName){
                

                if(rootNodeStr.getNode(i).isSubStrs()==true){

                    if(rootNodeStr.getNode(i).getNodeName().equals(crossOverPoint)){

                        gblRootNodeStrPrt = (Node) Node.deepCopy(rootNodeStr.getNode(i));
                        gblWeightStrPrt = rootNodeStr.getNode(i).getWeight();
                        //rootNodeStr.deleteNode(rootNodeStr.getNode(i));

                        return;
                    }
                    else{
                        listRepeatedNodes.add(rootNodeStr.getNode(i)); 
                    }
                }

                // if virtual node
                if(rootNodeStr.getNode(i).isLeaf()==false){
                    
                    breakStrategy(rootNodeStr.getNode(i), crossOverPoint, listRepeatedNodes);       
                }                    
            }
        }
    }   
    
    public static void join(Node rootNodeStr, String crossOverPoint, Node lclRootNodeStrPrt, List<Node> listRepeatedNodes){
        
        // number of children
        int nodeChildren = rootNodeStr.getNodeSize();

        // repeated node check 
        boolean matchName = false;
                        
        // loop: number of children
        for(int i=0; i< nodeChildren; i++){   
            
            // repeated node check 
            matchName = false;
            for(int j=0; j<listRepeatedNodes.size(); j++){
                if(listRepeatedNodes.get(j)==rootNodeStr.getNode(i)){
                    matchName = true;
                }
            }
            
            if(!matchName){
                
                //if(rootNodeStr.getNode(i).isSubStrs()==true){

                if(rootNodeStr.getNode(i).getNodeName().equals(crossOverPoint)) {

                    System.out.println("join.rootNodeStr.getNode(i): "+rootNodeStr.getNode(i).getNodeName());
                    System.out.println("crossOverPoint: "+crossOverPoint);
                    
                    // replace the node of strategy 1 with rootNode of strategy 2 in order to make a hybrid one 
                    rootNodeStr.replaceNode(i, lclRootNodeStrPrt);

                    return;
                }
                //}
                else{
                    listRepeatedNodes.add(rootNodeStr.getNode(i)); 
                }
                
                    // if virtual node
                if(rootNodeStr.getNode(i).isLeaf()==false){                
                    System.out.println("join.rootNodeStr.getNode(i): "+rootNodeStr.getNode(i).getNodeName());
                    // recursive call to change the names of subsequent nodes
                    join(rootNodeStr.getNode(i), crossOverPoint, lclRootNodeStrPrt, listRepeatedNodes);
                }
            
            }


        }    
    }
    

    
    public static void mutationReplace(Node rootNodeStr, String crossOverPoint, Node lclRootNodeStrPrt, List<Node> listRepeatedNodes){

        // number of children
        int nodeChildren = rootNodeStr.getNodeSize();

        // repeated node check 
        boolean matchName = false;
                        
        // loop: number of children
        for(int i=0; i< nodeChildren; i++){   
            
            // repeated node check 
            matchName = false;
            for(int j=0; j<listRepeatedNodes.size(); j++){
                if(listRepeatedNodes.get(j)==rootNodeStr.getNode(i)){
                    matchName = true;
                }
            }
            
            if(!matchName){
                
                //if(rootNodeStr.getNode(i).isSubStrs()==true){

                if(rootNodeStr.getNode(i).getNodeName().equals(crossOverPoint)) {

                    lclRootNodeStrPrt.setWeight(rootNodeStr.getNode(i).getWeight());
                    // replace the node of strategy 1 with rootNode of strategy 2 in order to make a hybrid one 
                    rootNodeStr.replaceNode(i, lclRootNodeStrPrt);

                    return;
                }
                //}
                else{
                    listRepeatedNodes.add(rootNodeStr.getNode(i)); 
                }
            }

                // if virtual node
            if(rootNodeStr.getNode(i).isLeaf()==false){                    
                // recursive call to change the names of subsequent nodes
                mutationReplace(rootNodeStr.getNode(i), crossOverPoint, lclRootNodeStrPrt, listRepeatedNodes);
            }
        }             
    }
    
            
    public static List<List<Node>> mutation(List<List<Node>> crossedOverList){

        List<List<Node>> mutatedList = new ArrayList<>();
        
        return mutatedList;        
        
    }
    

    /* arr[]  ---> Input Array
    data[] ---> Temporary array to store current combination
    start & end ---> Staring and Ending indexes in arr[]
    index  ---> Current index in data[]
    r ---> Size of a combination to be printed */
    static void combinationUtil(int arr[], int data[], int start,
                                int end, int index, int r, List <int[]> resultList)
    {
        // Current combination is ready to be printed, print it
        if (index == r)
        {   /*
            for (int j=0; j<r; j++){
                System.out.print(data[j]+" ");
            }
            
            System.out.println("");
            */
            resultList.add(data.clone());
            
            return;
        }
 
        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i=start; i<=end && end-i+1 >= r-index; i++)
        {
            data[index] = arr[i];
            combinationUtil(arr, data, i+1, end, index+1, r, resultList);
        }
    }
 
    // The main function that prints all combinations of size r
    // in arr[] of size n. This function mainly uses combinationUtil()
    static void printCombination(int arr[], int n, int r, List <int[]> resultList)
    {
        // A temporary array to store all combination one by one
        int data[]=new int[r];
 
        // Print all combination using temporary array 'data[]'
        combinationUtil(arr, data, 0, n-1, 0, r, resultList);
    }    

    // call the function to select cases from the truth table 
    public static double[] callTruthTableFunc(List <String> pathArrayList, int cols, double P, int minusNum){
        
        // list of integer array to store availability combinations
        List<int[]> intList = new ArrayList<>();
        
        // to combinations as string and integers 
        String[] stringLine;
        int[] intLine;
        
        // availability equation
        double eqAvailabilityValue = 0;
        
        // loop: number of combinations
        for(int i=0; i<pathArrayList.size(); i++){
            
            // split combination of comma 
            stringLine = pathArrayList.get(i).split(",");    
            intLine = new int [stringLine.length];
           
            // convert the combinations into integer array
            for (int k = 0; k < stringLine.length; k++) {
                intLine[k] = Integer.parseInt(stringLine[k]);
            }
            
            // add combinations to the list of integer array
            intList.add(intLine);
            
        }
  
        // convert list of integer array into 2D integer array
        int data[][] = new int[intList.size()][]; 
        for(int m=0; m<intList.size(); m++){
            data[m] = new int[intList.get(m).length];
            for(int n=0; n<intList.get(m).length; n++){
                data[m][n] = intList.get(m)[n]-minusNum;
            }
        } 
        
        // number of rows and columns to generate truth table
        //int cols;
        int rows;
        
        // to store the truth table
        ArrayList<ArrayList<Integer>> doubleList;       
        
        System.out.println("N: "+ cols);
        
        // if number of nodes N is not coming from the database
        rows = (int) Math.pow(2,cols); 
        
        // return truth table 
        doubleList = returnTruthTable(rows, cols);                
        
        // select relevant cases from the truth table and calculate availability value from the availability equation to plot  
        double availCost[] = selectCases(doubleList, rows, cols, data, P);  
        
        return availCost;
    }

    
    // to draw availability performance graph 
    public static List <List<Double>> returnAvailability(Node rootNodeHybridStrategy, int n, double[] P){
        
        // to fetch and store the combinations for read and write (valid quorums)
        List<String> pathArrayListRead;
        List<String> pathArrayListWrite;
        
        Set<String> removeDuplicates;
        
        // to add the yaxis values (double[] yAxisRead, double[] yAxisWrite) of read and write to this list to pass it on to the charting library function
        List <List<Double>> yAxisAvailCostReadWriteList = new ArrayList<>();
        
        // to check if the combination ends
        boolean checkParentEnd = true;
               
        // initializing the double arrays to store the yaxis values for the read and write access operations 
        List <Double> yAxisAvailRead = new ArrayList<>();
        List <Double> yAxisAvailWrite = new ArrayList<>();
        
        // initializing the double arrays to store the yaxis values for the read and write access operations 
        List <Double> yAxisCostRead = new ArrayList<>();
        List <Double> yAxisCostWrite = new ArrayList<>();
        
            
        // initializing the ArrayLists to store all the combinations for the read and write for a document/strategy   
        pathArrayListRead = new ArrayList();
        pathArrayListWrite = new ArrayList();
            
        // take the rootnode of each graph/structure/strategy/document/tree to traverse the tree
        int minVPValue[] = new int[]{Integer.parseInt(rootNodeHybridStrategy.getNodeName().replaceAll("[^\\d.]", "")),100};
        
        // subtract this value from the leaf node names in order to make the naming consistant for the truthtable indexes
        minVPValue = findMin(rootNodeHybridStrategy, minVPValue);

        // to check if the combination ends
        checkParentEnd = true;
  
        // return combinations for the read operation
        pathArrayListRead = findPath(rootNodeHybridStrategy, checkParentEnd, "read");
            
        //System.out.println("\n-Read:");        
        // format it so that we can pass it as an input to another function to calculate availability equation for the read operation 
        formatOutput(pathArrayListRead);
        
        removeDuplicates = new HashSet<>(pathArrayListRead);
        pathArrayListRead.clear();
        pathArrayListRead.addAll(removeDuplicates);
        
        for(int k=0; k< pathArrayListRead.size(); k++){
            //System.out.println("pathArrayListRead.get("+k+"): "+pathArrayListRead.get(k));
        }

        
        // return combinations for the write operation
        pathArrayListWrite = findPath(rootNodeHybridStrategy, checkParentEnd, "write");
        //System.out.println("\n"+i+"-Write:");

        //System.out.println("\n-Write:");        
        // format it so that we can pass it as an input to another function to calculate availability equation for write operation
        formatOutput(pathArrayListWrite);

        removeDuplicates = new HashSet<>(pathArrayListWrite);
        pathArrayListWrite.clear();
        pathArrayListWrite.addAll(removeDuplicates);
        
        for(int k=0; k< pathArrayListWrite.size(); k++){
            //System.out.println("pathArrayListWrite.get("+k+"): "+pathArrayListWrite.get(k));
        }

        double availCost[];
        
        // calculate the availability equation
        for(int l=0; l<P.length; l++){
           
           availCost = new double[2];
           availCost = callTruthTableFunc(pathArrayListRead, n, P[l], minVPValue[1]);
 
           yAxisAvailRead.add(availCost[0]);
           yAxisCostRead.add(availCost[1]);
        }
        
        for(int l=0; l<P.length; l++){
           
           availCost = new double[2];
           availCost = callTruthTableFunc(pathArrayListWrite, n, P[l], minVPValue[1]);
 
           yAxisAvailWrite.add(availCost[0]);
           yAxisCostWrite.add(availCost[1]);           
           
        }
        /*
        // for printing the availability of the access operation against the 
        for(int m=0; m<P.length; m++){
            System.out.print(P[m]+"  "+yAxisRead[m]+"\n");
        }
        for(int n=0; n<P.length; n++){            
            System.out.print(P[n]+"  "+yAxisWrite[n]+"\n");            
        }            
        */            
            
        // add the yaxis values of read and write to the list to pass it on to the charting library function (xaxis values 
        // will be the probability of individual replicas P) 
        yAxisAvailCostReadWriteList.add(yAxisAvailRead); // read availability 
        yAxisAvailCostReadWriteList.add(yAxisAvailWrite); // write availability
        yAxisAvailCostReadWriteList.add(yAxisCostRead); // read cost 
        yAxisAvailCostReadWriteList.add(yAxisCostWrite); // write cost 
          
        return yAxisAvailCostReadWriteList;         
    } 


    // to find the maximum number node among the dot graph nodes in order to assign new names accordingly 
    public static int[] findMin(Node rootNode, int[] minVPValue){
        
        // to find the children of the parent node
        int nodeChildren = rootNode.getNodeSize();
        int tempComparison = 0; // temporary var to perform comparison
        
        // loop: number of children
        for(int i=0; i< nodeChildren; i++){   
            
            // if virtual node
            if(rootNode.getNode(i).isLeaf()==false){
            
               // to get the integer in the node name
               tempComparison = Integer.parseInt(rootNode.getNode(i).getNodeName().replaceAll("[^\\d.]", ""));
               
               // comparison to find the min number
               if(minVPValue[0]>tempComparison){
                   
                   minVPValue[0]=tempComparison;
               }
               
               minVPValue=findMin(rootNode.getNode(i), minVPValue);
            }

            // if leaf node 
            else{
                // to get the integer in the node name
                tempComparison = Integer.parseInt(rootNode.getNode(i).getNodeName().replaceAll("[^\\d.]", "")); 
                
                // comparison to find the min number                
                if(minVPValue[1]>tempComparison){
                    
                   minVPValue[1]=tempComparison;
                }
            }
        }
        
        // return int array of size two; stores min number for both the virtual and leaf nodes 
        return minVPValue;
    } 
    
    public static double calculateAvailNew(int sizeArray[], int N, double P){
        
        double eqAvailabilityValue=0;
        int quorum = 0;
        
        for(int i=0; i<sizeArray.length; i++){
            
            quorum = sizeArray[i];
            eqAvailabilityValue += (Math.pow(P, quorum) * Math.pow((1-P),N-quorum));
        }
        
        return eqAvailabilityValue;
    }
    
    
    public static List <List<Double>> returnAvailabilityNew(Node rootNodeHybridStrategy, int n, double[] P){
        
        // to fetch and store the combinations for read and write (valid quorums)
        List<String> pathArrayListRead;
        List<String> pathArrayListWrite;
        // to add the yaxis values (double[] yAxisRead, double[] yAxisWrite) of read and write to this list to pass it on to the charting library function
        List <List<Double>> yAxisReadWriteList = new ArrayList<>();        
        Set<String> removeDuplicates;
        // double arrays to store the yaxis values for the read and write access operations
        List <Double> yAxisRead = new ArrayList<>();
        List <Double> yAxisWrite = new ArrayList<>(); 
        
        
        // to check if the combination ends
        boolean checkParentEnd = true;
   
        // return combinations for the read operation
        pathArrayListRead = findPath(rootNodeHybridStrategy, checkParentEnd, "read");
        System.out.println("\n-Read:");
        
        // format it so that we can pass it as an input to another function to calculate availability equation for the read operation 
        formatOutput(pathArrayListRead);
        
        removeDuplicates = new HashSet<>(pathArrayListRead);
        pathArrayListRead.clear();
        pathArrayListRead.addAll(removeDuplicates);
        
        int sizeArrayR[] = new int[pathArrayListRead.size()]; 
        
        for(int k=0; k< pathArrayListRead.size(); k++){
            System.out.println("pathArrayListRead.get("+k+"): "+pathArrayListRead.get(k));
            String quorum[] = pathArrayListRead.get(k).split(",");
            sizeArrayR[k] = quorum.length;
        
        }        
        
        
        // to check if the combination ends
        checkParentEnd = true;
        
        // return combinations for the write operation
        pathArrayListWrite = findPath(rootNodeHybridStrategy, checkParentEnd, "write");
        System.out.println("\n-Write:");

        // format it so that we can pass it as an input to another function to calculate availability equation for write operation
        formatOutput(pathArrayListWrite);

        removeDuplicates = new HashSet<>(pathArrayListWrite);
        pathArrayListWrite.clear();
        pathArrayListWrite.addAll(removeDuplicates);
        
        int sizeArrayW[] = new int[pathArrayListWrite.size()]; 
                
        for(int k=0; k< pathArrayListWrite.size(); k++){
            System.out.println("pathArrayListWrite.get("+k+"): "+pathArrayListWrite.get(k));
            String quorum[] = pathArrayListWrite.get(k).split(",");
            sizeArrayW[k] = quorum.length;            
        }
           

            
        // calculate the availability equation
        for(int l=0; l<P.length; l++){
            //System.out.println(l);
            yAxisRead.add(calculateAvailNew(sizeArrayR, n, P[l]));
        }
        for(int l=0; l<P.length; l++){
           yAxisWrite.add(calculateAvailNew(sizeArrayW, n, P[l]));
        }
        
        // add the yaxis values of read and write to the list to pass it on to the charting library function (xaxis values 
        // will be the probability of individual replicas P) 
        yAxisReadWriteList.add(yAxisRead);
        yAxisReadWriteList.add(yAxisWrite);
        
        
        
        
        
        
        
        
        
        return yAxisReadWriteList;
    }    

    
    // for calculating the cost
    public static int[] calculateCost(List<String> pathArrayList){
        
        // to store min and max costs 
        int minMaxCost[]= new int[2];
        
        // combination lengths
        int combinationLengths[] = new int[pathArrayList.size()];
        
        // number of combination at each index
        for(int i=0; i< pathArrayList.size(); i++){
           combinationLengths[i] = Arrays.asList(pathArrayList.get(i).split(",")).size();
        }
        // sort the array of lengths to get max and min value
        Arrays.sort(combinationLengths);       
        minMaxCost[0] = combinationLengths[0];
        minMaxCost[1] = combinationLengths[combinationLengths.length-1];        
        
        // return max and min cost in the same variable 
        return minMaxCost;         
    }     
    
    
    public static void modifyQuorums(Node rootNodeStr, String crossOverPoint, int num, List<Node> listRepeatedNodes){
        
        // number of children
        int nodeChildren = rootNodeStr.getNodeSize();
        // repeated node check 
        boolean matchName = false;
                        
        // loop: number of children
        for(int i=0; i< nodeChildren; i++){   
            
            // repeated node check 
            matchName = false;
            for(int j=0; j<listRepeatedNodes.size(); j++){
                if(listRepeatedNodes.get(j)==rootNodeStr.getNode(i)){
                    matchName = true;
                }
            }
            
            if(!matchName){
                

                if(rootNodeStr.getNode(i).getMutable()==true){
                    
                    Node node = new Node();
                    
                    if(rootNodeStr.getNodeName().equals(crossOverPoint)){
                        node = rootNodeStr;
                    }

                    else if(rootNodeStr.getNode(i).getNodeName().equals(crossOverPoint)){
                        
                        node = rootNodeStr.getNode(i);
                    }
                    
                    if(!node.getNodeName().equals("")){
                        double selected = Math.ceil(((double)node.getNodeSize()/100)*20);
                        int count=0;
                        List<Integer> myList = new ArrayList<>();
                        
                        for(int j=0; j<node.getNodeSize(); j++){
                            myList.add(j);
                        }    
                        
                        while(count<selected){
                            
                            int rand = (int) ((Math.random() * (myList.size()-1)));
                            rand = myList.get(rand);
                            
                            node.getNode(rand).setWeight(node.getNode(rand).getWeight()+ num);
                            myList.remove(rand);
                            
                            count++;
                        }
                        
                        double qR = node.getVotesR();
                        double qW = node.getVotesW();
                        double totalWeight = 0;
                        
                        
                        for(int k=0; k<node.getNodeSize(); k++){
                            totalWeight += node.getNode(k).getWeight();
                        }                        
                        
                        while(!(qW > totalWeight/2)){
                            qW++; 
                        }
                        
                        while(!(qR + qW > totalWeight)){
                            qR++; 
                        }            
                        
                        node.setVotesR((int)qR);
                        node.setVotesW((int)qW);                        
                        
                        break;
                    }
                
                    else{
                        listRepeatedNodes.add(rootNodeStr.getNode(i)); 
                    }
                }

                // if virtual node
                if(rootNodeStr.getNode(i).isLeaf()==false){
                    
                    modifyQuorums(rootNodeStr.getNode(i), crossOverPoint, num, listRepeatedNodes);       
                }                    
            }
        }
    } 
    

    public static void flipQuorums(Node rootNodeStr, String crossOverPoint, List<Node> listRepeatedNodes){
        
        // number of children
        int nodeChildren = rootNodeStr.getNodeSize();
        // repeated node check 
        boolean matchName = false;
                        
        // loop: number of children
        for(int i=0; i< nodeChildren; i++){   
            
            // repeated node check 
            matchName = false;
            for(int j=0; j<listRepeatedNodes.size(); j++){
                if(listRepeatedNodes.get(j)==rootNodeStr.getNode(i)){
                    matchName = true;
                }
            }
            
            if(!matchName){
                

                if(rootNodeStr.getNode(i).isSubStrs()==true){
                    
                    Node node = new Node();
                    
                    if(rootNodeStr.getNodeName().equals(crossOverPoint)){
                        node = rootNodeStr;
                    }

                    else if(rootNodeStr.getNode(i).getNodeName().equals(crossOverPoint)){
                        
                        node = rootNodeStr.getNode(i);
                    }
                    
                    if(!node.getNodeName().equals("")){
                        
                        double qR = node.getVotesR();
                        double qW = node.getVotesW();
                        
                        node.setVotesR((int)qW);
                        node.setVotesW((int)qR); 
                                                 
                        return;
                    }
                
                    else{
                        listRepeatedNodes.add(rootNodeStr.getNode(i)); 
                    }
                }

                // if virtual node
                if(rootNodeStr.getNode(i).isLeaf()==false){
                    
                    flipQuorums(rootNodeStr.getNode(i), crossOverPoint, listRepeatedNodes);       
                }                    
            }
        }
    } 

    public static void reduceStructure(Node rootNodeStr, String crossOverPoint, List<Node> listRepeatedNodes){
        
        // number of children
        int nodeChildren = rootNodeStr.getNodeSize();
        // repeated node check 
        boolean matchName = false;
                        
        // loop: number of children
        for(int i=0; i< nodeChildren; i++){   
            
            // repeated node check 
            matchName = false;
            for(int j=0; j<listRepeatedNodes.size(); j++){
                if(listRepeatedNodes.get(j)==rootNodeStr.getNode(i)){
                    matchName = true;
                }
            }
            
            if(!matchName){
                

                if(rootNodeStr.getNode(i).isSubStrs()==true){
                    
                    Node node = new Node();
                    
                    if(rootNodeStr.getNodeName().equals(crossOverPoint)){
                        node = rootNodeStr;
                    }

                    else if(rootNodeStr.getNode(i).getNodeName().equals(crossOverPoint)){
                        
                        node = rootNodeStr.getNode(i);
                        
                    }
                    
                    if(!node.getNodeName().equals("")){
                        
                        node = new Node();
                        node.setNodeName("p0");
                        node.setVotesR((int)0);
                        node.setVotesW((int)0);
                        node.setIsSubStrs(true);
                        node.setMutable(false);     
                        node.setWeight(rootNodeStr.getNode(i).getWeight());
                        
                                
                        return;
                    }
                
                    else{
                        listRepeatedNodes.add(rootNodeStr.getNode(i)); 
                    }
                }

                // if virtual node
                if(rootNodeStr.getNode(i).isLeaf()==false){
                    
                    reduceStructure(rootNodeStr.getNode(i), crossOverPoint, listRepeatedNodes);       
                }                    
            }
        }
    }
    
    public static void performMutation(Node rootNode, List<Double> intraMutationProbs) throws IOException, InterruptedException{      
        
        Random rand = new Random();
        String mutationPoint = "";
        boolean isLeafNode;
        int num =1;
        
        List<String> mutationEvents = new ArrayList<>(Arrays.asList("mutationType1", "mutationType2"));
        double prob = rand.nextDouble(); 
        String event = "";
        
        try{

        
        if(prob <= intraMutationProbs.get(0)){
            event = mutationEvents.get(0);
        }
        else {
            event = mutationEvents.get(1);   
        }
        
        if (event.equals("mutationType1")){ 

            // start counting from 1 because the couting starts after the rootNode which is by default a subStr              
            List<String> numOfSubStrs = new ArrayList();
            //numOfSubStrs.add(rootNode.getNodeName());
            numOfSubStrs = calNumOfSubStrs(rootNode, numOfSubStrs, new ArrayList<Node>());
            
            String search = "V";
            boolean mutBool = false;           
            for(String str: numOfSubStrs) {
                if(str.trim().contains(search)){
                   mutBool = true;
                }
            }

            if(mutBool){
                do{
                    isLeafNode = false; 
                    mutationPoint = numOfSubStrs.get(rand.nextInt(numOfSubStrs.size()));       

                    if(mutationPoint.contains("p")){
                       isLeafNode = true; 
                    }
                    System.out.println("Statement 0: "+ numOfSubStrs);
                }

                // make crossover with either virtual or physical nodes and to avoid having two 
                // complete strategies' swap or two leaf nodes' swap as it doesn't impact the result    
                while(isLeafNode);             

                Node node = new Node();
                node.setVotesR((int)0);
                node.setVotesW((int)0);
                node.setIsSubStrs(true);
                node.setMutable(false);                          
                node.setNodeName("p0");
                //node.setWeight((int)1);

                //convertIntoJPG(rootNode, "sub", 1);
                mutationReplace(rootNode, mutationPoint, node, new ArrayList<Node>()); // replace the specified virtual ndoe by a new physical node 
                //convertIntoJPG(rootNode, "sub", 2);

                int maxVPValue[] = {1,0};
                changeNames(rootNode, maxVPValue, new ArrayList<Node>());

                //convertIntoJPG(rootNode, "sub", 3);
                //reduceStructure(rootNode, crossOverPoint, new ArrayList<Node>());
            }
        }
        
        else if (event.equals("mutationType2")){
            
            List<String> numOfMutables = new ArrayList();
            if(rootNode.getMutable()){
                numOfMutables.add(rootNode.getNodeName());
            }
            numOfMutables = calNumOfMutables(rootNode, numOfMutables, new ArrayList<Node>());  
            
            if(numOfMutables.size()>0){ // if there is any point in the structure for this type of mutation 
                
                do{
                    isLeafNode = false; 
                    mutationPoint = numOfMutables.get(rand.nextInt(numOfMutables.size()));       

                    if(mutationPoint.contains("p")){
                       isLeafNode = true; 
                    }
                }
                // make crossover with either virtual or physical nodes and to avoid having two 
                // complete strategies' swap or two leaf nodes' swap as it doesn't impact the result    
                while(isLeafNode); 
                
                if(rootNode.getNodeSize()>2){
                    modifyQuorums(rootNode, mutationPoint, num, new ArrayList<Node>()); 
                }
            }
        }
        
        } catch (Exception ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
        
            //flipQuorums(rootNode, crossOverPoint, new ArrayList<Node>());
     
    }
    
    public void checkConsistencyWhole(){
        
    }
    
    public static double returnSingleObjfitness(double availability, double cost, int N, double objectiveWeight){

        double fitnessAvail = objectiveWeight * (availability);
        //double fitnessCost = (1.0 - objectiveWeight) * (cost);
        double fitnessCost = (1.0 - objectiveWeight) * (N/(double)cost);
        
        //fitnessCost = fitnessCost;
        
        return fitnessAvail + fitnessCost;
    }

}
