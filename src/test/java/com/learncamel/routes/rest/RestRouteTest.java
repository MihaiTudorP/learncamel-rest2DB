package com.learncamel.routes.rest;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestRouteTest extends CamelTestSupport {
    private Logger logger = LoggerFactory.getLogger(RestRouteTest.class);
    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RestRouteBuilder();
    }

    @Test
    public void restCallUSA(){
        String response = template.requestBody("direct:restCall", "USA", String.class);
        logger.info("Response: [{}]", response);
        assertNotNull(response);
    }
}
