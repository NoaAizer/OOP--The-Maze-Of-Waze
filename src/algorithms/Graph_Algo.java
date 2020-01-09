package algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dataStructure.*;
import oop_dataStructure.*;




/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms{
	private graph g;


	/**
	 * Default constructor.
	 */
	public Graph_Algo() {
	}
	/**
	 * Constructor
	 * @param graph represents the given graph
	 */
	public Graph_Algo(graph graph) {
		this.g=graph;
	}
	/**
	 * Init this set of algorithms on the parameter - graph.
	 * @param g is the given graph
	 */
	@Override
	public void init(graph g) {
		this.g=g;
	}
	/**
	 * Init a graph from file
	 * @param file_name represents the graph file.
	 */
	@Override
	public void init(String file_name) 
	{
		try
		{    
			FileInputStream file = new FileInputStream(file_name); 
			ObjectInputStream in = new ObjectInputStream(file); 
			g = (graph)in.readObject(); 
			in.close(); 
			file.close(); 
		} 
		catch(IOException ex) 
		{ 
			System.out.println("IOException is caught"); 
		} 
		catch(ClassNotFoundException ex) 
		{ 
			System.out.println("ClassNotFoundException is caught"); 
		} 
	}
	/** Saves the graph to a file.
	 * 
	 * @param file_name represents the name of the new file.
	 */
	@Override
	public void save(String file_name) 
	{
		try
		{  
			FileOutputStream file = new FileOutputStream(file_name); 
			ObjectOutputStream out = new ObjectOutputStream(file); 
			out.writeObject(g); 
			out.close(); 
			file.close(); 
		}   
		catch(IOException ex) 
		{ 
			System.out.println("IOException is caught"); 
		} 
	}
	/**
	 * Returns true if and only if (iff) there is a valid path from EVREY node to each
	 * other node. NOTE: assume directional graph - a valid path (a-->b) does NOT imply a valid path (b-->a).
	 * @return TRUE- if there a valid path from Every node to each, otherwise return FALSE.
	 */
	@Override
	public boolean isConnected() {
		if(g.getV()==null)return false;//there are no nodes
		boolean result=runDFS(g);
		if(!result) return false;//DFS traversal doesn't visit all nodes.
		graph tran_g=getTranspose(g);//Create a reversed graph 
		result=runDFS(tran_g);//Do DFS for reversed graph starting from the same node as before.
		return result;
	}
	/**
	 * returns the length of the shortest path between src to dest
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		try {
			for(Iterator<node_data> it=g.getV().iterator();it.hasNext();) {
				node_data n=it.next();
				n.setWeight(Double.POSITIVE_INFINITY);
				n.setInfo("");
				n.setTag(0);
			}
			g.getNode(src).setWeight(0);
			Queue<node_data> Q = new LinkedList<node_data>();
			Q.add(g.getNode(src));
			for(Iterator<node_data> it=g.getV().iterator();it.hasNext();)
			{ 
				node_data v=it.next();
				if(v.getKey()!=src)
					Q.add(v);
			}
			while(Q.size()!=0)
			{
				int u=findMinNode(Q);
				g.getNode(u).setTag(1);
				for(Iterator<edge_data> edgeIt=g.getE(u).iterator();edgeIt.hasNext();)
				{
					edge_data e=edgeIt.next();
					node_data e_dest=g.getNode(e.getDest());
					if(e_dest.getTag()==0&&e_dest.getWeight()>g.getNode(u).getWeight()+e.getWeight())
					{
						e_dest.setWeight(g.getNode(u).getWeight()+e.getWeight());
						e_dest.setInfo(""+u);
					}
				}	
			}
			for(Iterator<node_data> nodeIt=g.getV().iterator();nodeIt.hasNext();) {
				nodeIt.next().setTag(0);
			}
		}
		catch(Exception e) {

		}
		if(g.getNode(dest)==null) return Double.POSITIVE_INFINITY;
		return g.getNode(dest).getWeight();
	}


	/**
	 * returns the the shortest path between src to dest - as an ordered List of nodes:
	 * src--> n1-->n2-->...dest
	 * see: https://en.wikipedia.org/wiki/Shortest_path_problem
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		List<node_data> path=new ArrayList <node_data>();
		double dist=shortestPathDist(src, dest);
		if(dist<Double.POSITIVE_INFINITY)// there is a path
		{
			node_data v= g.getNode(dest);
			path.add(v);
			while(!v.getInfo().isEmpty())
			{
				int prev_node=Integer.parseInt(v.getInfo());
				path.add(g.getNode(prev_node));
				v.setInfo("");
				v=g.getNode(prev_node);
			}
			List<node_data> reversed_path=new ArrayList <node_data>();
			while(path.size()!=0)
				reversed_path.add(path.remove(path.size()-1));
			return reversed_path;
		}
		return null;
	}
	/**
	 * computes a relatively short path which visit each node in the targets List.
	 * Note: this is NOT the classical traveling salesman problem, 
	 * as you can visit a node more than once, and there is no need to return to source node - 
	 * just a simple path going over all nodes in the list. 
	 * @param targets represents a list of the requested nodes.
	 * @return the shortest path between those nodes.
	 */
	@Override
	public List<node_data> TSP(List<Integer> targets) 
	{
		//NOTE: There is another way (was left as a remark) which give the shortest path between the nodes -Take each one of the nodes
		// to be a source node, and pick the node with the shortest path. This way take a lot of time so we decided
		//to pick the source node randomly.
		if(!haveAPath(targets))return null;// Checks if there a path between these nodes.
		List<node_data> path=new ArrayList<node_data>();
		//						double tempMin=Double.POSITIVE_INFINITY;
		//						int currShortestSrcIn=0;
		//						for(int j=0;j<targets.size();j++) {
		//							double min=shortestPathDistToAll(j, targets);
		//							if(min<tempMin) {
		//								currShortestSrcIn=j;
		//								tempMin=min;
		//							}
		//						}
		//						int srcIndex=currShortestSrcIn;
		try {
			int srcIndex=(int)(Math.random()*targets.size());// Randomly takes one of the nodes in the list as a src node

			int src= targets.get(srcIndex);
			int destIndex=0,dest=0;
			targets.remove(srcIndex);
			path.add(g.getNode(src));
			while(!targets.isEmpty()) {
				double minPath=Double.POSITIVE_INFINITY;
				//Finds the shortest path between the src and one of the nodes
				for(int i=0;i<targets.size();i++) {
					double dist=shortestPathDist(src, targets.get(i));
					if(dist<minPath) {
						minPath=dist;
						dest=targets.get(i);
						destIndex=i;
					}
				}
				List<node_data> tempPath=shortestPath(src, dest);
				if(tempPath==null) return null;
				boolean flag_first=true;
				for (node_data n : tempPath) {
					if(flag_first) flag_first=false;
					else path.add(n);
				}

				targets.remove(destIndex);
				src=dest;
			}
		}
		catch(Exception e) {};

		return path;
	}
	/** 
	 * Compute a deep copy of this graph.
	 * @return the new copied graph.
	 */
	@Override
	public graph copy() {
		String file_name = "fileForCopy.txt";
		this.save(file_name);
		Graph_Algo ga=new Graph_Algo();
		ga.init(file_name);
		File file = new File(file_name); 
		file.delete(); 
		return ga.g;
	}
	/**
	 * Graph getter.
	 * @return the graph in this class.
	 */
	public graph getG() {
		return g;
	}

	/*****************Private Methods********************/
	/**
	 * Runs DFS algorithm on the given graph.
	 * @param g2 represents the graph to be traveled.
	 * @return true- if all nodes are connected , otherwise false.
	 */
	private boolean runDFS(graph g2) {
		boolean flag_first=true;
		//Marks all the nodes as not visited 
		for(Iterator<node_data> it=g2.getV().iterator();it.hasNext();) {
			it.next().setTag(0);
		}
		//Do DFS traversal starting from first node. 
		for(Iterator<node_data> it=g2.getV().iterator();it.hasNext()&&flag_first;) {
			node_data v=it.next();
			flag_first=false;
			DFSUtil(v.getKey(),g2);
		}
		// If DFS traversal doesn't visit all nodes, then return false.
		for(Iterator<node_data> it=g2.getV().iterator();it.hasNext();) {
			node_data v=it.next();
			if(g2.getNode(v.getKey()).getTag()==0)
				return false;
		}
		return true;
	}

	/**
	 * Runs DFS starting from the first node.
	 * @param key represents the first node.
	 */
	private void DFSUtil(int key,graph g2) 
	{ 

		// Mark the current node as visited
		g2.getNode(key).setTag(1); 

		if(g2.getE(key)==null)return;//there are no edges
		// Recur for all the nodes adjacent to this node
		for(Iterator<edge_data> edgeIt=g2.getE(key).iterator();edgeIt.hasNext();)
		{
			edge_data e=edgeIt.next();
			node_data e_dest=g2.getNode(e.getDest());
			if(e_dest.getTag()==0)
				DFSUtil(e_dest.getKey(),g2);	
		}
	}
	/**
	 * Transposes the original graph.
	 * @return transpose of this graph 
	 */
	private graph getTranspose(graph g2) 
	{ 
		DGraph tran_g = new DGraph(); 

		//Add all the nodes.
		for(Iterator<node_data> it=g2.getV().iterator();it.hasNext();) {
			node_data v=it.next();
			tran_g.addNode(v);
		}
		//Add all the edges (but in opposite direction).
		for(Iterator<node_data> it=g2.getV().iterator();it.hasNext();) {
			node_data v=it.next();
			if(g2.getE(v.getKey())!=null)
				for(Iterator<edge_data> edgeIt=g2.getE(v.getKey()).iterator();edgeIt.hasNext();) {
					edge_data e=edgeIt.next();
					node_data e_dest=g2.getNode(e.getDest());
					tran_g.connect(e_dest.getKey(), v.getKey(), e.getWeight());
				}
		}
		return tran_g; 
	} 
	/**
	 * Finds the node with the minimum weight and removes it from the queue.
	 * @param q represents the queue with the unvisited nodes.
	 * @return the node with the minimum weight
	 */
	private int findMinNode(Queue<node_data> q) {
		double weight=Double.POSITIVE_INFINITY;
		int minNode=0;
		node_data temp=null;
		for (Iterator<node_data> it = q.iterator(); it.hasNext();) {
			node_data node=it.next();
			if(node.getWeight()<weight)
			{
				weight=node.getWeight();
				minNode=node.getKey();
				temp=node;
			}
		}
		q.remove(temp);
		return minNode;
	}
	// NOTE: Related to the remark in TSP method.
	/**
	 * Calculates the shortest path from one of the nodes in the target list to the rest.
	 * @param srcIndex the index of this node.
	 * @param targets 
	 * @return the shortest path distance from the requested node
	 */
	//	private double shortestPathDistToAll(int srcIndex,List<Integer> targets) {
	//		List<node_data> path=new ArrayList<node_data>();
	//		List<Integer> targetsTemp=new ArrayList<Integer>();
	//		double min= Double.POSITIVE_INFINITY;
	//		for (Integer tar : targets) {
	//			targetsTemp.add(tar);
	//		}
	//		try {
	//			int src= targetsTemp.get(srcIndex);
	//			int destIndex=0,dest=0;
	//			targetsTemp.remove(srcIndex);
	//			path.add(g.getNode(src));
	//			while(!targetsTemp.isEmpty()) {
	//				double minPath=Double.POSITIVE_INFINITY;
	//				//Finds the shortest path between the src and one of the nodes
	//				for(int i=0;i<targetsTemp.size();i++) {
	//					if(targetsTemp.get(i)==null)
	//						throw new RuntimeException("ERR: the requseted node is not exist in the graph.");
	//					else
	//					{double dist=shortestPathDist(src, targetsTemp.get(i));
	//					if(dist<minPath) {
	//						minPath=dist;
	//						dest=targetsTemp.get(i);
	//						destIndex=i;
	//					}
	//					}
	//					min+=minPath;
	//					targetsTemp.remove(destIndex);
	//					src=dest;
	//				}
	//			}
	//		}
	//		catch(Exception e) {System.out.println(e.getMessage());}
	//		return min; 
	//	}
	/**
	 * Checks if there a path between some of the nodes in the graph
	 * @param targets represents a list of the requested nodes in the graph
	 * @return true- if there a path, otherwise return false.
	 */
	private boolean haveAPath(List<Integer> targets) {
		Graph_Algo ga=new Graph_Algo();
		ga.init(g);
		List<Integer>keys=new ArrayList<Integer>();
		for (Iterator<node_data> itNode = g.getV().iterator(); itNode.hasNext();) 
			keys.add(itNode.next().getKey());
		boolean src=false,first=true;
		int srcID=0;
		//Checks if the src node is in the node targets list.
		for(Iterator<node_data> it=ga.getG().getV().iterator();it.hasNext()&&first;) {
			int key=it.next().getKey();
			if(targets.contains(key)) {
				src=true;
				srcID=key;
			}
			first=false;
		}
		if(src) {
			//Checks if there is a path to the src node.
			ga.isSrcConnect();
			first=true;
			for (Integer key : keys) {
				if(key!=srcID) {
					if(targets.contains(key)&&ga.getG().getNode(key).getTag()==0)
						return false;
				}
			}
		}
		ga.isConnect();	
		for (Integer key : keys) {
			if(key!=srcID) {
				if(targets.contains(key)&&ga.getG().getNode(key).getTag()==0)
					return false;
			}
		}
		return true;
	}
	/**
	 * Checks if the graph is connected (except a path to the source node)
	 * @return the graph after visiting the nodes.
	 */
	private graph isConnect() {
		if(g.getV()==null)return null;//there are no nodes
		boolean result=runDFS(g);
		if(!result) return null;//DFS traversal doesn't visit all nodes.
		return g;
	}
	/**
	 * Checks if there a path to the source node from the other nodes.
	 * @return  the graph after visiting the nodes.
	 */
	private graph isSrcConnect() {
		graph tran_g=getTranspose(g);//Create a reversed graph 
		runDFS(tran_g);//Do DFS for reversed graph starting from the same node as before.
		return g;
	}


}