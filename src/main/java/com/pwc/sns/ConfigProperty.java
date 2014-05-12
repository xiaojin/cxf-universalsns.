package com.pwc.sns;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

public class ConfigProperty {
		private static Logger logger = Logger.getLogger(ConfigProperty.class);
		private static byte[] configBinary;
		private final static String PRO_FILE = "oauthConfig.properties";
		private final static String DEV_FILE = "oauthTestConfig.properties";
		public static synchronized byte[] getConfigBinary() throws IOException {
			if(configBinary == null){
				String FILE_NAME="";
				String env = System.getProperty("env");
				if(env == null || env.equalsIgnoreCase("")|| env.equalsIgnoreCase("dev")){
					FILE_NAME = DEV_FILE;
				}
				else if(env.equalsIgnoreCase("prod")){
					FILE_NAME = PRO_FILE;
				}
				InputStream is = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(FILE_NAME);
				
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nRead;
				byte[] data = new byte[4096];
		
				while ((nRead = is.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
				}
		
				buffer.flush();
				configBinary = buffer.toByteArray();
				logger.debug("Loading config.properties file.");
			}
			return configBinary;
		}
}
