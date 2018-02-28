package com.flickr.connection;
import java.sql.*;

public class AccessConnect
{
	public AccessConnect() {}
	
	public static Connection dbConnect(String db_connect_string, String db_userid, String db_password)
	{
		try
		{
//			DriverManager.registerDriver(new sun.jdbc.odbc.JdbcOdbcDriver());
			Connection conn = DriverManager.getConnection(db_connect_string, db_userid, db_password);
			System.out.println("connected");
			return conn;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}