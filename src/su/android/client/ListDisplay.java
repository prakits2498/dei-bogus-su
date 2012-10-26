package su.android.client;

import greendroid.app.GDActivity;

import java.util.Collection;
import java.util.List;

import su.android.model.TestObject;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Manages the display of the watch list.
 *  
 * @author Kah
 */
public class ListDisplay<T> {
	/**
	 * Reference to the list of stocks that are in the watch list.
	 */
	private final Collection<T> itemsCollection;

	/**
	 * Reference to the view that displays the watch list on the user interface.
	 * This needs to inflated later, since the instance of the layout inflater
	 * is required to create it.
	 */
	private final TableLayout display;

	private final Context appContext;

	private final DisplayAdapter<T> dataAdapter;

	private SelectionReceiver<T> selectionObserver;

	private T highlighted;

	private int weekIndex;
	private String[] daysOfWeek = {"Segunda","Terça","Quarta","Quinta","Sexta","Sábado","Domingo"};

	/**
	 * Constructs the display manager for the watch list.
	 * 
	 * @param itemsCollection
	 * 			The set of items to display in the list.
	 */
	public ListDisplay(Collection<T> itemsCollection, Context appContext, DisplayAdapter<T> dataAdapter, int weekIndex) 
	{
		this.appContext = appContext;
		this.dataAdapter = dataAdapter;

		display = new TableLayout(appContext);

		TableLayout.LayoutParams displayParams = new TableLayout.LayoutParams(
				TableRow.LayoutParams.FILL_PARENT,
				TableRow.LayoutParams.WRAP_CONTENT);


		displayParams.setMargins(10, 100, 10, 10);
		display.setLayoutParams(displayParams);

		display.setStretchAllColumns(true);

		this.itemsCollection = itemsCollection;
		this.weekIndex = weekIndex;
		initialiseDisplay();
	}

	/**
	 * Retrieves the display component to display the contents of the watch
	 * list on the user interface.
	 * 
	 * @return The display view for viewing the contents of the watch list.
	 */
	public View getDisplay() 
	{
		return display;
	}

	/**
	 * Retrieves the last item that was selected, but not necessarily "clicked".
	 * 
	 * @return The item that is currently at the cursor.
	 */
	public T getSelected() 
	{
		return highlighted;
	}

	/**
	 * Loads the current contents of the list to the displayed list. 
	 */
	private void initialiseDisplay() 
	{
		// All rows will have the same parameters, so just create it once.
		TableRow.LayoutParams rowParams = new 
				TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
						TableRow.LayoutParams.WRAP_CONTENT);
		rowParams.setMargins(10, 200, 10, 10);


		TextView dayOfWeek = new TextView(appContext);
		dayOfWeek.setText(daysOfWeek[weekIndex]);
		dayOfWeek.setTextSize(18);
		dayOfWeek.setTextColor(this.appContext.getResources().getColor(R.color.myTurquesa));
		display.addView(dayOfWeek);

		// Go through the list and create table entries for each one.
		for (T item: itemsCollection) 
		{
			TableRow newRow = new TableRow(appContext);
			newRow.setLayoutParams(rowParams);

			View[] content = dataAdapter.getContent(item);
			for (int index = 0; index < content.length; index++) 
			{
				newRow.addView(content[index]);
			}

			newRow.setFocusable(true);
			newRow.setFocusableInTouchMode(true);
			newRow.setOnFocusChangeListener(new RowHighlighter(item));
			newRow.setOnTouchListener(new RowSelector(item));

			display.addView(newRow);
		}

		Button reservarButton = new Button(appContext);
		reservarButton.setText("Reservar");
		reservarButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				List<TestObject> itemsSelected = (List<TestObject>) selectionObserver.getItemsSelected();
				Log.i(">>>>> items ", ""+itemsSelected.size());
				
