package com.flickr.search;

import java.util.Set;

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
		
		for(String cityName:cityList){
			cityWorker=new CityWorker(cityName, countryCode);

			if(Flag.getApiPerm()){ cityWorker.getPlaceIDList4City(); }
			
			if(worldCityDB.updateCityDone(cityName)){
				Flag.totalCity++;
				Flag.lastFinCity=cityName;
			}
		}
		
	}

	@Override
	public void run() {
		getPlaceList4Country();
	}

}
