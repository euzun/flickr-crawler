package com.flickr.search;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.flickr.db.WorldCityDB;
import com.flickr.flickr.Place;
import com.flickr.function.Flag;
import com.flickr.function.HTTPRequest;
import com.flickr.function.Sleep;


public class CityWorker{
	ExecutorService executorService=Executors.newCachedThreadPool();
	@SuppressWarnings("rawtypes")
	ArrayList<Future> threadList=new ArrayList<Future>();
	ArrayList<Place> placeArrayList=new ArrayList<Place>();
	Future<?> f;
	Place finPlace;
	PlaceIDExecuter idExecuter;
	private String cityName,countryCode;
	private ArrayList<Place> placeList=new ArrayList<Place>(); 
	WorldCityDB cityDB=new WorldCityDB();
	public CityWorker(String cityName, String countryCode){
		this.cityName=cityName;
		this.countryCode=countryCode;
	}

	public void getPlaceIDList4City(){

		Flag.searchedPlace4City=0;
		if(Flag.getApiPerm()){  placeList=(new HTTPRequest()).getPlaceList(this.cityName); }
		Flag.totalPlace4City=placeList.size();

		Flag.threadFactory=new ThreadFactory() {
			final AtomicInteger threadNumber = new AtomicInteger(1);
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r,cityName+ ": "+threadNumber.getAndIncrement());
			}
		};
		
		for(Place place:placeList){
			Flag.placeCrawlingNow=place.getPlaceId();

			if(!cityDB.IsInsertedPlace(place.getPlaceId())){
				place.setCountryCode(this.countryCode);
				idExecuter=new PlaceIDExecuter(place.getPlaceId());
				if(Flag.getApiPerm()){ 
					f=executorService.submit(idExecuter);
					threadList.add(f);
					placeArrayList.add(place);
				}
			}

			while(Flag.getApiPerm() && threadList.size()>Flag.maxPhotoWorker){	// Limit thread counts
				Sleep.sleep(200);
				for(int i=threadList.size()-1;i>=0;i--){
					if(threadList.get(i).isDone()){
						threadList.remove(i);
						finPlace=placeArrayList.remove(i);
						if(!cityDB.IsInsertedPlace(finPlace.getPlaceId())){
							cityDB.insertPlace(finPlace);
						}
						Flag.searchedPlace4City++;
						Flag.totalPlaceFinished++;
						Flag.lastPlaceFinished=finPlace.getPlaceId();
					}
				}
			}
		}
		
		executorService.shutdown();
		
		while(!executorService.isTerminated()){
			Sleep.sleep(500);
		}

	}
}