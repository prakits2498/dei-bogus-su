package su.android.client;

import greendroid.widget.QuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;

public class CategoryGridView 
{
	private QuickActionWidget gridView;
	private MainScreen mainScreen;
	
	public CategoryGridView(final MainScreen mainScreen)
	{
		this.mainScreen= mainScreen;
		gridView = new QuickActionGrid(mainScreen);		
		gridView.addQuickAction(new MyQuickAction(mainScreen,
				R.drawable.arts_entertainment_44, R.string.mainCat1));
		gridView.addQuickAction(new MyQuickAction(mainScreen,
				R.drawable.food_44, R.string.mainCat2));
		gridView.addQuickAction(new MyQuickAction(mainScreen,
				R.drawable.cafe_44, R.string.mainCat3));
		gridView.addQuickAction(new MyQuickAction(mainScreen,
				R.drawable.nightlife_44, R.string.mainCat4));
		gridView.addQuickAction(new MyQuickAction(mainScreen,
				R.drawable.shops_44, R.string.mainCat5));
		gridView.addQuickAction(new MyQuickAction(mainScreen,
				R.drawable.parks_outdoors_44, R.string.mainCat6));
		gridView.addQuickAction(new MyQuickAction(mainScreen,
				R.drawable.all_cat_44, R.string.all));

		gridView.setOnQuickActionClickListener(new OnQuickActionClickListener() {
			public void onQuickActionClicked(QuickActionWidget widget, int position) 
			{
				String category;
				switch(position)
				{
					case 0: category = mainScreen.getResources().getString(R.string.mainCat1);break;
					case 1: category = mainScreen.getResources().getString(R.string.mainCat2);break; 
					case 2: category = mainScreen.getResources().getString(R.string.mainCat3);break; 
					case 3: category = mainScreen.getResources().getString(R.string.mainCat4);break; 
					case 4: category = mainScreen.getResources().getString(R.string.mainCat5);break; 
					case 5: category = mainScreen.getResources().getString(R.string.mainCat6);break; 
					case 6: category = mainScreen.getResources().getString(R.string.all);break; 
					default: category = mainScreen.getResources().getString(R.string.all); break;
				}
				mainScreen.getCurrentAppContext().setCategory(category);
				mainScreen.onNotifyItemsOverlay(category);
				//mainScreen.onNotifyClusterOverlay(category);
			}
		});
	}
	
	public void onActivateCategory(View v)
	{
		this.gridView.show(v);
	}
	
	
	private static class MyQuickAction extends QuickAction {

		private static final ColorFilter BLACK_CF = new LightingColorFilter(
				Color.BLACK, Color.BLACK);

		public MyQuickAction(Context ctx, int drawableId, int titleId) {
			super(ctx, buildDrawable(ctx, drawableId), titleId);
		}

		private static Drawable buildDrawable(Context ctx, int drawableId) {
			Drawable d = ctx.getResources().getDrawable(drawableId);
			return d;
		}

	}
}
