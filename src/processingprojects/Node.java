package processingprojects;

import java.util.ArrayList;

public class Node {
	String name;
	boolean wall = false;
	int x;
	int y;
	double gScore;
	double fScore;
	Node previous;
	ArrayList<Node> neighbors = new ArrayList<Node>();
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public double heuristic_cost_estimate(int goalX, int goalY) {
		if(!(this.x >= 98 && this.y >= 98)) {
			return Math.sqrt((goalX-x)*(goalX-x) + (goalY - y)*(goalY - y));
		} else {
			return -1;
		}
	}
}