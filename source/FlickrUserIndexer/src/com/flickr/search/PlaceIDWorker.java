package com.flickr.search;

import com.flickr.function.Flag;
import com.flickr.function.HTTPRequest;


public class PlaceIDWorker implements Runnable{
	private String placeID;

	public PlaceIDWorker(String placeID){
		this.placeID=placeID;
	}


	@Override
	public void run() {
		Flag.searchedUser4Place=0;
		if(Flag.getApiPerm()){ (new HTTPRequest()).getUserIDList(placeID); }
	}
}

