import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.flickr.function.ReadWrite;




public class BoundingBox {
	/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
	/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
	private double distance(double lat1, double lon1, double lat2, double lon2) {
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

	private double longDiffAtLat(double lat,double d){
		return rad2deg(Math.acos( (Math.cos(deg2rad(d/111.18957696))-Math.pow(Math.sin(deg2rad(lat)), 2))/Math.pow(Math.cos(deg2rad(lat)), 2) ) );
	}
	
	public static void main(String[] args) {
		ReadWrite rw=new ReadWrite();
		
		String s=rw.readFromFile("C:\\sample.txt");
		System.out.println(s);
		System.out.println(s.replaceAll("'", "").replaceAll("\"", "").replaceAll("/", "").replaceAll("\\\\", "").trim());
		
//		System.out.println(Integer.toHexString(16777215));
//		
//		// TODO Auto-generated method stub
//		BoundingBox bbox=new BoundingBox();
//		
//		
//		System.out.println(bbox.longDiffAtLat(40.0, 5.0));
//		
//		System.out.println(bbox.distance(43, -74, 43, -74+bbox.longDiffAtLat(43.0, 5.0)));
//		
//		double latMax=42.9133974,latMin=latMax-0.91,lngMax=-71,lngMin=-71.123;
//		int zoomLevel;
//		
//		double latDiff = latMax - latMin;
//		double lngDiff = lngMax - lngMin;
//
//		double maxDiff = (lngDiff > latDiff) ? lngDiff : latDiff;
//		if (maxDiff < 360 / Math.pow(2, 20)) {
//		    zoomLevel = 21;
//		} else {
//		    zoomLevel = (int) (-1*( (Math.log(maxDiff)/Math.log(2)) - (Math.log(360)/Math.log(2))));
//		    if (zoomLevel < 1)
//		        zoomLevel = 1;
//		}
//		try {
//			double latCenter=(latMax+latMin)/2, longCenter=(lngMax+lngMin)/2;
////            String path = "https://maps.googleapis.com/maps/api/staticmap?center="+latCenter+","+longCenter+"&zoom="+(zoomLevel)+"&size=475x250&markers=color:red%7Clabel:A%7C"+latCenter+","+longCenter;
//			String path="https://maps.googleapis.com/maps/api/staticmap?center=42.5,-75.0&zoom=5&size=475x250&markers=color:red%7Clabel:C%7C42.5,-75.0&markers=color:red%7Clabel:1%7C40.0,-79.0&markers=color:red%7Clabel:2%7C40.0,-71.0&markers=color:red%7Clabel:3%7C45.0,-71.0&markers=color:red%7Clabel:4%7C45.0,-79.0";
//            System.out.println("Get Image from " + path);
//            URL url = new URL(path);
//            BufferedImage image = ImageIO.read(url);
//            System.out.println("Load image into frame...");
//            JLabel label = new JLabel(new ImageIcon(image));
//            JFrame f = new JFrame();
//            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            f.getContentPane().add(label);
//            f.pack();
//            f.setLocation(200, 200);
//            f.setVisible(true);
//        } catch (Exception exp) {
//            exp.printStackTrace();
//        }
	}

}
