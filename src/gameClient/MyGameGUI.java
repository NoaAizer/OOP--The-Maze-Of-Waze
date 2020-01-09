package gameClient;

import java.awt.Color;
import java.awt.Font;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import dataStructure.*;
import utils.*;

public class MyGameGUI implements Runnable {
	private static DecimalFormat df1 = new DecimalFormat("#.#");
	DGraph g;
	int mc;

	/**
	 * Add a graph to draw
	 * @param _g represents the graph for the drawing.
	 */
	public MyGameGUI() {
		DGraph gr= new DGraph();
		this.init(gr);

	}
	public MyGameGUI(DGraph gg) {
		this.init(gg);
	}
	public void init (DGraph g) {
		this.g=g;
		this.mc=g.getMC();
		Thread t1=new Thread(this);
		t1.start();
		StdDraw.initGraph(g);


	}
	@Override
	public void run() {
		while(true) {
			synchronized(this) {
				if(this.mc!=this.g.getMC()) {
					this.drawGraph();
					this.mc=this.g.getMC();
				}
				else {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
	/**
	 * Initialize the size of the frame according to the x,y ranges.
	 */
	public void initSize() {
		StdDraw.setCanvasSize(1000,800);
		Range rx= rangeX(this.g.getV());
		Range ry= rangeY(this.g.getV());
		double limx=(rx.get_max()-rx.get_min())*0.2;
		double limy=(ry.get_max()-ry.get_min())*0.2;
		StdDraw.setXscale(rx.get_min()-limx, rx.get_max()+limx);
		StdDraw.setYscale(ry.get_min()-limy, ry.get_max()+limy);
	}

	/**
	 * Drawing the graph in the frame.
	 */
	public void drawGraph() {
		initSize();
		for (node_data n : g.getV() ){
			drawNode(this.g,n);
			if (g.getE(n.getKey()) != null) {
				for(Iterator<edge_data> edgeIt=g.getE(n.getKey()).iterator();edgeIt.hasNext();) {
					edge_data e=edgeIt.next();
					drawEdge(this.g,e);
				}
			}
		}
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
	public static void drawNode(DGraph g2,node_data n) {
		StdDraw.setFont(new Font("Calibri", Font.CENTER_BASELINE, 16));
		Range ry= rangeY(g2.getV());
		Point3D src=n.getLocation();
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(Color.BLUE);
		StdDraw.point(src.x(), src.y());
		StdDraw.text(src.x(), src.y()+0.03*ry.get_length(), "" + n.getKey());
	}

	/**
	 * Draws a given edge on the frame.
	 * @param g2 represents the given graph.
	 * @param e represents the edge for drawing.
	 */
	public static void drawEdge(DGraph g2,edge_data e) {
		node_data src=g2.getNode(e.getSrc());
		node_data dest=g2.getNode(e.getDest());
		Point3D s= src.getLocation();
		Point3D d=dest.getLocation();
		StdDraw.setPenColor(Color.GRAY);
		StdDraw.setPenRadius();
		StdDraw.line(s.x(), s.y(), d.x(), d.y());
		double x1=s.x()+(d.x()-s.x())*0.9;
		double y1=s.y()+(d.y()-s.y())*0.9;
		StdDraw.setPenRadius(0.02);	
		StdDraw.setPenColor(Color.yellow);
		StdDraw.point(x1, y1);
		double x0=(s.x()+3*d.x())/4;
		double y0=(s.y()+3*d.y())/4;
		df1.setRoundingMode(RoundingMode.DOWN);
		double w =Double.valueOf(df1.format(e.getWeight()));
		StdDraw.setPenColor(Color.GRAY);
		StdDraw.setPenRadius(0.006);
		StdDraw.text(x0, y0, ""+w);
	}
	/**
	 * Creates a range of the x values of the nodes.
	 * @param collection represents a list with all the nodes in the graph.
	 * @return the x range.
	 */
	public static Range rangeX (Collection<node_data> collection) {
		double min= Double.POSITIVE_INFINITY , max=Double.NEGATIVE_INFINITY;
		for (node_data n: collection) {
			if(n.getLocation().x()<min) min=n.getLocation().x();
			if(n.getLocation().x()>max) max=n.getLocation().x();
		}
		return new Range (min,max);
	}
	/**
	 * Creates a range of the y values of the nodes.
	 * @param collection represents a list with all the nodes in the graph.
	 * @return the y range.
	 */
	public static Range rangeY (Collection<node_data> collection) {
		double min= Double.POSITIVE_INFINITY , max=Double.NEGATIVE_INFINITY;
		for (node_data n: collection)  {
			if(n.getLocation().y()<min) min=n.getLocation().y();
			if(n.getLocation().y()>max) max=n.getLocation().y();
		}
		return new Range (min,max);
	}
	public static void drawRobot(DGraph g,node_data n) {
		Random random = new Random();
		final float hue = random.nextFloat();
		// Saturation between 0.1 and 0.3
		final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
		final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
		Color color = Color.getHSBColor(hue, saturation, luminance);
		Point3D src=n.getLocation();
		StdDraw.setPenRadius(0.03);
		StdDraw.setPenColor(color);
		StdDraw.point(src.x(), src.y());
	}
	public static void drawFruit(DGraph gg,Fruit f) {
		if(f.getType()==1)
			StdDraw.picture(f.getPos().x(), f.getPos().y(), "data/apple.png",0.0007,0.0004);
		else if(f.getType()==-1)
			StdDraw.picture(f.getPos().x(), f.getPos().y(), "data/banana.png",0.0007,0.0004);
	}
	public static void main(String[] args) {

		DGraph g= new DGraph();
		//		OOP_Point3D p1,p2,p3,p4,p5,p6,p7;
		//		node_data n1,n2,n3,n4,n5,n6,n7;

		//		p1=new Point3D(-10,-10,0);
		//		p2=new Point3D(-10,10,0);
		//		p3=new Point3D(40,0,0);
		//		p4=new Point3D(80,10,0);
		//		p5=new Point3D(80,-10,0);
		//		p6=new Point3D(90,30,0);
		//		p7=new Point3D(100,0,0);
		//
		//
		//
		//		n1=new Node(1,p1);
		//		n2=new Node(2,p2);
		//		n3=new Node(3,p3);
		//		n4=new Node(4,p4);
		//		n5=new Node(5,p5);
		//		n6=new Node(6,p6);
		//		n7=new Node(7,p7);
		//
		//		g.addNode(n1);
		//		g.addNode(n2);
		//		g.addNode(n3);
		//		g.addNode(n4);
		//		g.addNode(n5);
		//		g.addNode(n6);
		//		g.addNode(n7);
		//
		//		g.connect(n1.getKey(), n2.getKey(), 4);
		//		g.connect(n2.getKey(), n1.getKey(), 4);
		//		g.connect(n1.getKey(), n3.getKey(), 3);
		//		g.connect(n2.getKey(), n4.getKey(), 5);
		//		g.connect(n3.getKey(), n2.getKey(), 1);
		//		g.connect(n3.getKey(), n5.getKey(), 8);
		//		g.connect(n4.getKey(), n3.getKey(), 11);
		//		g.connect(n4.getKey(), n6.getKey(), 2);
		//		g.connect(n5.getKey(), n4.getKey(), 2);
		//		g.connect(n5.getKey(), n1.getKey(), 7);
		//		g.connect(n5.getKey(), n7.getKey(), 5);
		//		g.connect(n6.getKey(), n7.getKey(), 3);
		//		g.connect(n7.getKey(), n4.getKey(), 10);
		MyGameGUI app = new MyGameGUI(g);
		app.drawGraph();

	}
}
