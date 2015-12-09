package jfrmanager.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RestOracleStorageProviderWrapper {

	public static RestOracleStorageProvider getProvider() throws IOException {
	     String resourceName = "storagecloud.properties"; 
	     ClassLoader loader = Thread.currentThread().getContextClassLoader();
	     Properties prop = new Properties();
	     try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
	         prop.load(resourceStream);
	     }
	         
		RestOracleStorageProvider storage = new RestOracleStorageProvider(
				prop.getProperty("authurl"),
				prop.getProperty("baseurl"),
				prop.getProperty("servicename"),
				prop.getProperty("username"),
				prop.getProperty("password"));
		
		return storage;
	}
}
