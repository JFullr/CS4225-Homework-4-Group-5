package main;

import java.io.File;
import java.io.IOException;

import matrix.Matrix;
import utils.FileUtils;

public class Main {

	public static void main(String[] args) {
		
		Matrix m0 = new Matrix(new File("matrix1.txt"));
		System.out.println(m0.stringify());
		
		try {
			Matrix m1 = new Matrix(new String(FileUtils.readFile("matrix2.txt")));
			System.out.println(m1.stringify());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
