package com.flickr.search;

import java.util.HashSet;
import java.util.Set;

import com.flickr.db.User;
import com.flickr.db.UserDB;
import com.flickr.function.Flag;
import com.flickr.function.HTTPRequest;

public class PlaceIDWorker {
	String placeID,countryCode;
	UserWorker userWorker;
	Set<String> userIDList;
	User user;
	UserDB userDB;

	public PlaceIDWorker(String placeID, String countryCode){
		this.placeID=placeID;
		this.countryCode=countryCode;
	}

	public void getUserList4PlaceID(){
		userIDList=new HashSet<String>();
		if(Flag.getApiPerm()){ userIDList=(new HTTPRequest()).getUserIDList(placeID); }

		userDB=new UserDB();

		if(userIDList!=null){
			for(String userId:userIDList){
				user=new User(userId,placeID,countryCode);

				userWorker=new UserWorker(userId);

				if(Flag.getApiPerm()){ userWorker.getPhotoList4User(); }

				if(userDB.insertUser(user)){
					Flag.totalUser++;
					Flag.lastFinUser=userId;
				}

			}
		}
	}
}


