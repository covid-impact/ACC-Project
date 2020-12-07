package textprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.io.IOException;
import java.io.InputStreamReader;


class InvertedIndex {
    Map<Integer,String> sources; 	//all the text files being loaded in with string as file name and integer as index position
    HashMap<String, HashSet<Integer>> index; //store the terms/words from each text file and store an doc position for that term
    RankingWebpages ranking;
    
    InvertedIndex() {
        sources = new HashMap<Integer,String>();
        index = new HashMap<String, HashSet<Integer>>();
        ranking = new RankingWebpages();
    }

    public void createIndex() throws IOException{
        final String PCNAME = "yahagi"; //enter your pc-name here
        
		File webSites = new File("C:\\Users\\" + PCNAME + "\\Downloads\\cleanedFull"); //must include directory path to text files
		File[] sites = webSites.listFiles();
       	
		for(int i = 0; i < 7; i++) {
			try(BufferedReader file = new BufferedReader(new FileReader(sites[i])))
	            {
	                String line;
	                sources.put(i, sites[i].getName().trim()); 	//add index position and file name to sources HashMap
	                
	                while((line = file.readLine()) != null) {   //read file until you cannot
	                    String[] terms = line.split("\\W+");    //take each line and tokenize by using regex of whitespaces
	                    
	                    for(String term : terms){ 				//search through the tokenized terms array
	                    	term = term.toLowerCase(); 	    	//convert to lowercase
	                        
	                    	if (!index.containsKey(term))  					//if HashMap index does not contain any word found in the tokenize terms array
	                            index.put(term, new HashSet<Integer>());	//add the newly founded term/word and the file index 
	                        
	                    	index.get(term).add(i);
	                    }
	                    
	                }
	         } catch (IOException e){
	        	 System.out.println("Error! " + sites[i].getName() + " file not found!");
	           }
	        }
		this.createFrequency();
    	}
    
    public void createFrequency() throws IOException{
        final String PCNAME = "yahagi"; //enter your pc-name here
        
		File webSites = new File("C:\\Users\\" + PCNAME + "\\Downloads\\cleanedFull"); //must include directory path to text files
		File[] sites = webSites.listFiles();
       	
		for(int i = 0; i < 7; i++) {
			try(BufferedReader file = new BufferedReader(new FileReader(sites[i])))
	            {
	                String line;
	                sources.put(i, sites[i].getName().trim()); 	//add index position and file name to sources HashMap
	                
	                while((line = file.readLine()) != null) {   //read file until you cannot
	                    String[] terms = line.split("\\W+");    //take each line and tokenize by using regex of whitespaces
	                    
	                    for(String term : terms){
	                    	term = term.toLowerCase();
	                        
	                    	if (!ranking.frequency.containsKey(term)) {  		//if HashMap index does not contain any word found in the tokenize terms array
	                    		HashMap<Integer,Integer> sourcefrequency = new HashMap<Integer,Integer>();
	                    		sourcefrequency.put(i,1);
	                    		
	                    		ranking.frequency.put(term,sourcefrequency);
	                    	} else {
	                    		HashMap<Integer,Integer> sourcefrequency = ranking.frequency.get(term); //get word source
	                    		
	                    		if(!sourcefrequency.containsKey(i)) {	//if source word was not recorded
	                    			sourcefrequency.put(i, 1);			//source, appeared once
	                    		} else {
	                    			sourcefrequency.replace(i, sourcefrequency.get(i)+1);//frequency +1
	                    		}
	                    		
	                    	}
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
        //only handle first term for now
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
	        	System.out.println("\t" + sources.get(index));
	        }
	        
	        System.out.println(sources);
	        System.out.println(index);
	        
	        List<Entry<Integer,Integer>> rankedResult = ranking.sortByMultiWords(terms);
	        //System.out.println(rankedResult);
	        System.out.println("Ranked in decending order of frequency(no. or occurance):");
	        for (Entry<Integer, Integer> el : rankedResult) {
	        	System.out.println("sourceIndex: " + el.getKey()+" frequency: "+ el.getValue());
	        }
	        //System.out.println(ranking.frequency);
	        //ranking.sortAll();
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
