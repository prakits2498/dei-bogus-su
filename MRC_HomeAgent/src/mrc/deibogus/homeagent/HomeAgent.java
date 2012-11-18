package mrc.deibogus.homeagent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import mrc.deibogus.data.HomeAgentData;
import mrc.deibogus.data.Request;

public class HomeAgent extends Thread {

	private ObjectInputStream inObject;
	private ObjectOutputStream outObject;
	private Socket clientSocket;
	private boolean logged;

	//MBT: <home_address, care_of_address, association_lifetime>
	private HashMap<String, HomeAgentData> mobilityBindingTable = new HashMap<String, HomeAgentData>();
	private ArrayList networkNodes = new ArrayList();

	public HomeAgent(Socket clientSocket, ObjectInputStream in, ObjectOutputStream out) throws ClassNotFoundException {
		this.clientSocket = clientSocket;
		this.outObject = out;
		this.inObject = in;
		this.start();
	}

	public void run() {
		while(logged) {
			try {
				outObject.flush();

				//read request from client
				Request new_request = (Request) inObject.readObject();

				if(new_request.getType().equals("pacoteCNtoMN")) { //1
					System.out.println("> Request received");
					//Context cont = (Context) new_request;

					//TRATA DO PEDIDO E ENVIA RESPOSTA AO CLIENTE

					//send response to client
					//outObject.writeObject(r);
					outObject.flush();
				}
				
				if(new_request.getType().equals("pacoteEncapsulado")) { //2
					System.out.println("> Request received");
					//Context cont = (Context) new_request;

					//TRATA DO PEDIDO E ENVIA RESPOSTA AO CLIENTE

					//send response to client
					//outObject.writeObject(r);
					outObject.flush();
				}
				
				if(new_request.getType().equals("pedidoRegisto")) { //3
					System.out.println("> Request received");
					//Context cont = (Context) new_request;

					//TRATA DO PEDIDO E ENVIA RESPOSTA AO CLIENTE

					//send response to client
					//outObject.writeObject(r);
					outObject.flush();
				}
				
				if(new_request.getType().equals("cancelamentoRegisto")) { //4
					System.out.println("> Request received");
					//Context cont = (Context) new_request;

					//TRATA DO PEDIDO E ENVIA RESPOSTA AO CLIENTE

					//send response to client
					//outObject.writeObject(r);
					outObject.flush();
				}
				

			} catch (SocketException e2) {
				System.err.println("Socket Closed!");
				logged = false;
				break;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	//5
	public void temporizadorTTL() {
		
	}

	//TODO funcao de broadcast para verificacao de presenca
	//6
	void broadcast() {

	}
	
	
	
	
}
