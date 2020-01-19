package dataStructure;


import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.Point3D;

public class DGraph implements graph, Serializable{

	int mc;
	int edgesCounter;
	HashMap<Integer,node_data> nodes;
	HashMap<Integer,HashMap<Integer,edge_data>> edges;
	/**
	 * Default constructor. 
	 */
	public DGraph() {
		this.mc = 0;
		this.edgesCounter = 0;
		this.nodes = new HashMap<Integer,node_data>();
		this.edges = new HashMap<Integer,HashMap<Integer,edge_data>>();
	}
	/**
	 * Builds a new graph from a given graph.
	 * @param g represents the given graph.
	 */
	public DGraph(DGraph g) {
		this.mc = g.getMC();
		this.edgesCounter = g.edgeSize();
		this.nodes=(HashMap<Integer, node_data>) g.getNodes();
		this.edges = (HashMap<Integer,HashMap<Integer,edge_data>>) g.getEdges();
	}
	
    public DGraph(final String file_name) {
        try {
            this.init();
            node_data.resetCount();
            final Scanner scanner = new Scanner(new File(file_name));
            final String jsonString = scanner.useDelimiter("\\A").next();
            scanner.close();
            final JSONObject graph = new JSONObject(jsonString);
            final JSONArray nodes = graph.getJSONArray("Nodes");
            final JSONArray edges = graph.getJSONArray("Edges");
            for (int i = 0; i < nodes.length(); ++i) {
                final int id = nodes.getJSONObject(i).getInt("id");
                final String pos = nodes.getJSONObject(i).getString("pos");
                final Point3D p = new Point3D(pos);
                this.addNode(new Node(id, p));
            }
            for (int i = 0; i < edges.length(); ++i) {
                final int s = edges.getJSONObject(i).getInt("src");
                final int d = edges.getJSONObject(i).getInt("dest");
                final double w = edges.getJSONObject(i).getDouble("w");
                this.connect(s, d, w);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
	 * return the node_data by the node_id,
	 * @param key - the node_id
	 * @return the node_data by the node_id, null if none.
	 */
	@Override
	public node_data getNode(int key) {
		if(nodes.get(key)!=null)
			return nodes.get(key);
		else
			return null;
	}
	/**
	 * Returns the data of the edge (src,dest), null if none.
	 * Note: this method should run in O(1) time.
	 * @param src represents the source node
	 * @param dest represents the destination node.
	 * @return the data of the edge, null if none.
	 */
	@Override
	public edge_data getEdge(int src, int dest) {
		if(edges.get(src)!=null&&edges.get(src).containsKey(dest))
			return edges.get(src).get(dest);
		else
			return null;
	}
	/**
	 * Adds a new node to the graph with the given node_data.
	 * @param n represents the given node.
	 */
	@Override
	public void addNode(node_data n) {
		try {
			if(nodes.get(n.getKey())!=null)
				throw new RuntimeException("ERR: The node is already exist in the graph");
			nodes.put(n.getKey(),n);
			mc++;
		}
		catch(Exception e) {}
	}
	/**
	 * Connect an edge with weight w between node src to node dest.
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @param w - positive weight representing the cost (aka time, price, etc) between src--dest.
	 */
	@Override
	public void connect(int src, int dest, double w) {
		try {
			if(w<=0)
				throw new RuntimeException("ERR: The weight should be larger than 0");
			if(src==dest)
				throw new RuntimeException("ERR: The nodes should be different");
			if(nodes.containsKey(src)&&nodes.containsKey(dest)) {
				Edge e = new Edge(src, dest, w,"",0);	
				if(!edges.containsKey(src)) {//if the src node is already exist
					HashMap<Integer,edge_data> newEdge= new HashMap<Integer,edge_data>();//empty hashmap
					edges.put(src, newEdge);
				}
				edges.get(src).put(dest,e);
				mc++;
				edgesCounter++;
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the nodes in the graph. 
	 * @return Collection of node_data elements represents a list of the nodes in the graph.
	 */
	@Override
	public Collection<node_data> getV() {
		return nodes.values();
	}
	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the edges getting out of 
	 * the given node (all the edges starting (source) at the given node). 
	 * @return Collection of edge_data elements represents a list of the edges in the graph.
	 */
	@Override
	public Collection<edge_data> getE(int node_id) {
		if(edges.containsKey(node_id))// the node is exist
			return this.edges.get(node_id).values();
		return null;
	}
	/**
	 * Delete the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 * This method should run in O(n), |V|=n, as all the edges should be removed.
	 * @return the data of the removed node (null if none). 
	 * @param key represents the ID of the requested node.
	 */
	@Override
	public node_data removeNode(int key) {
		if(nodes.containsKey(key)) {// the node is exist
			if(nodes.containsKey(key))// if there is an edge with this node as a src
				for (Integer node : nodes.keySet())// runs on all the nodes
				{
					if(node!=key&& edges.containsKey(node)&&edges.get(node).containsKey(key)) {// the key is the dest node
						edges.get(node).remove(key);
						edgesCounter--;
						mc++;
					}
					if(node==key&&edges.containsKey(key)) {// the key is the src node
						int numOfEdges=edges.get(key).size();// how many edges has been removed
						mc+=numOfEdges;
						edgesCounter-=numOfEdges;
						edges.remove(key);
					}

				}
			mc++;
			node_data removed=nodes.remove(key);// remove from the node hashmap.
			return removed;
		}
		return null;
	}
	/**
	 * Delete the edge from the graph, 
	 * Note: this method should run in O(1) time.
	 * @param src represents the source node.
	 * @param dest represents the destination node.
	 * @return the data of the removed edge (null if none).
	 */
	@Override
	public edge_data removeEdge(int src, int dest) {
		if(edges.containsKey(src)&&edges.get(src).containsKey(dest)) {// the edge is exist
			mc++;
			edgesCounter--;
			return edges.get(src).remove(dest);
		}
		return null;
	}
	/** Returns the number of vertices (nodes) in the graph.
	 * @return the number of vertices.
	 */
	@Override
	public int nodeSize() {
		return nodes.size();
	}
	/** 
	 * Returns the number of edges (assume directional graph).
	 * Note: this method should run in O(1) time.
	 * @return the number of edges.
	 */
	@Override
	public int edgeSize() {
		return edgesCounter;
	}
	/**
	 * Returns the Mode Count - for testing changes in the graph.
	 * @return the amount of change made on the graph.
	 */
	@Override
	public int getMC() {
		return mc;
	}
	/**
	 * Nodes getter.
	 * @return the hashmap that contains all the nodes.
	 */
	public HashMap<Integer, node_data> getNodes() {
		return this.nodes;
	}
	/**
	 * Edges getter.
	 * @return the hashmap that contains all the edges.
	 */
	public HashMap<Integer, HashMap<Integer, edge_data>> getEdges() {
		return this.edges;
	}

/**
 * Initializes a DGraph from Json file.
 * @param jsonSTR represents the given Json file.
 */
	public void init(final String jsonSTR) {
		try {
			node_data.resetCount();
			this.init();
			this.edgesCounter = 0;
			final JSONObject graph = new JSONObject(jsonSTR);
			final JSONArray nodes = graph.getJSONArray("Nodes");
			final JSONArray edges = graph.getJSONArray("Edges");
			for (int i = 0; i < nodes.length(); ++i) {
				final int id = nodes.getJSONObject(i).getInt("id");
				final String pos = nodes.getJSONObject(i).getString("pos");
				final Point3D p = new Point3D(pos);
				this.addNode(new Node(id, p));
			}
			for (int i = 0; i < edges.length(); ++i) {
				final int s = edges.getJSONObject(i).getInt("src");
				final int d = edges.getJSONObject(i).getInt("dest");
				final double w = edges.getJSONObject(i).getDouble("w");
				this.connect(s, d, w);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Converts a DGraph to json file.
	 * @return a json file of a DGrpah.
	 */
	public String toJSON() {
        final JSONObject allEmps = new JSONObject();
        final JSONArray VArray = new JSONArray();
        final JSONArray EArray = new JSONArray();
        final Collection<node_data> V = this.getV();
        final Iterator<node_data> iter = V.iterator();
        Iterator<edge_data> itr = null;
        try {
            while (iter.hasNext()) {
                final node_data nn = iter.next();
                final int n = nn.getKey();
                final String p = nn.getLocation().toString();
                final JSONObject node = new JSONObject();
                node.put("id", n);
                node.put("pos", (Object)p);
                VArray.put((Object)node);
                itr = this.getE(n).iterator();
                while (itr.hasNext()) {
                    final edge_data ee = itr.next();
                    final JSONObject edge = new JSONObject();
                    edge.put("src", ee.getSrc());
                    edge.put("dest", ee.getDest());
                    edge.put("w", ee.getWeight());
                    EArray.put((Object)edge);
                }
            }
            allEmps.put("Nodes", (Object)VArray);
            allEmps.put("Edges", (Object)EArray);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return allEmps.toString();
    }
	  private void init() {
	        this.nodes = new HashMap<Integer, node_data>();
	        this.edges = new HashMap<Integer, HashMap<Integer, edge_data>>();
	    }


}