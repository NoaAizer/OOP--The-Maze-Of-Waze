package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import elements.*;
import utils.Point3D;


public class Game_Algo {

	public static final double EPS1=0.000001;
	public static Graph_Algo algo;

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

	public static boolean isOnEdge(Point3D p, edge_data e, double type, graph g) {
		int src=g.getNode(e.getSrc()).getKey();
		int dest=g.getNode(e.getDest()).getKey();
		if(type<0 && dest>src) return false;
		if(type>0 && src>dest) return false;
		return isOnEdge(p, src, dest, g);
	}

	public static void updateEdge(Fruit fr, graph g) {
		Iterator<node_data> itr = g.getV().iterator();
	}

	/**
	 * Finds the edge on which the fruit is located.
	 * @param g represents the graph of the game.
	 * @param fr represents the given fruit.
	 * @return the edge of the given fruit.
	 */
	public static edge_data edgeOfFruit(DGraph g, Fruit fr) {
		for(node_data n: g.getV()) {	
			for(edge_data e: g.getE(n.getKey())) {
				if(isOnEdge(fr.getPos(), e, fr.getType(),g))
					return e;
			}
		}
		return null;
	}

	/**
	 * Creates a list of all the fruits in the game.
	 * @param game represents the given game.
	 * @return the list of the fruits.
	 */
	public static List<Fruit> createFruitsList(game_service game)
	{
		List<Fruit> fruits = new ArrayList<Fruit>();
		Iterator<String> frItr=game.getFruits().iterator();
		while(frItr.hasNext())
		{
			String fruit=frItr.next();
			fruit=fruit.substring(9,fruit.length()-1);
			Fruit f=createFruit(fruit);
			fruits.add(f);//add to the list of fruit
		}
		//sort the array by value
		for(int i=0;i<fruits.size();i++)
		{
			for(int j=fruits.size()-1;j>i;j--)
			{
				if(fruits.get(j).getValue()>fruits.get(j-1).getValue())
				{
					Fruit f=fruits.get(j);
					fruits.set(j, fruits.get(j-1));
					fruits.set(j-1, f);
				}
			}
		}
		return fruits;
	}
	/**
	 * Creates a fruit from a string (json).
	 * @param fruitStr represents a json file of a fruit.
	 * @return an object of the given fruit.
	 */
	private static Fruit createFruit(String fruitStr) {
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
	 * Creates a list of all the robots in the game.
	 * @param game represents the given game.
	 * @return the list of the robots.
	 */
	public static List<Robot> createRobotsList(game_service game)
	{
		List<Robot> robots=new ArrayList<Robot>();
		List<String> rob=game.getRobots();
		for(String robStr:rob)
		{
			Robot r = createRobot(robStr);
			robots.add(r);
		}
		return robots;
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
	public static  Game createGame(String gameStr) 
	{	
		gameStr=gameStr.substring(14,gameStr.length()-1);
		Gson gson = new Gson();
		try
		{
			Game gameObj=gson.fromJson(gameStr, Game.class);
			return gameObj;
		} 
		catch ( JsonSyntaxException  e) //default value for unreadable file
		{
			throw new RuntimeException("wrong format for Game");
		}
	}
	public static void moveRobotsAuto(game_service game , DGraph g, List<Fruit>fruits) {
		algo= new Graph_Algo(g);
		List<String> log = game.move();
		long t = game.timeToEnd();
		for(int i=0;i<log.size();i++) {// log.size= the amount of robots
			String robot_json = log.get(i);
			Robot robot= createRobot(robot_json);
			int rid = robot.getId();
			int src = robot.getSrc();
			int dest = robot.getDest();
			System.out.println(robot);

			if(dest==-1) {	
				dest = nextNode(src, fruits);// pick the next node
				game.chooseNextEdge(rid, dest);// move the robot
				System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
				System.out.println(robot);
			}
		}
	}

private static int nextNode(int src, List<Fruit> fruits) {
	DGraph g= (DGraph) algo.getG();
	int key=-1;
	edge_data edge=null;
	HashMap<Fruit,edge_data> edgesOfFruits=new HashMap<>();
	HashMap<Integer,Fruit> nodeOfFruits= new HashMap<>();
	List<node_data> path=new ArrayList<node_data>();
	List <Integer> targetNodes= new ArrayList<Integer>();
	for (Fruit fruit : fruits) {
		key=findFruitSrc(fruit, g);
		if(key!=-1)
			nodeOfFruits.put(key, fruit);
		edge=edgeOfFruit(g, fruit);
		edge.setTag(0);// the fruit hasn't visited
		if(edge!=null) edgesOfFruits.put(fruit,edge);

	}
	if(nodeOfFruits.containsKey(src)) {// the robot is on a src node of a fruit edge.
		Fruit f=nodeOfFruits.get(src);
		edge_data e=edgeOfFruit(g,f);
		if(edgesOfFruits.get(f).getTag()==0) {
			if(e.getSrc()==src) {
				targetNodes.add(e.getDest());
				edgesOfFruits.get(f).setTag(1);// the fruit is visited
				return e.getDest();
			}
			else {
				targetNodes.add(e.getSrc());
				edgesOfFruits.get(f).setTag(1);// the fruit is visited
				return e.getSrc();
			}
		}
	}
	else {
		double min=Double.POSITIVE_INFINITY;
		int dest=-1;
		double dist;
		for (Integer node : nodeOfFruits.keySet()) {
			dist=algo.shortestPathDist(src, node);
			if(dist<=min&&!targetNodes.contains(node))
			{
				min=dist;
				dest=node;
			}try {
				path=algo.shortestPath(src, dest);
				targetNodes.add(path.get(1).getKey());
				return path.get(1).getKey();

			}
			catch (Exception e) {
				return path.get(1).getKey();
			}

		}
	}
	return -1;
}

/**
 * a very simple random walk implementation!
 * @param g
 * @param src
 * @return
 */
//	private static int randomWalk(DGraph g, int src) {
//		int ans = -1;
//		Collection<edge_data> ee = g.getE(src);// the edges of the node
//		Iterator<edge_data> itr = ee.iterator();
//		int s = ee.size();// num of edeges
//		int r = (int)(Math.random()*s);
//		int i=0;
//		while(i<r) {itr.next();i++;}
//		ans = itr.next().getDest();
//		return ans;
//	}

/**
 * Checks if a given point is represents a robot.
 * @param game represents the current game.
 * @param g represents the graph of the level.
 * @param x represents the x value of the given point.
 * @param y represents the y value of the given point.
 * @return the robot in that point, if there is no robot in this location- returns null.
 */
public static Robot isARobot(game_service game, DGraph g,double x,double y) {
	List<String>robListStr=game.move();
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
public static void moveRobotsMan(game_service game, DGraph g) {
	//		Point3D p=new Point3D(0,0);
	//		List<String> log = game.move();
	//		
	//		if (log != null) {// there are robots
	//			if (StdDraw.isMousePressed()) {
	//				while (StdDraw.isMousePressed()&&i%2==1)
	//				p = new Point3D(StdDraw.mouseX(), StdDraw.mouseY());
	//				// check that a robot was clicked
	//				System.out.println("p1 "+p);
	//				Robot r=isARobot(game,g,p.x(),p.y());
	//				int id=r.getId();
	//				int src = r.getSrc();
	//				StdDraw.isMousePressed=false;
	////				while (StdDraw.isMousePressed()) {
	//				
	//					p = new Point3D(StdDraw.mouseX(), StdDraw.mouseY());
	//					System.out.println("p2 "+p);
	//					System.out.println(StdDraw.isMousePressed);
	//					
	////				}
	//				int dest=isANode(g,p.x(),p.y());
	//				System.out.println("ddddd" +dest);
	//				if(dest!=-1) {
	//					System.out.println("is a node = true");
	//					dest=isADest(g,dest,src);
	//					System.out.println("ans after dest "+dest);
	//				}
	//				//				int dest=nextNodeMan(g,p);
	//				//				//				if (key != -1) {
	//				//				// get keys of adjacent nodes in a string
	//				//				Collection<edge_data> adjNodes = g.getE(src);
	//				//				Iterator<edge_data> adjItr = adjNodes.iterator();
	//				//				String adj = "";
	//				//				while (adjItr.hasNext()) {
	//				//					adj += adjItr.next().getDest() + ", ";
	//				//				}
	//				//				// cosmetics
	//				//				adj = adj.substring(0, adj.length() - 2);
	//				//				try {
	//				//					JFrame f = new JFrame("Choose next node");
	//				//					int choice = Integer.parseInt(JOptionPane.showInputDialog(f,
	//				//							"Options: " + adj));
	//				//					// check that chosen node is valid
	//				long i = game.chooseNextEdge(id, dest);
	//				if (i != -1)
	//					System.out.println("Robot " + id + " moves  to " + dest);
	//				else
	//					System.out.println("Robot cannot move to " + dest);
	//
	//				//				} catch (Exception exception) {
	//				//					System.out.println(exception);
	//				//					JFrame f = new JFrame();
	//				//					JOptionPane.showMessageDialog(f, "Bad input.",
	//				//							"Alert", JOptionPane.WARNING_MESSAGE);
	//				//				}
	//
	//				//}
	//				long m = game.chooseNextEdge(id, src);
	//				if (m != -1) {
	//					System.out.println("Robot " + id + " moves  to " + src);
	//				}
	//				StdDraw.isMousePressed=true;
	//			}
	//
	//		}
	//	}
	//		
	//		//		Robot r=StdDraw.r;
	//		//		int src=r.getSrc();
	//		//		int dest=r.getDest();
	//		//		long t = game.timeToEnd();
	//		////		List<String> log = game.move();
	//		////		if(log!=null) {
	//		////			long t = game.timeToEnd();
	//		////			for(int i=0;i<log.size();i++) {
	//		////				String robot_json = log.get(i);
	//		////				try {
	//		////					JSONObject line = new JSONObject(robot_json);
	//		////					JSONObject ttt = line.getJSONObject("Robot");
	//		////					int rid = ttt.getInt("id");
	//		////					int src = ttt.getInt("src");
	//		////					int dest = ttt.getInt("dest");
	//		//
	//		//					if(dest==-1) {	
	//		//						dest =StdDraw.currKey;
	//		//						game.chooseNextEdge(src, dest);
	//		//						System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
	//		//						System.out.println(r);
	//		//					}
	//		//				} 
	//		//				catch (JSONException e) {e.printStackTrace();}
	//		//			}
}
public static int isADest(DGraph g,int dest, int src) {
	Collection<edge_data> ee = g.getE(src);
	for (edge_data e: ee) {
		if(e.getDest()==dest)
			return dest;
	}
	return -1;
}
/**
 * Checks if the given points represents a node in the graph.
 * @param g represents the graph of the game.
 * @param x represents the x value of the given point.
 * @param y represents the y value of the given point.
 * @return the key of the node in the given point, if there is no node in that location- returns null.
 */
public static int isANode(DGraph g,double x,double y) {
	for(Iterator<node_data> verIter=g.getV().iterator();verIter.hasNext();) {
		node_data nd=verIter.next();
		if(Math.abs(x-nd.getLocation().x())<= 0.0005
				&&Math.abs(y-nd.getLocation().y())<= 0.0005)
			return nd.getKey();
	}
	return -1;
}
/**
 * Finds the node which we need to get in order to eat the fruit.
 * @param f represents the given fruit.
 * @param g represents the given graph.
 * @return the found node key, returns -1 if the node cannot be found.
 */
private static int findFruitSrc(Fruit f,DGraph g) {
	double type=f.getType();
	for(node_data n: g.getV()) {	
		for(edge_data e: g.getE(n.getKey())) {
			if(isOnEdge(f.getPos(), e, f.getType(),g)) {
				if(type==-1) 
					return Math.max(e.getSrc(), e.getDest());
				return Math.min(e.getSrc(), e.getDest());
			}
		}
	}
	return -1;
}
/**
 * Position the robots in an auto mode next to the fruits according to their values (from high to low).
 * @param g represents the given graph
 * @param fruits represents a list of all the fruit in the game.
 */
public static void autoRobotLocation(DGraph g,List<Fruit>fruits)
{
	Game game=Game_Algo.createGame(MyGameGUI.getGame().toString());
	Collections.sort(fruits,new FruitComperator());// sort the fruits by their values (from high to low).
	int numOfFruits=fruits.size();
	Fruit[] frArr = new Fruit [numOfFruits];
	int i=0;
	for (Fruit fruit : fruits) {
		frArr[i]=fruit;
		i++;
	}
	int numOfRobots=game.getRobots();
	int fruitNum=0;
	for(i=0;i<numOfRobots;i++) {
		fruitNum=i;
		if(fruitNum>=numOfFruits)fruitNum=i%numOfFruits;
		MyGameGUI.getGame().addRobot(findFruitSrc(frArr[fruitNum], g));///FIX TO SRC NODE
		System.out.println(findFruitSrc(frArr[fruitNum], g));
	}
}
//	private static int nextNodeMan(DGraph g, int src) {
//		int ans = -1;
//		Point3D p=new Point3D(0,0);
//		while(StdDraw.isMousePressed()) 
//			p = new Point3D(StdDraw.mouseX(), StdDraw.mouseY());
//		System.out.println("p  "+p);
//		ans=isANode(g,p.x(),p.y());
//		if(ans!=-1) {
//			System.out.println("is a node = true");
//			ans=isADest(g,ans,src);
//			System.out.println("ans after dest "+ans);
//		}
//
//
//		return ans;
//	}
}



