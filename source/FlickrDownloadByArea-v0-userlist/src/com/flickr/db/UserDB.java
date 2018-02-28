package com.flickr.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.flickr.connection.DBQuery;
import com.flickr.flickr.User;

public class UserDB {	
	DBQuery dbQuery=new DBQuery();
	public boolean insertUser(User u) {
		if(IsInsertedUser(u.getUserId())){
			return false;
		}
		String[] names = { "user_id","user_name", "bbox_id"};
		String[] values = { u.getUserId(),u.getUserName().replace("'", "").replace("\"", "").replace("/", "").replace("\\","").trim(),u.getBboxId()+""};
		
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
		String query = "SELECT * FROM user WHERE user_id='"+userId+"'";
		ResultSet rs = dbQuery.ExecuteQuery(query);
		query=null;
		
		try {
			return rs.first();
		} catch (SQLException e) {
			rs=null;
			return false;
		}
	}
	
	public void resetUnFinishedUser(){
		String query="DELETE FROM photo WHERE photo.user_id NOT IN (SELECT user_id FROM user)";
		dbQuery.ExecuteNonQuery(query);
		query=null;
	}
	
	public void updateStat(){
		
	}
}
