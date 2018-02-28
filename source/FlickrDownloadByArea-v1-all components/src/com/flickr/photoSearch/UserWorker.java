package com.flickr.photoSearch;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.flickr.function.Flag;
import com.flickr.function.HttpRequest;
import com.flickr.function.Sleep;

public class UserWorker {
	@SuppressWarnings("rawtypes")
	public void crawlPhotoList4User(String userID){
		
		HttpRequest httpRequest=new HttpRequest();
		
		Flag.searchedPhoto4User=0;
		int totalPage=0;
		if(Flag.getApiPerm()){  totalPage=httpRequest.getPageCount4User(userID); }
		
		ExecutorService executorService=Executors.newCachedThreadPool();
		ArrayList<Future> threadList=new ArrayList<Future>();
		PhotoPageExecutor pageExecuter;
		
		for(int i=0;i<totalPage;i++){
			pageExecuter=new PhotoPageExecutor(userID, i+1);
			
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
		
		while(Flag.getApiPerm() && threadList.size()>0){	// Limit thread counts
			Sleep.sleep(200);
			for(int j=threadList.size()-1;j>=0;j--){
				if(threadList.get(j).isDone()){
					threadList.remove(j);
				}
			}
		}
		
		executorService.shutdown();
		while(!executorService.isTerminated()){
			Sleep.sleep(500);
			for(int j=threadList.size()-1;j>=0;j--){
				if(threadList.get(j).isDone()){
					threadList.remove(j);
				}
			}
		}
	}
	
	
}
