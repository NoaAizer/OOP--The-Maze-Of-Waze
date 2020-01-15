package gameClient;

import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import Server.Game_Server;
import Server.game_service;
import dataStructure.*;
import elements.*;
import utils.*;

public class MyGameGUI implements Runnable {
	//private static DecimalFormat df1 = new DecimalFormat("#.#");
	//	private static Color colorR = Color.BLUE;
	private static game_service game;
	private int scenario_num;
	public DGraph g;
	private List<Fruit> fruits;
	private List<Robot> robots;
	public List<Integer> robotsColor;
	private double minX=Double.POSITIVE_INFINITY;
	private double minY=Double.POSITIVE_INFINITY;
	private double maxX=Double.NEGATIVE_INFINITY;
	private double maxY=Double.NEGATIVE_INFINITY;
	private int mode;
	public static KML_Logger kml;

	/**
	 * Add a graph to draw
	 * @param _g represents the graph for the drawing.
	 */
	public MyGameGUI() {
		init();
		StdDraw.enableDoubleBuffering();
		drawGraph();
		fruits=Game_Algo.createFruitsList(getGame());
		drawFruits();
		StdDraw.show();
		if(mode==1)
			Game_Algo.autoRobotLocation(g,fruits);
		else
			PickARobotPlace();
		robots=Game_Algo.createRobotsList(getGame());
		drawRobots();
		StdDraw.show();
		Thread t=new Thread(this);
		t.start();
	}
	private void init() {
		this.scenario_num=pickScenario(24);
		setGame(Game_Server.getServer(scenario_num));
		this.mode=pickMode();//0=manual , 1=auto
		g=new DGraph();
		g.init(getGame().getGraph());
		StdDraw.initGraph(g);
		initSize();

	}
	public void init (graph g) {
		this.g=(DGraph)g;
	}

	public static int pickScenario(int num) {
		Integer[] options= new Integer [num];
		for (int i = 0; i < options.length; i++) {
			options[i]=i;	
		}
		int scenario_num = (Integer)JOptionPane.showInputDialog(null, "Pick a level to play:", 
				"Pick a level:", JOptionPane.QUESTION_MESSAGE, null, options, null);
		return scenario_num;
	}
	public static int pickMode() {
		String[] options = new String[] {"Manual", "Auto"};
		int ans = JOptionPane.showOptionDialog(null, "Pick a mode to play:", "Game Mode",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, options[0]);
		return ans;
	}

	
	/**
	 * Initialize the size of the frame according to the x,y ranges.
	 */
	private void initSize() {
		StdDraw.setCanvasSize(800,500);
		rangeX(this.g.getV());
		rangeY(this.g.getV());
		double limx=(maxX-minX)*0.2;
		double limy=(maxY-minY)*0.2;
		StdDraw.setXscale(minX-limx, maxX+limx);
		StdDraw.setYscale(minY-limy, maxY+limy);
	}
	public void repaint()
	{
		update();
		fruits=Game_Algo.createFruitsList(getGame());
		drawFruits();
		robots=Game_Algo.createRobotsList(getGame());
		drawRobots();
		StdDraw.show();
	}

	/**
	 * Drawing the graph in the frame.
	 */
	public void drawGraph() {
		for (node_data n : g.getV() ){
			drawNode(this.g,n);
			if (g.getE(n.getKey()) != null) {
				for(Iterator<edge_data> edgeIt=g.getE(n.getKey()).iterator();edgeIt.hasNext();) {
					edge_data e=edgeIt.next();
					drawEdge(this.g,e);
				}
			}
		}
		StdDraw.setPenColor(Color.BLACK);
		StdDraw.setPenRadius(0.020);
		StdDraw.text(minX,maxY+0.001, "TIME TO END: "+getGame().timeToEnd()/1000);;
		Game result=Game_Algo.createGame(getGame().toString());
		StdDraw.text(minX+0.01,maxY+0.001, "LEVEL: "+(scenario_num));
		StdDraw.text(minX+0.02,maxY+0.001, "POINTS: "+result.getGrade());
	}

