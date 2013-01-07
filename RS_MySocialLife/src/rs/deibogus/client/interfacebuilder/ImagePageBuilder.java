package rs.deibogus.client.interfacebuilder;

import java.util.ArrayList;

import rs.deibogus.client.GreetingService;
import rs.deibogus.client.GreetingServiceAsync;
import rs.deibogus.shared.Foto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Builder Design Pattern
 * "ConcreteBuilder"
 * @author bfurtado, durval
 *
 */
public class ImagePageBuilder extends PageBuilder {
	
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private FlowPanel navBar;
	private FlowPanel navBarInner;
	private FlowPanel navBarContainer;
	
	private FlowPanel photoContainer;
	private HTML horizontalLine;
	
	private FlowPanel row;
	private ArrayList<HorizontalPanel> photoPanels;
	
	private Anchor logo;
	private HTML footer;
	
	private ArrayList<HTML> photos;
	private ArrayList<String> photosURL;
	
	public ImagePageBuilder(ArrayList<String> photosURL) {
		this.photosURL = photosURL;
	}
	
	@Override
	public void buildStructure() {
		//HEADER STRUCTURE
		navBar = new FlowPanel();
		navBar.getElement().setId("navBar");
		navBar.setStyleName("navbar navbar-fixed-top");

		navBarInner = new FlowPanel();
		navBarInner.getElement().setId("navBarInner");
		navBarInner.setStyleName("navbar-inner");

		navBarContainer = new FlowPanel();
		navBarContainer.getElement().setId("navBarContainer");
		navBarContainer.setStyleName("container");

		navBar.add(navBarInner);
		navBarInner.add(navBarContainer);
		RootPanel.get().add(navBar);

		//MAIN STRUCTURE
		photoContainer = new FlowPanel();
		photoContainer.getElement().setId("photoContainer");
		photoContainer.setStyleName("container");

		horizontalLine = new HTML("<hr>");
		photoContainer.add(horizontalLine);

		RootPanel.get().add(photoContainer);
	}

	@Override
	public void destructStructure() {
		navBar.removeFromParent();
		navBarInner.removeFromParent();
		navBarContainer.removeFromParent();
		
		photoContainer.removeFromParent();
		horizontalLine.removeFromParent();
		
		row.removeFromParent();
		
		for(HorizontalPanel panel : photoPanels) {
			panel.removeFromParent();
		}
		
	}

	@Override
	public void buildHeader() {
		logo = new Anchor();
		logo.setStyleName("brand");
		logo.setText("My Social Life");
		navBarContainer.add(logo);
	}
	
	@Override
	public void destructHeader() {
		logo.removeFromParent();
	}

	@Override
	public void buildMain() {
		/*photo = new HTML("<h3>Deep Sky<small> By <a href=\"http://commons.wikimedia.org/wiki/File:Nature_1.jpg\">Srawat56</a><small><h3>" 
				+ "<a rel=\"lightbox[portfolio] tooltip\" title=\"This is a tooltip.\" href=\"img/thumb1.jpg\"><img src=\"img/thumb1.jpg\" width=\"80%\" height=\"80%\" alt=\"Thumbnail\"></a>");

		photo2 = new HTML("<h3>Cenas<small> By <a href=\"http://commons.wikimedia.org/wiki/File:Nature%27s_Valley_(S._Africa)_2.jpg\">Cenass</a><small><h3>" 
				+ "<a rel=\"lightbox[portfolio] tooltip\" title=\"This is a tooltip.\" href=\"img/thumb2.jpg\"><img src=\"img/thumb2.jpg\" width=\"80%\" height=\"80%\" alt=\"Thumbnail\"></a>");
		 */
		ArrayList<String> tempPhotos = new ArrayList<String>();
		tempPhotos.add("img/thumb1.jpg");
		tempPhotos.add("img/thumb2.jpg");
		tempPhotos.add("img/thumb3.png");
		/*tempPhotos.add("img/thumb2.jpg");
		tempPhotos.add("img/thumb1.jpg");
		tempPhotos.add("img/thumb2.jpg");
		tempPhotos.add("img/thumb1.jpg");
		tempPhotos.add("img/thumb2.jpg");
		tempPhotos.add("img/thumb1.jpg");
		tempPhotos.add("img/thumb2.jpg");
		tempPhotos.add("img/thumb2.jpg");*/
		
		photos = new ArrayList<HTML>();
		photoPanels = new ArrayList<HorizontalPanel>();
		
		
		
		//TODO percorrer a lista de fotos
		/*HTML ph1 = page.createPhotoWithLightbox("img/thumb1.jpg", "Image1", "This is the image1", "Cenas", 80, 80);
		HTML ph2 = page.createPhotoWithLightbox("img/thumb2.jpg", "Image2", "This is the image2", "Africa", 80, 80);
		HTML ph3 = page.createPhotoWithLightbox("img/thumb1.jpg", "Image1", "This is the image3", "Cenas", 80, 80);
		HTML ph4 = page.createPhotoWithLightbox("img/thumb2.jpg", "Image2", "This is the image4", "Africa", 80, 80);
		HTML ph5 = page.createPhotoWithLightbox("img/thumb1.jpg", "Image1", "This is the image5", "Cenas", 80, 80);
		HTML ph6 = page.createPhotoWithLightbox("img/thumb2.jpg", "Image2", "This is the image6", "Africa", 80, 80);
		HTML ph7 = page.createPhotoWithLightbox("img/thumb1.jpg", "Image1", "This is the image7", "Cenas", 80, 80);
		HTML ph8 = page.createPhotoWithLightbox("img/thumb2.jpg", "Image2", "This is the image8", "Africa", 80, 80);
		
		photos.add(ph1);
		photos.add(ph2);
		photos.add(ph3);
		photos.add(ph4);
		photos.add(ph5);
		photos.add(ph6);
		photos.add(ph7);
		photos.add(ph8);*/
		
		HorizontalPanel photoPanel = null;
		row = createRow();
		for(int i=0; i<tempPhotos.size(); i++) {
			HTML ph = page.createPhotoWithLightbox(tempPhotos.get(i), "Image"+(i+1), "This is the image"+(i+1), "Cenas"+(i+1), 80, 80);
			photos.add(ph);
			
			if(i%3 == 0) {
				photoPanel = createPhotoPanel();
				row.add(photoPanel);
				photoPanels.add(photoPanel);
			}
			
			photoPanel.add(photos.get(i));
		}

	}
	
	private FlowPanel createRow() {
		FlowPanel row = new FlowPanel();
		row.getElement().setId("row");
		row.setStyleName("row");
		photoContainer.add(row);
		
		return row;
	}
	
	private HorizontalPanel createPhotoPanel() {
		HorizontalPanel photoPanel = new HorizontalPanel();
		photoPanel.getElement().setId("cover");
		photoPanel.setStyleName("span4");
		return photoPanel;
	}
	
	@Override
	void destructMain() {
		for(HTML photo : photos) {
			photo.removeFromParent();
		}
	}

	@Override
	public void buildFooter() {
		footer = new HTML("<hr><footer class=\"row\"><p>&copy;2013 DeiBogusTeam<br></p></footer>");
		photoContainer.add(footer);
	}
	
	@Override
	void destructFooter() {
		footer.removeFromParent();
	}
	

}
