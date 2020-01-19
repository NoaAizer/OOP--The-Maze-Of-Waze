package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import Server.game_service;
import dataStructure.*;
import elements.*;
import gameClient.MyGameGUI;
import utils.Point3D;


public class Game_Algo {

	public static final double EPS1=0.000001;
	public static Graph_Algo algo;


	/**
	 * Creates a new arena from the json file of the service game;
	 * @param gameStr represents the json file.
	 * @return a new arena.
	 */ 
	public static Arena createArenaFromJson(String gameStr) 
	{	
		gameStr=gameStr.substring(14,gameStr.length()-1);
		Gson gson = new Gson();
		try
		{
			Arena gameObj=gson.fromJson(gameStr, Arena.class);
			return gameObj;
		} 
		catch ( JsonSyntaxException  e) //default value for unreadable file
		{
			throw new RuntimeException("wrong format for Game");
		}
	}
	/**
	 * Updates the grade of the game after each move.
	 * @return the updated grade.
	 */
	public static int updateGrade() {
		return Game_Algo.createArenaFromJson(MyGameGUI.arena.getGame().toString()).getGrade();
	}
	/**
	 * Updates the moves of in the game.
	 * @return the amount of moves in the game.
	 */
	public static int updateMoves() {
		return Game_Algo.createArenaFromJson(MyGameGUI.arena.getGame().toString()).getMoves();
	}
	/**
	 * Creates a fruit from a string (json).
	 * @param fruitStr represents a json file of a fruit.
	 * @return an object of the given fruit.
	 */
	public static Fruit createFruit(String fruitStr) {
		Gson gson = new Gson();
		try
		{
			Fruit f=gson.fromJson(fruitStr, Fruit.class);
			return f;
		} 
		catch ( JsonSyntaxException  e) //default value for unreadable file
		{
			throw new RuntimeException("wrong format for fruit");
		}

	}
	/**
	 * Creates a list of all the fruits in the game.
	 * @return the list of the fruits.
	 */
	public static List<Fruit> createFruitsList()
	{
		List<Fruit> fruits = new ArrayList<Fruit>();
		Iterator<String> frItr=MyGameGUI.arena.getGame().getFruits().iterator();
		while(frItr.hasNext())
		{
			String fruit=frItr.next();
			fruit=fruit.substring(9,fruit.length()-1);
			Fruit f=createFruit(fruit);
			if (MyGameGUI.kml != null) {
				if (f.getType() == -1) {
					MyGameGUI.kml.addPlaceMark("banana", f.getPos());
				} else {
					MyGameGUI.kml.addPlaceMark("apple", f.getPos());
				}
			}
			fruits.add(f);//add to the list of fruit
		}
		return fruits;
	}
	/**
	 * Creates a robot from a string (json).
	 * @param robotStr represents a json file of a robot.
	 * @return an object of the given robot.
	 */
	public static Robot createRobot(String robotStr) {
		robotStr=robotStr.substring(9,robotStr.length()-1);
		Gson gson = new Gson();
		try
		{
			Robot r=gson.fromJson(robotStr, Robot.class);
			return r;
		} 
		catch ( JsonSyntaxException  e) //default value for unreadable file
		{
			throw new RuntimeException("wrong format for fruit");
		}

	}
	/**
	 * Creates a list of all the robots in the game.
	 * @return the list of the robots.
	 */
	public static List<Robot> createRobotsList()
	{
		List<Robot> robots=new ArrayList<Robot>();
		List<String> rob=MyGameGUI.arena.getGame().getRobots();
		for(String robStr:rob)
		{
			Robot r = createRobot(robStr);
			if (MyGameGUI.kml != null) {
				MyGameGUI.kml.addPlaceMark( "robot", r.getPos());
			}
			robots.add(r);
		}
		return robots;
	}

