package com.flickr.connection;

import java.sql.Connection;

public class DBManager
{
	public DBManager() {}

	private static String GetConnectionParams(String fieldName)
	{
		Connect connect = new Connect();
		return connect.DBConnectionParam(fieldName);
	}
	
	public static OrtakEnum.TProvider DBProvider()
	{		
		String prvT=GetConnectionParams("PROVIDER");

		if (prvT.equals("POSTGRE"))
			return  OrtakEnum.TProvider.ptPostgre;
		else if (prvT.equals("ORACLE81") || prvT.equals("ORACLE"))
			return OrtakEnum.TProvider.ptOracle;
		else if (prvT.equals("MSSQL"))
			return  OrtakEnum.TProvider.ptMSSQL; //MSSQL
		else if (prvT.equals("ACCESS"))
			return  OrtakEnum.TProvider.ptAccess;
		else  
		return  OrtakEnum.TProvider.ptMYSQL;
	}
	
	private static String GetPassword()
	{
		String result="";
		if (GetConnectionParams("OLDPASSWORD")=="E")
		{
			result +=(char)(89 + 11);
			result +=(char)(117);
			result +=(char)(97);
			result +=(char)(121) ;
			result +=(char)(101);
			result +=(char)(99 + 11);
		}
		else
		{
			result += (char)(85 + 15);
			result += (char)(120 + 1);
			result += (char)(90 + 7);
			result += (char)(41 + 8);
			result += (char)(47 + 3);
			result += (char)(45 + 6);
			result += (char)(121);
			result += (char)(99 + 2);
			result += (char)(101 + 9);
			result += (char)(119 + 2);
		}
		return result;
	}
	
	private static Connection GetConnection()
	{
		MYSQLConnect.dbConnect("jdbc:mysql://localhost/flickr?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true","root","");
	}
	
	public static OrtakEnum.TProvider Provider = DBProvider();
	public static String IP = GetConnectionParams("IP");
	public static String PORT = GetConnectionParams("PORT");
	public static String DataSource = GetConnectionParams("DATASOURCE");
	public static String InitialCatalog = GetConnectionParams("INITIALCATALOG");
	public static String Password = GetConnectionParams("PASSWORD");
	public static Connection cnn = GetConnection();
}


