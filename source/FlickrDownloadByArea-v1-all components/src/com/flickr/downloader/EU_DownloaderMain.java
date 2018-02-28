package com.flickr.downloader;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFileChooser;

import com.flickr.db.PhotoDB;
import com.flickr.db.UserDB;
import com.flickr.flickr.Photo;
import com.flickr.function.Flag;
import com.flickr.function.Sleep;


public class EU_DownloaderMain implements Runnable{
	

	@SuppressWarnings("rawtypes")
	@Override
	public void run() {
		
		JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        f.showSaveDialog(null);
        Flag.downloadDir=f.getSelectedFile()+"/";
		
		
		ArrayList<Future> threadList=new ArrayList<Future>();
		ArrayList<Integer> timeList=new ArrayList<Integer>();
		ExecutorService executorService;
		EU_SingleFileWorker singleFileWorker;
		UserDB userDB=new UserDB();
		PhotoDB photoDB=new PhotoDB();
		ArrayList<String> userList=userDB.getUndownloadedUserList();
		ArrayList<Photo> photoList; 
		ArrayList<String> executedPhotoList=new ArrayList<String>();
		File dir;
		File file;
		String excPhotoID;
		Flag.totalUser=userList.size();
		
		for(String userID:userList){
			photoList=photoDB.getPhotoList4User(userID);
			
			if(photoList.size()>0){
				//create directory
				dir=new File(Flag.downloadDir+userID);
				if(!dir.exists()){
					dir.mkdir();
				}
				Flag.userDownloadingNow=userID;
				Flag.downloadedPhoto4User=0;
				Flag.totalPhoto4User=photoList.size();
				executorService=Executors.newCachedThreadPool();
				for(Photo photo:photoList){
					singleFileWorker=new EU_SingleFileWorker(photo.getImgUrl(), Flag.downloadDir+userID+"/"+photo.getPhotoId()+".jpg");
					threadList.add(executorService.submit(singleFileWorker));
					timeList.add(0);
					executedPhotoList.add(photo.getPhotoId());
					while(threadList.size()>Flag.maxWorker){	// Limit thread counts
						Sleep.sleep(200);
						for(int i=threadList.size()-1;i>=0;i--){
							if(threadList.get(i).isDone() || timeList.get(i)>1500){
								threadList.remove(i);
								timeList.remove(i);
								excPhotoID=executedPhotoList.remove(i);
								file=new File(Flag.downloadDir+userID+"/"+excPhotoID+".jpg");
								if(file.exists() && file.length()>0){
									photoDB.updateDownloadedPhoto(excPhotoID);
									Flag.downloadedPhoto4User++;
								}
							}else{
								timeList.set(i, timeList.get(i)+1);
							}
						}
					}
					
					Flag.getPausePerm();
				}
				
				while(threadList.size()>0){	// Update last threads
					Sleep.sleep(200);
					for(int i=threadList.size()-1;i>=0;i--){
						if(threadList.get(i).isDone()|| timeList.get(i)>1500){
							threadList.remove(i);
							timeList.remove(i);
							excPhotoID=executedPhotoList.remove(i);
							file=new File(Flag.downloadDir+userID+"/"+excPhotoID+".jpg");
							if(file.exists() && file.length()>0){
								photoDB.updateDownloadedPhoto(excPhotoID);
								Flag.downloadedPhoto4User++;
							}
						}else{
							timeList.set(i, timeList.get(i)+1);
						}
					}
				}
				executorService.shutdown();
				while(!executorService.isTerminated()){
					Sleep.sleep(500);
				}
				executorService=null;
				threadList.clear();
				
				userDB.updateDownloadedUser(userID);
				Flag.totalUserDownloaded++;
			}else{
				//remove user and his photos
				photoDB.deletePhotos4User(userID);
				Flag.totalUser--;
			}
			photoList.clear();
			
			
		}
	}

}
