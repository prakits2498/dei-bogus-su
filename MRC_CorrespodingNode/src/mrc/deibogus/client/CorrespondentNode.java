package mrc.deibogus.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import mrc.deibogus.data.CorrespondentNodeData;
import mrc.deibogus.data.Pacote;
import mrc.deibogus.data.Response;

public class CorrespondentNode extends Thread {

	//TODO mapear o porto para um IP
	public static int homeAgentPort = 7000; //HOMEAGENTADDRESS 
	private final String homeAgentIP = "192.168.1.17"; //port = 7000

	private final String myIP = "192.168.169.2";
	private final String myMAC = "00:23:6c:8f:73:ac";

	private final String destinationIP = "192.168.169.1";

	private boolean logged = false;

	public Socket s;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	
	private InputStreamReader text_in = new InputStreamReader(System.in);
	private BufferedReader text_buf = new BufferedReader(text_in);

	static ClientResponse cc;

	
	public static void main (String args[]) {
		CorrespondentNode mb = new CorrespondentNode();

		System.out.println("CorrespondentNode Started ["+mb.myIP+"]");

		if(mb.connect(homeAgentPort)) {
			mb.conectarRede();

			mb.actions(mb);
		}
	}

	public synchronized void actions(CorrespondentNode mb) {
		while(logged) {
			System.out.println("1 - Enviar pacote");
			System.out.println("Option: ");
			try {
				int op = Integer.parseInt(text_buf.readLine());

				switch(op) {
				case 1: mb.sendPacket(); break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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

				System.out.println("CN["+myIP+"] > Conectado ao HA " + homeAgentIP);

				return true;
			} catch (UnknownHostException e) {
				System.out.println(">> Warning: Server not available! Working on it...");
			} catch (IOException e) {
				System.out.println(">> Warning: Server not available! Working on it...");
			}
		} while(retry < 5);
		System.err.println("CN["+myIP+"] > HomeAgent " + homeAgentIP + " nao esta disponivel.");
		return false;
	}


	public synchronized boolean conectarRede() {
		Response resp = null;

		CorrespondentNodeData data = new CorrespondentNodeData();
		data.setIP(myIP);
		data.setType("ConnectCN");

		try {
			
			out.writeObject(data);
			resp = (Response) in.readObject();
			
		} catch (IOException e) {
			System.err.println("CN["+myIP+"] > Erro ao conectar na rede.");
		} catch (ClassNotFoundException e) {
			System.err.println("CN["+myIP+"] > Erro ao conectar na rede.");
		}

		if (resp.isResponse()) {
			this.logged = true;

			cc = new ClientResponse(in, out, myIP);
			cc.start();
			
			System.out.println("CN["+myIP+"] > guardado pelo HA[" + homeAgentIP + "].");

			return true;
		} else {
			System.err.println("CN["+myIP+"] > Erro ao conectar na rede.");
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

		System.out.println("CN["+myIP+"] > Pacote vai ser enviado para MN[" + destinationIP + "].");

		try {
			out.writeObject(packet);
		} catch (IOException e) {
			System.err.println("CN["+myIP+"] > Erro ao enviar pacote para [" + destinationIP + "].");
		}
	}

}


class ClientResponse extends Thread {
	private String myIP;

	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private static boolean logged = true;

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
				System.out.println("CN["+myIP+"] > ClientResponse Waiting for messages...");
				Object response = in.readObject();

				if(response instanceof Pacote) {
					System.out.println("CN["+myIP+"] > Pacote recebido do Mobile Node [" + ((Pacote) response).getSource() + "].");
					System.out.println("CN["+myIP+"] > Mensagem: " + ((Pacote) response).getData());
				}

			} catch (IOException e) {
				//e.printStackTrace();
				System.err.println("CN["+myIP+"] > Socket closed!");
				logged = false;
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
