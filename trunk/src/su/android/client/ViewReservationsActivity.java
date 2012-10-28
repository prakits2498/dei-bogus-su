package su.android.client;

import greendroid.app.GDActivity;

import java.util.HashMap;

import su.android.model.DayEventsRequest;
import su.android.server.connection.ServerConnection;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class ViewReservationsActivity extends GDActivity {
	
	int userId;
	int year;
	int month;
	int day;
	
	private ServerConnection conn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.la);
		
		Bundle b = new Bundle();
		b = getIntent().getExtras();
		Log.i("USER", Integer.toString(b.getInt("idUser")));
//		userId = b.getInt("idUser");
//		year = b.getInt("year");
//		month = b.getInt("month");
//		day = Integer.parseInt(b.getString("day"));
		DayEventsRequest req = new DayEventsRequest();
		req.setIdUser(b.getInt("idUser"));
		req.setYear(b.getInt("year"));
		req.setMonth(b.getInt("month"));
		req.setDay(Integer.parseInt(b.getString("day")));
		
		//server interface
		conn = new ServerConnection();
		req = conn.getMenuFromReservations(req);
		
		Log.i("data: ", req.getDay()+" "+req.getMonth()+" "+req.getIdUser());
		Log.i("dados da bd", "---- "+req.getLunchEvents().get("sopa"));
		
		HashMap<String, String> almoco = req.getLunchEvents();
		HashMap<String, String> jantar = req.getDinnerEvents();
		
		
		int i = 0; 	//flag para ver se ja foi inserido algum menu
		
		if(almoco.get("cantina") != null) {
			i++;
			TextView cantina = (TextView) findViewById(R.id.cantina_ID);
			cantina.setText(almoco.get("cantina") + " - " + almoco.get("slot"));
			if(almoco.get("sopa") != null) {
				TextView sopa = (TextView) findViewById(R.id.sopa_ID);
				sopa.setText(almoco.get("sopa").substring(0, almoco.get("sopa").indexOf("|")));
			}
			
			if(almoco.get("carne") != null) {
				TextView carne = (TextView) findViewById(R.id.carne_ID);
				
				if(almoco.get("carne").contains("|"))
					carne.setText(almoco.get("carne").substring(0, almoco.get("carne").indexOf("|")));
				else
					carne.setText(almoco.get("carne"));
			} else if(almoco.get("peixe") != null) {
				TextView peixe = (TextView) findViewById(R.id.peixe_ID);
				peixe.setText(almoco.get("peixe").substring(0, almoco.get("peixe").indexOf("|")));
			}
			
			TextView preco = (TextView) findViewById(R.id.price_ID);
			preco.setText(almoco.get("preco")+" €");
			
			TextView precoId = (TextView) findViewById(R.id.price_text_ID);
			precoId.setText("Preço");
		}
		
		if(jantar.get("cantina") != null){
			if(i==0){
				i++;
				
				TextView cantina = (TextView) findViewById(R.id.cantina_ID);
				cantina.setText(jantar.get("cantina") + " - " + jantar.get("slot"));
				
				if(almoco.get("sopa") != null) {
					TextView sopa = (TextView) findViewById(R.id.sopa_ID);
					sopa.setText(almoco.get("sopa").substring(0, almoco.get("sopa").indexOf("|")));
				}
				
				if(almoco.get("carne") != null) {
					TextView carne = (TextView) findViewById(R.id.carne_ID);

					if(almoco.get("carne").contains("|"))
						carne.setText(almoco.get("carne").substring(0, almoco.get("carne").indexOf("|")));
					else
						carne.setText(almoco.get("carne"));
				} else if(almoco.get("peixe") != null) {
					TextView peixe = (TextView) findViewById(R.id.peixe_ID);
					peixe.setText(almoco.get("peixe").substring(0, almoco.get("peixe").indexOf("|")));
				}
				
				TextView preco = (TextView) findViewById(R.id.price_ID);
				preco.setText(jantar.get("preco")+" €");
				TextView precoId = (TextView) findViewById(R.id.price_text_ID);
				precoId.setText("Preço");
			}
			else{
				i++;
				TextView cantina = (TextView) findViewById(R.id.cantina_ID2);
				cantina.setText(jantar.get("cantina") + " - " + jantar.get("slot"));
				
				if(almoco.get("sopa") != null) {
					TextView sopa = (TextView) findViewById(R.id.sopa_ID2);
					sopa.setText(almoco.get("sopa").substring(0, almoco.get("sopa").indexOf("|")));
				}
				
				if(almoco.get("carne") != null) {
					TextView carne = (TextView) findViewById(R.id.carne_ID2);
					
					if(almoco.get("carne").contains("|"))
						carne.setText(almoco.get("carne").substring(0, almoco.get("carne").indexOf("|")));
					else
						carne.setText(almoco.get("carne"));
					
				} else if(almoco.get("peixe") != null) {
					TextView peixe = (TextView) findViewById(R.id.peixe_ID2);
					peixe.setText(almoco.get("peixe").substring(0, almoco.get("peixe").indexOf("|")));
				}
				
				TextView preco = (TextView) findViewById(R.id.price_ID2);
				preco.setText(jantar.get("preco")+" €");
				TextView precoId = (TextView) findViewById(R.id.price_text_ID2);
				precoId.setText("Preço");
			}
		}
		
	}
	
}
