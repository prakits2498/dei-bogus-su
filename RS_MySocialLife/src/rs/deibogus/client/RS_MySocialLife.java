package rs.deibogus.client;

import java.util.ArrayList;

import rs.deibogus.client.interfacebuilder.ImagePageBuilder;
import rs.deibogus.client.interfacebuilder.Interface;
import rs.deibogus.client.interfacebuilder.LoginPageBuilder;
import rs.deibogus.client.interfacebuilder.PageBuilder;
import rs.deibogus.client.interfacebuilder.SocialNetworkLoginPageBuilder;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RS_MySocialLife implements EntryPoint {
	RootPanel rootPanel = RootPanel.get();

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private final Interface page = new Interface();

	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		initialLogin();
	}

	public void initialLogin() {
		PageBuilder login = new LoginPageBuilder();
		page.setPageBuilder(login);
		page.construct();

		final FlowPanel wrapper = (FlowPanel) RootPanel.get().getWidget(0);
		final FlowPanel form = (FlowPanel) wrapper.getWidget(0);

		final FlowPanel loginContent = (FlowPanel) form.getWidget(1);
		final TextBox username = (TextBox) loginContent.getWidget(0);
		final PasswordTextBox password = (PasswordTextBox) loginContent.getWidget(2);

		final FlowPanel loginFooter = (FlowPanel) form.getWidget(2);
		final Button sendButton = (Button) loginFooter.getWidget(0);
		final Button registerButton = (Button) loginFooter.getWidget(1); //TODO funçao de registar

		//final TextBox username = TextBox.wrap(Document.get().getElementById("usernameTxtBox"));
		//final PasswordTextBox password = PasswordTextBox.wrap(Document.get().getElementById("passwordTxtBox"));
		//final Button sendButton = Button.wrap(Document.get().getElementById("sendButtonLogin"));

		//final TextBox username = (TextBox) RootPanel.get("loginContent").getWidget(0);
		//final PasswordTextBox password = (PasswordTextBox) RootPanel.get("loginContent").getWidget(2);
		//final Button sendButton = (Button) RootPanel.get("loginFooter").getWidget(0);

		class MyHandler implements ClickHandler, KeyUpHandler {
			public void onClick(ClickEvent event) {
				sendLoginToServer();
			}

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendLoginToServer();
				}
			}

			private void sendLoginToServer() {
				String txtToServer = username.getText() + " " + password.getText();
				
				if (!this.validateInput(username.getText()) || !this.validateInput(password.getText())) {
					//errorLabel.setText("Please enter a valid username or password");
					System.out.println(SERVER_ERROR);
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				greetingService.confirmLogin(txtToServer, "app", new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						System.out.println(SERVER_ERROR);
					}

					public void onSuccess(String result) {
						page.destruct();
						initialPage();
					}
				});
			}

			private boolean validateInput(String input) {
				if (input == null) {
					return false;
				}

				if(input.contains(" "))
					return false;

				return input.length() > 0;
			}
		}

		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		username.addKeyUpHandler(handler);
		password.addKeyUpHandler(handler);
	}

	public void initialPage() {
		PageBuilder socialNetworkLogins = new SocialNetworkLoginPageBuilder();
		page.setPageBuilder(socialNetworkLogins);
		page.construct();
		
		ArrayList<String> photosURL = new ArrayList<String>(); //TODO meter as fotos neste URL ou nalgum objecto k tenha la a lista de fotos
		PageBuilder imagesPage = new ImagePageBuilder(photosURL);
		page.setPageBuilder(imagesPage);
		page.construct();

	}

}
