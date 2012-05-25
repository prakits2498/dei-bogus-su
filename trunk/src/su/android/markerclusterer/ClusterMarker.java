/*
 * Copyright (C) 2009 Huan Erdao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package su.android.markerclusterer;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;
import android.util.Log;

import su.android.mapviewutil.GeoBounds;
import su.android.mapviewutil.GeoItem;
import su.android.markerclusterer.GeoClusterer.GeoCluster;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * Overlay extended class to display Clustered Marker.
 * 
 * @author Huan Erdao
 */
public class ClusterMarker extends Overlay {

	/** cluster object */
	protected final GeoCluster cluster_;
	/**
	 * screen density for multi-resolution get from
	 * contenxt.getResources().getDisplayMetrics().density;
	 */
	protected float screenDensity_ = 1.0f;

	protected final float TXTSIZE = 16.0f;

	/** Paint object for drawing icon */
	protected final Paint paint_;
	/** List of GeoItems within */
	protected final List<GeoItem> GeoItems_;
	/** center of the cluster */
	protected final GeoPoint center_;
	/** Bitmap objects for icons */
	protected final List<MarkerBitmap> markerIconBmps_;
	/** icon marker type */
	protected int markerTypes = 0;
	/** select state for cluster */
	protected boolean isSelected_ = false;
	/** selected item number in GeoItem List */
	protected int selItem_;
	/** Text Offset */
	protected int txtHeightOffset_;
	/** number of checkins of GeoItems within the cluster */
	protected int nCheckins;

	/**
	 * @param cluster
	 *            a cluster to be rendered for this marker
	 * @param markerIconBmps
	 *            icon set for marker
	 */
	public ClusterMarker(GeoCluster cluster, List<MarkerBitmap> markerIconBmps,
			float screenDensity) {
		cluster_ = cluster;
		markerIconBmps_ = markerIconBmps;
		center_ = cluster_.getLocation();
		GeoItems_ = cluster_.getItems();
		nCheckins = cluster_.getnCheckins();
		screenDensity_ = screenDensity;
		paint_ = new Paint();
		paint_.setStyle(Paint.Style.STROKE);
		paint_.setAntiAlias(true);
		paint_.setColor(Color.WHITE);
		paint_.setTextSize(TXTSIZE * screenDensity_);
		paint_.setTextAlign(Paint.Align.CENTER);
		paint_.setTypeface(Typeface.DEFAULT_BOLD);
		FontMetrics metrics = paint_.getFontMetrics();
		txtHeightOffset_ = (int) ((metrics.bottom + metrics.ascent) / 2.0f);
		/* check if we have selected item in cluster */
		selItem_ = 0;
		for (int i = 0; i < GeoItems_.size(); i++) {
			if (GeoItems_.get(i).isSelected()) {
				selItem_ = i;
				isSelected_ = true;
			}
		}
		setMarkerBitmap();
	}

	/**
	 * change icon bitmaps according to the state.
	 */
	protected void setMarkerBitmap() {
		markerTypes = -1;
		for (int i = 0; i < markerIconBmps_.size(); i++) {
			if (GeoItems_.size() < markerIconBmps_.get(i).getItemMax()) { // COMO DEFINIMOS O TAMANHO DO ICONE? QUAL O CRITERIO?
				markerTypes = i;
				paint_.setTextSize(markerIconBmps_.get(markerTypes)
						.getTextSize() * screenDensity_);
				FontMetrics metrics = paint_.getFontMetrics();
				txtHeightOffset_ = (int) ((metrics.bottom + metrics.ascent) / 2.0f);
				break;
			}
		}
		if (markerTypes < 0)
			markerTypes = markerIconBmps_.size() - 1;
	}

	/**
	 * draw icon.
	 * 
	 * @param canvas
	 *            Canvas object.
	 * @param mapView
	 *            MapView object.
	 * @param shadow
	 *            shadow flag.
	 */
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		if (mapView.getZoomLevel() < 16) {
			// cluster_.onNotifyDrawFromMarker();
			Projection proj = mapView.getProjection();
			Point p = proj.toPixels(center_, null);
			GeoBounds bounds = new GeoBounds(proj.fromPixels(0, 0),
					proj.fromPixels(mapView.getWidth(), mapView.getHeight()));
			if (!bounds.isInBounds(center_))
				return;
			MarkerBitmap mkrBmp = markerIconBmps_.get(markerTypes);
			Bitmap bmp = isSelected_ ? mkrBmp.getBitmapSelect() : mkrBmp
					.getBitmapNormal();
			Point grid = mkrBmp.getGrid();
			Point gridReal = new Point((int) (grid.x * screenDensity_ + 0.5f),
					(int) (grid.y * screenDensity_ + 0.5f));
			canvas.drawBitmap(bmp, p.x - gridReal.x, p.y - gridReal.y, paint_);
			// Draw a circle
			// Paint circle = new Paint(Paint.ANTI_ALIAS_FLAG);
			// the circle to mark the spot
			// circle.setColor(Color.parseColor("#88ff0000"));
			// circle.setAlpha(35); // trasparenza
			// // int radius = metersToRadius(1000, mapView,
			// // (double) center_.getLatitudeE6() / 1000000);
			// canvas.drawCircle(p.x, p.y, 56, circle);
			// End of circle
			String caption = String.valueOf(nCheckins);
			int x = p.x;
			int y = p.y - txtHeightOffset_;
			canvas.drawText(caption, x, y, paint_);
		}
	}

	private int metersToRadius(float meters, MapView map, double latitude) {
		return (int) (map.getProjection().metersToEquatorPixels(meters) * (1 / Math
				.cos(Math.toRadians(latitude))));
	}

	/**
	 * check if the marker is selected.
	 * 
	 * @return true if selected state.
	 */
	public boolean isSelected() {
		return isSelected_;
	}

	/**
	 * clears selected state.
	 */
	public void clearSelect() {
		isSelected_ = false;
		if (selItem_ < GeoItems_.size()) {
			GeoItems_.get(selItem_).setSelect(false);
		}
		setMarkerBitmap();
	}

	/**
	 * get center location of the marker.
	 * 
	 * @return GeoPoint object of current marker center.
	 */
	public GeoPoint getLocation() {
		return center_;
	}

	/**
	 * get selected item's location. null if nothing is selected.
	 * 
	 * @return GeoPoint object for selected item. null if nothing selected.
	 */
	public GeoPoint getSelectedItemLocation() {
		if (selItem_ < GeoItems_.size()) {
			return GeoItems_.get(selItem_).getLocation();
		}
		return null;
	}

	@Override
	public boolean onTap(final GeoPoint p, final MapView mapView) {
		Projection proj = mapView.getProjection();
		Point pos = proj.toPixels(p, null);
		GeoPoint gpCenter = cluster_.getLocation();
		Point ptCenter = proj.toPixels(gpCenter, null);
		final int GridSizePx = (int) (GeoClusterer.GRIDSIZE * screenDensity_ + 0.5f);
		if (pos.x >= ptCenter.x - GridSizePx
				&& pos.x <= ptCenter.x + GridSizePx
				&& pos.y >= ptCenter.y - GridSizePx
				&& pos.y <= ptCenter.y + GridSizePx) {
			Log.i("ClusterMarker", "TAPPED!");
			this.cluster_.onTapCalledFromMarker(true);
		}
		return false;
	}

	protected boolean onTap(int index) {
		return true;
	}
}
