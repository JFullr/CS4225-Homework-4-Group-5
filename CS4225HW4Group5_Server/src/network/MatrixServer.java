package network;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import matrix.Matrix;
import utils.ConfigUtils;
import utils.network.Server;

/**
 * The Class MatrixServer.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class MatrixServer {

	private static final String FILE_PORT_KEY = "port";

	private int port;

	private Server server;

	/**
	 * Instantiates a new matrix server.
	 *
	 * @param initFile the init file
	 * @throws Exception the exception
	 */
	public MatrixServer(File initFile) throws Exception {

		HashMap<String, String> config = ConfigUtils.readConfigFile(initFile.getPath());

		this.port = Integer.parseInt(config.get(FILE_PORT_KEY));

	}

	/**
	 * Instantiates a new matrix server.
	 *
	 * @param port the port
	 */
	public MatrixServer(int port) {
		this.port = port;
	}

	/**
	 * Starts the matrix server
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * 
	 * @return if the server started from this call
	 */
	public boolean start() {
		this.server = null;
		try {
			this.server = new Server(this.port);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return this.server.start((client) -> {
			Matrix[] matrixes = null;
			try {
				matrixes = (Matrix[]) client.readBlocking();
			} catch (ClassNotFoundException e) {
				System.err.println("Illegal Object Read");
				return;
			} catch (IOException e) {
				return;
			}
			MatrixEval eval = new MatrixEval(matrixes);
			try {
				client.sendData(eval);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
