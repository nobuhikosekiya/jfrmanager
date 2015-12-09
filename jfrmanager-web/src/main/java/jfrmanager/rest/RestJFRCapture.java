package jfrmanager.rest;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import jfrmanager.api.JFRCaptureImpl;
import jfrmanager.entity.JFRCaptureRequest;
import jfrmanager.entity.JFRCaptureResponse;
import jfrmanager.jsfbean.CaptureResultDetail;

@Path("/capture")
public class RestJFRCapture {

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_HTML)
	public Response execCapture(@Context UriInfo uriInfo, JFRCaptureRequest jfrRequest) {
		
		JFRCaptureImpl capture = new JFRCaptureImpl();
        URI requestURL = uriInfo.getRequestUri();
		try {
			JFRCaptureResponse jfrResponse = capture.execCapture(jfrRequest, requestURL);
			
			StringBuffer sb = new StringBuffer();
			sb.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>JFR Capture Result</title></head><body>");
			for (CaptureResultDetail detail : jfrResponse.getDetails()) {
				sb.append("<a href=" + detail.getUrl() + ">");
				sb.append(detail.getLabel());
				sb.append("</a>");
				sb.append(" " + detail.getMessage());
				sb.append("<br/>");
			}
			sb.append("</body></html>");
			return Response.ok().entity(sb.toString()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		
	}
}
