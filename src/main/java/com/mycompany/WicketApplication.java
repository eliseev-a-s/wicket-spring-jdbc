package com.mycompany;

import com.mycompany.config.DataInitializer;
import com.mycompany.view.HomePage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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

		DataInitializer dataInitializer = new DataInitializer();
		dataInitializer.init();
	}
}
