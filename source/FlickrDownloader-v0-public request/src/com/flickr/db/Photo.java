package com.flickr.db;

public class Photo {
	private String photoId="";
	private String userId="";
	private String flickrMake="";
	private String flickrModel="";
	private String flickrSoftware="";
	private int imgWidth;
	private int imgHeight;
	private String exposureTime="";
	private double exposureBias;
	private String exposureMode="";
	private int isoSpeed;
	private String imgUrl="";
	private String originalDate="";
	private String modifiedDate="";
	private boolean isValid;
	public String getPhotoId() {
		return photoId;
	}
	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFlickrMake() {
		return flickrMake;
	}
	public void setFlickrMake(String flickrMake) {
		this.flickrMake = flickrMake;
	}
	public String getFlickrModel() {
		return flickrModel;
	}
	public void setFlickrModel(String flickrModel) {
		this.flickrModel = flickrModel;
	}
	public String getFlickrSoftware() {
		return flickrSoftware;
	}
	public void setFlickrSoftware(String flickrSoftware) {
		this.flickrSoftware = flickrSoftware;
	}
	public int getImgWidth() {
		return imgWidth;
	}
	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}
	public int getImgHeight() {
		return imgHeight;
	}
	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}
	public String getExposureTime() {
		return exposureTime;
	}
	public void setExposureTime(String exposureTime) {
		this.exposureTime = exposureTime;
	}
	public double getExposureBias() {
		return exposureBias;
	}
	public void setExposureBias(double exposureBias) {
		this.exposureBias = exposureBias;
	}
	public String getExposureMode() {
		return exposureMode;
	}
	public void setExposureMode(String exposureMode) {
		this.exposureMode = exposureMode;
	}
	public int getIsoSpeed() {
		return isoSpeed;
	}
	public void setIsoSpeed(int isoSpeed) {
		this.isoSpeed = isoSpeed;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getOriginalDate() {
		return originalDate;
	}
	public void setOriginalDate(String originalDate) {
		this.originalDate = originalDate;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
}
