package dataStructure;

import java.io.Serializable;

public class Edge implements edge_data, Serializable{

	private int src;
	private int dest;
	private double weight;
	private String info;
	private int tag;


	public Edge(int src, int dest, double weight, String info, int tag) {
		this.src = src;
		this.dest = dest;
		this.weight = weight;
		this.info = info;
		this.tag = tag;
	}
	public Edge(int src, int dest, double weight) {
		this.src = src;
		this.dest = dest;
		this.weight = weight;
		this.info = "";
		this.tag = 0;
	}
	public Edge(edge_data ed) {
		this.src=ed.getSrc();
		this.dest=ed.getDest();
		this.weight=ed.getWeight();
		this.info=ed.getInfo();
		this.tag=ed.getTag();
	}

	@Override
	public int getSrc() {
		return src;

	}

	@Override
	public int getDest() {
		return dest;

	}

	@Override
	public double getWeight() {
		return weight;
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
	public boolean equals(Object edge)
	{
		if(edge instanceof Edge)
		{
			if(((Edge) edge).getSrc()==this.src &&((Edge) edge).getDest()==this.dest && 
					((Edge) edge).getWeight()==this.weight && ((Edge) edge).getInfo()==this.info && 
					((Edge) edge).getTag()==this.getTag())
				return true;
			return false;
		}
		else
			return false;
	}
	public String toString()
	{
		String edge= "src: "+this.getSrc()+" dest: "+this.getDest()+" weight: "+this.getWeight();
		return edge;
	}
}