package com.icbms.appserver.http;

public class ServerInfo {
	public final static String LOCAL_HOSTIP = "166.104.112.32:38200";
	//http://192.168.0.12:8080/Icbms/iot/ControlDeviceServlet?pid=5&aid=2
	public final static String LOCAL_NOTIFICATION_PATH = "Icbms/iot/ControlDeviceServlet";
	
	public final static String HERIT_SERVER = "166.104.112.34:8080";
	public static final String CSEBASE_PATH = "herit-in/herit-cse";
	
	public static final String ORIGIN = "TAS";
	
	/**
	 * 30 seconds
	 */
	public static final int HTTP_CONNECTION_TIMEOUT = 30 * 1000;
	
	/**
	 * 70 seconds
	 */
	public static final int HTTP_SOCKET_TIMEOUT = 70 * 1000;
}
