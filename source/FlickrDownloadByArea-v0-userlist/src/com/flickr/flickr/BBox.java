package com.flickr.flickr;

public class BBox {
	private int bboxId;
	private double minLat,minLong,maxLat,maxLong;

	public BBox(double minLong, double minLat, double maxLong, double maxLat) {
		super();
		this.minLat = minLat;
		this.minLong = minLong;
		this.maxLat = maxLat;
		this.maxLong = maxLong;
	}
	
	public BBox(int bboxId, double minLong, double minLat, double maxLong, double maxLat) {
		super();
		this.bboxId = bboxId;
		this.minLat = minLat;
		this.minLong = minLong;
		this.maxLat = maxLat;
		this.maxLong = maxLong;
	}

	public int getBboxId() {
		return bboxId;
	}

	public void setBboxId(int bboxId) {
		this.bboxId = bboxId;
	}

	public double getMinLat() {
		return minLat;
	}

	public void setMinLat(double minLat) {
		this.minLat = minLat;
	}

	public double getMinLong() {
		return minLong;
	}

	public void setMinLong(double minLong) {
		this.minLong = minLong;
	}

	public double getMaxLat() {
		return maxLat;
	}

	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}

	public double getMaxLong() {
		return maxLong;
	}

	public void setMaxLong(double maxLong) {
		this.maxLong = maxLong;
	}

	@Override
	public String toString() {
		return minLong + "," + minLat + "," + maxLong + "," +maxLat;
	}
	
	public String toCenterPoint(){
		return Math.round((minLat+maxLat)*500)/1000.0+","+Math.round((minLong+maxLong)*500)/1000.0;
	}
}
