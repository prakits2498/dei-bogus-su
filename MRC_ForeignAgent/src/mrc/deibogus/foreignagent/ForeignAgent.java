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

	//Registo deste FA num HA
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
			System.err.println("FA["+myIP+"] > Erro ao registar-se no HA " + haIP);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addHA(String IP, Communication communication) {
		this.HAsockets.put(IP, communication);
	}

	//1 - Recebe pedido de registo --------------- qt tempo mete no lft?
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
			
			System.out.println("Pedido de registo do MN " + data.getIP() + " enviado pelo FA " + myIP + " ao HA "+ data.getHomeAgentAddress());
			
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
			
			System.out.println("Renovacao de registo do MN " + data.getIP() + " enviado pelo FA " + myIP + " ao HA "+ data.getHomeAgentAddress());
		}

	}

	//2 - Recebe resposta de HA a pedido de registo
	public void nodeRegisterResponse(Response resp){
		if(!resp.isResponse()){
			visitorListTable.remove(resp.getIP());
			nodesSockets.remove(resp.getIP());
			
			System.out.println("Registo do MN " + resp.getIP() + "no FA " + myIP + "efectuado SEM sucesso");
		}
		else{
			System.out.println("Registo do MN " + resp.getIP() + "no FA " + myIP + "efectuado com sucesso");
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
			
			System.out.println("FA["+myIP+"] > Enviou pacote para HA["+d.getHomeAgentAddress()+"] para este enviar para " + packet.getDestination());

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
			visitorListTable.remove(mb.getIP());
			System.out.println("FA["+myIP+"] > registo do MN[" + mb.getIP() + "] cancelado");
		}
		
		//TODO comunicar isso ao HA? Secaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

	}


//5 - Temporizador de TTL
public void temporizadorTTL() {
	/*
	 * Para cada entrada da VLT
	 *   decrementa TTL
	 *   Se TTL chegou a zero
	 *     elimina entrada correspondente da VTL
	 */
	
	System.out.println("FA["+myIP+"] > a enviar TTL");

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

		int sequenceNumber = 0; //TODO perguntar pra k serve o sequence number
		if(arranque) {
			sequenceNumber = generateRandomInteger(0, 255);
		} else {
			sequenceNumber = generateRandomInteger(256, 65635);
		}

		sendAdvertisementMessage();
	}

	private void sendAdvertisementMessage() {
		System.out.println("FA["+myIP+"] > A enviar advertisement message");
		
		AgentAdvertisementMessage advertisementMessage = new AgentAdvertisementMessage();
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