package su.android.client;

import su.android.model.AppContext;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class BrowseDialog {

	private MainScreen mainScreen;
	private TextView dayText;
	private TextView hourText;
	private LayoutInflater inflater;
	private View layout;
	private SeekBar dayBar;
	private SeekBar hourBar;
	private AlertDialog alertDialog;
	
	public BrowseDialog(final MainScreen mainScreen)
	{
		this.mainScreen = mainScreen;			
		inflater = (LayoutInflater) mainScreen.getSystemService(mainScreen.getApplicationContext().LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.dialog_day_hour, null);
		dayText = (TextView) layout.findViewById(R.id.dayLabelValue);
		hourText = (TextView) layout.findViewById(R.id.hourLabelValue);
		AlertDialog.Builder builder = new AlertDialog.Builder(mainScreen).setView(layout);		
		dayBar = (SeekBar)layout.findViewById(R.id.dayBar);
		hourBar = (SeekBar)layout.findViewById(R.id.hourBar);
		
		AppContext ctxt = mainScreen.getCurrentAppContext();
		dayBar.setProgress(ctxt .getDayOfWeekIndex());		
		dayText.setText(ctxt.getDayOfWeek());
		hourBar.setProgress(ctxt.getHourOfDay());
		hourText.setText(ctxt.getHourOfDay()+"H");
		

		builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				mainScreen.getCurrentAppContext().setDayOfWeekIndex(dayBar.getProgress());
				mainScreen.getCurrentAppContext().setHourOfDay(hourBar.getProgress());
				mainScreen.handle();
			}
		});
		
		dayBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				dayText.setText(AppContext.days[progress]);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
		});

		hourBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				hourText.setText(String.valueOf(progress)+"H");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
		});
		
		alertDialog = builder.create();
		alertDialog.setTitle("Browse Mode");
	}
	
	public void onActivateDialog()
	{				
		alertDialog.show();
	}
	
	
}
