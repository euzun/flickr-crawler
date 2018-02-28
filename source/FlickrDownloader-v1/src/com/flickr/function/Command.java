package com.flickr.function;

import java.io.IOException;

public class Command {

	public static void run(String command) throws IOException{
		Runtime.getRuntime().exec(command);
	}
}
