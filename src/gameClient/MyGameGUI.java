package gameClient;

import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import Server.Game_Server;
import Server.game_service;
import algorithms.Game_Algo;
import dataStructure.*;
import elements.*;
import utils.*;

public class MyGameGUI implements Runnable {


	private int scenario_num;
	public static Arena arena= new Arena();
	private double minX=Double.POSITIVE_INFINITY;
	private double minY=Double.POSITIVE_INFINITY;
	private double maxX=Double.NEGATIVE_INFINITY;
	private double maxY=Double.NEGATIVE_INFINITY;
	private int mode;
	public int id;
	public static KML_Logger kml;


	/**
	 * Starts a new game.
	 */
	public MyGameGUI() {
		init();
		if(this.scenario_num==-1||this.mode==-1)return;
		StdDraw.enableDoubleBuffering();
		drawGraph();
		drawFruits();
		StdDraw.show();
		if(mode==1)
			Game_Algo.autoRobotLocation();
		else
			PickARobotPlace();
		drawRobots();
		StdDraw.show();
		Thread t=new Thread(this);
		t.start();
	}
	/**
	 * Initialize an auto game with a given scenario number
	 * @param sc_num represents the given scenario number.
	 */
	public MyGameGUI(int sc_num) {
		this.scenario_num=sc_num;
		game_service game=Game_Server.getServer(scenario_num);
		this.mode=1;
		arena=Game_Algo.createArenaFromJson(game.toString());
		arena.init(game);
		StdDraw.initGraph(arena.getG());
		initSize();
		StdDraw.enableDoubleBuffering();
		drawGraph();
		drawFruits();
		StdDraw.show();
		if(mode==1)
			Game_Algo.autoRobotLocation();
		else
			PickARobotPlace();
		drawRobots();
		StdDraw.show();
		Thread t=new Thread(this);
		t.start();
	}
	/**
	 * Initializes the level ,the mode and the game and draws the graph.
	 */
	private void init() {
		this.id=Integer.parseInt(JOptionPane.showInputDialog("Enter your ID:"));
		SimpleDB.getDetails(this.id);
		this.scenario_num=pickScenario(24);
		if(this.scenario_num==-1)return;
		game_service game=Game_Server.getServer(scenario_num);
		this.mode=pickMode();//0=manual , 1=auto
		if(this.mode==-1)return;
		arena=Game_Algo.createArenaFromJson(game.toString());
		arena.init(game);
		StdDraw.initGraph(arena.getG());
		initSize();

	}
	/**
	 * Initialize the size of the frame according to the x,y ranges.
	 */
	private void initSize() {
		StdDraw.setCanvasSize(1000,700);
		rangeX(arena.getG().getV());
		rangeY(arena.getG().getV());
		double limx=(maxX-minX)*0.15;
		double limy=(maxY-minY)*0.15;
		StdDraw.setXscale(minX-limx, maxX+limx);
		StdDraw.setYscale(minY-limy, maxY+limy);
	}
	/**
	 * Creates a range of the x values of the nodes.
	 * @param collection represents a list with all the nodes in the graph.
	 * 
	 */
	private void rangeX (Collection<node_data> collection) {
		for (node_data n: collection) {
			if(n.getLocation().x()<minX) minX=n.getLocation().x();
			if(n.getLocation().x()>maxX) maxX=n.getLocation().x();
		}
	}
	/**
	 * Creates a range of the y values of the nodes.
	 * @param collection represents a list with all the nodes in the graph.
	 */
	private void rangeY (Collection<node_data> collection) {
		for (node_data n: collection)  {
			if(n.getLocation().y()<minY) minY=n.getLocation().y();
			if(n.getLocation().y()>maxY) maxY=n.getLocation().y();
		}

	}
	/**
	 * Updates the graph of the game.
	 * @param g  represents the updated graph.
	 */
	public void init (graph g) {
		arena.setG((DGraph)g);
	}
	/**
	 * Ask the user to pick a level to play.
	 * @param num represents the amount of levels
	 * @return the chosen level.
	 */
	public static int pickScenario(int num) {
		Integer[] options= new Integer [num];
		for (int i = 0; i < options.length; i++) {
			options[i]=i;	
		}

		try {
			int scenario_num = (Integer)JOptionPane.showInputDialog(null, "Pick a level to play:", 
					"Pick a level:", JOptionPane.QUESTION_MESSAGE, null, options, null);
			return scenario_num;
		}
		catch(Exception e) {e.getStackTrace();};
		return -1;
	}
	/**
	 * Asks the user to pick the mode he wants to play. (Manual or Auto)
	 * @return 0- manual 1-Auto according to the user choice.
	 */
	public static int pickMode() {
		String[] options = new String[] {"Manual", "Auto"};
		try {
			int ans = JOptionPane.showOptionDialog(null, "Pick a mode to play:", "Game Mode",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
					null, options, options[0]);
			return ans;
		}catch(Exception e) {e.getStackTrace();};
		return -1;
	}

	/**
	 * Asks the user to pick a node to put the robot on. (In manual mode)
	 */

