///////////////////////////////////////////////////////////////////////////////
//                  
// Main Class File:  TheGame.java
// File:             DirectedGraph.java
// Semester:         CS 367 Fall 2015
//
// Author:           Andrew Zietlow arzietlow@wisc.edu
// CS Login:         azietlow
// Lecturer's Name:  Jim Skrentny
// Lab Section:      Lecture 1
//
//
// Pair Partner:     N/A
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Implements GraphADT with an adjacency lists representation. The Hashmap 
 * will map a vertex to its adjacency list, which in turn is an ArrayList<V>. 
 * This is the underlying structure of the game. It is used in initialization
 * and in execution.
 * <p>Bugs: none known
 *
 * @author azietlow
 */

public class DirectedGraph<V> implements GraphADT<V>{
	//V is a key, ArrayList<V> is that key's adjacency list
	private HashMap<V, ArrayList<V>> hashmap;

	/**Creates an empty graph with the default initial capacity (16) 
	 * and the default load factor (0.75)
	 */
    public DirectedGraph() {
    	hashmap =  new HashMap<V, ArrayList<V>>();
    }

    /**Constructs an empty HashMap with with the same mappings as 
     * the specified Map
     * @param (hashmap) the Map to copy the dimensions of 
     * @throws IllegalArgumentException if the parameter is null
     */
    public DirectedGraph(HashMap<V, ArrayList<V>> hashmap) {
    	if (hashmap == null) throw new IllegalArgumentException();
    	hashmap = new HashMap<V, ArrayList<V>>(hashmap);
    }

    
	/** Adds the specified vertex to this graph if not already present. 
	 * @param (vertex) the specified vertex
	 * @return whether or not the add was successful
	 */
    @Override
    public boolean addVertex(V vertex) {
    	if (vertex == null) throw new IllegalArgumentException();
    	if (hashmap.containsKey(vertex)) return false; //graph contains V
    	hashmap.put(vertex, new ArrayList<V>());
    	return true;
    }

    
    /**
     * Creates a new edge from vertex v1 to v2
     * The specified edge must not already exist, and v1 can't be .equal to v2
     * @param (v1, v2) the source and destination vertexes
     * @return whether or not the edge was successfully added
     * @throws IllegalArgumentException if v1 or v2 are not in the graph
     */
    @Override
    public boolean addEdge(V v1, V v2) {
    	//returns false if the vertices are the same or null, or edge exists
    	if ((v1 == null) || (v2 == null)) return false;
    	if (v1.equals(v2)) return false; 
    	//only throws exception when a vertex is not in the graph
    	if ((!hashmap.containsKey(v1)) || (!hashmap.containsKey(v2))) 
    		throw new IllegalArgumentException(); 
    	ArrayList<V> edges = hashmap.get(v1);
    	if (edges.contains(v2)) return false;//edge exists 
    	else { 
    		edges.add(v2);
    		return true;
    	}
    }

    /**
     * Returns a set of all vertices to which (vertex) has outward edges
     * @param (vertex) the vertex to gather neighbors from
     * @throws IllegalArgumentException if (vertex) is not found in the graph
     * @return the neighbor vertexes of (vertex)
     */
    @Override
    public Set<V> getNeighbors(V vertex) {
    	if ((vertex == null) || (hashmap.containsKey(vertex) == false)) 
			throw new IllegalArgumentException();
    	Set<V> neighbors = new HashSet<V>();
    	ArrayList<V> neighborList = hashmap.get(vertex);
    	neighbors.addAll(neighborList);
    	return neighbors;
    }
    
    /**If both v1 and v2 exist in the graph, AND an edge exists from v1 to v2, 
     * removes the edge from this graph. Otherwise does nothing.
     * @param (v1, v2) source and destination vertex
     */
    @Override
    public void removeEdge(V v1, V v2) {
        if ((v1 == null) || (v2 == null)) return;
    	if ((!hashmap.containsKey(v1)) || (!hashmap.containsKey(v2))) return;
    	ArrayList<V> edges = hashmap.get(v1);
    	if (!edges.contains(v2)) return;
    	edges.remove(v2);
    }

    /**
     * Returns a set of all the vertices in the graph.
     * @return the vertex set of the graph
     */
    @Override
    public Set<V> getAllVertices() {
		return hashmap.keySet();
    }
    
    
	////////////////////////////////////////////////////////////////////////////
	////////////////////////       CS 367 Code       ///////////////////////////
	////////////////////////////////////////////////////////////////////////////

    @Override
    //Returns a String that depicts the Structure of the Graph
    //This prints the adjacency list
    //This has been done for you
    //DO NOT MODIFY
    public String toString() {
        StringWriter writer = new StringWriter();
        for (V vertex: this.hashmap.keySet()) {
            writer.append(vertex + " -> " + hashmap.get(vertex) + "\n");
        }
        return writer.toString();
    }

}
