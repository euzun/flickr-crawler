package com.flickr.search;

import java.util.Set;

import com.flickr.db.UserDB;
import com.flickr.db.WorldCityDB;
import com.flickr.function.Flag;

public class MainSearchThread implements Runnable{
	private String countryCode;
	Set<String> cityList;
	CityWorker cityWorker;
	WorldCityDB worldCityDB;
	
	
	public MainSearchThread(String countryCode){
		this.countryCode=countryCode;
	}
	public void getPlaceList4Country(){
		worldCityDB=new WorldCityDB();
		cityList=worldCityDB.getCitybyCountry(countryCode);
		
		Flag.totalCity4Country=cityList.size();
		for(String cityName:cityList){
			Flag.cityCrawlingNow=cityName;
			cityWorker=new CityWorker(cityName, countryCode);

			if(Flag.getApiPerm()){ cityWorker.getPlaceIDList4City(); }
			
			worldCityDB.updateCityDone(cityName);
			
			Flag.lastCityFinished=cityName;
			Flag.totalCityFinished++;
		}
		
	}

	@Override
	public void run() {
		(new UserDB()).resetUnFinishedUser();
		getPlaceList4Country();
	}

}
