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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import su.android.mapviewutil.GeoBounds;
import su.android.mapviewutil.GeoItem;
import su.android.model.POI;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

/**
 * Class for Clustering geotagged content.
 * this clustering came from "markerclusterer" which is available as opensource at
 * http://code.google.com/p/gmaps-utility-library/
 * this is android ported version with modification to fit to the application
 * @author Huan Erdao
 */
public class ClusteringAlgorithm {
	
	/** grid size for clustering(dip). */
	public final static int GRIDSIZE = 70;//56;

	/** screen density for multi-resolution
	 *	get from contenxt.getResources().getDisplayMetrics().density;  */
	protected float screenDensity_ = 1.0f;
	
	/** MapView object. */
	protected final MapView mapView_;
	/** GeoItem ArrayList object to be shown. */
	protected List<GeoItem> items_ = new ArrayList<GeoItem>();
	/** GeoItem ArrayList object that are out of viewport to be clustered. */
	protected List<GeoItem> leftItems_ = new ArrayList<GeoItem>();
	/** Clustered object list. */
	protected List<GeoCluster> clusters_ = new ArrayList<GeoCluster>();
	/** MarkerBitmap object for marker icons. */
	protected final List<MarkerBitmap> markerIconBmps_;
	/** selected cluster object. */
	protected GeoCluster selcluster_ = null;
	/** check counter for tapping all cluster object. */
	protected int tapCheckCount_ = 0;
	/** GeoBound to check moves of the map view. */
	protected GeoBounds savedBounds_;
	/** flag for detecting map moves. true if map is moving or zooming. */
	protected boolean isMoving_;
	
	private HashMap<GeoCluster, List<POI>> poiCluster;
	/**
	 * @param mapView MapView object.
	 * @param markerIconBmps MarkerBitmap objects for icons.
	 * @param screenDensity Screen Density.
	 */
	public ClusteringAlgorithm(MapView mapView, List<MarkerBitmap> markerIconBmps, float screenDensity){
		mapView_ = mapView;
		markerIconBmps_ = markerIconBmps;
		screenDensity_ = screenDensity;
		isMoving_ = false;
		poiCluster = new HashMap<GeoCluster, List<POI>>();
	}

	public void addItems(List<POI> poiList)
	{
		for(int i = 0; i < poiList.size(); i++)
		{
			POI poi = poiList.get(i);
			double lat = poi.getLocationArray()[0];
			double lng = poi.getLocationArray()[1];		
			GeoItem item = new GeoItem(i, (int) (lat * 1E6),
					(int) (lng * 1E6), poi.getAffluence());
			GeoCluster cluster = addItem(item);
			if(cluster != null)
			{
				if(poiCluster.containsKey(cluster))
				{
					List<POI> temp = poiCluster.get(cluster);
					temp.add(poi);
				}
				else
				{
					List<POI> temp = new ArrayList<POI>();
					temp.add(poi);
					poiCluster.put(cluster, temp);
				}
			}
		}
	}
	
	public GeoCluster addItem(GeoItem item) 
	{
		// if not in viewport, add to leftItems_
		if(!isItemInViewport(item)) {
			leftItems_.add(item);
			return null;
		}
		// else add to items_;
		items_.add(item);
		GeoCluster cluster = null;
		Projection proj = mapView_.getProjection();
		Point pos = proj.toPixels(item.getLocation(), null);
		// check existing cluster
		for(int i=0; i < clusters_.size(); i++) 
		{
			  cluster = clusters_.get(i);
			  GeoPoint gpCenter = cluster.getLocation();
			  if(gpCenter == null)
				  continue;
			  Point ptCenter = proj.toPixels(gpCenter,null);
			  // find a cluster which contains the marker.
			  final int GridSizePx = (int) (GRIDSIZE * screenDensity_ + 0.5f);
			  if(pos.x >= ptCenter.x - GridSizePx && pos.x <= ptCenter.x + GridSizePx &&
				  pos.y >= ptCenter.y - GridSizePx && pos.y <= ptCenter.y + GridSizePx) 
			  {
				  cluster.addItem(item);
				  return cluster;
			  }
		}
		// No cluster contain the marker, create a new cluster.
		return createCluster(item);
	}

	/**
	 * Create Cluster Object.
	 * override this method, if you want to use custom GeoCluster class.
	 * @param item GeoItem to be set to cluster.
	 */
	 private GeoCluster createCluster(GeoItem item){
		 GeoCluster cluster = new GeoCluster(this);
		 cluster.addItem(item);
		 clusters_.add(cluster);
		 return cluster;
	 }

	 public List<GeoCluster> getClusters()
	 {
		 for(GeoCluster geoCluster: clusters_)
		 {
			 for(POI poi: this.poiCluster.get(geoCluster))
			 {
				 poi.setZoneAffluence((int)(poi.getAffluence()*100)/Math.max((int)geoCluster.getAffluence(), 1));
			 }
		 }
		 return this.clusters_;
	 }
	
	/**
	 * check if the item is within current viewport.
	 * @return true if item is within viewport.
	 */
	protected final boolean isItemInViewport(GeoItem item){
		savedBounds_ = getCurBounds();
		return savedBounds_.isInBounds(item.getLocation());
	}

	/**
	 * get current Bound
	 * @return current GeoBounds
	 */
	protected final GeoBounds getCurBounds()
	{
		Projection proj = mapView_.getProjection();
		return new GeoBounds(proj.fromPixels(0,0),proj.fromPixels(mapView_.getWidth(),mapView_.getHeight()));
	}

	/**
	 * add items that were not clustered in last clustering.
	 */
	protected void addLeftItems() {
		if(leftItems_.size()==0){
			return;
		}
		ArrayList<GeoItem> currentLeftItems = new ArrayList<GeoItem>();
		currentLeftItems.addAll(leftItems_);
		leftItems_.clear();
		for(int i=0; i<currentLeftItems.size(); i++) {
			addItem(currentLeftItems.get(i));
		}
	}

	/**
	 * re-add items for clustering.
	 * @param items GeoItem list to be clustered.
	 */
	protected void reAddItems(List<GeoItem> items) {
		int len = items.size();
		for(int i=len-1; i>=0; i--) {
			addItem(items.get(i));
		}
		addLeftItems();
	}
	
	public void clearAlgorithm() 
	{
		this.items_.clear();
		this.clusters_.clear();
		this.leftItems_.clear();
	}
	
	/**
	 * get cluster
	 * @param pos position of cluster.
	 * @return GeoCluster object. null if index out of bounds
	 */
	public GeoCluster getCluster(int pos) {
		if( pos < 0 || pos > clusters_.size() )
			return null;
		return clusters_.get(pos);
	}
	
	public MapView getMap()
	{
		return this.mapView_;
	}
	
	public float getDensity()
	{
		return this.screenDensity_;
	}
	
	public List<MarkerBitmap> getBitmaps()
	{
		return this.markerIconBmps_;
	}
	
	public List<POI> getClusterPoiList(GeoCluster cluster)
	{
		if(poiCluster.containsKey(cluster))
		{
			return poiCluster.get(cluster);
		}
		return new ArrayList<POI>();
	}
	
	
}
