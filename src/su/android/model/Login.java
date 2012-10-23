package su.android.model;

public class Login {
	String email;
	String pass;
	
	public Login(String e, String p){
		this.email=e;
		this.pass=p;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
}
