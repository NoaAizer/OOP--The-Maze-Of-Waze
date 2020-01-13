package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import elements.Fruit;
import elements.Game;
import elements.Robot;
import utils.Point3D;

public class Game_Algo {

	public static final double EPS1=0.000001;

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

	public static boolean isOnEdge(Point3D p, edge_data e, int type, graph g) {
		int src=g.getNode(e.getSrc()).getKey();
		int dest=g.getNode(e.getDest()).getKey();
		if(type<0 && dest>src) return false;
		if(type>0 && src>dest) return false;
		return isOnEdge(p, src, dest, g);
	}

	public static void updateEdge(Fruit fr, graph g) {
		Iterator<node_data> itr = g.getV().iterator();
	}
	//	public edge_data edgeOfFruit(DGraph g) {
	//		Point3D fruitPos=this.getPos();
	//		for(node_data n: g.getV()) {	
	//			Point3D srcPos=n.getLocation();
	//			for(edge_data e: g.getE(n.getKey())) {
	//				Point3D destPos=g.getNode(e.getDest()).getLocation();
	//				if(type==-1&&n.getKey()>e.getDest()||type==1&&n.getKey()<e.getDest())
	//				if(Math.sqrt(Math.pow(fruitPos.x()-srcPos.x(), 2)+Math.pow(fruitPos.y()-srcPos.y(), 2))+
	//						Math.sqrt(Math.pow(fruitPos.x()-destPos.x(), 2)+Math.pow(fruitPos.y()-destPos.y(), 2))-
	//						Math.sqrt(Math.pow(srcPos.x()-destPos.x(), 2)+Math.pow(srcPos.y()-destPos.y(), 2))<=EPSILON )
	//					return e;
	//			}
	//		}
	//		return null;
	//	}
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
	public static List<Robot> createRobotsList(game_service game)
	{
		List<Robot> robots=new ArrayList<Robot>();
		List<String> rob=game.getRobots();
		for(String robStr:rob)
		{
			Robot r = createRobot(robStr);
			System.out.println(r);
			robots.add(r);
		}
		return robots;
	}
	
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
	public static void moveRobots(game_service game , DGraph g) {
		List<String> log = game.move();
		if(log!=null) {
			long t = game.timeToEnd();
			for(int i=0;i<log.size();i++) {
				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");

					if(dest==-1) {	
						dest = nextNode(g, src);
						game.chooseNextEdge(rid, dest);
						System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
						System.out.println(ttt);
					}
				} 
				catch (JSONException e) {e.printStackTrace();}
			}
		}
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(DGraph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}

}
