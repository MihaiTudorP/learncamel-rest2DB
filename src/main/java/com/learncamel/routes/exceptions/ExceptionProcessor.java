package com.learncamel.routes.exceptions;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionProcessor implements org.apache.camel.Processor {
    private Logger logger = LoggerFactory.getLogger(ExceptionProcessor.class);
    public void process(Exchange exchange) throws Exception {
        Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        logger.error("Exception Message "  + e.getMessage());
        logger.error("Exception Class "  + e.getClass());

        String failedEndoint = (String) exchange.getProperty(Exchange.FAILURE_ENDPOINT);
        logger.error("Failed Endpoint : " + failedEndoint);


        exchange.getIn().setBody("An exception occurred in the route.");
    }
}
