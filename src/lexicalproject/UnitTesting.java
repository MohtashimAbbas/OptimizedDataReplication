/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicalproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Mohtashim's
 */


// unit test for availability 
// unit test for cost 
// unit test for crossovers 
// unit test for priorities 
// unit test for extended voting 
// unit test for mutation 

public class UnitTesting {
    
    // 1. MCS(3)
    // 2. ROWA(3)
    // 3. MCS(4)
    // 4. ROWA(4)
    // 5. GRID(4)
    // 6. TQP(4)
    

    
//List<List<String>> writeListAllStr = new ArrayList<>();
    
    
    
    public boolean pathCheck(List<List<String>> calculatedReadWriteList, List<List<String>> savedReadWriteList){
        
        boolean isCheck = false;
        Set set1;
        Set set2;
        String elements[];
        
        if(calculatedReadWriteList.get(0).size()!= savedReadWriteList.get(0).size()){
            isCheck = false;
            return isCheck; 
        }
        if(calculatedReadWriteList.get(1).size()!= savedReadWriteList.get(1).size()){
            isCheck = false;
            return isCheck;             
        }        
        
        
        for(int i=0; i < calculatedReadWriteList.get(0).size(); i++){
            
            elements = calculatedReadWriteList.get(0).get(i).split(",");
            set1 = new HashSet(Arrays.asList(elements));
            
            isCheck = false;
            
            for(int j=0; j< savedReadWriteList.get(0).size(); j++){
                
                elements = savedReadWriteList.get(0).get(j).split(",");
                set2 = new HashSet(Arrays.asList(elements));
                
                if(set1.equals(set2)){
                    isCheck =true;
                    break;
                }  
            }
            
            if(isCheck!=true){
                return isCheck;
            }

        }
        
       
        return isCheck;
    }
    
    public boolean availCheck(){
        boolean isCheck = true;
        
        return isCheck;
    }    
    
    public List<List<String>> func(String path) throws FileNotFoundException, IOException{
        
        File file = new File(path); 
        BufferedReader br = new BufferedReader(new FileReader(file)); 

        List<List<String>> readWriteList = new ArrayList<>();        
        String st; 
        List<String> tempRead = new ArrayList();
        List<String> tempWrite = new ArrayList();
        
        while ((st = br.readLine()) != null) {
            
            tempRead.add(st);
        
        }         
        
        tempWrite = tempRead.subList(tempRead.indexOf("W")+1, tempRead.size());
        tempRead = tempRead.subList(0, tempRead.indexOf("W"));
    
        readWriteList.add(tempRead);
        readWriteList.add(tempWrite);
        
        return readWriteList;
        
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
                
        String path = "C:\\Users\\Mohtashim's\\Desktop\\testData\\1. MCS(3).txt";
        
    }
       
}
