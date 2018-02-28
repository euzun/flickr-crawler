package com.flickr.main;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class JavaScript {
	public void testFlickrApiKey() throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		HtmlPage page = (new WebClient()).getPage("https://www.flickr.com/services/api/explore/flickr.places.find");
		String res = ((HtmlPage) page.getForms().get(1).getInputByValue("Call Method...").click()).getPage().asXml();
		System.out.println(res.substring(res.indexOf("api_key=")+8,res.indexOf("format=rest")-5));
	}

	
	public static void main(String[] args) throws Exception {
		JavaScript js=new JavaScript();
		js.testFlickrApiKey();
	}

}
