package example;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import api.Graph;
import api.TaggedVertex;
import pa1.Crawler;
import pa1.Index;

/**
 * Example illustrating basic usage of the jsoup library.
 * This example finds all links in a given url and 
 * identifies links that should be ignored (according to 
 * Util.ignoreLink) and those that
 * link to non-html documents.
 */
public class JSoupTest {

	public static void main(String[] args) throws IOException {
	
	    String url = "https://en.wikipedia.org/wiki/Gouraud_shading";
	      
	    System.out.println("Fetching " + url);
	    
	    Crawler crawler = new Crawler(url, 10, 30);
	    
	    // Testing the Adjacency list implementation
	    long startTimeMatrix = System.nanoTime();
	    Graph<String> g = crawler.crawl();
	    long endTimeMatrix = System.nanoTime();
	    ArrayList<String> vertices = g.vertexData();
	    for(int i = 0; i < vertices.size(); i++) {
	    	System.out.println("" + vertices.get(i));
	    	List<Integer> neighbors = g.getNeighbors(i);
	    	for (int j = 0; j < neighbors.size(); j++) {
	    		System.out.println("--" + vertices.get(neighbors.get(j)));
	    	}
	    }
	    
	    System.out.println("Matrix time: " + (endTimeMatrix-startTimeMatrix));
	    
	    System.out.println("\n\n\n\n");
	    
	    // Create Index
	    Index index = new Index(g.vertexDataWithIncomingCounts());
	    index.makeIndex();

	    // Do regular search
	    System.out.println("\n\n\n\n");
	    System.out.println("Regular Search:");
	    index.search("shading").forEach(vertex -> {
	    	System.out.println("URL: " + vertex.getVertexData() + " Count: " + vertex.getTagValue());
	    });

	    // Do and search
	    System.out.println("\n\n\n\n");
	    System.out.println("And Search:");
	    index.searchWithAnd("shading", "tree").forEach(vertex -> {
	    	System.out.println("URL: " + vertex.getVertexData() + " Count: " + vertex.getTagValue());
	    });
	    
	    // Do or search
	    System.out.println("\n\n\n\n");
	    System.out.println("Or Search:");	    
	    index.searchWithOr("shading", "tree").forEach(vertex -> {
	    	System.out.println("URL: " + vertex.getVertexData() + " Count: " + vertex.getTagValue());
	    });
	    // Do not search
	    System.out.println("\n\n\n\n");
	    System.out.println("Not Search:");
	    index.searchAndNot("shading", "tree").forEach(vertex -> {
	    	System.out.println("URL: " + vertex.getVertexData() + " Count: " + vertex.getTagValue());
	    });
	    
	}

}
