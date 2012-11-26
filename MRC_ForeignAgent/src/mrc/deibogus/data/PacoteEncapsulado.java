package mrc.deibogus.data;

import java.io.Serializable;

public class PacoteEncapsulado extends Request implements Serializable {

	private static final long serialVersionUID = -7671152337571010335L;

	private String source;
	private String destination;
	private String protocol;
	private Pacote data;
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Pacote getData() {
		return data;
	}

	public void setData(Pacote data) {
		this.data = data;
	}
	
	
}
