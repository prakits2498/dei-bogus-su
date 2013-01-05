package rs.deibogus.client.interfacebuilder;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Builder Design Pattern
 * "Product"
 * @author bfurtado, durval
 *
 */
public class Page {
	RootPanel rootPanel = RootPanel.get();

	public Page() {
	}
	
	public HTML createHtmlText(String text, String htmlTag, String alignment, boolean wordWrap, String panelID) {
		HTML htmlText = new HTML("<"+htmlTag+" align=\""+alignment+"\"> "+text+" </"+htmlTag+">", wordWrap);
		
		if(!panelID.equals("")) {
			if(panelID.equals("root"))
				RootPanel.get().add(htmlText);
			else
				RootPanel.get(panelID).add(htmlText);
		}
		
		return htmlText;
	}
	
	public HTML createHtmlDiv(String id, String cssStyle, String panelID) {
		final HTML htmlDiv = new HTML("<div id=\""+id+"\"> </div>", true);
		
		if(!cssStyle.equals(""))
			htmlDiv.setStyleName(cssStyle);
		
		if(!panelID.equals("")) {
			if(panelID.equals("root"))
				RootPanel.get().add(htmlDiv);
			else
				RootPanel.get(panelID).add(htmlDiv);
		}
		
		return htmlDiv;
	}
	
	public FlowPanel createFlowPanel(String id, String cssStyle) {
		FlowPanel panel = new FlowPanel();
		panel.getElement().setId(id);
		
		if(!cssStyle.equals(""))
			panel.setStyleName(cssStyle);
		
		return panel;
	}

	public TextBox createTextBox(String placeholder, boolean visible, String cssStyle, String panelID) {
		final TextBox txt = new TextBox();
		txt.getElement().setPropertyString("placeholder", placeholder);
		
		if(!cssStyle.equals(""))
			txt.setStyleName(cssStyle);
		
		txt.setVisible(visible);

		if(!panelID.equals("")) {
			if(panelID.equals("root"))
				RootPanel.get().add(txt);
			else
				RootPanel.get(panelID).add(txt);
		}

		return txt;
	}

	public TextBox createHiddenTextBox(String text) {
		final TextBox txt = new TextBox();
		txt.setText(text);
		txt.setFocus(true);
		txt.selectAll();
		//rootPanel.add(txt, x, y);

		return txt;
	}

	public PasswordTextBox createPasswordTextBox(String placeholder, boolean visible, String cssStyle, String panelID) {
		
		final PasswordTextBox password = new PasswordTextBox();
		password.getElement().setPropertyString("placeholder", placeholder);
		
		if(!cssStyle.equals(""))
			password.setStyleName(cssStyle);
		
		if(!panelID.equals("")) {
			if(panelID.equals("root"))
				RootPanel.get().add(password);
			else
				RootPanel.get(panelID).add(password);
		}
		
		return password;
	}

	public PasswordTextBox createHiddenPasswordTextBox() {
		final PasswordTextBox passwordTxt = new PasswordTextBox();
		//rootPanel.add(passwordTxt, x, y);

		return passwordTxt;
	}

	public Button createButton(String text, boolean visible, String cssStyle, String panelID) {
		final Button button = new Button();
		button.setText(text);
		
		if(!cssStyle.equals(""))
			button.setStyleName(cssStyle);
		
		if(!panelID.equals("")) {
			if(panelID.equals("root"))
				RootPanel.get().add(button);
			else
				RootPanel.get(panelID).add(button);
		}
		
		return button;
	}

	public Button createHiddenButton(String label) {
		final Button sendButton = new Button(label);
		//rootPanel.add(sendButton, x, y);

		return sendButton;
	}

	public Image createImage(String url, int x, int y, String width, String height) {
		Image image = new Image(url);
		image.setSize(width, height);
		rootPanel.add(image, x, y);

		return image;
	}

	public VerticalPanel createVerticalPanel(String label){
		VerticalPanel vp = new VerticalPanel();
		rootPanel.add(vp, 0, 0);

		return vp;
	}

	public VerticalPanel createHiddenVerticalPanel(String label){
		VerticalPanel vp = new VerticalPanel();
		//rootPanel.add(vp, 0, 0);

		return vp;
	}

	public PopupPanel createHiddenPopupPanel(String label){
		PopupPanel popup = new PopupPanel();
		//rootPanel.add(popup, 500, 500); //depois faz-se popup.center()

		return popup;
	}
}
