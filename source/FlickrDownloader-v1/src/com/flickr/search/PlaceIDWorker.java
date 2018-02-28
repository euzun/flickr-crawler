package com.flickr.search;

import java.util.HashSet;
import java.util.Set;

import com.flickr.db.UserDB;
import com.flickr.flickr.User;
import com.flickr.function.Flag;
import com.flickr.function.HTTPRequest;


public class PlaceIDWorker {
	private Set<String> userIDList=new HashSet<String>();
	private UserDB userDB=new UserDB();
	private User user;
	private String placeID;
	private UserWorker userWorker;

	public PlaceIDWorker(String placeID){
		this.placeID=placeID;
	}

	public void getUserList4PlaceID(){
		Flag.searchedUser4Place=0;
		if(Flag.getApiPerm()){ userIDList=(new HTTPRequest()).getUserIDList(placeID); }
		
		Flag.totalUser4Place=userIDList.size();
		
		for(String userId:userIDList){
			Flag.userCrawlingNow=userId;
			
			if(!userDB.IsInsertedUser(userId)){
				user=new User(userId,placeID);
				userWorker=new UserWorker(userId);
				if(Flag.getApiPerm()){ userWorker.getPhotoList4User(); }
				
				if(userDB.insertUser(user)){
					Flag.lastUserInserted=userId;
					Flag.totalUserInserted++;
				}
			}
			
			Flag.totalUserSearched++;
			Flag.searchedUser4Place++;
		}
	}
}

