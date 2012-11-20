package mrc.deibogus.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

import mrc.deibogus.data.MobileNodeData;
import mrc.deibogus.data.Pacote;
import mrc.deibogus.data.Request;
import mrc.deibogus.data.Response;
import mrc.deibogus.homeagent.HomeAgent;
import mrc.deibogus.simulator.Communication;

public class Connection extends Thread {

	private ObjectInputStream inObject;
	private ObjectOutputStream outObject;
	private Socket clientSocket;
	
	private boolean connected;
	
	private HomeAgent homeAgent;
	
	private HashMap<String, Communication> FAsockets = new HashMap<String, Communication>();
	private HashMap<String,	Communication> nodesSockets = new HashMap<String, Communication>();
	
	public Connection(HomeAgent homeAgent, Socket clientSocket, ObjectInputStream in, ObjectOutputStream out) {
		this.inObject = in;
		this.outObject = out;
		this.clientSocket = clientSocket;
		
		this.homeAgent = homeAgent;
		//this.homeAgent.setServer(this);
		
		connected = true;
		this.start();
	}
	
	public void run() {
		while(connected) {
			try {
				outObject.flush();
				
				//read request from client
				Request request = (Request) inObject.readObject();
				Response response = new Response();
				
				if(request.getType().equals("ConnectFA")) {
					MobileNodeData data = (MobileNodeData) request;
					System.out.println("> Pedido de conexao na rede de um Foreign Agent ["+data.getIP()+"]");

					Communication communication = new Communication(data.getIP(), this.inObject, this.outObject, this.clientSocket);
					
					synchronized (homeAgent) {
						if(homeAgent.getState().name().equals("WAITING")) {
							homeAgent.addFA(data, communication);
							homeAgent.notify();
						}
					}

					//send response to client
					response.setResponse(true);
					outObject.writeObject(response);
					outObject.flush();
				}
				
				if(request.getType().equals("ConnectMN")) {
					MobileNodeData data = (MobileNodeData) request;
					System.out.println("> Pedido de registo na rede de um Mobile Node ["+data.getIP()+"]");

					Communication communication = new Communication(data.getIP(), this.inObject, this.outObject, this.clientSocket);
					
					synchronized (homeAgent) {
						if(homeAgent.getState().name().equals("WAITING")) {
							homeAgent.addMN(data, communication);
							homeAgent.notify();
						}
					}

					//send response to client
					response.setResponse(true);
					outObject.writeObject(response);
					outObject.flush();
				}
				
				if(request.getType().equals("pacoteCNtoMN")) { //1
					Pacote packet = (Pacote) request;
					
					synchronized (homeAgent) {
						if(homeAgent.getState().name().equals("WAITING")) {
							homeAgent.sendPacket(packet);
							homeAgent.notify();
						}
					}

					//send response to client
					/*response.setResponse(true);
					response.setType("pacote");
					outObject.writeObject(response);
					outObject.flush();*/
				}
				
				if(request.getType().equals("pacoteEncapsulado")) { //2
					System.out.println("> Request received");
					//Context cont = (Context) new_request;

					//TRATA DO PEDIDO E ENVIA RESPOSTA AO CLIENTE

					//send response to client
					//outObject.writeObject(r);
					outObject.flush();
				}
				
				if(request.getType().equals("pedidoRegisto")) { //3
					System.out.println("> Request received");
					//Context cont = (Context) new_request;

					//TRATA DO PEDIDO E ENVIA RESPOSTA AO CLIENTE

					//send response to client
					//outObject.writeObject(r);
					outObject.flush();
				}
				
				if(request.getType().equals("cancelamentoRegisto")) { //4
					System.out.println("> Request received");
					//Context cont = (Context) new_request;

					//TRATA DO PEDIDO E ENVIA RESPOSTA AO CLIENTE

					//send response to client
					//outObject.writeObject(r);
					outObject.flush();
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
