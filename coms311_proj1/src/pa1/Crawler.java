package pa1;

import api.Graph;
import api.Util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Implementation of a basic web crawler that creates a graph of some portion of
 * the world wide web.
 *
 * @author Steven Rein and Amith Kopparapu Venkata Boja
 */
public class Crawler {

	private String seedUrl;
	private int maxDepth;
	private int maxPages;

	/**
	 * Constructs a Crawler that will start with the given seed url, including only
	 * up to maxPages pages at distance up to maxDepth from the seed url.
	 * 
	 * @param seedUrl
	 * @param maxDepth
	 * @param maxPages
	 */
	public Crawler(String seedUrl, int maxDepth, int maxPages) {
		this.seedUrl = seedUrl;
		this.maxDepth = maxDepth;
		this.maxPages = maxPages;
	}

	public Graph<String> crawl() {
		DirectedMatrixGraph<String> directedGraph = new DirectedMatrixGraph<>(maxPages);
		Queue<String> queue = new LinkedList<>();
		List<Integer> depth = new ArrayList<>(); // Used to track depth.
		queue.add(seedUrl);
		directedGraph.addVertex(seedUrl);
		depth.add(0);
		int depthIndex = 0;
		boolean reachedMaxPages = directedGraph.vertexData().size() >= maxPages;

		while (!queue.isEmpty()) {
			ArrayList<String> linksSeen = new ArrayList<>();
			String url = queue.remove();
			linksSeen.add(url);
			if (depthIndex + 1 % 50 == 0) {
				JSoupUtil.bePolite();
			}
			Document doc = JSoupUtil.getDocument(url);
			if (doc == null) {
				continue;
			}
			Elements elements = JSoupUtil.getElements(doc);
			for (Element element : elements) {
				String link = JSoupUtil.getAbsoluteUrl(element);
				// Links seen determines whether we have seen the new link on the page already!
				if (!(Util.ignoreLink(url, link) || linksSeen.contains(link))) {
					linksSeen.add(link);
					// Checks if vertex already exists.
					boolean alreadyHaveVertex = directedGraph.vertexData().contains(link);
					// Checks if depth will be exceed by adding this new vertex.
					boolean depthExceded = depth.get(depthIndex) + 1 > maxDepth
							&& !directedGraph.vertexData().contains(link);
					if (!(alreadyHaveVertex || reachedMaxPages || depthExceded)) {
						queue.add(link);
						directedGraph.addVertex(link);
						depth.add(depth.get(depthIndex) + 1);
						if (!reachedMaxPages && directedGraph.vertexData().size() >= maxPages) {
							reachedMaxPages = true;
						}
					}
					if (!depthExceded && !(!alreadyHaveVertex && reachedMaxPages)) {
						directedGraph.addEdge(url, link);
					}
				}
			}
			depthIndex++;
		}

		return directedGraph;
	}
}
