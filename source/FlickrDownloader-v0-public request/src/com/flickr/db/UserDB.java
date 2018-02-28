package com.flickr.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.flickr.connection.DBManager;

public class UserDB {	
	public boolean insertUser(User u) {
		String[] names = { "user_id", "place_id", "country_code"};
		String[] values = { u.getUserId(),u.getPlaceId(),u.getCountryCode()};
		
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

}
