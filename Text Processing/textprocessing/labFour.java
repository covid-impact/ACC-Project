package textprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.InputStreamReader;


class InvertedIndex {
    Map<Integer,String> sources;
    HashMap<String, HashSet<Integer>> index;

    InvertedIndex() {
        sources = new HashMap<Integer,String>();
        index = new HashMap<String, HashSet<Integer>>();
    }

    public void createIndex() throws IOException{
        final String PCNAME = "Owner"; //enter your pc-name here
        
		File webSites = new File("C:\\Users\\" + PCNAME + "\\Downloads\\cleanedFull"); //must include directory path to text files
		File[] sites = webSites.listFiles();
       	
		for(int i = 0; i < 20; i++) {
			try(BufferedReader file = new BufferedReader(new FileReader(sites[i])))
	            {
	                String line;
	                //sources.put(i, fileToString(sites[i])); 	//test line, should delete this line 
	                sources.put(i, sites[i].getName().trim()); 	//add index position and file name to sources HashMap
	                
	                while((line = file.readLine()) != null) {   //read file until you cannot
	                    String[] terms = line.split("\\W+");    //take each line and tokenize by using regex of whitespaces
	                    
	                    for(String term : terms){ 				//search through the tokenized terms array
	                    	term = term.toLowerCase(); 	    	//convert to lowercase
	                        
	                    	if (!index.containsKey(term))  					//if HashMap index does not contain any word found in the tokenize terms array
	                            index.put(term, new HashSet<Integer>());	//add
	                        
	                    	index.get(term).add(i);
	                    }
	                    
	                }
	         } catch (IOException e){
	        	 System.out.println("Error! " + sites[i].getName() + " file not found!");
	           }
	        }
    	}
    
    public String fileToString(File textFileToString) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(textFileToString));
		StringBuilder sb = new StringBuilder();    
		String line;
		try {
		    while ((line = br.readLine()) != null) {
		       sb.append(line);
		    }
		} finally {
			
		    br.close();
		}
		
		String finalString = sb.toString();
		
    	return finalString;
    }
    
    public void find(String search){
        String[] terms = search.split("\\W+"); //java regex for non-word characters
        HashSet<Integer> result = new HashSet<Integer>(index.get(terms[0].toLowerCase()));
        
	        for(String term : terms){
	        	result.retainAll(index.get(term));
	        }
	
	        if(result.size() == 0) {
	            System.out.println("Could not find search parameter.");
	            return;
	        }
	        
	        System.out.println("Result located in: ");
	        for(int index : result){
	            System.out.println("\t" + sources.get(index));
	        }
    	}
}
public class labFour {


	public static void main(String[] args) throws IOException{
		InvertedIndex index = new InvertedIndex();
		index.createIndex();
		
		System.out.print("What are you looking for? ");
	    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    String userSearch = in.readLine();

	    index.find(userSearch);
	}

}
