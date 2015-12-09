package jfrmanager.api;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jfrmanager.entity.ContainerInfo;
import jfrmanager.entity.ObjectInfo;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class RestOracleStorageProvider {
	private Client client;
	private String servicename;
	private String baseurl;
	private String authurl;
	private String authtoken;

	public RestOracleStorageProvider(String authurl, String baseurl,
			String servicename, String storageUser, String storagePassword) {
		this.authurl = authurl;
		this.baseurl = baseurl;
		this.servicename = servicename;
		client = ClientBuilder.newClient();
		getToken(storageUser, storagePassword);
	}

	private void getToken(String storageUser, String storagePassword) {
		Response response = client
				.target(authurl)
				.request()
				.header("X-Storage-User",
						servicename + ":" + storageUser)
				.header("X-Storage-Pass", storagePassword).get();
		authtoken = response.getHeaderString("X-Auth-Token");
	}

	public Response download(String container, String objectname) {
		String url = baseurl + servicename + '/' + container + '/' + objectname;
		return client.target(url).request().header("X-Auth-Token", authtoken)
				.get();
	}

	public List<ObjectInfo> listobjects(String container) {
		String url = baseurl + servicename + '/' + container;
		GenericType<List<ObjectInfo>> objectnames = new GenericType<List<ObjectInfo>>() {
		};
		return client.target(url).request(MediaType.APPLICATION_XML_TYPE)
				.header("X-Auth-Token", authtoken).get(objectnames);
	}

	public Response upload(String container, String objectname,
			FileInputStream fis) {
		MultiPart multiPart = new MultiPart();
		multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

		multiPart.bodyPart(new BodyPart(fis,
				MediaType.APPLICATION_OCTET_STREAM_TYPE));
		String url = baseurl + servicename + '/' + container + '/' + objectname;
		client = ClientBuilder.newBuilder().register(MultiPartFeature.class)
				.build();
		return client.target(url).request(MediaType.MULTIPART_FORM_DATA_TYPE)
				.accept(MediaType.APPLICATION_XML)
				.header("X-Auth-Token", authtoken)
				.put(Entity.entity(multiPart, multiPart.getMediaType()));
	}

	public Response createContainer(String container) {
		String url = baseurl + servicename + '/' + container;
		
		return client.target(url).request().accept(MediaType.APPLICATION_XML)
				.header("X-Auth-Token", authtoken).put(Entity.text(""));
	}

	public List<ContainerInfo> listContainers(String prefix) {
		String url = baseurl + servicename + "?format=xml";
		
		List<ContainerInfo> containers = client.target(url).request().accept(MediaType.APPLICATION_XML_TYPE)
				.header("X-Auth-Token", authtoken).get(new GenericType<List<ContainerInfo>>() {});
		
		List<ContainerInfo> filteredContainers = new ArrayList<ContainerInfo>();
		for (ContainerInfo containerInfo : containers) {
			if (containerInfo.getName().startsWith(prefix)) {
				filteredContainers.add(containerInfo);
			}
		}
		
		return filteredContainers;
				
	}
}
