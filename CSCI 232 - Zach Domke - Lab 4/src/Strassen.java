//CSCI 232 Lab 4
//Zach Domke
//12-8-16

import java.io.*;
import java.util.*;

public class Strassen {
	static int matSize;
	public static void main(String[] args) throws IOException{
		Scanner in = new Scanner(System.in);
		System.out.print("Enter size of matrices (power of 2): ");
		matSize = in.nextInt();
		int[][] matrixA = new int[matSize][matSize];
		int[][] matrixB = new int[matSize][matSize];
		int[][] matrixC = new int[matSize][matSize];
		matrixFill(matrixA);
		matrixFill(matrixB);
		matrixFill(matrixC);
		
		/*
		drawMatrix(matrixA);
		System.out.println("X");
		drawMatrix(matrixB);
		*/
		
		long start = System.currentTimeMillis();
		
		matrixC = solveStr(matrixA, matrixB);
		
		long end = System.currentTimeMillis();
		
		/*
		System.out.println("ANSWER:");
		drawMatrix(matrixC);
		*/
		
		System.out.println((end - start) + " ms");
	}
	
	public static void matrixFill(int[][] matrix){
		Random randomGenerator = new Random();
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[i].length; j++){
				matrix[i][j] = randomGenerator.nextInt(100);
			}
		}
	}
	
	public static void drawMatrix(int[][] matrix){
		System.out.println();
		for(int i = 0; i < matrix.length; i++){
			System.out.print("| " +matrix[i][0]);
			
			for(int j = 1; j < matrix[i].length; j++){
				System.out.print(", " + matrix[i][j]);
			}
			System.out.println(" |");
		}
		System.out.println();
	}
	
	public static int[][] solveBF(int[][] matrixA, int[][] matrixB){
		int n = matrixA.length;
		
		int[][] result = new int[n][n];
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				result[i][j] = 0;
				for(int k = 0; k < n; k++){
					result[i][j] += matrixA[i][k] * matrixB[k][j];
				}
			}
		}
		return result;
	}
	
	public static int[][] solveStr(int[][] A, int[][] B){
		int s = 32;
		int n = A.length;
		int[][] R = new int[n][n];
		
		if((n%2 != 0 ) && (n !=1)){
			int[][] a1, b1, c1;
			int n1 = n+1;
			a1 = new int[n1][n1];
			b1 = new int[n1][n1];
			c1 = new int[n1][n1];

			for(int i=0; i<n; i++)
				for(int j=0; j<n; j++){
					a1[i][j] =A[i][j];
					b1[i][j] =B[i][j];
				}
			c1 = solveStr(a1, b1);
			for(int i=0; i<n; i++)
				for(int j=0; j<n; j++)
					R[i][j] =c1[i][j];
			return R;
		}
		
		if(n == 1){
			R[0][0] = A[0][0] * B[0][0];
		} else if(n == s){
			R = solveBF(A, B);
			return R;
		} else {
			int [][] A11 = new int[n/2][n/2];
			int [][] A12 = new int[n/2][n/2];
			int [][] A21 = new int[n/2][n/2];
			int [][] A22 = new int[n/2][n/2];

			int [][] B11 = new int[n/2][n/2];
			int [][] B12 = new int[n/2][n/2];
			int [][] B21 = new int[n/2][n/2];
			int [][] B22 = new int[n/2][n/2];

			divide(A, A11, 0 , 0);
			divide(A, A12, 0 , n/2);
			divide(A, A21, n/2, 0);
			divide(A, A22, n/2, n/2);

			divide(B, B11, 0 , 0);
			divide(B, B12, 0 , n/2);
			divide(B, B21, n/2, 0);
			divide(B, B22, n/2, n/2);

			int [][] P1 = solveStr(add(A11, A22), add(B11, B22));
			int [][] P2 = solveStr(add(A21, A22), B11);
			int [][] P3 = solveStr(A11, sub(B12, B22));
			int [][] P4 = solveStr(A22, sub(B21, B11));
			int [][] P5 = solveStr(add(A11, A12), B22);
			int [][] P6 = solveStr(sub(A21, A11), add(B11, B12));
			int [][] P7 = solveStr(sub(A12, A22), add(B21, B22));

			int [][] C11 = add(sub(add(P1, P4), P5), P7);
			int [][] C12 = add(P3, P5);
			int [][] C21 = add(P2, P4);
			int [][] C22 = add(sub(add(P1, P3), P2), P6);

			copy(C11, R, 0 , 0);
			copy(C12, R, 0 , n/2);
			copy(C21, R, n/2, 0);
			copy(C22, R, n/2, n/2);
		}
		return R;
	}

	public static int [][] add(int [][] A, int [][] B){
		int n = A.length;
		int [][] result = new int[n][n];

		for(int i=0; i<n; i++)
			for(int j=0; j<n; j++)
				result[i][j] = A[i][j] + B[i][j];

		return result;
	}

	public static int [][] sub(int [][] A, int [][] B){
		int n = A.length;
		int [][] result = new int[n][n];

		for(int i=0; i<n; i++)
			for(int j=0; j<n; j++)
				result[i][j] = A[i][j] - B[i][j];

		return result;
	}

	public static void divide(int[][] p1, int[][] c1, int iB, int jB){
		for(int i1 = 0, i2=iB; i1<c1.length; i1++, i2++)
			for(int j1 = 0, j2=jB; j1<c1.length; j1++, j2++){
				c1[i1][j1] = p1[i2][j2];
			}
	}

	public static void copy(int[][] c1, int[][] p1, int iB, int jB){
		for(int i1 = 0, i2=iB; i1<c1.length; i1++, i2++)
			for(int j1 = 0, j2=jB; j1<c1.length; j1++, j2++){
				p1[i2][j2] = c1[i1][j1];
			}
	}
}
