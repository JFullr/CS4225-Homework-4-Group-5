package network;

import java.net.Socket;

import matrix.Matrix;

/**
 * The Class MatrixRequest.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class MatrixRequest {

	private Socket client;
	private Matrix[] matrixes;

	/**
	 * Instantiates a new matrix request.
	 *
	 * @param client   the client
	 * @param matrixes the matrixes
	 */
	public MatrixRequest(Socket client, Matrix... matrixes) {
		this.matrixes = matrixes;
		this.client = client;
	}

	/**
	 * Process to client.
	 */
	public void processToClient() {
		MatrixEval eval = new MatrixEval(this.matrixes);

		/// TODO send to client

	}

}
