package com.flickr.photoSearch;

import java.util.Set;

import com.flickr.db.PhotoDB;
import com.flickr.flickr.Photo;
import com.flickr.function.Flag;
import com.flickr.function.HttpRequest;

public class PhotoPageWorker implements Runnable{

	private String userID;
	private int page;
	public PhotoPageWorker(String userID,int page){
		this.userID=userID;
		this.page=page;
	}
	@Override
	public void run() {
		Set<Photo> photoList=null;
		HttpRequest httpRequest=new HttpRequest();
		if(Flag.getApiPerm()){photoList=httpRequest.getPhotoList4User(userID, page);}
		PhotoDB photoDB=new PhotoDB();
		
		for(Photo photo:photoList){
			if(photoDB.insertPhoto(photo)){
				Flag.totalPhotoCrawled++;
				Flag.lastPhotoCrawled=photo.getPhotoId();
			}
			photo=null;
		}
		// Object release	
		photoList.clear();
		photoList=null;
		httpRequest=null;
		photoDB=null;
	}

}
