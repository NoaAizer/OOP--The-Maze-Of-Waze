package elements;

import utils.Point3D;

public class Robot {

	private int id;
	private double value;
	private int src;
	private int dest;
	private double speed;
	private String pos;

	/**
	 * Id getter.
	 * @return the id number of the robot.
	 */
	public int getId() 
	{
		return id;
	}
	/**
	 * Value getter.
	 * @return the value of the robot.
	 */
	public double getValue() 
	{
		return value;
	}
	/**
	 * Src getter.
	 * @return the node on which the robot is located.
	 */
	public int getSrc() 
	{
		return src;
	}
	/**
	 * Dest getter.
	 * @return the next node to which the robot goes.
	 */
	public int getDest() 
	{
		return dest;
	}
	/**
	 * Speed getter.
	 * @return the speed of the robot.
	 */
	public double getSpeed() 
	{
		return speed;
	}
/**
 * Position getter.
 * @return the position of the robot.
 */
	public Point3D getPos() 
	{
		return new Point3D(pos);
	}
/**
 * Updates the node of the robot.
 * @param src the new robot node.
 */
	public void setSrc(int src) 
	{
		this.src=src;
	}
	/**
	 * Updates the destination of the robot.
	 * @param dest the new destination node.
	 */
	public void setDest(int dest) 
	{
		this.dest=dest;
	}
	/**
	 * 	Updates the robot location.
	 * @param p the new robot location.
	 */
	public void setPos(Point3D p) 
	{
		this.pos=""+p.x()+","+p.y()+","+p.z();

	}
	
	public String toString() 
	{
		return "id: " +id+ " value: "+value+" speed: "+speed +" pos: "+pos+ " src: "+src+" dest: "+dest;

	}
}
