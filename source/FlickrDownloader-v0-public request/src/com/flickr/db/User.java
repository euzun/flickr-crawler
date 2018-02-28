package com.flickr.db;

public class User {
	private String userId;
	private String placeId;
	private String countryCode;
	
	public User(String userId,String placeId,String countryCode){
		this.userId=userId;
		this.placeId=placeId;
		this.countryCode=countryCode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
}
