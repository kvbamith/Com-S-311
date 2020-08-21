package pa1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import api.TaggedVertex;
import api.Util;

/**
 * Implementation of an inverted index for a web graph.
 * 
 * @author Steven Rein and Amith Kopparapu Venkata Boja
 */
public class Index {
	
	private List<TaggedVertex<String>> taggedUrls;
	private Map<String, Map<String, Integer>> index;
	
	/**
	 * Constructs an index from the given list of urls.  The
	 * tag value for each url is the indegree of the corresponding
	 * node in the graph to be indexed.
	 * @param urls
	 *   information about graph to be indexed
	 */
	public Index(List<TaggedVertex<String>> urls) {
		taggedUrls = urls;
		index = null;
	}
  
	/**
	 * Creates the index.
	 */
	public void makeIndex() {
		int politeIndex = 0;
		Scanner scan;
		index = new HashMap<String, Map<String, Integer>>();
		for (int i = 0; i < taggedUrls.size(); i++) {
			politeIndex++;
			if (politeIndex + 1 % 50 == 0) {
				JSoupUtil.bePolite();
			}
			String url = taggedUrls.get(i).getVertexData();
			String body = JSoupUtil.getBodyText(url);
			if (body == null) {
				continue;
			}
			int indegree = taggedUrls.get(i).getTagValue();
			scan = new Scanner(body);
			while(scan.hasNext()) {
				String word = Util.stripPunctuation(scan.next());
					if (index.containsKey(word)) {
						Map<String, Integer> urlToWordCountMap = index.get(word);
						if (urlToWordCountMap.containsKey(url)) {
							urlToWordCountMap.put(url, urlToWordCountMap.get(url) + indegree);
						} else {
							urlToWordCountMap.put(url, indegree);
						}
						index.put(word, urlToWordCountMap);
					} else if (!Util.isStopWord(word)) { // This check happening here instead of earlier. Causes isStopWord to only run (number of different words over all pages) times.
						Map<String, Integer> map = new HashMap<String, Integer>();
						map.put(url, indegree);
						index.put(word, map);
					}
			}
			scan.close();
		}
	}
  
	/**
	 * Searches the index for pages containing keyword w.  Returns a list
	 * of urls ordered by ranking (largest to smallest).  The tag 
	 * value associated with each url is its ranking.  
	 * The ranking for a given page is the number of occurrences
	 * of the keyword multiplied by the indegree of its url in
	 * the associated graph.  No pages with rank zero are included.
	 * @param w
	 *   keyword to search for
	 * @return
	 *   ranked list of urls
	 */
	public List<TaggedVertex<String>> search(String w) {
		if (index == null || index.get(w) == null) { // If you did not run make index, will return null.
			return null;
		}
		LinkedList<TaggedVertex<String>> searchResults = new LinkedList<>();
		index.get(w).entrySet().forEach(entry -> {
			boolean successfulAdd = false;
			for (int j = 0; j < searchResults.size(); j++) {
				if (entry.getValue() >= searchResults.get(j).getTagValue()) {
					searchResults.add(j, new TaggedVertex<String>(entry.getKey(), entry.getValue()));
					successfulAdd = true;
					break;
				}
			}
			if (!successfulAdd) {
				searchResults.add(new TaggedVertex<String>(entry.getKey(), entry.getValue()));
			}
		});
		
		return searchResults;
	}

	/**
	 * Searches the index for pages containing both of the keywords w1
	 * and w2.  Returns a list of qualifying
	 * urls ordered by ranking (largest to smallest).  The tag 
	 * value associated with each url is its ranking.  
	 * The ranking for a given page is the number of occurrences
	 * of w1 plus number of occurrences of w2, all multiplied by the 
	 * indegree of its url in the associated graph.
	 * No pages with rank zero are included.
	 * @param w1
	 *   first keyword to search for
	 * @param w2
	 *   second keyword to search for
	 * @return
	 *   ranked list of urls
	 */
	public List<TaggedVertex<String>> searchWithAnd(String w1, String w2) {
		if (index == null || index.get(w1) == null || index.get(w2) == null) { // If you did not run make index, will return null.
			return null;
		}
		LinkedList<TaggedVertex<String>> searchResults = new LinkedList<>();
		int w1Size = index.get(w1).size();
		int w2Size = index.get(w2).size();
		Set<Entry<String, Integer>> setResults;
		Map<String, Integer> mapResults;
		// Since an entry must exist in both to be counted, 
		// we want the one we iterate over to be the smaller set of urls.
		if (w1Size >= w2Size) {
			setResults = index.get(w2).entrySet();
			mapResults = index.get(w1);
		} else {
			setResults = index.get(w1).entrySet();
			mapResults = index.get(w2);
		}
		setResults.forEach(entry -> {
			if (mapResults.get(entry.getKey()) != null) {
				int rank = entry.getValue() + mapResults.get(entry.getKey()).intValue();
				boolean successfulAdd = false;
				for (int j = 0; j < searchResults.size(); j++) {
					if (rank >= searchResults.get(j).getTagValue()) {
						searchResults.add(j, new TaggedVertex<String>(entry.getKey(), rank));
						successfulAdd = true;
						break;
					}
				}
				if (!successfulAdd) {
					searchResults.add(new TaggedVertex<String>(entry.getKey(), rank));
				}
			}
		});

		return searchResults;
	}
  
