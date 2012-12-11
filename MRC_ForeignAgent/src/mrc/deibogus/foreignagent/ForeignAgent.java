package mrc.deibogus.foreignagent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import mrc.deibogus.data.AgentAdvertisementMessage;
import mrc.deibogus.data.MobileNodeData;
import mrc.deibogus.data.Pacote;
import mrc.deibogus.data.PacoteEncapsulado;
import mrc.deibogus.data.Response;
import mrc.deibogus.simulator.Communication;


public class ForeignAgent extends Thread{

	private String myIP;
	private String haIP;
	private String myMAC;
	private boolean connected;

	private HashMap<String, Communication> HAsockets = new HashMap<String, Communication>();
	private HashMap<String,	Communication> nodesSockets = new HashMap<String, Communication>();
	private HashMap<String, MobileNodeData> visitorListTable = new HashMap<String, MobileNodeData>();

	public ForeignAgent(String myIP,String haIP) {
		this.haIP = haIP;
		this.myIP = myIP;
		this.connected = true;
		broadcast(true);
		this.start();
	}

	public void run() {
		System.out.println("ForeignAgent Started ["+myIP+"]");
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

	//Registo deste FA num HA
	public void registoHA(ObjectInputStream in, ObjectOutputStream out, Communication com){
		MobileNodeData data = new MobileNodeData();
		data.setIP(myIP);
		data.setMacAddress(myMAC);
		data.setType("ConnectFA");
		//System.out.println("Pedido de registo do FA "+myIP+" para o HA "+haIP);

		try {
			out.writeObject(data);
			Response response = (Response) in.readObject();

			if(response.isResponse()){
				this.addHA(haIP,com);
				//System.out.println("Registo do FA "+myIP+" noo HA "+haIP + " efectuado com sucesso.");
			}
			//else
			//System.out.println("Registo do FA "+myIP+" noo HA "+haIP + " efectuado sem sucesso.");

		} catch (IOException e) {
			System.err.println("FA["+myIP+"] > Erro ao registar-se no HA " + haIP);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addHA(String IP, Communication communication) {
		this.HAsockets.put(IP, communication);
	}

	//1 - Recebe pedido de registo
	public void addMN(MobileNodeData data, Communication communication) {
		/*
		 * Consulta VL
		 * Se MN existe na VL ent‹o
		 *    Actualiza entrada (TTL)
		 * Sen‹o
		 *    Cria entrada tempor‡ria
		 * Envia pedido de registo ao HA
		 */

		if(visitorListTable.containsKey(data.getIP())){
			MobileNodeData temp = visitorListTable.get(data.getIP());
			temp.setLifeTimeLeft(data.getLifeTimeLeft());
			visitorListTable.put(data.getIP(), temp);

			data.setType("pedidoRegistoFA");

			System.out.println("FA["+myIP+"] > Pedido de registo do MN[" + data.getIP() + "] - TTL actualizado");
		}
		else
		{
			this.nodesSockets.put(data.getIP(), communication);
			this.visitorListTable.put(data.getIP(), data);

			data.setType("pedidoRegistoFA");
			System.out.println("FA["+myIP+"] > Pedido de registo do MN[" + data.getIP() + "] - criada entrada tempor‡ria");
		}
		
		Communication com = HAsockets.get(data.getHomeAgentAddress());
		try {
			System.out.println("FA["+myIP+"] > Pedido de registo do MN[" + data.getIP() + "] enviado ao HA["+ data.getHomeAgentAddress()+"]");
			com.getOut().writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//2 - Recebe resposta de HA a pedido de registo
	public void nodeRegisterResponse(Response resp){
		/*
		 * Se Resposta positiva ent‹o
		 *    Marca entrada como confirmada
		 * Sen‹o
		 *    Elimina entrada tempor‡ria
		 * Envia resultado do pedido ao MN
		 */

		if(resp.isResponse()){
			System.out.println("FA["+myIP+"] > Resposta de HA positiva: MN[" + resp.getIP() + "] registado com sucesso");
			visitorListTable.get(resp.getIP()).setLifeTimeLeft(resp.getTTL());
		}
		else {
			visitorListTable.remove(resp.getIP());
			nodesSockets.remove(resp.getIP());
			System.out.println("FA["+myIP+"] > Resposta de HA negativa: MN[" + resp.getIP() + "] nao registado.");
		}
		
		try {
			System.out.println("FA["+myIP+"] > Resposta de HA enviada a MN[" + resp.getIP() + "]");
			nodesSockets.get(resp.getIP()).getOut().writeObject(resp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//3 - Recebe pacote encapsulado, vindo de HA
	public void receivePacketFromHa(String HAA, PacoteEncapsulado packetE) {
		/*
		 * Desencapsula pacote
		 * Consulta VL
		 * Se MN nao existe entao
		 *   ignora pacote
		 * Senao 
		 *   entrega pacote ao MAC address correspondente (MN)
		 */

		Pacote packet = this.desencapsulaPacote(packetE);
		if(!visitorListTable.containsKey(packet.getDestination())){
			System.out.println("FA["+myIP+"] > MN[" + packet.getDestination() + "] nao existe na VL - pacote ignorado");
		}
		else {
			try { 
				String ipDestination = packet.getDestination();
				String macDestination = visitorListTable.get(ipDestination).getMacAddress();
				System.out.println("FA["+myIP+"] > A enviar pacote para MN[" + ipDestination + "] com o MacAddress["+macDestination+"]");
				
				nodesSockets.get(ipDestination).getOut().writeObject(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	//4 - Recebe pacote vindo de MN, destinado a CN
	public void sendPacket(Pacote packet) {
		/*
		 * Consulta VL
		 * Se MN nao esta na VL entao
		 *   ignora pacote
		 * Senao
		 *   Determina HA
		 *   Encapsula Pacote
		 *   Envia pacote para HA
		 */

		System.out.println("FA["+myIP+"] > Pacote recebido de "+packet.getSource()+" para "+packet.getDestination());

		if(!visitorListTable.containsKey(packet.getSource())) {
			System.out.println("FA["+myIP+"] > Ignora pacote para CN["+packet.getDestination()+"] porque MN[" + packet.getSource() + "] nao esta na VL");
		} else {
			MobileNodeData d = visitorListTable.get(packet.getSource());
			if(existHAA(d.getHomeAgentAddress())){
				PacoteEncapsulado packetE = this.encapsulaPacote(packet);

				try {
					HAsockets.get(d.getHomeAgentAddress()).getOut().writeObject(packetE);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			System.out.println("FA["+myIP+"] > Pacote enviado para HA["+d.getHomeAgentAddress()+"] para este enviar ao MN[" + packet.getDestination()+"]");

		}
	}




	//4 - Recebe pedido de cancelamento de registo --> ISTO FOI INVENTADO DURVAL PIRES?
	/*public void cancelamentoRegisto(MobileNodeData mb) {
		/*
		 * Consulta MBT
		 * Se MN nao existe na MBT entao
		 *   ignora pedido
		 * Senao
		 *   verifica credenciais
		 *   elimina entrada correspondente na MBT
		 *

		if(!visitorListTable.containsKey(mb.getIP())) {
			System.out.println("FA["+myIP+"] > MN nao existe na VLT - pedido ignorado");
		} else {
			visitorListTable.remove(mb.getIP());
			System.out.println("FA["+myIP+"] > registo do MN[" + mb.getIP() + "] cancelado");
		}
	}*/


	//5 - Temporizador de TTL
	public void temporizadorTTL() {
		/*
		 * Para cada entrada da VL
		 *   decrementa TTL
		 *   Se TTL chegou a zero
		 *     elimina entrada correspondente da VT
		 */

		//System.out.println("FA["+myIP+"] > a enviar TTL");

		HashMap<String, MobileNodeData> mbtAux = new HashMap<String, MobileNodeData>(visitorListTable);
		for(String key : mbtAux.keySet()) {
			MobileNodeData data = mbtAux.get(key);

			int ttl = data.getLifeTimeLeft();
			ttl--;

			if(ttl == 0) {
				visitorListTable.remove(key);
				System.out.println("FA["+myIP+"] > TTL: "+key+" eliminado da VLT");
			} else {
				data.setLifeTimeLeft(ttl);
				visitorListTable.put(key, data);
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
		//System.out.println("FA["+myIP+"] > A enviar advertisement message");

		AgentAdvertisementMessage advertisementMessage = new AgentAdvertisementMessage(sequenceNumber);
		advertisementMessage.setHomeAgent(true);

		for(String key : visitorListTable.keySet()) {
			MobileNodeData data = visitorListTable.get(key);
			advertisementMessage.addCareOfAddress(myIP); //AQUI MUDEI POR SER FA MAS TENHO ALGUMAS DUVIDAS. ATENCIONE
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
		System.out.println("FA["+myIP+"] > A encapsular pacote");

		MobileNodeData d = visitorListTable.get(packet.getSource());
		PacoteEncapsulado packetE = new PacoteEncapsulado();
		packetE.setSource(myIP);
		packetE.setDestination(d.getHomeAgentAddress());
		packetE.setProtocol("IP in IP");
		packetE.setData(packet);
		packetE.setType("pacoteEncapsulado");

		return packetE;
	}

	private Pacote desencapsulaPacote(PacoteEncapsulado packetE) {
		System.out.println("FA["+myIP+"] > A desencapsular pacote");

		return packetE.getData();
	}

	private boolean existHAA(String HAA) {
		for(String key : visitorListTable.keySet()) {
			MobileNodeData data = visitorListTable.get(key);
			if(data.getHomeAgentAddress().equals(HAA))
				return true;
		}

		return false;
	}

}