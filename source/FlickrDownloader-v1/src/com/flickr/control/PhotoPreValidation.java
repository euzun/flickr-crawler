package com.flickr.control;

import com.flickr.db.PhotoDB;
import com.flickr.flickr.Photo;
import com.flickr.function.Flag;
import com.flickr.function.HTTPRequest;

public class PhotoPreValidation {
	PhotoDB photoDB=new PhotoDB();
	Photo photo=new Photo();
	HTTPRequest httpRequest;
	
	public Photo isPreValid(String photoId) {
		photo.setPhotoId(photoId);
		try {
			if (!hasDublicate()) {
				httpRequest=new HTTPRequest();
				setSizeUrl();
				if (hasValidSize() && hasOriginalUrl() && isJpg()) {
					setExifData();
					if(hasWantedFields()){
						photo.setValid(true);
					}else{
						photo.setValid(false);
					}
				} else {
					photo.setValid(false);
				}
			} else {
				photo.setValid(false);
			}
		} catch (Exception e) {
			photo.setValid(false);
		}

		return photo;
	}
	
//	DB process
	private boolean hasDublicate() {
		return photoDB.hasDuplicate(photo.getPhotoId());
	}
	
//	Flickr-Http request process
	private void setSizeUrl(){
		if(Flag.getApiPerm()){ this.photo=httpRequest.getSizeRequest(photo); }
	}
	
	private boolean hasValidSize() {
		if (photo.getImgWidth() >= 1024	&& photo.getImgHeight() >= 1024)
			return true;
		return false;
	}

	private boolean hasOriginalUrl() {
		if (photo.getImgUrl() != null)
			return true;
		return false;
	}

	private boolean isJpg() {
		if (photo.getImgUrl().split("_o.")[1].split("\"")[0].trim().equalsIgnoreCase("jpg"))
			return true;
		return false;
	}

	private void setExifData(){
		if(Flag.getApiPerm()){ this.photo=httpRequest.getExifRequest(photo); }
	}
	
	public boolean hasWantedFields() {
		if ((photo.getExposureTime() != null) && photo.getIsoSpeed() > 0)
			return true;
		return false;

	}
	
	
}
