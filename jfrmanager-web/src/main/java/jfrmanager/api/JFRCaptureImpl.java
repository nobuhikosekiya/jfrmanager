package jfrmanager.api;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.ws.rs.core.Response;

import jfrmanager.entity.CaptureDiagImageResult;
import jfrmanager.entity.JFRCaptureRequest;
import jfrmanager.entity.JFRCaptureResponse;
import jfrmanager.jsfbean.CaptureResultDetail;

public class JFRCaptureImpl {

	public JFRCaptureResponse execCapture(JFRCaptureRequest jfrRequest, URI requestURL) throws Exception {
		WebLogicServerAccessor svm = new WebLogicServerAccessor();

		Map<String, CaptureDiagImageResult> imagenames;

		Set<String> servers = jfrRequest.getRequestedServers();
		imagenames = svm.captureDiagImage(servers);

		Map<String, String> images = new HashMap<String, String>();
		for (String servername : imagenames.keySet()) {
			CaptureDiagImageResult captureResult = imagenames.get(servername);
			if (!captureResult.isError()) {
				images.put(servername, captureResult.getImageName());
			}
		}
		RestJFRUploaderClient restclient = new RestJFRUploaderClient();

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setTimeZone(TimeZone.getTimeZone("JST"));
		String container_name = "jfr_" + df.format(new java.util.Date());

		boolean success = createContainer(container_name);
		if (!success) {
			throw new Exception("could not create storage container");
		}

		Map<String, jfrmanager.entity.CaptureResult> results = restclient
				.orderUpload(container_name, images);

		List<CaptureResultDetail> details = new ArrayList<CaptureResultDetail>();
		for (String server : results.keySet()) {
			jfrmanager.entity.CaptureResult capres = results
					.get(server);

			CaptureResultDetail detail = new CaptureResultDetail();
			if (capres.getLocation() != null) {
				URI showasdownload = new URI(requestURL.getScheme(), null,
						requestURL.getHost(), requestURL.getPort(), capres
								.getLocation().getPath(), null, null);
				detail.setUrl(showasdownload.toString());
				detail.setLabel(showasdownload.toString());
			}
			detail.setMessage(server + " status:" + capres.getStatus());

			details.add(detail);
		}
		
		JFRCaptureResponse response = new JFRCaptureResponse();
		response.addDetails(details);
		return response;
	}

	private boolean createContainer(String container_name) throws IOException {
		RestOracleStorageProvider storage = RestOracleStorageProviderWrapper.getProvider();
		Response restres = storage.createContainer(container_name);
		if (restres.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
			return true;
		} else {
			return false;
		}
	}
}
