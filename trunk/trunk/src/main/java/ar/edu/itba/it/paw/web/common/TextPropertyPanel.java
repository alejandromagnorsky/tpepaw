package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.User;

@SuppressWarnings("serial")
public class TextPropertyPanel extends Panel {

	public TextPropertyPanel(String id, PropertyModel<?> model, String resource) {
		super(id);
		add(new Label("propertyLabel", new StringResourceModel(resource, null)));
		add(new Label("separator", ": "));
		add(new Label("propertyValue", model));
		Object value = model.getObject();
		if (value == null || (value instanceof String && ((String)value).isEmpty()))
			setVisible(false);
	}
	
	public TextPropertyPanel(String id, DateTime date, String resource) {
		super(id);
		add(new Label("propertyLabel", new StringResourceModel(resource, null)));
		add(new Label("separator", ": "));
		DateTimePanel panel = new DateTimePanel("propertyValue", date);
		add(panel);
		if (date == null)
			setVisible(false);
	}
	
	public TextPropertyPanel(String id, IModel<User> model, String resource) {
		super(id);
		add(new Label("propertyLabel", new StringResourceModel(resource, null)));
		add(new Label("separator", ": "));
		UsernamePanel panel = new UsernamePanel("propertyValue", model);
		add(panel);
		if (model.getObject() == null)
			setVisible(false);
	}

}
