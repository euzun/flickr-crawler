package com.flickr.search;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.flickr.function.Flag;
import com.flickr.function.Sleep;

public class PlaceIDExecuter implements Runnable {
	ExecutorService executorService=Executors.newSingleThreadScheduledExecutor(Flag.threadFactory);
	private String placeID;
	public PlaceIDExecuter(String placeID){
		this.placeID=placeID;
	}
	
	
	@Override
	public void run() {
		if(Flag.getApiPerm()){ executorService.submit(new PlaceIDWorker(placeID)); }
		executorService.shutdown();
		
		while(!executorService.isTerminated()){
			Sleep.sleep(200);
		}
	}

}
