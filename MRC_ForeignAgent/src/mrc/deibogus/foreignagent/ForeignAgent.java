package mrc.deibogus.foreignagent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import mrc.deibogus.data.ForeignAgentData;
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

	private HashMap<String, MobileNodeData> mobileNodes = new HashMap<String, MobileNodeData>();

	//MBT: <home_address, care_of_address, association_lifetime>
	private HashMap<String, MobileNodeData> visitorListTable = new HashMap<String, MobileNodeData>();

	//private HashMap<String, String> networkNodes = new HashMap<String, String>(); //IP - MAC

	public ForeignAgent(String myIP,String haIP) {
		this.haIP = haIP;
		this.myIP = myIP;
		this.connected = true;

		this.start();
	}

	public void run() {
		System.out.println("ForeignAgent Started ["+myIP+"]");
		while(connected) {
			synchronized(this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//TODO meter temporizador e chamar a fun�ao temporizadorTTL
		}
	}
	
	//2 - Recebe resposta de HA a pedido de registo ----------------DEPOIS POR NO CONNECTION IGUAL AOS OUTROS PA FICAR TUDO PIPI
	public void registoHA(ObjectInputStream in, ObjectOutputStream out, Communication com){
		MobileNodeData data = new MobileNodeData();
		data.setIP(myIP);
		data.setMacAddress(myMAC);

		data.setType("ConnectFA");
		System.out.println("Pedido de registo do FA "+myIP+" para o HA "+haIP);

		try {
			out.writeObject(data);
			Response response = (Response) in.readObject();
			if(response.isResponse()){
				this.addHA(haIP,com);
				System.out.println("Registo do FA "+myIP+" noo HA "+haIP + " efectuado com sucesso.");
			}
			else
				System.out.println("Registo do FA "+myIP+" noo HA "+haIP + " efectuado sem sucesso.");
		} catch (IOException e) {
			System.err.println("FA["+myIP+"] > Erro ao registar.");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addHA(String IP, Communication communication) {
		this.HAsockets.put(IP, communication);
	}

	//1 - Recebe pedido de registo e 2 - Recebe resposta de HA a pedido de registo
	public void addMN(MobileNodeData data, Communication communication) {
		Communication com = HAsockets.get(data.getHomeAgentAddress());
		
		if(!visitorListTable.containsKey(data.getIP())){
			this.nodesSockets.put(data.getIP(), communication);
			this.visitorListTable.put(data.getIP(), data);
			data.setType("pedidoRegistoFA");
			try {
				com.getOut().writeObject(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			MobileNodeData temp = visitorListTable.get(data.getIP());
			temp.setLifeTimeLeft(temp.getLifeTimeLeft()+10); //AQUI COLOCAR O TEMPO QUE FOR PARA AUMENTAR
			visitorListTable.put(data.getIP(), temp);
			data.setType("pedidoRegistoFA");
			try {
				com.getOut().writeObject(temp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void nodeRegisterResponse(Response resp){
		if(!resp.isResponse()){
			visitorListTable.remove(resp.getIP());
			nodesSockets.remove(resp.getIP());
		}
		
		try {
			nodesSockets.get(resp.getIP()).getOut().writeObject(resp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//4 - Recebe pacote vindo de MN, destinado a CN
	public void sendPacket(Pacote packet) {
		/*
		 * Consulta VLT
		 * Se MN nao esta na VLT entao
		 *   ignora pacote
		 * Senao
		 *   Determina HA
		 *   Encapsula Pacote
		 *   Envia pacote para HA
		 */

		System.out.println("FA["+myIP+"] > Pacote recebido de "+packet.getSource()+" para "+packet.getDestination());

		if(!visitorListTable.containsKey(packet.getSource())) {
			System.out.println("FA["+myIP+"] > Ignora pacote para CN["+packet.getDestination()+"] porque MN[" + packet.getSource() + "] nao esta na VLT");
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
			
		}
	}

	//3 - Recebe pacote encapsulado, vindo de HA
	public void receivePacketFromHa(String HAA, PacoteEncapsulado packetE) {
		/*
		 * Desencapsula pacote
		 * Consulta VLT
		 * Se MN nao existe entao
		 *   ignora pacote
		 * Senao 
		 *   entrega pacote ao MAC address correspondente (MN)
		 */
		
		Pacote packet = this.desencapsulaPacote(packetE);
		if(!visitorListTable.containsKey(packet.getDestination())){
			System.out.println("FA["+myIP+"] > MN " + packet.getDestination() + " nao existe na VLT - pacote ignorado");
		}
		else {
			try { //TODO AQUI FALTA POR O GAJO A PROCURAR POR MAC ADDRESS
				System.out.println("FA["+myIP+"] > A enviar pacote para MN["+packet.getDestination()+"]");
				nodesSockets.get(packet.getDestination()).getOut().writeObject(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
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

		if(!visitorListTable.containsKey(mb.getIP())) {
			System.out.println("FA["+myIP+"] > MN nao existe na VLT - pedido ignorado");
		} else {
			if(mobileNodes.containsKey(mb.getIP())) {
				visitorListTable.remove(mb.getIP());
				
				System.out.println("FA["+myIP+"] > registo cancelado");
			} else {
				System.out.println("FA["+myIP+"] > MN nao pertence a FN");
			}
			
		}
	}

	//5 - Temporizador de TTL
	public void temporizadorTTL() {
		/*
		 * Para cada entrada da VLT
		 *   decrementa TTL
		 *   Se TTL chegou a zero
		 *     elimina entrada correspondente da VTL
		 */
		
		System.out.println("FA["+myIP+"] > temporizador TTL");
		
		HashMap<String, MobileNodeData> mbtAux = new HashMap<String, MobileNodeData>(visitorListTable);
		for(String key : mbtAux.keySet()) {
			MobileNodeData data = mbtAux.get(key);
			
			data.setLifeTimeLeft(data.getLifeTimeLeft()-1);
			visitorListTable.put(key, data);
			if(data.getLifeTimeLeft() == 0) {
				visitorListTable.remove(key);
			}
		}
	}

	//6 - Arranque, ou temporizador de anuncio de FA
	public void broadcast() {
		/*
		 * Se Arranque entao
		 *   Sequence number = [0...255]
		 * Senao
		 *   Sequence number = [256...65635]
		 *   Incrementa Sequence number
		 * Envia anuncio por broadcast
		 */
		
		//TODO funcao de broadcast para verificacao de presenca
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