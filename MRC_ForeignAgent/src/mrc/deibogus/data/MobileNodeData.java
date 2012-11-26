package mrc.deibogus.data;

import java.io.Serializable;

public class MobileNodeData extends Request implements Serializable {
	
	private static final long serialVersionUID = -8267659253197207472L;

	private String IP;
	private String macAddress; //00:23:6c:8f:73:ab
	private int lifeTimeLeft;
	private String homeAgentAddress;
	private String careOfAddress;
	
	public String getIP() {
		return IP;
	}
	
	public void setIP(String IP) {
		this.IP = IP;
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
