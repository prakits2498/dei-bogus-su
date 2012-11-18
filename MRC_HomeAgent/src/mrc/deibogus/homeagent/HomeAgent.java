package mrc.deibogus.homeagent;

import java.util.ArrayList;
import java.util.HashMap;

import mrc.deibogus.data.HomeAgentData;
import mrc.deibogus.server.Connection;

public class HomeAgent {

	private int id;
	private boolean logged;
	private Connection server;

	//MBT: <home_address, care_of_address, association_lifetime>
	private HashMap<String, HomeAgentData> mobilityBindingTable = new HashMap<String, HomeAgentData>();
	private ArrayList networkNodes = new ArrayList();

	public HomeAgent(int id) {
		this.id = id;
		this.logged = true;
	}

	public void setServer(Connection c) {
		this.server = c;
	}

	public void run() {
		while(logged) {	
			System.out.println(">>> HomeAgent ["+id+"] <<<");

			//TODO faz o que tem a fazer

			//notifica o servidor
			synchronized(server) {
				if(server.getState().name().equals("WAITING")) {
					server.notify();
				}
			}

			//e fica a espera que o servidor o acorde
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
