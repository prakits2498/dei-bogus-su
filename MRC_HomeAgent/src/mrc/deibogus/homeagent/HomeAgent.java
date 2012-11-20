package mrc.deibogus.homeagent;

import java.io.IOException;
import java.util.HashMap;

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

	private HashMap<String, MobileNodeData> mobileNodes = new HashMap<String, MobileNodeData>();

	//MBT: <home_address, care_of_address, association_lifetime>
	private HashMap<String, HomeAgentData> mobilityBindingTable = new HashMap<String, HomeAgentData>();

	private HashMap<String, String> networkNodes = new HashMap<String, String>(); //IP - MAC

	public HomeAgent(String myIP) {
		this.myIP = myIP;
		this.connected = true;

		this.loadNetworkNodes();

		this.start();
	}

	public void run() {
		System.out.println("HomeAgent Started ["+myIP+"]");
		while(connected) {
			synchronized(this) {
				try {
					this.wait();
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
		//TODO carregar networkNodes do ficheiro 

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

		System.out.println("HA["+myIP+"] > Recebido pedido de registo de FA");
		
		if(mobilityBindingTable.containsKey(mb.getIP())) {
			HomeAgentData data = mobilityBindingTable.get(mb.getIP());
			data.setCareOfAddress(mb.getCareOfAddress());
			data.setLifeTime(mb.getLifeTimeLeft());
			mobilityBindingTable.put(mb.getIP(), data);
			
			System.out.println("HA["+myIP+"] > MN ja existe na MBT - registo actualizado");
		} else {
			Response resp = new Response();
			resp.setType("RespRegistoFA");

			if(!networkNodes.containsKey(mb.getIP())) {
				resp.setResponse(false);
				
				System.out.println("HA["+myIP+"] > MN nao pertence a HN");
			} else {
				HomeAgentData data = new HomeAgentData();
				data.setCareOfAddress(mb.getCareOfAddress());
				data.setLifeTime(mb.getLifeTimeLeft());
				mobilityBindingTable.put(mb.getIP(), data);

				resp.setResponse(true);
				
				System.out.println("HA["+myIP+"] > MN registado");
			}

			try {
				FAsockets.get(mb.getCareOfAddress()).getOut().writeObject(resp);
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
		
		System.out.println("HA["+myIP+"] > temporizador TTL");
		
		HashMap<String, HomeAgentData> mbtAux = new HashMap<String, HomeAgentData>(mobilityBindingTable);
		for(String key : mbtAux.keySet()) {
			HomeAgentData data = mbtAux.get(key);
			
			int ttl = data.getLifeTime();
			ttl--;
			
			if(ttl == 0) {
				mobilityBindingTable.remove(key);
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
		System.out.println("HA["+myIP+"] > A encapsular pacote");

		HomeAgentData d = mobilityBindingTable.get(packet.getDestination());
		PacoteEncapsulado packetE = new PacoteEncapsulado();
		packetE.setSource(myIP);
		packetE.setDestination(d.getCareOfAddress());
		packetE.setProtocol("IP in IP");
		packetE.setData(packet);

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

}
