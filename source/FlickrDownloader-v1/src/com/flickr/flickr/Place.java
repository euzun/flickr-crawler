package com.flickr.flickr;

public class Place {
	private String placeId;
	private String placeLong;
	private String placeLat;
	private String countryCode;
	
	public Place(String placeId,String placeLong,String placeLat){
		this.placeId=placeId;
		this.placeLong=placeLong;
		this.placeLat=placeLat;
	}
	
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public String getPlaceLong() {
		return placeLong;
	}
	public void setPlaceLong(String placeLong) {
		this.placeLong = placeLong;
	}
	public String getPlaceLat() {
		return placeLat;
	}
	public void setPlaceLat(String placeLat) {
		this.placeLat = placeLat;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

}
