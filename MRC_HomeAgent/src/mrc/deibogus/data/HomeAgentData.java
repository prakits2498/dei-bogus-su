package mrc.deibogus.data;

public class HomeAgentData {
	private String careOfAddress;
	private int lifeTime;
	
	public HomeAgentData() {
		this.lifeTime = 10;
	}
	
	public String getCareOfAddress() {
		return careOfAddress;
	}
	
	public void setCareOfAddress(String careOfAddress) {
		this.careOfAddress = careOfAddress;
	}

	public int getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}
	
	

}
