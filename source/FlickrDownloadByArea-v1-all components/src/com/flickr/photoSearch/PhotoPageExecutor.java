package com.flickr.photoSearch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.flickr.function.Flag;
import com.flickr.function.Sleep;

public class PhotoPageExecutor implements Runnable{
	private String userID;
	private int page;
	public PhotoPageExecutor(String userID,int page){
		this.userID=userID;
		this.page=page;
	}
	@Override
	public void run() {
		ExecutorService executorService=Executors.newSingleThreadExecutor();
		PhotoPageWorker pageWorker=new PhotoPageWorker(userID,page);
		if(Flag.getApiPerm()){ 
			executorService.submit(pageWorker); 
		}
		executorService.shutdown();
		
		while(!executorService.isTerminated()){
			Sleep.sleep(200);
		}
		
		// Object release	
		pageWorker=null;
		executorService=null;
	}

}
