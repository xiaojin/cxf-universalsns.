package com.pwc.sns;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

public class ConfigProperty {
		private static Logger logger = Logger.getLogger(ConfigProperty.class);
		private static byte[] configBinary;
		public static synchronized byte[] getConfigBinary() throws IOException {
			if(configBinary == null){
				InputStream is = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("access.properties");
				
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
