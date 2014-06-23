package com.pwc.sns.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class NotAuthorizedException extends WebApplicationException{
	private static final long serialVersionUID = 1L;
	public NotAuthorizedException(String message)
	{
		super(Response.status(Status.UNAUTHORIZED).entity(message).type(MediaType.TEXT_PLAIN).build());
	}
	
}
