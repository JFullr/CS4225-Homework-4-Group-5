package utils.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
	
	private String address;
	private int port;
	private int timeout;
	private Socket client;
	
	private boolean hasIn;
	private boolean hasOut;
	
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	public Client(String address, int port) {
		
		this.initClient(address,port,0);
		
	}
	
	public Client(String address, int port, int timeoutInSeconds) {
		
		this.initClient(address,port,timeoutInSeconds);
		
	}
	
	private void initClient(String address, int port, int timeoutInSeconds) {
		this.input = null;
		this.output = null;
		this.address = address;
		this.port = port;
		this.hasIn = false;
		this.hasOut = false;
		this.timeout = timeoutInSeconds*1000;
	}
	
	public Client(Socket initialized) throws IOException {
		
		this.client = initialized;
		
		try {
			this.input = new ObjectInputStream(this.client.getInputStream());
			this.hasIn = true;
		}catch(java.lang.NullPointerException e) {e.printStackTrace();}
		try {
			this.output = new ObjectOutputStream(this.client.getOutputStream());
			this.hasOut = true;
		}catch(java.lang.NullPointerException e) {}
		
	}
	
	public void connect(Object initData) throws IOException {
		
		this.client = new Socket(this.address, this.port);
		this.client.setSoTimeout(this.timeout);
		
		this.output = new ObjectOutputStream(this.client.getOutputStream());
		if(initData == null) {
			this.output.writeObject(0);
		} else {
			this.output.writeObject(initData);
		}
		this.output.flush();
		
		this.input = new ObjectInputStream(this.client.getInputStream());
		
	}
	
	public void sendData(Object data) throws IOException {
		
		if(!this.hasOut) {
			return;
		}
		
		this.output.writeObject(data);
		this.output.flush();
		
	}
	
	public Object readBlocking() throws ClassNotFoundException, IOException {
		
		if(!this.hasIn) {
			return null;
		}
		
		return this.input.readObject();
		
	}

}
