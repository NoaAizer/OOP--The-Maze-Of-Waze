package elements;

public class Game {
	private int fruits;
	private int moves;
	private int grade;
	private int robots;
	private String graph;
	
	public Game(int fruits, int moves, int grade, int robots, String graph) {
		super();
		this.fruits = fruits;
		this.moves = moves;
		this.grade = grade;
		this.robots = robots;
		this.graph = graph;
	}

	public int getFruits() {
		return fruits;
	}

	public int getMoves() {
		return moves;
	}

	public int getGrade() {
		return grade;
	}

	public int getRobots() {
		return robots;
	}

	public String getGraph() {
		return graph;
	}

}