				// Switching to Reserva Activity
				appContext.startActivity(createReservaIntent(itemsSelected));
			}
		});

		display.addView(reservarButton);

	}
	
	public Intent createReservaIntent(List<TestObject> itemsSelected) {
		String userID = "", poiID = "", priceMeal = "", typeOfMeal = "", mealID = "", sopaTv = "", carneTv = "", peixeTv = "", dayMeal="", monthMeal="";
		boolean sopa = false, carne = false, peixe = false;
		for(TestObject item : itemsSelected) {
			poiID = item.getMenuDetails().getPOI();
			userID = item.getUserID();
			if(item.isDinner()) {
				priceMeal = item.getMenuDetails().getMenuDinner().get(0).getPrice();
				mealID = item.getMenuDetails().getMenuDinner().get(0).getId();
				
				dayMeal = item.getMenuDetails().getMenuDinner().get(0).getDay();
				monthMeal = item.getMenuDetails().getMenuDinner().get(0).getMonth();
			}
			else {
				priceMeal = item.getMenuDetails().getMenuLunch().get(0).getPrice();
				mealID = item.getMenuDetails().getMenuLunch().get(0).getId();
				
				dayMeal = item.getMenuDetails().getMenuLunch().get(0).getDay();
				monthMeal = item.getMenuDetails().getMenuLunch().get(0).getMonth();
			}
			
			if(item.isSopa()) {
				sopaTv = item.getItems()[0];
				sopa = true;
			}
						
			if(item.isCarne()) {
				carneTv = item.getItems()[0];
				carne = true;
			}
			
			if(item.isPeixe()) {
				peixeTv = item.getItems()[0];
				peixe = true;
			}
			
			typeOfMeal = item.getTypeOfMeal();
		}
		
		Intent i = new Intent(appContext, ReservaActivity.class);
		i.putExtra("dayOfWeek", daysOfWeek[weekIndex]);
		i.putExtra("typeOfMeal", typeOfMeal);
		i.putExtra("idMeal", mealID);
		i.putExtra("sopa", sopa);
		i.putExtra("carne", carne);
		i.putExtra("peixe", peixe);
		
		i.putExtra("sopaTv", sopaTv);
		i.putExtra("carneTv", carneTv);
		i.putExtra("peixeTv", peixeTv);
		
		i.putExtra("userID", userID);
		i.putExtra("poiID", poiID);
		i.putExtra("priceMeal", priceMeal);
		
		i.putExtra("dayMeal", dayMeal);
		i.putExtra("monthMeal", monthMeal);
		i.putExtra("yearMeal", "2012");
		
		
		return i;
	}

	/**
	 * Changes the receiver that is notified when an item is "clicked".
	 * 
	 * @param receiver
	 * 			The receiver that will be notified of changes.
	 */
	public void setSelectionReceiver(SelectionReceiver<T> receiver)
	{
		this.selectionObserver = receiver;
	}

	/**
	 * This focus change listener handles the changing the background colour
	 * for highlighting the focused row. It also updates the variable keeping
	 * track the highlighted item.
	 */
	private class RowHighlighter implements  View.OnFocusChangeListener, View.OnTouchListener
	{

		/**
		 * The item that the highlighter is associated with. 
		 */
		private final T association;

		public RowHighlighter(T association) 
		{
			this.association = association;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onFocusChange(View v, boolean hasFocus)
		{
			int bgColourDisabled = Color.TRANSPARENT;
			int bgColourEnabled = v.getContext().getResources().getColor(R.color.myTurquesa);

			if (hasFocus == true) 
			{
				Log.v("ListDisplay", "hasFocus() is true");
				Log.v("ListDisplay", "display focus is " + display.isFocused());
				highlighted = association;

				v.setBackgroundColor(bgColourEnabled);
			} else {
				Log.v("ListDisplay", "hasFocus() is false");
				Log.v("ListDisplay", "display focus is " + display.isFocused());
				//v.setBackgroundColor(bgColourDisabled);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			if (event.getAction() != MotionEvent.ACTION_UP) 
			{
				v.requestFocus();
			}
			return false;
		}

	}

	/**
	 * Handles the "clicking" on a row.
	 * 
	 * @author Kah
	 */
	private class RowSelector implements View.OnTouchListener 
	{
		private final T association;

		public RowSelector(T association) 
		{
			this.association = association;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (selectionObserver != null) {
				selectionObserver.itemSelected(v, association);
			}

			return false;
		}
	}
}
