package com.mycompany.view;

import com.mycompany.model.User;
import com.mycompany.service.UserService;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HomePage extends WebPage {

	private static final Logger logger = LoggerFactory.getLogger("Admin");

	@SpringBean
	private UserService userService;

	private final WebMarkupContainer webMarkupContainer;

	public HomePage() {

		final User user = new User();
		final List<User> users = userService.findAll();
		final ListDataProvider<User> listDataProvider = new ListDataProvider<>(users);

		final Form<User> form = new Form<>("form", new CompoundPropertyModel<>(user));
		final Button submitButton = new Button("submit");
		final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");

		form.add(new TextField<String>("name").setRequired(true));
		form.add(new TextField<String>("email").add(EmailAddressValidator.getInstance()).setRequired(true));
		form.add(feedbackPanel);
		form.add(submitButton);
		form.setOutputMarkupId(true);
		add(form);

		submitButton.add(new AjaxFormSubmitBehavior(form, "click") {

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				if (userService.createUser(new User(user.getName(), user.getEmail()))) {
					users.add(userService.findByName(user.getName()));
					logger.info("Created user. Name: " + user.getName() + ", EMAIL: " + user.getEmail());
					target.add(form);
				} else {
					logger.info("Пользователь с именем " + user.getName() + " уже существует!");
					feedbackPanel.error("Пользователь с таким именем уже существует!");
				}
			}
		});

		final DataView<User> dataView = new DataView<User>("users", listDataProvider) {

			@Override
			protected void populateItem(final Item<User> item) {
				final User user = item.getModelObject();
				item.add(new Label("id", user.getId()));
				item.add(new Label("name", user.getName()));
				item.add(new Label("email", user.getEmail()));
				item.add(new AjaxLink<String>("edit") {

					@Override
					public void onClick(AjaxRequestTarget target) {
						User user = item.getModelObject();
						Session.get().setAttribute("id", user.getId());
						Session.get().setAttribute("name", user.getName());
                        Session.get().setAttribute("email", user.getEmail());
                        Session.get().bind();
                        setResponsePage(UpdateUser.class);
					}
				});

				item.add(new AjaxLink<String>("delete") {

					@Override
					public void onClick(AjaxRequestTarget target) {
						User user = item.getModelObject();
						userService.removeUser(user.getId());
						users.remove(user);
						logger.info("Delete user. ID: " + user.getId() + ", Name: " + user.getName() + ", EMAIL: " + user.getEmail());
						target.add(webMarkupContainer);
					}
				});
			}
		};

		webMarkupContainer = new WebMarkupContainer("table");
		webMarkupContainer.setOutputMarkupId(true);
		webMarkupContainer.add(dataView);
		add(webMarkupContainer);
    }
}
