package com.flickr.search;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.flickr.function.Flag;

public class PhotoExecuter implements Runnable{
	private String photoID,userID;
	ExecutorService executorService=Executors.newFixedThreadPool(1);
	Future<?> f;
	public PhotoExecuter(String photoID, String userID){
		this.photoID=photoID;
		this.userID=userID;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Flag.photoCounter++;
		
		f=executorService.submit(new PhotoWorker(photoID, userID));
		executorService.shutdown();
		try {
			if(!executorService.awaitTermination(5, TimeUnit.SECONDS)){
//				There are threads not terminated
				f.cancel(true);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Flag.photoCounter--;
		
		
	}

}
