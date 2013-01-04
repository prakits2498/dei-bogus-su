package rs.deibogus.server.login;

/**
 * Bridge Design Pattern
 * @author bfurtado, durval
 *
 */
public class Login implements ILogin {

	private ILoginImplementor implementor = null;
	
	public Login(ILoginImplementor imp) {
		this.implementor = imp;
	}
	
	public boolean confirmLogin(String user, String pass) {
		return implementor.login(user, pass);
	}
}
