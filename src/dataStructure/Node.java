package dataStructure;

import java.io.Serializable;

import utils.Point3D;

public class Node implements node_data, Serializable{

	private int key;
	private Point3D location;
	private double weight;
	private String info;
	private int tag;

	public Node(int key, Point3D location, double weight, String info, int tag) {
		this.key = key;
		this.location = location;
		this.weight = weight;
		this.info = info;
		this.tag = tag;
	}
	public Node(int key,Point3D location) {
		this.key=key;
		this.location=location;
		this.weight=Double.POSITIVE_INFINITY;
		this.info=" ";
		this.tag=0;

	}
	@Override
	public int getKey() {
		return key;
	}

	@Override
	public Point3D getLocation() {
		return location;
	}

	@Override
	public void setLocation(Point3D p) {
		this.location=p;
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public void setWeight(double w) {
		this.weight=w;

	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public void setInfo(String s) {
		this.info=s;
	}

	@Override
	public int getTag() {
		return tag;
	}

	@Override
	public void setTag(int t) {
		this.tag=t;
	}
	public String toString()
	{
		String node= "key: "+this.getKey()+" location: "+this.getLocation()+" weight: "+this.getWeight();
		return node;
	}

}