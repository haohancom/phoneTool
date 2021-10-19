package com.phone.tool.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DBInitializeConfig {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initialize() throws Exception{
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Commands(" +
                            "request varchar(128) Primary key," +
                            "response varchar(128) ," +
                            "delay varchar(128) ," +
                            "sender varchar(128) not null," +
                            "receiver varchar(128) not null," +
                            "function_name varchar(128) not null," +
                            "description varchar(128) )"
            ) ;
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Ports(" +
                            "port varchar(128) Primary key)"
            );
            statement.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}