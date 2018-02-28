package com.flickr.flickr;


public class FlickrLink {
	public static String apiKey = "YOUR_API_KEY";

	public static String getBBoxRequestLink(BBox bbox,int perPage, int page){
		return "https://api.flickr.com/services/rest/?method=flickr.photos.search"+
				"&api_key="+apiKey+
				"&min_upload_date=1990-01-01"+
				"&bbox="+bbox.toString()+
				"&accuracy=1"+
				"&extras=owner_name"+
				"&per_page="+perPage+
				"&page="+page+
				"&format=rest";
	}
}
