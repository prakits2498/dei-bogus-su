package rs.deibogus.client.interfacebuilder;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Builder Design Pattern
 * "ConcreteBuilder"
 * @author bfurtado, durval
 *
 */
public class LoginPageBuilder extends PageBuilder {
	
	private HTML title;
	private HTML subTitle;
	private TextBox username;
	private HTML htmlDivUsernameIcon;
	private HTML htmlDivPasswordIcon;
	private PasswordTextBox password;
	private Button sendButton;
	private Button registerButton;
	
	private FlowPanel wrapper;
	private FlowPanel form;
	private FlowPanel loginHeader;
	private FlowPanel loginContent;
	private FlowPanel loginFooter;
	private FlowPanel gradient;

	public LoginPageBuilder() {
	}
	
	@Override
	public void buildStructure() {
		wrapper = page.createFlowPanel("wrapper", "");
		form = page.createFlowPanel("formID", "login-form");
		loginHeader = page.createFlowPanel("loginHeader", "header");
		loginContent = page.createFlowPanel("loginContent", "content");
		loginFooter = page.createFlowPanel("loginFooter", "footer");
		gradient = page.createFlowPanel("grad", "gradient");
		
		wrapper.add(form);
		wrapper.add(gradient);
		form.add(loginHeader);
		form.add(loginContent);
		form.add(loginFooter);
		
		RootPanel.get().add(wrapper);
			
	}
	
	@Override
	public void destructStructure() {
		wrapper.removeFromParent();
		form.removeFromParent();
		loginHeader.removeFromParent();
		loginContent.removeFromParent();
		loginFooter.removeFromParent();
		gradient.removeFromParent();
	}

	@Override
	public void buildHeader() {
		title = page.createHtmlText("My Social Life Login", "h1", "center", true, "");
		//subTitle = page.createHtmlText("please insert your MySocialLife login", "span", "center", true, "loginHeader");
		loginHeader.add(title);
	}
	
	@Override
	public void destructHeader() {
		title.removeFromParent();
		//subTitle.removeFromParent();
	}

	@Override
	public void buildMain() {
		//username = page.createTextBox("Insert Username", 147, 44);
		username = page.createTextBox("Username", true, "input username", "");
		username.getElement().setId("usernameTxtBox");
		htmlDivUsernameIcon = page.createHtmlDiv("userIcon", "user-icon", "");
		loginContent.add(username);
		loginContent.add(htmlDivUsernameIcon);
		
		//password = page.createPasswordTextBox(150, 84);
		password = page.createPasswordTextBox("Password", true, "input password", "");
		password.getElement().setId("passwordTxtBox");
		htmlDivPasswordIcon = page.createHtmlDiv("passIcon", "pass-icon", "");
		loginContent.add(password);
		loginContent.add(htmlDivPasswordIcon);
		
		//sendButton = page.createButton("Login", 196, 137);
		sendButton = page.createButton("Login", true, "button", "");
		sendButton.getElement().setId("sendButtonLogin");
		loginFooter.add(sendButton);
		
		registerButton = page.createButton("Register", true, "register", "");
		registerButton.getElement().setId("registerButtonLogin");
		loginFooter.add(registerButton);
	}
	
	@Override
	public void destructMain() {
		username.removeFromParent();
		htmlDivUsernameIcon.removeFromParent();
		password.removeFromParent();
		htmlDivPasswordIcon.removeFromParent();
		sendButton.removeFromParent();
		registerButton.removeFromParent();
	}

	@Override
	public void buildFooter() {
		
	}
	
	@Override
	public void destructFooter() {
		//TODO
	}

}
