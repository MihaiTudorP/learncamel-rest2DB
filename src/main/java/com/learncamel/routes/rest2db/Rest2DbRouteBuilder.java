package com.learncamel.routes.rest2db;

import com.learncamel.processors.jdbc.InsertProcessor;
import com.learncamel.routes.exceptions.ExceptionProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rest2DbRouteBuilder extends RouteBuilder {
    private Logger logger = LoggerFactory.getLogger(Rest2DbRouteBuilder.class);
    public void configure() throws Exception {

        from("timer:learnTimer?period=10s")
                .onException(PSQLException.class, Exception.class).handled(true).log("Exception While inserting messages.").process(new ExceptionProcessor()).end()
                .to("log:?level=INFO&showBody=true")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader(Exchange.HTTP_URI, simple("http://restcountries.eu/rest/v2/alpha/us"))
                .to("http://restcountries.eu/rest/v2/alpha/us").convertBodyTo(String.class)
                .to("log:?level=INFO&showBody=true")
                .process(new InsertProcessor())
                .to("jdbc:PGDataSource")
                .to("sql:select * from country_capital?dataSource=PGDataSource")
                /*.to("direct:dbOutput")*/;
    }
}
