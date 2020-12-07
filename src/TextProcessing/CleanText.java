package TextProcessing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Utils.FileUtils;
import Utils.StringUtils;

public class CleanText {
	
	public static void main (String[] args) throws IOException {

		File dir = new File("./all-Text-Files");
	    
	    FileUtils ft = new FileUtils();
		StringUtils st = new StringUtils();
	    
		String[] allFileNames =  ft.getFileNamesFromDir(dir);
		
		for (String name : allFileNames) {
			Scanner scan = new Scanner(new File("./all-Text-Files/" + name));
			int i = 0;
			String body  = "";
		    while(scan.hasNextLine()){
		    	String line = scan.nextLine().replaceAll("\\p{P}", "");
		        
		    	if (i == 2) {
		    		body = line;
		    	}
		        i += 1;
		        //Here you can manipulate the string the way you want
		    }
		    
		    
		    body = st.removeStopWords(body);
		    body = st.Stem(body, "English");
		    
		    File dest = new File ("cleaned-text");
		    if(ft.checkFolderExistsOrMake(dest)) {
		    	List<String> str = new ArrayList<String>();
				str.add(body);
				ft.writeToFile(dest.getAbsolutePath() + "\\" + name, str);
				System.out.println("Saved " + dest.getAbsolutePath() + "\\" + name);
		    }
			
		}
	}
}
