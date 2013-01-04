package rs.deibogus.client.interfacebuilder;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

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

	public TextBox createTextBox(String text, int x, int y) {
		final TextBox txt = new TextBox();
		txt.setText(text);
		txt.setFocus(true);
		txt.selectAll();
		rootPanel.add(txt, x, y);
		
		return txt;
	}

	public PasswordTextBox createPasswordTextBox(int x, int y) {
		final PasswordTextBox passwordTxt = new PasswordTextBox();
		rootPanel.add(passwordTxt, x, y);
		
		return passwordTxt;
	}

	public Button createButton(String label, int x, int y) {
		final Button sendButton = new Button(label);
		rootPanel.add(sendButton, x, y);
		
		return sendButton;
	}
	
	
}
