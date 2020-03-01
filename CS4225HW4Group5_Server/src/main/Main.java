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
	 * @throws IOException          Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public static void main(String[] args) throws IOException, InterruptedException {

		MatrixServer server;
		try {
			server = new MatrixServer(new File("config.ini"));
		} catch (Exception e) {
			System.out.println(ERROR_CONFIG_START);
			return;
		}

		server.start();
		
		Scanner exit = new Scanner(System.in);
		while (true) {

			System.out.println("Type 'exit' to end the server");

			String input = exit.nextLine();
			if (input.toLowerCase().equals("exit")) {
				exit.close();
				System.exit(0);
			}
		}
	}

}
