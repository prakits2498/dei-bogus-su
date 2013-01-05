package rs.deibogus.client.interfacebuilder;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

	@Override
	public void buildHeader() {
		picasaLogo = page.createImage("http://2.bp.blogspot.com/-m9CD-tmeo1M/ULNHIBy_KkI/AAAAAAAAAHw/fpdDmzbEMNA/s1600/picasa-logo.png", 100, 10, "80px", "80px");
		picasaLogo.getElement().setId("picasaLogo");
		
		flickrLogo = page.createImage("http://www.peterboroughlibdems.org.uk/wp-content/uploads/2011/05/Flickr-logo.png", 200, 10, "80px", "80px");
		flickrLogo.getElement().setId("flickrLogo");
		
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
	    //PopUpPanelContents.add(sendButtonFlickr);
	    popup.setTitle("PopUpPanel");
	    popup.setWidget(PopUpPanelContents);
	    
	    popup.setVisible(false);
	    
	    class FlickrImage implements ClickHandler {
			public void onClick(ClickEvent event) {
				openPopUp();
			}

			private void openPopUp() {
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
			
		}
		
		flickrLogo.addClickHandler(new FlickrImage());
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



}
