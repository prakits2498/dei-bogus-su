
package su.android.client;

import greendroid.app.GDActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import su.android.model.Meal;
import su.android.model.Reserva;
import su.android.model.Slot;
import su.android.server.connection.MailClient;
import su.android.server.connection.ServerConnection;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReservaActivity extends GDActivity implements AdapterView.OnItemSelectedListener {


	private ServerConnection conn;

	private boolean temSopa = false;
	private boolean temCarne = false;
	private boolean temPeixe = false;

	TextView slotSelectedTv;
	String[] slotsAux;
	int indexSlotSelected;
	Reserva reserva;
	String radioButtonSelected;
	String backup; //guardada no txt e enviada por mail
	
	double priceMeal;
	
	private HashMap<String, String> reservaExtras = new HashMap<String, String>();

	public ReservaActivity() {
		conn = ServerConnection.getInstance();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setActionBarContentView(R.layout.about);

		getExtras();

		TextView data = (TextView) findViewById(R.id.data);

		TextView sopaTv = (TextView) findViewById(R.id.sopaTv_ID);
		TextView pratoTv = (TextView) findViewById(R.id.prato_ID);

		TextView price = (TextView) findViewById(R.id.reservaPrice);
		
		
		try {
			NumberFormat format = NumberFormat.getInstance(Locale.FRENCH);
			Number number = format.parse(reservaExtras.get("priceMeal"));
			priceMeal = number.doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		price.setText(priceMeal+" �");

		Meal meal = new Meal();
		meal.setId(reservaExtras.get("idMeal"));
		meal.setPrice(reservaExtras.get("priceMeal"));

		if(temSopa) {
			meal.setSopa(reservaExtras.get("sopaTv"));
			sopaTv.setText(reservaExtras.get("sopaTv"));
		}

		if(temCarne) {
			meal.setCarne(reservaExtras.get("carneTv"));
			pratoTv.setText(reservaExtras.get("carneTv"));
		}
		else if(temPeixe) {
			meal.setPeixe(reservaExtras.get("peixeTv"));
			pratoTv.setText(reservaExtras.get("peixeTv"));
		}

		reserva = new Reserva();
		reserva.setPoiID(reservaExtras.get("poiID"));
		reserva.setUserID(reservaExtras.get("userID"));
		reserva.setMeal(meal);
		reserva.setDay(Integer.parseInt(reservaExtras.get("dayMeal")));
		reserva.setMonth(Integer.parseInt(reservaExtras.get("monthMeal")));
		reserva.setPriceMeal(reservaExtras.get("priceMeal"));
		reserva = conn.getSlots(reserva);
		Log.i("getSlots BD: ", "dia: "+reserva.getDay()+" mes: "+reserva.getMonth()+" cantina: "+reserva.getPoiID()+" mealID: "+reserva.getMeal().getId());
		
		Log.i("n slots: ", ""+reserva.getSlots().size());
		
		data.setText(reserva.getDay()+"-"+reserva.getMonth()+"-2012");
		
		Spinner slotSpin = (Spinner) findViewById(R.id.slot);
		slotSelectedTv = new TextView(this.getApplicationContext());
		setSlots();

		slotSpin.setOnItemSelectedListener(this);

		ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, slotsAux);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		slotSpin.setAdapter(aa);

		Button confirmar = (Button) findViewById(R.id.confirmar);
		confirmar.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				boolean reservado = false;
				String payment = "";
				
				String idSlotSelected = reserva.getSlots().get(indexSlotSelected).getIdSlot();
				reserva.setSlotID(idSlotSelected);
				
				RadioButton paypalButton = (RadioButton) findViewById(R.id.paypal);
				RadioButton multibancoButton = (RadioButton) findViewById(R.id.multibanco);
				RadioButton creditosButton = (RadioButton) findViewById(R.id.creditos);

				if(creditosButton.isChecked()) {
					
					double userCredits = conn.getCredits(Integer.parseInt(reserva.getUserID()));
					
					double userCreditsA = userCredits - priceMeal;
					
					if(verifyCredits(userCredits)) {
						reserva.setCreditos(true);						
						reserva.setPaid(true);
						reservado = true;
						
						reserva.setUserCredits(Double.toString(userCreditsA));
						
						conn.actualizaCreditos(reserva.getUserID(), Double.toString(userCreditsA));
						conn.actualizaNumReservados(reserva.getSlotID());
						conn.makeReservationSlots(reserva);

						payment = "Pre-Pagamento";
					} else {
						// get your custom_toast.xml layout
						LayoutInflater inflater = getLayoutInflater();
		 
						View layout = inflater.inflate(R.layout.custom_toast,
						  (ViewGroup) findViewById(R.id.custom_toast_layout_id));
						layout.setBackgroundColor(v.getResources().getColor(R.color.darkgray));
		 
						// set a dummy image
						ImageView image = (ImageView) layout.findViewById(R.id.image);
						image.setImageResource(R.drawable.euro_icon_blue_big_03);
		 
						// set a message
						TextView text = (TextView) layout.findViewById(R.id.text);
						text.setText("N�o tem saldo suficiente para efectuar pagamento.");
		 
						// Toast...
						Toast toast = new Toast(getApplicationContext());
						toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						toast.setDuration(Toast.LENGTH_SHORT);
						toast.setView(layout);

						toast.show();
						
						reservado = false;
					}
				} else if(paypalButton.isChecked()) {
					//send to paypal 
					reserva.setCreditos(false);
					reservado = true;
					reserva.setPaid(false);
					
					conn.makeReservationSlots(reserva);
					
					payment = "PayPal";
				} else if(multibancoButton.isChecked()) {
					String ent = "10664";
					String ref = "124566788";
					String valor = reserva.getPriceMeal();
					reserva.setCreditos(false);
					reservado = true;
					reserva.setPaid(false);
					
					conn.makeReservationSlots(reserva);
					
					payment = "MB";
				}
				
				
				if(reservado) {
					try {
						File myFile = new File("/sdcard/reservas.txt");
						boolean existe = myFile.createNewFile();
						FileOutputStream fOut = new FileOutputStream(myFile,true);
						OutputStreamWriter myOutWriter = 
												new OutputStreamWriter(fOut);
						
						Slot temp = reserva.getSlots().get(indexSlotSelected);
						String cantina = conn.getNameCantina(reserva.getPoiID());
						
						backup = ("Cantina: " + cantina) + "\n" + "Slot: " + temp.getDay() + "/" + temp.getMonth() + " - " + temp.getHour() + "\n";
						backup = backup.concat("Menu: ");
						if(temCarne)
							backup = backup.concat(reserva.getMeal().getCarne());
						if(temSopa)
							backup = backup.concat(" - " + reserva.getMeal().getSopa());
						if(temPeixe)
							backup = backup.concat(" - " + reserva.getMeal().getPeixe());
						backup = backup.concat("\nPreço: " + reserva.getPriceMeal() + "\n");
						backup = backup.concat("Metodo de pagamento: " + payment + "\n\n\n");
						
						myOutWriter.append(backup);
						myOutWriter.close();
						fOut.close();

					} catch (Exception e) {
						Toast.makeText(getBaseContext(), e.getMessage(),
								Toast.LENGTH_SHORT).show();
					}
					
					String email = conn.getEmail(reserva.getUserID());
					
					MailClient sender = new MailClient();
					try {
						sender.sendMail("deiBogus@gmail.com", "deiBogus1","smtp.gmail.com","durvalp1@gmail.com","Histórico de reservas",backup);
					} catch (AddressException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Intent i = new Intent(v.getContext(), PaymentActivity.class);
					i.putExtra("userID", reserva.getUserID());
					i.putExtra("payment", payment);
					i.putExtra("priceMeal", reserva.getPriceMeal());
					
					v.getContext().startActivity(i);
				}
			}
		});
	}
	
	
	
	public boolean verifyCredits(double userCredits) {
		double aux = userCredits - priceMeal;
		if(aux >= 0)
			return true;
		else 
			return false;
	}

	public void onItemSelected(AdapterView<?> parent, View v, int slotPosition, long id) {
		slotSelectedTv.setText(slotsAux[slotPosition]);
		indexSlotSelected = slotPosition;
	}

	public void onNothingSelected(AdapterView<?> parent) {
		slotSelectedTv.setText("Seleccione um slot");
	}

	private void setSlots() {
		List<Slot> slots = reserva.getSlots();
		slotsAux = new String[slots.size()];

		for(int i=0; i<slots.size(); i++) {
			Slot s = slots.get(i);
			if(s.isAvailable()) {
				if(s.getMinute() == 0)
					slotsAux[i] = s.getDay()+"-"+s.getMonth()+"-2012 "+s.getHour()+":"+s.getMinute()+"0";
				else
					slotsAux[i] = s.getDay()+"-"+s.getMonth()+"-2012 "+s.getHour()+":"+s.getMinute();
			} else 
				slotsAux[i] = "Nenhum slot disponivel";
			
			Log.i("slotsAux: ", slotsAux[i]);
		}
	}

	private void getExtras() {
		Bundle b = new Bundle();
		b = getIntent().getExtras();

		reservaExtras.put("userID", b.getString("userID"));
		reservaExtras.put("poiID", b.getString("poiID"));
		reservaExtras.put("idMeal", b.getString("idMeal"));
		reservaExtras.put("typeOfMeal", b.getString("typeOfMeal")); //lunch ou dinner
		reservaExtras.put("priceMeal", b.getString("priceMeal"));
		reservaExtras.put("dayOfWeek", b.getString("dayOfWeek"));

		reservaExtras.put("sopaTv", b.getString("sopaTv"));
		reservaExtras.put("carneTv", b.getString("carneTv"));
		reservaExtras.put("peixeTv", b.getString("peixeTv"));

		temSopa = b.getBoolean("sopa");
		temCarne = b.getBoolean("carne");
		temPeixe = b.getBoolean("peixe");

		reservaExtras.put("dayMeal", b.getString("dayMeal"));
		reservaExtras.put("monthMeal", b.getString("monthMeal"));
	}

}
