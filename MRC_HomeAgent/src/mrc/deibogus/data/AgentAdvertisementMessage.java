package mrc.deibogus.data;

import java.util.ArrayList;

public class AgentAdvertisementMessage {

	private ArrayList<String> careOfAddresses;
	private boolean homeAgent;
	
	public AgentAdvertisementMessage() {
		this.homeAgent = true;
		this.careOfAddresses = new ArrayList<String>();
	}

	public ArrayList<String> getCareOfAddresses() {
		return careOfAddresses;
	}

	public void setCareOfAddresses(ArrayList<String> careOfAddresses) {
		this.careOfAddresses = careOfAddresses;
	}

	public boolean isHomeAgent() {
		return homeAgent;
	}

	public void setHomeAgent(boolean homeAgent) {
		this.homeAgent = homeAgent;
	}
	
	public void addCareOfAddress(String careOfAddress) {
		this.careOfAddresses.add(careOfAddress);
	}

}
