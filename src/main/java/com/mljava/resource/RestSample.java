package com.mljava.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/rest")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface RestSample {
	
	@GET
	Response sample();
	
}
