package Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algorithms.Game_Algo;
import elements.Robot;
import utils.Point3D;

class RobotTest {
	String robotJson, robotJson2;

	@BeforeEach
	void setupRobot() {
		robotJson="{\"Robot\":{\"id\":0,\"value\":0.0,\"src\":9,\"dest\":-1,\"speed\":1.0,\"pos\":\"1,2,3\"}}";
		robotJson2="{\"Robot\":{\"id\":-20,\"value\":11,\"src\":-10,\"dest\":-100,\"speed\":15,\"pos\":\"-1.1,2.2,-3.3\"}}";
	}
	
	/**
	 * Tests createRobot(robotStr)- in Game_Algo.
	 */
	@Test
	void testcreateRobot() {
		Robot r= Game_Algo.createRobot(robotJson);
		Robot r2= Game_Algo.createRobot(robotJson2);
		assertEquals(r.getId(), 0);
		assertEquals(r2.getId(), -20);
		if(r.getValue()!=0||r2.getValue()!=11)
			fail("Robot value is incorrect");
		if(r.getSrc()!=9||r2.getSrc()!=-10)
			fail("Robot source node is incorrect");
		if(r.getDest()!=-1||r2.getDest()!=-100)
			fail("Robot destination is incorrect");
		if(r.getSpeed()!=1||r2.getSpeed()!=15)
			fail("Robot speed is incorrect");
		Point3D excepted=new Point3D(1,2,3);
		assertEquals("Check Robot position",r.getPos(),excepted);
		Point3D excepted2=new Point3D(-1.1,2.2,-3.3);
		assertEquals("Check Robot position",r2.getPos(),excepted2);
	}
	/**
	 * Tests getId();
	 */
	@Test
	void testgetId() {
		Robot r= Game_Algo.createRobot(robotJson);
		assertEquals(r.getId(), 0);
	}
	/**
	 * Tests getValue();
	 */
	@Test
	void testgetValue() {
		Robot r= Game_Algo.createRobot(robotJson);
		if(r.getValue()!=0)
			fail("Robot value should be 0");
	}
	/**
	 * Tests getSrc();
	 */
	@Test
	void testgetSrc() {
		Robot r= Game_Algo.createRobot(robotJson);
		if(r.getSrc()!=9)
			fail("Robot source node should be 9");
	}
	/**
	 * Tests getDest();
	 */
	@Test
	void testgetDest() {
		Robot r= Game_Algo.createRobot(robotJson);
		if(r.getDest()!=-1)
			fail("Robot destination node be -1");
	}
	/**
	 * Tests getSpeed();
	 */
	@Test
	void testgetSpeed() {
		Robot r= Game_Algo.createRobot(robotJson);
		if(r.getSpeed()!=1)
			fail("Robot speed should be 9");
	}	
	/**
	 * Tests getPos();
	 */
	@Test
	void testgetPos() {
		Robot r= Game_Algo.createRobot(robotJson);
		Point3D excepted=new Point3D(1,2,3);
		assertEquals("Check Robot position",r.getPos(),excepted);
	}
	/**
	 * Tests setSrc(int src);
	 */
	@Test
	void testsetSrc() {
		Robot r= Game_Algo.createRobot(robotJson);
		Robot r2= Game_Algo.createRobot(robotJson2);
		r.setSrc(r2.getSrc());
		assertEquals("Check Robot source node",r.getSrc(),r2.getSrc());
	}
	/**
	 * Tests setDest(int dest);
	 */
	@Test
	void testsetDest() {
		Robot r= Game_Algo.createRobot(robotJson);
		Robot r2= Game_Algo.createRobot(robotJson2);
		r.setDest(r2.getDest());
		assertEquals("Check Robot destination node ",r.getDest(),r2.getDest());
	}
	/**
	 * Tests setPos(Point3D pos);
	 */
	@Test
	void testsetPos() {
		Robot r= Game_Algo.createRobot(robotJson);
		Robot r2= Game_Algo.createRobot(robotJson2);
		r.setPos(r2.getPos());
		assertEquals("Check Robot position",r.getPos(),r2.getPos());
	}

}
