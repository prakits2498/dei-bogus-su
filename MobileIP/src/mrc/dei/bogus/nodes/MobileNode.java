package mrc.dei.bogus.nodes;

public class MobileNode {
	private String name;
	private String macAddress;
	private int lifeTimeLeft;
	private String homeAgent;
	
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
	
	public String getHomeAgent() {
		return homeAgent;
	}
	
	public void setHomeAgent(String homeAgent) {
		this.homeAgent = homeAgent;
	}
	
	
}
