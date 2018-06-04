package com.learncamel.routes.rest2db;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;

public class Rest2DbRouteTest extends CamelTestSupport {
    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new Rest2DbRouteBuilder();
    }
}
