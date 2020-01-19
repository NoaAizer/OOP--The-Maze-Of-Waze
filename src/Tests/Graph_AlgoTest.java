package Tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataStructure.*;
import algorithms.Graph_Algo;
import utils.Point3D;

public class Graph_AlgoTest {

	graph g=new DGraph();
	Graph_Algo ga=new Graph_Algo();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{

	}
	@BeforeEach
	void setupGraphs() {
		ga.init(g);

		Point3D p1,p2,p3,p4,p5,p6,p7;
		node_data n1,n2,n3,n4,n5,n6,n7;


		p1=new Point3D(-10,-10,0);
		p2=new Point3D(-10,10,0);
		p3=new Point3D(40,0,0);
		p4=new Point3D(80,10,0);
		p5=new Point3D(80,-10,0);
		p6=new Point3D(90,30,0);
		p7=new Point3D(100,0,0);



		n1=new Node(1,p1,0,null,0);
		n2=new Node(2,p2,0,null,0);
		n3=new Node(3,p3,0,null,0);
		n4=new Node(4,p4,0,null,0);
		n5=new Node(5,p5,0,null,0);
		n6=new Node(6,p6,0,null,0);
		n7=new Node(7,p7,0,null,0);

		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		g.addNode(n6);
		g.addNode(n7);

		g.connect(n1.getKey(), n2.getKey(), 4);
		g.connect(n2.getKey(), n1.getKey(), 4);
		g.connect(n1.getKey(), n3.getKey(), 3);
		g.connect(n2.getKey(), n4.getKey(), 5);
		g.connect(n3.getKey(), n2.getKey(), 1);
		g.connect(n3.getKey(), n5.getKey(), 8);
		g.connect(n4.getKey(), n3.getKey(), 11);
		g.connect(n4.getKey(), n6.getKey(), 2);
		g.connect(n5.getKey(), n4.getKey(), 2);
		g.connect(n5.getKey(), n1.getKey(), 7);
		g.connect(n5.getKey(), n7.getKey(), 5);
		g.connect(n6.getKey(), n7.getKey(), 3);
		g.connect(n7.getKey(), n4.getKey(), 10);
	}
	/**
	 * Test init(graph) function
	 */
	@Test
	public void testInitGraph() {
		ga.init(g);

	}

