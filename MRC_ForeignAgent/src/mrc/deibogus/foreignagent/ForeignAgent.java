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
	private HashMap<String, ForeignAgentData> visitorListTable = new HashMap<String, ForeignAgentData>();

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

	public void addMN(MobileNodeData data, Communication communication) {
		this.nodesSockets.put(data.getIP(), communication);
		this.mobileNodes.put(data.getIP(), data);
	}

	//1 - Recebe pacote vindo de HA destinado a MN
	public void sendPacket(Pacote packet) {
		/*
		 * Consulta VLT
		 * Se MN nao esta na VLT entao
		 *   Entrega pacote ao MN
		 * Senao
		 *   Determina CoA
		 *   Encapsula Pacote
		 *   Envia pacote para CoA
		 */

		System.out.println("FA["+myIP+"] > Pacote recebido de "+packet.getSource()+" para "+packet.getDestination());

		if(!visitorListTable.containsKey(packet.getDestination())) {
			if(nodesSockets.containsKey(packet.getDestination())) {
				Communication mb = nodesSockets.get(packet.getDestination());
				System.out.println("FA["+myIP+"] > A enviar pacote para MN["+packet.getDestination()+"]");

				try {
					mb.getOut().writeObject(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} else {
			ForeignAgentData d = visitorListTable.get(packet.getDestination());
			PacoteEncapsulado packetE = this.encapsulaPacote(packet);

			try {
				HAsockets.get(d.getHomeAgentAddress()).getOut().writeObject(packetE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//2 - Recebe pacote encapsulado, vindo de HA
	public void receivePacket(String HAA, PacoteEncapsulado packetE) {
		/*
		 * Consulta VLT
		 * Se HA nao existe entao
		 *   ignora pacote
		 * Senao 
		 *   desencapsula pacote
		 *   envia pacote para o destinatario (MN)
		 */

		if(!existHAA(HAA)) {
			System.out.println("FA["+myIP+"] > HAA nao existe - pacote ignorado");
		} else {
			Pacote packet = this.desencapsulaPacote(packetE);

			try {
				System.out.println("FA["+myIP+"] > A enviar pacote para CN["+packet.getDestination()+"]");
				nodesSockets.get(packet.getDestination()).getOut().writeObject(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

//	//3 - Recebe pedido de registo, vindo de FA
//	public void registoFA(MobileNodeData mb) {
//		/*
//		 * Consulta MBT
//		 * Se MN ja existe na MBT entao
//		 *   Actualiza entrada na MBT (CoA, TTL)
//		 * Senao
//		 *   Verifica credenciais
//		 *   Cria entrada na MBT
//		 *   Envia Confirmacao (pos/neg) ao FA
//		 */
//
//		System.out.println("HA["+myIP+"] > Recebido pedido de registo de FA");
//		
//		if(mobilityBindingTable.containsKey(mb.getIP())) {
//			HomeAgentData data = mobilityBindingTable.get(mb.getIP());
//			data.setCareOfAddress(mb.getCareOfAddress());
//			data.setLifeTime(mb.getLifeTimeLeft());
//			mobilityBindingTable.put(mb.getIP(), data);
//			
//			System.out.println("HA["+myIP+"] > MN ja existe na MBT - registo actualizado");
//		} else {
//			Response resp = new Response();
//			resp.setType("RespRegistoFA");
//
//			if(!networkNodes.containsKey(mb.getIP())) {
//				resp.setResponse(false);
//				
//				System.out.println("HA["+myIP+"] > MN nao pertence a HN");
//			} else {
//				HomeAgentData data = new HomeAgentData();
//				data.setCareOfAddress(mb.getCareOfAddress());
//				data.setLifeTime(mb.getLifeTimeLeft());
//				mobilityBindingTable.put(mb.getIP(), data);
//
//				resp.setResponse(true);
//				
//				System.out.println("HA["+myIP+"] > MN registado");
//			}
//
//			try {
//				FAsockets.get(mb.getCareOfAddress()).getOut().writeObject(resp);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		}
//	}

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
		 * Para cada entrada da MBT
		 *   decrementa TTL
		 *   Se TTL chegou a zero
		 *     elimina entrada correspondente da MBT
		 */
		
		System.out.println("FA["+myIP+"] > temporizador TTL");
		
		HashMap<String, ForeignAgentData> mbtAux = new HashMap<String, ForeignAgentData>(visitorListTable);
		for(String key : mbtAux.keySet()) {
			ForeignAgentData data = mbtAux.get(key);
			
			int ttl = data.getLifeTimeLeft();
			ttl--;
			
			if(ttl == 0) {
				visitorListTable.remove(key);
			}
		}
	}

	//6 - Arranque, ou temporizador de anuncio de HA/FA
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

		ForeignAgentData d = visitorListTable.get(packet.getDestination());
		PacoteEncapsulado packetE = new PacoteEncapsulado();
		packetE.setSource(myIP);
		packetE.setDestination(d.getCareOfAddress());
		packetE.setProtocol("IP in IP");
		packetE.setData(packet);

		return packetE;
	}

	private Pacote desencapsulaPacote(PacoteEncapsulado packetE) {
		System.out.println("FA["+myIP+"] > A desencapsular pacote");

		return packetE.getData();
	}

	private boolean existHAA(String HAA) {
		for(String key : visitorListTable.keySet()) {
			ForeignAgentData data = visitorListTable.get(key);
			if(data.getHomeAgentAddress().equals(HAA))
				return true;
		}

		return false;
	}

}