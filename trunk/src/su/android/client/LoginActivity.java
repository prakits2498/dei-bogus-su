package su.android.client;

import su.android.model.Login;
import su.android.server.connection.ServerConnection;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private ServerConnection conn;
	private Button loginButton;
	private EditText emailEdit;
	private EditText passEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		//server interface
		conn = new ServerConnection();
		
		//liga�oes com elementos do layout
		loginButton = (Button) findViewById(R.id.btnLogin);
		
//		AlertDialog.Builder alert = new AlertDialog.Builder(this);
//
//		alert.setTitle("Title");
//		alert.setMessage("Message");
//
//		// Set an EditText view to get user input 
//		final EditText input = new EditText(this);
//		alert.setView(input);
//
//		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//		public void onClick(DialogInterface dialog, int whichButton) {
//		  String value = input.getText().toString();
//		  // Do something with value!
//		  }
//		});
//
//		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//		  public void onClick(DialogInterface dialog, int whichButton) {
//		    // Canceled.
//		  }
//		});

//		alert.show();

		//handle login button
		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				try {   
//                    GMailSender sender = new GMailSender("deiBogus@gmail.com", "deiBogus1");
//                    sender.sendMail("912894137",   
//                            "Reserva confirmada",
//                            "deiBogus@gmail.com",
//                            "durvalp1@gmail.com");   
//                } catch (Exception e) {   
//                    Log.e("SendMail", e.getMessage(), e);   
//                }
				
				emailEdit = (EditText) findViewById(R.id.emailInput);
				passEdit = (EditText) findViewById(R.id.passwordlInput);
				
				//verify integrity
				if(emailEdit.getText().toString().equals("") || passEdit.getText().toString().equals("")){
					String text = "Missing input";

					Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
					toast.show();	
					
					
					return;
					
				}
				
				
				//Verify credentials on DB
				Login login = new Login(emailEdit.getText().toString(),passEdit.getText().toString());
				
				int id=-1;
				try {
					id = conn.verifyLogin(login);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(id != -1){
					// Switching to Main Activity
					Intent i = new Intent(getApplicationContext(), MainScreen.class);
					i.putExtra("idUser", id);
					startActivity(i);
				}
				else{
					String text = "Invalid Login";

					Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
					toast.show();

				}

			}
		});
	}
}