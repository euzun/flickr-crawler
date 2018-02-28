package com.flickr.search;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.flickr.flickr.BBox;
import com.flickr.function.Flag;
import com.flickr.function.Sleep;

public class PageExecuter implements Runnable{
	private ExecutorService executorService=Executors.newSingleThreadExecutor();
	private PageWorker pageWorker;
	private BBox bbox;
	private int page;
	public PageExecuter(BBox bbox,int page){
		this.bbox=bbox;
		this.page=page;
	}
	@Override
	public void run() {
		pageWorker=new PageWorker(bbox,page);
		if(Flag.getApiPerm()){ 
			executorService.submit(pageWorker); 
		}
		executorService.shutdown();
		
		while(!executorService.isTerminated()){
			Sleep.sleep(200);
		}
		
		// Object release	
		bbox=null;
		pageWorker=null;
		executorService=null;
	}

}
