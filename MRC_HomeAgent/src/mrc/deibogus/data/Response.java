package mrc.deibogus.data;

import java.io.Serializable;

public class Response extends Request implements Serializable{

	private static final long serialVersionUID = 1118725747271241598L;
	private boolean response;

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}

}
