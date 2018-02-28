package com.flickr.function;

public class Flag {
	public static int photoCounter=0;
	public static int maxPhotoWorker=20;
	
	public static boolean apiKeyFlag=false;
	
	
	public static int totalUser=0;
	public static int totalPhoto=0;
	public static int totalCity=0;
	public static String lastInsertUser,lastInsertPhoto,lastFinUser,lastFinCity;
	
	public static boolean getApiPerm(){
		while(Flag.apiKeyFlag){
			System.out.println("INVALID API");
			Sleep.sleep(200);
		}
		return true;
	}
}
