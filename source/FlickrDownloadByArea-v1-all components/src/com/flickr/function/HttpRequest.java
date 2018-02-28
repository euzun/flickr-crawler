package com.flickr.function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.flickr.db.BBoxDB;
import com.flickr.flickr.BBox;
import com.flickr.flickr.FlickrLink;
import com.flickr.flickr.Photo;
import com.flickr.flickr.User;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HttpRequest{
	private HttpURLConnection urlConnection = null;
	private URL url;
	private BufferedReader bufferedReader;
    private InputStreamReader streamReader;
    private FlickrLink flickrLink=new FlickrLink();
    
	private void connectUrl(URL url) throws IOException{
		urlConnection = (HttpURLConnection) url.openConnection();
	    urlConnection.setDoInput(true);
	    urlConnection.setConnectTimeout(15000);
	    urlConnection.setReadTimeout(30000);
	    urlConnection.connect();
	}
	
	private HttpURLConnection getURLConnection(String urlStr){
		try {
			url=new URL(urlStr);
			connectUrl(url);
			
			
		    if(urlConnection.getResponseCode() == 429 || urlConnection.getResponseCode() == 403){
		    	Flag.systemMessageText="TOO MANY REQUEST: "+urlConnection.getResponseCode()  + " @ "+Flag.ft.format(new Date());
		    	waitConnection();
		    }

		} catch (IOException e) {
			if(e.getMessage().equals("connect timed out") || e.getMessage().equals("Read timed out")){
				Flag.systemMessageText=e.getMessage().toUpperCase()  + " @ "+Flag.ft.format(new Date());
				waitConnection();
			}
		}
		
		if(urlConnection.getContentLength()==123 || urlConnection.getContentLength()==125){ // Invalid API KEY content length is 123
			String oldApi=FlickrLink.apiKey; // backup old api key
			
			synchronized (HttpRequest.class) { // update key just one time by first thread gets class object monitor
				
				if(oldApi.equalsIgnoreCase(FlickrLink.apiKey)){ // check if first thread updated the key
					updateApiKey(); // update the api key 
				}
				
				if(oldApi.equals(FlickrLink.apiKey))// check if api key is same, that is valid content with 123 length come by coincidence
					return urlConnection;// then return first urlconnection
				
				return getURLConnection(urlStr.replace(oldApi, FlickrLink.apiKey));// else, api key is updated recall request by changing api key
			}
	    }
		
		return urlConnection;
	}
	
	private synchronized void waitConnection(){
		synchronized (HttpRequest.class) {
			try{
				urlConnection.disconnect();
	    		connectUrl(url);
	    		
	    		while(urlConnection.getResponseCode() == 429 || urlConnection.getResponseCode() == 403){
	    			Flag.sleepFlag=true;
	    			urlConnection.disconnect();
	    			Sleep.sleep(60000);
	    			connectUrl(url);
	    		}
			}catch(Exception e){
				Flag.sleepFlag=true;
				Sleep.sleep(60000);
				waitConnection();
			}
			Flag.sleepFlag=false;
		} 
	}
	
	private synchronized void updateApiKey(){
		try{
				Flag.sleepFlag=true;
				Flag.systemMessageText="API KEY UPDATE" + " @ "+Flag.ft.format(new Date());
				
				WebClient webClient=new WebClient();
				HtmlPage page = webClient.getPage("https://www.flickr.com/services/api/explore/flickr.test.null");
				String res = ((HtmlPage) page.getForms().get(1).getInputByValue("Call Method...").click()).getPage().asXml();
				FlickrLink.apiKey=res.substring(res.indexOf("api_key=")+8,res.indexOf("format=rest")-5);
				
				// Object release
				webClient.closeAllWindows();
				webClient=null;
				page=null;
				res=null;
				
				Flag.sleepFlag=false;
		} catch(Exception e){
			System.out.println("API KEY Update Error");
		}
		
	}
	
	
	private BufferedReader getHttpResponse(String request){
		try {
			urlConnection=getURLConnection(request);
			if(urlConnection.getContentLength()==117){
				
				// Object release
				url=null;
				urlConnection.disconnect();
				urlConnection=null;
				
				return null;
			}
			
			streamReader=new InputStreamReader(urlConnection.getInputStream());
			bufferedReader=new BufferedReader(streamReader);
			
			return bufferedReader;
		} catch (IOException e) {
			return null;
		}
		
	}
	
	private String getTagValue(String nextLine,String tag){
		if (nextLine.indexOf(tag+"=") >= 0) {
			return nextLine.split(tag+"=\"")[1].split("\"")[0].trim();
		}else{
			return "";
		}
	}
		
	
	public int getPageCount4BBox(BBox bbox){
		if(Flag.getApiPerm()){ 
			bufferedReader=getHttpResponse(flickrLink.getBBoxRequest(bbox,1,1));
		}
		
		String nextLine,response;
		if(bufferedReader!=null){
			try {
				while ((nextLine = bufferedReader.readLine()) != null) {
					response=getTagValue(nextLine, "total");
					if (response.length()>0) {
						Flag.totalEntry4BBox=Integer.parseInt(response);
						
						// Object release	
						nextLine=null;
						response=null;
						url=null;
						urlConnection.disconnect();
						urlConnection=null;
						streamReader.close();
						streamReader=null;
						bufferedReader.close();
						bufferedReader=null;
						
						return (int) Math.ceil(Flag.totalEntry4BBox/500.0);
					}
				}
				
				// Object release
				nextLine=null;
				response=null;
				url=null;
				urlConnection.disconnect();
				urlConnection=null;
				streamReader.close();
				streamReader=null;
				bufferedReader.close();	
				bufferedReader=null;
				
			} catch (Exception e) { } 
		}
		BBoxDB bboxDB=new BBoxDB();
		bboxDB.deleteBBox(bbox);
		
		// Object release	
		bboxDB=null;
		
		return 0;
	}
	
	public Set<User> getUserIDList(BBox bbox, int page){
		Set<User> userList=new HashSet<User>();
		User user;
		try {
			String nextLine,response;
			if(Flag.getApiPerm()){ bufferedReader=getHttpResponse(flickrLink.getBBoxRequest(bbox,500,page));}
			while ((nextLine = bufferedReader.readLine()) != null) {
				response=getTagValue(nextLine, "owner");
				if (response.length()>0) {
					user=new User(response, getTagValue(nextLine, "ownername").replaceAll("\\\\", "").replaceAll("'", "").replaceAll("\"", "").replaceAll("/", "").trim(), bbox.getBboxId());
					userList.add(user);
					
					user=null;// Object release
					
					Flag.searchedEntry4BBox++;
					Flag.totalEntrySearched++;
				}
			}
			
			// Object release
			flickrLink=null;
			nextLine=null;
			response=null;
			url=null;
			urlConnection.disconnect();
			urlConnection=null;
			streamReader.close();
			streamReader=null;
			bufferedReader.close();	
			bufferedReader=null;
		} catch (IOException e) {
		}
		return userList;
	}
	
	public int getPageCount4User(String userId){
		if(Flag.getApiPerm()){ 
			bufferedReader=getHttpResponse(flickrLink.getPhotoRequest(userId,1,1));
		}
		
		String nextLine,response;
		int totalEntry=0;
		
		if(bufferedReader!=null){
			try {
				while ((nextLine = bufferedReader.readLine()) != null) {
					response=getTagValue(nextLine, "total");
					if (response.length()>0) {
						totalEntry=Integer.parseInt(response);
						Flag.totalPhoto4User=totalEntry;
						
						// Object release	
						nextLine=null;
						response=null;
						url=null;
						urlConnection.disconnect();
						urlConnection=null;
						streamReader.close();
						streamReader=null;
						bufferedReader.close();
						bufferedReader=null;
						
						return (int) Math.ceil(totalEntry/500.0);
					}
				}
				
				// Object release
				nextLine=null;
				response=null;
				url=null;
				urlConnection.disconnect();
				urlConnection=null;
				streamReader.close();
				streamReader=null;
				bufferedReader.close();	
				bufferedReader=null;
				
			} catch (Exception e) { } 
		}
		
		return 0;
	}
	
	public Set<Photo> getPhotoList4User(String userId,int page){
		if(Flag.getApiPerm()){ 
			bufferedReader=getHttpResponse(flickrLink.getPhotoRequest(userId,500,page));
		}
		String nextLine,response;
		Set<Photo> photoList=new HashSet<Photo>();
		Photo photo;
		if(bufferedReader!=null){
			try {
				while ((nextLine = bufferedReader.readLine()) != null) {
					response=getTagValue(nextLine,"id");
					if(response.length()>0){
						photo=new Photo();
						photo.setPhotoId(response);
						photo.setUserId(userId);
						photo.setUploadDate(getTagValue(nextLine,"dateupload"));
						
						// Does not add photo if it does not have original img, or img dimensions lower than 1024
						// And checks the exif data, if required fields are obtained photo is crawled
						
						response=getTagValue(nextLine,"url_o");
						if(response.length()>0){
							photo.setImgUrl(response);
							photo.setImgHeight(Integer.parseInt(getTagValue(nextLine,"height_o")));
							photo.setImgWidth(Integer.parseInt(getTagValue(nextLine,"width_o")));
							
							if( (photo.getImgHeight()>=1024) && (photo.getImgWidth()>=1024) && (photo.getImgUrl().split("_o.")[1].split("\"")[0].trim().equalsIgnoreCase("jpg")) ){
								photoList.add(photo);
//								photo=getExifRequest(photo);
//								if(photo.isValid()){
//									photoList.add(photo);
//								}
							}
						}
					}
					Flag.searchedPhoto4User++;
					Flag.totalPhotoSearched++;
				}
				
				// Object release
				streamReader.close();
				bufferedReader.close();	
			} catch (Exception e) { } 
		}
		
		// Object release
		flickrLink=null;
		nextLine=null;
		response=null;
		url=null;
		urlConnection.disconnect();
		urlConnection=null;
		streamReader=null;
		bufferedReader=null;
		return photoList;
	}
	
	private String getRaw(String nextLine){
		return nextLine.trim().replaceAll("<raw>", "").replaceAll("</raw>","").trim();
	}
	
	public Photo getExifRequest(Photo photo){
		int exifCounter=0;
		String nextLine,response;
		BufferedReader bufferedReader;
		try{
//			System.out.println(flickrLink.getExifRequest(photo.getPhotoId()));
			bufferedReader = getHttpResponse(flickrLink.getExifRequest(photo.getPhotoId()));
			while ((nextLine = bufferedReader.readLine()) != null) {
				response = getTagValue(nextLine, "label");
				
				if (response.equalsIgnoreCase("Make")) {
					photo.setFlickrMake(getRaw(bufferedReader.readLine()));
					exifCounter++;
				} else if (response.equalsIgnoreCase("Model")) {
					photo.setFlickrModel(getRaw(bufferedReader.readLine()));
					exifCounter++;
				} else if (response.equalsIgnoreCase("Software")) {
					photo.setFlickrSoftware(getRaw(bufferedReader.readLine()).replaceAll("'", ""));
					exifCounter++;
				} else if (response.equalsIgnoreCase("Date and Time (Modified)")) {
					photo.setModifiedDate(getRaw(bufferedReader.readLine()));
					exifCounter++;
				} else if (response.equalsIgnoreCase("Date and Time (Original)")) {
					photo.setOriginalDate(getRaw(bufferedReader.readLine()));
					exifCounter++;
				} else if (response.equalsIgnoreCase("Exposure")) {
					photo.setExposureTime(getRaw(bufferedReader.readLine()));
					exifCounter++;
					double eT=Double.parseDouble(photo.getExposureTime().split("/")[0]) / Double.parseDouble(photo.getExposureTime().split("/")[1]);
					if(eT<0.01 || eT>0.1){
						photo.setValid(false);
					}
				} else if (response.equalsIgnoreCase("Exposure Mode")) {
					photo.setExposureMode(getRaw(bufferedReader.readLine()));
					exifCounter++;
				} else if (response.equalsIgnoreCase("Exposure Bias")) {
					photo.setExposureBias(getRaw(bufferedReader.readLine()));
					exifCounter++;
				} else if (response.equalsIgnoreCase("ISO Speed")) {
					photo.setIsoSpeed(getRaw(bufferedReader.readLine()));
					exifCounter++;
					
					if(Integer.parseInt(photo.getIsoSpeed())<100 || Integer.parseInt(photo.getIsoSpeed())>1000){
						photo.setValid(false);
					}
				}

				if (exifCounter == 9) {
					break;
				}
			}
		} catch (IOException e) {
			photo.setValid(false);
		}
		
		if ((photo.getExposureTime().equals("")) || (photo.getIsoSpeed().equals(""))){
			photo.setValid(false);
		}
		
		nextLine=null;
		response=null;
		bufferedReader=null;
		return photo;
	}
}
