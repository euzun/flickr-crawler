package com.flickr.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.flickr.connection.DBQuery;
import com.flickr.flickr.Photo;

public class PhotoDB {
	DBQuery dbQuery=new DBQuery();
	
	public boolean insertPhoto(Photo p){
		if(IsInsertedPhoto(p.getPhotoId())){
			return false;
		}
		
//		String[] names = {"user_id","photo_id",
//				"flickr_make","flickr_model","flickr_software",
//				"img_width","img_height",
//				"exposure_time","exposure_bias","exposure_mode",
//				"iso_speed","upload_date","original_date","modified_date","img_url"
//				};
//		String[] values={p.getUserId(),p.getPhotoId(),
//				p.getFlickrMake(),p.getFlickrModel(),p.getFlickrSoftware(),
//				p.getImgWidth()+"",p.getImgHeight()+"",
//				p.getExposureTime(),p.getExposureBias(),p.getExposureMode(),
//				p.getIsoSpeed(),p.getUploadDate(),p.getOriginalDate(),p.getModifiedDate(),p.getImgUrl()};
		
		String[] names = {"user_id","photo_id",
				"img_width","img_height","upload_date","img_url"
				};
		String[] values={p.getUserId(),p.getPhotoId(),
				p.getImgWidth()+"",p.getImgHeight()+"",p.getUploadDate(),p.getImgUrl()};
		
		if (dbQuery.insert("photo", names, values) != 0) {
			//Object release
			names=null;
			values=null;
			
			return true;
		}else{
			//Object release
			names=null;
			values=null;
			
			return false;
		}
	}
	
	private boolean IsInsertedPhoto(String photoId){
		ResultSet rs = dbQuery.ExecuteQuery("SELECT photo_id FROM photo WHERE photo_id='"+photoId+"'");
		try {
			return rs.first();
		} catch (SQLException e) {
			rs=null;
			return false;
		}
	}
	
	public ArrayList<Photo> getPhotoList4User(String userID){
		ArrayList<Photo> photoList=new ArrayList<Photo>();
		Photo photo;
		ResultSet rs=dbQuery.ExecuteQuery("SELECT COUNT(*) FROM photo WHERE user_id='"+userID+"'");
		try {
			if(rs.next() && rs.getInt(1)>49){
				rs = dbQuery.ExecuteQuery("SELECT photo_id,img_url FROM photo WHERE is_downloaded=0 AND user_id='"+userID+"'");
				while (rs.next()) {
					photo=new Photo();
					photo.setPhotoId(rs.getString("photo_id"));
					photo.setImgUrl(rs.getString("img_url"));
					photoList.add(photo);
				}
			}
			rs=null;
		} catch (SQLException e) {
		}
		return photoList;
	}
	
	public void updateDownloadedPhoto(String photoID){
		dbQuery.ExecuteNonQuery("UPDATE photo SET is_downloaded=1 WHERE photo_id='"+photoID+"'");
	}
	
	public void deletePhotos4User(String userID){
		dbQuery.ExecuteNonQuery("DELETE FROM user WHERE user_id='"+userID+"'");
		dbQuery.ExecuteNonQuery("DELETE FROM photo WHERE user_id='"+userID+"'");
	}
	
	public int getInsertedPhotoUser(String userID){
		int count=0;
		ResultSet rs = dbQuery.ExecuteQuery("SELECT COUNT(*) FROM photo WHERE user_id='"+userID+"'");
		try {
			if (rs.next()) {
				count=Integer.parseInt(rs.getString(1));
			}
			rs=null;
		} catch (SQLException e) {
		}
		return count;
	}
}
