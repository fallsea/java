package com.fallsea.id.snowflake;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @Description: 测试类
 * @Copyright: 2017 www.fallsea.com Inc. All rights reserved.
 * @author: fallsea
 * @version 1.0
 * @date: 2017年12月11日 下午1:35:17
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	
    	long startTime = System.currentTimeMillis();
    	
    	for (int i = 0; i < 1000000; i++) {
			
    		long id = IdUtil.getId();
			System.err.println(id);
		}
    	
    	long endTime = System.currentTimeMillis();
    	
    	System.out.println(endTime - startTime );
    	
    	
        assertTrue( true );
    }
}
