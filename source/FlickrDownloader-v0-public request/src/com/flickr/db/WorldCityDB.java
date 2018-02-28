package com.flickr.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.flickr.connection.DBManager;

public class WorldCityDB {
	public boolean insertCity(WorldCity wc) {
		String[] names = { "country_code", "city_name"};
		String[] values = { wc.getCountryCode(), wc.getCityName()};
		if (DBManager.insert("city", names, values) != 0) {
			return true;
		} else {
			return false;
		}
	}

	public Set<String> getCitybyCountry(String countryCode){
		String query = "SELECT DISTINCT(city_name) FROM city WHERE is_done=0 AND country_code='"+countryCode+"'";
		ResultSet rs = DBManager.ExecuteQuery(query);
		Set<String> wcList=new HashSet<String>();
		try {
			while (rs.next()) {
				if(rs.getString("city_name").length()>3){
					wcList.add(rs.getString("city_name"));
				}
			}
		} catch (SQLException e) {
		}
		return wcList;
	}
	
	
	public Vector<WorldCity> getCountryList(){
		String query = "SELECT * FROM country ORDER BY country_name";
		ResultSet rs = DBManager.ExecuteQuery(query);
		Vector<WorldCity> countryVector=new Vector<WorldCity>();
		WorldCity worldCity;
		try {
			while (rs.next()) {
				worldCity=new WorldCity();
				worldCity.setCountryCode(rs.getString("country_code"));
				worldCity.setCountryName(rs.getString("country_name"));
				countryVector.add(worldCity);
			}
		} catch (SQLException e) {
		}
		return countryVector;
	}
	
	public boolean updateCityDone(String cityName){
		if(DBManager.ExecuteNonQuery("UPDATE city SET is_done=1 WHERE city_name='"+cityName+"'")!=0)
			return true;
		return false;
	}
}
