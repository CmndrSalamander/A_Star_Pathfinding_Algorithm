package processingprojects;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PImage;


public class GameOfLife extends PApplet {
	static int cols = 100;
	static int rows = 100;
	public int startTime;
	public int timer;
	int w;
	int h;
	int boxX;
	int boxY;
	int gameState = 0;
	double rep = 300;
	int[][] board;
	int[][] neighbors;
	
	enum GameState {
        ACTIVE,
        PAUSED
    }    
    static GameState currentState;
	public static void main(String[] args) {
		PApplet.main("processingprojects.GameOfLife");
	}
	public void settings() {
		size(900, 900);
	}
	public void setup() {
		startTime = millis();
		board = new int[cols][rows];
		neighbors = new int[cols][rows];
		noStroke();
		background(0);
		currentState = GameState.PAUSED;
	}
	public void draw() {
		if(gameState==0) {
			currentState = GameState.PAUSED;
		} else {
			currentState = GameState.ACTIVE;
		}
		if(currentState==GameState.PAUSED) {
			drawPaused();
		} else {
			drawActive();
		}
	}
	public void drawPaused() {
		timer = millis() - startTime;
		w = width/cols;
		h = height/rows;
		if(timer > 1) {
			startTime = millis();
			for(int i = 0; i < cols; i++) {
				for(int j = 0; j < rows; j++) {
					if(board[i][j] == 0) {
						fill(0, 0, 0);
					} else {
						fill(255, 255, 255);
					}
					rect(i*w, j*h, w, h);
					neighbors[i][j] = check(i, j);
				}
			}
		}
	}
	public void drawActive() {
		timer = millis() - startTime;
		w = width/cols;
		h = height/rows;
		if(timer > rep) {
			startTime = millis();
			for(int i = 0; i < cols; i++) {
				for(int j = 0; j < rows; j++) {
					if(board[i][j] == 0) {
						fill(0, 0, 0);
					} else {
						fill(255, 255, 255);
					}
					rect(i*w, j*h, w, h);
					neighbors[i][j] = check(i, j);
				}
			}
			for(int i = 0; i < cols; i++) {
				for(int j = 0; j < rows; j++) {
					if(board[i][j]==0) {
						if(neighbors[i][j]==3) {
							board[i][j] = 1;
						}
					} else {
						if(neighbors[i][j] < 2) {
							board[i][j] = 0;
						} else if(neighbors[i][j]==2 || neighbors[i][j]==3) {
							board[i][j] = 1;
						} else {
							board[i][j] = 0;
						}
					}
				}
			}
		}
	}
	public int check(int x, int y) {
		int neighborSum = 0;
		neighborSum += board[wrap(x+1)][wrap(y)];
		neighborSum += board[wrap(x+1)][wrap(y+1)];
		neighborSum += board[wrap(x)  ][wrap(y+1)];
		neighborSum += board[wrap(x-1)][wrap(y+1)];
		neighborSum += board[wrap(x-1)][wrap(y)];
		neighborSum += board[wrap(x-1)][wrap(y-1)];
		neighborSum += board[wrap(x)  ][wrap(y-1)];
		neighborSum += board[wrap(x+1)][wrap(y-1)];

		return neighborSum;
	}
	public int wrap(int n) {
		if(n < 0) {
			n = cols - 1;
		} else if(n == cols) {
			n = 0;
		}
		return n;
	}
	public void mouseDragged() {
		boxX = (mouseX - mouseX % 9) / 9;
    	boxY = (mouseY - mouseY % 9) / 9;
    	board[boxX][boxY] = 1;
	}
    public void mousePressed() {
    	boxX = (mouseX - mouseX % 9) / 9;
    	boxY = (mouseY - mouseY % 9) / 9;
    	board[boxX][boxY] = (board[boxX][boxY] + 1) % 2;
    }
    public void keyPressed() {
    	if(key == ' ') {
    		gameState = (gameState + 1) % 2;
    	} else if(key == 's') {
    		rep = rep / 0.9;
    		if(rep > 1000) {
    			rep = 1000;
    		}
    	} else if(key == 'w') {
    		rep = rep * 0.9;
    	} else if(key == 'x') {
    		for(int i = 0; i < cols; i++) {
				for(int j = 0; j < rows; j++) {
					board[i][j] = 0;
				}
			}
    	}
    }
}