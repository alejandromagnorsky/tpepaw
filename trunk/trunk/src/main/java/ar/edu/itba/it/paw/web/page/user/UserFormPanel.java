package ar.edu.itba.it.paw.web.page.user;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.validator.PatternValidator;

import ar.edu.itba.it.paw.model.User.View;

@SuppressWarnings("serial")
public class UserFormPanel extends Panel {
	
	public UserFormPanel(String id){
		super(id);
		
		add(new RequiredTextField<String>("username"));
		PasswordTextField passwordField = new PasswordTextField("password");
		PasswordTextField confirmPasswordField = new PasswordTextField("confirmPassword");
		passwordField.setRequired(true);
		confirmPasswordField.setRequired(true);
		add(passwordField);
		add(confirmPasswordField);
		add(new RequiredTextField<String>("fullname"));
		add(new RequiredTextField<String>("email").add(new PatternValidator("[A-Za-z0-9\\._]+@[A-Za-z0-9]+(\\.[A-Za-z]{2,4}){1,2}")));
		
		IModel<List<View>> viewsModel = new LoadableDetachableModel<List<View>>() {
			@Override
			protected List<View> load() {
				return Arrays.asList(View.values());
			}
		};
		CheckGroup<View> defaultViewsGroup = new CheckGroup<View>("defaultViews");
		ListView<View> defaultViewsView = new ListView<View>("defaultViewsView", viewsModel) {
			@Override
			protected void populateItem(ListItem<View> item) {
				item.add(new Check<View>("checkbox", item.getModel()));
				item.add(new Label("name", new PropertyModel<String>(item.getModel(), "caption")));
			}
		};
		defaultViewsGroup.add(new Label("defaultViewsLabel", new ResourceModel("defaultViews")));
		defaultViewsGroup.add(new CheckGroupSelector("viewSelector"));
		defaultViewsGroup.add(defaultViewsView);
		defaultViewsView.setReuseItems(true);
		add(defaultViewsGroup);
		
		add(new FeedbackPanel("feedback"));
	}

}
