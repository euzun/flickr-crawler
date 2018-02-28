package com.flickr.function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.flickr.flickr.Place;
import com.flickr.flickr.Request;
import com.flickr.search.PageExecuter;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HTTPRequest{
	HttpURLConnection urlConnection = null;
	private URL url;
	private BufferedReader bufferedReader;
	SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
  
    String flickrId="owner";
    
	private void connectUrl(URL url) throws IOException{
		Flag.getApiPerm();
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
	
	public BufferedReader getHttpResponse(String request){
		try {
			return new BufferedReader(new InputStreamReader(getURLConnection(request).getInputStream()));
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
		
	public String getTagValue(String nextLine,String tag){
		if (nextLine.indexOf(tag+"=") >= 0) {
			return nextLine.split(tag+"=\"")[1].split("\"")[0].trim();
		}else{
			return "";
		}
	}
		
	public ArrayList<Place> getPlaceList(String placeName){
		ArrayList<Place> placeList=new ArrayList<Place>();
		Place place;
		String nextLine,response;
		try {
			bufferedReader=getHttpResponse(Request.getPlaceIDRequest(placeName));
			
			while ((nextLine = bufferedReader.readLine()) != null) {
				response=getTagValue(nextLine, "place_id");
				if (response.length()>0) {
					place=new Place(response,getTagValue(nextLine, "longitude"),getTagValue(nextLine, "latitude"));
					placeList.add(place);
				}
			}
			bufferedReader.close();
		} catch (IOException e) {
		}
		return placeList;
	}
	
	@SuppressWarnings("rawtypes")
	public void getUserIDList(String placeID){
		try {
			int pages=0,counter = 0;
			String nextLine,response;
			ExecutorService exService=Executors.newCachedThreadPool();
			ArrayList<Future> threadList=new ArrayList<Future>();
			if(Flag.getApiPerm()){ bufferedReader=getHttpResponse(Request.getRequestLink(placeID,flickrId, 1));}
			while ((nextLine = bufferedReader.readLine()) != null) {
				response=getTagValue(nextLine, "pages");
				if (response.length()>0) {
					// AFTER GETTING ALL PLACES, GET PLACES AFTER 3000s PAGE
//					pages=Math.min(Integer.parseInt(response),3000);
					pages=Integer.parseInt(response);
					break;
				}
			}
			bufferedReader.close();
			
			for(int i=1;i<=pages;i++){
				if(Flag.getApiPerm()){ threadList.add(exService.submit(new PageExecuter(placeID, i))); }
				
				while(Flag.getApiPerm() && threadList.size()>2*Flag.maxPhotoWorker){
					Sleep.sleep(200);
					for(int j=threadList.size()-1;j>=0;j--){
						if(threadList.get(j).isDone()){
							threadList.remove(j);
							counter++;
							Flag.messageBoxText=placeID+ " (Page "+counter+"/"+pages+")";
						}
					}
				}
			}
			
			exService.shutdown();
			while(!exService.isTerminated()){
				Sleep.sleep(200);
			}
			
		} catch (IOException e) {
		}
	}

	
}
