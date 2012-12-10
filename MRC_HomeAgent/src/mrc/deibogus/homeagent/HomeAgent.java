package mrc.deibogus.homeagent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import mrc.deibogus.data.AgentAdvertisementMessage;
import mrc.deibogus.data.HomeAgentData;
import mrc.deibogus.data.MobileNodeData;
import mrc.deibogus.data.Pacote;
import mrc.deibogus.data.PacoteEncapsulado;
import mrc.deibogus.data.Response;
import mrc.deibogus.simulator.Communication;

public class HomeAgent extends Thread {
	private String myIP;
	private boolean connected;

	private HashMap<String, Communication> FAsockets = new HashMap<String, Communication>();
	private HashMap<String,	Communication> nodesSockets = new HashMap<String, Communication>();

	private HashMap<String, MobileNodeData> mobileNodes = new HashMap<String, MobileNodeData>(); //nodes que se conectaram na HN
	//private HashMap<String, CorrespondentNodeData> correspondentNodes = new HashMap<String, CorrespondentNodeData>();

	//MBT: <home_address, care_of_address, association_lifetime>
	private HashMap<String, HomeAgentData> mobilityBindingTable = new HashMap<String, HomeAgentData>();

	private HashMap<String, String> networkNodes = new HashMap<String, String>(); //IP - MAC -> nodes da HN

	public HomeAgent(String myIP) {
		this.myIP = myIP;
		this.connected = true;

		this.loadNetworkNodes();
		this.broadcast(true);

		this.start();
	}

