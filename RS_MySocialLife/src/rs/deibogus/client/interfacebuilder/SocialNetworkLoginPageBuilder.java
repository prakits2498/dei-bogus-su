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
public class SocialNetworkLoginPageBuilder extends PageBuilder {

	private TextBox usernameFlickr;
	private TextBox usernamePicasa;
	private PasswordTextBox passwordFlickr;
	private PasswordTextBox passwordPicasa;
	private Button sendButtonFlickr;
	private Button sendButtonPicasa;

	@Override
	public void buildHeader() {
		// TODO Auto-generated method stub

	}

	@Override
	public void buildMain() {
		//usernameFlickr = page.createTextBox("Insert Username", 147, 44);
		//passwordFlickr = page.createPasswordTextBox(150, 84);
		sendButtonFlickr = page.createButton("Flickr Frob Request", 196, 137);
		sendButtonFlickr.getElement().setId("sendButtonFlickrLogin");
		
		usernamePicasa = page.createTextBox("Insert Username", 147, 200);
		usernamePicasa.getElement().setId("usernamePicasaTxtBox");
		
		passwordPicasa = page.createPasswordTextBox(150, 240);
		passwordPicasa.getElement().setId("passwordPicasaTxtBox");
		
		sendButtonPicasa = page.createButton("Picasa Login", 196, 290);
		sendButtonPicasa.getElement().setId("sendButtonPicasaLogin");
	}

	@Override
	public void buildFooter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destructHeader() {
		// TODO Auto-generated method stub

	}

	@Override
	void destructMain() {
		sendButtonFlickr.removeFromParent();
		usernamePicasa.removeFromParent();
		passwordPicasa.removeFromParent();
		sendButtonPicasa.removeFromParent();
	}

	@Override
	void destructFooter() {
		// TODO Auto-generated method stub

	}



}
