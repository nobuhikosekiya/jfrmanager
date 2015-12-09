package jfrmanager.entity;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="jfrCaptureRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class JFRCaptureRequest {

	@XmlElementWrapper(name="selectedServers")
    @XmlElement(name="value")
	Set<String> selectedServers;
	
	public Set<String> getRequestedServers() {
		return selectedServers;
	}

	public void setRequestedServers(Set<String> selectedServers) {
		this.selectedServers = selectedServers;
	}

}
