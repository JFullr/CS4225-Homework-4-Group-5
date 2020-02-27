package network;

import java.net.Socket;

import matrix.Matrix;

public class MatrixRequest {
	
	private Socket client;
	private Matrix[] matrixes;
	
	public MatrixRequest(Socket client, Matrix... matrixes) {
		this.matrixes = matrixes;
		this.client = client;
	}
	
	public void processToClient() {
		MatrixEval eval = new MatrixEval(this.matrixes);
		
		///TODO send to client
		
	}

}
