package com.flickr.main;

import java.util.Vector;

import com.flickr.connection.DBManager;
import com.flickr.db.WorldCity;
import com.flickr.db.WorldCityDB;
import com.flickr.function.ReadWrite;

public class WorldCityMain {

	public static void main(String[] args) {
		ReadWrite rw=new ReadWrite();
		WorldCity wc=new WorldCity();
		WorldCityDB wcDB=new WorldCityDB();

		// Read city names from .txt db
		@SuppressWarnings("unchecked")
		Vector<String> country=rw.fileToVector("~\\country.txt");
		String[] names={"country_code","country_name"};
		
		for(int i=0;i<country.size();i++){
			try{
				String[] values={country.get(i).substring(0, 2),country.get(i).substring(3)};
				DBManager.insert("country", names, values);
				}
			catch(Exception e){
			}

		}


	}

}
