package com.flickr.photoSearch;

import java.util.ArrayList;

import com.flickr.db.UserDB;
import com.flickr.function.Flag;

public class MainPhotoSearchThread implements Runnable{
	UserDB userDB=new UserDB();
	UserWorker userWorker=new UserWorker();
	@Override
	public void run() {
		ArrayList<String> userList=userDB.getUnfinishedUserList();
		
		Flag.totalUser=userList.size();
		
		for(int i=0;Flag.getApiPerm() && i<userList.size();i++){
			Flag.userCrawlingNow=userList.get(i);
			userWorker.crawlPhotoList4User(userList.get(i));
			
			userDB.updateUser(userList.get(i));
			Flag.totalUserCrawled++;
		}
		
	}

}
