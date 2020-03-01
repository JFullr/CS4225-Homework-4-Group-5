package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

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

	private static final String CLIENT_CONFIG_FILE = "config.ini";

	public static final int MAX_MATRIX_PRINT_WIDTH = 10;
	public static final int MAX_MATRIX_PRINT_HEIGHT = 20;

	public static final int MAX_MATRIX_SIZE = 150;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) {

		try {

			Matrix[] toMultiply = null;

			if (args != null && args.length != 0) {

				toMultiply = Matrix.readMatrixes(new File(args[0]));

			} else {
				System.out.println("No matrix file was given");
				toMultiply = getUserMatrixPath();
			}

			MatrixClient multiplier = new MatrixClient(new File(CLIENT_CONFIG_FILE));
			MatrixEval result = multiplier.multiplyMatrices(toMultiply);

			if (ErrorHandler.hasNextError()) {
				handleErrors();
			} else {
				processResult(result);
			}

		} catch (Exception e) {
			System.err.println("Error: caused by: " + e.getMessage());
			System.exit(1);
		}

	}

	private static void processResult(MatrixEval result) {
		if (result == null) {
			System.out.println("The Server Did Not Give A Response");
			return;
		}

		if (result.getMatrix() != null) {
			if (result.getMatrix().getHeight() > MAX_MATRIX_PRINT_HEIGHT
					|| result.getMatrix().getWidth() > MAX_MATRIX_PRINT_WIDTH) {
				System.out.println("The matrix is too large to display and will be saved to disk.");
			} else {
				System.out.println(result.getMatrix().stringify());
			}
		}
		System.out.println("Errors: " + result.getError());
		System.out.println("Computed In:");
		long miliseconds = result.getTimeMilliseconds();
		long seconds = miliseconds / 1000;
		long minutes = seconds / 60;
		System.out.println(minutes + "mins  " + seconds + "seconds  " + miliseconds + "millis");

		if (result.getMatrix() != null) {
			saveMatrixToDisk(result.getMatrix());
		}

	}

	private static void saveMatrixToDisk(Matrix matrix) {
		File save = new File("MatrixCalculation.txt");
		try (PrintWriter out = new PrintWriter(save)) {
			out.println(matrix.getStorageRepresentation());
			out.flush();

			System.out.println("The Matrix is available to at: " + save.getAbsolutePath());

		} catch (Exception e) {
			System.out.println("Could Not Write The Matrix To File");
		}
	}

	private static Matrix[] getUserMatrixPath() {

		Matrix[] read = null;

		Scanner input = new Scanner(System.in);
		while (true) {
			try {
				System.out.println("Input a matrix file path; or type 'exit' to exit");
				String path = input.nextLine();

				if (path.toLowerCase().startsWith("exit")) {
					System.exit(0);
				}

				read = Matrix.readMatrixes(new File(path));

				break;
			} catch (Exception e) {
			}
		}

		input.close();

		return read;

	}

	private static void handleErrors() {

		while (ErrorHandler.hasNextError()) {
			System.out.println(ErrorHandler.consumeNextError());
		}

	}

}
