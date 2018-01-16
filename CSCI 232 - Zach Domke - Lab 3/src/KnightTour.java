//CSCI 232 Lab 3
//Zach Domke
//11-12-16

import java.io.*;
import java.util.*;

class KnightTour {
	static int moves = 0;
 
    /* A utility function to check if i,j are
       valid indexes for N*N chessboard */
    static boolean isSafe(int x, int y, Space sol[][], int inSize) {
        return (x >= 0 && x < inSize && y >= 0 && y < inSize && !sol[x][y].getVisited());
    }
 
    /* A utility function to print solution
       matrix sol[N][N] */
    static void printSolution(Stack<Integer> solution, int inSize) {
    	System.out.println(solution);
    }
 
    /* This function solves the Knight Tour problem
       using Backtracking.  This  function mainly
       uses solveKTUtil() to solve the problem. It
       returns false if no complete tour is possible,
       otherwise return true and prints the tour.
       Please note that there may be more than one
       solutions, this function prints one of the
       feasible solutions.  */
    static boolean solveKT(int inSize, int inX, int inY) {
    	int size = inSize;
    	int startX = inX;
    	int startY = inY;
    	
        Space sol[][] = new Space[size][size];
        
        Stack<Integer> solution = new Stack();
        
 
        /* Initialization of solution matrix */
        int j = 1;
    	for (int x = 0; x < size; x++){
    		for (int y = 0; y < size; y++){
    			sol[x][y] = new Space(false, j);
    			j++;
    		}
    	}
 
       /* xMove[] and yMove[] define next move of Knight.
          xMove[] is for next value of x coordinate
          yMove[] is for next value of y coordinate */
        int xMove[] = {2, 1, -1, -2, -2, -1, 1, 2};
        int yMove[] = {1, 2, 2, 1, -1, -2, -2, -1};
 
        // Since the Knight is initially at the first block
        sol[startX][startY].setVisited();
        solution.push(sol[startX][startY].getValue());
        
        /* Start from 0,0 and explore all tours using
           solveKTUtil() */
        if (!solveKTUtil(size, startX, startY, 1, sol, xMove, yMove, solution)) {
            System.out.println("FAILURE:");
            System.out.println("Total Number of Moves = " + moves);
            System.out.print("Moving Sequence: ");
            printSolution(solution, size);
            return false;
        } else {
            System.out.println("SUCCESS:");
            System.out.println("Total Number of Moves = " + moves);
            System.out.print("Moving Sequence: ");
            printSolution(solution, size);
        }
 
        return true;
    }
 
    /* A recursive utility function to solve Knight
       Tour problem */
    static boolean solveKTUtil(int inSize, int x, int y, int movei, Space sol[][], int xMove[], int yMove[], Stack<Integer> solution) {
    	int size = inSize;
        int k, next_x, next_y;
        if (movei == size * size){
            return true;
        }
        /* Try all next moves from the current coordinate
            x, y */
        for (k = 0; k < 8; k++) {
        	moves++;
            next_x = x + xMove[k];
            next_y = y + yMove[k];
            if (isSafe(next_x, next_y, sol, size)) {
            	solution.push(sol[next_x][next_y].getValue());
                sol[next_x][next_y].setVisited();
                if (solveKTUtil(size, next_x, next_y, movei + 1,sol, xMove, yMove, solution))
                    return true;
                else
                	solution.pop();
                    sol[next_x][next_y].unsetVisited();// backtracking
            }
        }
        return false;
    }
 
    /* Driver program to test above functions */
    public static void main(String args[]) {
    	Scanner in = new Scanner(System.in);
		System.out.print("Enter board size (8 for 8x8):");
		int size = in.nextInt();
		
		System.out.print("Enter beginning square (1 to " + (size * size) + "):");
		int start = in.nextInt();
		int startX;
		int startY;
		if((start % size) == 0){
			startX = (start / size) - 1;
			startY = size - 1;
		} else {
			startX = (start / size);
			startY = (start % size) - 1;
		}
		
        solveKT(size, startX, startY);
    }
}

class Space{
	boolean visited;
	int value;
	
	public Space(boolean visit, int var){
		visited = visit;
		value = var;
	}
	
	public void setVisited(){
		visited = true;
	}
	
	public void unsetVisited(){
		visited = false;
	}
	
	public boolean getVisited(){
		return visited;
	}
	
	public int getValue(){
		return value;
	}
}