package su.android.model;



import su.android.client.DisplayAdapter;
import su.android.client.R;
import android.content.Context;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

public class TestAdapter implements DisplayAdapter<TestObject>
{

	private Context appContext;
	
	public TestAdapter(Context appContext) 
	{
		this.appContext = appContext;
	}
	
	@Override
	public View[] getContent(TestObject rowObject)
	{
		
		
		TextView contents[] = new TextView[2];
		String[] labels = rowObject.getItems();
		
		TableRow.LayoutParams textViewParams = new 
				TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
						TableRow.LayoutParams.WRAP_CONTENT);
		textViewParams.setMargins(20, 20, 5, 10); //setMargins(left, top, right, bottom);
	
		for (int index = 0; index < 2; index++) 
		{
			contents[index] = new TextView(appContext);
			
			contents[index].setMaxWidth(220);
			contents[index].setLayoutParams(textViewParams);
			
			if(index == 0) {
				contents[index].setTextColor(this.appContext.getResources().getColor(R.color.myDarkGray));
				contents[index].setTextSize(16);
			}
			else {
				contents[index].setTextColor(this.appContext.getResources().getColor(R.color.orange));
				contents[index].setTextSize(12);
			}
			
			contents[index].setText(labels[index]);
		}
		
		return contents;
	}

}
