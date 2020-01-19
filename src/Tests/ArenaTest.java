package Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;

import algorithms.Game_Algo;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.node_data;
import elements.Arena;
import utils.Point3D;

//NOTE: Some of the function are not implemented because they only can be use during running a game with
// MyGameGUI, such as init, getFruitsList, getRobotsList etc. - they use MyGameGUI.arena.getGame() in some of the functions
// and that can only be initialized in a real game during running MyGameGUI.

class ArenaTest {
	String gameJson, gameJson2;
	game_service game, game2;

	@BeforeEach
	void setupRobot() {
		gameJson="{\"GameServer\":{\"fruits\":1,\"moves\":0,\"grade\":0,\"robots\":1,\"graph\":\"data/A0\"}}";
		gameJson2="{\"GameServer\":{\"fruits\":6,\"moves\":5,\"grade\":39,\"robots\":3,\"graph\":\"data/A5\"}}";
		game = Game_Server.getServer(2);
		game2 = Game_Server.getServer(23);
	}

	/**
	 * Tests createArenaFromJson(gameStr)- in Game_Algo.
	 */
	@Test
	void testcreateArenaFromJson() {
		Arena ar= Game_Algo.createArenaFromJson(gameJson);
		Arena ar2= Game_Algo.createArenaFromJson(gameJson2);

		if(ar.getFruits()!=1||ar2.getFruits()!=6)
			fail("Amount of fruits is incorrect");
		if(ar.getMoves()!=0||ar2.getMoves()!=5)
			fail("Amount of moves is incorrect");
		if(ar.getGrade()!=0||ar2.getGrade()!=39)
			fail("The game grade is incorrect");
		if(ar.getRobots()!=1||ar2.getRobots()!=3)
			fail("Amount of robots is incorrect");
		String gr_file="data/A0";
		assertEquals("Checks graph file name", gr_file, ar.getGraph());
	}
	/**
	 * Tests getFruits() function.
	 */
	@Test
	void testgetFruits() {
		Arena ar= Game_Algo.createArenaFromJson(gameJson);
		Arena ar2= Game_Algo.createArenaFromJson(gameJson2);

		if(ar.getFruits()!=1||ar2.getFruits()!=6)
			fail("Amount of fruits is incorrect");
	}
	/**
	 * Tests getFruits() function.
	 */
	@Test
	void testgetMoves() {
		Arena ar= Game_Algo.createArenaFromJson(gameJson);
		Arena ar2= Game_Algo.createArenaFromJson(gameJson2);


		if(ar.getMoves()!=0||ar2.getMoves()!=5)
			fail("Amount of moves is incorrect");
	}
	/**
	 * Tests getGrade() function.
	 */
	@Test
	void testgetGrade() {
		Arena ar= Game_Algo.createArenaFromJson(gameJson);
		Arena ar2= Game_Algo.createArenaFromJson(gameJson2);

		if(ar.getGrade()!=0||ar2.getGrade()!=39)
			fail("The game grade is incorrect");
	}

	/**
	 * Tests getRobots() function.
	 */
	@Test
	void testgetRobots() {
		Arena ar= Game_Algo.createArenaFromJson(gameJson);
		Arena ar2= Game_Algo.createArenaFromJson(gameJson2);

		if(ar.getRobots()!=1||ar2.getRobots()!=3)
			fail("Amount of robots is incorrect");
	}

	/**
	 * Tests getGraph() function.
	 */
	@Test
	void testgetGraph() {
		Arena ar= Game_Algo.createArenaFromJson(gameJson);
		Arena ar2= Game_Algo.createArenaFromJson(gameJson2);

		String gr_file1="data/A0";
		assertEquals("Checks graph file name", gr_file1, ar.getGraph());

		String gr_file2="data/A5";
		assertEquals("Checks graph file name", gr_file2, ar2.getGraph());
	}

	/**
	 * Tests setG() function.
	 */
	@Test
	void testsetG() {
		Arena ar= Game_Algo.createArenaFromJson(gameJson);

		DGraph g =new DGraph();
		node_data n1=new Node(1,new Point3D(-1,-1,1),0,null,0);
		node_data n2=new Node(2,new Point3D(-1,1,4),0,null,0);
		g.addNode(n1);
		g.addNode(n2);
		g.connect(n1.getKey(),n2.getKey(), 1);

		ar.setG(g);
		assertEquals("Check set graph",ar.getG(),g);
	}

}