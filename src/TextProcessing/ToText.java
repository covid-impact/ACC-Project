/**
 * @author Prabhjyot Singh Dhillon
 */
package TextProcessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Utils.FileUtils;
import memoryManagement.In;
import memoryManagement.IndexMinPQ;


/**
 * Converts the saved links to text files
 */
public class ToText {
	
	/**
	 * merges the different links into one file using multiway merge
	 * @param streams different file streams to merge
	 * @return a list of all links
	 */
	private static ArrayList<String> merge(In[] streams) { 
		ArrayList<String> merger = new ArrayList<String>();
		
        int N = streams.length; 
        IndexMinPQ<String> pq = new IndexMinPQ<String>(N); 
        for (int i = 0; i < N; i++) 
            if (!streams[i].isEmpty()) 
                pq.insert(i, streams[i].readString()); 

        // Extract and print min and read next from its stream. 
        while (!pq.isEmpty()) {
        	merger.add(pq.minKey()); 
            int i = pq.delMin(); 
            if (!streams[i].isEmpty()) 
                pq.insert(i, streams[i].readString()); 
        }
        
        return merger;
    }
	
	/**
	 * gets the text from the given URL. Saves it text file with first line as the link, second the title and finally the whole text
	 * @param url link to get the data from
	 * @param i id for the file
	 * @param dir directory to save the files in
	 * @throws IOException exception if URL can't be fetched or directory related error
	 */
	public static void readAndConvert(String url, int i, File dir) throws IOException {
		try {
			Document document = Jsoup.connect(url).get();
			
			String fileText = url;
			fileText += "\n" + document.title();
			fileText += "\n" + document.text();
			
			PrintWriter writer = new PrintWriter(dir.getAbsolutePath() + "/" + i + ".txt");
			System.out.println(document.title().replaceAll("[^a-zA-Z0-9\\.\\-]", "_") + " saved as " + "./" + dir.getAbsolutePath() + "/" + i + ".txt");
			
			writer.println(fileText);
			writer.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * gets all the files with links and merges them to one 
	 * @return a list of all links
	 * @throws FileNotFoundException exception if link files are not found
	 */
	public static ArrayList<String> getLinks() throws FileNotFoundException {
		String[] fnames = {"amazon.txt", "google.txt", "imdb.txt", "news.txt", "wiki-en.txt"};
        int N = fnames.length; 
        In[] streams = new In[N]; 
        for (int i = 0; i < N; i++) 
            streams[i] = new In(fnames[i]);
        
        ArrayList<String> merger = merge(streams);
        
        FileUtils ft = new FileUtils();
        
        ft.writeToFile("unique_links.txt", merger);
//        saveToFile("all.txt", merger);
        
        return merger;
	}
	
	/**
	 * gets all the links, converts them to text and saves in a file 
	 * @param args arguments from termianl if any
	 * @throws IOException excpetion in case of directory error
	 */
	public static void main (String[] args) throws IOException {
		ArrayList<String> alls = getLinks();
		
		// to get only unique links
		Set<String> all = new HashSet<>(alls);
		
		File dir = new File("./all-Text-Files");
		FileUtils ft = new FileUtils(); 
		if (ft.checkFolderExistsOrMake(dir)) {
			int i = 1;
			for (String name : all) {
				readAndConvert(name, i, dir);
				i += 1;
			}
		}
	}

}
