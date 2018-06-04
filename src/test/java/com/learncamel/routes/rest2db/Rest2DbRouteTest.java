package com.learncamel.routes.rest2db;

import com.learncamel.routes.jdbc.DBPostgresRouteTest;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Rest2DbRouteTest extends CamelTestSupport {
    private Logger logger = LoggerFactory.getLogger(Rest2DbRouteTest.class);

    final String JDBC_DRIVER = "org.postgresql.Driver";
    final String DB_URL = "jdbc:postgresql://localhost:5432/testDB";
    final String USER = "postgres";
    final String PASS = "postgres";

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new Rest2DbRouteBuilder();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        String jdbcURL = "jdbc:postgresql://localhost:5432/testDB";
        DataSource dataSource = setupDataSource(jdbcURL);

        SimpleRegistry reg = new SimpleRegistry();
        reg.put("PGDataSource", dataSource);

        return new DefaultCamelContext(reg);

    }

    private static DataSource setupDataSource(String jdbcURL) {
        BasicDataSource ds = new BasicDataSource();
        ds.setUsername("postgres");
        ds.setPassword("postgres");
        ds.setUrl(jdbcURL);
        ds.setDriverClassName("org.postgresql.Driver");
        return ds;
    }

    @Test
    public void rest2DbRoute() throws SQLException, ClassNotFoundException {
        clearCountryDataTable();
        assertEquals(0,getDBRecordCount());
        List responseList = consumer.receiveBody("direct:dbOutput", ArrayList.class);
        logger.info("Response list size: [{}]", responseList.size());
        assertNotEquals(0, responseList.size());
    }

    private void clearCountryDataTable() throws ClassNotFoundException, SQLException {
        logger.info("Trying to clear the country table...");
        executeDBUpdate("delete from country_capital");
        logger.info("Country table cleared successfully!");
    }

    private int getDBRecordCount() throws SQLException, ClassNotFoundException {
        logger.debug("Getting DB record count in the countries table...");
        return getDBQueryLineCount("select count(*) from country_capital");
    }

    private int getDBQueryLineCount(String sql) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        Class.forName(JDBC_DRIVER);
        logger.debug("Connecting to the database...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        logger.debug("Connected successfully! Creating statement...");
        stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);
        if (!resultSet.next()){
            throw new SQLException("The result is empty");
        }
        int numberOfEntries = resultSet.getInt(1);
        resultSet.close();
        conn.close();
        return numberOfEntries;
    }

    private void executeDBUpdate(String sql) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Statement stmt = null;
        Class.forName(JDBC_DRIVER);
        logger.debug("Connecting to the database...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        logger.debug("Connected successfully! Creating statement...");
        stmt = conn.createStatement();
        stmt.executeUpdate(sql);
        conn.close();
    }
}
