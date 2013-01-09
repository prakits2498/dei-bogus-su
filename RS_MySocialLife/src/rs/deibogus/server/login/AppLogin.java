package rs.deibogus.server.login;

import rs.deibogus.server.DBManager;
import rs.deibogus.shared.SessionData;

/**
 * Bridge Design Pattern
 * @author bfurtado, durval
 *
 */
public class AppLogin implements ILoginImplementor {

	@Override
	public boolean login(String user, String pass,SessionData sessao) {
		DBManager db = DBManager.getInstance();
		if(db.clientLogin(user, pass))
			return true;
		
		return false;
	}

	@Override
	public boolean registerUser(String user, String pass) {
		DBManager db = DBManager.getInstance();
		return db.addClient(user, pass);
	}

	
}
