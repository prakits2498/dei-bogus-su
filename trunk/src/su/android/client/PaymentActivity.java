package su.android.client;

import su.android.model.ConfirmationData;
import su.android.server.connection.GMailSender;
import su.android.server.connection.ServerConnection;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import greendroid.app.GDActivity;

public class PaymentActivity extends GDActivity {

	String url = "http://www.paypal.com";
	Intent i = new Intent(Intent.ACTION_VIEW);
	Uri u = Uri.parse(url);
	Context context = this;
	ConfirmationData confData = new ConfirmationData();

	private ServerConnection conn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.pagamento);

		Bundle b = new Bundle();
		b = getIntent().getExtras();
		conn = new ServerConnection();

		int idUser = Integer.parseInt(b.getString("userID"));
		String metodo = b.getString("payment");
		String value = b.getString("priceMeal");
		confData.setIdUser(idUser);
		confData = conn.getConfirmationData(confData);

		TextView titulo = (TextView) findViewById(R.id.metodo);
		titulo.setText(metodo);

		if(metodo.equals("MB")){

			TextView entidade = (TextView) findViewById(R.id.campo1);
			entidade.setVisibility(View.VISIBLE);
			entidade.setText("Entidade:");
			TextView entidadeValue = (TextView) findViewById(R.id.campo1text);
			entidadeValue.setVisibility(View.VISIBLE);
			entidadeValue.setText("20631");

			TextView referencia = (TextView) findViewById(R.id.campo2);
			referencia.setVisibility(View.VISIBLE);
			referencia.setText("Referência:");
			TextView referenciaValue = (TextView) findViewById(R.id.campo2text);
			referenciaValue.setVisibility(View.VISIBLE);
			referenciaValue.setText("47278237328");

			TextView valor = (TextView) findViewById(R.id.campo3);
			valor.setVisibility(View.VISIBLE);
			valor.setText("Valor:");
			TextView valorValue = (TextView) findViewById(R.id.campo3text);
			valorValue.setVisibility(View.VISIBLE);
			valorValue.setText(value);

			TextView msg = (TextView) findViewById(R.id.preco);
			msg.setVisibility(View.VISIBLE);
			msg.setText("Não se esqueça de apresentar o talão no momento da refeição");
			TextView msgValue = (TextView) findViewById(R.id.price_ID);
			msgValue.setVisibility(View.VISIBLE);
			msgValue.setText("Obrigado.");

		}
		else if(metodo.equals("PayPal")){

			TextView entidade = (TextView) findViewById(R.id.campo1);
			entidade.setVisibility(View.VISIBLE);
			entidade.setText("Valor:");
			TextView entidadeValue = (TextView) findViewById(R.id.campo1text);
			entidadeValue.setVisibility(View.VISIBLE);
			entidadeValue.setText(value);

			ImageView paypal = (ImageView) findViewById(R.id.paypalimage);
			paypal.setVisibility(View.VISIBLE);
			paypal.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						// Start the activity
						i.setData(u);
						startActivity(i);
					} catch (ActivityNotFoundException e) {
						// Raise on activity not found
						Toast.makeText(context, "Browser not found.", Toast.LENGTH_SHORT);
					}
				} 
			});

		}
		else{

			TextView entidade = (TextView) findViewById(R.id.campo1);
			entidade.setVisibility(View.VISIBLE);
			entidade.setText("Valor:");
			TextView entidadeValue = (TextView) findViewById(R.id.campo1text);
			entidadeValue.setVisibility(View.VISIBLE);
			entidadeValue.setText(value);

			TextView msg = (TextView) findViewById(R.id.preco);
			msg.setVisibility(View.VISIBLE);
			msg.setText("Reserva efectuada com sucesso");
			TextView msgValue = (TextView) findViewById(R.id.price_ID);
			msgValue.setVisibility(View.VISIBLE);
			msgValue.setText("Obrigado.");
		}

		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage("912894137", null, "Reserva efectuada com sucesso", null, null); //FIXME questão da SMS
	}

}
