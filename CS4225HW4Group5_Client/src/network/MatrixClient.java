package network;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import matrix.Matrix;
import utils.ConfigUtils;
import utils.ErrorHandler;
import utils.network.Client;

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

	private static final String FILE_ADDRESS_KEY = "server-ip";
	private static final String FILE_PORT_KEY = "port";
	private static final int SOCKET_TIMEOUT_SECONDS = 3;

	private String address;
	private int port;

	/**
	 * Instantiates a new matrix client.
	 *
	 * @param initFile the init file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public MatrixClient(File initFile) {

		try {
			HashMap<String, String> config = ConfigUtils.readConfigFile(initFile.getPath());

			String address = config.get(FILE_ADDRESS_KEY);
			if (address == null) {
				throw null;
			}

			int port = Integer.parseInt(config.get(FILE_PORT_KEY));

			this.address = address;
			this.port = port;
		
		} catch (IOException e) {
			ErrorHandler.addError(ERROR_INIT_FILE);
			return;
		} catch (Exception e) {
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
	 * Requests the matrix server to evaluate and respond with a product of the
	 * given matrices and returns the response as a Matrix object.
	 *
	 * @param matrixes the matrixes
	 * @return the matrix
	 */
	public MatrixEval multiplyMatrices(Matrix... matrixes) {
		
		Client client = new Client(this.address, this.port, SOCKET_TIMEOUT_SECONDS);
		
		MatrixEval evaluated = null;
		try {
			client.connect(matrixes);
		} catch (IOException e1) {
			ErrorHandler.addError(ERROR_SERVER_CONNECTION);
			return null;
		}
		
		try {
			evaluated = (MatrixEval)client.readBlocking();
		} catch (ClassNotFoundException e1) {
			ErrorHandler.addError(ERROR_NETWORK_OBJECT);
		} catch (IOException e1) {
			ErrorHandler.addError(ERROR_SERVER_TIMEOUT);
		}

		return evaluated;

	}

}
