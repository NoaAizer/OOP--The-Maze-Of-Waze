package elements;

import utils.Point3D;

public class Robot {

	private int id;
	private double value;
	private int src;
	private int dest;
	private double speed;
	private String pos;
	public Robot(int Id,int Value,int Src,int Dest,double Speed,String str)
	{
		this.id=Id;
		this.value=Value;
		this.src=Src;
		this.dest=Dest;
		this.speed=Speed;
		this.pos=str;
	}
	public int getId() 
	{
		return id;
	}
	public double getValue() 
	{
		return value;
	}
	public int getSrc() 
	{
		return src;
	}
	public int getDest() 
	{
		return dest;
	}
	public double getSpeed() 
	{
		return speed;
	}
	public Point3D getPos() 
	{
		Point3D p=new Point3D(pos);
		return p;
	}

	public void setSrc(int src) 
	{
		this.src=src;
	}
	public void setDest(int dest) 
	{
		this.dest=dest;
	}
	public void setPos(Point3D p) 
	{
		this.pos=""+p.x()+","+p.y()+","+p.z();

	}
}
