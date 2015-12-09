package jfrmanager.entity;

import java.util.List;

import jfrmanager.jsfbean.CaptureResultDetail;

public class JFRCaptureResponse {

	private List<CaptureResultDetail> details;
	public void addDetails(List<CaptureResultDetail> details) {
		this.details = details;
	}

	public List<CaptureResultDetail> getDetails() {
		return details;
	}

}
