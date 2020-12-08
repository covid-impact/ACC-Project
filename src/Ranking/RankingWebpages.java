package Ranking;
/**
 * @author Frederick Chong Ho
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/*
 * Class ranking webpages
 * nested hashmap - frequency
 * hashmap<wordIndex, hashmap<sourceIndex, frequency>>
 * Public methods:
 * sortAll()
 * sortByWord(String)
 * sortByMultiWords(String[])
 * 
 */
public class RankingWebpages {
	//nested hashmap for storing the frequency
	public HashMap<String, HashMap<Integer,Integer>> frequency;
	//hashmap<wordIndex, hashmap<sourceIndex, frequency>>
	
	
	/*
	 * constructor, create nested hashmap to store the frequency for each word and webpage source
	 */
	public RankingWebpages() {
		frequency = new HashMap<String, HashMap<Integer,Integer>>();
	}
	
	/*
	 * sortall method, calling this will sort all the nested hashmaps according to the frequency
	 */
	public void sortAll() {
		System.out.println(frequency.values());
		for (String key : frequency.keySet()) {//for each word
			List<Entry<Integer,Integer>>list = this.sortByWord(key);//sort and return list
			//System.out.println(list);
		}
	}
	/*
	 * sort the hashmap with that particular word only
	 * @param word that the related hashmap need to be sorted
	 * @return list of source index in decending order of frequency
	 */

	public List<Entry<Integer,Integer>> sortByWord(String word){
		//get the hashmap with the word and sort
		List<Entry<Integer,Integer>> list = this.sortHashMap(this.frequency.get(word));
		//create a sorted map
		HashMap<Integer,Integer> sortedHashMap = new HashMap<Integer,Integer>();
		for (Entry<Integer, Integer> el: list) { //for each element in list
			sortedHashMap.put(el.getKey(), el.getValue());//put element to the map
		}
		//replacehashmap
		this.frequency.replace(word, sortedHashMap);
		return list;
	}
	/*
	 * sort a list of words
	 * add up the frequency in different hashmaps
	 * @param a list of words
	 * @return list of source index in decending order of overall frequency
	 */

	public List<Entry<Integer,Integer>> sortByMultiWords(String[] words){
		HashMap<Integer,Integer> overallRanking = new HashMap<Integer,Integer>();//hashmap storing the overall ranking
		//loop through each word in list
		for (String word : words) {
			//get the ranking as list
			List<Entry<Integer,Integer>>list = this.sortByWord(word);
			//add to the overall ranking
			for (Entry<Integer, Integer> el : list) {
				Integer key = el.getKey();
				Integer value = el.getValue();
				if (!overallRanking.containsKey(key)) {
					overallRanking.put(key, value);//create new
				} else {
					
					overallRanking.replace(key, value + overallRanking.get(key));//add up
				}
			}
		}
		return this.sortHashMap(overallRanking);
	}
	/*
	 * get hashmap and sort this hashmap according to the value(not key)
	 * @param hash map <key value>
	 * @return list of key value pair in dec order of value
	 */

	private List<Entry<Integer,Integer>> sortHashMap(HashMap<Integer,Integer> targetHashMap) {
		//turn to list
		List<Entry<Integer, Integer>> list = new ArrayList<>(targetHashMap.entrySet());
		//sort list
		Collections.sort(list, new Comparator<Entry<Integer, Integer>>(){
			public int compare(Entry<Integer, Integer> pair1, Entry<Integer, Integer> pair2) {
				//Desc order
				return pair2.getValue().compareTo(pair1.getValue());
			}
		});
		return list;
	}
}
