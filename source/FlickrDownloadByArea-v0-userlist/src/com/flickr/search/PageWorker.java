package com.flickr.search;

import java.util.Set;

import com.flickr.db.UserDB;
import com.flickr.flickr.BBox;
import com.flickr.flickr.User;
import com.flickr.function.Flag;
import com.flickr.function.HttpRequest;

public class PageWorker implements Runnable{
	
	private BBox bbox;
	private int page;
	public PageWorker(BBox bbox,int page){
		this.bbox=bbox;
		this.page=page;
	}

	@Override
	public void run() {
		Set<User> userList=null;
		HttpRequest httpRequest=new HttpRequest();
		if(Flag.getApiPerm()){userList=httpRequest.getUserIDList(bbox, page);}
		UserDB userDB=new UserDB();
		
		for(User user:userList){
			synchronized (PageWorker.class) {
				if(userDB.insertUser(user)){

					Flag.totalUserCrawled++;
					Flag.lastUserCrawled=user.getUserId()+" / "+user.getUserName();
				}
			}
			user=null;
		}
		
		// Object release	
		bbox=null;
		userList.clear();
		userList=null;
		httpRequest=null;
		userDB=null;
	}

}