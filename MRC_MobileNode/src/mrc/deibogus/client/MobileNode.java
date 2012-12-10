package mrc.deibogus.client;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import mrc.deibogus.data.AgentAdvertisementMessage;
import mrc.deibogus.data.MobileNodeData;
import mrc.deibogus.data.Pacote;
import mrc.deibogus.data.Response;

public class MobileNode extends Thread {

	//TODO mapear o porto para um IP
	public static int homeAgentPort = 7000; //HOMEAGENTADDRESS 
	public static int foreignAgentPort = 6000;

	private String currentNetwork = "hn";

	private final String homeAgentIP = "192.168.1.17"; //port = 7000
	private final String foreignAgentIP = "192.168.1.16"; //port = 6000 

	private final String myIP = "192.168.169.1";
	private final String myMAC = "00:23:6c:8f:73:ab";
	public int LIFETIME = 10;

	private final String destinationIP = "192.168.169.2";

	private boolean logged = false;

	public Socket s;
	public ObjectOutputStream out;
	public ObjectInputStream in;

	static ClientResponse cc; //Pq static?

	private InputStreamReader text_in = new InputStreamReader(System.in);
	private BufferedReader text_buf = new BufferedReader(text_in);

	private static Timer timer  = new Timer();
	private int ttl = LIFETIME;

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
			System.out.println("4 - Desconectar");
			System.out.println("Option: ");
			try {
				int op = Integer.parseInt(text_buf.readLine());

				switch(op) {
				case 1: mb.changeNetwork(); break;
				case 2: mb.sendPacket(); break;
				case 3: mb.printNetwork();break;
				//case 4: break; //TODO
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void  printNetwork(){
		System.out.println("O Mobile Node " + myIP + " esta na rede " + currentNetwork);
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

				System.out.println("MB["+myIP+"] > Conectado a nova rede");

				return true;
			} catch (UnknownHostException e) {
				//System.out.println(">> Warning: Server not available! Working on it...");
			} catch (IOException e) {
				//System.out.println(">> Warning: Server not available! Working on it...");
			}
		} while(retry < 5);
		System.err.println("MB["+myIP+"] > Agent nao esta disponivel.");
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

			cc = new ClientResponse(in, out, myIP, this, timer);
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
		data.setLifeTimeLeft(LIFETIME);
		data.setType("ConnectMN");

		System.out.println("MB["+myIP+"] > a conectar ao FA");
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

			cc = new ClientResponse(in, out, myIP, this, timer);
			cc.start();
			System.out.println("MB["+myIP+"] > Conectado a rede.");

			return true;
		} else {
			System.err.println("MB["+myIP+"] > Erro ao conectar na rede.");
		}

		return false;
	}

	public synchronized boolean changeNetwork() {

		try {
			this.s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(currentNetwork.equals("hn")){
			if(this.connect(foreignAgentPort)) {
				this.conectarRedeFA();
				
				timer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						ttl--;
						System.out.println("MB["+myIP+"] > TTL: "+ttl);
						if(ttl == 0) {
							sendSolicitationMessage();
							
							try {
								timer.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							ttl = LIFETIME;

							System.out.println("MB["+myIP+"] > Solicitation message enviada ao FA");
						}
					}
				}, 2000, 2000);
			}
			this.currentNetwork = "fn";
		}
		else{
			if(this.connect(homeAgentPort)) {
				this.conectarRede();

				timer.cancel();
			}
			this.currentNetwork = "hn";
		}

		return false;
	}

	private void sendSolicitationMessage() {
		MobileNodeData data = new MobileNodeData();
		data.setIP(myIP);
		data.setMacAddress(myMAC);
		data.setHomeAgentAddress(homeAgentIP);
		data.setCareOfAddress(foreignAgentIP);
		data.setLifeTimeLeft(LIFETIME);
		data.setType("ConnectMN");

		System.out.println("MB["+myIP+"] > a enviar solicitation message ao FA");
		try {
			out.writeObject(data);
		} catch (IOException e) {
			System.err.println("MB["+myIP+"] > Erro ao enviar solicitation message.");
		} 
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

	private MobileNode mb;
	private Timer timer;
	
	ClientResponse() {

	}

	ClientResponse(ObjectInputStream in, ObjectOutputStream out, String myIP, MobileNode mb, Timer timer) {
		this.in = in;
		this.out = out;

		this.myIP = myIP;
		this.mb = mb;
		this.timer = timer;
	}

	public void run() {

		while(logged) {
			try {
				//System.out.println("MB["+myIP+"] > Waiting for response...");
				Object response = in.readObject();

				if(response instanceof Pacote) {
					System.out.println("MB["+myIP+"] > Pacote recebido.");
				}

				if(response instanceof AgentAdvertisementMessage) {
					System.out.println("MB["+myIP+"] > Advertisement message recebida do Agente " + response);
				}
				
				if(response instanceof Response) {
					synchronized (mb) {
						mb.LIFETIME = ((Response) response).getTTL();
						
						timer.notify();
					}
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

}
