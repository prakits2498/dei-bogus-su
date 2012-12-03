package mrc.deibogus.data;

import java.io.Serializable;

public class Response extends Request implements Serializable{

	private static final long serialVersionUID = 1118725747271241598L;
	private boolean response;
	private String IP;

	public String getIP() {
		return IP;
	}

	public void setIP(String IP) {
		this.IP = IP;
	}

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}

}
