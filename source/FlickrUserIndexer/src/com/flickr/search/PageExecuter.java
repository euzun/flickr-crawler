package com.flickr.search;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.flickr.function.Flag;
import com.flickr.function.Sleep;

public class PageExecuter implements Runnable{
	ExecutorService executorService=Executors.newCachedThreadPool();
	String placeID;
	int page;
	public PageExecuter(String placeID,int page){
		this.placeID=placeID;
		this.page=page;
	}
	@Override
	public void run() {
		if(Flag.getApiPerm()){ executorService.submit(new PageWorker(placeID,page)); }
		executorService.shutdown();
		
		while(!executorService.isTerminated()){
			Sleep.sleep(200);
		}
	}

}
