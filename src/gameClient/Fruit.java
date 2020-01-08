package gameClient;

import oop_dataStructure.oop_edge_data;
import oop_utils.OOP_Point3D;

public class Fruit {
private double value;
private double type;
private OOP_Point3D pos;
private oop_edge_data e;

public Fruit(String s) {
	this.value = Double.valueOf(s.substring(s.indexOf("value")+7,s.indexOf(","+'"'+"type")));
	this.type = Double.valueOf(s.substring(s.indexOf("type")+6,s.indexOf(","+'"'+"pos")));
	this.pos = new OOP_Point3D(s.substring(s.indexOf("pos")+6,s.length()-3));
	//this.e = e;
}
public Fruit(double value, double type, OOP_Point3D pos, oop_edge_data e) {
	this.value = value;
	this.type = type;
	this.pos = pos;
	this.e = e;
}
public double getValue() {
	return value;
}
public double getType() {
	return type;
}
public OOP_Point3D getPos() {
	return pos;
}
public oop_edge_data getE() {
	return e;
}
@Override
	public String toString() {
return "value "+value+" type "+type+ " pos "+pos;
	}
}
