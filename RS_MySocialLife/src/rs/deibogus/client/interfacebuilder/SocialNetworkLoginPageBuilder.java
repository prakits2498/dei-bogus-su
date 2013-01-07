package rs.deibogus.client.interfacebuilder;

import java.util.ArrayList;

import rs.deibogus.client.GreetingService;
import rs.deibogus.client.GreetingServiceAsync;
import rs.deibogus.shared.Foto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Builder Design Pattern
 * "ConcreteBuilder"
 * @author bfurtado, durval
 *
 */
public class SocialNetworkLoginPageBuilder extends PageBuilder {

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private TextBox usernamePicasa;
	private PasswordTextBox passwordPicasa;
	private Button sendButtonFlickr;
	private Button sendButtonPicasa;
	private Image picasaLogo;
	private Image flickrLogo;

	@Override
	public void buildHeader() {
		picasaLogo = page.createImage("http://2.bp.blogspot.com/-m9CD-tmeo1M/ULNHIBy_KkI/AAAAAAAAAHw/fpdDmzbEMNA/s1600/picasa-logo.png", "60px", "60px");
		picasaLogo.setStyleName("gallery");
		picasaLogo.getElement().setId("picasaLogo");

		flickrLogo = page.createImage("http://www.peterboroughlibdems.org.uk/wp-content/uploads/2011/05/Flickr-logo.png", "55px", "55px");
		flickrLogo.setStyleName("gallery");
		flickrLogo.getElement().setId("flickrLogo");

		sendButtonFlickr = page.createHiddenButton("Flickr Frob Request");
		sendButtonFlickr.getElement().setId("sendButtonFlickrLogin");

		//usernamePicasa = page.createHiddenTextBox("Insert Username");
		usernamePicasa = page.createTextBox("Username", true, "", "");
		usernamePicasa.getElement().setId("usernamePicasaTxtBox");

		//passwordPicasa = page.createHiddenPasswordTextBox();
		passwordPicasa = page.createPasswordTextBox("Password", true, "", "");
		passwordPicasa.getElement().setId("passwordPicasaTxtBox");

		//sendButtonPicasa = page.createHiddenButton("Picasa Login");
		sendButtonPicasa = page.createButton("Login", false, "", "");
		sendButtonPicasa.getElement().setId("sendButtonPicasaLogin");

		handlerMethods();
	}

	public void handlerMethods() {
		class FlickrHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				sendLoginToServer();
			}

			private void sendLoginToServer() {

				if(sendButtonFlickr.getText().equals("Flickr Frob Request")){
					sendButtonFlickr.setText("Confirm Flickr Login");
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
							Window.alert("Login no Flickr bem sucedido!");
							greetingService.getPhotos(new AsyncCallback<ArrayList<Foto>>() {
								public void onFailure(Throwable caught) {
									// Show the RPC error message to the user
									System.out.println(SERVER_ERROR);
								}

								public void onSuccess(ArrayList<Foto> result) {
									Window.alert("Recebeu " + Integer.toString(result.size()) + "fotos!");
								}
							});
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

				if (!this.validateInput(usernamePicasa.getText()) || !this.validateInput(passwordPicasa.getText())) {
					System.out.println(SERVER_ERROR);
					return;
				}

				// Then, we send the input to the server.
				sendButtonPicasa.setEnabled(false);
				greetingService.confirmLogin(txtToServer, "picasa", new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						System.out.println(SERVER_ERROR);
					}

					public void onSuccess(String result) {
						Window.alert("Login no Picasa bem sucedido!");

						greetingService.getPhotos(new AsyncCallback<ArrayList<Foto>>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								System.out.println(SERVER_ERROR);
							}

							public void onSuccess(ArrayList<Foto> result) {
								Window.alert("Recebeu " + Integer.toString(result.size()) + "fotos!");
							}
						});
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

		flickrLogo.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ArrayList<Widget> w = new ArrayList<Widget>();
				w.add(sendButtonFlickr);

				final PopupPanel popup = page.createPopupAndContents(w, "popupContentsFlickr");
			}
		});

		picasaLogo.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ArrayList<Widget> w = new ArrayList<Widget>();
				w.add(usernamePicasa);
				w.add(passwordPicasa);
				w.add(sendButtonPicasa);

				final PopupPanel popup = page.createPopupAndContents(w, "popupContentsPicasa");
			}
		});
	}

	@Override
	public void buildMain() {

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

	@Override
	public void buildStructure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destructStructure() {
		// TODO Auto-generated method stub

	}



}
