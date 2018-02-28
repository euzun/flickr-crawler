package com.flickr.function;

import java.text.SimpleDateFormat;


public class Flag {
	public static int maxWorker=20;
	public static boolean sleepFlag=false;
	
	public static int totalEntrySearched,totalUserCrawled;
	public static String lastUserCrawled,bboxCrawlingNow;
	
	public static int totalBBox4Area,totalEntry4BBox;
	public static int searchedBBox4Area,searchedEntry4BBox;
	
	public static String systemMessageText, bboxCenter;
	public static SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
	
	public static boolean getApiPerm(){
		while(Flag.sleepFlag){
			Sleep.sleep(200);
		}
		return true;
	}
}
