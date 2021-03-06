package com.pwc.sns.interceptor;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.InterceptorChain;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import com.pwc.sns.dao.ClientDao;
import com.pwc.sns.dto.Client;
import com.pwc.sns.exception.NotAuthorizedException;


public class SecurityInterceptor extends AbstractPhaseInterceptor<Message> {
	private ServerAuthHeader serverAuthHeader;
	private static final Log LOGGER = LogFactory
			.getLog(SecurityInterceptor.class);
	
	private ClientDao clientDao;
	
	
	public void setClientDao(ClientDao clientDao){
		
		this.clientDao = clientDao;
	}

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
//		HashMap<Object, Object> responseHeader = (HashMap<Object, Object>) message.get(org.apache.cxf.message.Message.PROTOCOL_HEADERS);
		String uri = (String) message
				.get(org.apache.cxf.message.Message.PATH_INFO);
		String queryString = (String) message
				.get(org.apache.cxf.message.Message.QUERY_STRING);
		String authID = "";
		if(queryString.indexOf("udid")>=0){
			authID = queryString.substring(queryString.indexOf("udid=")+5);
		}
		LOGGER.debug("SecurityInterceptor request =============URI:" + uri
				+ "=============");
		String ip = request.getHeader("x-Forwarded-For");
		LOGGER.debug("SecurityInterceptor request =============IP:" + ip
				+ "=============");
//		String authID = (String) responseHeader.get("Authority");
		if(authID !=null && !"".equals(authID)){
			boolean isExited = shouldContinue(authID);
			if(isExited){
			}else{
				chain.abort();
				throw new NotAuthorizedException("You do not have Permission");
			}
		}else{
			chain.abort();
			throw new NotAuthorizedException("You do not have Permission");
		}
//		PrintWriter writer = null ;
//		try {
//			writer = response.getWriter();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		boolean checkInDatabase = true;
//		if (checkInDatabase) {
//			chain.abort();
//		} else {
//			this.handlerDBSearch();
//		}
	}

	public ServerAuthHeader getServerAuthHeader() {
		return serverAuthHeader;
	}

	public void setServerAuthHeader(ServerAuthHeader serverAuthHeader) {
		this.serverAuthHeader = serverAuthHeader;
	}

	private boolean shouldContinue(String udid) {
		Client client = this.clientDao.findClientByUdid(udid);
		if(client == null){
			return false;
		}
		return true;
	}
}
