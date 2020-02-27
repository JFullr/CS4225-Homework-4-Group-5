package main;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import matrix.Matrix;
import network.MatrixClient;

public class Main {

	public static final int MAX_MATRIX_SIZE = 100;

	public static void main(String[] args) throws IOException {
		
		Matrix[] toMultiply = Main.randomMultipliableMatricies();
		MatrixClient multiplier = new MatrixClient(new File("config.ini"));
		Matrix result = multiplier.multiplyMatricies(toMultiply);
		
		///TODO file handling and printing

	}

	private static Matrix[] randomMultipliableMatricies() {

		Random rand = new Random();

		int height = rand.nextInt(MAX_MATRIX_SIZE) + 1;
		int width = rand.nextInt(MAX_MATRIX_SIZE) + 1;
		int height2 = rand.nextInt(MAX_MATRIX_SIZE) + 1;

		Matrix a = new Matrix(height, width);
		Matrix b = new Matrix(height2, height);

		for (int y = 0; y < a.getHeight(); y++) {
			for (int x = 0; x < a.getWidth(); x++) {
				a.setValue(y, x, rand.nextDouble());
			}
		}

		for (int y = 0; y < b.getHeight(); y++) {
			for (int x = 0; x < b.getWidth(); x++) {
				b.setValue(y, x, rand.nextDouble());
			}
		}

		return new Matrix[] { a, b };

	}

}
