package jfrmanager.entity;

import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "object")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ObjectInfo {

	private String name;
	private String contentType;
	private Date lastModified;
	private long bytes;
	private String hash;
	private Map<String, String> customMetadata;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public long getBytes() {
		return bytes;
	}

	public void setBytes(long bytes) {
		this.bytes = bytes;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Map<String, String> getCustomMetadata() {
		return customMetadata;
	}

	public void setCustomMetadata(Map<String, String> customMetadata) {
		this.customMetadata = customMetadata;
	}

}
