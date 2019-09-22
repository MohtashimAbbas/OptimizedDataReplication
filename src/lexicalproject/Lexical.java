/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicalproject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import java.awt.Color;
import java.awt.Shape;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.swing.WindowConstants;
import static lexicalproject.GeneticP.checkRelaxedFitness;
import static lexicalproject.GeneticP.performCrossoverSingle;
import static lexicalproject.GeneticP.performMutation;
import static lexicalproject.GeneticP.returnSingleObjfitness;
import org.apache.commons.lang.StringUtils;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

/**
 *
 * @author Mohtashim's
 */

// extended voting availability calculation added 
// crossover for extended voting added 

public class Lexical {
    
    public void finalize(){System.out.println("object is garbage collected");}
    
    public static List <List<Node>> strategiesFunc(String strategies[]){
        String nString;
        String strategy;
        int n;
        List <Node> nodeList = new ArrayList<>();
        List <List<Node>> listOfListNodes = new ArrayList<List<Node>>();
        
        ConvertToVoting obj = new ConvertToVoting();
        
        int maxVPValue[] = {0,0};
        
        for(int i=0; i<strategies.length; i++){
            
            nString = strategies[i].substring(strategies[i].indexOf("(") + 1);
            nString = nString.substring(0, nString.indexOf(")"));
            
            n = Integer.parseInt(nString);
            
            strategy = strategies[i].substring(0, strategies[i].indexOf("("));
            
            System.out.println(strategy);
            System.out.println(n);
            
                 
            nodeList = obj.callConvertToVoting(n, strategy, maxVPValue);
            
            Node rootNode = nodeList.get(0);
            
            maxVPValue = findMax(rootNode, maxVPValue);
                    
            listOfListNodes.add(nodeList);
            
        }
        return listOfListNodes;
    }

    
    // to merge two structures/ strategies
    public static void joinSpecific(Node rootNodeStrategy1, Node rootNodeStrategy2, String nodeNameToReplace) {
        
        // children of the parent node
        int nodeChildren = rootNodeStrategy1.getNodeSize();
        
        // loop: number of children 
        for(int i=0; i< nodeChildren; i++){   
         
            // if the node in the structure matches the leaf node to be replaced
            if(rootNodeStrategy1.getNode(i).getNodeName().equals(nodeNameToReplace)){
     
                // replace the node of strategy 1 with rootNode of strategy 2 in order to make a hybrid one 
                rootNodeStrategy1.replaceNode(i, rootNodeStrategy2);
                
            }
            else{

                joinSpecific(rootNodeStrategy1.getNode(i), rootNodeStrategy2, nodeNameToReplace);
            }
        }
        
        // return the hybrid strategy  
        // return Strategy1;
    }  
    
    
    // to merge two structures/ strategies
    public static void joinSpecifics(Node rootNodeStrategy1, Node rootNodeStrategy2, Node originalFixedRootNode, int leafNodeNumber, int nodeNumberToReplace[]) {
        
        // children of the parent node
        int nodeChildren = rootNodeStrategy1.getNodeSize();
        
        // loop: number of children 
        for(int i=0; i< nodeChildren; i++){   
            
            // if the node in the structure matches the leaf node to be replaced
            if(rootNodeStrategy1.getNode(i).isLeaf()){
                
                leafNodeNumber++;
                
                for(int j=0; j<nodeNumberToReplace.length; j++){
                    
                    // if the node in the structure matches the leaf node to be replaced
                    if(rootNodeStrategy1.getNode(i).getNodeName().equals("p"+nodeNumberToReplace[j]) ){

                        //rootNodeStrategy2 = (Node) Node.deepCopy(rootNodeStrategy2); 
                        assignNamesToNodes(originalFixedRootNode, rootNodeStrategy2);
               
                        // replace the node of strategy 1 with rootNode of strategy 2 in order to make a hybrid one 
                        //rootNodeStrategy1.replaceNode(i, (Node) Node.deepCopy(rootNodeStrategy2));
                        rootNodeStrategy1.replaceNode(i, rootNodeStrategy2);
                        break;
                    }
                }
            }
            
            else{

                joinSpecifics(rootNodeStrategy1.getNode(i), rootNodeStrategy2, originalFixedRootNode, leafNodeNumber, nodeNumberToReplace);
            }
        }
        
        // return the hybrid strategy  
        //return Strategy1;
    }  

    
    public static void joinAll(Node rootNodeStrategy1, Node rootNodeStrategy2, Node fixedRootNode){

        // children of the parent node
        int nodeChildren = rootNodeStrategy1.getNodeSize();
        
        // loop: number of children 
        for(int i=0; i< nodeChildren; i++){   
         
            // if the node in the structure matches the leaf node to be replaced
            if(rootNodeStrategy1.getNode(i).isLeaf()){
 
               rootNodeStrategy2 = (Node) Node.deepCopy(rootNodeStrategy2); 
               assignNamesToNodes(fixedRootNode, rootNodeStrategy2);
                
                // replace the node of strategy 1 with rootNode of strategy 2 in order to make a hybrid one 
                rootNodeStrategy1.replaceNode(i, rootNodeStrategy2);
                
 
            }
            else{

                joinAll(rootNodeStrategy1.getNode(i), rootNodeStrategy2, fixedRootNode);
            }
        }        
    }
    
    public static void convertIntoJPG(Node rootNode, String folderName, int graphIndex) throws IOException, InterruptedException{
        
        // delete the folders for hybrid and sub graphs and recreate it for new dot images
        new File("C:\\Users\\Mohtashim's\\Desktop\\images\\"+folderName).delete();
        new File("C:\\Users\\Mohtashim's\\Desktop\\images\\"+folderName).mkdir();
            
            
            // for converting the hybrid strategy into .jpg visual graph
            // rootNodeName and its data to form the dot format graph 
            //String rootNodeName = rootNode.getNodeName()+ "[shape=rect, label=\""+rootNode.getNodeName()+"("+rootNode.getVotesR()+","+rootNode.getVotesW()+") "+rootNode.getWeight()+" "+rootNode.isSubStrs()+"\"] \n";
            
            String rootNodeName = rootNode.getNodeName()+ "[shape=rect, label=\""+rootNode.getNodeName()+"("+rootNode.getVotesR()+","+rootNode.getVotesW()+") "+rootNode.getWeight()+" "+"\"] \n";

            // get the nodes and edges of the graph to pass it on to form a dot format graph and convert it into .jpg file 

            String [] nodesEdges = makeDotGraph(rootNode, new String[]{rootNodeName,""}, new ArrayList<String>());

            nodesEdges[1] = removeDuplicateEdges(nodesEdges[1]);
        
            // to print the nodes and edges of the hybrid graph
            //System.out.println(i);
            System.out.println(nodesEdges[0]);
            System.out.println(nodesEdges[1]);
            
            // put the delay so that the same dot graph won't be converted into JPG again and again
            TimeUnit.SECONDS.sleep(1);
            
            // to make the dot format graph and convert it into .jpg file 
            drawDotGraphJPG(nodesEdges[0], nodesEdges[1], graphIndex, folderName);                    
                
    }
    
    public static String displayDotTxt(Node rootNode){
             
        // rootNodeName and its data to form the dot format graph 
        String rootNodeName = rootNode.getNodeName()+ "[shape=rect, label=\""+rootNode.getNodeName()+"("+rootNode.getVotesR()+","+rootNode.getVotesW()+")\"] \n";
        
        // get the nodes and edges of the graph to pass it on to form a dot format graph and convert it into .jpg file 
        String [] nodesEdges = makeDotGraph(rootNode, new String[]{rootNodeName,""}, new ArrayList<String>());

        nodesEdges[1] = removeDuplicateEdges(nodesEdges[1]);

        // data replication strategy as .dot text
        String graphTxt =   "digraph D {\n" +
                                nodesEdges[0] +
                                nodesEdges[1] +
                                "\n" +
                            "}";           
    
        return graphTxt;
    }
    
