package com.flickr.search;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Set;

import com.flickr.db.UserDB;
import com.flickr.flickr.Request;
import com.flickr.flickr.User;
import com.flickr.function.Flag;
import com.flickr.function.HTTPRequest;

public class PageWorker implements Runnable{
	String placeID,nextLine,response;
	int page;
	String flickrId="owner";
	
	public PageWorker(String placeID,int page){
		this.placeID=placeID;
		this.page=page;
	}

	@Override
	public void run() {
		BufferedReader bufferedReader=null;
		HTTPRequest httpRequest=new HTTPRequest();
		if(Flag.getApiPerm()){  bufferedReader= httpRequest.getHttpResponse(Request.getRequestLink(placeID,flickrId, page)); }
		Set<String> userIdList=new HashSet<String>();
		User user=new User("",placeID);
		UserDB userDB=new UserDB();
		
		try{
			while ((nextLine = bufferedReader.readLine()) != null) {
				response=httpRequest.getTagValue(nextLine, flickrId);
				if (response.length()>0) {
					userIdList.add(response);
				}
			}
			bufferedReader.close();
			
			for(String userID:userIdList){
				user.setUserId(userID);
				if((!userDB.IsInsertedUser(userID)) && userDB.insertUser(user)){
					Flag.lastUserInserted=placeID;
					Flag.totalUserInserted++;
				}
				Flag.totalUserSearched++;
			}
			userIdList.clear();
		}catch(Exception e){}
	}

}