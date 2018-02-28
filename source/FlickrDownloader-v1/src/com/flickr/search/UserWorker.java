package com.flickr.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.flickr.function.Flag;
import com.flickr.function.HTTPRequest;
import com.flickr.function.Sleep;

public class UserWorker{
	
	@SuppressWarnings("rawtypes")
	private ArrayList<Future> threadList;
	private ExecutorService photoExecutorService;
	private Set<String> photoIDList=new HashSet<String>();
	private PhotoExecuter photoExecuter;
	private Future<?> future;
	private String userId;

	public UserWorker(String userId){
		this.userId=userId;
	}
	
	@SuppressWarnings("rawtypes")
	public void getPhotoList4User(){

		Flag.searchedPhoto4User=0;
		
		if(Flag.getApiPerm()){ photoIDList=(new HTTPRequest()).getPhotoIDList(this.userId); }
		if(photoIDList.size()>50){
			Flag.threadFactory=new ThreadFactory() {
				final AtomicInteger threadNumber = new AtomicInteger(1);
				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r,userId+ ": "+threadNumber.getAndIncrement());
				}
			};
			threadList=new ArrayList<Future>();
			photoExecutorService=Executors.newCachedThreadPool();
			
			Flag.messageBoxText="CRAWLING PHOTOS";
			Flag.totalPhoto4User=photoIDList.size();
			
			
			for(String photoId:photoIDList){
				photoExecuter=new PhotoExecuter(photoId, this.userId);

				if(Flag.getApiPerm()){ 
					future=photoExecutorService.submit(photoExecuter); 
					threadList.add(future);
				}

				
				while(Flag.getApiPerm() && threadList.size()>Flag.maxPhotoWorker){	// Limit thread counts
					Sleep.sleep(200);
					for(int i=threadList.size()-1;i>=0;i--){
						if(threadList.get(i).isDone()){
							threadList.remove(i);
						}
					}
				}
				
				Flag.totalPhotoSearched++;
				Flag.searchedPhoto4User++;
			}
			photoExecutorService.shutdown();	//Shutdown photoWorker executor and wait until all submitted tasks finish

			try {
				if(!photoExecutorService.awaitTermination(10, TimeUnit.SECONDS)){
					for(int i=threadList.size()-1;i>=0;i--){	//There are threads not terminated
						if(threadList.get(i).cancel(true)){
							System.out.println(i+". Thread is Cancelled!");
						}
					}
				}
			} catch (InterruptedException e) {
			}
			
			
		}




	}


}
