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
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

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

	private TextBox usernameFlickr;
	private TextBox usernamePicasa;
	private PasswordTextBox passwordFlickr;
	private PasswordTextBox passwordPicasa;
	private Button sendButtonFlickr;
	private Button sendButtonPicasa;
	private Image picasaLogo;
	private Image flickrLogo;
	private VerticalPanel PopUpPanelContents;
	private PopupPanel popup;
	//private Button photoTesting;

	@Override
	public void buildHeader() {
		picasaLogo = page.createImage("http://2.bp.blogspot.com/-m9CD-tmeo1M/ULNHIBy_KkI/AAAAAAAAAHw/fpdDmzbEMNA/s1600/picasa-logo.png", 100, 10, "60px", "60px");
		picasaLogo.setStyleName("gallery");
		picasaLogo.getElement().setId("picasaLogo");
		
		flickrLogo = page.createImage("http://www.peterboroughlibdems.org.uk/wp-content/uploads/2011/05/Flickr-logo.png", 200, 10, "55px", "55px");
		flickrLogo.setStyleName("gallery");
		flickrLogo.getElement().setId("flickrLogo");
		
		//photoTesting = page.createButton("Teste Photos", true, "", "root");
		
		sendButtonFlickr = page.createHiddenButton("Flickr Frob Request");
		sendButtonFlickr.getElement().setId("sendButtonFlickrLogin");
		
		usernamePicasa = page.createHiddenTextBox("Insert Username");
		usernamePicasa.getElement().setId("usernamePicasaTxtBox");
		
		passwordPicasa = page.createHiddenPasswordTextBox();
		passwordPicasa.getElement().setId("passwordPicasaTxtBox");
		
		sendButtonPicasa = page.createHiddenButton("Picasa Login");
		sendButtonPicasa.getElement().setId("sendButtonPicasaLogin");
		
		popup = page.createHiddenPopupPanel("popup");
		popup.getElement().setId("popup");
	    popup.center();
	    popup.setStyleName("demo-PopUpPanel");
	    PopUpPanelContents = page.createHiddenVerticalPanel("popupContent");
	    PopUpPanelContents.getElement().setId("popupContents");
	    popup.setTitle("PopUpPanel");
	    popup.setWidget(PopUpPanelContents);
	    PopUpPanelContents.add(sendButtonFlickr);
	    PopUpPanelContents.add(usernamePicasa);
	    PopUpPanelContents.add(passwordPicasa);
	    PopUpPanelContents.add(sendButtonPicasa);
	    popup.setVisible(false);
	    
	    handlerMethods();
	}
	
	public void handlerMethods(){
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
							Window.alert("Login no Flickr bem sucedido!");
							popup.setVisible(false);
							greetingService.getPhotos(new AsyncCallback<ArrayList<Foto>>() {
								public void onFailure(Throwable caught) {
									// Show the RPC error message to the user
									System.out.println(SERVER_ERROR);
								}

								public void onSuccess(ArrayList<Foto> result) {
									Window.alert("Recebeu " + Integer.toString(result.size()) + "fotos!");
									
//									System.out.println(result.get(0).getTitle() + " " + result.get(0).getUrl());
//									System.out.println(result.get(1).getTitle() + " " + result.get(1).getUrl());
									// TODO Auto-generated method stub
									
								}
							});
							
							//photosPage(); //TODO aqui n pode passar logo para a pagina das fotos pk sao 2 logins
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
						Window.alert("Login no Picasa bem sucedido!");
						popup.setVisible(false);
						greetingService.getPhotos(new AsyncCallback<ArrayList<Foto>>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								System.out.println(SERVER_ERROR);
							}

							public void onSuccess(ArrayList<Foto> result) {
								Window.alert("Recebeu " + Integer.toString(result.size()) + "fotos!");
								
//								System.out.println(result.get(0).getTitle() + " " + result.get(0).getUrl());
//								System.out.println(result.get(1).getTitle() + " " + result.get(1).getUrl());
								// TODO Auto-generated method stub
								
							}
						});
						
						//photosPage(); //TODO aqui n pode passar logo para a pagina das fotos pk sao 2 logins
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
		
		class FlickrImage implements ClickHandler {
			public void onClick(ClickEvent event) {
				PopUpHandler();
			}

			private void PopUpHandler() {
				if(!popup.isVisible()){
					//PopUpPanelContents.clear();
					PopUpPanelContents.getWidget(0).setVisible(true);
					PopUpPanelContents.getWidget(1).setVisible(false);
					PopUpPanelContents.getWidget(2).setVisible(false);
					PopUpPanelContents.getWidget(3).setVisible(false);
					popup.setVisible(true);
				    //popup = page.createPopupPanel("popup");
				    //popup.center();
				    //popup.setStyleName("demo-PopUpPanel");
				    //PopUpPanelContents = page.createEmptyVerticalPanel("popupContent");
				    //popup.setTitle("PopUpPanel");
	//			    message = new HTML("Click 'Close' to close");
	//			    message.setStyleName("demo-PopUpPanel-message");
	//			    ClickHandler listener = new ClickListener()
	//			    {
	//			        public void onClick(Widget sender)
	//			        {
	//			            popup.hide();
	//			        }
	//			    };
	//			    button = new Button("Close", listener);
	//			    holder = new SimplePanel();
	//			    holder.add(button);
	//			    holder.setStyleName("demo-PopUpPanel-footer");
				    //PopUpPanelContents.add(new Button("teste"));
				    //PopUpPanelContents.add(holder);
				    //popup.setWidget(PopUpPanelContents);
				}
				else{
					popup.setVisible(false);
				}
			    
			}
			
		}
		
		flickrLogo.addClickHandler(new FlickrImage());
		
		class PicasaImage implements ClickHandler {
			public void onClick(ClickEvent event) {
				PopUpHandler();
			}

			private void PopUpHandler() {
				if(!popup.isVisible()){
					//PopUpPanelContents.clear();
					PopUpPanelContents.getWidget(0).setVisible(false);
					PopUpPanelContents.getWidget(1).setVisible(true);
					PopUpPanelContents.getWidget(2).setVisible(true);
					PopUpPanelContents.getWidget(3).setVisible(true);
					popup.setVisible(true);
				    //popup = page.createPopupPanel("popup");
				    //popup.center();
				    //popup.setStyleName("demo-PopUpPanel");
				    //PopUpPanelContents = page.createEmptyVerticalPanel("popupContent");
				    //popup.setTitle("PopUpPanel");
	//			    message = new HTML("Click 'Close' to close");
	//			    message.setStyleName("demo-PopUpPanel-message");
	//			    ClickHandler listener = new ClickListener()
	//			    {
	//			        public void onClick(Widget sender)
	//			        {
	//			            popup.hide();
	//			        }
	//			    };
	//			    button = new Button("Close", listener);
	//			    holder = new SimplePanel();
	//			    holder.add(button);
	//			    holder.setStyleName("demo-PopUpPanel-footer");
				    //PopUpPanelContents.add(new Button("teste"));
				    //PopUpPanelContents.add(holder);
				    //popup.setWidget(PopUpPanelContents);
				}
				else{
					popup.setVisible(false);
				}
				
			}
			
		}
		
		picasaLogo.addClickHandler(new PicasaImage());
	}

	@Override
	public void buildMain() {
		//usernameFlickr = page.createTextBox("Insert Username", 147, 44);
		//passwordFlickr = page.createPasswordTextBox(150, 84);
//		sendButtonFlickr = page.createButton("Flickr Frob Request", 196, 137);
//		sendButtonFlickr.getElement().setId("sendButtonFlickrLogin");
//		
//		usernamePicasa = page.createTextBox("Insert Username", 147, 200);
//		usernamePicasa.getElement().setId("usernamePicasaTxtBox");
//		
//		passwordPicasa = page.createPasswordTextBox(150, 240);
//		passwordPicasa.getElement().setId("passwordPicasaTxtBox");
//		
//		sendButtonPicasa = page.createButton("Picasa Login", 196, 290);
//		sendButtonPicasa.getElement().setId("sendButtonPicasaLogin");
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
