package com.mycompany.view;

import com.mycompany.model.User;
import com.mycompany.service.UserService;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class UpdateUser extends WebPage {

    @SpringBean
    UserService userService;

    private final User user;

    public UpdateUser() {

        user = new User(Session.get().getAttribute("id"), Session.get().getAttribute("name"), Session.get().getAttribute("email"));

        final Form<User> form = new Form<>("form", new CompoundPropertyModel<>(user));
        final Button submitButton = new Button("submit");

        form.add(new FeedbackPanel("feedback"));
        form.add(new TextField<String>("name").setRequired(true));
        form.add(new TextField<String>("email").setRequired(true));
        form.add(submitButton);
        add(form);

        submitButton.add(new AjaxFormSubmitBehavior(form, "click") {

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                userService.updateUser(user);
                setResponsePage(HomePage.class);
            }
        });
    }
}
