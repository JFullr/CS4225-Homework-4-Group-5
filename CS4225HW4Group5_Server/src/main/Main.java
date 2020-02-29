package main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import network.MatrixServer;

/**
 * The Class Main.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Main {
	
	private static final String ERROR_CONFIG_START = "Could Not Read Config File";
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		
		/*
		Matrix m0 = new Matrix(new File("matrix1.txt"));
		System.out.println(m0.stringify());
		
		Matrix m1 = new Matrix(new String(FileUtils.readFile("matrix2.txt")));
		System.out.println(m1.stringify());
		
		Matrix m3 = m0.multiply(m1);
		
		System.out.println(m3.stringify());
		//*/
		
		MatrixServer server;
		try {
			server = new MatrixServer(new File("config.ini"));
		} catch (Exception e) {
			System.out.println(ERROR_CONFIG_START);
			return;
		}
		server.startServer();
		
		Scanner exit = new Scanner(System.in);
		while(true) {
			
			System.out.println("Type 'exit' to end the server");
			
			String input = exit.nextLine();
			if(input.toLowerCase().equals("exit")) {
				exit.close();
				System.exit(0);
			}
		}
		
	}

}