	public void run() {
		System.out.println("HomeAgent Started ["+myIP+"]");
		while(connected) {
			synchronized(this) {
				try {
					this.wait(10000);
					temporizadorTTL();
					broadcast(false);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void addFA(MobileNodeData data, Communication communication) {
		this.FAsockets.put(data.getIP(), communication);
	}

	public void addMN(MobileNodeData data, Communication communication) {
		this.nodesSockets.put(data.getIP(), communication);
		this.mobileNodes.put(data.getIP(), data);
	}

	private void loadNetworkNodes() {
		try {
			FileReader fr = new FileReader("data/networkNodes.txt");
			BufferedReader reader = new BufferedReader(fr);

			String line;
			while ((line = reader.readLine()) != null) {
				String[] l = line.split(" ");
				networkNodes.put(l[0], l[1]);
			}
			reader.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//1 - Recebe pacote vindo de CN destinado a MN
	public void sendPacket(Pacote packet) {
		/*
		 * Consulta MBT
		 * Se MN nao esta na MBT entao
		 *   Entrega pacote ao MN
		 * Senao
		 *   Determina CoA
		 *   Encapsula Pacote
		 *   Envia pacote para CoA
		 */

		System.out.println("HA["+myIP+"] > Pacote recebido de "+packet.getSource()+" para "+packet.getDestination());

		if(!mobilityBindingTable.containsKey(packet.getDestination())) {
			if(nodesSockets.containsKey(packet.getDestination())) {
				Communication mb = nodesSockets.get(packet.getDestination());
				System.out.println("HA["+myIP+"] > A enviar pacote para MN["+packet.getDestination()+"]");

				try {
					mb.getOut().writeObject(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} else {
			HomeAgentData d = mobilityBindingTable.get(packet.getDestination());
			PacoteEncapsulado packetE = this.encapsulaPacote(packet);

			try {
				FAsockets.get(d.getCareOfAddress()).getOut().writeObject(packetE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//2 - Recebe pacote encapsulado, vindo de CoA
	public void receivePacket(String coA, PacoteEncapsulado packetE) {
		/*
		 * Consulta MBT
		 * Se CoA nao existe entao
		 *   ignora pacote
		 * Senao 
		 *   desencapsula pacote
		 *   envia pacote para o destinatario (CN)
		 */

		if(!existCoA(coA)) {
			System.out.println("HA["+myIP+"] > CoA nao existe - pacote ignorado");
		} else {
			Pacote packet = this.desencapsulaPacote(packetE);

			try {
				System.out.println("HA["+myIP+"] > A enviar pacote para CN["+packet.getDestination()+"]");
				nodesSockets.get(packet.getDestination()).getOut().writeObject(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	//3 - Recebe pedido de registo, vindo de FA
	public void registoFA(MobileNodeData mb) {
		/*
		 * Consulta MBT
		 * Se MN ja existe na MBT entao
		 *   Actualiza entrada na MBT (CoA, TTL)
		 * Senao
		 *   Verifica credenciais
		 *   Cria entrada na MBT
		 *   Envia Confirmacao (pos/neg) ao FA
		 */

		int lifetime = 30;
		Response resp = new Response();
		resp.setType("RespRegistoFA");
		resp.setIP(mb.getIP());

		System.out.println("HA["+myIP+"] > Recebido pedido de registo de FA");
		
		if(mb.getLifeTimeLeft() < 50) {
			lifetime = mb.getLifeTimeLeft();
		} else {
			lifetime = 30;
		}
		
		resp.setTTL(lifetime);

		if(mobilityBindingTable.containsKey(mb.getIP())) {
			HomeAgentData data = mobilityBindingTable.get(mb.getIP());
			data.setCareOfAddress(mb.getCareOfAddress());
			data.setLifeTime(lifetime);
			mobilityBindingTable.put(mb.getIP(), data);

			resp.setResponse(true);

			System.out.println("HA["+myIP+"] > MN ja existe na MBT - registo actualizado");
		} else {

			if(!mobileNodes.containsKey(mb.getIP())) {
				resp.setResponse(false);

				System.out.println("HA["+myIP+"] > MN nao pertence a HN");
			} else {
				HomeAgentData data = new HomeAgentData();
				data.setCareOfAddress(mb.getCareOfAddress());
				data.setLifeTime(lifetime);
				mobilityBindingTable.put(mb.getIP(), data);

				resp.setResponse(true);

				System.out.println("HA["+myIP+"] > MN registado");
			}

		}
		
		try {
			FAsockets.get(mb.getCareOfAddress()).getOut().writeObject(resp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//4 - Recebe pedido de cancelamento de registo
	public void cancelamentoRegisto(MobileNodeData mb) {
		/*
		 * Consulta MBT
		 * Se MN nao existe na MBT entao
		 *   ignora pedido
		 * Senao
		 *   verifica credenciais
		 *   elimina entrada correspondente na MBT
		 */

		if(!mobilityBindingTable.containsKey(mb.getIP())) {
			System.out.println("HA["+myIP+"] > MN nao existe na MBT - pedido ignorado");
		} else {
			if(networkNodes.containsKey(mb.getIP())) {
				mobilityBindingTable.remove(mb.getIP());
				
				System.out.println("HA["+myIP+"] > registo cancelado");
			} else {
				System.out.println("HA["+myIP+"] > MN nao pertence a HN");
			}

		}
	}

	//5 - Temporizador de TTL
	public void temporizadorTTL() {
		/*
		 * Para cada entrada da MBT
		 *   decrementa TTL
		 *   Se TTL chegou a zero
		 *     elimina entrada correspondente da MBT
		 */

		System.out.println("HA["+myIP+"] > a enviar TTL");

		HashMap<String, HomeAgentData> mbtAux = new HashMap<String, HomeAgentData>(mobilityBindingTable);
		for(String key : mbtAux.keySet()) {
			HomeAgentData data = mbtAux.get(key);

			int ttl = data.getLifeTime();
			ttl--;

			if(ttl == 0) {
				mobilityBindingTable.remove(key);
				System.out.println("HA["+myIP+"] > TTL: "+key+" eliminado da MBT");
			} else {
				data.setLifeTime(ttl);
				mobilityBindingTable.put(key, data);
			}

		}
	}

	//6 - Arranque, ou temporizador de anuncio de HA/FA
	public void broadcast(boolean arranque) {
		/*
		 * Se Arranque entao
		 *   Sequence number = [0...255]
		 * Senao
		 *   Sequence number = [256...65635]
		 *   Incrementa Sequence number
		 * Envia anuncio por broadcast
		 */

		int sequenceNumber = 0;
		if(arranque) {
			sequenceNumber = generateRandomInteger(0, 255);
		} else {
			sequenceNumber = generateRandomInteger(256, 65635);
		}

		sendAdvertisementMessage(sequenceNumber);
	}

	private void sendAdvertisementMessage(int sequenceNumber) {
		System.out.println("HA["+myIP+"] > A enviar advertisement message");
		
		AgentAdvertisementMessage advertisementMessage = new AgentAdvertisementMessage(sequenceNumber);
		advertisementMessage.setHomeAgent(true);
		
		for(String key : mobilityBindingTable.keySet()) {
			HomeAgentData data = mobilityBindingTable.get(key);
			advertisementMessage.addCareOfAddress(data.getCareOfAddress());
		}
		
		HashMap<String, Communication> aux = new HashMap<String, Communication>(nodesSockets);
		for(String ip : aux.keySet()) {
			try {
				Communication c = aux.get(ip);
				c.getOut().writeObject(advertisementMessage);
			} catch (IOException e) {
				//e.printStackTrace();
				System.err.println("HA["+myIP+"] > socket removido");
				nodesSockets.remove(ip);
			}
		}
	}

	private int generateRandomInteger(int aStart, int aEnd) {
		double randomInt = aStart + (Math.random() * (aEnd - aStart));
		return (int) randomInt;
	}

	private PacoteEncapsulado encapsulaPacote(Pacote packet) {
		System.out.println("HA["+myIP+"] > A encapsular pacote");

		HomeAgentData d = mobilityBindingTable.get(packet.getDestination());
		PacoteEncapsulado packetE = new PacoteEncapsulado();
		packetE.setSource(myIP);
		packetE.setDestination(d.getCareOfAddress());
		packetE.setProtocol("IP in IP");
		packetE.setData(packet);
		packetE.setType("pacoteEncapsulado");

		return packetE;
	}

	private Pacote desencapsulaPacote(PacoteEncapsulado packetE) {
		System.out.println("HA["+myIP+"] > A desencapsular pacote");
		return packetE.getData();
	}

	private boolean existCoA(String coA) {
		for(String key : mobilityBindingTable.keySet()) {
			HomeAgentData data = mobilityBindingTable.get(key);
			if(data.getCareOfAddress().equals(coA))
				return true;
		}

		return false;
	}

	public void addSocket(String IP, Communication com) {
		this.nodesSockets.put(IP, com);
	}
	
	public void removeSocket(Socket socket) {
		HashMap<String, Communication> aux = new HashMap<String, Communication>(nodesSockets);
		for(String ip : aux.keySet()) {
			Communication c = aux.get(ip);
			if(c.getSocket() == socket) {
				this.nodesSockets.remove(ip);
				break;
			}
		}
		
	}

}
