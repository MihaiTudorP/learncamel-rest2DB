package com.learncamel.processors.jdbc;

import org.apache.camel.Exchange;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsertProcessor implements org.apache.camel.Processor {
    private Logger logger = LoggerFactory.getLogger(InsertProcessor.class);
    public void process(Exchange exchange) throws Exception {
        String input = (String) exchange.getIn().getBody();
        logger.info("Input to be persisted : " + input);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(input);

        JSONObject jsonObject = (JSONObject) obj;

        String name = (String) jsonObject.get("name");

        String capital = (String) jsonObject.get("capital");

        String insertQuery = "INSERT INTO country_capital values ('"+ name + "', '" + capital + "')";
        logger.info("The insert query is : " + insertQuery);
        exchange.getIn().setBody(insertQuery);
    }
}
