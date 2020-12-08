package InvertedIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import Ranking.RankingWebpages;

import java.util.Map.Entry;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class InvertedIndex {
    Map<Integer,String> sources; 	//all the text files being loaded in with string as file name and integer as index position
    HashMap<String, HashSet<Integer>> index; //store the terms/words from each text file and store an doc position for that term
    RankingWebpages ranking;
	private int repeatNumber = 80;
	private String format = "%-10s%-10s%-30s%-30s\n";
    
    public InvertedIndex() {
        sources = new HashMap<Integer,String>();
        index = new HashMap<String, HashSet<Integer>>();
        ranking = new RankingWebpages();
    }

    public void createIndex() throws IOException{
        final String PCNAME = "yahagi"; //enter your pc-name here
        
		File webSites = new File("./cleaned-text"); //must include directory path to text files
		File[] sites = webSites.listFiles();
       	
		for(int i = 0; i < 1000; i++) {
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
        
		File webSites = new File("./cleaned-text"); //must include directory path to text files
		File[] sites = webSites.listFiles();
       	
		for(int i = 0; i < 1000; i++) {
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
    
    public List<String> find(String search) throws IOException {
        String[] terms = search.split("\\W+"); //java regex for non-word characters
        //only handle first term for now
        if(index.containsKey(terms[0].toLowerCase())) {
        	HashSet<Integer> result = new HashSet<Integer>(index.get(terms[0].toLowerCase()));
            
	        for(String term : terms){
	        	result.retainAll(index.get(term));
	        }
	
	        if(result.size() == 0) {
	            System.out.println("Could not find search parameter.");
	            return null;
	        }
	        
	        System.out.println("Results for " + search);

	        System.out.println("Ranked in descending order of frequency(no. or occurence):");
	        
	        System.out.println("Total Results: " + result.size());
	        System.out.print("-".repeat(repeatNumber ) + "\n");
			System.out.format(format , "Index", "ID", "Frequnecy", "Title");
	        System.out.print("-".repeat(repeatNumber ) + "\n");
//	        for(int index : result){
//	        	System.out.println("\t" + sources.get(index));
////	        	System.out.println("\t" + sources.get(index));
//	        }
	        
//	        System.out.println(sources);
//	        System.out.println(index);
	        List<String> files = new ArrayList<String>();
	        List<Entry<Integer,Integer>> rankedResult = ranking.sortByMultiWords(terms);
	        int index = 1;
	        HashSet<String> hs = new HashSet<String>();
	        for (Entry<Integer, Integer> el : rankedResult) {
	        	String filename = sources.get(el.getKey());
	        	final List<String> lines = Files.readAllLines(Paths.get("./all-Text-Files/" + filename), StandardCharsets.ISO_8859_1);
	        	if (el.getValue() > 10 && !hs.contains(lines.get(1))) {
		        	System.out.format(format, index, filename, el.getValue(), lines.get(1));
//		        	lines.get(1).replaceFirst("(.{10}).+(.{10})", "$1...$2")
		        	files.add(lines.get(0));
		        	hs.add(lines.get(1));
	        	}
//	        	System.out.println("sourceIndex: " + el.getKey() + " " +sources.get(el.getKey()) + " frequency: "+ el.getValue());
	        	index += 1;
	        }
	        System.out.print("-".repeat(repeatNumber ) + "\n");
	        //System.out.println(ranking.frequency);
	        //ranking.sortAll();
	        return files;
        }
        
		return null;
        
    	}
}
//public class labFour {
//
//
//	public static void main(String[] args) throws IOException{
//		InvertedIndex index = new InvertedIndex();
//		index.createIndex();
//		
//		System.out.print("What are you looking for? ");
//	    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//	    String userSearch = in.readLine();
//
//	    index.find(userSearch);
//	}
//
//}
