package network;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import matrix.Matrix;
import utils.FileUtils;

public class MatrixClient {
	
	private static final String FILE_IP_KEY = "server-ip";
	private static final String FILE_PORT_KEY = "port";
	
	private String address;
	private int port;
	
	private Socket client;
	
	public MatrixClient(File initFile) throws IOException {
		String[] data = FileUtils.readLines(initFile.getPath());
		for(String line : data) {
			if(line.toLowerCase().startsWith(FILE_IP_KEY)) {
				line = line.substring(line.indexOf(":")+1);
				this.address = line.trim();
			}else if(line.toLowerCase().startsWith(FILE_PORT_KEY)) {
				line = line.substring(line.indexOf(":")+1);
				this.port = Integer.parseInt(line.trim());
			}
		}
	}
	
	public MatrixClient(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	public Matrix multiplyMatricies(Matrix... matrixes){
		
		if(this.client == null) {
			this.client = this.createClient();
			if(this.client == null) {
				return null;
			}
		}
		
		Matrix evaluated = this.requestServer(matrixes);
		
		return evaluated;
		
	}
	
	
	
	private Matrix requestServer(Matrix... matrixes) {
		return this.waitResponse();
	}
	
	private Matrix waitResponse() {
		return null;
	}
	
	private Socket createClient() {
		
		Socket client = null;
		try {
			client = new Socket(this.address, this.port);
			client.setSoTimeout(3);
		} catch (Exception e) {
			client = null;
		}
		
		return client;
	}

}
