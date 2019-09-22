/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicalproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static lexicalproject.Lexical.convertIntoJPG;
import static lexicalproject.Lexical.makeDBDocument;
import static lexicalproject.Lexical.returnN;

/**
 *
 * @author Mohtashim's
 */
public class ConvertToVoting {
    
    public List <Node> callConvertToVoting(int n, String strategyName, int maxVPValue[]){
        
        List <Node> nodeList = new ArrayList<>();
        
        if (strategyName.equals("MCS")){
            
            nodeList = mcsToVoting(n, maxVPValue);
        }
        else if (strategyName.equals("ROWA")){
            
            nodeList = rowaToVoting(n, maxVPValue);
        }
        else if (strategyName.equals("TQP")){
            
            nodeList = tqpToVoting(n, maxVPValue);
        }      
        else if (strategyName.equals("GRID")){
            
            //nodeList = gridToVoting2(n, maxVPValue);

        }   
        
        return nodeList;
    }

    public List<Node> mcsToVoting(int n, int maxVPValue[]){
        
        List <Node> nodeList = new ArrayList<>();
        Node node = new Node();
        Node tempNode = new Node();
        int votesR = 0;
        int votesW = 0;        
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1));
        
        // even
        if(n%2==0){
            
            votesR = n/2;
            votesW = (n/2)+1;
        }
        // odd
        else{
            votesR = (n+1)/2;
            votesW = (n+1)/2;
        }

        node.setVotesR(votesR);
        node.setVotesW(votesW);
        
        tempNode = node;

        node.setIsSubStrs(true);
        
        nodeList.add(node);
        
        for(int i=0; i<n; i++){
            
            node = new Node();
            node.setNodeName("p"+(maxVPValue[1] = maxVPValue[1]+1));
            node.setVotesR(0);
            node.setVotesW(0);
            tempNode.setNode(node);
        }
        
        return nodeList;
    }
    
    public List<Node> rowaToVoting(int n, int maxVPValue[]){
        
        List <Node> nodeList = new ArrayList<>();
        Node node = new Node();
        Node tempNode = new Node();       
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1));
        
        node.setVotesR(1);
        node.setVotesW(n);
        tempNode = node;
        
        node.setIsSubStrs(true);
        
        nodeList.add(node);
        
        for(int i=0; i<n; i++){
            
            node = new Node();
            node.setNodeName("p"+(maxVPValue[1]= maxVPValue[1]+1));
            node.setVotesR(0);
            node.setVotesW(0);
            tempNode.setNode(node);
        }
        
        return nodeList;        
    }

    public List<Node> specifiedVoting(int n, int votesR, int votesW,int maxVPValue[]){
        
        List <Node> nodeList = new ArrayList<>();
        Node node = new Node();
        Node tempNode = new Node();       
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1));
        
        node.setVotesR(votesR);
        node.setVotesW(votesW);
        tempNode = node;
        
        node.setIsSubStrs(true);
        
        nodeList.add(node);
        
        for(int i=0; i<n; i++){
            
            node = new Node();
            node.setNodeName("p"+(maxVPValue[1]= maxVPValue[1]+1));
            node.setVotesR(0);
            node.setVotesW(0);
            tempNode.setNode(node);
        }
        
        return nodeList;        
    }
    
    public List<Node> tqpToVoting(int n, int maxVPValue[]){
        List <Node> nodeList = new ArrayList<>();

        Node node = new Node();
        Node tempNode = new Node(); 
        
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1));
        node.setVotesR(1);
        node.setVotesW(2);
        
        tempNode = node;
        
        node.setIsSubStrs(true);
        nodeList.add(node);
 
        node = new Node();
        node.setNodeName("p"+(maxVPValue[1] = maxVPValue[1]+1));
        node.setVotesR(0);
        node.setVotesW(0);
        
        nodeList.add(node);
        tempNode.setNode(node);        

        node = new Node();              
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1));
        node.setVotesR(2);
        node.setVotesW(2);
        
        node.setIsSubStrs(true);
        
        nodeList.add(node);
        tempNode.setNode(node);   
        
        tempNode = node;
        
        for(int i=0; i<n; i++){
            
            node = new Node();
            node.setNodeName("p"+(maxVPValue[1] = maxVPValue[1]+1));
            node.setVotesR(0);
            node.setVotesW(0);
            
            nodeList.add(node);
            tempNode.setNode(node);
           
        }
        
        return nodeList;
    }
    
    
    public List<Node> gridToVoting(int n, int maxVPValue[]){
        List <Node> nodeList = new ArrayList<>();

        Node node = new Node();
        Node tempNode = new Node(); 
        
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v1 - v2 v3
        node.setVotesR(1);
        node.setVotesW(2);
        
        tempNode = node;
        
        node.setIsSubStrs(true);
        nodeList.add(node);
 
        node = new Node();
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v2
        node.setVotesR(2);
        node.setVotesW(2);
        
        nodeList.add(node);
        tempNode.setNode(node);        

        Node temp2 = node;
        
        node = new Node();
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v3
        node.setVotesR(1);
        node.setVotesW(1);        
        
        nodeList.add(node);
        tempNode.setNode(node); 
        
        Node temp3 = node;
        
        node = new Node();
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v4
        node.setVotesR(1);
        node.setVotesW(1);        
        
        nodeList.add(node);
        temp2.setNode(node); 
        
        Node temp4 = node; 
                
        node = new Node();              
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v5
        node.setVotesR(1);
        node.setVotesW(1); 
        
        nodeList.add(node);
        temp2.setNode(node);   
        
        Node temp5 = node;
        

        node = new Node();              
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v6
        node.setVotesR(2);
        node.setVotesW(2);
        
        nodeList.add(node);
        temp3.setNode(node);  
        
        Node temp6 = node;
        
        node = new Node();              
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v7
        node.setVotesR(2);
        node.setVotesW(2);
        
        nodeList.add(node);
        temp3.setNode(node);  
        
        Node temp7 = node;

        node = new Node();              
        node.setNodeName("p"+(maxVPValue[1] = maxVPValue[1]+1)); //p1
        node.setVotesR(2);
        node.setVotesW(2);
        
  
        nodeList.add(node);

        temp4.setNode(node);
        temp6.setNode(node);
        
        node = new Node();              
        node.setNodeName("p"+(maxVPValue[1] = maxVPValue[1]+1)); //p2
        node.setVotesR(2);
        node.setVotesW(2);
        
        nodeList.add(node);
        
        temp4.setNode(node);
        temp6.setNode(node);


        node = new Node();              
        node.setNodeName("p"+(maxVPValue[1] = maxVPValue[1]+1)); //p3
        node.setVotesR(0);
        node.setVotesW(0);
        
        nodeList.add(node);

        temp5.setNode(node);
        temp7.setNode(node);
              
        node = new Node();              
        node.setNodeName("p"+(maxVPValue[1] = maxVPValue[1]+1)); //p4
        node.setVotesR(0);
        node.setVotesW(0);
        
        nodeList.add(node);

        temp5.setNode(node);
        temp7.setNode(node);      
        
        for(int i=0; i<n; i++){
            
            node = new Node();
            node.setNodeName("p"+(maxVPValue[0] = maxVPValue[0]+1));
            node.setVotesR(0);
            node.setVotesW(0);
            
            nodeList.add(node);
            tempNode.setNode(node);
           
        }
        
        
        return nodeList;
    }    
    
    public List<Node> gridToVoting2(int n, int cols, int rows, int maxVPValue[]){
        List <Node> nodeList = new ArrayList<>();
        
            
        Node node = new Node();
        Node tempNode = new Node(); 
        
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v1
        node.setVotesR(1);
        node.setVotesW(2);
        
        tempNode = node;
        
        node.setIsSubStrs(true);
        nodeList.add(node);
 
        node = new Node();
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v2
        node.setVotesR(cols);
        node.setVotesW(cols);
        
        nodeList.add(node);
        tempNode.setNode(node);        

        Node temp2 = node;
        
        node = new Node();
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v3
        node.setVotesR(1);
        node.setVotesW(1);        
        
        nodeList.add(node);
        tempNode.setNode(node); 
        
        Node temp3 = node;
        
        List<Node> list1 = new ArrayList<>();      
        for(int i=0; i<cols; i++) {

            node = new Node();
            node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v4 v5
            node.setVotesR(1);
            node.setVotesW(1);        

            nodeList.add(node);
            temp2.setNode(node); 

            list1.add(node);
        }
        
        List<Node> list2 = new ArrayList<>();
        for(int i=0; i<cols; i++) {

            node = new Node();
            node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v6 v7
            node.setVotesR(cols);
            node.setVotesW(cols);        

            nodeList.add(node);
            temp3.setNode(node); 

            list2.add(node);
        }
        
        List<Node> list3 = new ArrayList<>();
        for(int i=0; i<n; i++){

            node = new Node();              
            node.setNodeName("p"+(maxVPValue[1] = maxVPValue[1]+1)); //p1 p2 p3 p4
            node.setVotesR(0);
            node.setVotesW(0);
        
            nodeList.add(node); 
            
            list3.add(node);
        }

            int i=0;
            int j=0;
            
        while(i<list1.size() && j<n){
            for(int k=0; k<rows; k++){

                list1.get(i).setNode(list3.get(j+k));
                list2.get(i).setNode(list3.get(j+k));

            }
            i++;
            j+=rows;
        }
        
        return nodeList;
    }   


    public List<Node> gridToVoting3(int cols, int rows, int maxVPValue[]){
        
        int n = cols*rows;  
        List <Node> nodeList = new ArrayList<>();            
        Node node = new Node();
        Node tempNode = new Node(); 
        
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v1
        node.setVotesR(1);
        node.setVotesW(2);
        
        tempNode = node;
        
        node.setIsSubStrs(true);
        nodeList.add(node);
 
        node = new Node();
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v2
        node.setVotesR(cols);
        node.setVotesW(cols);
        
        nodeList.add(node);
        tempNode.setNode(node);        

        Node temp2 = node;
        
        node = new Node();
        node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v3
        node.setVotesR(1);
        node.setVotesW(1);        
        
        nodeList.add(node);
        tempNode.setNode(node); 
        
        Node temp3 = node;
        
        List<Node> list1 = new ArrayList<>();      
        for(int i=0; i<cols; i++) {

            node = new Node();
            node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v4 v5
            node.setVotesR(1);
            node.setVotesW(1);        

            nodeList.add(node);
            temp2.setNode(node); 

            list1.add(node);
        }
        
        List<Node> list2 = new ArrayList<>();
        for(int i=0; i<cols; i++) {

            node = new Node();
            node.setNodeName("V"+(maxVPValue[0] = maxVPValue[0]+1)); //v6 v7
            node.setVotesR(cols);
            node.setVotesW(cols);        

            nodeList.add(node);
            temp3.setNode(node); 

            list2.add(node);
        }
        
        List<Node> list3 = new ArrayList<>();
        for(int i=0; i<n; i++){

            node = new Node();              
            node.setNodeName("p"+(maxVPValue[1] = maxVPValue[1]+1)); //p1 p2 p3 p4
            node.setVotesR(0);
            node.setVotesW(0);
        
            nodeList.add(node); 
            
            list3.add(node);
        }

            int i=0;
            int j=0;
            
        while(i<list1.size() && j<n){
            for(int k=0; k<rows; k++){

                list1.get(i).setNode(list3.get(j+k));
                list2.get(i).setNode(list3.get(j+k));

            }
            i++;
            j+=rows;
        }
        
        return nodeList;
    } 

    
    public List<Node> defineNewVoting(int n, int votesR, int votesW){
        
        List <Node> nodeList = new ArrayList<>();
        Node node = new Node();
        Node tempNode = new Node();       
        node.setNodeName("V1");
        
        node.setVotesR(votesR);
        node.setVotesW(votesW);
        tempNode = node;

        node.setIsSubStrs(true);
        
        nodeList.add(node);
        
        for(int i=0; i<n; i++){
            
            node = new Node();
            node.setNodeName("p"+(i+1));
            node.setVotesR(0);
            node.setVotesW(0);
            tempNode.setNode(node);
        }

        return nodeList;        
    }    

    public static void main(String[] args) throws IOException, InterruptedException {
        
        // Generate voting from two to 
        List<Node> strategies= new ArrayList<>();
        ConvertToVoting obj = new ConvertToVoting();
        int maxVPValue[] = {0,0};
        Node rootNode;
        
        for(int i=2; i<=16; i++){
            
            strategies.add(obj.rowaToVoting(i,maxVPValue).get(0));
            strategies.add(obj.mcsToVoting(i,maxVPValue).get(0));
        }

        strategies.add(obj.gridToVoting3(2,2,maxVPValue).get(0));
        strategies.add(obj.gridToVoting3(3,3,maxVPValue).get(0));
        strategies.add(obj.gridToVoting3(4,4,maxVPValue).get(0));
        
        // to convert into images 
        for(int i=0; i<strategies.size(); i++){ 
            
            rootNode = strategies.get(i); 
            convertIntoJPG(rootNode, "sub", i+1);  
            makeDBDocument(rootNode, returnN(rootNode), "pop18"); 
        }
                 
    }    
}
