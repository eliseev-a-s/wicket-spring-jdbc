package com.mycompany.config;

import com.mycompany.model.User;
import com.mycompany.service.UserService;
import com.mycompany.utils.DBHelper;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger("Config");

    @SpringBean
    private UserService userService;

    private enum UserData {
        JOHN("John","john.doe@testmail.com"),
        PETR("Petr", "petrov@testmail.com"),
        JAMES("James", "smith@testmail.com");

        private final String name;
        private final String email;

        UserData(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }

    public void init() {
        logger.info("Data init has been started!!!");
        dataInit();
        logger.info("Data init has been done!!!");
    }

    private void dataInit() {

        createTable();
//        createUsers();
    }

    private void createTable() {

        Connection connection = DBHelper.getConnection();

        try (Statement statement = connection.createStatement()) {

            String sqlTable =  "CREATE TABLE users" +
                    "(id BIGINT AUTO_INCREMENT, " +
                    " name VARCHAR(255), " +
                    " email VARCHAR(255), " +
                    " PRIMARY KEY ( id ))";
            statement.executeUpdate(sqlTable);

			String sqlUsers =  "INSERT INTO users (name, email) VALUES ('Ivan', 'ivan@gmail.com'); " +
					"INSERT INTO users (name, email) VALUES ('Petr', 'petr@yahoo.com'); " +
					"INSERT INTO users (name, email) VALUES ('Alex', 'alex@gmail.com')";
			statement.executeUpdate(sqlUsers);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUsers() {

        User userJohn = new User();
        userJohn.setName(UserData.JOHN.name);
        userJohn.setEmail(UserData.JOHN.email);
        userService.createUser(userJohn);

        User userPetr = new User();
        userPetr.setName(UserData.PETR.name);
        userPetr.setEmail(UserData.PETR.email);
        userService.createUser(userPetr);

        User userJames = new User();
        userJames.setName(UserData.JAMES.name);
        userJames.setEmail(UserData.JAMES.email);
        userService.createUser(userJames);
    }
}
