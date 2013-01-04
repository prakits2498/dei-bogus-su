package rs.deibogus.server.login;


/**
 * Bridge Design Pattern
 * @author bfurtado, durval
 *
 */
public interface ILoginImplementor {
	public boolean login(String user, String pass);
}
