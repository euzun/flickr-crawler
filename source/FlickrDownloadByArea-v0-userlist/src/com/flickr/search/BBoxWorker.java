package com.flickr.search;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.flickr.flickr.BBox;
import com.flickr.function.Flag;
import com.flickr.function.HttpRequest;
import com.flickr.function.Sleep;


public class BBoxWorker{
	ExecutorService executorService=Executors.newCachedThreadPool();
	HttpRequest httpRequest=new HttpRequest();
	@SuppressWarnings("rawtypes")
	ArrayList<Future> threadList=new ArrayList<Future>();
	PageExecuter pageExecuter;
	public void search4BBox(BBox bbox){
		Flag.searchedEntry4BBox=0;
		int totalPage=0;
		if(Flag.getApiPerm()){  totalPage=httpRequest.getEntryPageCount(bbox); }

		for(int i=0;i<totalPage;i++){
			pageExecuter=new PageExecuter(bbox, i+1);
			if(Flag.getApiPerm()){ 
				threadList.add(executorService.submit(pageExecuter));
			}
			
			while(Flag.getApiPerm() && threadList.size()>Flag.maxWorker){	// Limit thread counts
				Sleep.sleep(200);
				for(int j=threadList.size()-1;j>=0;j--){
					if(threadList.get(j).isDone()){
						threadList.remove(j);
					}
				}
			}
			
			pageExecuter=null;
		}
		
		
		executorService.shutdown();
		while(!executorService.isTerminated()){
			Sleep.sleep(500);
		}
		
		// Object release
		threadList=null;
		bbox=null;		
		httpRequest=null;
		executorService=null;
	}
}