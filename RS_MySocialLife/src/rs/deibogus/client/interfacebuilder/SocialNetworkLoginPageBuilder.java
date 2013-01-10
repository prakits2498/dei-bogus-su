package rs.deibogus.client.interfacebuilder;

import java.util.ArrayList;

import rs.deibogus.client.ClientData;
import rs.deibogus.client.GreetingService;
import rs.deibogus.client.GreetingServiceAsync;
import rs.deibogus.shared.Foto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

/**
 * Builder Design Pattern
 * "ConcreteBuilder"
 * @author bfurtado, durval
 *
 */
public class SocialNetworkLoginPageBuilder extends PageBuilder {

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	private Interface builder;

	private static final String UPLOAD_ACTION_URL = GWT.getModuleBaseURL() + "upload";

	private TextBox usernamePicasa;
	private PasswordTextBox passwordPicasa;
	private Button sendButtonFlickr;
	private Button sendButtonPicasa;
	private Image picasaLogo;
	private Image flickrLogo;
	private Image uploadImage;
	private ClientData client = ClientData.getInstance();
	private FormPanel form;
	
	public PopupPanel popupPicasa;
	public PopupPanel popupFlickr;
	public PopupPanel popupUpload;

	public SocialNetworkLoginPageBuilder(ImagePageBuilder imagesPage){
		builder = new Interface();
		builder.setPageBuilder(imagesPage);
	}

	@Override
	public void buildHeader() {
		picasaLogo = page.createImage("http://2.bp.blogspot.com/-m9CD-tmeo1M/ULNHIBy_KkI/AAAAAAAAAHw/fpdDmzbEMNA/s1600/picasa-logo.png", "60px", "60px");
		picasaLogo.setStyleName("gallery");
		picasaLogo.getElement().setId("picasaLogo");
		picasaLogo.setTitle("Login to Picasa");
		picasaLogo.getElement().getStyle().setCursor(Cursor.POINTER); 

		flickrLogo = page.createImage("http://www.peterboroughlibdems.org.uk/wp-content/uploads/2011/05/Flickr-logo.png", "55px", "55px");
		flickrLogo.setStyleName("gallery");
		flickrLogo.getElement().setId("flickrLogo");
		flickrLogo.setTitle("Login to Flickr");
		flickrLogo.getElement().getStyle().setCursor(Cursor.POINTER); 
		
		uploadImage = page.createImage("http://cdn1.iconfinder.com/data/icons/simplicio/64x64/file_add.png", "50px", "50px");
		uploadImage.setStyleName("gallery");
		uploadImage.getElement().setId("uploadImage");
		uploadImage.setTitle("Upload Image");
		uploadImage.getElement().getStyle().setCursor(Cursor.POINTER); 
	}

	@Override
	public void destructHeader() {
		picasaLogo.removeFromParent();
		flickrLogo.removeFromParent();
		uploadImage.removeFromParent();
	}

	@Override
	public void buildMain() {
		buildPopUp();
		buildPopupUpload();
	}
	
	private void buildPopupUpload() {
		form = page.createUploadForm(UPLOAD_ACTION_URL);
		
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				popupUpload.setVisible(false);
				
				greetingService.getPhotos(new AsyncCallback<ArrayList<Foto>>() {
					public void onFailure(Throwable caught) {
						Window.alert("No photos.");
					}

					public void onSuccess(ArrayList<Foto> result) {
						client.setFotos(result);
						builder.destruct();
						builder.construct();
					}
				});
				
				
				Window.alert(event.getResults());
			}
		});
		
		
		uploadImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ArrayList<Widget> w = new ArrayList<Widget>();
				w.add(form);
				popupUpload = page.createPopupAndContents(w, "popupContentsUpload");
			}
		});
	}
	
	@Override
	void destructMain() {
		sendButtonFlickr.removeFromParent();
		usernamePicasa.removeFromParent();
		passwordPicasa.removeFromParent();
		sendButtonPicasa.removeFromParent();
		
		form.removeFromParent();
	}

	private void buildPopUp() {
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


	@Override
	public void buildFooter() {
	}

	@Override
	void destructFooter() {
	}

	@Override
	public void buildStructure() {
	}

	@Override
	public void destructStructure() {
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
							Window.alert("Login error.");
						}

						public void onSuccess(String result) {
							//System.out.println(result);
							Window.open(result,"_blank","");
						}
					});
				}
				else
				{
					greetingService.confirmLogin("", "flickr", new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							Window.alert("Login error.");
						}

						public void onSuccess(String result) {
							popupFlickr.setVisible(false);
							Window.alert("Flickr Login was successful!");
							greetingService.getPhotos(new AsyncCallback<ArrayList<Foto>>() {
								public void onFailure(Throwable caught) {
									Window.alert("No photos.");
								}

								public void onSuccess(ArrayList<Foto> result) {
									//Window.alert("Recebeu " + Integer.toString(result.size()) + "fotos!");
									//client.getFotos().addAll(result);
									client.setFotos(result);
									builder.destruct();
									builder.construct();
								}
							});
						}
					});
				}
			}

		}

		class PicasaHandler implements ClickHandler, KeyUpHandler {
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
				String txtToServer = usernamePicasa.getText() + " " + passwordPicasa.getText();

				if (!this.validateInput(usernamePicasa.getText()) || !this.validateInput(passwordPicasa.getText())) {
					Window.alert("Please insert a valid username and/or password.");
					return;
				}

				sendButtonPicasa.setEnabled(false);
				greetingService.confirmLogin(txtToServer, "picasa", new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						Window.alert("Login Error.");
						sendButtonPicasa.setEnabled(true);
						enter = false;
					}

					public void onSuccess(String result) {
						popupPicasa.setVisible(false);
						Window.alert("Login on Picas was successful!");

						greetingService.getPhotos(new AsyncCallback<ArrayList<Foto>>() {
							public void onFailure(Throwable caught) {
								Window.alert("No photos!");
							}

							public void onSuccess(ArrayList<Foto> result) {
								//Window.alert("Recebeu " + Integer.toString(result.size()) + "fotos!");
								//client.getFotos().addAll(result);
								client.setFotos(result);
								builder.destruct();
								builder.construct();
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

				popupFlickr = page.createPopupAndContents(w, "popupContentsFlickr");
			}
		});

		picasaLogo.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ArrayList<Widget> w = new ArrayList<Widget>();
				w.add(usernamePicasa);
				w.add(passwordPicasa);
				w.add(sendButtonPicasa);

				popupPicasa = page.createPopupAndContents(w, "popupContentsPicasa");
				usernamePicasa.setCursorPos(0);
			}
		});
	}

}
