
package su.android.client;

import greendroid.app.GDActivity;

import java.util.HashMap;
import java.util.List;

import su.android.model.Meal;
import su.android.model.Reserva;
import su.android.model.Slot;
import su.android.server.connection.ServerConnection;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ReservaActivity extends GDActivity implements AdapterView.OnItemSelectedListener {
	
	
	private ServerConnection conn;
	
	private boolean temSopa = false;
	private boolean temCarne = false;
	private boolean temPeixe = false;
	
	TextView slotSelectedTv;
	List<Slot> slots;
	
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
        price.setText(reservaExtras.get("priceMeal")+" Û");
        
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
        
        Spinner slotSpin = (Spinner) findViewById(R.id.slot);
        slotSelectedTv = new TextView(this.getApplicationContext());
        
        Reserva reserva = new Reserva();
        reserva.setPoiID(reservaExtras.get("poiID"));
        reserva.setUserID(reservaExtras.get("userID"));
        reserva.setMeal(meal);
        reserva.setDay(Integer.parseInt(reservaExtras.get("dayMeal")));
        reserva.setMonth(Integer.parseInt(reservaExtras.get("monthMeal")));
        
        reserva = conn.getSlots(reserva);
        slots = reserva.getSlots();
        
        slotSpin.setOnItemSelectedListener(this);

		ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, slots);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		slotSpin.setAdapter(aa);
        
        Button confirmar = (Button) findViewById(R.id.confirmar);
        
    }
    
    public void onItemSelected(AdapterView<?> parent, View v, int slotPosition, long id) {
    	Slot slot = slots.get(slotPosition);
    	if(slot.isAvailable())
    		if(slot.getMinute() == 0)
    			slotSelectedTv.setText(slot.getDay()+"-"+slot.getMonth()+"-2012 "+slot.getHour()+":"+slot.getMinute()+"0");
    		else
    			slotSelectedTv.setText(slot.getDay()+"-"+slot.getMonth()+"-2012 "+slot.getHour()+":"+slot.getMinute());
    	else
    		slotSelectedTv.setText("Nenhum slot disponivel");
	}

	public void onNothingSelected(AdapterView<?> parent) {
		slotSelectedTv.setText("");
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
