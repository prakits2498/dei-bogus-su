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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
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

		/*FlowPanel wrapper = new FlowPanel();
		wrapper.getElement().setId("wrapper");

		FlowPanel form = new FlowPanel();
		form.getElement().setId("formID");
		form.setStyleName("login-form");

		FlowPanel loginHeader = new FlowPanel();
		FlowPanel loginContent = new FlowPanel();
		FlowPanel loginFooter = new FlowPanel();

		loginHeader.setStyleName("header");
		loginContent.setStyleName("content");
		loginFooter.setStyleName("footer");


		HTML htmlTitle = new HTML("<h1 align=\"center\"> My Social Life Login </h1>", true);
		//HTML htmlSubTitle = new HTML("<span align=\"center\"> Insert login to My Social Life WebApp </span>", true);

		loginHeader.add(htmlTitle);
		//RootPanel.get("loginHeader").add(htmlTitle);
		//RootPanel.get("loginHeader").add(htmlSubTitle);

		final TextBox username = new TextBox();
		username.getElement().setPropertyString("placeholder", "Username");
		username.setStyleName("input username");
		loginContent.add(username);
		//RootPanel.get("loginContent").add(username);

		final HTML htmlUsernameInput = new HTML("<div> </div>", true);
		htmlUsernameInput.setStyleName("user-icon");
		loginContent.add(htmlUsernameInput);
		//RootPanel.get("loginContent").add(htmlUsernameInput);

		final PasswordTextBox password = new PasswordTextBox();
		password.getElement().setPropertyString("placeholder", "Password");
		password.setStyleName("input password");
		loginContent.add(password);
		//RootPanel.get("loginContent").add(password);

		final HTML htmlPasswordInput = new HTML("<div></div>", true);
		htmlPasswordInput.setStyleName("pass-icon");
		loginContent.add(htmlPasswordInput);
		//RootPanel.get("loginContent").add(htmlPasswordInput);


		final Button submitButton = new Button();
		submitButton.setText("Login");
		submitButton.setStyleName("button");
		loginFooter.add(submitButton);
		//RootPanel.get("loginFooter").add(submitButton);

		final Button registerButton = new Button();
		registerButton.setText("Register");
		registerButton.setStyleName("register");
		loginFooter.add(registerButton);
		//RootPanel.get("loginFooter").add(registerButton);


		FlowPanel gradient = new FlowPanel();
		gradient.setStyleName("gradient");

		wrapper.add(form);
		wrapper.add(gradient);
		form.add(loginHeader);
		form.add(loginContent);
		form.add(loginFooter);
		RootPanel.get().add(wrapper);*/

		initialLogin();
		//photosPage();
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

		PageBuilder socialNetworkPage = new SocialNetworkLoginPageBuilder();
		page.setPageBuilder(socialNetworkPage);
		page.construct();
		
		PageBuilder imagesPage = new ImagePageBuilder();
		page.setPageBuilder(imagesPage);
		page.construct();

		//		final Button sendButtonFlickr = Button.wrap(Document.get().getElementById("sendButtonFlickrLogin"));
		//		final TextBox usernamePicasa = TextBox.wrap(Document.get().getElementById("usernamePicasaTxtBox"));
		//		final PasswordTextBox passwordPicasa = PasswordTextBox.wrap(Document.get().getElementById("passwordPicasaTxtBox"));
		//		final Button sendButtonPicasa = Button.wrap(Document.get().getElementById("sendButtonPicasaLogin"));
		//		
		////		final Image flickrLogo = Image.wrap(Document.get().getElementById("flickrLogo"));
		////		final Image picasaLogo = Image.wrap(Document.get().getElementById("picasaLogo"));
		////		final VerticalPanel PopUpPanelContents = (VerticalPanel)(Document.get().getElementById("popupContents"));
		////		private PopupPanel popup = PopupPanel.
		//		
		//		System.out.println(sendButtonFlickr.getText());
		//		
		//		class FlickrHandler implements ClickHandler {
		//			public void onClick(ClickEvent event) {
		//				sendLoginToServer();
		//			}
		//
		//			private void sendLoginToServer() {
		//
		//				if(sendButtonFlickr.getText().equals("Flickr Frob Request")){
		//					sendButtonFlickr.setText("Confirm Flickr Login");
		//					//textToServerLabel.setText(textToServer);
		//					//serverResponseLabel.setText("");
		//					greetingService.getURL(new AsyncCallback<String>() {
		//						public void onFailure(Throwable caught) {
		//							// Show the RPC error message to the user
		//							System.out.println(SERVER_ERROR);
		//						}
		//		
		//						public void onSuccess(String result) {
		//							System.out.println(result);
		//							Window.open(result,"_blank","");
		//						}
		//					});
		//				}
		//				else
		//				{
		//					greetingService.confirmLogin("", "flickr", new AsyncCallback<String>() {
		//						public void onFailure(Throwable caught) {
		//							// Show the RPC error message to the user
		//							System.out.println(SERVER_ERROR);
		//						}
		//		
		//						public void onSuccess(String result) {
		//							photosPage(); //TODO aqui n pode passar logo para a pagina das fotos pk sao 2 logins
		//						}
		//					});
		//				}
		//			}
		//		}
		//		
		//		class PicasaHandler implements ClickHandler, KeyUpHandler {
		//			public void onClick(ClickEvent event) {
		//				sendLoginToServer();
		//			}
		//
		//			@Override
		//			public void onKeyUp(KeyUpEvent event) {
		//				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		//					sendLoginToServer();
		//				}
		//			}
		//
		//			private void sendLoginToServer() {
		//				String txtToServer = usernamePicasa.getText() + " " + passwordPicasa.getText();
		//
		//				// First, we validate the input.
		//				//errorLabel.setText("");
		//
		//				System.out.println(txtToServer);
		//				
		//				if (!this.validateInput(usernamePicasa.getText()) || !this.validateInput(passwordPicasa.getText())) {
		//					//errorLabel.setText("Please enter a valid username or password");
		//					System.out.println(SERVER_ERROR);
		//					return;
		//				}
		//
		//				// Then, we send the input to the server.
		//				sendButtonPicasa.setEnabled(false);
		//				//textToServerLabel.setText(textToServer);
		//				//serverResponseLabel.setText("");
		//				greetingService.confirmLogin(txtToServer, "picasa", new AsyncCallback<String>() {
		//					public void onFailure(Throwable caught) {
		//						// Show the RPC error message to the user
		//						System.out.println(SERVER_ERROR);
		//					}
		//
		//					public void onSuccess(String result) {
		//						photosPage(); //TODO aqui n pode passar logo para a pagina das fotos pk sao 2 logins
		//					}
		//				});
		//			}
		//
		//			private boolean validateInput(String input) {
		//				if (input == null) {
		//					return false;
		//				}
		//
		//				if(input.contains(" "))
		//					return false;
		//
		//				return input.length() > 0;
		//			}
		//		}
		//		
		//		FlickrHandler handlerFlickr = new FlickrHandler();
		//		PicasaHandler handlerPicasa = new PicasaHandler();
		//		sendButtonFlickr.addClickHandler(handlerFlickr);
		//		sendButtonPicasa.addClickHandler(handlerPicasa);
		//		usernamePicasa.addKeyUpHandler(handlerPicasa);
		//		passwordPicasa.addKeyUpHandler(handlerPicasa);

	}

	public void photosPage() {
		/*RootPanel.get().setStyleName("body");
		Grid grid = new Grid(5, 5);
		grid.setStyleName("body");
		Image img = new Image("http://us.123rf.com/400wm/400/400/tr3gi/tr3gi1107/tr3gi110700001/10017592-rainbow-flower-multi-colored-petals-of-daisy-flower-isolated-on-white-background-range-of-happy-mult.jpg");
		//img.setStyleName("polaroid1");
		//grid.setWidget(0, 0, img);

		Image img2 = new Image("http://digital-photography-school.com/wp-content/uploads/2007/11/flower.jpg");
		img2.setTitle("flower");
		//img2.setStyleName("polaroid2");
		//grid.setWidget(0, 1, img2);


		//rootPanel.add(grid);

		HorizontalPanel panel = new HorizontalPanel();
		panel.setStyleName("polaroid1");
		panel.add(img);
		RootPanel.get().add(panel);

		HorizontalPanel panel2 = new HorizontalPanel();
		panel2.setStyleName("polaroid2");
		panel2.add(img2);
		RootPanel.get().add(panel2);*/

		FlowPanel navBar = new FlowPanel();
		navBar.getElement().setId("navBar");
		navBar.setStyleName("navbar navbar-fixed-top");

		FlowPanel navBarInner = new FlowPanel();
		navBarInner.getElement().setId("navBarInner");
		navBarInner.setStyleName("navbar-inner");

		FlowPanel navBarContainer = new FlowPanel();
		navBarContainer.getElement().setId("navBarContainer");
		navBarContainer.setStyleName("container");

		Anchor logo = new Anchor();
		logo.setStyleName("brand");
		logo.setText("MySocialLife");
		navBarContainer.add(logo);

		navBar.add(navBarInner);
		navBarInner.add(navBarContainer);
		RootPanel.get().add(navBar);


		FlowPanel photoContainer = new FlowPanel();
		photoContainer.getElement().setId("photoContainer");
		photoContainer.setStyleName("container");

		HTML horizontalLine = new HTML("<hr>");
		photoContainer.add(horizontalLine);

		FlowPanel row = new FlowPanel();
		row.getElement().setId("row");
		row.setStyleName("row");
		photoContainer.add(row);

		HorizontalPanel photoPanel = new HorizontalPanel();
		photoPanel.getElement().setId("photo");
		photoPanel.setStyleName("span4");
		row.add(photoPanel);

		HTML photo = new HTML("<h3>Deep Sky<small> By <a href=\"http://commons.wikimedia.org/wiki/File:Nature_1.jpg\">Srawat56</a><small><h3>" 
				+ "<a rel=\"lightbox[portfolio] tooltip\" title=\"This is a tooltip.\" href=\"img/thumb1.jpg\"><img src=\"img/thumb1.jpg\" alt=\"Thumbnail\"></a>");

		HTML photo2 = new HTML("<h3>Cenas<small> By <a href=\"http://commons.wikimedia.org/wiki/File:Nature%27s_Valley_(S._Africa)_2.jpg\">Cenass</a><small><h3>" 
				+ "<a rel=\"lightbox[portfolio] tooltip\" title=\"This is a tooltip.\" href=\"img/thumb2.jpg\"><img src=\"img/thumb2.jpg\" alt=\"Thumbnail\"></a>");

		photoPanel.setSpacing(100);

	}
}