	/**
	 * Open a new frame with the updated graph.(and close the previous frame). 
	 */
	public void update() {
		StdDraw.clear();
		this.drawGraph();

	}
	/**
	 * Draws a given node on the frame.
	 * @param g2 represents the given graph.
	 * @param n represents the node for drawing.
	 */
	public void drawNode(DGraph g2,node_data n) {
		StdDraw.setFont(new Font("Calibri", Font.CENTER_BASELINE, 16));
		Point3D src=n.getLocation();
		StdDraw.setPenRadius(0.01);
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
		StdDraw.setPenColor(Color.GRAY);
		StdDraw.setPenRadius();
		StdDraw.line(s.x(), s.y(), d.x(), d.y());
		//		double x1=s.x()+(d.x()-s.x())*0.9;
		//		double y1=s.y()+(d.y()-s.y())*0.9;
		//		StdDraw.setPenRadius(0.02);	
		//		StdDraw.setPenColor(Color.yellow);
		//		StdDraw.point(x1, y1);
		//		double x0=(s.x()+3*d.x())/4;
		//		double y0=(s.y()+3*d.y())/4;
		//		df1.setRoundingMode(RoundingMode.DOWN);
		//		double w =Double.valueOf(df1.format(e.getWeight()));
		//		StdDraw.setPenColor(Color.GRAY);
		//		StdDraw.setPenRadius(0.006);
		//		StdDraw.text(x0, y0, ""+w);
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
	//	private static void color() {
	//					Random random = new Random();
	//					final float hue = random.nextFloat();
	//					// Saturation between 0.1 and 0.3
	//					final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
	//					final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
	//					colorR = Color.getHSBColor(hue, saturation, luminance);	
	//	}
	public void drawRobots() {

		for(int i=0;i<robots.size();i++)
		{
			Robot r=robots.get(i);
			Point3D src=r.getPos();
			StdDraw.setPenRadius(0.03);
			StdDraw.setPenColor(Color.MAGENTA);
			StdDraw.point(src.x(), src.y());
		}
	}
	public void PickARobotPlace()
	{
		Game game=Game_Algo.createGame(MyGameGUI.getGame().toString());
		int numOfRobots=game.getRobots();
		int numOfNodes=g.nodeSize();
		Integer[] options= new Integer [numOfNodes];
		int counter=0;
		for(node_data nd: g.getV()) {
			options[counter]=nd.getKey();	
			counter++;
		}
		for(int i=1;i<=numOfRobots;i++) {
			int nodeKey = (Integer)JOptionPane.showInputDialog(null, "Pick a node to place the robot:", 
					"Add Robot:", JOptionPane.QUESTION_MESSAGE, null, options, null);
			MyGameGUI.getGame().addRobot(nodeKey);
			//			Random random = new Random();
			//			final float hue = random.nextFloat();
			//			// Saturation between 0.1 and 0.3
			//			final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
			//			final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
			//			Color color = Color.getHSBColor(hue, saturation, luminance);
			//			node_data n= g.getNode(nodeKey);
			//			Point3D src=n.getLocation();
			//			StdDraw.setPenRadius(0.03);
			//			StdDraw.setPenColor(color.red);
			//			StdDraw.point(src.x(), src.y());
		}
		for(String robotStr: MyGameGUI.getGame().getRobots()) {
			Game_Algo.createRobot(robotStr);

		}

	}


	public void drawFruits() {
		for(Fruit f:fruits) {
			if(f.getType()==1)
				StdDraw.picture(f.getPos().x(), f.getPos().y(), "data/apple.png",0.0007,0.0004);
			else if(f.getType()==-1)
				StdDraw.picture(f.getPos().x(), f.getPos().y(), "data/banana.png",0.0007,0.0004);
		}
		StdDraw.show();

	}
	public static game_service getGame() {
		return game;
	}
	public static void setGame(game_service game) {
		MyGameGUI.game = game;
	}
	public void run() 
	{
		getGame().startGame();
		kml = new KML_Logger(scenario_num);
		
		int index=0;
		while(getGame().isRunning())
		{
			if(mode==1)
				Game_Algo.moveRobotsAuto(getGame(),g,fruits);
			else
				StdDraw.manMode=true;
			try
			{
				Thread.sleep(100);
				if(index%2==0)
					repaint();
				index++;
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		int result=Game_Algo.createGame(getGame().toString()).getGrade();
		kml.kmlEnd();
		JOptionPane.showMessageDialog(null, "Game Over! \nYour grade: "+result,"Finish", JOptionPane.CLOSED_OPTION);
	}
	
	
	public static void main(String[] args) {
	}

}
