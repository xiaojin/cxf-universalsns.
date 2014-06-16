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

import com.pwc.sns.dao.ClientDao;

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
	public Response newClient(@QueryParam("name") String name)
			throws IOException {
		ResponseBuilder rb = Response.ok("Internal Service", MediaType.TEXT_PLAIN);
		return rb.build();
	}
}
