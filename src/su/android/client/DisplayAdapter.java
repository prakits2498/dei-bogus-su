package su.android.client;



import android.view.View;

/**
 * The adapter is used by the {@link ListDisplay} to determine what should be
 * put in the row.
 * 
 * @param <T>
 * 			The type of objects that are adapted. This should be the same type
 * 			as the one given to the list. 
 * 
 * @author Kah
 */
public interface DisplayAdapter<T>
{	
	/**
	 * Given the object to display on the row, this will generate the views for
	 * each of the cells in the table. Each entry in the array corresponds to a
	 * column in the table. It is generally expected that the length of the 
	 * array does not change between rows (so that all rows have the same
	 * number of columns).
	 * 
	 * @param rowObject
	 * 			The object to display on the row.
	 * 
	 * @return The array containing the contents of each column for the row.
	 */
	View[] getContent(T rowObject);
}
