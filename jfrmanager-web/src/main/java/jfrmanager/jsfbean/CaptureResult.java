package jfrmanager.jsfbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
@ManagedBean(name = "captureResultMBean")
public class CaptureResult {

	private List<CaptureResultDetail> captureResultDetail;

	public List<CaptureResultDetail> getCaptureResultDetail() {
		return captureResultDetail;
	}

	public void setCaptureResultDetail(List<CaptureResultDetail> captureResultDetail) {
		this.captureResultDetail = captureResultDetail;
	}
}
