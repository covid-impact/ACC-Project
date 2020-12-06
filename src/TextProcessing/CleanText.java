package TextProcessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class CleanText {
	
	private static List <String> stop_words = new ArrayList<String>();
	
	public CleanText() throws FileNotFoundException {
		Scanner sw = new Scanner(new File("stop_words.txt"));
	    while(sw.hasNextLine()){
	    	String line = sw.nextLine();
	    	CleanText.stop_words.add(line);
	    }
	}
	
	public static String[] getFileNamesFromDir(File dir) {
		File[] allFiles = dir.listFiles();
		System.out.println("Total Files: " + allFiles.length);
		String[] allFileNames = new String[allFiles.length];
		for (int i = 0; i < allFiles.length; i++) {
			allFileNames[i] = allFiles[i].getName();
//			System.out.println("\t" + allFiles[i].getName());
		}
		
		return allFileNames;
	}
	
	private static void saveToFile(String filename, List<String> list) {
		try {
			FileWriter writer = new FileWriter(filename); 
			for(String str: list) {
			  writer.write(str.trim() + System.lineSeparator());
			}
			writer.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		
	}
	
	public String removeStopWords(String txt) {
		String[] bodyData = txt.split(" ");
	    List <String>list = new ArrayList<String>(Arrays.asList(bodyData));
	    
	    System.out.println(stop_words.size());
	    list.removeAll(stop_words);
	    String body = String.join(" ", list);
	    
	    return body;
	}
	
	public static void main (String[] args) throws IOException {

		File dir = new File("./all");
	    
	    CleanText ct = new CleanText();
		
		String[] allFileNames =  getFileNamesFromDir(dir);
		
		for (String name : allFileNames) {
			Scanner scan = new Scanner(new File("./all/" + name));
			int i = 0;
			String title = "";
			String body  = "";
		    while(scan.hasNextLine()){
		    	String line = scan.nextLine().replaceAll("\\p{P}", "");
		    	if (i == 0) {
		    		title = line;
		    	}
		        
		    	if (i == 1) {
		    		body = line;
		    	}
		        i += 1;
		        //Here you can manipulate the string the way you want
		    }
		    
		    body = ct.removeStopWords(body);
		    
		    String dest = "./cleanedFull/";
			List<String> str = new ArrayList<String>();
			str.add(body);
			saveToFile(dest + title + ".txt", str);
		}
	}
}
