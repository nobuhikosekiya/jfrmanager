package jfrmanager.jsfbean;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "captureResultDetail")
public class CaptureResultDetail {

	private String message;
	private String url;
	private String label;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
