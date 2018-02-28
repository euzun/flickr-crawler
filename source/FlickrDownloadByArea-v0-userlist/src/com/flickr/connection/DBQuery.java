package com.flickr.connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBQuery {
	
	public ResultSet ExecuteQuery( String sqlStr ) 
	{
		try
		{
			Statement stmt = DBManager.cnn.createStatement();
			ResultSet rset = stmt.executeQuery( sqlStr );
			
			stmt=null;
			
			return rset;	
		}
		catch (SQLException e)
		{
			System.err.println( e.getMessage() );
			return null;
		}
	}
	
	public int ExecuteNonQuery( String sqlStr ) 
	{
		try
		{
			Statement stmt = DBManager.cnn.createStatement();
			stmt.executeUpdate( sqlStr, Statement.RETURN_GENERATED_KEYS );
			ResultSet rs = stmt.getGeneratedKeys();
			
			stmt=null;
			
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
	
	public String cleanString( String str ) {
		
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
	
	private boolean isInt( String data ) {
		if( data.equals( "" ) )
			return false;
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(data);
		boolean result = m.matches();
		
		p=null;
		m=null;
		
		return result;
	}
	
	public int insert( String tableName, String[] names, String[] values ) {
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
		
		tNames=null;
		tValues=null;
		
		return ExecuteNonQuery( todo );
	}
	
	public int update( String tableName, String[] names, String[] values, String clause ) {
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
		
		tNamesAndValues=null;
		
		return ExecuteNonQuery( todo );
	}
	
	public String[] select( String tableName, String[] fields, String clause ) {
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
			
			rs=null;
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		
		tFields=null;
		tClause=null;
		todo=null;
		
		return result;
	}
}
