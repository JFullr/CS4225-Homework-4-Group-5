package network;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;

import matrix.Matrix;
import utils.FileUtils;

public class MatrixServer {

	private static final String FILE_PORT_KEY = "port";
	private static final Object REQUESTS_LOCK = new Object();
	
	private Queue<MatrixRequest> requests;
	
	private int port;
	private ServerSocket server;

	public MatrixServer(File initFile) throws IOException {
		String[] data = FileUtils.readLines(initFile.getPath());
		for (String line : data) {
			if (line.toLowerCase().startsWith(FILE_PORT_KEY)) {
				line = line.substring(line.indexOf(":") + 1);
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
		this.requests = new ArrayDeque<MatrixRequest>();
		this.requestsReaderThread();
		this.requestsProcesserThread();
	}

	private void requestsReaderThread() {
		new Thread(() -> {
			while(true) {
				try {
					
					//wait for any client to connect
					Socket client = this.server.accept();
					
					//handles many new client connections on threads
					this.handleNewRequest(client);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void requestsProcesserThread() {
		new Thread(() -> {
			while(true) {
				MatrixRequest process = null;
				synchronized(REQUESTS_LOCK) {
					process = this.requests.remove();
				}
				process.processToClient();
			}
		}).start();
	}
	
	private void handleNewRequest(Socket client) {
		new Thread(() -> {
			
			try {
				///TODO write to client, and wait for matrix data
				client.getOutputStream();
				
				///TODO read data, and then process into matricies
				
				Matrix[] deserialized = null;
				
				synchronized(REQUESTS_LOCK) {
					this.requests.add(new MatrixRequest(client,deserialized));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

}