     // to make dot format graph
    public static String[] makeDotGraph(Node rootNode, String [] nodesEdges, List<String> listRepeatedNodes){
        
        System.out.println("makeDotGraph.rootNode: "+ rootNode.getNodeName());
        
        int nodeChildren = rootNode.getNodeSize();
        
        for(int i=0; i<nodeChildren; i++){
               
            // if virtual node
            if(rootNode.getNode(i).isLeaf()==false){ 
                
                nodesEdges[1] += rootNode.getNodeName() + " -> " + rootNode.getNode(i).getNodeName()+ ";";
                nodesEdges = makeDotGraph(rootNode.getNode(i), nodesEdges, listRepeatedNodes);
            }
            // if leaf node 
            else{   
                nodesEdges[1] += rootNode.getNodeName() + " -> " + rootNode.getNode(i).getNodeName()+ ";";
            }              
            
            // repeated node check 
            boolean matchName = false;
            for(int j=0; j<listRepeatedNodes.size(); j++){
                if(listRepeatedNodes.get(j).equals(rootNode.getNode(i).getNodeName())){
                    matchName = true;
                }
            }       
            
            if(matchName!=true){
                
                //nodesEdges[0] += rootNode.getNode(i).getNodeName()+ " [votesR="+rootNode.getNode(i).getVotesR()+", votesW="+rootNode.getNode(i).getVotesW()+", shape=rect, label=\""+rootNode.getNode(i).getNodeName()+"("+rootNode.getNode(i).getVotesR()+","+rootNode.getNode(i).getVotesW()+") "+rootNode.getNode(i).getWeight()+" "+rootNode.getNode(i).isSubStrs()+" \", isSub="+rootNode.getNode(i).isSubStrs()+", weight="+rootNode.getNode(i).getWeight()+"]; ";    
                nodesEdges[0] += rootNode.getNode(i).getNodeName()+ " [votesR="+rootNode.getNode(i).getVotesR()+", votesW="+rootNode.getNode(i).getVotesW()+", shape=rect, label=\""+rootNode.getNode(i).getNodeName()+"("+rootNode.getNode(i).getVotesR()+","+rootNode.getNode(i).getVotesW()+") "+rootNode.getNode(i).getWeight()+ " \", isSub="+rootNode.getNode(i).isSubStrs()+", mutable="+rootNode.getNode(i).getMutable()+", weight="+rootNode.getNode(i).getWeight()+"]; ";    
                
                listRepeatedNodes.add(rootNode.getNode(i).getNodeName());
            }
        }
        return nodesEdges;
    } 
    
    // to convert dot format graph into an image 
    public static void drawDotGraphJPG(String nodes, String edges, int graphIndex, String folderName) throws IOException{
        
        // to write the dot format graph into text file
        BufferedWriter writer = null;
        
        try{

            // use graphviz to convert .dot text into .jpg file
            String dotPath = "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe";
            
            // file to store .dot text
            File file = new File("C:\\Users\\Mohtashim's\\Desktop\\images\\"+folderName+"\\input.txt");
            
            // data replication strategy as .dot text
            String graphTxt =   "digraph D {\n" +
                                    nodes +
                                    edges +
                                    "\n" +
                                "}";                      
            
            // write the strategy as .dot text into the .txt file and close the writer  
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(graphTxt);
            writer.close();
            
            // to read the .dot text from
            String fileInputPath = "C:\\Users\\Mohtashim's\\Desktop\\images\\"+folderName+"\\input.txt";
            
            // output path of the graph, that is, the path of the image that is going to be created with graphviz
            String fileOutputPath = "C:\\Users\\Mohtashim's\\Desktop\\images\\"+folderName+"\\myOutput"+graphIndex+".jpg";

            // type of output image, in this case it is jpg
            String tParam = "-Tjpg";
            String tOParam = "-o";        

            // we concatenate our addresses. What I did is create a vector, to be able to edit the input and output addresses, using the variables initialized before

            // remember the command in the windows console:  C: \ Program Files \ Graphviz 2.21 \ bin \ dot.exe -Tjpg grafo1.txt -o grafo1.jpg  This is what we concatenate in the following vector:

            String [] cmd = new String [5]; 
            cmd [0] = dotPath; 
            cmd [1] = tParam; 
            cmd [2] = fileInputPath; 
            cmd [3] = tOParam; 
            cmd [4] = fileOutputPath;        


            // We invoke our class 
            Runtime rt = Runtime.getRuntime();
    
            // Now we execute as we do in console
            rt.exec (cmd);
                    
        } catch (IOException ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        } finally {
            try {
                writer.close();
            } 
            catch (Exception ex) { 
                System.out.println(ex.toString());
            }
        }       
    }   
    
    // to remove duplicate edges from the String of edges 
    public static String removeDuplicateEdges(String edges){
        // split the string on ';' 
        String[] arrayRemoveDuplicates = edges.split(";");
        StringBuilder builder = new StringBuilder();
        
        // remove the repeated values from the string array
        arrayRemoveDuplicates = new HashSet<String>(Arrays.asList(arrayRemoveDuplicates)).toArray(new String[0]);
        
        // remove the other unwanted chars 
        List<String> list = new ArrayList<String>(Arrays.asList(arrayRemoveDuplicates));
        list.removeAll(Arrays.asList(" ", "", null));

        arrayRemoveDuplicates = new String[list.size()];
        arrayRemoveDuplicates = list.toArray(arrayRemoveDuplicates);        
        
        // convert the string array into semicolon separated strings
        for (String s : arrayRemoveDuplicates) {
            builder.append(s).append("; ");
        }  
        // convert back to string
        edges = builder.toString();        
        
        return edges;
    }

    // to find the maximum number node among the dot graph nodes in order to assign new names accordingly 
    public static int[] findMax(Node rootNode, int[] maxVPValue){
        
        // to find the children of the parent node
        int nodeChildren = rootNode.getNodeSize();
        int tempComparison = 0; // temporary var to perform comparison
        
        // loop: number of children
        for(int i=0; i< nodeChildren; i++){   
            
            // if virtual node
            if(rootNode.getNode(i).isLeaf()==false){
            
               // to get the integer in the node name
               tempComparison = Integer.parseInt(rootNode.getNode(i).getNodeName().replaceAll("[^\\d.]", ""));
               
               // comparison to find the max number
               if(maxVPValue[0]<tempComparison){
                   
                   maxVPValue[0]=tempComparison;
               }
               
               maxVPValue=findMax(rootNode.getNode(i), maxVPValue);
            }

            // if leaf node 
            else{
                // to get the integer in the node name
                tempComparison = Integer.parseInt(rootNode.getNode(i).getNodeName().replaceAll("[^\\d.]", "")); 
                
                // comparison to find the max number                
                if(maxVPValue[1]<tempComparison){
                    
                   maxVPValue[1]=tempComparison;
                }
            }
        }
        
        // return int array of size two; stores max number for both the virtual and leaf nodes 
        return maxVPValue;
    }
    
    // to assign new names to the strategy 
    public static Node assignNamesToNodes(Node rootNodeStrategy1, Node rootNodeStrategy2){
         
        // for rootNode of strategy 1
        int maxVPValue[] = new int[]{Integer.parseInt(rootNodeStrategy1.getNodeName().replaceAll("[^\\d.]", "")), 0};
        
        // find max value for the virtual and leaf node among all the nodes for strategy 1
        maxVPValue = findMax(rootNodeStrategy1, maxVPValue);
        
        // root node of strategy2
        String rootNodeName = rootNodeStrategy2.getNodeName();
        
        // change the name of the rootNode for strategy 2
        rootNodeName = rootNodeName.replaceAll("\\d+",""+ ++maxVPValue[0]);        
        rootNodeStrategy2.setNodeName(rootNodeName);         
        
        // change the names of the rest of the nodes for strategy 2
        changeNames(rootNodeStrategy2, maxVPValue, new ArrayList<Node>());
                               
        // return strategy 2 with new names
        return rootNodeStrategy2;
    }
    
