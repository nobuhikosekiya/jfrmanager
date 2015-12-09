package jfrmanager.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jfrmanager.entity.CaptureResult;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class RestJFRUploaderClient {

	public Map<String, CaptureResult> orderUpload(String container, Map<String, String> imageNames) throws Exception {
		WebLogicServerAccessor svm = new WebLogicServerAccessor();
		Map<String, String> addresses = svm.getServerAddressList(imageNames
				.keySet());

	     String resourceName = "application.properties"; 
	     ClassLoader loader = Thread.currentThread().getContextClassLoader();
	     Properties prop = new Properties();
	     try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
	         prop.load(resourceStream);
	     }
	     
		Map<String, CaptureResult> resutls = new HashMap<String, CaptureResult>();
		for (String servername : addresses.keySet()) {
			String imageName = imageNames.get(servername);
			String serveraddress = addresses.get(servername);
			Client client = ClientBuilder.newBuilder().newClient();
			Response response = client
					.target("http://" + serveraddress + "/jfrmanager/resources/jfrupload/" + container + "/" + imageName)
					.register(HttpAuthenticationFeature.basic(prop.getProperty("app.username"), prop.getProperty("app.password")))
					.request()
					.accept(MediaType.APPLICATION_XML).post(null);
			
			resutls.put(servername, createResultMessage(response));
		}
		
		return resutls;

	}

	private CaptureResult createResultMessage(Response response) {
		CaptureResult res = new CaptureResult();
		res.setLocation(response.getLocation());
		res.setStatus(response.getStatus());
		return res;
	}


}
