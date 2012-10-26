
package su.android.client;

import greendroid.app.GDActivity;

import java.util.HashMap;
import java.util.List;

import su.android.model.Meal;
import su.android.model.MenuDetails;
import su.android.model.Reserva;
import su.android.model.Slot;
import su.android.server.connection.ServerConnection;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ReservaActivity extends GDActivity {
	
	
	private ServerConnection conn;
	private MenuDetails poiDetails;
	
	private boolean temSopa = false;
	private boolean temCarne = false;
	private boolean temPeixe = false;
	
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
        
        Spinner slot_spin = (Spinner) findViewById(R.id.slot);
        
        Reserva reserva = new Reserva();
        reserva.setPoiID(reservaExtras.get("poiID"));
        reserva.setUserID(reservaExtras.get("userID"));
        reserva.setMeal(meal);
        
        reserva = conn.getSlots(reserva);
        List<Slot> slots = reserva.getSlots();
        
        Button confirmar = (Button) findViewById(R.id.confirmar);
        
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
    }
    
}
