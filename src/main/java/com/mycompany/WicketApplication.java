package com.mycompany;

import com.mycompany.utils.DBHelper;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class WicketApplication extends WebApplication
{
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();

		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.scan("com.mycompany");
		annotationConfigApplicationContext.refresh();
		getComponentInstantiationListeners().add(new SpringComponentInjector(this, annotationConfigApplicationContext));

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
}
