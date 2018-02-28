package com.flickr.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.flickr.connection.DBManager;
import com.flickr.flickr.Photo;


public class PhotoDB {
	
	public boolean hasDuplicate(String photoId){
		String query = "SELECT photo_id FROM photo WHERE photo_id='"+photoId+"'";
		ResultSet rs = DBManager.ExecuteQuery(query);
		try {
			return rs.first();
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean hasValidSoftware(String software){
		String query = "SELECT * FROM banned_software WHERE INSTR('"+software+"',software_name)>0";
		ResultSet rs = DBManager.ExecuteQuery(query);
		try {
			if (rs.next())
				return false;
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean insertPhotoID(Photo photo) {
		String[] names = { "user_id", "photo_id", "flickr_make",
				"flickr_model", "flickr_software", "img_width", "img_height",
				"exposure_time", "exposure_bias", "exposure_mode", "iso_speed",
				"img_url" };
		String[] values = { photo.getUserId(), photo.getPhotoId(),
				photo.getFlickrMake(), photo.getFlickrModel(),
				photo.getFlickrSoftware(), photo.getImgWidth() + "",
				photo.getImgHeight() + "", photo.getExposureTime(),
				photo.getExposureBias() + "", photo.getExposureMode(),
				photo.getIsoSpeed() + "", photo.getImgUrl() };
		if (DBManager.insert("photo", names, values) != 0)
			return true;
		return false;
	}

}
