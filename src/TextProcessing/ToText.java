/**
 * 
 */
package TextProcessing;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import memoryManagement.In;
import memoryManagement.IndexMinPQ;


/**
 * @author Prabhjyot Singh Dhillon
 *
 */
public class ToText {
	
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
	
	public static void readAndConvert(String url) throws IOException {
		try {
			Document document = Jsoup.connect(url).get();
			String fileText = document.title();
			fileText = fileText + "\n" + document.text();
			PrintWriter writer = new PrintWriter("./all/" + document.title().replaceAll("[^a-zA-Z0-9\\.\\-]", "_") + ".txt");
			System.out.println(document.title().replaceAll("[^a-zA-Z0-9\\.\\-]", "_") + " saved");
			writer.println(fileText);
			writer.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static final String dir = "./full-text";
	
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

	
	public static ArrayList<String> getLinks() {
		String[] fnames = {"amazon.txt", "google.txt", "imdb.txt", "news.txt", "wiki-en.txt"};
        int N = fnames.length; 
        In[] streams = new In[N]; 
        for (int i = 0; i < N; i++) 
            streams[i] = new In(fnames[i]);
        
        ArrayList<String> merger = merge(streams);
        saveToFile("all.txt", merger);
        
        return merger;
	}
	
	public static void main (String[] args) throws IOException {
		ArrayList<String> alls = getLinks();
		Set<String> all = new HashSet<>(alls);
		
		
		for (String name : all) {
			readAndConvert(name);
		}
	}

}
