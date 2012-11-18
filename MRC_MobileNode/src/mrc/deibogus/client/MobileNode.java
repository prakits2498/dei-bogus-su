package mrc.deibogus.client;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MobileNode {

	private boolean logged = false;
	private int myID;
	public static int homeAgentPort = 7000; //HOMEAGENTADDRESS
	
	public Socket s;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	
	static ClientResponse cc;
	
	public static void main (String args[]) {
		System.out.println("MobileNode");
		MobileNode mb = new MobileNode();

		mb.connect(homeAgentPort);
		//mb.mainMenu(mb);
	}
	
	public synchronized boolean connect(int port) {
		int retry = 0;

		do{
			retry++;
			try {
				//System.out.println(">> Connecting to server! Attempting "+retry+"...");

				s = new Socket("localhost", port);
				out = new ObjectOutputStream( s.getOutputStream());
				out.flush();
				in = new ObjectInputStream( s.getInputStream());

				System.out.println(">> Connected to server!");
				
				this.logged = true;
				this.myID = 1; //TODO
				cc = new ClientResponse(in,out,this.myID);
				cc.start();
				
				return true;
			} catch (UnknownHostException e) {
				//System.out.println(">> Warning: Server not available! Working on it...");
			} catch (IOException e) {
				//System.out.println(">> Warning: Server not available! Working on it...");
			}
		} while(retry < 5);
		System.out.println("Server not available! Please try again later...");
		return false;
	}
	
	
}


class ClientResponse extends Thread {
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private static boolean logged = true;
	private int myID;
	private FileWriter logger;

	ClientResponse() {

	}

	ClientResponse(ObjectInputStream in, ObjectOutputStream out, int myID) {
		this.in = in;
		this.out = out;
		this.myID = myID;
	}

	public void run() {
		//Response new_response = null;

		while(logged) {
			//try {
				System.out.println("Waiting for response...");
				//new_response = (Response) in.readObject();
			/*} catch (IOException e) {
				//e.printStackTrace();
				System.err.println("Socket closed!");
				logged = false;
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}*/

			/*if(new_response.getType().equals("recommendation")) {
				
			}*/
			
		}
	}

	private String currentTime() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String dateNow = formatter.format(currentDate.getTime());
		//System.out.println("Now the date is :=>  " + dateNow);
		return dateNow;
	}

}
