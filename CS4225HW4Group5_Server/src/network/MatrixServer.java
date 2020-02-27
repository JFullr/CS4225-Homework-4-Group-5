package network;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import utils.FileUtils;

public class MatrixServer {
	
	private static final String FILE_PORT_KEY = "port";
	
	private int port;
	
	private ServerSocket server;
	
	public MatrixServer(File initFile) throws IOException {
		String[] data = FileUtils.readLines(initFile.getPath());
		for(String line : data) {
			if(line.toLowerCase().startsWith(FILE_PORT_KEY)) {
				line = line.substring(line.indexOf(":")+1);
				this.port = Integer.parseInt(line.trim());
				break;
			}
		}
		
		this.initServer();
	}
	
	public MatrixServer(int port) throws IOException {
		this.port = port;
		this.initServer();
	}
	
	
	
	
	private void initServer() throws IOException {
		this.server = new ServerSocket(this.port);
	}
	

}
