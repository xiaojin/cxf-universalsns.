package com.pwc.sns;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * Implement Multithread HttpClient
 */
public class HttpConnectionManager {

	private static HttpParams httpParams;
	private static ClientConnectionManager connectionManager;
	
	public final static int MAX_TOTAL_CONNECTIONS = 800;
	public final static int WAIT_TIMEOUT = 60000;
	public final static int MAX_ROUTE_CONNECTIONS = 400;
	public final static int CONNECT_TIMEOUT = 10000;
	public final static int READ_TIMEOUT = 10000;
	
	static {
		httpParams = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(httpParams, MAX_TOTAL_CONNECTIONS);
		ConnManagerParams.setTimeout(httpParams, WAIT_TIMEOUT);
		ConnPerRouteBean connPerRoute = new ConnPerRouteBean(MAX_ROUTE_CONNECTIONS);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);
		HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, READ_TIMEOUT);
//		SchemeRegistry register = new SchemeRegistry();
//		register.register(new Scheme("http",PlainSocketFactory.getSocketFactory(),80));
//		register.register(new Scheme("https",SSLSocketFactory.getSocketFactory(),443));
//		connectionManager = new ThreadSafeClientConnManager(httpParams,register);
	}
	/**
	 * @return instance of HttpClient
	 */
	public static HttpClient getHttpClient(){
		return new DefaultHttpClient(connectionManager,httpParams);
	}
}
