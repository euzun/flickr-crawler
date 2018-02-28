package com.flickr.search;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.flickr.function.Flag;

public class PhotoExecuter implements Runnable{
	ExecutorService executorService=Executors.newSingleThreadScheduledExecutor(Flag.threadFactory);
	private String photoID,userID;
	Future<?> f;
	public PhotoExecuter(String photoID, String userID){
		this.photoID=photoID;
		this.userID=userID;
	}
	
	@Override
	public void run() {
		f=executorService.submit(new PhotoWorker(photoID, userID));
		executorService.shutdown();
		
		try {
			if(executorService.awaitTermination(8, TimeUnit.SECONDS)){
				if( Flag.getApiPerm() ){ f.cancel(true); }
			}
		} catch (InterruptedException e) {
		}
		
	}

}
