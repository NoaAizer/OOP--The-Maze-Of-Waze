package elements;


import java.util.Comparator;

public class FruitComperator implements Comparator<Fruit> 
/**
 * Sort the fruits by their values
 */
{
	public int compare(Fruit f1, Fruit f2) {
			if(f1.value>(f2.value)) return 1;
		return 2;
		
	} 
} 

