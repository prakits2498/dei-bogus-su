package rs.deibogus.client.interfacebuilder;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

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
	
	public void createLoginForm() {
		/*final FormPanel form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.addStyleName("table-center");
		form.addStyleName("login-FormPanel");
		
		VerticalPanel holder = new VerticalPanel();

		holder.add(new Label("Username"));
		TextBox userid = new TextBox();
		userid.setName("username");
		holder.add(userid);

		holder.add(new Label("Password"));
		PasswordTextBox passwd = new PasswordTextBox();
		passwd.setName("passwd");
		holder.add(passwd);

		holder.add(new Button("Submit", new ClickListener()
		{
		    public void onClick(Widget sender)
		    {
		        // form.submit();
		    }
		}));

		form.add(holder);
		
		form.addFormHandler(new FormHandler()
		{
		    public void onSubmit(FormSubmitEvent event)
		    {
		        // if (something_is_wrong)
		        // {
		        // Take some action
		        // event.setCancelled(true);
		        // }
		    }

		    public void onSubmitComplete(
		        FormSubmitCompleteEvent event)
		    {
		        Window.alert(event.getResults());
		    }
		});

		rootPanel.add(form);*/
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
	
	public Image createImage(String url, int x, int y, String width, String height) {
		Image image = new Image(url);
		image.setSize(width, height);
		rootPanel.add(image, x, y);
		
		return image;
	}
}
