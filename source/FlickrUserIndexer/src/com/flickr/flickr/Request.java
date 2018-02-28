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
			"&content_type=1"+
			"&place_id="+tagId+
			"&media=photos&extras=original_format%2C+o_dims%2C+url_o"+
			"&per_page=500"+
			"&page="+page+
			"&format=rest";
		
		return "https://api.flickr.com/services/rest/?method=flickr.photos.search"+
		"&api_key="+apiKey+
		"&content_type=1"+
		"&user_id="+tagId+
		"&media=photos&extras=original_format%2C+o_dims%2C+url_o"+
		"&per_page=500"+
		"&page="+page+
		"&format=rest";
	}
}