	/**
	 * Test init(file_name) function
	 */
	@Test
	public void testInitFromFile()
	{
		try
		{
			ga.save("TestInit");
			Graph_Algo ga2 = new Graph_Algo();
			ga2.init("TestInit");
			assertTrue(ga.getG().getV().size() == ga2.getG().getV().size());
			assertTrue(ga.getG().getE(1).size() == ga2.getG().getE(1).size());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Test save(file_name) function
	 */
	@Test
	public void testSaveToFile() {
		try
		{
			ga.save("TestSave");
			Graph_Algo ga2 = new Graph_Algo();
			ga2.init("TestSave");
			assertTrue(ga.getG().getV().size() == ga2.getG().getV().size());
			assertTrue(ga.getG().getE(1).size() == ga2.getG().getE(1).size());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Test isConnected() function
	 */
	@Test
	public void testIsConnected() 
	{
		assertTrue("Error: faild to return true",ga.isConnected());
		edge_data e1=g.removeEdge(5,1);
		edge_data e2=g.removeEdge(2,1);
		assertFalse("Error: faild to return false",ga.isConnected());
		g.connect(e1.getSrc(),e1.getDest(),e1.getWeight());
		g.connect(e2.getSrc(),e2.getDest(),e2.getWeight());
		g.removeEdge(4, 6);
		assertFalse("Error: faild to return false",ga.isConnected());
	}

	/**
	 * Test shortestPathDist(src,dest) function
	 */
	@Test
	public void testShortestPathDist()
	{
		if(11!=ga.shortestPathDist(1, 6))
			fail("Faild! The shortest distance is 11");
		edge_data e1=g.removeEdge(2,4);
		if(15!=ga.shortestPathDist(1, 6))
			fail("Faild! The shortest distance now is 15");
		edge_data e2=g.removeEdge(4, 6);
		if(Double.POSITIVE_INFINITY!=ga.shortestPathDist(1, 6))
			fail("Faild! There is no path.");
		g.connect(e1.getSrc(),e1.getDest(),e1.getWeight());
		g.connect(e2.getSrc(),e2.getDest(),e2.getWeight());
		if(10!=ga.shortestPathDist(2, 7))
			fail("Faild! The shortest distance is 10");
		g.connect(2, 6, 1);
		if(4!=ga.shortestPathDist(2, 7))
			fail("Faild! The shortest distance now is 4");
	}
	/**
	 * Test shortestPath(src,dest) function
	 */
	@Test
	public void testShortestPath()
	{
		List <node_data> ans=ga.shortestPath(1,6);
		String ans_keys="";
		for (node_data n : ans)
			ans_keys+=n.getKey();
		if(!ans_keys.equals("1246")&&!ans_keys.equals("13246"))
			fail("There is shorther path");
		edge_data e1=g.removeEdge(2,4);
		ans=ga.shortestPath(1,6);
		ans_keys="";
		for (node_data n : ans)
			ans_keys+=n.getKey();
		assertEquals("There is shorther path",ans_keys,"13546");
		edge_data e2=g.removeEdge(4, 6);
		assertNull("There is no path",ga.shortestPath(1,6));
		g.connect(e1.getSrc(),e1.getDest(),e1.getWeight());
		g.connect(e2.getSrc(),e2.getDest(),e2.getWeight());
		ans=ga.shortestPath(2,7);
		ans_keys="";
		for (node_data n : ans)
			ans_keys+=n.getKey();
		assertEquals("There is shorther path",ans_keys,"2467");
		g.connect(2, 6, 1);
		ans=ga.shortestPath(2,7);
		ans_keys="";
		for (node_data n : ans)
			ans_keys+=n.getKey();
		assertEquals("There is shorther path",ans_keys,"267");
	}

	/**
	 * Test TSP(list nodes) function
	 */
	@Test
	public void testTSP() {
		List<Integer> checkList=new ArrayList <Integer>();
		checkList.add(4);
		checkList.add(7);
		checkList.add(1);
		List<node_data> path=new ArrayList  <node_data>();
		List<Integer> actual=new ArrayList <Integer>();
		List<Integer> expected=new ArrayList <Integer>();
		expected.add(1);expected.add(2);expected.add(4);expected.add(6);expected.add(7);
		path=ga.TSP(checkList);
		for(int i=0;i<path.size();i++)
			actual.add(path.get(i).getKey());
		if(!actual.contains(1)||!actual.contains(4)||!actual.contains(7))
			fail("The path doesn't contain all the requested nodes.");
		if(actual.get(0)==1) assertEquals(actual.toString(),"[1, 2, 4, 6, 7]");
		if(actual.get(0)==4) assertEquals(actual.toString(),"[4, 6, 7, 4, 3, 2, 1]");
		if(actual.get(0)==7) assertEquals(actual.toString(),"[7, 4, 3, 2, 1]");
		//node 1 is not connected to the other.
		ga.getG().removeEdge(2,1);
		ga.getG().removeEdge(5,1);
		checkList.add(1);
		checkList.add(4);
		checkList.add(7);
		path=ga.TSP(checkList);
		assertNull("There is no path",path);
	}

	/**
	 * Test copy() function
	 */
	@Test
	public void testCopy() {
		graph newG=ga.copy();		
		assertEquals(newG.getMC(),g.getMC());
		assertEquals(newG.getV().size(),g.getV().size());
		for(int i=1;i<=7;i++)
			assertEquals(newG.getE(i).size(),g.getE(i).size());	
	}

}