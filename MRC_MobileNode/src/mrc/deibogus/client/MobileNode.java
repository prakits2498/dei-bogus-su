package mrc.deibogus.client;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import mrc.deibogus.data.MobileNodeData;
import mrc.deibogus.data.Pacote;
import mrc.deibogus.data.Response;

public class MobileNode extends Thread {

	//TODO mapear o porto para um IP
	public static int homeAgentPort = 7000; //HOMEAGENTADDRESS 
	public static int foreignAgentPort = 6000;
	
	private static String currentNetwork = "hn";

	private final String homeAgentIP = "192.168.1.17"; //port = 7000
	private final String foreignAgentIP = "192.168.1.16"; //port = 6000 

	private final String myIP = "192.168.169.1";
	private final String myMAC = "00:23:6c:8f:73:ab";

	private final String destinationIP = "192.168.169.2";

	private boolean logged = false;

	public Socket s;
	public ObjectOutputStream out;
	public ObjectInputStream in;

	static ClientResponse cc; //Pq static?

	private InputStreamReader text_in = new InputStreamReader(System.in);
	private BufferedReader text_buf = new BufferedReader(text_in);

	public static void main (String args[]) {
		MobileNode mb = new MobileNode();

		System.out.println("MobileNode Started ["+mb.myIP+"]");

		if(mb.connect(homeAgentPort)) {
			mb.conectarRede();

			mb.actions(mb);
		}
	}

	public synchronized void actions(MobileNode mb) {
		while(logged) {
			System.out.println("1 - Mudar de rede");
			System.out.println("2 - Enviar pacote");
			System.out.println("3 - Mostrar rede");
			System.out.println("Option: ");
			try {
				int op = Integer.parseInt(text_buf.readLine());

				switch(op) {
				case 1: mb.changeNetwork(); break;
				case 2: mb.sendPacket(); break;
				case 3: mb.printNetwork();break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void  printNetwork(){
		System.out.println("O Mobile Node " + myIP + " está na rede " + currentNetwork);
	}

	public synchronized boolean connect(int port) {
		int retry = 0;

		do{
			retry++;
			try {
				s = new Socket("localhost", port);
				out = new ObjectOutputStream( s.getOutputStream());
				out.flush();
				in = new ObjectInputStream( s.getInputStream());

				System.out.println("MB["+myIP+"] > Conectado ao HA");

				return true;
			} catch (UnknownHostException e) {
				//System.out.println(">> Warning: Server not available! Working on it...");
			} catch (IOException e) {
				//System.out.println(">> Warning: Server not available! Working on it...");
			}
		} while(retry < 5);
		System.err.println("MB["+myIP+"] > HomeAgent nao esta disponivel.");
		return false;
	}


	public synchronized boolean conectarRede() {
		Response resp = null;

		MobileNodeData data = new MobileNodeData();
		data.setIP(myIP);
		data.setMacAddress(myMAC);

		data.setType("ConnectMN");

		try {
			out.writeObject(data);

			resp = (Response) in.readObject();
		} catch (IOException e) {
			System.err.println("MB["+myIP+"] > Erro ao conectar na rede.");
		} catch (ClassNotFoundException e) {
			System.err.println("MB["+myIP+"] > Erro ao conectar na rede.");
		}

		if (resp.isResponse()) {
			this.logged = true;

			cc = new ClientResponse(in, out, myIP);
			cc.start();
			System.out.println("MB["+myIP+"] > Conectado a rede.");

			return true;
		} else {
			System.err.println("MB["+myIP+"] > Erro ao conectar na rede.");
		}

		return false;
	}
	
	public synchronized boolean conectarRedeFA() {
		Response resp = null;

		MobileNodeData data = new MobileNodeData();
		data.setIP(myIP);
		data.setMacAddress(myMAC);
		data.setHomeAgentAddress(homeAgentIP);
		data.setCareOfAddress(foreignAgentIP);

		data.setType("ConnectMN");

		try {
			out.writeObject(data);

			resp = (Response) in.readObject();
		} catch (IOException e) {
			System.err.println("MB["+myIP+"] > Erro ao conectar na rede.");
		} catch (ClassNotFoundException e) {
			System.err.println("MB["+myIP+"] > Erro ao conectar na rede.");
		}

		if (resp.isResponse()) {
			this.logged = true;

			cc = new ClientResponse(in, out, myIP);
			cc.start();
			System.out.println("MB["+myIP+"] > Conectado a rede.");

			return true;
		} else {
			System.err.println("MB["+myIP+"] > Erro ao conectar na rede.");
		}

		return false;
	}

	public synchronized boolean changeNetwork() {
		//TODO faz logout do HA
		
		//TODO conecta-se ao FA
		try {
			this.s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(currentNetwork.equals("hn")){
			if(this.connect(foreignAgentPort)) {
				this.conectarRedeFA();
			}
			this.currentNetwork = "fn";
		}
		else{
			if(this.connect(homeAgentPort)) {
				this.conectarRede();
			}
			this.currentNetwork = "hn";
		}

		return false;
	}

	public synchronized void sendPacket() {
		Pacote packet = new Pacote();
		packet.setSource(myIP); //IP ou MAC?
		packet.setDestination(destinationIP);
		packet.setProtocol("TCP");
		packet.setData("Pacote de "+myIP+" para "+destinationIP);

		packet.setType("pacoteCNtoMN");

		System.out.println("MB["+myIP+"] > Pacote enviado");

		try {
			out.writeObject(packet);
		} catch (IOException e) {
			System.err.println("MB["+myIP+"] > Erro ao enviar pacote.");
		}
	}

}


class ClientResponse extends Thread {
	private String myIP;

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private static boolean logged = true;
	private FileWriter logger;

	ClientResponse() {

	}

	ClientResponse(ObjectInputStream in, ObjectOutputStream out, String myIP) {
		this.in = in;
		this.out = out;

		this.myIP = myIP;
	}

	public void run() {

		while(logged) {
			try {
				//System.out.println("MB["+myIP+"] > Waiting for response...");
				Object response = in.readObject();

				if(response instanceof Pacote) {
					System.out.println("MB["+myIP+"] > Pacote recebido.");
				}

			} catch (IOException e) {
				//e.printStackTrace();
				System.err.println("MB["+myIP+"] > Socket closed!");
				logged = false;
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
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
