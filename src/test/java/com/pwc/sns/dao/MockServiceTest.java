package com.pwc.sns.dao;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.*;
import com.github.tomakehurst.wiremock.core.Options;
import com.pwc.sns.testsupport.WireMockTestClient;


import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
public class MockServiceTest {

	protected static WireMockServer wireMockServer;
	protected static WireMockTestClient testClient;

//	@ClassRule
//	public WireMockClassRule wireMockRule = new WireMockClassRule(8089);
//	
//	@Rule
//	public WireMockClassRule instanceRule = wireMockRule;
	
	@BeforeClass
	public static void setupServer(){
		setupServer(wireMockConfig());
	}
	@AfterClass
	public static void serverShutdown(){
		wireMockServer.shutdown();
	}
	
	public static void setupServer(Options options)
	{
		wireMockServer = new WireMockServer(options);
		wireMockServer.start();
		WireMock.configure();
	}
	@Before
	public void init() throws InterruptedException{
		WireMock.resetToDefault();
	}
//	@Test
//	public void exampleTest(){
//		new UrlMatchingStrategy().setUrl("");
//		MappingBuilder builder = new MappingBuilder(RequestMethod.GET, null);
//		
//		instanceRule.stubFor(builder);
////		stubFor(get(urlEqualTo(""))
////				.withHeader()
////				.willReturrn(aResponse()
////						.withStatus(200)
////						.withHeader("")
////						.withBody("<response>Some content</response>")));
//	}
}
