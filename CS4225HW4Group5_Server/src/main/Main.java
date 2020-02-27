package main;

import java.io.IOException;

import matrix.Matrix;
import utils.FileUtils;

public class Main {

	public static void main(String[] args) {
		
		Matrix m0 = new Matrix();
		m0.readFromFile("matrix1.txt");
		System.out.println(m0.getString());
		
		Matrix m1 = new Matrix();
		try {
			m0.readFromString(new String(FileUtils.readFile("matrix2.txt")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(m1.getString());
		
	}

}
