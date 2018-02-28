package com.flickr.function;

import org.silvertunnel_ng.netlib.adapter.url.URLGlobalUtil;
import org.silvertunnel_ng.netlib.api.NetFactory;
import org.silvertunnel_ng.netlib.api.NetLayer;
import org.silvertunnel_ng.netlib.api.NetLayerIDs;

public class TOR {

	public static void anonymize(){
		// classic:   TcpipNetLayer with NetLayerIDs.TCPIP (--> HTTP over plain TCP/IP);
	    // anonymous: TorNetLayer with NetLayerIDs.TOR (--> HTTP over TCP/IP over Tor network):
		NetLayer lowerNetLayer = NetFactory.getInstance().getNetLayerById(NetLayerIDs.TOR); 
		// wait until TOR is ready (optional):
		lowerNetLayer.waitUntilReady();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// redirect URL handling (JVM global)
	    URLGlobalUtil.initURLStreamHandlerFactory();
	    // the following method could be called multiple times
	    // to change layer used by the global factory over the time:
	    URLGlobalUtil.setNetLayerUsedByURLStreamHandlerFactory(lowerNetLayer);
	}
	
}
