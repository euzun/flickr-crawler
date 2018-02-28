package com.flickr.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.flickr.function.Flag;
import com.flickr.function.HTTPRequest;
import com.flickr.function.Sleep;

public class UserWorker{
	ExecutorService photoExecutorService=Executors.newCachedThreadPool();
	private String userId;
	HTTPRequest httpRequest;
	Set<String> photoIDList;
	PhotoExecuter photoExecuter;
	Future<?> future;
	ArrayList<Future> threadList=new ArrayList<Future>();
	public UserWorker(String userId){
		this.userId=userId;
	}

	public void getPhotoList4User(){
		photoIDList=new HashSet<String>();
		if(Flag.getApiPerm()){ photoIDList=(new HTTPRequest()).getPhotoIDList(this.userId); }

		if(photoIDList!=null){
			for(String photoId:photoIDList){
				photoExecuter=new PhotoExecuter(photoId, this.userId);

				if(Flag.getApiPerm()){ 
					future=photoExecutorService.submit(photoExecuter); 
					threadList.add(future);
				}
//				if(Flag.getApiPerm()){ photoWorker.run(); }
				//			Limit thread counts

				while(Flag.photoCounter>Flag.maxPhotoWorker){
					Sleep.sleep(200);
				}
				
			}
		}

		//		Shutdown photoWorker executor and wait until all submitted tasks finish
		photoExecutorService.shutdown();
		
		while(!photoExecutorService.isTerminated()){
			Sleep.sleep(200);
		}
//		try {
//			if(!photoExecutorService.awaitTermination(10, TimeUnit.SECONDS)){
////				There are threads not terminated
//				for(Future<?> f:threadList){
//					if(f.cancel(true)){
//						System.out.println(threadList.indexOf(f));
//					}
//				}
//			}
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}


	}


}
