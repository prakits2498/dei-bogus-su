
package su.android.client;

import greendroid.app.GDActivity;

import java.util.HashMap;

import su.android.model.MenuDetails;
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

        loadReservaDetails();
       
        TextView data = (TextView) findViewById(R.id.data);
        
        TextView sopaTv = (TextView) findViewById(R.id.sopaTv_ID);
        TextView pratoTv = (TextView) findViewById(R.id.prato_ID);
        
        TextView price = (TextView) findViewById(R.id.reservaPrice);
        price.setText(reservaExtras.get("priceMeal")+" Û");
        
        if(temSopa)
        	sopaTv.setText(reservaExtras.get("sopaTv"));
        
        if(temCarne)
        	pratoTv.setText(reservaExtras.get("carneTv"));
        else if(temPeixe)
        	pratoTv.setText(reservaExtras.get("peixeTv"));
        
        Spinner slot = (Spinner) findViewById(R.id.slot);
        
        
        
        Button confirmar = (Button) findViewById(R.id.confirmar);
        
    }
    
    private void getExtras() {
    	Bundle b = new Bundle();
        b = getIntent().getExtras();
        
        reservaExtras.put("userID", b.getString("userID"));
        reservaExtras.put("poiID", b.getString("poiID"));
        reservaExtras.put("idMeal", b.getString("idMeal"));
        reservaExtras.put("typeOfMeal", b.getString("typeOfMeal"));
        reservaExtras.put("priceMeal", b.getString("priceMeal"));
        reservaExtras.put("dayOfWeek", b.getString("dayOfWeek"));
        
        reservaExtras.put("sopaTv", b.getString("sopaTv"));
        reservaExtras.put("carneTv", b.getString("carneTv"));
        reservaExtras.put("peixeTv", b.getString("peixeTv"));
        
        temSopa = b.getBoolean("sopa");
        temCarne = b.getBoolean("carne");
        temPeixe = b.getBoolean("peixe");
    }
    
    private void loadReservaDetails() {
    	
    }
    
}
