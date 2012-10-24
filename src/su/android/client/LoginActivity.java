package su.android.client;

import su.android.model.Login;
import su.android.server.connection.ServerConnection;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
		
		//ligaçoes com elementos do layout
		loginButton = (Button) findViewById(R.id.btnLogin);

		//handle login button
		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
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
				int id = conn.verifyLogin(login);
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