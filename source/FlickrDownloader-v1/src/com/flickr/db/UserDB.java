package com.flickr.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.flickr.connection.DBManager;
import com.flickr.flickr.User;

public class UserDB {	
	public boolean insertUser(User u) {
		String[] names = { "user_id", "place_id"};
		String[] values = { u.getUserId(),u.getPlaceId()};
		
		if (checkUser(u.getUserId()) && DBManager.insert("user", names, values) != 0) {
			return true;
		}
		return false;
		
	}
	
	private boolean checkUser(String userId){
		String query = "SELECT user_id FROM photo WHERE user_id='"+userId+"'";
		ResultSet rs = DBManager.ExecuteQuery(query);
		try {
			return rs.first();
		} catch (SQLException e) {
			System.out.println("CHECK EXCEPTION");
			return false;
		}
	}

	
	public boolean IsInsertedUser(String userId){
		String query = "SELECT * FROM user WHERE user_id='"+userId+"'";
		ResultSet rs = DBManager.ExecuteQuery(query);
		try {
			return rs.first();
		} catch (SQLException e) {
			System.out.println("CHECK EXCEPTION");
			return false;
		}
	}
	
	public void resetUnFinishedUser(){
		String query="DELETE FROM photo WHERE photo.user_id NOT IN (SELECT user_id FROM user)";
		DBManager.ExecuteNonQuery(query);
	}
}
