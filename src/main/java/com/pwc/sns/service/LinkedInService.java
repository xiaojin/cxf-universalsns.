package com.pwc.sns.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONObject;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.com.google.common.base.Joiner;
import com.pwc.sns.HttpConnectionManager;
import com.pwc.sns.dao.ClientDao;
import com.pwc.sns.exception.BadRequestException;
import com.pwc.sns.oauth.Oauth2SignObject;
import com.pwc.sns.oauth.Oauth2Signature;
import com.pwc.sns.oauth.OauthSignObject;
import com.pwc.sns.oauth.OauthSignObject.REQUESTTYPE;
import com.pwc.sns.oauth.OauthSignature;
import com.pwc.sns.service.entity.ApiEntity;
import com.pwc.sns.service.entity.ErrorResponseEntity;
import com.pwc.sns.service.entity.Linkedin;
import com.pwc.sns.service.entity.TokenResponseEntity;
import com.pwc.sns.util.ConfigProperty;
import com.pwc.sns.util.SNSConstants;
import com.pwc.sns.util.SnsUtil;

/**
 * Implementation of the interface of {@link ApiService}
 */
@CrossOriginResourceSharing(allowAllOrigins = true,
/*
 * allowOrigins = { "http://area51.mil:31415" },
 */
allowCredentials = true
// maxAge = 1,
// allowHeaders = {
// "X-custom-1", "X-custom-2"
// },
// exposeHeaders = {
// "X-custom-3", "X-custom-4"
// }
)
@Path("/linkedin")
public class LinkedInService {
	
	private ClientDao clientDao;
	
	public void setClientDao(ClientDao clientDao){
		this.clientDao = clientDao;
	}
	/**
	 *  Get Network Updates API returns the users network updates, which is the LinkedIn term for the user's feed
	 *  
	 * @return
	 * @throws IOException
	 */
	@POST
	@Path("/networkupdate")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response requestToken(ApiEntity entity) throws IOException {
			Linkedin linkedin = new Linkedin(entity);
			return Response.ok(linkedin.getNetWorkUpdate(), MediaType.APPLICATION_XML)
					.build();
	}

}
