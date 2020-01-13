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
	@Override
	public String toString() {
		return "value "+value+" type "+type+ " pos "+pos;
	}
}
