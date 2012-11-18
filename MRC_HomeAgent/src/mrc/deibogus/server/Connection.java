package mrc.deibogus.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Connection extends Thread {

	private ObjectInputStream inObject;
	private ObjectOutputStream outObject;
	private Socket clientSocket;
	private int clientID;
	private boolean logged;

	public Connection(Socket clientSocket, ObjectInputStream in, ObjectOutputStream out) throws ClassNotFoundException {
		this.clientSocket = clientSocket;
		this.outObject = out;
		this.inObject = in;
		this.start();
	}
	
	public void run() {
		while(logged) {
			try {
				outObject.flush();

				//read request from client
				//Request new_request = (Request) inObject.readObject();
				
				/*if(new_request.getType().equals("requestPOIs")) {
					System.out.println("> Request received from user "+this.clientID);
					//Context cont = (Context) new_request;
					
					synchronized (agent) {
						if(agent.getState().name().equals("NEW")) {
							agent.start();
							agent.setContext(cont);
						} else if(agent.getState().name().equals("WAITING")) {
							agent.setContext(cont);
							agent.notify();
						}
					}
					
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					
					//send response to client
					outObject.writeObject(r);
					outObject.flush();
				}*/
				
			} catch(SocketException e2) {
				System.err.println("Socket Closed!");
				logged = false;
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
