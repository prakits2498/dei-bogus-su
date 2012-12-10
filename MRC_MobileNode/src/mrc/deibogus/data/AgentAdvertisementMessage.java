package mrc.deibogus.data;

import java.io.Serializable;
import java.util.ArrayList;

public class AgentAdvertisementMessage extends Request implements Serializable {

	private static final long serialVersionUID = -3881154186501067881L;
	
	private ArrayList<String> careOfAddresses;
	private boolean homeAgent;
	private int sequenceNumber;
	
	public AgentAdvertisementMessage(int sequenceNumber) {
		this.setSequenceNumber(sequenceNumber);
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

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

}
