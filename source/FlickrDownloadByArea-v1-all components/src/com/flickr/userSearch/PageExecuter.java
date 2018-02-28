package com.flickr.userSearch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.flickr.flickr.BBox;
import com.flickr.function.Flag;
import com.flickr.function.Sleep;

public class PageExecuter implements Runnable{
	
	private BBox bbox;
	private int page;
	public PageExecuter(BBox bbox,int page){
		this.bbox=bbox;
		this.page=page;
	}
	@Override
	public void run() {
		ExecutorService executorService=Executors.newSingleThreadExecutor();
		PageWorker pageWorker=new PageWorker(bbox,page);
		
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
