package com.flickr.flickr;


public class FlickrLink {
	public static String apiKey = "YOUR_API_KEY";

	public String getBBoxRequest(BBox bbox,int perPage, int page){
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
	
	public String getPhotoRequest(String userId,int perPage, int page){
		return "https://api.flickr.com/services/rest/?method=flickr.photos.search"+
				"&api_key="+apiKey+
				"&user_id="+userId+
				"&extras=date_upload,url_o"+
				"&per_page="+perPage+
				"&page="+page+
				"&format=rest";
		
	}
	
	public String getExifRequest(String photoId){
		return "https://api.flickr.com/services/rest/?method=flickr.photos.getExif"+
				"&api_key="+apiKey+
				"&photo_id="+photoId+
				"&format=rest";
		
	}
}
