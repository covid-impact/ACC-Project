package textprocessing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sorting.Sort;

public class RankingWebpages {
	HashMap<String, HashMap<Integer,Integer>> frequency;
	//hashmap<wordIndex, hashmap<sourceIndex, frequency>>
	
	public RankingWebpages() {//constructor, make frequency
		frequency = new HashMap<String, HashMap<Integer,Integer>>();
	}	
	public void sortAll() {
		System.out.println(frequency.values());
		for (String key : frequency.keySet()) {
			List<Entry<Integer,Integer>>list = this.sortByWord(key);
			//System.out.println(list);
		}
	}
	public List<Entry<Integer,Integer>> sortByWord(String word){
		List<Entry<Integer,Integer>> list = this.sortHashMap(this.frequency.get(word));
		//replace
		HashMap<Integer,Integer> sortedHashMap = new HashMap<Integer,Integer>();
		for (Entry<Integer, Integer> el: list) { //for each element in list
			sortedHashMap.put(el.getKey(), el.getValue());
		}
		//replacehashmap
		this.frequency.replace(word, sortedHashMap);
		return list;
	}
	
	public List<Entry<Integer,Integer>> sortByMultiWords(String[] words){
		HashMap<Integer,Integer> overallRanking = new HashMap<Integer,Integer>();
		for (String word : words) {
			List<Entry<Integer,Integer>>list = this.sortByWord(word);
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
