package com.pwc;

import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
/**
 * Interface of the social framework webservice
 */

@Path("/service")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface ApiService {

	/**
	 * Function of getProfile
	 * @param {@link ApiEntity}
	 * @return Response 
	 */
	@POST
	@Path("/profile")
	public Response postProfile(ApiEntity entity) throws IOException;
	/**
	 * Function of postStatus
	 * @param {@link ApiEntity}
	 * @return Response
	 */
	@POST
	@Path("/message")
	public Response postMessage(ApiEntity entity) throws IOException;

}
