package rs.deibogus.server.login;

import rs.deibogus.server.DBManager;

/**
 * Bridge Design Pattern
 * @author bfurtado, durval
 *
 */
public class AppLogin implements ILoginImplementor {

	@Override
	public boolean login(String user, String pass) {
		DBManager db = DBManager.getInstance();
		if(db.clientLogin(user, pass))
			return true;
		
		return false;
	}

	
}
