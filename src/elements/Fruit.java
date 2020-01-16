package elements;

import utils.Point3D;

public class Fruit implements Comparable<Fruit>{
	public double value;
	public double type;
	private String pos;

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
/**
 * Compare 2 fruits by value.
 */
	@Override
	public int compareTo(Fruit f) {
		Double val= this.value;
		return  -val.compareTo(f.value); 

	}
}