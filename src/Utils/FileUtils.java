package Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class FileUtils {
	
	/**
	 * Checks if the the given folder exists if not tries to create it
	 * @param folder the folder to check
	 * @return boolean true, in case a folder is present or successfully created else false
	 */
	public boolean checkFolderExistsOrMake(File folder) {
		try {
			if(folder.exists()) {
				return true;
			} else {
				folder.mkdir();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String[] getFileNamesFromDir(File dir) {
		File[] allFiles = dir.listFiles();
		System.out.println("Total Files: " + allFiles.length);
		String[] allFileNames = new String[allFiles.length];
		for (int i = 0; i < allFiles.length; i++) {
			allFileNames[i] = allFiles[i].getName();
//			System.out.println("\t" + allFiles[i].getName());
		}
		
		return allFileNames;
	}

	
	public void writeToFile(String filename, HashSet<String> URLS) {
	      FileWriter writer;
	      try {
	          writer = new FileWriter(filename);
	          URLS.forEach(a -> {
	              try {
	                  writer.write(a + "\n");
	              } catch (IOException e) {
	                  System.err.println(e.getMessage());
	              }
	          });
	          writer.close();
	      } catch (IOException e) {
	          System.err.println(e.getMessage());
	      }
	  }
	
	public void writeToFile(String filename, List<String> list) {
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
	
	public List<String> getFromFile(String file) throws FileNotFoundException {
		Scanner scan = new Scanner(new File(file));
		List<String> words = new ArrayList<String>();
		while(scan.hasNextLine()){
	    	String line = scan.nextLine();
	        
	    	words.add(line);
	    }
		return words;
	}
}
