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
import com.flickr.flickr.User;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HttpRequest{
	private HttpURLConnection urlConnection = null;
	private URL url;
	private BufferedReader bufferedReader;
    private InputStreamReader streamReader;
    
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
		
	
	public int getEntryPageCount(BBox bbox){
		if(Flag.getApiPerm()){ 
			bufferedReader=getHttpResponse(FlickrLink.getBBoxRequestLink(bbox,1,1));
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
			if(Flag.getApiPerm()){ bufferedReader=getHttpResponse(FlickrLink.getBBoxRequestLink(bbox,500,1));}
			while ((nextLine = bufferedReader.readLine()) != null) {
				response=getTagValue(nextLine, "owner");
				if (response.length()>0) {
					user=new User(response, getTagValue(nextLine, "ownername"), bbox.getBboxId());
					userList.add(user);
					
					user=null;// Object release
					
					synchronized (this) {
						Flag.searchedEntry4BBox++;
						Flag.totalEntrySearched++;
					}
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
		} catch (IOException e) {
		}
		return userList;
	}
	
}
