/**
 * 
 */
package UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

import InvertedIndex.InvertedIndex;
import TextProcessing.Suggestions;
import Utils.StringUtils;

/**
 * @author Prabhjyot Singh Dhillon
 *
 */
public class UI {
	
	private static void openLink(String url) throws IOException, URISyntaxException {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
	    	System.out.println("\nOpening " + url);
	        Desktop.getDesktop().browse(new URI(url));
	    }
	}
	
	private static void options (List<String> files, InvertedIndex index) throws IOException, URISyntaxException {
	    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nInput options: Searching index to open in browser \t 's' to search again \t 'q' to quit");
		
		System.out.print("\nSelect option: ");
	    String option = in.readLine();
	    switch(option) {
	    	case "s":
	    		start(index);
	    		break;
	    	case "q":
	    		System.exit(0);
	    		break;
	    	default:
	    	    int url_index = Integer.parseInt(option);
	    	    openLink(files.get(url_index-1));
	    	    options(files, index);
	  }
	}
	
	private static void start(InvertedIndex index) throws IOException, URISyntaxException {
	    StringUtils st = new StringUtils();
	    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    System.out.print("What are you looking for? ");
	    String userSearch = in.readLine();
	    
	    List<String> files = index.find(st.Stem(userSearch, "English"));
	    
	    if (files == null) {
	    	System.out.println("\nNo result found for " + userSearch);
	    	
	    	System.out.println("\nTry these words");
	    	Suggestions sg = new Suggestions();
	    	List<String> sugg = sg.suggestions(userSearch);
	    	
	    	for (String word : sugg) {
				if (sugg.size() > 1) {
					System.out.print(word + " | " );
				} else  {
	
					System.out.println(word);
				}
			}
	    	System.out.println("\n");
	    }
	    options(files, index);
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
		InvertedIndex index = new InvertedIndex();
		index.createIndex();
		
		start(index);
	}

}
