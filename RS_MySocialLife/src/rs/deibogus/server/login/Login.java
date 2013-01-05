package rs.deibogus.server.login;

import rs.deibogus.shared.SessionData;

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
	
	public boolean confirmLogin(String user, String pass, SessionData sessao) {
		return implementor.login(user, pass,sessao);
	}
}
