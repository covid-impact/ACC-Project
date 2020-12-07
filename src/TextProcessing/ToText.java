/**
 * 
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
