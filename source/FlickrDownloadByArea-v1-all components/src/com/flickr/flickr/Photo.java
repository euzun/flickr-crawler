package com.flickr.flickr;

public class Photo {
	private String userId;
	private String photoId;
	private String flickrMake;
	private String flickrModel;
	private String flickrSoftware;
	private String exposureTime="";
	private String exposureBias;
	private String exposureMode;
	private String isoSpeed="";
	private String imgUrl;
	private String originalDate;
	private String modifiedDate;
	private String uploadDate;
	private int imgWidth,imgHeight;
	private boolean isValid=true;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPhotoId() {
		return photoId;
	}
	public void setPhotoId(String photoId) {
		this.photoId = photoId;
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
	public String getExposureTime() {
		return exposureTime;
	}
	public void setExposureTime(String exposureTime) {
		this.exposureTime = exposureTime;
	}
	public String getExposureBias() {
		return exposureBias;
	}
	public void setExposureBias(String exposureBias) {
		this.exposureBias = exposureBias;
	}
	public String getExposureMode() {
		return exposureMode;
	}
	public void setExposureMode(String exposureMode) {
		this.exposureMode = exposureMode;
	}
	public String getIsoSpeed() {
		return isoSpeed;
	}
	public void setIsoSpeed(String isoSpeed) {
		this.isoSpeed = isoSpeed;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
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
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
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
