package com.learncamel.routes.rest;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class RestRouteBuilder extends RouteBuilder {
    public void configure() throws Exception {
        from("direct:restCall")
                .to("log:?level=INFO&showBody=true")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader(Exchange.HTTP_URI, simple("https://restcountries.eu/rest/v2/alpha/${body}"))
                .to("https://restcountries.eu/rest/v2/alpha/${body}")
                .to("log:?level=INFO&showBody=true");
    }
}
