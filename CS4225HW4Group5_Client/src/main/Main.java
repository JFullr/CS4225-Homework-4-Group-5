package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
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
				
			}else {
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

	/// TODO file handling and printing
	private static void processResult(MatrixEval result) {
		if (result == null) {
			System.out.println("The Server Did Not Give A Response");
			return;
		}

		if (result.getMatrix() != null) {
			if(result.getMatrix().getHeight() > MAX_MATRIX_PRINT_HEIGHT || 
				result.getMatrix().getWidth() > MAX_MATRIX_PRINT_WIDTH){
				System.out.println("The matrix is too large to display and will be saved to disk.");
			}else {
				System.out.println(result.getMatrix().stringify());
			}
		}
		System.out.println("Errors: "+result.getError());
		System.out.println("Computed In:");
		long miliseconds = result.getTimeMilliseconds();
		long seconds = miliseconds/1000;
		long minutes = seconds/60;
		System.out.println(minutes+"mins  "+seconds+"seconds  "+miliseconds+"millis");
		
		if (result.getMatrix() != null) {
			saveMatrixToDisk(result.getMatrix());
		}
		
	}
	
	
	private static void saveMatrixToDisk(Matrix matrix) {
		File save = new File("MatrixCalculation.txt");
		try (PrintWriter out = new PrintWriter(save)){
			out.println(matrix.stringify());
			out.flush();
			
			System.out.println("The Matrix is available to at: "+save.getAbsolutePath());
			
		}catch(Exception e) {
			System.out.println("Could Not Write The Matrix To File");
		}
	}
	
	
	
	private static Matrix[] getUserMatrixPath() {
		
		Matrix[] read = null;
		
		Scanner input = new Scanner(System.in);
		while(true) {
			try {
				System.out.println("Input a matrix file path; or type 'exit' to exit");
				String path = input.nextLine();
				
				if(path.toLowerCase().startsWith("exit")) {
					System.exit(0);
				}
				
				read = Matrix.readMatrixes(new File(path));
				
				break;
			}catch(Exception e) { }
		}
		
		input.close();
		
		return read;
		
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
	
	private static void stressTest(final Matrix... matricies) {
		Thread[] pool = new Thread[7000];
		MatrixClient multiplier = new MatrixClient(new File("config.ini"));
		
		for(int i = 0; i < pool.length; i++) {
			pool[i] = new Thread(()->{
				MatrixEval eval = multiplier.multiplyMatrices(matricies);
				//process(eval);
			});
		}
		
		for(int i = 0; i < pool.length; i++) {
			pool[i].start();
		}
		
		for(int i = 0; i < pool.length; i++) {
			try {
				pool[i].join();
			} catch (InterruptedException e) { e.printStackTrace();}
		}
		
	}

}
