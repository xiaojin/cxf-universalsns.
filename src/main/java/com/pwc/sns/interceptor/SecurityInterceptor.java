package com.pwc.sns.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.InterceptorChain;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;


public class SecurityInterceptor extends AbstractPhaseInterceptor<Message> {
	private ServerAuthHeader serverAuthHeader;
	private static final Log LOGGER = LogFactory
			.getLog(SecurityInterceptor.class);

	public SecurityInterceptor() {
		this(Phase.RECEIVE);
	}

	public SecurityInterceptor(String phase) {
		super(phase);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) message
				.get(AbstractHTTPDestination.HTTP_REQUEST);
		InterceptorChain chain = message.getInterceptorChain();
		
//		HttpServletResponse response =(HttpServletResponse)message.get(AbstractHTTPDestination.HTTP_RESPONSE);
//		PrintWriter writer = null ;
//		try {
//			writer = response.getWriter();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String uri = (String) message
				.get(org.apache.cxf.message.Message.PATH_INFO);
		LOGGER.debug("SecurityInterceptor request =============URI:" + uri
				+ "=============");
		String ip = request.getHeader("x-Forwarded-For");
		LOGGER.debug("SecurityInterceptor request =============IP:" + ip
				+ "=============");
		boolean checkInDatabase = true;
		if (checkInDatabase) {
			chain.abort();
		} else {
			this.handlerDBSearch();
		}
	}

	public ServerAuthHeader getServerAuthHeader() {
		return serverAuthHeader;
	}

	public void setServerAuthHeader(ServerAuthHeader serverAuthHeader) {
		this.serverAuthHeader = serverAuthHeader;
	}

	private void handlerDBSearch() {

	}
}
