package com.mljava.resource.impl;

import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mljava.resource.RestSample;



public class RestSampleImpl implements RestSample {
	


	
	@GET
	public Response sample() {	return Response.ok("HareKrishna..", MediaType.TEXT_PLAIN).build();
	}
}
