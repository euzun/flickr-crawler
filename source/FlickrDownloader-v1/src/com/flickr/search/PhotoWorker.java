package com.flickr.search;

import com.flickr.control.PhotoPreValidation;
import com.flickr.db.PhotoDB;
import com.flickr.flickr.Photo;
import com.flickr.function.Flag;

public class PhotoWorker implements Runnable{

	private String photoID,userID;
	Photo photo;
	PhotoDB photoDB;
	PhotoPreValidation photoValidation;
	public PhotoWorker(String photoID, String userID){
		this.photoID=photoID;
		this.userID=userID;
	}
	
	private void crawlPhoto(){
		photoValidation=new PhotoPreValidation();
		photoDB=new PhotoDB();
		if(Flag.getApiPerm() && (photo=photoValidation.isPreValid(this.photoID)).isValid() ){
			photo.setUserId(this.userID);
			if(photoDB.insertPhotoID(photo)){
				Flag.lastPhotoInserted=photoID;
				Flag.totalPhotoInserted++;
			}
		}
	}
	@Override
	public void run() {
		crawlPhoto();
	}

}
