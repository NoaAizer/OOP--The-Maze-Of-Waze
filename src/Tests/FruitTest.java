package Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algorithms.Game_Algo;
import elements.Fruit;
import utils.Point3D;

class FruitTest {
	String fruitJson, fruitJson2;

	@BeforeEach
	void setupRobot() {
		fruitJson="{\"value\":8.0,\"type\":-1,\"pos\":\"3,2,1\"}";
		fruitJson2="{\"value\":80,\"type\":1,\"pos\":\"-1.1,0,4\"}";;
	}

	/**
	 * Tests createFruit(fruitStr)- in Game_Algo.
	 */
	@Test
	void testcreateFruit() {
		Fruit fr= Game_Algo.createFruit(fruitJson);
		Fruit fr2= Game_Algo.createFruit(fruitJson2);

		if(fr.getType()!=-1||fr2.getType()!=1)
			fail("Fruit type is incorrect");
		if(fr.getValue()!=8||fr2.getValue()!=80)
			fail("Fruit value is incorrect");
		Point3D excepted=new Point3D(3,2,1);
		assertEquals("Check Fruit position",fr.getPos(),excepted);
		Point3D excepted2=new Point3D(-1.1,0,4);
		assertEquals("Check Robot position",fr2.getPos(),excepted2);
	}
	/**
	 * Tests getValue();
	 */
	@Test
	void testgetValue() {
		Fruit fr= Game_Algo.createFruit(fruitJson);
		if(fr.getValue()!=8)
			fail("Fruit value should be 0");
	}
	/**
	 * Tests getType();
	 */
	@Test
	void testgetType() {
		Fruit fr= Game_Algo.createFruit(fruitJson);
		if(fr.getType()!=-1)
			fail("Fruit type should be -1");
	}
	/**
	 * Tests getPos();
	 */
	@Test
	void testgetPos() {
		Fruit fr2= Game_Algo.createFruit(fruitJson2);
		Point3D excepted2=new Point3D(-1.1,0,4);
		assertEquals("Check Fruit position",fr2.getPos(),excepted2);
	}

	/**
	 * Tests setPos(Point3D pos);
	 */
	@Test
	void testsetPos() {
		Fruit fr= Game_Algo.createFruit(fruitJson);
		Fruit fr2= Game_Algo.createFruit(fruitJson2);

		fr.setPos(fr2.getPos());
		assertEquals("Check Fruit position",fr.getPos(),fr2.getPos());
	}

	/**
	 * Tests compareTo(fruit);
	 */
	@Test
	void testCompareTo() {
		Fruit fr= Game_Algo.createFruit(fruitJson);
		Fruit fr2= Game_Algo.createFruit(fruitJson2);
		int val1=fr.compareTo(fr2);
		int val2=fr2.compareTo(fr);
		if(val1!=1)
			fail("Should return 1 - fr is larger ");
		if(val2!=-1)
			fail("Should return -1 - fr2 is smaller ");
	}

}
