package com.flickr.function;

import java.util.concurrent.ThreadFactory;

public class Flag {
	public static int maxPhotoWorker=20;
	public static boolean sleepFlag=false;
	public static ThreadFactory threadFactory;
	
	public static int totalPhotoInserted,totalUserInserted,totalPlaceFinished,totalCityFinished;
	public static String lastPhotoInserted,lastUserInserted,lastPlaceFinished,lastCityFinished;
	public static int totalPhotoSearched,totalUserSearched;
	
	public static int totalCity4Country,totalPlace4City,totalUser4Place,totalPhoto4User;
	public static int searchedPlace4City,searchedUser4Place,searchedPhoto4User;
	public static String cityCrawlingNow,placeCrawlingNow,userCrawlingNow;
	
	public static String messageBoxText,sleepBoxText;
	
	public static boolean getApiPerm(){
		while(Flag.sleepFlag){
			Sleep.sleep(200);
		}
		return true;
	}
}