	/**
	 * Position the robots in an auto mode next to the fruits according to their values (from high to low).
	 */
	public static void autoRobotLocation()
	{
		List<Fruit> fruits=MyGameGUI.arena.getFruitsList();
		Collections.sort(fruits);// sort the fruits by their values (from high to low).
		int numOfFruits=fruits.size();
		Fruit[] frArr = new Fruit [numOfFruits];
		int i=0;
		for (Fruit fruit : fruits) {
			frArr[i]=fruit;
			i++;
		}
		int numOfRobots=MyGameGUI.arena.getRobots();
		int fruitNum=0;
		for(i=0;i<numOfRobots;i++) {
			fruitNum=i;
			if(fruitNum>=numOfFruits)fruitNum=i%numOfFruits;
			MyGameGUI.arena.getGame().addRobot(findFruitSrc(frArr[fruitNum]));
			MyGameGUI.arena.setRobotsList(Game_Algo.createRobotsList());
		}
	}
	/**
	 * Move the robots in an auto mode. Each robot goes to the closest fruit which unselected by another robot.
	 * @param game represents the game server
	 * @param g represents the game graph.
	 * @param fruits represents the list of the fruit in the game.
	 */
	public static void moveRobotsAuto(game_service game , DGraph g, List<Fruit>fruits) {
		ArrayList<Integer> chosenDest= new ArrayList<Integer>();
		algo= new Graph_Algo(g);
		List<String> log = game.move();
		if(log!=null) {
			for(int i=0;i<log.size();i++) {// log.size= the amount of robots
				String robot_json = log.get(i);
				Robot robot= createRobot(robot_json);
				chosenDest.add(robot.getDest());	
				int rid = robot.getId();
				int src = robot.getSrc();
				int dest = robot.getDest();

				if(dest==-1) {	
					HashMap<Integer,Fruit> nodeOfFruits=nodesOfFruits();
					if(nodeOfFruits.containsKey(src)) {// the robot is on a src node of a fruit edge.
						dest = bestAdj(src);
						game.chooseNextEdge(rid,dest);
						chosenDest.add(dest);
					}
					else {
						dest = closetFruit(src);
						if(!chosenDest.contains(dest)) {
							game.chooseNextEdge(rid, dest);
							chosenDest.add(dest);
						}
						else {
							//Finds a neighbor node (of the robot node) where no robot goes.
							for(edge_data e:algo.getG().getE(src)) {
								if(!chosenDest.contains(e.getDest())) {
									chosenDest.add(e.getDest());
									game.chooseNextEdge(rid, e.getDest());
								}
							}

						}
					}
				}
			}
		}
		resetEdgesTag();
	}
	/**
	 * Initializes all the edges to be unvisited.
	 */
	private static void resetEdgesTag() {
		for(node_data n: algo.getG().getV()) 	
			for(edge_data e: algo.getG().getE(n.getKey())) 
				e.setTag(0);
	}
	/**
	 * Finds the adjacent edge with the highest fruit values.
	 * @param src represents the robot node.
	 * @return the destination node of the edge found .
	 */
	private static int bestAdj(int src) {
		List<Fruit> fruits=MyGameGUI.arena.getFruitsList();
		DGraph g= (DGraph) algo.getG();
		double maxVal=0;
		int maxDest=-1;
		for (edge_data edge:g.getE(src)) {
			double currVal=0;
			for (Fruit f : fruits) {
				if(isOnEdge(f.getPos(), edge,f.getType())) 
					currVal+=f.getValue();	
			}
			if(maxVal<currVal) {
				maxVal=currVal;
				maxDest=edge.getDest();
			}
		}
		g.getEdge(src, maxDest).setTag(1);
		return maxDest;
	}
	/**
	 * Finds the closet fruit to the robot.
	 * @param src represents the robot node.
	 * @return the source node of the closet fruit edge.
	 */
	private static int closetFruit(int src) {
		double min=Double.POSITIVE_INFINITY;
		int dest=-1;
		double dist;
		HashMap <Integer,Fruit>nodes=nodesOfFruits();
		for (Integer node : nodes.keySet()) {
			dist=algo.shortestPathDist(src, node);
			if(dist<min&&edgeOfFruit(nodes.get(node)).getTag()==0)
			{
				min=dist;
				dest=node;
			}
		}
		
		edgeOfFruit(nodes.get(dest)).setTag(1);
		return algo.shortestPath(src, dest).get(1).getKey();
	}

