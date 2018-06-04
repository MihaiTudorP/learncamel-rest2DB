package com.learncamel.routes.jdbc;

import com.learncamel.processors.jdbc.InsertProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBPostgresRouteBuilder extends RouteBuilder {
    private Logger logger = LoggerFactory.getLogger(DBPostgresRouteBuilder.class);
    public void configure() throws Exception {
        from("direct:dbInput")
                .onException(PSQLException.class).handled(true).log("Exception While inserting messages.").end()
                .to("log:?level=INFO&showBody=true")
                .process(new InsertProcessor())
                .to("jdbc:PGDataSource")
                .to("sql:select * from country_capital?dataSource=PGDataSource")
                .to("log:?level=INFO&showBody=true");
    }
}
