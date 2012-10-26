package su.android.client;

import java.util.List;

import android.view.View;


/**
 * Defines the interface for notifying 
 * 
 * @param <T>
 * 			The type of objects that will be received 
 * 
 * @author Kah
 */
public interface SelectionReceiver<T>
{
	/**
	 * This method is triggered when an item is "clicked".
	 * 
	 * @param item
	 * 			The item that was selected.
	 */
	void itemSelected(View v, T item);
	List<T> getItemsSelected();
}
