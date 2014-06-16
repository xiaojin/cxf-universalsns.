package com.pwc.sns.util;

import org.junit.Test;

public class SnsUtilTest {

	@Test
	public void testGetUuid(){
		assert(SnsUtil.getUuid().length()>0);
	}
}
