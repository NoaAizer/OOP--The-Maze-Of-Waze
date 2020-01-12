package elements;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;

public class Fruit {
	final private double EPSILON= 0.000001;
	private double value;
	private double type;
	private String pos;

	public Fruit(double value,int type,String str)
	{
		this.value=value;
		this.type=type;
		this.pos=str;
	}

	public double getValue() {
		return value;
	}
	public double getType() {
		return type;
	}
	public Point3D getPos() {
		Point3D p=new Point3D(pos);
		return p;
	}
	public void setPos(Point3D p) 
	{
		this.pos=""+p.x()+","+p.y()+","+p.z();
	}
	public edge_data edgeOfFruit(DGraph g) {
		Point3D fruitPos=this.getPos();
		for(node_data n: g.getV()) {	
			Point3D srcPos=n.getLocation();
			for(edge_data e: g.getE(n.getKey())) {
				Point3D destPos=g.getNode(e.getDest()).getLocation();
				if(type==-1&&n.getKey()>e.getDest()||type==1&&n.getKey()<e.getDest())
				if(Math.sqrt(Math.pow(fruitPos.x()-srcPos.x(), 2)+Math.pow(fruitPos.y()-srcPos.y(), 2))+
						Math.sqrt(Math.pow(fruitPos.x()-destPos.x(), 2)+Math.pow(fruitPos.y()-destPos.y(), 2))-
						Math.sqrt(Math.pow(srcPos.x()-destPos.x(), 2)+Math.pow(srcPos.y()-destPos.y(), 2))<=EPSILON )
					return e;
			}
		}
		return null;
	}
	@Override
	public String toString() {
		return "value "+value+" type "+type+ " pos "+pos;
	}
}
