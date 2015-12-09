package jfrmanager.rest;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import jfrmanager.api.RestOracleStorageProvider;
import jfrmanager.api.RestOracleStorageProviderWrapper;
import jfrmanager.entity.ContainerInfo;
import jfrmanager.entity.ObjectInfo;

@Path("/download")
public class RestJFRDownloader {
	RestOracleStorageProvider storage;

	public RestJFRDownloader() throws IOException {
		storage = RestOracleStorageProviderWrapper.getProvider();
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response listContainers(@Context UriInfo uriInfo) {
		List<ContainerInfo> containers = storage.listContainers("jfr_");
		
		StringBuffer sb = new StringBuffer();
		sb.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>container list</title></head><body>");
		for (ContainerInfo containerInfo : containers) {
			sb.append("<a href=" + uriInfo.getAbsolutePath() + "/"
					+ containerInfo.getName() + ">"
					+ containerInfo.getName() + "</a>");
			sb.append(" files: " + containerInfo.getCount());
			sb.append("<br/>");
		}
		sb.append("</body></html>");

		return Response.ok().entity(sb.toString()).build();
	}
	
	@GET
	@Path("/{container_name}")
	@Produces(MediaType.TEXT_HTML)
	public Response listObjects(@Context UriInfo uriInfo, @PathParam("container_name") String container_name) {
		List<ObjectInfo> objects = storage.listobjects(container_name);

		StringBuffer sb = new StringBuffer();
		sb.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>object list</title></head><body>");
		for (ObjectInfo object : objects) {
			sb.append("<a href=" + uriInfo.getAbsolutePath() + "/"
					+ object.getName() + ">"
					+ object.getName() + "</a>");
			sb.append("  bytes: " + object.getBytes());
			sb.append("<br/>");
		}
		sb.append("</body></html>");
		return Response.ok().entity(sb.toString()).build();
	}

	@GET
	@Path("/{container_name}/{object}/")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(
			@PathParam("container_name") String container_name,
			@PathParam("object") String object) {

		Response response = storage.download(container_name, object);

		return response;
	}

}
