package com.flickr.connection;

import java.io.*; 
import java.net.URL;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public  class Connect {

	public String GetParamValue(String paramTable, String paramField,String defaultValue)
	{
		String  result=defaultValue;
		
		URL url=  this.getClass().getResource("");
		String webInf="WEB-INF";
		String filePath=url.getPath().replace("%20", " ");

		filePath =filePath.substring(0,filePath.indexOf(webInf)+webInf.length());
		
//		String filePath = Sessions.getCurrent().getWebApp().getRealPath("WEB-INF");
//		filePath +="/connection.xml";
		
		try
		{
			File file = new File(filePath);
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (file);
			doc.getDocumentElement().normalize();
			NodeList listParamTable = doc.getElementsByTagName(paramTable);

			for (int s = 0; s < listParamTable.getLength(); s++)
			{
				Node fstNode = listParamTable.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element fstElmnt = (Element) fstNode;
					NodeList fstNmElmntLst = fstElmnt.getElementsByTagName(paramField);
					Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
					NodeList fstNm = fstNmElmnt.getChildNodes();
					result = ((Node) fstNm.item(0)).getNodeValue();
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	
/*	public String ProjectParam(String paramField,String defaultValue)
	{
	   return GetParamValue("PROJECTPARAM",paramField,defaultValue);
	}
	
	public String ProjectParam(String paramField)
	{
		return ProjectParam(paramField,"");
	}*/
	
	public String DBConnectionParam(String paramField,String defaultValue)
	{
		return "";//GetParamValue("CONNECTIONPARAM",paramField,defaultValue);
	}
	
	public String DBConnectionParam(String paramField)
	{
	   return "";//DBConnectionParam(paramField,"");
	}
	
}
