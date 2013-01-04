package rs.deibogus.client.interfacebuilder;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Builder Design Pattern
 * "ConcreteBuilder"
 * @author bfurtado, durval
 *
 */
public class LoginPageBuilder extends PageBuilder {
	
	private TextBox username;
	private PasswordTextBox password;
	private Button sendButton;

	@Override
	public void buildHeader() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void destructHeader() {
		//TODO
	}

	@Override
	public void buildMain() {
		username = page.createTextBox("Insert Username", 147, 44);
		username.getElement().setId("usernameTxtBox");
		password = page.createPasswordTextBox(150, 84);
		password.getElement().setId("passwordTxtBox");
		sendButton = page.createButton("Login", 196, 137);
		sendButton.getElement().setId("sendButtonLogin");
	}
	
	@Override
	public void destructMain() {
		username.removeFromParent();
		password.removeFromParent();
		sendButton.removeFromParent();
	}

	@Override
	public void buildFooter() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void destructFooter() {
		//TODO
	}

}
