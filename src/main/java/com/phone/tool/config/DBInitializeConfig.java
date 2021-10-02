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
                    "CREATE TABLE IF NOT EXISTS Logs(" +
                            "id varchar(128) Primary key, " +
                            "conf_id varchar(128) not null," +
                            "conf_name varchar(128) not null," +
                            "user_name varchar(128) not null," +
                            "date_time varchar(128) not null, " +
                            "raw_data blob not null," +
                            "formatted_data blob )"
            ) ;
            statement.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}