    // to change the name of the strategy
    public static int[] changeNames(Node rootNode, int[] maxVPValue, List<Node> listRepeatedNodes){
        
        // number of children
        int nodeChildren = rootNode.getNodeSize();
        String currNodeName = "";
        
        // repeated node check 
        boolean matchName = false;
        
        // loop: number of children
        for(int i=0; i< nodeChildren; i++){   

            // repeated node check 
            matchName = false;
            for(int j=0; j<listRepeatedNodes.size(); j++){
                if(listRepeatedNodes.get(j)==rootNode.getNode(i)){
                    matchName = true;
                }
            }
            
            // System.out.println(rootNode.getNode(i).getNodeName());
            // if virtual node
            if(rootNode.getNode(i).isLeaf()==false){
 
                // if name name is not already updated, update it
                if(!matchName){
                    
                    listRepeatedNodes.add(rootNode.getNode(i));
                    
                    // assign (maxNumber of virtual node + 1) to the other strategy's node
                    maxVPValue[0] = maxVPValue[0] + 1;
                    
                    System.out.println("changeNames.previous: "+rootNode.getNode(i).getNodeName());

                    currNodeName = rootNode.getNode(i).getNodeName().replaceAll("\\d+",""+maxVPValue[0]);                            
                    rootNode.getNode(i).setNodeName(currNodeName);   

                    System.out.println("changeNames.current: "+rootNode.getNode(i).getNodeName());
                    
                    // recursive call to change the names of subsequent nodes
                    maxVPValue = changeNames(rootNode.getNode(i), maxVPValue, listRepeatedNodes);
                }
            }
            // if leaf node 
            else{
               
                
                // if name name is not already updated, update it
                if(!matchName){
                    
                    listRepeatedNodes.add(rootNode.getNode(i));
                    
                    // assign (maxNumber of virtual node + 1) to the other strategy's node                
                    maxVPValue[1] = maxVPValue[1] + 1;
                    
                    System.out.println("changeNames.previous: "+rootNode.getNode(i).getNodeName());
                    
                    currNodeName = rootNode.getNode(i).getNodeName().replaceAll("\\d+",""+maxVPValue[1]);        
                    rootNode.getNode(i).setNodeName(currNodeName);  
                    
                    System.out.println("changeNames.current: "+rootNode.getNode(i).getNodeName());
                    
                     
                }
            }
            //listRepeatedNodes.add(rootNode.getNode(i));   
        }
        
        return maxVPValue;
    }   

    
    // to find the combinations for the availability
    public static List<String> findPath(Node node, boolean checkCombinationEnd, String readOrWrite){

        // initially 'votes' is initialized by -1
        int votes=-1;
        
        // check if the votes are for read or write operation
        if(readOrWrite.equals("read")){
           votes = node.getVotesR();
        }
        else if(readOrWrite.equals("write")){
           votes = node.getVotesW(); 
        }
        
        // get children of the current node 
        int nodeChildren = node.getNodeSize();
        int arr[] = new int[nodeChildren];       
        
        // array for combinations
        for(int j=0; j<nodeChildren; j++){
            arr[j] = j;            
        }
        
        // number of combinations on the indexes of the above array
        int n = arr.length; 
        int totalWeight = 0;
        
        // resulted combinations
        List <int[]> resultList = new ArrayList<>(); 
        
        for(int voteCombinations=votes; voteCombinations>0; voteCombinations--){            
            // combinations according to the votes to store in the resultList
            printCombination(arr, n, voteCombinations, resultList); 

        }

        for(int i=0; i<resultList.size(); i++){
            
            totalWeight = 0;
            
            for(int j=0; j<resultList.get(i).length; j++){
                totalWeight += node.getNode(resultList.get(i)[j]).getWeight();
            }
            
            if(totalWeight < votes){
                //resultList.remove(i);
                resultList.set(i, null);
            }
        }
        
        resultList.removeAll(Collections.singleton(null));
        
        
        
        List<String> doubleTempString = new ArrayList<>();
        
        // boolean to check if the node is virtual
        boolean checkVirtualNode = false;
        
        List <String> returnList = new ArrayList<>();
        
        // to process and store the nodes temporarily in order to put it in the 'doubleTempList'
        String tempString ="";
        // to store the output combinations 
        List<List<String>> doubleTempList = new ArrayList<List<String>>();
        
        for (int i=0; i< resultList.size(); i++){
            
            
            checkVirtualNode = false; 
 
            doubleTempList.add(new ArrayList<>());
            
            for(int j=0; j< resultList.get(i).length; j++){

                // why are you not checking leaf node for the parent
                // if the child is a leaf node
                if(node.getNode(resultList.get(i)[j]).isLeaf()){
                    
                    // add node to the 'tempSrting'
                    tempString += node.getNode(resultList.get(i)[j]).getNodeName();
                                   
                }
                
                // if the child is a virtual node
                else{
                    // it is a virtual node
                    checkVirtualNode = true;
                    
                    // to preserve the previous value of checkParentEnd after the function call
                    boolean tempCheckCombinationEnd = checkCombinationEnd;
                    
                    // when virtual node; not a leaf node 
                    if(!tempString.isEmpty()){
                        appendFunc(doubleTempList.get(i), new ArrayList<>(Arrays.asList(tempString)));   
                        tempString="";
                    }
                    
                    if(checkCombinationEnd){
                        // boolean for checking to put an 'end' at the end of a combination
                        // boolean variable to check if the loop is equal to the expected combinations
                        checkCombinationEnd = j >= resultList.get(i).length-1;  
                    } 
                    
                    appendFunc(doubleTempList.get(i), findPath(node.getNode(resultList.get(i)[j]), checkCombinationEnd, readOrWrite)); 
                    
                    checkCombinationEnd = tempCheckCombinationEnd;
                }
            }
            
            /*
            check all four possible cases:
                    a virtual node and combination ends
                    neither a virtual node, nor the combination ends
                    not a virtual node but the combination ends 
                    a virtual node but the combination doesn't end
            */
                
            if(!tempString.isEmpty()){
                
                // if a virtual node 
                if(checkVirtualNode){          
                    
                    // a virtual node and the combination ends
                    if(checkCombinationEnd){ 

                       // to check all the combinations under virtual node are done
                       appendFunc(doubleTempList.get(i), new ArrayList<>(Arrays.asList(tempString+"end")));                     
                    } 
                    
                    // a virtual node but the combination doesn't end
                    else{ 
                        appendFunc(doubleTempList.get(i), new ArrayList<>(Arrays.asList(tempString)));
                    }
                }
                
                // not a virtual node 
                else{ 
                    // not a virtual node but the combination ends
                    if(checkCombinationEnd){ 

                        // append the 'tempString' with the doubleTempList and put an 'end'  
                        appendFunc(doubleTempList.get(i), new ArrayList<>(Arrays.asList(tempString+"end")));   

                    }          
                    else{ //neither a virtual node nor the combination ends, add the 'tempString' simply to the 'doubleTempList' 
                        doubleTempList.get(i).add(tempString);
                    }                 
                }
            }
            tempString="";
            
        }  
        
        returnList = doubleTempList.stream().flatMap(List::stream).collect(Collectors.toList());
        return returnList;

    }   
    
    // put 'end' when the combination ends   
    public static void appendFunc(List <String> obj1, List<String> obj2){
        
        int tempListSizeObj1 = obj1.size();
        int tempListSizeObj2 = obj2.size();
        
        if(tempListSizeObj1==0 && tempListSizeObj2>0){
            obj1.addAll(obj2);
        }
        else if(tempListSizeObj1>0 && tempListSizeObj2>0){
            for(int i=0; i<tempListSizeObj1; i++){

                if(!obj1.get(i).endsWith("end")){
                    System.out.println("obj1.get(i):"+obj1.get(i));
                    
                    for(int j=0; j< tempListSizeObj2; j++){
                        System.out.println("obj2.get(j):"+obj2.get(j));
                        
                        obj1.add(obj1.get(i)+obj2.get(j));
                    }

                    obj1.remove(i);
                    i--; 
                    tempListSizeObj1--;                
                }
            } 
            if(tempListSizeObj1 == obj1.size()){
                obj1.addAll(obj2);
            }
        }
        //tempListSizeObj1 = obj1.size();
        
        System.gc();
    }
    
