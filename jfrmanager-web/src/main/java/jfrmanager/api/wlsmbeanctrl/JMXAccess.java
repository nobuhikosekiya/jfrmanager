package jfrmanager.api.wlsmbeanctrl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.naming.NamingException;

public class JMXAccess {
	private MBeanServerConnection connection;
	private JMXConnector connector;
	private final ObjectName service;

	public JMXAccess() {
		try {
			service = new ObjectName(
					"com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
		} catch (MalformedObjectNameException e) {
			throw new AssertionError(e.getMessage());
		}
	}

	public void connect() throws Exception {

	     String resourceName = "weblogic.properties"; 
	     ClassLoader loader = Thread.currentThread().getContextClassLoader();
	     Properties prop = new Properties();
	     try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
	         prop.load(resourceStream);
	     }
	     
	     
		String protocol = "t3";
		Integer portInteger = Integer.valueOf("7001");
		int port = portInteger.intValue();
		String jndiroot = "/jndi/";
		String mserver = "weblogic.management.mbeanservers.domainruntime";
		JMXServiceURL serviceURL = new JMXServiceURL(protocol, "localhost",
				port, jndiroot + mserver);

		Hashtable h = new Hashtable();
		h.put(Context.SECURITY_PRINCIPAL, prop.get("admin.username"));
		h.put(Context.SECURITY_CREDENTIALS, prop.get("admin.password"));
		h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,
				"weblogic.management.remote");
		h.put("jmx.remote.x.request.waiting.timeout", new Long(10000));
		connector = JMXConnectorFactory.connect(serviceURL, h);
		connection = connector.getMBeanServerConnection();

	}

	private ObjectName[] getServerRuntimes() throws Exception {
		return (ObjectName[]) connection
				.getAttribute(service, "ServerRuntimes");
	}

	public String captureDiagImage(String servername) throws Exception {
		ObjectName[] serverRT = getServerRuntimes();
		System.out.println("got server runtimes");
		int length = (int) serverRT.length;
		for (int i = 0; i < length; i++) {

			String name = (String) connection.getAttribute(serverRT[i], "Name");
			String state = (String) connection.getAttribute(serverRT[i],
					"State");
			System.out.println("Server name: " + name + ".   Server state: "
					+ state);

			if (name.equals(servername)) {
				ObjectName wldfruntime = (ObjectName) connection.getAttribute(
						serverRT[i], "WLDFRuntime");

				ObjectName wldfimageruntime = (ObjectName) connection
						.getAttribute(wldfruntime, "WLDFImageRuntime");
				ObjectName WLDFImageCreationTaskRuntimeMBean = (ObjectName) connection
						.invoke(wldfimageruntime, "captureImage",
								new Object[] { 1 },
								new String[] { "java.lang.Integer" });

				String diagimagepath = (String) connection.getAttribute(
						WLDFImageCreationTaskRuntimeMBean, "Description");
				
				while(true) {
					String status = (String) connection.getAttribute(
							WLDFImageCreationTaskRuntimeMBean, "Status");
					if (status.equals("Completed")) {
						break;
					}
					Thread.sleep(500);
				}

				return diagimagepath;
			}

		}
		return null;
	}

	public Map<String, String> getServerAddress() throws Exception {
		Map<String, String> servers = new HashMap<String, String>();
		try {
			ObjectName[] serverRT = getServerRuntimes();
			int length = (int) serverRT.length;
			for (int i = 0; i < length; i++) {
				String name = (String) connection.getAttribute(serverRT[i],
						"Name");
				String listenAddress = ((String) connection.getAttribute(
						serverRT[i], "ListenAddress")).split("/")[1];
				int listenPort = (Integer) connection.getAttribute(serverRT[i],
						"ListenPort");
				System.out.println("Server name: " + name
						+ ".   Server listenAddress: " + listenAddress
						+ " port: " + listenPort);
				servers.put(name,
						listenAddress + ":" + Integer.toString(listenPort));
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return servers;
	}

	public class MNServerAddress {
		public String listenAddress;
		public int listenPort;
	}

	public Set<String> getServers() throws Exception {
		Set<String> servers = new HashSet<String>();
		ObjectName[] serverRT;
		serverRT = getServerRuntimes();

		System.out.println("got server runtimes");
		int length = (int) serverRT.length;
		for (int i = 0; i < length; i++) {
			String name = (String) connection.getAttribute(serverRT[i], "Name");
			servers.add(name);
		}
		return servers;
	}

	public Map<String, String> getServerAddress(List<String> requestedServers)
			throws Exception {

		Map<String, String> servers = new HashMap<String, String>();
		try {
			ObjectName[] serverRT = getServerRuntimes();
			System.out.println("got server runtimes");
			int length = (int) serverRT.length;
			for (int i = 0; i < length; i++) {
				String name = (String) connection.getAttribute(serverRT[i],
						"Name");
				if (!requestedServers.contains(name)) {
					continue;
				}
				String listenAddress = ((String) connection.getAttribute(
						serverRT[i], "ListenAddress")).split("/")[1];
				int listenPort = (Integer) connection.getAttribute(serverRT[i],
						"ListenPort");
				System.out.println("Server name: " + name
						+ ".   Server listenAddress: " + listenAddress
						+ " port: " + listenPort);
				servers.put(name, listenAddress + Integer.toString(listenPort));
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return servers;
	}

}