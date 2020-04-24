package com.mycompany.config;

import com.mycompany.utils.DBHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger("DataInitializer");

    Connection connection = DBHelper.getConnection();

    public void init() {
        logger.info("Data init has been started!!!");
        dataInit();
        logger.info("Data init has been done!!!");
    }

    private void dataInit() {

        createTable();
        createUsers();
    }

    private void createTable() {

        try (Statement statement = connection.createStatement()) {

            String sqlTable =  "CREATE TABLE users" +
                    "(id BIGINT AUTO_INCREMENT, " +
                    " name VARCHAR(255) NOT NULL UNIQUE, " +
                    " email VARCHAR(255) NOT NULL, " +
                    " PRIMARY KEY ( id ))";
            statement.executeUpdate(sqlTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUsers() {

        try (Statement statement = connection.createStatement()) {

            String sqlUsers =  "INSERT INTO users (name, email) VALUES ('Ivan', 'ivan@gmail.com'); " +
                    "INSERT INTO users (name, email) VALUES ('Petr', 'petr@yahoo.com'); " +
                    "INSERT INTO users (name, email) VALUES ('Alex', 'alex@gmail.com')";
            statement.executeUpdate(sqlUsers);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
