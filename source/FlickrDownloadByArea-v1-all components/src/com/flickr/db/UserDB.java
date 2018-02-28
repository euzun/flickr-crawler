package com.flickr.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.flickr.connection.DBQuery;
import com.flickr.flickr.User;

public class UserDB {	
	DBQuery dbQuery=new DBQuery();
	public boolean insertUser(User u) {
		
		if(IsInsertedUser(u.getUserId())){
			return false;
		}
		String[] names = { "user_id","user_name", "bbox_id"};
		String[] values = { u.getUserId(),u.getUserName(),u.getBboxId()+""};
		
		if (dbQuery.insert("user", names, values) != 0) {
			//Object release
			names=null;
			values=null;
			
			return true;
		}else{
			String[] values2 = { u.getUserId(),"",u.getBboxId()+""};
			dbQuery.insert("user", names, values2);
			//Object release
			values2=null;
		}
		//Object release
		names=null;
		values=null;
		
		
		return false;
	}
	
	public boolean IsInsertedUser(String userId){
		ResultSet rs = dbQuery.ExecuteQuery("SELECT * FROM user WHERE user_id='"+userId+"'");
		try {
			return rs.first();
		} catch (SQLException e) {
			rs=null;
			return false;
		}
	}
	
	public void resetUnFinishedUser(){
		dbQuery.ExecuteNonQuery("DELETE FROM photo WHERE photo.user_id NOT IN (SELECT user_id FROM user)");
	}
	
	public ArrayList<String> getUnfinishedUserList(){
		ArrayList<String> userList=new ArrayList<String>();
		ResultSet rs = dbQuery.ExecuteQuery("SELECT user_id FROM user WHERE is_done=0 ORDER BY bbox_id");
		try {
			while (rs.next()) {
				userList.add(rs.getString("user_id"));
			}
			rs=null;
		} catch (SQLException e) {
		}
		return userList;
	}
	
	public void updateUser(String userID){
		dbQuery.ExecuteNonQuery("UPDATE user SET is_done=1 WHERE user_id='"+userID+"'");
	}
	
	public ArrayList<String> getUndownloadedUserList(){
		ArrayList<String> userList=new ArrayList<String>();
		ResultSet rs = dbQuery.ExecuteQuery("SELECT user_id FROM user WHERE is_done=1 AND is_downloaded=0");
		try {
			while (rs.next()) {
				userList.add(rs.getString("user_id"));
			}
			rs=null;
		} catch (SQLException e) {
		}
		return userList;
	}
	
	public void updateDownloadedUser(String userID){
		dbQuery.ExecuteNonQuery("UPDATE user SET is_downloaded=1 WHERE user_id='"+userID+"'");
	}
}
