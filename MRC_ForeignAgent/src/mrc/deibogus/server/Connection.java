package mrc.deibogus.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

import mrc.deibogus.data.MobileNodeData;
import mrc.deibogus.data.Pacote;
import mrc.deibogus.data.PacoteEncapsulado;
import mrc.deibogus.data.Request;
import mrc.deibogus.data.Response;
import mrc.deibogus.foreignagent.ForeignAgent;
import mrc.deibogus.simulator.Communication;

public class Connection extends Thread {

	private ObjectInputStream inObject;
	private ObjectOutputStream outObject;
	private Socket clientSocket;
	
	private boolean connected;
	
	private ForeignAgent foreignAgent;
	
	public Connection(ForeignAgent foreignAgent, Socket clientSocket, ObjectInputStream in, ObjectOutputStream out) {
		this.inObject = in;
		this.outObject = out;
		this.clientSocket = clientSocket;
		this.foreignAgent = foreignAgent;
		connected = true;
		this.start();
	}
	
	public void run() {
		while(connected) {
			try {
				outObject.flush();
				//read request from client
				Request request = (Request) inObject.readObject();
				
				//1 - Recebe pedido de registo
				if(request.getType().equals("ConnectMN")) {
					MobileNodeData data = (MobileNodeData) request;
					System.out.println("> Pedido de registo na rede de um Mobile Node ["+data.getIP()+"]");

					Communication communication = new Communication(data.getIP(), this.inObject, this.outObject, this.clientSocket);
					
					synchronized (foreignAgent) {
						if(foreignAgent.getState().name().equals("WAITING")) {
							foreignAgent.addMN(data, communication);
							foreignAgent.notify();
						}
					}
				}
				
				if(request.getType().equals("pacoteCNtoMN")) { //4 O NOME ENGANA, USEI ESTE PARA FICAR UNIFORME COM O HA, MAS ISTO É MN TO CN
					Pacote packet = (Pacote) request;
					
					System.out.println("FA recebeu pacote para encapsular e enviar");
					synchronized (foreignAgent) {
						if(foreignAgent.getState().name().equals("WAITING")) {
							foreignAgent.sendPacket(packet);
							foreignAgent.notify();
						}
					}
				}
				
				if(request.getType().equals("pacoteEncapsulado")) { //3
					PacoteEncapsulado packetE = (PacoteEncapsulado) request;
					
					System.out.println("FA recebeu pacote encapsulado");
					synchronized (foreignAgent) {
						if(foreignAgent.getState().name().equals("WAITING")) {
							foreignAgent.receivePacketFromHa(packetE.getSource(), packetE);
							foreignAgent.notify();
						}
					}
				}
				
				if(request.getType().equals("cancelamentoRegisto")) { //Este nem ta na folha.. somos bwe a frente
					MobileNodeData mb = (MobileNodeData) request;
					
					System.out.println("FA recebeu cancelamento de registo");
					synchronized (foreignAgent) {
						if(foreignAgent.getState().name().equals("WAITING")) {
							foreignAgent.cancelamentoRegisto(mb);
							foreignAgent.notify();
						}
					}
				}
				//2 - Resposta de Registo
				if(request.getType().equals("RespRegistoFA")) {
					Response resp = (Response) request;
					
					System.out.println("FA recebeu resposta de registo");
					synchronized (foreignAgent) {
						if(foreignAgent.getState().name().equals("WAITING")) {
							foreignAgent.nodeRegisterResponse(resp);
							foreignAgent.notify();
						}
					}
				}
				
			} catch(SocketException e2) {
				System.err.println("Socket Closed!");
				connected = false;
				break;
			} catch (IOException e) {
				System.err.println("Socket Closed!");
				connected = false;
				break;
			} catch (ClassNotFoundException e) {
				System.err.println("Socket Closed!");
				connected = false;
				break;
			}
		}
	}
}
