package mrc.deibogus.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import mrc.deibogus.homeagent.HomeAgent;

public class Server {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		int port = 7000; //HomeAgent Address: 192.168.1.17
		
		Socket socket;
		ObjectInputStream in;
		ObjectOutputStream out;

		System.out.println(">> Home Agent listening on port "+port+"...");
		ServerSocket listenSocket = new ServerSocket(port);

		HomeAgent homeAgent = new HomeAgent("192.168.1.17");

		while(true) {
			socket = listenSocket.accept(); //bloqueante

			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			
			new Connection(homeAgent, socket, in, out);
		}

	}
}
