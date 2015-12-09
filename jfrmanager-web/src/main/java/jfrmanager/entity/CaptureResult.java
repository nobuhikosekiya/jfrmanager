package jfrmanager.entity;

import java.net.URI;

public class CaptureResult {

	private int status;
	private URI location;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public URI getLocation() {
		return location;
	}

	public void setLocation(URI location) {
		this.location = location;
	}

}
