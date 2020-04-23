package com.mycompany;

import com.mycompany.model.User;
import com.mycompany.service.UserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
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

import java.util.List;

public class HomePage extends WebPage {

	@SpringBean
	private UserService userService;

	public HomePage() {

		final User user = new User();
		final List<User> users = userService.findAll();
		ListDataProvider<User> listDataProvider = new ListDataProvider<>(users);

		final Form<User> form = new Form<>("form", new CompoundPropertyModel<>(user));
		Button submitButton = new Button("submit");
		final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		form.add(feedbackPanel);
		form.add(new TextField<String>("name").setRequired(true));
		form.add(new TextField<String>("email").setRequired(true));
		form.add(submitButton);
		submitButton.add(new AjaxFormSubmitBehavior(form, "click") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				userService.createUser(new User(user.getName(), user.getEmail()));
				users.add(userService.findByName(user.getName()));
				target.add(form);
			}
		});
		form.setOutputMarkupId(true);
		add(form);

		final DataView<User> dataView = new DataView<User>("users", listDataProvider) {

			@Override
			protected void populateItem(final Item<User> item) {

				final User user = item.getModelObject();
				item.add(new Label("id", user.getId()));
				item.add(new Label("name", user.getName()));
				item.add(new Label("email", user.getEmail()));
			}
		};
		dataView.setOutputMarkupId(true);
		add(dataView);
    }
}