	public static boolean isOnEdge(Point3D p, Point3D src , Point3D dest) {
		boolean ans=false;
		double dist= src.distance2D(dest);
		double d1=src.distance2D(p)+p.distance2D(dest);
		if(dist>d1-EPS1) {ans=true;}
		return ans;
	}
	public static boolean isOnEdge(Point3D p, int s , int d, graph g) {
		Point3D src= g.getNode(s).getLocation();
		Point3D dest= g.getNode(d).getLocation();
		return isOnEdge(p, src, dest);
	}
	/**
	 * Checks if a given fruit is on the given edge.
	 * @param p represents the location of the fruit.
	 * @param e represents the given edge.
	 * @param type represents the type of the given fruit

	 * @return true- if the fruit is on the edge, otherwise return false.
	 */
	public static boolean isOnEdge(Point3D p, edge_data e, double type) {
		DGraph g= MyGameGUI.arena.getG();
		int src=g.getNode(e.getSrc()).getKey();
		int dest=g.getNode(e.getDest()).getKey();
		if(type<0 && dest>src) return false;
		if(type>0 && src>dest) return false;
		return isOnEdge(p, src, dest, g);
	}
	/**
	 * Finds the edge on which the fruit is located.
	 * @param fr represents the given fruit.
	 * @return the edge of the given fruit.
	 */
	private static edge_data edgeOfFruit(Fruit fr) {
		for(node_data n: MyGameGUI.arena.getG().getV()) {	
			for(edge_data e:  MyGameGUI.arena.getG().getE(n.getKey())) {
				if(isOnEdge(fr.getPos(), e, fr.getType()))
					return e;
			}
		}
		return null;
	}
	/**
	 * Connects each fruit to his edge source node.
	 * @return an hash map of nodes and their fruits.
	 */
	private static HashMap<Integer,Fruit> nodesOfFruits(){
		List<Fruit>fruits=MyGameGUI.arena.getFruitsList();
		HashMap<Integer,Fruit> nodes= new HashMap<>();
		int key=-1;
		for (Fruit fruit : fruits) {
			key=findFruitSrc(fruit);
			if(key!=-1)
				nodes.put(key, fruit);
		}
		return nodes;
	}
	/**
	 * Finds the node which we need to get in order to eat the fruit.
	 * @param f represents the given fruit.
	 * @return the found node key, returns -1 if the node cannot be found.
	 */
	public static int findFruitSrc(Fruit f) {
		double type=f.getType();
		for(node_data n: MyGameGUI.arena.getG().getV()) {	
			for(edge_data e:  MyGameGUI.arena.getG().getE(n.getKey())) {
				if(isOnEdge(f.getPos(), e, f.getType())) {
					if(type==-1) 
						return Math.max(e.getSrc(), e.getDest());
					return Math.min(e.getSrc(), e.getDest());
				}
			}
		}
		return -1;
	}
	/**
	 * Checks if a given point is represents a robot.
	 * @param x represents the x value of the given point.
	 * @param y represents the y value of the given point.
	 * @return the robot in that point, if there is no robot in this location- returns null.
	 */
	public static Robot isARobot(double x,double y) {
		List<String>robListStr=MyGameGUI.arena.getGame().move();
		List<Robot>robList=new ArrayList<Robot>();
		for (String r : robListStr) {
			robList.add(Game_Algo.createRobot(r));
		}
		for(Iterator<Robot> robIter=robList.iterator();robIter.hasNext();) {
			Robot r=robIter.next();
			if(Math.abs(x-r.getPos().x())<=0.001
					&&Math.abs(y-r.getPos().y())<=0.001) {
				return r;
			}
		}
		return null;
	}
	/**
	 * Checks if the given points represents a node in the graph.
	 * @param x represents the x value of the given point.
	 * @param y represents the y value of the given point.
	 * @return the key of the node in the given point, if there is no node in that location- returns null.
	 */
	public static int isANode(double x,double y) {
		for(Iterator<node_data> verIter=MyGameGUI.arena.getG().getV().iterator();verIter.hasNext();) {
			node_data nd=verIter.next();
			if(Math.abs(x-nd.getLocation().x())<= 0.0005
					&&Math.abs(y-nd.getLocation().y())<= 0.0005)
				return nd.getKey();
		}
		return -1;
	}
}
