package Tests;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import dataStructure.*;
import elements.Arena;
import elements.Fruit;
import elements.Robot;
import gameClient.MyGameGUI;
import algorithms.Game_Algo;
import utils.Point3D;


public class Game_AlgoTest {

	static MyGameGUI mg= new MyGameGUI(3);
	static game_service game=Game_Server.getServer(MyGameGUI.scenario_num);
	static Arena arena=Game_Algo.createArenaFromJson(game.toString());



	@BeforeEach
	void setUp() throws Exception 
	{
		arena.init(game);
	}
	
	
	/**
	 * Test isANode(x,y) function
	 */
	@Test
	public void testIsANode() {
		Point3D p= arena.getG().getNode(1).getLocation();
		if(Game_Algo.isANode(p.x(), p.y())!=1)
			fail("Should return 1");
		if(Game_Algo.isANode(0, 0)!=-1)
			fail("Should return -1");
	}
	
	
	/**
	 * Test isARobot(x,y) function
	 */
	@Test
	public void testIsARobot() {
		Point3D p= arena.getRobotsList().get(0).getPos();
		assertEquals(Game_Algo.isARobot(p.x(), p.y()).getId(),arena.getRobotsList().get(0).getId());
		assertNull(Game_Algo.isARobot(0, 0));
	}

	/**
	 * Test findFruitSrc(fruit) function
	 */
	@Test
	public void testFindFruitSrc() {
		ArrayList<Integer> listSrc =new ArrayList<Integer>();
		listSrc.add(4);
		listSrc.add(3);
		listSrc.add(9);
		for(Fruit f:arena.getFruitsList())
			if(!listSrc.contains(Game_Algo.findFruitSrc(f)))
				fail("find fruit src failed");	
	}
	/**
	 * Test isOnEdge(point,edge,type) function
	 */
	@Test
	public void testIsOnEdge() {
		if(!Game_Algo.isOnEdge(arena.getFruitsList().get(2).getPos(), new Edge(3,2,1.0980094622804095),-1))
			fail("is on edge failed");
		if(Game_Algo.isOnEdge(arena.getFruitsList().get(2).getPos(), new Edge(3,4,1.4301580756736283),-1))
			fail("is on edge failed");
	}

	/**
	 * Test autoRobotLocation() function
	 */
	@Test
	public void testAutoRobotLocation() {
		Game_Algo.autoRobotLocation();
		List<Integer> srcList= new ArrayList<>();
		srcList.add(9);
		srcList.add(4);
		srcList.add(3);
		for(Robot r:arena.getRobotsList())
			if(!srcList.contains(r.getSrc()))
				fail("auto robot location failed");			
	}

}