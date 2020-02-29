package main;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import matrix.Matrix;
import network.MatrixClient;
import network.MatrixEval;
import utils.ErrorHandler;

/**
 * The Class Main.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Main {

	public static final int MAX_MATRIX_PRINT_WIDTH = 15;

	public static final int MAX_MATRIX_SIZE = 150;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) {

		try {
			/*if (args.length == 0) {
				System.err.println("need to specify .txt file");
				System.exit(1);
			}*/
			Matrix[] toMultiply = Main.randomMultipliableMatricies();

			MatrixClient multiplier = new MatrixClient(new File("config.ini"));

			MatrixEval result = multiplier.multiplyMatrices(toMultiply);

			if (ErrorHandler.hasNextError()) {
				handleErrors();
			} else {
				process(result);
			}
		} catch (Exception e) {
			System.err.println("Error: caused by: " + e.getMessage());
			System.exit(1);
		}
	}

	/// TODO file handling and printing
	private static void process(MatrixEval result) {
		if (result != null) {

			if (result.getMatrix() != null) {
				System.out.println(result.getMatrix().stringify());
			}
			System.out.println(result.getError());
			System.out.println(result.getTimeMilliseconds());

		}
	}

	private static void handleErrors() {

		while (ErrorHandler.hasNextError()) {
			System.out.println(ErrorHandler.consumeNextError());
		}

	}

	private static Matrix[] randomMultipliableMatricies() {

		Random rand = new Random();

		int height = rand.nextInt(MAX_MATRIX_SIZE) + 1;
		int width = rand.nextInt(MAX_MATRIX_SIZE) + 1;
		int width2 = rand.nextInt(MAX_MATRIX_SIZE) + 1;

		Matrix matrixA = new Matrix(height, width);
		Matrix matrixB = new Matrix(width, width2);

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
