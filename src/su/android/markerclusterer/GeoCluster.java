package su.android.markerclusterer;

import java.util.ArrayList;
import java.util.List;

import su.android.mapviewutil.GeoBounds;
import su.android.mapviewutil.GeoItem;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Projection;

/**
 * GeoCluster class.
 * contains single marker object(ClusterMarker). mostly wraps methods in ClusterMarker.
 */
public class GeoCluster {
	
	/**	GeoClusterer object	 */
	private final ClusteringAlgorithm clusterer_;
	/**	center of cluster */
	protected GeoPoint center_;
	/**	list of GeoItem within cluster */
	private List<GeoItem> items_ = new ArrayList<GeoItem>();
	/** zoomlevel at the point Cluster was made */
	private int zoom_;
	/** number of checkins of GeoItems within the cluster */
	protected int nCheckins;

	/**
	 * @param clusterer GeoClusterer object.
	 */
	public GeoCluster(ClusteringAlgorithm clustering){
		clusterer_ = clustering;
		zoom_ = clustering.getMap().getZoomLevel();
	}
	
	/**
	 * add item to cluster object
	 * @param item GeoItem object to be added.
	 */
	public void addItem(GeoItem item){
		if(center_ == null){
			center_ = item.getLocation();
		}
		items_.add(item);
		nCheckins += item.getnCheckins();
	}
	
	/**
	 * get center of the cluster.
	 * @return center of the cluster in GeoPoint.
	 */
	public GeoPoint getLocation(){
		return center_;
	}
	
	public int getnCheckins(){
		return nCheckins;
	}
	
	public void setnCheckins(int checkins){
		this.nCheckins = checkins;
	}


	/**
	 * get zoomlevel.
	 * @return zoom level of the cluster.
	 */
	public int getZoomLevel(){
		return zoom_;
	}
	
	/**
	 * get list of GeoItem.
	 * @return list of GeoItem within cluster.
	 */
	public List<GeoItem> getItems(){
		return items_;
	}

	/**
	 * check if the GeoBounds are within cluster.
	 * @return true if bounds are within this cluster size.
	 */
	protected boolean isInBounds(GeoBounds bounds) {
		if(center_ == null) {
			return false;
		}
		Projection pro = this.clusterer_.getMap().getProjection();
		Point nw = pro.toPixels(bounds.getNorthWest(),null);
		Point se = pro.toPixels(bounds.getSouthEast(),null);
		Point centxy = pro.toPixels(center_,null);
		boolean inViewport = true;
		int GridSizePx = (int) (ClusteringAlgorithm.GRIDSIZE * clusterer_.getDensity() + 0.5f);
		if(zoom_ != this.clusterer_.getMap().getZoomLevel()) {
			int diff = this.clusterer_.getMap().getZoomLevel() - zoom_;
			GridSizePx = (int) (Math.pow(2, diff) * GridSizePx);
		}
		if(nw.x != se.x && (centxy.x + GridSizePx < nw.x || centxy.x - GridSizePx > se.x)) {
			inViewport = false;
		}
		if(inViewport && (centxy.y + GridSizePx < nw.y || centxy.y - GridSizePx > se.y)) {
			inViewport = false;
		}
		return inViewport;
	}
	
	public GeoPoint getCenter()
	{
		return center_;
	}
}
