package com.pwc.sns;

import org.junit.After;
import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = { "classpath:testContext.xml" })
public class BaseTest {
	@Before
    public void init() {
        // custom initialization code for resources loaded by testContext.xml 
		System.out.println("Before testing...");
    }

    @After
    public void cleanup() {
        // custom cleanup code for resources loaded by testContext.xml
    	System.out.println("Cleanup test...");
    }
}
