package mrc.deibogus.data;

public class MobileNodeData {
	private String name;
	private String macAddress;
	private int lifeTimeLeft;
	private String homeAgentAddress;
	private String careOfAddress;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMacAddress() {
		return macAddress;
	}
	
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	public int getLifeTimeLeft() {
		return lifeTimeLeft;
	}
	
	public void setLifeTimeLeft(int lifeTimeLeft) {
		this.lifeTimeLeft = lifeTimeLeft;
	}
	
	public String getHomeAgentAddress() {
		return homeAgentAddress;
	}
	
	public void setHomeAgentAddress(String homeAgentAddress) {
		this.homeAgentAddress = homeAgentAddress;
	}

	public String getCareOfAddress() {
		return careOfAddress;
	}

	public void setCareOfAddress(String careOfAddress) {
		this.careOfAddress = careOfAddress;
	}
	
	
	
}
