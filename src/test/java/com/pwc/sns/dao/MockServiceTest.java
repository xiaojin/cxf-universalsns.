package com.pwc.sns.dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.*;
import com.github.tomakehurst.wiremock.client.*;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.http.RequestMethod;
public class MockServiceTest {
	
	public MockServiceTest(){
		
	}
	protected static WireMockServer wireMockServer;
	@ClassRule
	public WireMockClassRule wireMockRule = new WireMockClassRule(8089);
	
	@Rule
	public WireMockClassRule instanceRule = wireMockRule;
	
	@BeforeClass
	public static void setupServer(){
		
	}
	@AfterClass
	public static void serverShutdown(){
		
	}
	public void setupServer(Options options)
	{
	}
	
	@Test
	public void exampleTest(){
		new UrlMatchingStrategy().setUrl("");
		MappingBuilder builder = new MappingBuilder(RequestMethod.GET, null);
		
		instanceRule.stubFor(builder);
//		stubFor(get(urlEqualTo(""))
//				.withHeader()
//				.willReturrn(aResponse()
//						.withStatus(200)
//						.withHeader("")
//						.withBody("<response>Some content</response>")));
	}
}
