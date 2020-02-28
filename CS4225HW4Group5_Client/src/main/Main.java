package main;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import matrix.Matrix;
import network.MatrixClient;

/**
 * The Class Main.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Main {

	public static final int MAX_MATRIX_SIZE = 100;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		
		Matrix[] toMultiply = Main.randomMultipliableMatricies();
		MatrixClient multiplier = new MatrixClient(new File("config.ini"));
		Matrix result = multiplier.multiplyMatrices(toMultiply);
		
		///TODO file handling and printing

	}
	
	private static Matrix[] randomMultipliableMatricies() {

		Random rand = new Random();

		int height = rand.nextInt(MAX_MATRIX_SIZE) + 1;
		int width = rand.nextInt(MAX_MATRIX_SIZE) + 1;
		int height2 = rand.nextInt(MAX_MATRIX_SIZE) + 1;

		Matrix matrixA = new Matrix(height, width);
		Matrix matrixB = new Matrix(height2, height);

		for (int y = 0; y < matrixA.getHeight(); y++) {
			for (int x = 0; x < matrixA.getWidth(); x++) {
				matrixA.setValue(y, x, rand.nextDouble());
			}
		}

		for (int y = 0; y < matrixB.getHeight(); y++) {
			for (int x = 0; x < matrixB.getWidth(); x++) {
				matrixB.setValue(y, x, rand.nextDouble());
			}
		}

		return new Matrix[] { matrixA, matrixB };

	}

}
