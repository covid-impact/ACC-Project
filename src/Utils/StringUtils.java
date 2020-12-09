/**
 * @author Prabhjyot Singh Dhillon
 */

package Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

/**
 * utilities for working with text
 */
public class StringUtils {
	
	private static List <String> stop_words = new ArrayList<String>();
	private static final String stop_words_file = "stop_words.txt";
	
	/**
	 * constructor to initialize the stop words
	 * @throws FileNotFoundException exception if file with stop words not found
	 */
	public StringUtils () throws FileNotFoundException {
		try {
			Scanner sw = new Scanner(new File(stop_words_file));
		    while(sw.hasNextLine()){
		    	String line = sw.nextLine();
		    	if (line.matches(".*\\p{Punct}")) {
		    		stop_words.add(line.replaceAll(".*\\p{Punct}", ""));
		    	}
		    	stop_words.add(line);
		    }
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	/**
	 * removes stop words from given text
	 * @param text text to remove stop words from
	 * @return stop words free text
	 */
	public String removeStopWords(String text) {
		String[] word_array = text.split(" ");
		List <String> word_list = new ArrayList<String>(Arrays.asList(word_array));
		
		word_list.removeAll(stop_words);
		
		String cleanedText = String.join(" ", word_list);
		return cleanedText;
	}
	
	/**
	 * stems the words in a text
	 * @param text text to stem words in
	 * @param language stemming language
	 * @return stemmed text
	 */
	public static String Stem(String text, String language){
        StringBuffer result = new StringBuffer();
        if (text!=null && text.trim().length()>0){
            StringReader tReader = new StringReader(text);
            Analyzer analyzer = new SnowballAnalyzer(Version.LUCENE_35,language);
            TokenStream tStream = analyzer.tokenStream("contents", tReader);
            TermAttribute term = tStream.addAttribute(TermAttribute.class);

            try {
                while (tStream.incrementToken()){
                    result.append(term.term());
                    result.append(" ");
                }
            } catch (IOException ioe){
                System.out.println("Error: "+ioe.getMessage());
            }
        }

        // If, for some reason, the stemming did not happen, return the original text
        if (result.length()==0)
            result.append(text);
        return result.toString().trim();
    }
}