	/**
	 * Searches the index for pages containing at least one of the keywords w1
	 * and w2.  Returns a list of qualifying
	 * urls ordered by ranking (largest to smallest).  The tag 
	 * value associated with each url is its ranking.  
	 * The ranking for a given page is the number of occurrences
	 * of w1 plus number of occurrences of w2, all multiplied by the 
	 * indegree of its url in the associated graph.
	 * No pages with rank zero are included.
	 * @param w1
	 *   first keyword to search for
	 * @param w2
	 *   second keyword to search for
	 * @return
	 *   ranked list of urls
	 */
	public List<TaggedVertex<String>> searchWithOr(String w1, String w2) {
		if (index == null) { // If you did not run make index, will return null.
			return null;
		} else if (index.get(w1) == null) {
			return search(w2);
		} else if (index.get(w2) == null) {
			return search(w1);
		}
		LinkedList<TaggedVertex<String>> searchResults = new LinkedList<>();
		Set<Entry<String, Integer>> w1Set = index.get(w1).entrySet();
		Set<Entry<String, Integer>> w2Set = index.get(w2).entrySet();
		
		// Creating a copy of the Map, that way we can remove values from this copy while 
		// iterating through w1 pages. This will allow us to avoid duplicates when compiling
		// the reusults from the w2 pages.
		Map<String, Integer> secondMap = new HashMap<String, Integer>();
		w2Set.forEach(entry -> {
			secondMap.put(entry.getKey(), entry.getValue());
		});
		
		w1Set.forEach(entry -> {
			int rank = entry.getValue().intValue();
			Integer otherRank = secondMap.get(entry.getKey());
			if (otherRank != null) {
				secondMap.remove(entry.getKey());
				rank += otherRank.intValue();
			}
			boolean successfulAdd = false;
			for (int j = 0; j < searchResults.size(); j++) {
				if (rank >= searchResults.get(j).getTagValue()) {
					searchResults.add(j, new TaggedVertex<String>(entry.getKey(), rank));
					successfulAdd = true;
					break;
				}
			}
			if (!successfulAdd) {
				searchResults.add(new TaggedVertex<String>(entry.getKey(), rank));
			}
		});
		
		
		Set<Entry<String, Integer>> w2Set2 = secondMap.entrySet();
		w2Set2.forEach(entry -> {
			boolean successfulAdd = false;
			for (int j = 0; j < searchResults.size(); j++) {
				if (entry.getValue() >= searchResults.get(j).getTagValue()) {
					searchResults.add(j, new TaggedVertex<String>(entry.getKey(), entry.getValue()));
					successfulAdd = true;
					break;
				}
			}
			if (!successfulAdd) {
				searchResults.add(new TaggedVertex<String>(entry.getKey(), entry.getValue()));
			}
		});
		
		return searchResults;
  	}
  
  	/**
  	 * Searches the index for pages containing keyword w1
  	 * but NOT w2.  Returns a list of qualifying urls
  	 * ordered by ranking (largest to smallest).  The tag 
  	 * value associated with each url is its ranking.  
  	 * The ranking for a given page is the number of occurrences
  	 * of w1, multiplied by the 
  	 * indegree of its url in the associated graph.
  	 * No pages with rank zero are included.
  	 * @param w1
  	 *   first keyword to search for
  	 * @param w2
  	 *   second keyword to search for
  	 * @return
  	 *   ranked list of urls
  	 */
  	public List<TaggedVertex<String>> searchAndNot(String w1, String w2) {
  		if (index == null || index.get(w1) == null) { // If you did not run make index, will return null.
			return null;
		} else if (index.get(w2) == null) {
			search(w1);
		}
		LinkedList<TaggedVertex<String>> searchResults = new LinkedList<>();
		index.get(w1).entrySet().forEach(entry -> {
			if (index.get(w2).get(entry.getKey()) == null) {
				boolean successfulAdd = false;
				for (int j = 0; j < searchResults.size(); j++) {
					if (entry.getValue() >= searchResults.get(j).getTagValue()) {
						searchResults.add(j, new TaggedVertex<String>(entry.getKey(), entry.getValue()));
						successfulAdd = true;
						break;
					}
				}
				if (!successfulAdd) {
					searchResults.add(new TaggedVertex<String>(entry.getKey(), entry.getValue()));
				}
			}
		});
				
		return searchResults;
  	}
}

