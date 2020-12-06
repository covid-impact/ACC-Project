package Crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler {
	private HashSet<String> visitedURLs;
	public static final Pattern LINK_REGEX = 
			Pattern.compile("[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
	public int MAX = 100;
	private String filename = "default.txt";
	private String regex = "";
	
	public Crawler (int MAX_LINKS, String file, String regexSite) {
		MAX = MAX_LINKS;
		filename = file;
		regex = regexSite;
		visitedURLs = new HashSet<String>();
	}
	
    public HashSet<String> getLinks (String url) throws IOException {
        HashSet<String> uniqueHrefs = new HashSet<String>();
    	try {
	        Document document = Jsoup.connect(url).get();
	        Elements hrefs = document.select("a[href]");
	        
	        for (Element href : hrefs) {
	        	String hrefLink = href.attr("abs:href").toString();
	//        	&& hrefLink.matches("https://(?:www\\.)?imdb.com/title/tt[^/]+/")
	        	if (hrefLink.matches("[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)") && hrefLink.matches(regex)) {
	            	uniqueHrefs.add(hrefLink);
	        	}
	        }
    	} catch (IOException e) {
          System.err.println("For '" + url + "': " + e.getMessage());
      }
        
        return uniqueHrefs;
    }
    
    public void crawler(String url) throws IOException {
    	visitedURLs.add(url);
    	HashSet<String> links = getLinks(url);
//		System.out.println(visitedURLs.size() < MAX);
    	for (String link : links) {
    		if(visitedURLs.contains(link)) {
    			continue;
    		}
    		System.out.println(link);
			visitedURLs.add(link);
			if (visitedURLs.size() < MAX) {
    			crawler(link);
			} else {
				return;
			}
    	}
    }
    
    public void writeToFile() {
      FileWriter writer;
      try {
          writer = new FileWriter(filename);
          visitedURLs.forEach(a -> {
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
    
    public static void main(String[] args) throws IOException {

//    	Crawler cgoogle = new Crawler(1000, "google.txt", "https?:\\/\\/(([^\\/]*\\.)|)google\\.com(|\\/.*)");
//    	cgoogle.crawler("https://www.google.com/search?q=google&oq=google");
//    	cgoogle.writeToFile();
    	
//    	Crawler cwiki = new Crawler(2000, "wiki-en.txt", "https?:\\/\\/(([^\\/]*\\.)|)en.wikipedia\\.org(|\\/.*)");
//    	cwiki.crawler("https://en.wikipedia.org/wiki/Web_crawler");
//    	cwiki.writeToFile();
    	
//    	Crawler cimdb = new Crawler(1000, "imdb.txt", "https?:\\/\\/(([^\\/]*\\.)|)imdb\\.com(|\\/.*)");
////    	Crawler cimdb = new Crawler(1000, "imdb.txt", "https://(?:www\\\\.)?imdb.com/title/tt[^/]+/");
//    	cimdb.crawler("https://www.imdb.com");
//    	cimdb.writeToFile();
    	
//    	Crawler cnews = new Crawler(1000, "news.txt", "https?://(([^/]*\\.)|)theverge\\.com(|/.*)");
//    	cnews.crawler("https://www.theverge.com/");
//    	cnews.writeToFile();
    	
    	Crawler camazon = new Crawler(700, "amazon.txt", "https?:\\/\\/(([^\\/]*\\.)|)amazon\\.com(|\\/.*)");
    	camazon.crawler("https://www.amazon.com/");
    	camazon.writeToFile();
    	
    }
}

//public class Crawler {
//    private static final int MAX_DEPTH = 1000;
//    private HashSet<String> links;
//    public static final Pattern LINK_REGEX_W3 = 
//			Pattern.compile("(http:\\/\\/www.|https:\\/\\/www.)imdb.com\\/title\\/tt([a-zA-Z0-9]+)(?:\\/)(?:^[a-zA-Z0-9]+)?");
//	
//
//    public Crawler() {
//        links = new HashSet<>();
//    }
//
//    public void getPageLinks(String URL) {
//    	System.out.println(links.size() < MAX_DEPTH);
//        if (links.size() < MAX_DEPTH) {
//            try {
//            	System.out.println(links.size());
//            	if (links.add(URL)) {
//            		System.out.println(URL);
//            	}
//
//                Document document = Jsoup.connect(URL).get();
//                Elements a = document.select("a[href]");
//                
////                ArrayList<String> links = new ArrayList<String>();
//                for (Element page : a) {
//
//            		links.add(page.attr("abs:href").toString());
//            		System.out.println(page.attr("abs:href").toString());
////                	if (page.attr("abs:href").toString().matches("https://(?:www\\.)?imdb.com/title/tt[^/]+/")) {
////                	}
//                }
//                for (String page : links) {
//                    getPageLinks(page);
//                }
//            } catch (IOException e) {
//                System.err.println("For '" + URL + "': " + e.getMessage());
//            }
//        } else {
//        	return;
//        }
//    }
//    
//    public void writeToFile(String filename) {
//        FileWriter writer;
//        try {
//            writer = new FileWriter(filename);
//            links.forEach(a -> {
//                try {
//                    writer.write(a + "\n");
//                } catch (IOException e) {
//                    System.err.println(e.getMessage());
//                }
//            });
//            writer.close();
//        } catch (IOException e) {
//            System.err.println(e.getMessage());
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        Crawler c = new Crawler();
//        c.getPageLinks("https://www.imdb.com/title/tt12392504/");
//        c.writeToFile("one.txt");
//    	
////    	Document document = Jsoup.connect("https://www.imdb.com/title/tt12392504").get();
////    	Elements links = document.select("a[href]");
////        
////        for (int i =0; i<links.size(); i++) {
////        	if (links.get(i).attr("abs:href").toString().matches("https://(?:www\\.)?imdb.com/title/tt[^/]+/")) {
////            	System.out.println(links.get(i).attr("abs:href").toString());
////        	}
////        }
//    }
//}
