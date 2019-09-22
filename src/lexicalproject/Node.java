package lexicalproject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mohtashim's
 */
public class Node implements Serializable {
    
    private String nodeName = "";
    private int votesR = 0;
    private int votesW = 0;
    private int priorityR = 0;
    private int priorityW = 0;
    private boolean isSub = false;
    private boolean mutable = false;    
    private int weight = 1;
    
    List<Node> nodeList = new ArrayList<>();
    
    public void setNodeName(String nodeName){
        this.nodeName = nodeName;
    }       
    public void setVotesR(int votesR){
        this.votesR = votesR;
    }
    public void setVotesW(int votesW){
        this.votesW = votesW;
    }   
    public void setPriorityR(int priorityR){
        this.priorityR = priorityR;
    }  
    public void setPriorityW(int priorityW){
        this.priorityW = priorityW;
    }   
    public void setWeight(int weight){
        this.weight = weight;
    }  
    public int getWeight(){
        return weight;
    }       
    public void setNode(Node node){
        this.nodeList.add(node);
    }
    public void replaceNode(int i, Node node){
        this.nodeList.remove(i);
        this.nodeList.add(i, node);
    }   
    public void deleteNode(Node node){
        this.nodeList.remove(node);
    }       
    
    public void setIsSubStrs(boolean isSub){
        this.isSub = isSub;
    }  
    public void setMutable(boolean mutable){
        this.mutable = mutable;
    }      
    
    
    public String getNodeName(){
        return this.nodeName;
    }
    public int getVotesR(){
        return this.votesR;
    }
    public int getVotesW(){
        return this.votesW;
    }   
    public int getPriorityR(){
        return this.priorityR;
    }  
    public int getPriorityW(){
        return this.priorityW;
    }   
    public Node getNode(int id){
        return this.nodeList.get(id);
    }   
    public int getNodeSize(){
        return this.nodeList.size();
    }       

    
    public boolean isLeaf(){
        
        return nodeList.size()==0;
    }
    public boolean isSubStrs(){
        
        return this.isSub;
    }
    public boolean getMutable(){
        
        return this.mutable;
    }    
    public boolean is1SR(){
        
        double qR = votesR;
        double qW = votesW;
        double totalWeight = 0;
                        
        for(int i=0; i<nodeList.size(); i++){
            
            totalWeight += nodeList.get(i).getWeight();
        }
        
        return (qW > totalWeight/2 && qR + qW > totalWeight);
                                            
    }
    
    public static Object deepCopy(Object object) {
      try {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
        outputStrm.writeObject(object);
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
        return objInputStream.readObject();
      }
      catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }    
}