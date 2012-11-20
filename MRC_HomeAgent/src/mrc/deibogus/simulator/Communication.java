package mrc.deibogus.simulator;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Communication {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	private String address;
	
	public Communication(String address, ObjectInputStream in, ObjectOutputStream out, Socket socket) {
		this.address = address;
		this.in = in;
		this.out = out;
		this.socket = socket;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
}
