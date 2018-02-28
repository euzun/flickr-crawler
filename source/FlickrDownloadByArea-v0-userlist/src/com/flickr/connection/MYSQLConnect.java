package com.flickr.connection;

import java.sql.*;

public class MYSQLConnect
{
	public MYSQLConnect() {}
	
	public static Connection dbConnect(String db_connect_string, String db_userid, String db_password)
	{
		try 
		{
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			Connection conn = DriverManager.getConnection(db_connect_string, db_userid, db_password);
			System.out.println("connected");
			return conn;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}