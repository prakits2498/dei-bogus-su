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
	private boolean pratoPrincipal;
	TestObject prato;
	
	public TestReceiver(Context parentCtx) 
	{
		this.parentCtx = parentCtx;
		listItemsSelected = new ArrayList<TestObject>();
		this.pratoPrincipal = false;
	}
	
	@Override
	public void itemSelected(View v, TestObject item)
	{

		if(listItemsSelected.contains(item)) {
			listItemsSelected.remove(item);
			v.setBackgroundColor(Color.TRANSPARENT);
			
			if(item.isCarne() || item.isPeixe())
				this.pratoPrincipal = false;
		}
		else {
			if(item.isCarne() || item.isPeixe()) {
				if(!this.pratoPrincipal) {
					this.pratoPrincipal = true;
					
					listItemsSelected.add(item);
					v.setBackgroundColor(parentCtx.getResources().getColor(R.color.myTurquesa));
				} else {
					//Nao adiciona porque j‡ existe um prato principal (carne ou peixe)
				}
			} else {
				listItemsSelected.add(item);
				v.setBackgroundColor(parentCtx.getResources().getColor(R.color.myTurquesa));
			}
			
		}
	}

	@Override
	public List<TestObject> getItemsSelected() {
		return this.listItemsSelected;
	}

}
