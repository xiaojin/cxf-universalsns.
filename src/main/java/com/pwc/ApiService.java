package com.pwc;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/service")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface ApiService {

	@POST
	@Path("/profile")
	public Response postProfile(ApiEntity entity) throws IOException;
	@POST
	@Path("/message")
	public Response postMessage(ApiEntity entity) throws IOException;
	
	@POST
    public Response postData(ApiEntity entity) throws IOException;
	@GET
	@Path("/text")
	public Response get() throws IOException;
	
}
