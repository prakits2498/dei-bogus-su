package mrc.deibogus.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		int port = 7000;
		Socket clientSocket;

		ObjectInputStream inObject;
		ObjectOutputStream outObject;

		System.out.println(">> Listening on port "+port+"...");
		ServerSocket listenSocket = new ServerSocket(port);

		//new HomeAgent();

		while(true) {			
			clientSocket = listenSocket.accept(); //bloqueante

			outObject = new ObjectOutputStream(clientSocket.getOutputStream());
			outObject.flush();
			inObject = new ObjectInputStream(clientSocket.getInputStream());

			new Connection(clientSocket, inObject, outObject);
		}

	}
}
