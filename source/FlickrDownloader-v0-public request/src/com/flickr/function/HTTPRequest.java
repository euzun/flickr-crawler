package com.flickr.function;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.flickr.db.Photo;
import com.flickr.flickr.Request;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HTTPRequest {
	private DataInputStream dis;
	private String nextLine;
	private String response;
	private String oldApi;
	private int exifCounter=0;
	private int flickrStatCode;
	private int placeTry=0,userTry=0,photoTry=0, sizeTry=0, exifTry=0;
	
	public BufferedReader getInputBuffer(String urlStr){
		try {
			URL url = new URL(urlStr);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setDoOutput(true); 
		    urlConnection.setDoInput(true);
		    urlConnection.connect();
		    
		    InputStream responseBodyIS = urlConnection.getInputStream();
		    return new BufferedReader(new InputStreamReader(responseBodyIS));
			
		} catch (IOException e) {
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	private DataInputStream getHttpResponse(String request) throws IOException{
		dis= new DataInputStream((new URL(request).openConnection()).getInputStream());
		
		while ((nextLine = dis.readLine()) != null) {
			response=getTagValue(nextLine, "stat");
			
			if(response.equals("ok")){
				return dis;
			}else if(response.equals("fail")){
				nextLine=dis.readLine();
				flickrStatCode=Integer.parseInt(getTagValue(nextLine, "code"));
				
				if(flickrStatCode==100){
					// Invalid API KEY
					oldApi=Request.apiKey;
					
					synchronized (HTTPRequest.class) {
						if(Flag.getApiPerm()){ updateApiKey(); }
						dis.close();
						return getHttpResponse(request.replace(oldApi, Request.apiKey));
					}
					
				}
			}
		}
		return dis;
	}
	
	public synchronized void updateApiKey(){
		try{
			if(isInvalidApiKey()){
				Flag.apiKeyFlag=true;
				WebClient webClient=new WebClient();
				HtmlPage page = webClient.getPage("https://www.flickr.com/services/api/explore/flickr.places.find");
				String res = ((HtmlPage) page.getForms().get(1).getInputByValue("Call Method...").click()).getPage().asXml();
				Request.apiKey=res.substring(res.indexOf("api_key=")+8,res.indexOf("format=rest")-5);
				webClient.closeAllWindows();
				Flag.apiKeyFlag=false;
			}
		} catch(Exception e){
			System.out.println("API KEY Update Error");
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public synchronized boolean isInvalidApiKey() throws IOException{
		dis= new DataInputStream((new URL(Request.getPlaceIDRequest("yozgat")).openConnection()).getInputStream());
		while ((nextLine = dis.readLine()) != null) {
			response=getTagValue(nextLine, "stat");
			if(response.equals("fail")){
				return true;
			}else if(response.equals("ok")){
				return false;
			}
		}
		return false;
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
	
	@SuppressWarnings("deprecation")
	private Set<String> getList4ID(String tagId, String flickrId) throws MalformedURLException, IOException{
		Set<String> idList=new HashSet<String>();
		int page=1;
		int pages=0;
		
		do{
			dis=getHttpResponse(Request.getRequestLink(tagId,flickrId, page));
			while ((nextLine = dis.readLine()) != null) {
				if(pages==0){
					response=getTagValue(nextLine, "pages");
					if (response.length()>0) {
						pages=Integer.parseInt(response);
					}
				}
				
				response=getTagValue(nextLine, flickrId);
				if (response.length()>0) {
					idList.add(response);
				}
			}
			page++;
		}while(pages>=page);
		
		return idList;
	}
		
	@SuppressWarnings("deprecation")
	public ArrayList<String> getPlaceIDList(String placeName){
		ArrayList<String> placeIDList=new ArrayList<String>();
		try {
			dis=getHttpResponse(Request.getPlaceIDRequest(placeName));
			
			while ((nextLine = dis.readLine()) != null) {
				response=getTagValue(nextLine, "place_id");
				if (response.length()>0) {
					placeIDList.add(response);
				}
			}
		} catch (IOException e) {
			System.out.println("ERROR PLACE LIST "+ placeTry+" : "+e.getMessage());
			if(++placeTry>4){
				return placeIDList;
			}
			Sleep.sleep(1000);
			return getPlaceIDList(placeName);
			
		}
		return placeIDList;
	}
	
	public Set<String> getUserIDList(String placeID){
		try {
			return getList4ID(placeID, "owner");
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
			return getList4ID(userId, "id");
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
			dis = getHttpResponse(Request.getSizeRequest(photo.getPhotoId()));
			while ((nextLine = dis.readLine()) != null) {
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
		} catch (IOException e) {
			System.out.println("ERROR SIZE "+ sizeTry+" : "+e.getMessage());
			System.out.println(Thread.currentThread().getName());
			Thread.currentThread().destroy();
			if(sizeTry++>4){
				return photo;
			}
			Sleep.sleep(1000);
			return getSizeRequest(photo);
		}
		return photo;
	}
	
	@SuppressWarnings("deprecation")
	public Photo getExifRequest(Photo photo){
		this.exifCounter=0;
		try{
			dis = getHttpResponse(Request.getExifRequest(photo.getPhotoId()));
			while ((nextLine = dis.readLine()) != null) {
				response = getTagValue(nextLine, "label");
				
				if (response.equalsIgnoreCase("Make")) {
					photo.setFlickrMake(getRaw(dis.readLine()));
					exifCounter++;
				} else if (response.equalsIgnoreCase("Model")) {
					photo.setFlickrModel(getRaw(dis.readLine()));
					exifCounter++;
				} else if (response.equalsIgnoreCase("Software")) {
					photo.setFlickrSoftware(getRaw(dis.readLine()).replaceAll("'", ""));
					exifCounter++;
				} else if (response.equalsIgnoreCase("Date and Time (Modified)")) {
					photo.setModifiedDate(getRaw(dis.readLine()));
					exifCounter++;
				} else if (response.equalsIgnoreCase("Date and Time (Original)")) {
					photo.setOriginalDate(getRaw(dis.readLine()));
					exifCounter++;
				} else if (response.equalsIgnoreCase("Exposure")) {
					photo.setExposureTime(getRaw(dis.readLine()));
					exifCounter++;
				} else if (response.equalsIgnoreCase("Exposure Mode")) {
					photo.setExposureMode(getRaw(dis.readLine()));
					exifCounter++;
				} else if (response.equalsIgnoreCase("Exposure Bias")) {
					photo.setExposureBias(Double.parseDouble(getRaw(dis.readLine())));
					exifCounter++;
				} else if (response.equalsIgnoreCase("ISO Speed")) {
					photo.setIsoSpeed(Integer.parseInt(getRaw(dis.readLine())));
					exifCounter++;
				}

				if (this.exifCounter == 9) {
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("ERROR EXIF "+ exifTry+" : "+e.getMessage());
			if(exifTry++>4){
				return photo;
			}
			Sleep.sleep(1000);
			return getExifRequest(photo);
		}

		return photo;
	}
}
