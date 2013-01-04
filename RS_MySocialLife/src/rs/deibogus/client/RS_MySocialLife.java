package rs.deibogus.client;

import rs.deibogus.client.interfacebuilder.Interface;
import rs.deibogus.client.interfacebuilder.LoginPageBuilder;
import rs.deibogus.client.interfacebuilder.PageBuilder;
import rs.deibogus.client.interfacebuilder.SocialNetworkLoginPageBuilder;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
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
		
		final TextBox username = TextBox.wrap(Document.get().getElementById("usernameTxtBox"));
		final PasswordTextBox password = PasswordTextBox.wrap(Document.get().getElementById("passwordTxtBox"));
		final Button sendButton = Button.wrap(Document.get().getElementById("sendButtonLogin"));

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

				// First, we validate the input.
				//errorLabel.setText("");

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
		System.out.println("initial page");
		
		PageBuilder login = new SocialNetworkLoginPageBuilder();
		page.setPageBuilder(login);
		page.construct();
		
		final Button sendButtonFlickr = Button.wrap(Document.get().getElementById("sendButtonFlickrLogin"));
		
		final TextBox usernamePicasa = TextBox.wrap(Document.get().getElementById("usernamePicasaTxtBox"));
		final PasswordTextBox passwordPicasa = PasswordTextBox.wrap(Document.get().getElementById("passwordPicasaTxtBox"));
		final Button sendButtonPicasa = Button.wrap(Document.get().getElementById("sendButtonPicasaLogin"));
		
		class FlickrHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				sendLoginToServer();
			}

			private void sendLoginToServer() {

				if(sendButtonFlickr.getText().equals("Flickr Frob Request")){
					sendButtonFlickr.setText("Confirm Flickr Login");
					//textToServerLabel.setText(textToServer);
					//serverResponseLabel.setText("");
					greetingService.getURL(new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							System.out.println(SERVER_ERROR);
						}
		
						public void onSuccess(String result) {
							System.out.println(result);
							Window.open(result,"_blank","");
						}
					});
				}
				else
				{
					greetingService.confirmLogin("", "flickr", new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							System.out.println(SERVER_ERROR);
						}
		
						public void onSuccess(String result) {
							photosPage(); //TODO aqui n pode passar logo para a pagina das fotos pk sao 2 logins
						}
					});
				}
			}
		}
		
		class PicasaHandler implements ClickHandler, KeyUpHandler {
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
				String txtToServer = usernamePicasa.getText() + " " + passwordPicasa.getText();

				// First, we validate the input.
				//errorLabel.setText("");

				System.out.println(txtToServer);
				
				if (!this.validateInput(usernamePicasa.getText()) || !this.validateInput(passwordPicasa.getText())) {
					//errorLabel.setText("Please enter a valid username or password");
					System.out.println(SERVER_ERROR);
					return;
				}

				// Then, we send the input to the server.
				sendButtonPicasa.setEnabled(false);
				//textToServerLabel.setText(textToServer);
				//serverResponseLabel.setText("");
				greetingService.confirmLogin(txtToServer, "picasa", new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						System.out.println(SERVER_ERROR);
					}

					public void onSuccess(String result) {
						photosPage(); //TODO aqui n pode passar logo para a pagina das fotos pk sao 2 logins
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
		
		FlickrHandler handlerFlickr = new FlickrHandler();
		PicasaHandler handlerPicasa = new PicasaHandler();
		sendButtonFlickr.addClickHandler(handlerFlickr);
		sendButtonPicasa.addClickHandler(handlerPicasa);
		usernamePicasa.addKeyUpHandler(handlerPicasa);
		passwordPicasa.addKeyUpHandler(handlerPicasa);
		
	}
	
	public void photosPage() {
		
	}
}
