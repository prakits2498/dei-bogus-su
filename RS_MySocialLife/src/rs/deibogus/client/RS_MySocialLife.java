package rs.deibogus.client;

import java.util.ArrayList;

import rs.deibogus.client.interfacebuilder.ImagePageBuilder;
import rs.deibogus.client.interfacebuilder.Interface;
import rs.deibogus.client.interfacebuilder.LoginPageBuilder;
import rs.deibogus.client.interfacebuilder.PageBuilder;
import rs.deibogus.client.interfacebuilder.SocialNetworkLoginPageBuilder;
import rs.deibogus.shared.Foto;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
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

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		initialLogin();
		/*if(Cookies.getCookie("logged") == null)
			initialLogin();
		else
			initialPage();*/
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
		final Button registerButton = (Button) loginFooter.getWidget(1);

		//final TextBox username = TextBox.wrap(Document.get().getElementById("usernameTxtBox"));
		//final PasswordTextBox password = PasswordTextBox.wrap(Document.get().getElementById("passwordTxtBox"));
		//final Button sendButton = Button.wrap(Document.get().getElementById("sendButtonLogin"));

		//final TextBox username = (TextBox) RootPanel.get("loginContent").getWidget(0);
		//final PasswordTextBox password = (PasswordTextBox) RootPanel.get("loginContent").getWidget(2);
		//final Button sendButton = (Button) RootPanel.get("loginFooter").getWidget(0);
		
		registerButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!validateInput(username.getText()) || !validateInput(password.getText())) {
					Window.alert("Introduza um username e password v‡lidos.");
					return;
				}
				
				String txtToServer = username.getText() + " " + password.getText();
				registerButton.setEnabled(false);
				greetingService.registerUser(txtToServer, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						Window.alert("Erro ao registar.");
						registerButton.setEnabled(true);
					}

					public void onSuccess(String result) {
						Window.alert("Conta registada com sucesso.");
					}
				});
				
			}
		});

		class MyHandler implements ClickHandler, KeyUpHandler {
			private boolean enter = false;
			
			public void onClick(ClickEvent event) {
				sendLoginToServer();
			}

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !enter) {
					sendLoginToServer();
					enter = true;
				}
			}

			private void sendLoginToServer() {
				String txtToServer = username.getText() + " " + password.getText();
				
				if (!validateInput(username.getText()) || !validateInput(password.getText())) {
					Window.alert("Introduza um username e password v‡lidos.");
					enter = false;
					return;
				}

				sendButton.setEnabled(false);
				greetingService.confirmLogin(txtToServer, "app", new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						//System.out.println(SERVER_ERROR);
						
						Window.alert("Login error.");
						sendButton.setEnabled(true);
						enter = false;
					}

					public void onSuccess(String result) {
						Cookies.setCookie("logged", "true");
						page.destruct();
						initialPage();
					}
				});
			}
		}

		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		username.addKeyUpHandler(handler);
		password.addKeyUpHandler(handler);
	}
	
	private boolean validateInput(String input) {
		if (input == null) {
			return false;
		}

		if(input.contains(" "))
			return false;

		return input.length() > 0;
	}

	public void initialPage() {
		/*greetingService.getPhotos(new AsyncCallback<ArrayList<Foto>>() {
			public void onFailure(Throwable caught) {
				//Window.alert("Nao tem fotos.");
			}

			public void onSuccess(ArrayList<Foto> result) {
				ClientData.getInstance().setFotos(result);
			}
		});*/
		
		
		PageBuilder imagesPage = new ImagePageBuilder();
		
		PageBuilder socialNetworkLogins = new SocialNetworkLoginPageBuilder((ImagePageBuilder)imagesPage);
		
		page.setPageBuilder(socialNetworkLogins);
		page.construct();
		
		page.setPageBuilder(imagesPage);
		page.construct();
		
		
		
	}

}