    // to format the availability combinations in order to pass it on to the next function to calculate the availability equation 
    public static void formatOutput(List <String> pathArrayList){
        
        String[] array;
        StringBuilder builder = new StringBuilder();
        String formatedOutput;
        
        for(int i=0; i<pathArrayList.size(); i++){
            
            // remove 'end' from the string
            if(pathArrayList.get(i).contains("end")){
               pathArrayList.set(i,pathArrayList.get(i).replaceAll("end", ""));
            }
            
            // array = pathArrayList.get(i).split("(?<=[0-9])");
            
            // split the string on 'p' and add comma after every digit 
            array = pathArrayList.get(i).split("p");
            
            // remove the repeated values from the string array
            array = new HashSet<String>(Arrays.asList(array)).toArray(new String[0]);
            
            // convert the string array into comma separated strings
            for (String s : array) {
                builder.append(s).append(",");
            }              
            formatedOutput = builder.toString();
            pathArrayList.set(i, ""+builder.toString().substring(1, formatedOutput.length()-1)+"");
            builder.setLength(0);
            
            //System.out.println(pathArrayList.get(i));    
        }
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
    
 

    public static List<String> calculateNumOfNodes(Node rootNode, List<String> nodeNames){
   
        int nodeChildren = rootNode.getNodeSize();
        
        for(int i=0; i<nodeChildren; i++){
 
            // if virtual node; recursive call
            if(rootNode.getNode(i).isLeaf()==false){ 

                calculateNumOfNodes(rootNode.getNode(i), nodeNames);
            }

            // if leaf node; increment
            else{   
                
                nodeNames.add(rootNode.getNode(i).getNodeName());
            }                  
        }

        return nodeNames;
    }
    
    public static int returnN(Node rootNode){
        
        List<String> leafNodeNames = new ArrayList<>();
        Set<String> hs = new HashSet<>();
        
        // returns all the leaf nodes' names of a structure as a List<String>
        leafNodeNames = calculateNumOfNodes(rootNode, leafNodeNames);

        // adds the List<String> to to HasSet to remove repeated values automatically
        hs.addAll(leafNodeNames);

        // clears the List<String> to add non-duplicate values from the HashSet back to the List<String> 
        leafNodeNames.clear();
        leafNodeNames.addAll(hs);

        return leafNodeNames.size();
    }    
    

    // to insert a database document 
    public static void  insertDocument(Document document, String tableName){
        MongoClient conn = null;
 
        try{
            // make the database connection
            conn = new MongoClient("localhost",27017);
            
            // database
            MongoDatabase database = conn.getDatabase("myDatabase");
            
            // collection
            MongoCollection<Document> myCollection = database.getCollection(tableName);
            
            // insert a document
            myCollection.insertOne(document);            

            // close the connection
            conn.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
	} 
        finally {
            // in case the connection is not closed, close it
            if (conn != null) {		    
                conn.close(); 
            }
	}
    }   
    
    // to insert a JSON document to the given table of the database  
    public static void makeDBDocument(Node rootNode, int n, String tableName){
            String [] nodesEdges;
            int votesR = rootNode.getVotesR();
            int votesW = rootNode.getVotesW();

             
            // rootNodeName and its data to form the dot format graph 
            String rootNodeName = rootNode.getNodeName()+ " [votesR="+votesR+", votesW="+votesW+", shape=rect, label=\""+rootNode.getNodeName()+"("+votesR+","+votesW+")\", isSub="+rootNode.isSubStrs()+", mutable="+rootNode.getMutable()+", weight="+rootNode.getWeight()+"]; ";
            
            // get the nodes and edges of the graph to pass it on to form a dot format grpah and convert it into .jpg file 
            nodesEdges = makeDotGraph(rootNode, new String[]{rootNodeName,""}, new ArrayList<String>());
            
            nodesEdges[1] = removeDuplicateEdges(nodesEdges[1]);
            
            // data replication strategy as .dot text
            String graphTxt =   "digraph G{" +
                                    " "+ nodesEdges[0] +
                                    ""+ nodesEdges[1] +
                                    "}";   

            Document document = new Document();
            
            document.put("dot", graphTxt);
           
            document.put("N", n); 

            insertDocument(document, tableName);        
    }    
    
    
    // building Java objects from json documents  
    public static List<List<Node>> buildJSONObject(List<Document> documents){
        
        // dot graph string
        String graphTxt; 
        // read and write votes 
        int votesR = 0; 
        int votesW = 0;
        
        int weight = 1;
        
        // priority of the paths in a dot graph
        int priorityR = 0;
        int priorityW = 0;
        // node and nodeName in a dot graph
        Node node = null;
        String nodeName; 
        // temp nodes to temporarily store nodes in order to save the links/edges of a dot graph
        Node tempNode1 =null;
        Node tempNode2 = null;
        // to parse the graph into the list 
        List<String> parsedList;
        // to store the nodes into a nodeList
        List <Node> nodeList = null;
        // double list to add list of nodes of a dot format graph 
        List<List<Node>> doubleList = new ArrayList<>();
        
        int lastIndexOfIsSub;
        boolean isSub = false;
        boolean mutable = false;
        
        int lastIndexOfR;
        int lastIndexOfW;  
        int lastIndexOfWeight;
        int lastIndexOfMutable;
                
        // string array to store the edges of dot graph 
        String edge[];
        
        // loop: number of documents 
        for(int i=0; i<documents.size(); i++){
            
            nodeList = new ArrayList<>();
            // get the dot format graph and format it 
            graphTxt = documents.get(i).get("dot").toString();
            graphTxt = graphTxt.replace("digraph G{", "");
            
            // parse the graph into nodes and edges 
            parsedList = Arrays.asList(graphTxt.split(";"));
            
            // loop: nodes and edges
            for(int j=0; j< parsedList.size(); j++){
                
                node = new Node();
  
                //System.out.println(""+parsedList.get(j));      
                
                // for the nodes 
                if(!(parsedList.get(j).contains("->") || parsedList.get(j).contains("}"))){
                    
                    nodeName = parsedList.get(j).substring(1, parsedList.get(j).indexOf(" ", 1));
                    node.setNodeName(nodeName);
                    //System.out.println("nodeName: "+nodeName);
                    
                    lastIndexOfR = parsedList.get(j).lastIndexOf("votesR=");
                    votesR = Integer.parseInt(""+parsedList.get(j).substring(lastIndexOfR+7, parsedList.get(j).indexOf(",", lastIndexOfR+7)));                    
                    node.setVotesR(votesR);
                    node.setWeight(weight);
                    
                    //System.out.println("votesR: "+votesR);
                    
                    lastIndexOfW = parsedList.get(j).lastIndexOf("votesW=");
                    votesW = Integer.parseInt(""+parsedList.get(j).substring(lastIndexOfW+7, parsedList.get(j).indexOf(",", lastIndexOfW+7)));       
                    node.setVotesW(votesW);
                    node.setWeight(weight);
                    //System.out.println("votesW: "+votesW+"\n");    
        
                    lastIndexOfWeight = parsedList.get(j).lastIndexOf("weight=");
                    weight = Integer.parseInt(""+parsedList.get(j).substring(lastIndexOfWeight+7, parsedList.get(j).indexOf("]", lastIndexOfWeight+7)));                           
                    node.setWeight(weight);
                    
                    
                    lastIndexOfIsSub = parsedList.get(j).lastIndexOf("isSub=");
                    isSub = Boolean.valueOf(""+parsedList.get(j).substring(lastIndexOfIsSub+6, parsedList.get(j).indexOf(",", lastIndexOfIsSub+6)));                     
                    node.setIsSubStrs(isSub);
                   
                    lastIndexOfMutable = parsedList.get(j).lastIndexOf("mutable=");
                    mutable = Boolean.valueOf(""+parsedList.get(j).substring(lastIndexOfMutable+8, parsedList.get(j).indexOf(",", lastIndexOfMutable+8)));                  
                    node.setMutable(mutable);
                    
                    nodeList.add(node);
                }  
                
                // for the edges 
                else if(parsedList.get(j).contains("->")){
                    edge = parsedList.get(j).split(" ");
                    
                    
                    for(int k=0; k<nodeList.size(); k++){
                        
                        if(nodeList.get(k).getNodeName().equals(edge[1])){
                            tempNode1 = nodeList.get(k);
                            //System.out.println("tempNode1: "+tempNode1.getNodeName());
                            //System.out.println("edge[0]: "+edge[1]);
                        }
                        else if(nodeList.get(k).getNodeName().equals(edge[3])){
                            tempNode2 = nodeList.get(k);
                            //System.out.println("tempNode2: "+tempNode2.getNodeName());
                            //System.out.println("edge[2]: "+edge[3]);
                        }    
                    }
                    // adding links for the nodes to form an edge in the data structure 
                    tempNode1.setNode(tempNode2);
                }    
            }
            // add each graph into the doubleList
            doubleList.add(nodeList);
        } 
        return doubleList;
    }

    // fetch and return the JSON documents from the database repository
    public static List<Document> returnDocuments(Bson query, String tableName){
        
        // connection 
        MongoClient conn = null;
        // JSON documents in database
        List<Document> documents = null;
        
        try{
            // make the database connection
            conn = new MongoClient("localhost",27017);
            MongoDatabase database;
            database = conn.getDatabase("myDatabase");
            // specific table documents
            MongoCollection<Document> myCollection = database.getCollection(tableName);
            
            // if filtering
            if(query!=null){             
                // querying the specific documents
                documents = (List<Document>) myCollection.find(query).into(
                        new ArrayList<>());                
            }
            // if there is no filter
            else{
                // querying all the documents
                documents = (List<Document>) myCollection.find().into(
                        new ArrayList<>());                  
            }
            
            // for printing
            /*
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
            */
            
            // close the connection
            conn.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
	} 
        finally {
            // in case the connection is not closed, close it
            if (conn != null) {		    
                    conn.close(); 
		}
	    }
        // return all the JSON documents
        return documents; 
    }    
    
    public static Node lexScannerAndInterpreter(String s){
        
        int leafNodeNumber = 0;
        int pos = s.indexOf(".");
        System.out.println(pos);
        
        String rel = s.substring(pos+1, s.length());

        //System.out.println(rel);

        String[] relations = rel.split("\\.");

        for (int i=0; i< relations.length; i++){
            System.out.println(relations[i]);
        }
        
        String[] strategies = (s.substring(0, pos)).split("\\*");
        
        for (int i=0; i< strategies.length; i++){
            System.out.println(strategies[i]);
        }
        
        if(relations.length==strategies.length-1){
            System.out.println("true");            
        }
        else{
            System.out.println("false");
        }

        List <List<Node>> listSrtategies = strategiesFunc(strategies);

        String[] token1 = rel.split("\\.");
 
        Node fixedRootNode;
          
        Node rootNodeStrategy1 = null;
        Node rootNodeStrategy2;
            
        for(int i=0; i<listSrtategies.size()-1; i++){
            //joinSpecific(listSrtategies.get(i).get(0), listSrtategies.get(i+1).get(0), relations[i]);
            
            fixedRootNode = listSrtategies.get(0).get(0);
            
            rootNodeStrategy1 = listSrtategies.get(0).get(0);
            rootNodeStrategy2 = listSrtategies.get(i+1).get(0);

            
            assignNamesToNodes(rootNodeStrategy1, rootNodeStrategy2);
            


            if(token1[i].equals("@")){
                joinAll(rootNodeStrategy1, rootNodeStrategy2, fixedRootNode);
            }
            else{
                String[] token2 = token1[i].split("&");
                

                int[] nodeNumberToReplace = Arrays.stream(token2).mapToInt(Integer::parseInt).toArray();    
            
                
                joinSpecifics(rootNodeStrategy1, rootNodeStrategy2, fixedRootNode, leafNodeNumber, nodeNumberToReplace);
            } 

            int maxVPValue[] = {1,0};        
            // change the names of the rest of the nodes for strategy 2
            changeNames(rootNodeStrategy1, maxVPValue, new ArrayList<Node>());
            
            //convertIntoJPG(rootNodeStrategy1, "sub");  
        }  

        int n = returnN(rootNodeStrategy1);
        System.out.println(n);
        String tableName = "abc";
        
        makeDBDocument(rootNodeStrategy1, n, tableName);

        /*            
        double[] P = {0.8};
        
        List <List<Double>> yAxisReadWriteList = returnAvailability(rootNodeStrategy1, n, P);
        
        List <List<Double>> yAxisReadList = new ArrayList<>();
        List <List<Double>> yAxisWriteist = new ArrayList<>();
        
        yAxisReadList.add(yAxisReadWriteList.get(0));
        yAxisWriteist.add(yAxisReadWriteList.get(1));
        
        // draw line graph for the availability analysis
        
        XYLineChart lineChartForAvailabilty = new XYLineChart(P, yAxisReadList, yAxisWriteist, 1);
        XYSeriesCollection dataset = lineChartForAvailabilty.initUI(P, yAxisReadList, yAxisWriteist, 1);
        
        lineChartForAvailabilty.setVisible(true);
                  
        */
        
        return rootNodeStrategy1;
    }
    
    public static boolean checkSameStrategy(Node Strategy1, Node Strategy2){
        
        boolean isSame = false;
        
        List<String> pathArrayListRead1 = new ArrayList();
        List<String> pathArrayListWrite1 = new ArrayList();
        
        List<String> pathArrayListRead2 = new ArrayList();
        List<String> pathArrayListWrite2 = new ArrayList();
        
        // to check if the combination ends
        boolean checkParentEnd = true;
        // return combinations for the read operation
        pathArrayListRead1 = findPath(Strategy1, checkParentEnd, "read");
        // format it so that we can pass it as an input to another function to calculate availability equation for the read operation 
        formatOutput(pathArrayListRead1);
        // return combinations for the write operation
        pathArrayListWrite1 = findPath(Strategy1, checkParentEnd, "write");
        // format it so that we can pass it as an input to another function to calculate availability equation for write operation
        formatOutput(pathArrayListWrite1);


        // return combinations for the read operation
        pathArrayListRead2 = findPath(Strategy2, checkParentEnd, "read");
        // format it so that we can pass it as an input to another function to calculate availability equation for the read operation 
        formatOutput(pathArrayListRead2);
        // return combinations for the write operation
        pathArrayListWrite2 = findPath(Strategy2, checkParentEnd, "write");
        // format it so that we can pass it as an input to another function to calculate availability equation for write operation
        formatOutput(pathArrayListWrite2);
        
        if((pathArrayListRead1.containsAll(pathArrayListRead2) && pathArrayListRead2.containsAll(pathArrayListRead1))  
                && (pathArrayListWrite1.containsAll(pathArrayListWrite2) && pathArrayListWrite2.containsAll(pathArrayListWrite1))){
            
            isSame = true;
        }
        
        return isSame;
    }
    
    /*    
    Step 1: Find the mean.
    Step 2: For each data point, find the square of its distance to the mean.
    Step 3: Sum the values from Step 2.
    Step 4: Divide by the number of data points.
    Step 5: Take the square root.   
    */
    public static double calculateSD(List<Double> listFitness){
        
        int size = listFitness.size();
        double mean = listFitness.stream().mapToDouble(Double::doubleValue).sum()/size;
        double temp = 0;

        for (int i=0; i<listFitness.size(); i++) {
            
            double val = listFitness.get(i);

            // Step 2:
            double squrDiffToMean = Math.pow(val - mean, 2);

            // Step 3:
            temp += squrDiffToMean;
        }

        // Step 4:
        double meanOfDiffs = (double) temp / (double) (size);

        // Step 5:
        return Math.sqrt(meanOfDiffs);
    
    }
    
    // to check intersection between read and write quorums 
    public static boolean checkIntersectionRWQ(Node rootNodeStr){
        
        boolean isIntersection = true;
        
        List<List<String>> doubleListRead = new ArrayList<>();
        List<List<String>> doubleListWrite = new ArrayList<>();
        List<String> tempList;
                
        
        boolean checkParentEnd = true;   // to check if the combination ends          
        List<String> pathArrayListRead = findPath(rootNodeStr, checkParentEnd, "read");
        formatOutput(pathArrayListRead);

        List<String> pathArrayListWrite = findPath(rootNodeStr, checkParentEnd, "write");
        formatOutput(pathArrayListWrite); 
        
        
        for(int i=0; i<pathArrayListRead.size(); i++){
            
            doubleListRead.add(new ArrayList<String> (Arrays.asList(pathArrayListRead.get(i).split("\\s*,\\s*"))));
        }
        for(int i=0; i<pathArrayListWrite.size(); i++){
            
            doubleListWrite.add(new ArrayList<String> (Arrays.asList(pathArrayListWrite.get(i).split("\\s*,\\s*"))));
        }
        
/*        
        for(int i=0; i<doubleListRead.size(); i++){
            //System.out.println("read: "+doubleListRead.get(i));
        }
        for(int i=0; i<doubleListWrite.size(); i++){
            //System.out.println("write: "+doubleListWrite.get(i));
        }
*/        

        for(int i=0; i<doubleListRead.size(); i++){
            
            for(int j=0; j<doubleListWrite.size(); j++){
                
                tempList = new ArrayList<>(doubleListRead.get(i));
                tempList.retainAll(doubleListWrite.get(j));
                
                if(tempList.isEmpty()){
                    
                    System.out.println("RQ: "+ StringUtils.join(doubleListRead.get(i), ','));
                    System.out.println("WQ: "+ StringUtils.join(doubleListWrite.get(j), ','));
                    
                    isIntersection = false;
                    return isIntersection;
                }
            }
        }
        return isIntersection;
    }
    
    
    public static void check1SR(Node rootNode, List<Node> listRepeatedNodes){
        
        // number of children
        int nodeChildren = rootNode.getNodeSize();       
        // repeated node check 
        boolean matchName = false;
        
        int totalWeight = 0;
        int qR = 0;
        int qW = 0;
        
        // loop: number of children
        for(int i=0; i< nodeChildren; i++){   

            // repeated node check 
            matchName = false;
            for(int j=0; j<listRepeatedNodes.size(); j++){
                if(listRepeatedNodes.get(j)==rootNode.getNode(i)){
                    matchName = true;
                }
            }
            
            // System.out.println(rootNode.getNode(i).getNodeName());
            // if virtual node
            if(rootNode.getNode(i).isLeaf()==false){
 
                // if name name is not already updated, update it
                if(!matchName){
                    
                    qR = rootNode.getVotesR();
                    qW = rootNode.getVotesW();
                    
                    for(int k=0; k<nodeChildren; k++){
                        
                        totalWeight += rootNode.getNode(k).getWeight();
                    }
                    
                    if( !(qW > totalWeight/2 && qR + qW > totalWeight) ){
                        
                    }
                    // recursive call to change the names of subsequent nodes
                    check1SR(rootNode.getNode(i), listRepeatedNodes);
                }
            }

            listRepeatedNodes.add(rootNode.getNode(i));   
        }
           
    } 
            
    public static void deleteCollections(){
    
        // connection 
        MongoClient conn = null;
        
        try{
            // make the database connection
            conn = new MongoClient("localhost",27017);
            MongoDatabase database = conn.getDatabase("myDatabase");
            
            MongoIterable collectionNames = database.listCollectionNames();
            MongoCursor<String> cursor = collectionNames.iterator();
            
            while(cursor.hasNext()){
                
                String table = cursor.next();
                
                if(table.contains("mu") || table.contains("lamda")){
                
                    database.getCollection(table).drop();                        
                }
            }
               
            // close the connection
            conn.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
	} 
        finally {
            // in case the connection is not closed, close it
            if (conn != null) {		    
                conn.close(); 
            }
	}
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        
        
        ///////////////////////
        ///////////////////////
        //
        // User Constraints
        //
        ///////////////////////
        ///////////////////////
        
        double[] P = {0.7}; // probability of individual replicas 
        
        List<Double> uReadAvail = Arrays.asList(0.90); // scenario specific desired read and write availibilities 
        List<Double> uWriteAvail = Arrays.asList(0.90); // scenario specific desired read and write availibilities       
        double uSumReadAvail = uReadAvail.stream().mapToDouble(Double::doubleValue).sum(); // sum of the defined availabilities of the read to be used for determining the fitness 
        double uSumWriteAvail = uWriteAvail.stream().mapToDouble(Double::doubleValue).sum(); // // sum of the defined availabilities of the write to be used for determining the fitness 
        
        // scenario specific desired number of replicas and their costs 
        int uN = 16;
        
        List<Double> uExpReadCost = Arrays.asList(2.0);
        List<Double> uExpWriteCost = Arrays.asList(2.0);        
        double uSumExpReadCost = uExpReadCost.stream().mapToDouble(Double::doubleValue).sum(); 
        double uSumExpWriteCost = uExpWriteCost.stream().mapToDouble(Double::doubleValue).sum(); 
        
        int uMinReadCost = 0; 
        int uMinWriteCost = 0; 
        int uMaxReadCost = 0;         
        int uMaxWriteCost = 0;   
        
        double fitnessWeightage = 0.8;

        double uf = returnSingleObjfitness(uSumReadAvail+uSumWriteAvail, uSumExpReadCost+uSumExpWriteCost, uN, fitnessWeightage); // user fitness
        double sf = 0; // strategy fitness
        
        System.out.println("Scenario");
        System.out.println("    Availability: "+ (uSumReadAvail+uSumWriteAvail));
        System.out.println("    Cost: "+ (uSumExpWriteCost+uSumExpWriteCost));
        System.out.println("    N: "+ uN);
        System.out.println("    Scenario fitness: "+ uf);
        
        /////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////
        //
        // Generating and inserting structures in repository
        //
        /////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////
        
        //String s = "MCS(3)*MCS(3)*ROWA(3)*ROWA(2).@.@.1&2";      
        //String s = "MCS(3)*MCS(3)*ROWA(3)*ROWA(2).p3.p2.p3";
        //String s = "MCS(3)*MCS(3)*ROWA(2)*MCS(4).@.1&6&8.@";
        //String s = "MCS(3)*MCS(4)*ROWA(4).@.1&6&8";
        //String s = "TQP(3)*TQP(3)*MCS(5).2&4.7";
        //String s = "MCS(3)*GRID(4).@";
        //String s = "MCS(3)*ROWA(3).@";
        //String s = "ROWA(3)*GRID(9).2&3";
        
        //lexScannerAndInterpreter(s);
        
        
        int mu = 13; // number of parents 
        int lamda = 30; // number of children
        int gen = 0; // generations count
        int muCount = 1; // counter for mu
        int lamdaCount = 1;// counter for lamda
        
        deleteCollections(); // delete all existing tables before creating new ones 
        
        String tblName = "All9"; // parent population
        //String tblName = "pop16"; // parent population
        String tblNameMu = "mu1"; // parent population
        String fldrNameMu = "mu1"; // folder for parent population        
        String tblNameLamda = "lamda1"; // parent population
        String fldrNameLamda = "lamda1"; // folder for parent population
        
        GeneticP.FitnessClass objFitness; // to store the fitness and other properties of a replication structure 
        //List <GeneticP.FitnessClass> selectedStrList = new ArrayList<>(); // to store the replication structures and their properties by applying a loose selection criteria with defined tolerence percentage    

        // to store the replication structures and their properties by applying a loose selection criteria with defined tolerence percentage    
        List <GeneticP.FitnessClass> muList = new ArrayList<>();
        List <GeneticP.FitnessClass> lambdaList = new ArrayList<>();
        List <GeneticP.FitnessClass> initialParentList = new ArrayList<>();
        
        List<Document> documents = returnDocuments(null, tblName); // fetch the documents from the database and if the argument is null, fetch all otherwise pass the query as an argument 
        List<List<Node>> doubleList = buildJSONObject(documents); // parsing of the database graph into formated replication strategy 
        
        //List<Node> listStrategies = new ArrayList<>();               
        Node rootNode;
        int N;
        
        // availability graph
        XYLineChart lineChartForAvailabilty = new XYLineChart(); // draw line graph for the availability analysis
        lineChartForAvailabilty.setVisible(true); // to draw the empty graph panel for availability 
        XYSeriesCollection dataset = new XYSeriesCollection(); // availability dataset for graph
        // fitnesslist, readFitnessList, writeFitnessList and size of the dataset
        dataset = lineChartForAvailabilty.initUI(new ArrayList<Double>(), new ArrayList<Double>(), new ArrayList<Double>(), new Integer (0));
        
        // population graph
        XYLineChartPop lineChartForPopulation = new XYLineChartPop(); // draw line graph for the population analysis
        lineChartForPopulation.setVisible(true); // to draw the empty graph panel for population
        XYSeriesCollection datasetPopulation = new XYSeriesCollection(); // population dataset for graph
        // fitnesslist and size of the dataset
        datasetPopulation = lineChartForPopulation.initUI(new ArrayList<Double>(), new Integer (0));

        // cost graph
        XYLineChartCost lineChartForCost= new XYLineChartCost(); // draw line graph for the population analysis
        lineChartForCost.setVisible(true); // to draw the empty graph panel for population
        XYSeriesCollection datasetCost = new XYSeriesCollection(); // population dataset for graph
        // fitnesslist and size of the dataset
        datasetCost = lineChartForCost.initUI(new ArrayList<Double>(), new ArrayList<Integer>());
        
        // pareto front
        ScatterPlot2 pareto = new ScatterPlot2(); 
        DefaultXYZDataset dataSetPareto = new DefaultXYZDataset();        
        dataSetPareto = pareto.initUI(new ArrayList<Double>(), new ArrayList<Double>(), dataSetPareto);
        
        XYPlot plot = (XYPlot)pareto.chart.getXYPlot();
        plot.setBackgroundPaint(new Color(255,228,196)); 
        Shape itemShape = ShapeUtilities.createDiagonalCross(3, 1);

        pareto.setSize(800, 400);  
        pareto.setLocationRelativeTo(null);  
        pareto.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  
        pareto.setVisible(true);  

        // to calculate fitness and feasibility of the population of parent strategies 
        List<Double> fitnessList = new ArrayList<>();
        Double fitnessValue = 0.0;
        List<Double> readFitnessList = new ArrayList<>();
        Double readfitnessV = 0.0;
        List<Double> writeFitnessList = new ArrayList<>();
        Double writefitnessV = 0.0;
  
        boolean fitness = false; // to check if the fitness is acheived or not 
        
        
        //////////////////////////////
        //////////////////////////////
        //
        // Calculate parents' fitness
        //
        //////////////////////////////
        //////////////////////////////

        
        for(int i=0; i<doubleList.size(); i++){ // process the parent population 
            
            rootNode = doubleList.get(i).get(0);
            //N = returnN(rootNode);
            //yAxisReadWriteList = returnAvailability(rootNode, N, P);  // to calculate the availability of the replication structures of the parent population 
            
            // apply relaxed fitness criteria 
            objFitness = new GeneticP.FitnessClass();
            objFitness = checkRelaxedFitness(rootNode, P, uSumReadAvail, uSumWriteAvail, uN, fitnessWeightage, uSumExpReadCost, uSumExpWriteCost, uMinReadCost, uMinWriteCost, uMaxReadCost, uMaxWriteCost);
            

            if(objFitness.getIsFit()){ // if the strategy meets our threshold
                
                muList.add(objFitness);
                initialParentList.add(objFitness);
                
                if(objFitness.getSingleObjFitness() >= uf){ // check if the fitness meets the desired user fitness criteria 

                    // no need to run the genetic algorithm since replication structure from the initial poplulation meets the desired user criteria 
                    fitness = true;


                    // plot the values one by one for each replication structure of parent population 
                    //dataset.getSeries(0).add(dataset.getSeries(0).getItemCount()+1, fitnessValue);  

                    System.out.println("\nsuitable strategy found, fitness    : "+objFitness.getSumReadWriteAvail());
                    System.out.println("suitable strategy found, read fitness : "+objFitness.getSumReadAvail());
                    System.out.println("suitable strategy found, write fitness: "+objFitness.getSumWriteAvail());
                    System.out.println("\n");

                    break;
                }
            }         
        }        
        
        
        for(int i=0; i< initialParentList.size(); i++){
            convertIntoJPG(initialParentList.get(i).getStrategy(), "initialParentList", i+1);
        }
   
        // sort the selected strategies in the reversed order and take the mu best strategies to pursue further
        muList.sort(Comparator.comparing(GeneticP.FitnessClass::getSingleObjFitness));    
           
        muList = muList.subList(Math.max(muList.size() - mu, 0), muList.size()); // mu best parents 
             
        //List <GeneticP.FitnessClass> initialParentList = new ArrayList<GeneticP.FitnessClass>(selectedStrList); // maintain initial parent population    
        GeneticP.FitnessClass bestFit = muList.get(muList.size()-1); // to keep track of the best strategy so far 
        
        for(int i=0; i<muList.size(); i++){
            
            rootNode = muList.get(i).getStrategy();
            
            readFitnessList.add(muList.get(i).getSumReadAvail());
            writeFitnessList.add(muList.get(i).getSumWriteAvail());
            
            fitnessValue = muList.get(i).getSingleObjFitness();
            fitnessList.add(fitnessValue);
            
            // plot the values one by one for each replication structure of parent population 
            dataset.getSeries(0).add(dataset.getSeries(0).getItemCount()+1, muList.get(i).getSingleObjFitness()); 
            dataset.getSeries(1).add(dataset.getSeries(1).getItemCount()+1, muList.get(i).getSumReadAvail()); 
            dataset.getSeries(2).add(dataset.getSeries(2).getItemCount()+1, muList.get(i).getSumWriteAvail()); 
            
            datasetCost.getSeries(0).add(datasetCost.getSeries(0).getItemCount()+1, muList.get(i).getSumReadCost() + muList.get(i).getSumWriteCost());
            datasetCost.getSeries(1).add(datasetCost.getSeries(1).getItemCount()+1, muList.get(i).getN());
            //datasetCost.getSeries(2).add(datasetCost.getSeries(2).getItemCount()+1, selectedStrList.get(i).getSumWriteCost());

            dataSetPareto.addSeries("s"+(dataSetPareto.getSeriesCount()+1),(new double[][] {{muList.get(i).getSumReadWriteAvail()}, {muList.get(i).getSumReadCost() + muList.get(i).getSumWriteCost()}, {(double)dataSetPareto.getSeriesCount()+1}}));
            plot.getRenderer().setSeriesShape(dataSetPareto.getSeriesCount()-1, itemShape);
            
            // convert initial popultion to .jpgs and insert replication strategies as a json documents in db
            convertIntoJPG(rootNode, fldrNameMu, dataset.getSeries(0).getItemCount());
            makeDBDocument(muList.get(i).getStrategy(), muList.get(i).getN(), tblNameMu);
        
            //System.out.println("Strategy"+dataset.getSeries(0).getItemCount()+": "+checkIntersectionRWQ(rootNode));
        }
                
         
        System.out.println("\navg fitness: "+fitnessList.stream().mapToDouble(Double::doubleValue).sum()/fitnessList.size());
        //Collections.sort(fitnessList);
        System.out.println("best fitness: "+bestFit.getSumReadWriteAvail()+"\n");
        
        // plot the values one by one for each replication structure of parent population 
        datasetPopulation.getSeries(0).add(datasetPopulation.getSeries(0).getItemCount()+1, fitnessList.get(fitnessList.size()-1));
             
        
        ////////////////////////////////////////////
        ////////////////////////////////////////////
        //
        // Genetic algorithm  
        //
        ////////////////////////////////////////////
        ////////////////////////////////////////////
        
        Random rand = new Random();
        
        double probInitialParentList = 0.3;
        double mutationProb = 0.2;
        List<Double> intraMutationProbs = new ArrayList<>(Arrays.asList(0.5, 0.5)); // mutationType1: reduceStructure, mutationType2: changeVoting
        //List<Double> intraCrossoverProbs = new ArrayList<>(Arrays.asList(0.1, 0.2, 0.3, 1.0)); // type1: complete strategies horizontally by a new rootNode, type2: substrategies horizontally by a new rootNode, type3: attach a complete strategy to another complete strategy as a child, type4: rest of the crossovers
        List<Double> intraCrossoverProbs = new ArrayList<>(Arrays.asList(0.2, 0.4, 0.6, 1.0)); // type1: complete strategies horizontally by a new rootNode, type2: substrategies horizontally by a new rootNode, type3: attach a complete strategy to another complete strategy as a child, type4: rest of the crossovers       
        
        
        // run the algorithm until the desired fitness level is achieved 
        while(fitness != true) {
                     
            readFitnessList.clear();
            writeFitnessList.clear();
            fitnessList.clear();
            lambdaList.clear();
            
            gen++; // generation count   
            muCount++;
            
            tblNameLamda = tblNameLamda.replaceAll("\\d","").concat(""+lamdaCount); // table name to insert graphs
            fldrNameLamda = fldrNameLamda.replaceAll("\\d","").concat(""+lamdaCount); // folder name to put .jpgs              
            
            tblNameMu = tblNameMu.replaceAll("\\d","").concat(""+muCount); // table name to insert graphs
            fldrNameMu = fldrNameMu.replaceAll("\\d","").concat(""+muCount); // folder name to put .jpgs           
          
/*            
            for(int i=0; i<selectedStrList.size(); i++){
                listStrategies.add(selectedStrList.get(i).getStrategy());
            }
*/
            
            // to store replication structure by applying a loose selection criteria with defined tolerence percentage  
            //selectedStrList.clear();
            int count = 0;
            int rand1;
            int rand2;
            Node rootNodeStr1;
            Node rootNodeStr2;
            
            while((count<lamda) && (fitness!= true)){
                
                rand1 = rand.nextInt(muList.size());
                rand2 = rand.nextInt(muList.size());

                System.out.println("rand1: "+rand1);
                System.out.println("rand2: "+rand2);
        
                rootNodeStr1 = muList.get(rand1).getStrategy();
                
                if(rand.nextInt() <= probInitialParentList){
                    
                    rand2 = rand.nextInt(initialParentList.size());
                    rootNodeStr2 = initialParentList.get(rand2).getStrategy();
                }
                else{
                    rootNodeStr2 = muList.get(rand2).getStrategy();
                }
                convertIntoJPG(rootNodeStr1, "sub", 1);
                convertIntoJPG(rootNodeStr2, "sub", 2);

                List<Node> crossedOverList = crossedOverList = performCrossoverSingle((Node) Node.deepCopy(rootNodeStr1), (Node) Node.deepCopy(rootNodeStr2), intraCrossoverProbs); // perform crossover to generte offspring    
                
                System.out.println("statement 10: " + crossedOverList.size());
                // check fitness of the new replication structures one by one which came into existence through cross over
                for(int i=0; i<crossedOverList.size(); i++){
                    
                    rootNode = crossedOverList.get(i);
                    
                    if(rand.nextDouble()<= mutationProb){ 
                        System.out.println("statement 10.1");
                        performMutation(rootNode, intraMutationProbs);
                    }
                    System.out.println("statement 11");
                    // apply relaxed fitness criteria 
                    objFitness = new GeneticP.FitnessClass();
                    objFitness = checkRelaxedFitness(rootNode, P, uSumReadAvail, uSumWriteAvail, uN, fitnessWeightage, uSumExpReadCost, uSumExpWriteCost, uMinReadCost, uMinWriteCost, uMaxReadCost, uMaxWriteCost);

                    System.out.println("statement 12");
                    
                    // if it meets the relaxed fitness criteria, put it in the selection list to use it for further crossovers
                    if(objFitness.getIsFit() == true){
                    
                        System.out.println("statement 13");
                        count++;
                        
                        readfitnessV = objFitness.getSumReadAvail();
                        writefitnessV = objFitness.getSumWriteAvail();

                        fitnessValue = objFitness.getSingleObjFitness();   
                        
                        // plot the fitness of the off-spring replication structure 
                        dataset.getSeries(0).add(dataset.getSeries(0).getItemCount()+1, fitnessValue);  
                        dataset.getSeries(1).add(dataset.getSeries(1).getItemCount()+1, readfitnessV);
                        dataset.getSeries(2).add(dataset.getSeries(2).getItemCount()+1, writefitnessV);


                        datasetCost.getSeries(0).add(datasetCost.getSeries(0).getItemCount()+1, objFitness.getSumReadCost() + objFitness.getSumWriteCost());                        
                        datasetCost.getSeries(1).add(datasetCost.getSeries(1).getItemCount()+1, objFitness.getN());
                        //datasetCost.getSeries(2).add(datasetCost.getSeries(2).getItemCount()+1, objFitness.getSumWriteCost());
            
           
                        dataSetPareto.addSeries("s"+(dataSetPareto.getSeriesCount()+1),(new double[][] {{readfitnessV + writefitnessV}, {objFitness.getSumReadCost() + objFitness.getSumWriteCost()}, {(double)dataSetPareto.getSeriesCount()+1}}));
                        plot.getRenderer().setSeriesShape(dataSetPareto.getSeriesCount()-1, itemShape);
                        
                        lambdaList.add(objFitness);
                        
                        System.out.println("statement 14");
                        
                        // convert into .jpg image file and put the jason into db for lamda    
                        convertIntoJPG(objFitness.getStrategy(), fldrNameLamda.replaceAll("\\d","").concat(""+(lamdaCount)), (dataset.getSeries(0).getItemCount()));                     
                        makeDBDocument(objFitness.getStrategy(), objFitness.getN(), tblNameLamda.replaceAll("\\d","").concat(""+(lamdaCount)));
                        
                        //System.out.println("Strategy"+dataset.getSeries(0).getItemCount()+": "+checkIntersectionRWQ(objFitness.getStrategy()));
                              
                        
                        // and if it also meets the desired fitness criteria defined by the user stop the algorithm and put it in the 
                        // initial population for future use 
                        if(fitnessValue >= uf){

                            fitness = true;
                            makeDBDocument(objFitness.getStrategy(), objFitness.getN(), tblName);            

                            System.out.println("\nsuitable strategy found, fitness    : "+fitnessValue);
                            System.out.println("suitable strategy found, read fitness : "+readfitnessV);
                            System.out.println("suitable strategy found, write fitness: "+writefitnessV);
                            System.out.println("\n");                        

                            break;
                        } 
                        
                        System.out.println("statement 15: "+count);
                        if(!(count < lamda)){
                            
                            break;
                        }
                    } 
                }      
            }
            muList.addAll(lambdaList); // mu + lamda 
            muList.sort(Comparator.comparing(GeneticP.FitnessClass::getSingleObjFitness)); // sorts the fitness value in a descending order        
            muList = muList.subList(Math.max(muList.size() - mu, 0), muList.size()); // selects mu best strategies 

            for(int i=0; i< mu; i++){
                
                readFitnessList.add(muList.get(i).getSumReadAvail());
                writeFitnessList.add(muList.get(i).getSumWriteAvail());
                fitnessList.add(muList.get(i).getSingleObjFitness());   
                
                // insert offsring to another table
                N = muList.get(i).getN();

                // plot the values one by one for each replication structure of parent population 
                dataset.getSeries(0).add(dataset.getSeries(0).getItemCount()+1, muList.get(i).getSingleObjFitness()); 
                dataset.getSeries(1).add(dataset.getSeries(1).getItemCount()+1, muList.get(i).getSumReadAvail()); 
                dataset.getSeries(2).add(dataset.getSeries(2).getItemCount()+1, muList.get(i).getSumWriteAvail()); 


                datasetCost.getSeries(0).add(datasetCost.getSeries(0).getItemCount()+1, muList.get(i).getSumReadCost() + muList.get(i).getSumWriteCost());
                datasetCost.getSeries(1).add(datasetCost.getSeries(1).getItemCount()+1, muList.get(i).getN());
                //datasetCost.getSeries(2).add(datasetCost.getSeries(2).getItemCount()+1, selectedStrList.get(i).getSumWriteCost());
                        
                //datasetCost.addValue(selectedStrList.get(i).getSumReadCost(), "Cost(r)", "Strategy "+ datasetCost.getRowCount()+1);
                //datasetCost.addValue(selectedStrList.get(i).getSumWriteCost(), "Cost(w)", "Strategy "+ datasetCost.getRowCount());
                       
                dataSetPareto.addSeries("s"+(dataSetPareto.getSeriesCount()+1),(new double[][] {{muList.get(i).getSumReadWriteAvail()}, {muList.get(i).getSumReadCost() + muList.get(i).getSumWriteCost()}, {(double)dataSetPareto.getSeriesCount()+1}}));
                plot.getRenderer().setSeriesShape(dataSetPareto.getSeriesCount()-1, itemShape);
            
                // convert into .jpg image file and put the jason into db for mu 
                convertIntoJPG(muList.get(i).getStrategy(), fldrNameMu.replaceAll("\\d","").concat(""+(muCount)), (dataset.getSeries(0).getItemCount()));
                makeDBDocument(muList.get(i).getStrategy(), N, tblNameMu.replaceAll("\\d","").concat(""+(muCount)));
                
                //System.out.println("Strategy"+dataset.getSeries(0).getItemCount()+": "+checkIntersectionRWQ(muList.get(i).getStrategy()));
            }
                  
        
            System.out.println("\nGen no.: "+gen); 
            System.out.println("\nGen size: "+readFitnessList.size()); 
            
            System.out.println("\navg fitness: "+fitnessList.stream().mapToDouble(Double::doubleValue).sum()/fitnessList.size());                     
            Collections.sort(fitnessList);
            System.out.println("\nbest fitness: "+fitnessList.get(fitnessList.size()-1));
            
            // plot the values one by one for each replication structure of parent population 
            datasetPopulation.getSeries(0).add(datasetPopulation.getSeries(0).getItemCount()+1, fitnessList.get(fitnessList.size()-1));


            lamdaCount++;
        }             
    }
}