package elements;



import utils.Point3D;

public class Fruit {
	public double value;
	public double type;
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
