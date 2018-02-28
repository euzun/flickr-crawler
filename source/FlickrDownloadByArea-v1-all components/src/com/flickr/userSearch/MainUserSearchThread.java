package com.flickr.userSearch;

import java.util.ArrayList;

import com.flickr.db.BBoxDB;
import com.flickr.flickr.BBox;
import com.flickr.function.BBoxDistance;
import com.flickr.function.Flag;

public class MainUserSearchThread implements Runnable{

	private BBox bbox;
	private BBoxDB bboxDB=new BBoxDB();
	private ArrayList<BBox> bbList;
	private BBoxWorker bBoxWorker;
	private boolean isDB;
	
	public MainUserSearchThread(BBox bbox,boolean isDB){
		this.bbox=bbox;
		this.isDB=isDB;
	}
	
	public void getBBoxList4Boundary(){
		bbList=(isDB)? bboxDB.getBBoxList():(new BBoxDistance()).loadBBoxList(bbox);
		
		bbox=null;  //Object release
		
		Flag.totalBBox4Area=bbList.size();
		for(int i=0;i<Flag.totalBBox4Area;i++){
			Flag.bboxCenter=bbList.get(i).toCenterPoint();
			Flag.bboxCrawlingNow=( (i+1)+"/"+ Flag.totalBBox4Area ) + " ["+ Flag.bboxCenter+"]";
			if(Flag.getApiPerm()){ 
				bBoxWorker= new BBoxWorker();
				bBoxWorker.search4BBox(bbList.get(i)); 
			}
			
			bboxDB.updateBBox(bbList.get(i));
			//Object release
			bbList.set(i, null);
			bBoxWorker=null;
			Flag.searchedBBox4Area++;
		}
		
	}

	@Override
	public void run() {
		getBBoxList4Boundary();
	}

}
