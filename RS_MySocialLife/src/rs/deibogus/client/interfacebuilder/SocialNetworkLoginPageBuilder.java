package rs.deibogus.client.interfacebuilder;

import java.util.ArrayList;

import rs.deibogus.client.ClientData;
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
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

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

	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private TextBox usernamePicasa;
	private PasswordTextBox passwordPicasa;
	private Button sendButtonFlickr;
	private Button sendButtonPicasa;
	private Image picasaLogo;
	private Image flickrLogo;
	private ClientData client = ClientData.getInstance();
	private FormPanel form;

	public SocialNetworkLoginPageBuilder(ImagePageBuilder imagesPage){
		builder = new Interface();
		builder.setPageBuilder(imagesPage);
	}

	@Override
	public void buildHeader() {
		picasaLogo = page.createImage("http://2.bp.blogspot.com/-m9CD-tmeo1M/ULNHIBy_KkI/AAAAAAAAAHw/fpdDmzbEMNA/s1600/picasa-logo.png", "60px", "60px");
		picasaLogo.setStyleName("gallery");
		picasaLogo.getElement().setId("picasaLogo");

		flickrLogo = page.createImage("http://www.peterboroughlibdems.org.uk/wp-content/uploads/2011/05/Flickr-logo.png", "55px", "55px");
		flickrLogo.setStyleName("gallery");
		flickrLogo.getElement().setId("flickrLogo");

		// Create a FormPanel and point it at a service.
		form = new FormPanel();
		form.setAction(UPLOAD_ACTION_URL);

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		// Create a TextBox, giving it a name so that it will be submitted.
		final TextBox tb = new TextBox();
		tb.setName("textBoxFormElement");
		panel.add(tb);

		// Create a ListBox, giving it a name and some values to be associated
		// with its options.
		ListBox lb = new ListBox();
		lb.setName("listBoxFormElement");
		lb.addItem("foo", "fooValue");
		lb.addItem("bar", "barValue");
		lb.addItem("baz", "bazValue");
		panel.add(lb);

		// Create a FileUpload widget.
		FileUpload upload = new FileUpload();
		upload.setName("uploadFormElement");
		panel.add(upload);

		// Add a 'submit' button.
		panel.add(new Button("Submit", new ClickHandler() {
			public void onClick(ClickEvent event) {
				form.submit();
			}
		}));

		// Add an event handler to the form.
		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				// This event is fired just before the form is submitted. We can
				// take this opportunity to perform validation.
				if (tb.getText().length() == 0) {
					Window.alert("The text box must not be empty");
					event.cancel();
				}
			}
		});

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// When the form submission is successfully completed, this
				// event is fired. Assuming the service returned a response of type
				// text/html, we can get the result text here (see the FormPanel
				// documentation for further explanation).
				Window.alert(event.getResults());
			}
		});

		RootPanel.get().add(form);
	}

	@Override
	public void buildMain() {
		buildPopUp();
	}

	public void buildPopUp(){
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
							greetingService.getPhotos("flickr",new AsyncCallback<ArrayList<Foto>>() {
								public void onFailure(Throwable caught) {
									// Show the RPC error message to the user
									System.out.println(SERVER_ERROR);
								}

								public void onSuccess(ArrayList<Foto> result) {
									Window.alert("Recebeu " + Integer.toString(result.size()) + "fotos!");
									client.getFotos().addAll(result);
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
					System.out.println(SERVER_ERROR);
					return;
				}

				// Then, we send the input to the server.
				sendButtonPicasa.setEnabled(false);
				greetingService.confirmLogin(txtToServer, "picasa", new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						//System.out.println(SERVER_ERROR);
						Window.alert("Erro ao fazer login.");
						sendButtonPicasa.setEnabled(true);
						enter = false;
					}

					public void onSuccess(String result) {
						Window.alert("Login no Picasa bem sucedido!");

						greetingService.getPhotos("picasa",new AsyncCallback<ArrayList<Foto>>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								//System.out.println(SERVER_ERROR);
								Window.alert("N‹o tem fotos!");
							}

							public void onSuccess(ArrayList<Foto> result) {
								Window.alert("Recebeu " + Integer.toString(result.size()) + "fotos!");
								client.getFotos().addAll(result);
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

}
