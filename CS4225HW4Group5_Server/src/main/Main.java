package main;

import java.io.File;
import java.io.IOException;

import matrix.Matrix;
import utils.FileUtils;

/**
 * The Class Main.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Main {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		
		Matrix m0 = new Matrix(new File("matrix1.txt"));
		System.out.println(m0.stringify());
		
		Matrix m1 = new Matrix(new String(FileUtils.readFile("matrix2.txt")));
		System.out.println(m1.stringify());
		
		Matrix m3 = m0.multiply(m1);
		
		System.out.println(m3.stringify());
		
	}

}
