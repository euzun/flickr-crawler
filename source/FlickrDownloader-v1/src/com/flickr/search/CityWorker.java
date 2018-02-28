package com.flickr.search;

import java.util.ArrayList;

import com.flickr.db.WorldCityDB;
import com.flickr.flickr.Place;
import com.flickr.function.Flag;
import com.flickr.function.HTTPRequest;


public class CityWorker{

	PlaceIDWorker placeIDWorker;
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

		for(Place place:placeList){
			Flag.placeCrawlingNow=place.getPlaceId();

			if(!cityDB.IsInsertedPlace(place.getPlaceId())){
				place.setCountryCode(this.countryCode);
				placeIDWorker=new PlaceIDWorker(place.getPlaceId());
				if(Flag.getApiPerm()){ 
					placeIDWorker.getUserList4PlaceID(); 	
				}
				cityDB.insertPlace(place);
			}

			Flag.lastPlaceFinished=place.getPlaceId();
			Flag.totalPlaceFinished++;
			Flag.searchedPlace4City++;
		}

	}
}