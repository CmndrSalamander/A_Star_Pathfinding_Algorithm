package processingprojects;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;


public class AStar extends PApplet {
	static int cols = 100;
	static int rows = 100;
	int w;
	int h;
	int black;
	int lowest;
	int wallChance = 35;
	int[][] board;
	Node[][] nodeBoard;
	ArrayList<Node> Nodes = new ArrayList<Node>();
	ArrayList<Node> closedSet = new ArrayList<Node>();
	ArrayList<Node> openSet = new ArrayList<Node>();
	ArrayList<Node> totalPath = new ArrayList<Node>();
	Node current;
	Random rand = new Random();

	public static void main(String[] args) {
		PApplet.main("processingprojects.AStar");
	}
	public void settings() {
		size(900, 900);
	}
	public void setup() {
		board = new int[cols][rows];
		nodeBoard = new Node[cols][rows];
		w = width/cols;
		h = height/rows;
		background(255, 255, 255);
		noStroke();
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				Node node = new Node(i, j);
				Nodes.add(node);
				node.gScore = Integer.MAX_VALUE;
				node.fScore = Integer.MAX_VALUE;
				nodeBoard[i][j] = node;
				node.x = i;
				node.y = j;
				black = rand.nextInt(100);
				if(black < wallChance) {
					board[i][j] = 1;
					node.wall = true;
				}
			}
		}
		board[0][0] = 0;
		board[99][99] = 0;
		Nodes.get(0).wall = false;
		Nodes.get(9999).wall = false;
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				if(board[i][j] == 0) {
					fill(255, 255, 255);
				} else {
					fill(0, 0, 0);
				}
				ellipse(i*w + w/2, j*h + h/2, w, h);
			}
		}
	}
	public void draw() {
		totalPath.addAll(A_Star(Nodes.get(0), Nodes.get(9999)));
		for(Node node : totalPath) {
			fill(0, 255, 0);
			ellipse(w/2, h/2, w, h);
			try {
				fill(255, 0, 0);
				stroke(255, 0, 0);
				strokeWeight(3);
				line(node.x*w + w/2, node.y*h + h/2, node.previous.x*w + w/2, node.previous.y*w + w/2);
			}
			catch(NullPointerException e) {
				
			}
		}
		fill(0, 0, 255);
		ellipse(99*w + w/2, 99*h + h/2, w, h);
	}
	public ArrayList<Node> reconstruct_path(Node current) {
		totalPath.add(current);
		try {
			while(current.previous != Nodes.get(0)) {
				current = current.previous;
				totalPath.add(current);
			}
		}
		catch (NullPointerException e) {
			System.out.println("No path exists.");
		}
		return totalPath;
	}
	public ArrayList<Node> A_Star(Node start, Node goal) {
		double tentativeGScore;
		start.gScore = 0;
		start.fScore = start.heuristic_cost_estimate(goal.x, goal.y);

		openSet.add(start);

		while(!openSet.isEmpty()) {
			closedSet.remove(goal);
			current = openSet.get(lowest(openSet));
			if(current == Nodes.get(9999)) {
				return reconstruct_path(current);
			}
			openSet.remove(current);
			closedSet.add(current);
			closedSet.remove(goal);
			findNeighbors(current);
			for(Node neighbor : current.neighbors) {
				if(closedSet.contains(neighbor)) {
					continue;
				}
				tentativeGScore = current.gScore + distance(current, neighbor);

				if(!openSet.contains(neighbor)) {
					openSet.add(neighbor);
				} else if(tentativeGScore >= neighbor.gScore) {
					continue;
				}
				neighbor.previous = current;
				neighbor.gScore = tentativeGScore;
				neighbor.fScore = neighbor.gScore + neighbor.heuristic_cost_estimate(99, 99);
			}
		}
		return reconstruct_path(current);
	}
	public double distance(Node node1, Node node2) {
		return Math.sqrt((node1.x-node2.x)*(node1.x-node2.x) + (node1.y-node2.y)*(node1.y-node2.y));
	}
	public void findNeighbors(Node node) {
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				if((Math.abs(node.x - nodeBoard[i][j].x) <= 1) && (Math.abs(node.y - nodeBoard[i][j].y) <= 1) && nodeBoard[i][j].wall == false) {
					node.neighbors.add(nodeBoard[i][j]);
				}
			}
		}
	}
	public int lowest(ArrayList<Node> NodeArray) {
		int lowest = Integer.MAX_VALUE;
		for(int i = 0; i < NodeArray.size(); i++) {
			if(NodeArray.get(i).fScore < lowest) {
				lowest = i;
			}
		}
		return lowest;
	}

}