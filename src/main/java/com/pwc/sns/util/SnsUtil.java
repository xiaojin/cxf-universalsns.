package com.pwc.sns.util;

import java.util.UUID;

public class SnsUtil {
	
	public static String getUuid(){
		final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
	    return uuid;
	}
}
