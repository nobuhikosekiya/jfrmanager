package jfrmanager.jsfbean;

import java.net.URI;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import jfrmanager.api.JFRCaptureImpl;
import jfrmanager.api.WebLogicServerAccessor;
import jfrmanager.entity.JFRCaptureRequest;
import jfrmanager.entity.JFRCaptureResponse;


@ManagedBean(name = "captureMBean")
public class JFRCapture {

	private WebLogicServerAccessor svm;
	private Set<String> servers;
	private Set<String> selectedServers;

	@ManagedProperty(value = "#{captureResultMBean}")
	private CaptureResult captureResult;

	public void setCaptureResult(CaptureResult captureResult) {
		this.captureResult = captureResult;
	}

	public Set<String> getSelectedServers() {
		return selectedServers;
	}

	public void setSelectedServers(Set<String> selectedServers) {
		this.selectedServers = selectedServers;
	}

	public Set<String> getServers() {
		return servers;
	}

	public void setServers(Set<String> servers) {
		this.servers = servers;
	}

	public JFRCapture() throws Exception {
		svm = new WebLogicServerAccessor();
		servers = svm.getServers();
	}

	public Object execCapture() throws Exception {
		
		JFRCaptureRequest jfrRequest = new JFRCaptureRequest();
		jfrRequest.setRequestedServers(selectedServers);
		
		JFRCaptureImpl capture = new JFRCaptureImpl();
		
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        URI requestURL = new URI(request.getRequestURL().toString());
		JFRCaptureResponse jfrResponse = capture.execCapture(jfrRequest, requestURL);
		
		captureResult.setCaptureResultDetail(jfrResponse.getDetails());

		return "captureResult.xhtml";
	}

//	private void createContainer(String container_name) {
//		RestOracleStorageProvider storage = new RestOracleStorageProvider(
//				"https://storage.us2.oraclecloud.com/auth/v1.0",
//				"https://myidentdom0329.storage.oraclecloud.com/v1/",
//				"Storage-myidentdom0329");
//		
//		storage.getToken();
//		Response restres = storage.createContainer(container_name);
//		
//	}

}
