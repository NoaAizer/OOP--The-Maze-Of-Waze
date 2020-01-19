package Tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;



class DGraphTest {

	private DGraph g;
	private Point3D p1,p2,p3,p4,p5,p6,p7;
	private node_data n1,n2,n3,n4,n5,n6,n7;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{

	}
	@BeforeEach
	void setupGraphs() {

		g=new DGraph();

		p1=new Point3D(-1,-1,1);
		p2=new Point3D(-1,1,4);
		p3=new Point3D(0,0,0);
		p4=new Point3D(8,1,-1);
		p5=new Point3D(-1,1,-1);
		p6=new Point3D(10,5,1);
		p7=new Point3D(9,0,1);

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
		g.connect(n1.getKey(), n3.getKey(), 3);
		g.connect(n2.getKey(), n4.getKey(), 5);
		g.connect(n3.getKey(), n2.getKey(), 6);
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
	 * Tests DGraph(DGraph) constructor.
	 */
	@Test
	void testDGraphConstructor() {
		DGraph gTest = new DGraph(g);
		assertEquals("Checks the nodes",g.getNodes(),gTest.getNodes());
		assertEquals("Checks the edges",g.getEdges() ,gTest.getEdges());
	}
	/**
	 * Tests getNode(key) function.
	 */
	@Test
	void testGetNode() {
		int key1=n1.getKey(), key7=n7.getKey();
		int key_false=10;
		assertEquals("Check the returned node",g.getNode(key1),n1);
		assertEquals("Check the returned node",g.getNode(key7),n7);
		assertNull("The node is not in the graph",g.getNode(key_false));
	}
	/**
	 * Tests getEdge(src,dest) function.
	 */
	@Test
	void testGetEdge() {
		int src_false=0, dst_false=9;
		assertNull("The source node is not in the graph",g.getEdge(src_false,n4.getKey()));
		assertNull("The destenation node is not in the graph",g.getEdge(n4.getKey(),dst_false));
		assertNull("The source and destenation nodes are not in the graph",g.getEdge(src_false,dst_false));
		assertNull("There is only an edge in the other direction",g.getEdge(n2.getKey(), n1.getKey()));
		edge_data ex_edge = new Edge (n2.getKey(),n4.getKey(),5);
		assertEquals("Check the returned edge",g.getEdge(n2.getKey(),n4.getKey()),ex_edge);
	}
	/**
	 * Tests addNode(node_data) function.
	 */
	@Test
	void testAddNode() {
		assertEquals(g.getNode(n1.getKey()),n1);
		assertEquals(g.getNode(n4.getKey()),n4);
		assertEquals(g.getNode(n7.getKey()),n7);
		int numOfNodes=g.nodeSize();
		g.addNode(n7);//try adding a node which already exists
		if(numOfNodes!=g.nodeSize())
			fail("ERR: Shouldn't add an existing node");
	}

	/**
	 * Tests connect(src,dest,weight) function.
	 */
	@Test
	void testConnect() {
		{
			assertEquals(g.getEdge(n1.getKey(),n2.getKey()).getSrc(),n1.getKey());
			assertEquals(g.getEdge(n1.getKey(),n2.getKey()).getDest(),n2.getKey());
			int numOfEdges=g.edgeSize();
			g.connect(n1.getKey(), n3.getKey(), -1);// negative weight
			g.connect(n1.getKey(), n1.getKey(), 1);// same node
			g.connect(n1.getKey(), 10, 1);// the destination node is not exist
			if(numOfEdges!=g.edgeSize())
				fail("ERR: Shouldn't add these edges");		
		}
	}

	/**
	 * Tests getV() function.
	 */
	@Test
	void testGetV() {
		{
			Collection<node_data> excepted= new ArrayList<>();
			excepted.add(n1);
			excepted.add(n2);
			excepted.add(n3);
			excepted.add(n4);
			excepted.add(n5);
			excepted.add(n6);
			excepted.add(n7);
			Collection<node_data> actual=g.getV();
			assertEquals(excepted.toString(),actual.toString());
		}
	}

	/**
	 * Tests getE() function.
	 */
	@Test
	void testGetE() {
		{
			Collection<edge_data> excepted= new ArrayList<>();
			excepted.add(new Edge (n1.getKey(), n2.getKey(), 4));
			excepted.add(new Edge (n1.getKey(), n3.getKey(), 3));
			Collection<edge_data> actual=g.getE(n1.getKey());
			if(!excepted.toString().equals(actual.toString()))
				fail("The edges should be the same");
		}
	}
	/**
	 * Tests removeNode(key) function.
	 */
	@Test
	void testremoveNode() 
	{
		n1=g.removeNode(n1.getKey());
		n2=g.removeNode(n2.getKey());
		node_data n10=g.removeNode(n1.getKey());
		assertNull("Already removed this node",n10);
		assertEquals("5 nodes should be left",g.nodeSize(),5);
		assertEquals("7 edged should be left",g.edgeSize(),7);		
	}

	/**
	 * Tests removeEdge(src,dest) function.
	 */
	@Test
	void testremoveEdge() 
	{
		g.removeEdge(n4.getKey(), n6.getKey());
		g.removeEdge(n5.getKey(), n4.getKey());
		g.removeEdge(n5.getKey(), n1.getKey());
		g.removeEdge(n5.getKey(), n7.getKey());
		g.removeEdge(n6.getKey(), n7.getKey());
		g.removeEdge(n7.getKey(), n4.getKey());
		assertEquals("6 edged should be left",g.edgeSize(),6);
		edge_data e_null=g.removeEdge(n4.getKey(),n6.getKey());
		assertNull("The edge is already removed",e_null);
		n1=g.removeNode(n1.getKey());
		edge_data e1_s=g.removeEdge(n1.getKey(),n2.getKey());//there is no such src vertex
		edge_data e1_d=g.removeEdge(n5.getKey(),n1.getKey());//there is no such dest vertex
		assertNull("There is no edge (the source node has been removed) ",e1_s);
		assertNull("There is no edge (the destenation node has been removed) ",e1_d);
	}

	/**
	 * Tests nodeSize() function.
	 */
	@Test
	void testNodeSize() 
	{
		assertEquals(7,g.nodeSize());
		g.removeNode(n1.getKey());
		assertEquals(6,g.nodeSize());
		g.removeNode(n1.getKey());
		assertEquals("There is no change",6,g.nodeSize());
	}

	/**
	 * Tests edgeSize() function.
	 */
	@Test
	void testEdegSize() 
	{
		assertEquals(12,g.edgeSize());
		g.removeEdge(n1.getKey(), n2.getKey());
		assertEquals("Should remove one edge",11,g.edgeSize());
		g.removeNode(n3.getKey());
		assertEquals("Should remove 4 edges",7,g.edgeSize());
	}

	/**
	 * Tests getMC() function.
	 */
	@Test
	void testGetMC() 
	{
		int current_mc=g.getMC();
		g.removeEdge(n1.getKey(), n2.getKey());
		assertEquals(current_mc+1,g.getMC());
		current_mc++;
		g.connect(n1.getKey(), n2.getKey(),2);
		assertEquals(current_mc+1,g.getMC());
		current_mc++;
		g.addNode(new Node(8,new Point3D(0,0,0),0,null,0));
		assertEquals(current_mc+1,g.getMC());
		current_mc++;
		g.removeNode(n3.getKey());
		assertEquals(current_mc+5,g.getMC());//remove one node and 4 edges.
		current_mc+=5;
	}

	/**
	 * Tests init(jsonFile) function.
	 */
	@Test

	void testInit()
	{
		game_service game = Game_Server.getServer(0); 
		String g = game.getGraph();
		DGraph gg = new DGraph();
		gg.init(g);
		if(gg.nodeSize()!=11||gg.edgeSize()!=22)
			fail("DGraph init is faild!");
	}


	/**
	 * Tests toJSON() function.
	 */
	@Test

	void testToJSON()
	{
		game_service game = Game_Server.getServer(0); 
		String excepted = game.getGraph();
		DGraph gEx = new DGraph();
		gEx.init(excepted);
		String actual= gEx.toJSON();
		assertEquals("JSON files should be the same!",actual,excepted);
	}
}
