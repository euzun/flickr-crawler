package com.flickr.connection;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		//<!-- [SQL için :MSSQL veya MSSQL] [Oracle için : ORACLE81 veya ORACLE] [Postgre için : POSTGRE ] -->

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
		//String Language = GetConnectionParams("LANGUAGE");
		
		if (Provider == OrtakEnum.TProvider.ptOracle)
  		  return OracleConnect.dbConnect("jdbc:oracle:thin:@"+DataSource+":1521:XE", InitialCatalog, GetPassword());		    
		else if (Provider == OrtakEnum.TProvider.ptAccess)
		  return AccessConnect.dbConnect("jdbc:odbc:NAME","","");
		else if (Provider == OrtakEnum.TProvider.ptMYSQL)
			 return MYSQLConnect.dbConnect("jdbc:mysql://localhost/flickr?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true","root","");

		else
		  return null;
	}
	
	public static OrtakEnum.TProvider Provider = DBProvider();
	public static String IP = GetConnectionParams("IP");
	public static String PORT = GetConnectionParams("PORT");
	public static String DataSource = GetConnectionParams("DATASOURCE");
	public static String InitialCatalog = GetConnectionParams("INITIALCATALOG");
	public static String Password = GetConnectionParams("PASSWORD");
	public static Connection cnn = GetConnection();
	
	public static ResultSet ExecuteQuery( String sqlStr ) 
	{
		try
		{
			Statement stmt = cnn.createStatement();
			ResultSet rset = stmt.executeQuery( sqlStr );
			return rset;	
		}
		catch (SQLException e)
		{
			System.err.println( e.getMessage() );
			return null;
		}
	}
	
	public static int ExecuteNonQuery( String sqlStr ) 
	{
		try
		{
			Statement stmt = cnn.createStatement();
			stmt.executeUpdate( sqlStr, Statement.RETURN_GENERATED_KEYS );
			ResultSet rs = stmt.getGeneratedKeys();
			if( rs.next() )
			    return rs.getInt(1);
			return -1;
		}
		catch (SQLException e)
		{
			System.err.println( e.getMessage() );
			return 0;
		}		
	}
	
	public static String cleanString( String str ) {
		
		String clean_string = str;
		
		clean_string = clean_string.replaceAll( "\\\\", "\\\\\\\\" );
        clean_string = clean_string.replaceAll( "\\n","\\\\n" );
        clean_string = clean_string.replaceAll( "\\r", "\\\\r" );
        clean_string = clean_string.replaceAll( "\\t", "\\\\t" );
        clean_string = clean_string.replaceAll( "\\00", "\\\\0" );
        clean_string = clean_string.replaceAll( "'", "\\\\'" );
        clean_string = clean_string.replaceAll( "\\\"", "\\\\\"" );
        
        return clean_string;
	}
	
	private static boolean isInt( String data ) {
		if( data.equals( "" ) )
			return false;
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(data);
		boolean result = m.matches();
		return result;
	}
	
	public static int insert( String tableName, String[] names, String[] values ) {
		String tNames = "", tValues = "";
		
		for( int i=0; i<names.length; i++ ) {
			tNames += names[ i ] + ", ";
			if( isInt( values[ i ] ) )
				tValues += "'" + values[ i ] + "', ";
			else
				tValues += "'" + values[ i ] + "', ";
		}
		
		tNames = tNames.substring( 0, tNames.length()-2 );
		tValues = tValues.substring( 0, tValues.length()-2 );
		
		String todo = "INSERT INTO " + tableName + " ( " +
					tNames + " ) values ( " + tValues + " )";
//		System.out.println(todo);
		return ExecuteNonQuery( todo );
	}
	
	public static int update( String tableName, String[] names, String[] values, String clause ) {
		String tNamesAndValues = "";
		
		for( int i=0; i<names.length; i++ ) {
			tNamesAndValues += names[ i ] + "=";
			if( isInt( values[ i ] ) )
				tNamesAndValues += values[ i ] + ", ";
			else
				tNamesAndValues += "\'" + values[ i ] + "\', ";
		}
		
		tNamesAndValues = tNamesAndValues.substring( 0, tNamesAndValues.length()-2 );
		
		String todo = "UPDATE " + tableName + " SET " + tNamesAndValues + " WHERE " + clause;
//		System.out.println(todo);
		return ExecuteNonQuery( todo );
	}
	
	public static String[] select( String tableName, String[] fields, String clause ) {
		String tFields = "";
		
		for( int i=0; i<fields.length; i++ )
			tFields += fields[ i ] + ", ";
		
		tFields = tFields.substring( 0, tFields.length()-2 );
		
		String tClause = clause.equals("") ? "" : " WHERE " + clause;
		
		String todo = "SELECT " + tFields + " FROM " + tableName + tClause;
		
		String[] result = null;
		
		try {
			ResultSet rs = ExecuteQuery( todo );
			result = new String[ fields.length ];
			int i=0;
			while( rs.next() )
				result[ i ] = rs.getString( fields[ i ] );
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		
		return result;
	}
}


