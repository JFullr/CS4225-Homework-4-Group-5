package network;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import matrix.Matrix;
import utils.ErrorHandler;
import utils.FileUtils;

/**
 * The Class MatrixClient handles the client side of the matrix operations.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class MatrixClient {
	
	private static final String ERROR_INIT_FILE = "Could Not Read Init File";
	private static final String ERROR_INIT_FILE_VALUE = "Illegal value in Init File";

	private static final String ERROR_SERVER_CONNECTION = "Could Not Connect To Server";
	private static final String ERROR_NETWORK_OBJECT = "Read an Illegal Object";
	private static final String ERROR_SERVER_TIMEOUT = "Server Timed Out";
	
	
	private static final String FILE_IP_KEY = "server-ip";
	private static final String FILE_PORT_KEY = "port";
	private static final int SOCKET_TIMEOUT_SECONDS = 30;

	private String address;
	private int port;
	private Socket client;

	/**
	 * Instantiates a new matrix client.
	 *
	 * @param initFile the init file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public MatrixClient(File initFile) {
		
		String[] data = null;
		
		try {

			data = FileUtils.readLines(initFile.getPath());
		
		}catch(Exception e) {
			ErrorHandler.addError(ERROR_INIT_FILE);
			return;
		}
		
		try {
			for (String line : data) {
				if (line.toLowerCase().startsWith(FILE_IP_KEY)) {
					line = line.substring(line.indexOf(":") + 1);
					this.address = line.trim();
				} else if (line.toLowerCase().startsWith(FILE_PORT_KEY)) {
					line = line.substring(line.indexOf(":") + 1);
					this.port = Integer.parseInt(line.trim());
				}
			}
		}catch(Exception e) {
			ErrorHandler.addError(ERROR_INIT_FILE_VALUE);
			return;
		}
	}

	/**
	 * Instantiates a new matrix client.
	 *
	 * @param address the address
	 * @param port    the port
	 */
	public MatrixClient(String address, int port) {
		this.address = address;
		this.port = port;
	}

	/**
	 * Requests the matrix server to evaluate and respond with a product of the given
	 * matrices and returns the response as a Matrix object.
	 *
	 * @param matrices the matrixes
	 * @return the matrix
	 */
	public MatrixEval multiplyMatrices(Matrix... matrixes) {

		if (this.client == null) {
			this.client = this.createClient();
			if (this.client == null) {
				ErrorHandler.addError(ERROR_SERVER_CONNECTION);
				return null;
			}
		}

		MatrixEval evaluated = null;
		try {
			
			ObjectOutputStream write = new ObjectOutputStream(this.client.getOutputStream());
			write.writeObject(matrixes);
			write.flush();
			
			ObjectInputStream response = new ObjectInputStream(this.client.getInputStream());
			evaluated = (MatrixEval) response.readObject();
			
		} catch (IOException e) {
			ErrorHandler.addError(ERROR_SERVER_TIMEOUT);
		} catch (ClassNotFoundException e) {
			ErrorHandler.addError(ERROR_NETWORK_OBJECT);
		}

		return evaluated;

	}

	private Socket createClient() {

		Socket client = null;
		try {
			client = new Socket(this.address, this.port);
			client.setSoTimeout(SOCKET_TIMEOUT_SECONDS);
		} catch (Exception e) {
			client = null;
		}

		return client;
	}

}
