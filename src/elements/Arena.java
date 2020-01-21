package elements;

import java.util.List;

import Server.game_service;
import algorithms.Game_Algo;
import dataStructure.DGraph;

public class Arena {
	private int fruits;
	private int moves;
	private int grade;
	private int robots;
	private String graph;
	private List<Fruit> fruitsList;
	private List<Robot> robotsList;
	private DGraph g=new DGraph();;
	private game_service game;

	/**
	 * Initializes the game , graph, fruits list and robots list of the arena from a given game.
	 * @param game represents the given game service.
	 */
	public void init(game_service game) {
		this.game=game;
		this.fruitsList=Game_Algo.createFruitsList();
		this.robotsList=Game_Algo.createRobotsList();
		this.g.init(game.getGraph());
	}
	/**
	 * 	Game getter.
	 * @return the game of that arena.
	 */
	public game_service getGame() {
		return game;
	}
	/**
	 * Fruits getter.
	 * @return the amount of fruits in the arena.
	 */
	public int getFruits() {
		return fruits;
	}
	/**
	 * Moves getter.
	 * @return the amount of moves in the arena.
	 */
	public int getMoves() {
		return moves;
	}
	/**
	 * Grade getter.
	 * @return the current grade of the game.
	 */
	public int getGrade() {
		return grade;
	}

	/**
	 * Robots getter.
	 * @return the amount of robots in the arena.
	 */
	public int getRobots() {
		return robots;
	}
	/**
	 * Graph getter.
	 * @return the graph file name.
	 */
	public String getGraph() {
		return graph;
	}
	/**
	 * Update the robot list of the game;
	 * @param robots represents the updated robots list.
	 */
	public void setRobotsList(List<Robot>robots){
		this.robotsList=robots;
	}

	/**
	 * Update the fruits list of the game;
	 * @param fruits represents the updated fruits list.
	 */
	public void setFruitsList(List<Fruit> fruits) {
		this.fruitsList=fruits;
	}

	/**
	 * Fruits list Getter.
	 * @return the fruits list of the game.
	 */
	public List<Fruit> getFruitsList(){
		return this.fruitsList; 
	}

	/**
	 * Robots list Getter.
	 * @return the robots list of the game.
	 */
	public List<Robot> getRobotsList(){
		return this.robotsList;
	}

	/**
	 * Graph getter.
	 * @return the graph of the game.
	 */
	public DGraph getG() {
		return this.g;
	}
	/**
	 * Update the graph in the arena.
	 * @param gr represents the updated graph.
	 */
	public void setG(DGraph gr) {
		this.g=gr;

	}
}
