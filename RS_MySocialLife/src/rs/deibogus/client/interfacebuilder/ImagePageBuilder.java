package rs.deibogus.client.interfacebuilder;

import java.util.ArrayList;

import rs.deibogus.client.ClientData;
import rs.deibogus.client.GreetingService;
import rs.deibogus.client.GreetingServiceAsync;
import rs.deibogus.shared.Foto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
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

	private Interface builder;

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
	//private ArrayList<Foto> photoCatalog;
	Button deleteButton;

	public ImagePageBuilder() {
		//this.photoCatalog = photos;
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

		photos = new ArrayList<HTML>();
		photoPanels = new ArrayList<HorizontalPanel>();

		ArrayList<Foto> catalogo = ClientData.getInstance().getFotos();
		HorizontalPanel photoPanel = null;

		row = createRow();
		for(int i=0; i<catalogo.size(); i++) {
			deleteButton = new Button();
			deleteButton.setText("Delete");
			deleteButton.addClickHandler(new DeleteHandler(catalogo.get(i)));
			
			
			HTML ph = page.createPhotoWithLightbox(catalogo.get(i).getUrl(), catalogo.get(i).getTitle(),catalogo.get(i).getTitle(), catalogo.get(i).getTitle(), 80, 80);
			photos.add(ph);

			if(i%3 == 0) {
				photoPanel = createPhotoPanel();
				photoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				row.add(photoPanel);
				photoPanels.add(photoPanel);
			}

			photoPanel.add(deleteButton);
			
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

		for(HorizontalPanel panel : photoPanels) {
			panel.removeFromParent();
		}

		row.removeFromParent();

		if(deleteButton != null)
			deleteButton.removeFromParent();
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

	class DeleteHandler implements ClickHandler {
		Foto photo;

		public DeleteHandler(Foto foto) {
			this.photo = foto;
		}

		public void onClick(ClickEvent event) {
			System.out.println(this.photo.getTitle());
			delete();
		}

		private void delete() {

			greetingService.deletePhoto(this.photo, new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
					System.out.println(SERVER_ERROR);
				}

				public void onSuccess(String result) {
					System.out.println("Delete: " + result);
					if(result.equals("true")){
						ClientData.getInstance().getFotos().remove(photo);
						destructMain();
						destructFooter();
						buildMain();
						buildFooter();
					}
					//Window.open(result,"_blank","");
				}
			});
		}

	}

}
