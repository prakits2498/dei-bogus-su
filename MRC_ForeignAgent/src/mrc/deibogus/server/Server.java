package mrc.deibogus.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import mrc.deibogus.data.MobileNodeData;
import mrc.deibogus.data.Pacote;
import mrc.deibogus.data.Response;
import mrc.deibogus.foreignagent.ForeignAgent;
import mrc.deibogus.foreignagent.ForeignAgent;
import mrc.deibogus.simulator.Communication;

public class Server {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		int port = 6000; //ForeignAgent Address: 192.168.1.16
		int portHA = 7000; // HomeAgent Address: 192.168.1.17
		String myIP = "192.168.1.16";
		String haIP = "192.168.1.17";
		
		ObjectInputStream in;
		ObjectOutputStream out;
		
		ForeignAgent foreignAgent;

		System.out.println(">> Foreign Agent connecting to Home Agent on port "+portHA+"...");
		
		Socket s = new Socket("localhost", portHA);
		out = new ObjectOutputStream( s.getOutputStream());
		in = new ObjectInputStream( s.getInputStream());
		out.flush();
		
		Communication communication = new Communication(haIP, in, out, s);
		foreignAgent = new ForeignAgent(myIP, haIP);
		foreignAgent.registoHA(in, out,communication);

		new Connection(foreignAgent, s, in, out);
		
		ServerSocket listenSocket = new ServerSocket(port);
		while(true) {
			
			Socket socket = listenSocket.accept(); //bloqueante
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			
			new Connection(foreignAgent, socket, in, out);
		}

	}
}
