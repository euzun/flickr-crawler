package com.flickr.function;

import java.text.SimpleDateFormat;


public class Flag {
	public static int maxWorker=20;
	public static boolean sleepFlag=false,pauseFlag=false;
	
	public static int totalEntrySearched,totalUserCrawled;
	public static String lastUserCrawled,bboxCrawlingNow;
	
	public static int totalBBox4Area,totalEntry4BBox;
	public static int searchedBBox4Area,searchedEntry4BBox;
	
	public static String systemMessageText, bboxCenter;
	public static SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
	
	
	public static int totalPhotoSearched,totalPhotoCrawled,totalPhoto4User,searchedPhoto4User,totalUser;
	public static String userCrawlingNow,lastPhotoCrawled,userDownloadingNow;
	
	public static int totalUserDownloaded,downloadedPhoto4User;
	
	public static String downloadDir;
	
	public static boolean getApiPerm(){
		while(sleepFlag || pauseFlag){
			Sleep.sleep(200);
		}
		return true;
	}
	
	public static void getPausePerm(){
		while(pauseFlag){
			Sleep.sleep(200);
		}
	}
}
