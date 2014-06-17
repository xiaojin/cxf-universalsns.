package com.pwc.sns.service;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;

import com.pwc.sns.dao.ClientDao;
import com.pwc.sns.dto.Client;
import com.pwc.sns.util.SnsUtil;

@CrossOriginResourceSharing(allowAllOrigins = true, allowCredentials = true)
@Path("/signup")
public class SignupService {
	
	private ClientDao clientDao;
	
	public void setClientDao(ClientDao clientDao){
		this.clientDao = clientDao;
	}
	
	@POST
	@Path("/new")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response newClient(@QueryParam("name") String name,SignupEntity entity)
			throws IOException {
		Client client = new Client();
		String myname = entity.getName() == null ? "": entity.getName();
		String phone = entity.getPhone() == null ? "" : entity.getPhone();
		String email = entity.getEmail() == null ? "" : entity.getEmail();
		String callbackurl = entity.getCallbackUrl() == null ? "" : entity.getCallbackUrl();
		String clientType = entity.getClientType() == null ? "" : entity.getClientType();
		String udid = SnsUtil.getUuid();
		client.setName(myname);
		client.setPhone(phone);
		client.setEmail(email);
		client.setCallbackUrl(callbackurl);
		client.setClientType(clientType);
		client.setUdid(udid);
		client.setStatus(0);
		clientDao.addClient(client);
		ResponseBuilder rb = Response.ok(udid, MediaType.TEXT_PLAIN);
		return rb.build();
	}
}
