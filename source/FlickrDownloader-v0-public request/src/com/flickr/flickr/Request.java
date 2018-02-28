package com.flickr.flickr;


public class Request {
	public static String apiKey = "YOUR_API_KEY";

	public static String getPlaceIDRequest(String placeName){
		return "https://api.flickr.com/services/rest/?method=flickr.places.find"+
				"&api_key="+apiKey+
				"&query="+placeName+
				"&format=rest";
	}
	
	public static String getRequestLink(String tagId, String flickrId, int page){
		if(flickrId.equals("owner"))
			return "https://api.flickr.com/services/rest/?method=flickr.photos.search"+
			"&api_key="+apiKey+
			"&place_id="+tagId+
			"&per_page=500"+
			"&page="+page+
			"&format=rest";
		
		return "https://api.flickr.com/services/rest/?method=flickr.photos.search"+
		"&api_key="+apiKey+
		"&user_id="+tagId+
		"&per_page=500"+
		"&page="+page+
		"&format=rest";
	}
	
	
	
	public static String getExifRequest(String photoId){
		return "https://api.flickr.com/services/rest/?method=flickr.photos.getExif"+
				"&api_key="+apiKey+
				"&photo_id="+photoId+
				"&format=rest";
		
	}
	
	public static String getSizeRequest(String photoId){
		return "https://api.flickr.com/services/rest/?method=flickr.photos.getSizes"+
				"&api_key="+apiKey+
				"&photo_id="+photoId+
				"&format=rest";
		
	}
}
