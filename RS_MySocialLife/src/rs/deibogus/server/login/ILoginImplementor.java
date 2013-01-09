package rs.deibogus.server.login;

import rs.deibogus.shared.SessionData;


/**
 * Bridge Design Pattern
 * @author bfurtado, durval
 *
 */
public interface ILoginImplementor {
	public boolean login(String user, String pass, SessionData sessao);
	public boolean registerUser(String user, String pass);
}
