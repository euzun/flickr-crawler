package com.flickr.downloader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class EU_SingleFileWorker implements Runnable{

	BufferedInputStream in;
	RandomAccessFile raf;
	HttpURLConnection conn;
	URL mURL;
	String mOutputFile;
	String byteRange;

	byte data[];
	int numRead;
	int BUFFER_SIZE = 1024;
	
	public EU_SingleFileWorker(String fileURL, String savePath){
		try {
			mURL = new URL(fileURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		mOutputFile = new String(savePath);
	}
	@Override
	public void run() {
		try {
			//open http connection to url
			conn = (HttpURLConnection) mURL.openConnection();
			// connect to server
			conn.connect();
			// get the input stream
			in = new BufferedInputStream(this.conn.getInputStream());
			// open the output file and seek to the start location
			raf = new RandomAccessFile(mOutputFile, "rw");
			data = new byte[BUFFER_SIZE];
			while (((numRead = in.read(data, 0, BUFFER_SIZE)) != -1)) {
				// write to buffer
				raf.write(data, 0, numRead);
			}

		} catch (Exception e) {} 
		finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {}
				
			}

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {}
			}
		}

	}

}
