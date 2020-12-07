package TextProcessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import Utils.FileUtils;
import Utils.StringUtils;
import algorithmDesign.Sequences;

public class Suggestions {
	
	private static void makeDictioanry( ) throws FileNotFoundException {
		File dir = new File("all-Text-Files");
		List<String> words = new ArrayList<String>();
		
		FileUtils ft = new FileUtils();
		String[] files = ft.getFileNamesFromDir(dir);
		int index = 1;
		for (String name : files) {
			Scanner scan = new Scanner(new File("./cleaned-text/" + name));
			System.out.println(index + " " + name);
			String body  = "";
		    while(scan.hasNextLine()){
		    	String line = scan.nextLine().toLowerCase();
		        
//		    	if (i == 2) {
		    		body = line.toLowerCase();
//		    	}
		    }
		    
		    String[] body_words = body.split(" ");
		    
		    Collections.addAll(words, body_words);
		}
		
//		Collections.sort(words);
		
		HashSet<String> set = new HashSet<String>(words);
		ft.writeToFile("dictionary.txt", set);
	}
	
	public List<String> suggestions(String input) throws FileNotFoundException {
		FileUtils ft = new FileUtils();
		Sequences sq = new Sequences();
		List<String> words = ft.getFromFile("dictionary.txt");
		
		List<String> suggestions = new ArrayList<String>();
		
		for (String word : words) {
			if (sq.editDistance(input.toLowerCase(), word) == 1) {
				suggestions.add(word);
			}
		}
		
		return suggestions;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
//		makeDictioanry();
		
		StringUtils st = new StringUtils();
		
		String input = "play";
		List<String> sugg = suggestions(st.Stem(input, "English"));
		
		for (String word : sugg) {
			if (sugg.size() > 1) {
				System.out.print(word + " | " );
			} else  {

				System.out.println(word);
			}
		}
		
	}
}
