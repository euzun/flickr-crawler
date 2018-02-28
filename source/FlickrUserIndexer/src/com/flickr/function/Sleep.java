package com.flickr.function;

public class Sleep {
	public static void sleep(int milisec){
		try {
			Thread.sleep(milisec);
		} catch (InterruptedException e) {
			System.out.println("Sleep Error in Thread: "+Thread.currentThread().getId());
		}
	}
	
}
