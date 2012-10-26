package su.android.model;



import java.util.ArrayList;
import java.util.List;

import su.android.client.R;
import su.android.client.SelectionReceiver;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class TestReceiver implements SelectionReceiver<TestObject>
{
	private final Context parentCtx;
	private List<TestObject> listItemsSelected;
	
	public TestReceiver(Context parentCtx) 
	{
		this.parentCtx = parentCtx;
		listItemsSelected = new ArrayList<TestObject>();
	}
	
	@Override
	public void itemSelected(View v, TestObject item)
	{
		//AlertDialog.Builder builder = new AlertDialog.Builder(parentCtx);
		
		
		if(listItemsSelected.contains(item)) {
			listItemsSelected.remove(item);
			v.setBackgroundColor(Color.TRANSPARENT);
		}
		else {
			listItemsSelected.add(item);
			v.setBackgroundColor(parentCtx.getResources().getColor(R.color.myTurquesa));
		}
		
		/*String items[] = item.getItems();
		builder.setMessage("Selected items: "+listItemsSelected.size());
		AlertDialog dialog = builder.create();
		dialog.show();*/
	}

	@Override
	public List<TestObject> getItemsSelected() {
		return this.listItemsSelected;
	}

}
