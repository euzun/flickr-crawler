package com.flickr.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.flickr.connection.DBQuery;
import com.flickr.flickr.BBox;

public class BBoxDB {
	DBQuery dbQuery=new DBQuery();
	
	public boolean insertBBox(BBox bbox) {
		String[] names = { "min_long", "min_lat","max_long","max_lat"};
		String[] values = { bbox.getMinLong()+"",bbox.getMinLat()+"",bbox.getMaxLong()+"",bbox.getMaxLat()+""};
		
		if (dbQuery.insert("bbox", names, values) != 0) {
			
			//Object release
			names=null;
			values=null;
			
			return true;
		}
		
		//Object release
		names=null;
		values=null;
		
		return false;
	}
	
	public ArrayList<BBox> getBBoxList(){
		String query="SELECT * FROM bbox WHERE is_done=0";
		ResultSet rs = dbQuery.ExecuteQuery(query);
		
		query=null;
		
		ArrayList<BBox> bboxList=new ArrayList<BBox>();
		BBox bbox;
		try {
			while (rs.next()) {
				bbox=new BBox(rs.getInt("bbox_id"), rs.getDouble("min_long"), rs.getDouble("min_lat"), rs.getDouble("max_long"), rs.getDouble("max_lat"));
				bboxList.add(bbox);
				bbox=null;
			}
			
			rs=null;
			
		} catch (SQLException e) {
		}
		return bboxList;
	}
	
	public void deleteBBox(BBox bbox){
		dbQuery.ExecuteNonQuery("DELETE FROM bbox WHERE bbox_id="+bbox.getBboxId());
	}
	
	public void updateBBox(BBox bbox){
		dbQuery.ExecuteNonQuery("UPDATE bbox SET is_done=1 WHERE bbox_id="+bbox.getBboxId());
	}
}
