package com.admin.media.election.client.utils;

public class Utils {
	public static String getTruncatedText(String text, int threshold){
		if(text.length() > threshold){
			return text.substring(0, threshold)+"...";
		}
		return text;
	}
}
