package network;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;

import matrix.Matrix;
import utils.FileUtils;

/**
 * The Class MatrixServer.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class MatrixServer {

	private static final String FILE_PORT_KEY = "port";
	private static final Object REQUESTS_LOCK = new Object();
	
	private volatile Queue<MatrixRequest> requests;
	private int port;
	private ServerSocket server;

	/**
	 * Instantiates a new matrix server.
	 *
	 * @param initFile the init file
	 * @throws IOException Signals that an I/O exception has occurred on file read failure.
	 */
	public MatrixServer(File initFile) throws IOException {
		
		String[] data = FileUtils.readLines(initFile.getPath());
		for (String line : data) {
			if (line.toLowerCase().startsWith(FILE_PORT_KEY)) {
				line = line.substring(line.indexOf(":") + 1);
				this.port = Integer.parseInt(line.trim());
				break;
			}
		}

	}

	/**
	 * Instantiates a new matrix server.
	 *
	 * @param port the port
	 */
	public MatrixServer(int port){
		this.port = port;
	}

	/**
	 * Starts the matrix server
	 *
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void startServer() throws IOException {
		
		this.server = new ServerSocket(this.port);
		this.requests = new ArrayDeque<MatrixRequest>();
		this.requestsReaderThread();
		this.requestsProcesserThread();
		
	}

	private void requestsReaderThread() {
		new Thread(() -> {
			while (true) {
				try {

					Socket client = this.server.accept();
					this.handleNewRequest(client);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void requestsProcesserThread() {
		new Thread(() -> {
			while (true) {
				MatrixRequest process = null;
				synchronized (REQUESTS_LOCK) {
					while(this.requests.isEmpty()) {
						try {
							REQUESTS_LOCK.wait();
						} catch (InterruptedException e) {e.printStackTrace();}
					}
					process = this.requests.remove();
				}
				try {
					process.processToClient();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void handleNewRequest(Socket client) {
		new Thread(() -> {

			try {
				
				ObjectInputStream incoming = new ObjectInputStream(client.getInputStream());

				Matrix[] deserialized = (Matrix[])incoming.readObject();

				synchronized (REQUESTS_LOCK) {
					this.requests.add(new MatrixRequest(client, deserialized));
					REQUESTS_LOCK.notifyAll();
				}

			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}).start();
	}

}
