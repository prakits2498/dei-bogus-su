package mrc.deibogus.data;

import java.io.Serializable;

public class CorrespondentNodeData extends Request implements Serializable {
private static final long serialVersionUID = -8267659253197207472L;
	
	private String IP;
	private String macAddress; //00:23:6c:8f:73:ab
	
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
	
	
	
}
