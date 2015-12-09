package jfrmanager.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="container")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ContainerInfo {
	private int count;
	private java.util.Map<java.lang.String,java.lang.String> customMetaData;
	private String metaDataValue;
	private String name;
	private long size;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public java.util.Map<java.lang.String, java.lang.String> getCustomMetaData() {
		return customMetaData;
	}
	public void setCustomMetaData(
			java.util.Map<java.lang.String, java.lang.String> customMetaData) {
		this.customMetaData = customMetaData;
	}
	public String getMetaDataValue() {
		return metaDataValue;
	}
	public void setMetaDataValue(String metaDataValue) {
		this.metaDataValue = metaDataValue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
}
