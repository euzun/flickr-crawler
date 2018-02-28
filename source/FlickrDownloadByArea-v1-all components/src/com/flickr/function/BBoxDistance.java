package com.flickr.function;

import java.util.ArrayList;

import com.flickr.db.BBoxDB;
import com.flickr.flickr.BBox;

public class BBoxDistance {
	/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
	/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
	public double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 111.18957696;
		return (dist);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public double dist2LongAtLat(double lat,double d){
		return rad2deg(Math.acos( (Math.cos(deg2rad(d/111.18957696))-Math.pow(Math.sin(deg2rad(lat)), 2))/Math.pow(Math.cos(deg2rad(lat)), 2) ) );
	}
	
	public ArrayList<BBox> loadBBoxList(BBox bbox){
		ArrayList<BBox> bboxList=new ArrayList<BBox>();
		BBox newBBox;
		BBoxDB bboxDB=new BBoxDB();
		double longDiff,latDiff=0.027;
		for(double i=bbox.getMinLat();i<bbox.getMaxLat()-latDiff;i=i+latDiff){
			longDiff=dist2LongAtLat(i, 3);
			for(double j=bbox.getMinLong();j<bbox.getMaxLong()-longDiff;j=j+longDiff){
				newBBox=new BBox(j, i, j+longDiff, i+latDiff);
				bboxDB.insertBBox(newBBox);
			}
		}
		
		bboxList=bboxDB.getBBoxList();
		return bboxList;
	}
	
//	public static void main(String[] arg){
//		BBoxDistance bBoxDistance=new BBoxDistance();
//		System.out.println(bBoxDistance.getBBoxList(new BBox(-79, 40, -71, 45)).size());
//	}
	
}
