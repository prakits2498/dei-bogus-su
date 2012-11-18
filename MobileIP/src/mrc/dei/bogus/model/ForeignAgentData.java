package mrc.dei.bogus.model;

public class ForeignAgentData {
	private String careOfAddress;
	private String macAddress;
	private int lifeTime;
	
	public String getCareOfAddress() {
		return careOfAddress;
	}
	
	public void setCareOfAddress(String careOfAddress) {
		this.careOfAddress = careOfAddress;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public int getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}
	
	
}
