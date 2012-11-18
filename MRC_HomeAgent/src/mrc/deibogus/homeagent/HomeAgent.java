package mrc.deibogus.homeagent;

import mrc.deibogus.server.Connection;

public class HomeAgent {

	private int id;
	private boolean logged;
	private Connection server;
	
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
