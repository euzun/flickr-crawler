package com.flickr.search;

import java.util.ArrayList;

import com.flickr.function.Flag;
import com.flickr.function.HTTPRequest;

public class CityWorker{

	PlaceIDWorker placeIDWorker;
	private String cityName,countryCode;
	private ArrayList<String> placeIDList=new ArrayList<String>(); 

	public CityWorker(String cityName, String countryCode){
		this.cityName=cityName;
		this.countryCode=countryCode;
	}

	public void getPlaceIDList4City(){
		
		if(Flag.getApiPerm()){  placeIDList=(new HTTPRequest()).getPlaceIDList(this.cityName); }

		if(placeIDList!=null){
			for(String placeID:placeIDList){
				placeIDWorker=new PlaceIDWorker(placeID,countryCode);
				if(Flag.getApiPerm()){ placeIDWorker.getUserList4PlaceID(); }
			}
		}

	}
}
