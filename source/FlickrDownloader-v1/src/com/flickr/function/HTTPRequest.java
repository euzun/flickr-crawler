package com.flickr.function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.flickr.flickr.Photo;
import com.flickr.flickr.Place;
import com.flickr.flickr.Request;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HTTPRequest {
	HttpURLConnection urlConnection = null;
	private URL url;
	private BufferedReader bufferedReader;
	private String nextLine;
	private String response;
	private int exifCounter=0;
	private int placeTry=0,userTry=0,photoTry=0, sizeTry=0, exifTry=0;
	SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
   
	private void connectUrl(URL url) throws IOException{
		urlConnection = (HttpURLConnection) url.openConnection();
	    urlConnection.setDoInput(true);
	    urlConnection.setConnectTimeout(15000);
	    urlConnection.setReadTimeout(30000);
	    urlConnection.connect();
	}
	
	public HttpURLConnection getURLConnection(String urlStr){
		try {
			url=new URL(urlStr);
			connectUrl(url);

		    if(urlConnection.getResponseCode() == 429 || urlConnection.getResponseCode() == 403){
		    	Flag.sleepBoxText="TOO MANY REQUEST: "+urlConnection.getResponseCode()  + " @ "+ft.format(new Date());
		    	waitConnection();
		    }
		    if(urlConnection.getContentLength()==123 || urlConnection.getContentLength()==125){ // Invalid API KEY content length is 123
				String oldApi=Request.apiKey; // backup old api key
				
				synchronized (HTTPRequest.class) { // update key just one time by first thread gets class object monitor
					
					if(oldApi.equalsIgnoreCase(Request.apiKey)){ // check if first thread updated the key
						updateApiKey(); // update the api key 
					}
					
					if(oldApi.equals(Request.apiKey))// check if api key is same, that is valid content with 123 length come by coincidence
						return urlConnection;// then return first urlconnection
					
					urlConnection.disconnect();
					return getURLConnection(urlStr.replace(oldApi, Request.apiKey));// else, api key is updated recall request by changing api key
				}
		    }
		    
		} catch (IOException e) {
			if(e.getMessage().equals("connect timed out") || e.getMessage().equals("Read timed out")){
				Flag.sleepBoxText=e.getMessage().toUpperCase()  + " @ "+ft.format(new Date());
				waitConnection();
			}
		}
		
		return urlConnection;
	}
	
	private BufferedReader getHttpResponse(String request){
		try {
			bufferedReader=new BufferedReader(new InputStreamReader(getURLConnection(request).getInputStream()));
			return bufferedReader;
		} catch (IOException e) {
			return null;
		}
		
	}
	
	
	
	private synchronized void waitConnection(){
		synchronized (HTTPRequest.class) {
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
	
	public synchronized void updateApiKey(){
		try{
				Flag.sleepFlag=true;
				Flag.sleepBoxText="API KEY UPDATE" + " @ "+ft.format(new Date());
				
				WebClient webClient=new WebClient();
				HtmlPage page = webClient.getPage("https://www.flickr.com/services/api/explore/flickr.places.find");
				String res = ((HtmlPage) page.getForms().get(1).getInputByValue("Call Method...").click()).getPage().asXml();
				Request.apiKey=res.substring(res.indexOf("api_key=")+8,res.indexOf("format=rest")-5);
				webClient.closeAllWindows();
				
				Flag.sleepFlag=false;
		} catch(Exception e){
			System.out.println("API KEY Update Error");
		}
		
	}
		
	private String getTagValue(String nextLine,String tag){
		if (nextLine.indexOf(tag+"=") >= 0) {
			return nextLine.split(tag+"=\"")[1].split("\"")[0].trim();
		}else{
			return "";
		}
	}
	
	private String getRaw(String nextLine){
		return nextLine.trim().replaceAll("<raw>", "").replaceAll("</raw>","").trim();
	}
	
	private Set<String> getList4ID(String tagId, String flickrId, String messageBody) throws MalformedURLException, IOException{
		Set<String> idList=new HashSet<String>();
		int page=1;
		int pages=0;
		int total=0;
		
		do{
			bufferedReader=getHttpResponse(Request.getRequestLink(tagId,flickrId, page));
			while ((nextLine = bufferedReader.readLine()) != null) {
				if(pages==0){
					response=getTagValue(nextLine, "pages");
					if (response.length()>0) {
						pages=Integer.parseInt(response);
					}
					
					response=getTagValue(nextLine, "total");
					if (response.length()>0) {
						total=Math.min(Integer.parseInt(response),100000);
					}
				}
				
				response=getTagValue(nextLine, flickrId);
				if (response.length()>0) {
					idList.add(response);
					Flag.messageBoxText=messageBody+" ("+idList.size()+"/"+total+")";
				}
				
//				Take max 100000 images
				if(idList.size()>100000){
					return idList;
				}
			}
			page++;
		}while(pages>=page);
		bufferedReader.close();
		
		return idList;
	}
		
	public ArrayList<Place> getPlaceList(String placeName){
		ArrayList<Place> placeList=new ArrayList<Place>();
		Place place;
		try {
			bufferedReader=getHttpResponse(Request.getPlaceIDRequest(placeName));
			
			while ((nextLine = bufferedReader.readLine()) != null) {
				response=getTagValue(nextLine, "place_id");
				if (response.length()>0) {
					place=new Place(response,getTagValue(nextLine, "longitude"),getTagValue(nextLine, "latitude"));
					placeList.add(place);
					
					Flag.messageBoxText="Getting PLACE List.. "+placeList.size();
				}
			}
			bufferedReader.close();
		} catch (IOException e) {
			
			System.out.println("ERROR PLACE LIST "+ placeTry+" : "+e.getMessage());
			if(++placeTry>4){
				return placeList;
			}
			Sleep.sleep(1000);
			return getPlaceList(placeName);
			
		}
		return placeList;
	}
	
	public Set<String> getUserIDList(String placeID){
		try {
			return getList4ID(placeID, "owner","Getting USER List..");
		} catch (IOException e) {
			System.out.println("ERROR USER LIST "+ userTry+" : "+e.getMessage());
			if(++userTry>4){
				return null;
			}
			Sleep.sleep(1000);
			return getUserIDList(placeID);
		}
	}
	
	public Set<String> getPhotoIDList(String userId){
		try {
			return getList4ID(userId, "id","Getting PHOTO List..");
		} catch (IOException e) {
			System.out.println("ERROR PHOTO LIST "+ photoTry+" : "+e.getMessage());
			if(++photoTry>4){
				return null;
			}
			Sleep.sleep(1000);
			return getPhotoIDList(userId);
		}
	}
	
	@SuppressWarnings("deprecation")
	public Photo getSizeRequest(Photo photo){
		try {
			bufferedReader = getHttpResponse(Request.getSizeRequest(photo.getPhotoId()));
			while ((nextLine = bufferedReader.readLine()) != null) {
				response = getTagValue(nextLine, "label");
				if (response.equalsIgnoreCase("Original")) {
					
					response = getTagValue(nextLine, "width");
					photo.setImgWidth(Integer.parseInt(response));
					
					response = getTagValue(nextLine, "height");
					photo.setImgHeight(Integer.parseInt(response));
					
					response = getTagValue(nextLine, "source");
					photo.setImgUrl(response);
					break;
				}
			}
			bufferedReader.close();
		} catch (IOException e) {
			System.out.println("ERROR SIZE "+ sizeTry+" : "+e.getMessage());
			System.out.println(Thread.currentThread().getName());
			Thread.currentThread().destroy();
			if(++sizeTry>4){
				return photo;
			}
			Sleep.sleep(1000);
			return getSizeRequest(photo);
		}
		return photo;
	}
	
	public Photo getExifRequest(Photo photo){
		this.exifCounter=0;
		try{
			bufferedReader = getHttpResponse(Request.getExifRequest(photo.getPhotoId()));
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
				} else if (response.equalsIgnoreCase("Exposure Mode")) {
					photo.setExposureMode(getRaw(bufferedReader.readLine()));
					exifCounter++;
				} else if (response.equalsIgnoreCase("Exposure Bias")) {
					photo.setExposureBias(Double.parseDouble(getRaw(bufferedReader.readLine())));
					exifCounter++;
				} else if (response.equalsIgnoreCase("ISO Speed")) {
					photo.setIsoSpeed(Integer.parseInt(getRaw(bufferedReader.readLine())));
					exifCounter++;
				}

				if (this.exifCounter == 9) {
					break;
				}
			}
			bufferedReader.close();
		} catch (IOException e) {
			System.out.println("ERROR EXIF "+ exifTry+" : "+e.getMessage());
			if(++exifTry>4){
				return photo;
			}
			Sleep.sleep(1000);
			return getExifRequest(photo);
		}

		return photo;
	}
}
