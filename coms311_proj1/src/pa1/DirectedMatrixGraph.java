package pa1;

import java.util.ArrayList;
import java.util.List;

import api.Graph;
import api.TaggedVertex;

/**
 * Implementation of a graph that stores vertices in an ArrayList and stores
 * edges in an adjacency matrix.
 *
 * @author Steven Rein and Amith Kopparapu Venkata Boja
 */
public class DirectedMatrixGraph<E> implements Graph<E> {

	private ArrayList<E> vertices;
	// First dimension is source, Second is destination
	private int[][] adjMatrix;
	
	public DirectedMatrixGraph(int maxVertices) {
		vertices = new ArrayList<E>();
		adjMatrix = new int[maxVertices][maxVertices];
	}
	
	public void addVertex(E src) {
		if (!vertices.contains(src)) {
			vertices.add(src);
		}
	}
	
	public void addEdge(E src, E dest) {
		int srcIndex = vertices.indexOf(src);
		int destIndex = vertices.indexOf(dest);
		if (srcIndex == -1 || destIndex == -1) {
			return;
		}
		adjMatrix[srcIndex][destIndex] = 1;
	}
	
	/**
	 * Returns an ArrayList of the actual objects constituting the vertices
	 * of this graph.
	 * @return
	 *   ArrayList of objects in the graph
	 */
	public ArrayList<E> vertexData() {
		return vertices;
	}	
	
	/**
	 * Returns an ArrayList that is identical to that returned by vertexData(), except
	 * that each vertex is associated with its incoming edge count.
	 * @return
	 *   ArrayList of objects in the graph, each associated with its incoming edge count
	 */
	public ArrayList<TaggedVertex<E>> vertexDataWithIncomingCounts() {
		ArrayList<TaggedVertex<E>> response = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			int count = 0;
			for (int j = 0; j < vertices.size(); j++) {
				count = adjMatrix[j][i] == 1 ? count + 1 : count;
			}
			response.add(new TaggedVertex<E>(vertices.get(i), count));
		}
		return response;
	}
	  
	/**
	 * Returns a list of outgoing edges, that is, a list of indices for neighbors
	 * of the vertex with given index.
	 * This method may throw ArrayIndexOutOfBoundsException if the index 
	 * is invalid.
	 * @param index
	 *   index of the given vertex according to vertexData()
	 * @return
	 *   list of outgoing edges
	 */
	public List<Integer> getNeighbors(int index) {
		if (index >= vertices.size()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		ArrayList<Integer> neighbors = new ArrayList<>();
		for (int i = 0; i < adjMatrix[index].length; i++) {
			if (adjMatrix[index][i] == 1) {
				neighbors.add(i);
			}
		}
		return neighbors;
	}
	  
	/**
	 * Returns a list of incoming edges, that is, a list of indices for vertices 
	 * having the given vertex as a neighbor.
	 * This method may throw ArrayIndexOutOfBoundsException if the index 
	 * is invalid. 
	 * @param index
	 *   index of the given vertex according to vertexData()
	 * @return
	 *   list of incoming edges
	 */
	public List<Integer> getIncoming(int index) {
		if (index >= vertices.size()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		ArrayList<Integer> incoming = new ArrayList<>();
		for (int i = 0; i < adjMatrix.length; i++) {
			 if (adjMatrix[i][index] == 1) {
				 incoming.add(i);
			 }
		}
		return incoming;
	}
	
}
