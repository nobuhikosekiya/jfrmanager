package jfrmanager.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import jfrmanager.api.RestOracleStorageProvider;
import jfrmanager.api.RestOracleStorageProviderWrapper;

@Path("/jfrupload")
public class RestJFRUploader {

	RestOracleStorageProvider storage;

	private boolean delete = false;

	public RestJFRUploader() throws IOException {
		storage = RestOracleStorageProviderWrapper.getProvider();
	}

	@POST
	@Path("/{container}/{object}")
	@Produces(MediaType.APPLICATION_XML)
	public Response execUploadToCS(@Context UriInfo uriInfo,
			@PathParam("container") String container_name,
			@PathParam("object") String object) {

		FileInputStream fis;
		try {
			String path = System.getenv("DOMAIN_HOME") + File.separator
					+ "servers" + File.separator
					+ System.getProperty("weblogic.Name") + File.separator
					+ "logs" + File.separator + "diagnostic_images"
					+ File.separator + object;
			File file = new File(path);

			System.out.println("found file:  " + file);
			fis = new FileInputStream(file);

			Response response = storage.upload(container_name, file.getName(),
					fis);

			ResponseBuilder response2;
			if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
				URI objecturi = uriInfo.getBaseUriBuilder()
						.path("/download/{container_name}/{object}")
						.build(container_name, file.getName());
				response2 = Response.created(objecturi);

				System.out.println("uploaded   " + file
						+ " to container: " + container_name);
				if (delete) {
					file.delete();
					System.out.println("deleted   " + file);
				}
			} else {
				response2 = Response.status(response.getStatus());
			}
			return response2.build();

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResponseBuilder response = Response.status(Response.Status.NOT_FOUND);
		return response.build();

	}
}
