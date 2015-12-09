package jfrmanager.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jfrmanager.api.wlsmbeanctrl.JMXAccess;
import jfrmanager.entity.CaptureDiagImageResult;

public class WebLogicServerAccessor {

	public Set<String> getServers() throws Exception {
		JMXAccess jmx = new JMXAccess();
		jmx.connect();
		return jmx.getServers();
	}

	public Map<String, String> getServerAddressList(Set<String> requestedServers)
			throws Exception {
		Map<String, String> results = new HashMap<String, String>();
		JMXAccess jmx = new JMXAccess();
		jmx.connect();
		Map<String, String> servers = jmx.getServerAddress();

		for (String servername : requestedServers) {
			results.put(servername, servers.get(servername));
		}

		return results;

	}

	public Map<String, CaptureDiagImageResult> captureDiagImage(
			Set<String> requestedServers) throws Exception {
		JMXAccess jmxaccess = new JMXAccess();
		jmxaccess.connect();
		Map<String, CaptureDiagImageResult> results = new HashMap<String, CaptureDiagImageResult>();
		for (String servername : requestedServers) {
			CaptureDiagImageResult result = new CaptureDiagImageResult();
			try {
				String imagepath = jmxaccess.captureDiagImage(servername);
				result.setImagePath(imagepath);
			} catch (Exception e) {
				e.printStackTrace();
				result.setErrorDescription(e.getMessage());
				result.setError(true);
			}
			results.put(servername, result);
		}
		return results;

	}
}
