package mrc.deibogus.data;

import java.io.Serializable;

public class Request implements Serializable {

	private static final long serialVersionUID = 7923661429084687689L;
	private String type;

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
}