	public void PickARobotPlace()
	{
		int numOfRobots=arena.getRobots();
		int numOfNodes=arena.getG().nodeSize();
		Integer[] options= new Integer [numOfNodes];
		int counter=0;
		for(node_data nd: arena.getG().getV()) {
			options[counter]=nd.getKey();	
			counter++;
		}
		for(int i=1;i<=numOfRobots;i++) {
			try {
				int nodeKey = (Integer)JOptionPane.showInputDialog(null, "Pick a node to place the robot:", 
						"Add Robot:", JOptionPane.QUESTION_MESSAGE, null, options, null);
				arena.getGame().addRobot(nodeKey);
				kml.addPlaceMark("robot", arena.getG().getNode(nodeKey).getLocation());
			}catch(Exception e) {};

		}
		arena.setRobotsList(Game_Algo.createRobotsList());
	}
	/**
	 * Updates the frame painting.
	 */
	public void repaint()
	{
		StdDraw.clear();
		this.drawGraph();
		arena.setFruitsList(Game_Algo.createFruitsList());
		drawFruits();
		arena.setRobotsList(Game_Algo.createRobotsList());
		drawRobots();
		StdDraw.show();
	}

	/**
	 * Drawing the graph in the frame.
	 */
	public void drawGraph() {
		for (node_data n : arena.getG().getV() ){
			drawNode(arena.getG(),n);
			if (arena.getG().getE(n.getKey()) != null) {
				for(Iterator<edge_data> edgeIt=arena.getG().getE(n.getKey()).iterator();edgeIt.hasNext();) {
					edge_data e=edgeIt.next();
					drawEdge(arena.getG(),e);
				}
			}
		}
		StdDraw.setPenColor(Color.BLACK);
		StdDraw.setPenRadius(0.020);
		StdDraw.text(minX,maxY+0.0007, "TIME TO END: "+arena.getGame().timeToEnd()/1000);;
		StdDraw.text(minX+0.01,maxY+0.0007, "LEVEL: "+(scenario_num));
		StdDraw.text(minX+0.02,maxY+0.0007, "POINTS: "+Game_Algo.updateGrade());
	}

	/**
	 * Draws a given node on the frame.
	 * @param g2 represents the given graph.
	 * @param n represents the node for drawing.
	 */
	public void drawNode(DGraph g2,node_data n) {
		StdDraw.setFont(new Font("Calibri", Font.CENTER_BASELINE, 16));
		Point3D src=n.getLocation();
		StdDraw.setPenRadius(0.015);
		StdDraw.setPenColor(Color.BLUE);
		StdDraw.point(src.x(), src.y());
		StdDraw.text(src.x(), src.y()+0.03*(maxY-minY), "" + n.getKey());
	}

	/**
	 * Draws a given edge on the frame.
	 * @param g2 represents the given graph.
	 * @param e represents the edge for drawing.
	 */
	public void drawEdge(DGraph g2,edge_data e) {
		node_data src=g2.getNode(e.getSrc());
		node_data dest=g2.getNode(e.getDest());
		Point3D s= src.getLocation();
		Point3D d=dest.getLocation();
		StdDraw.setPenColor(Color.BLACK);
		StdDraw.setPenRadius(0.002);
		StdDraw.line(s.x(), s.y(), d.x(), d.y());
	}
	/**
	 * Draws all the robots on the frame.
	 */
	public void drawRobots() {
		for(int i=0;i<arena.getRobotsList().size();i++)
		{
			Robot r=arena.getRobotsList().get(i);
			Point3D src=r.getPos();
			StdDraw.picture(src.x(), src.y(), "data/robot.png",0.0015,0.0009);
		}
	}

	/**
	 * Draws all the fruits on the frame.
	 */
	public void drawFruits() {
		for(elements.Fruit f:arena.getFruitsList()) {
			if(f.getType()==1)
				StdDraw.picture(f.getPos().x(), f.getPos().y(), "data/apple.png",0.0007,0.0004);
			else if(f.getType()==-1)
				StdDraw.picture(f.getPos().x(), f.getPos().y(), "data/banana.png",0.0007,0.0004);
		}
		StdDraw.show();

	}
	public int getScnum()
	{
		return this.scenario_num;
	}
	public void run() 
	{
		arena.getGame().startGame();
		kml = new KML_Logger(scenario_num,arena.getGame().timeToEnd());
		int index=0;
		while(arena.getGame().isRunning())
		{
			for (node_data node : arena.getG().getV()) {
				kml.addPlaceMark("node", node.getLocation());
				for(edge_data edge: arena.getG().getE(node.getKey()))
					kml.addEdge(node.getLocation(), arena.getG().getNode(edge.getDest()).getLocation());
			}
			if(mode==1)
				Game_Algo.moveRobotsAuto(arena.getGame(),arena.getG(),arena.getFruitsList());
			else
				StdDraw.manMode=true;

			try
			{
				List<String> stat = arena.getGame().getRobots();
				for(int i=0;i<stat.size();i++) 
					System.out.println(index+") "+stat.get(i));
				Thread.sleep(105);
				if(index%2==0)
					repaint();


				index++;
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		kml.kmlEnd();
		String res = arena.getGame().toString();
		//String remark = "This string should be a KML file!!";
		arena.getGame().sendKML(kml.toString()); // Should be your KML (will not work on case -1).
		System.out.println(res);
		JOptionPane.showMessageDialog(null, "Game Over! \nYou earned "+Game_Algo.updateGrade()+ " points"
				,"Finish", JOptionPane.CLOSED_OPTION);
	}
	public static void main(String[] args) {
	}

}
