package jfrmanager.entity;

import java.io.File;

public class CaptureDiagImageResult {

	private String imagePath;
	private String imageName;
	private String errorDescription;
	private boolean isError;

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
		this.imageName = new File(imagePath).getName();
	}

	public void setErrorDescription(String error) {
		errorDescription = error;
	}
	
	public String getErrorDescription() {
		return errorDescription;
	}

	public void setError(boolean b) {
		isError = b;
	}

	public String getImageName() {
		return imageName;
	}

	public boolean isError() {
		return isError;
	}

}
