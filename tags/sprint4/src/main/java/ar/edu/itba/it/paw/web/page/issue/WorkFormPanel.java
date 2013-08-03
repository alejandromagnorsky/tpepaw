package ar.edu.itba.it.paw.web.page.issue;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.validator.StringValidator;

import ar.edu.itba.it.paw.model.Time;

@SuppressWarnings("serial")
public class WorkFormPanel extends Panel {
	
	public WorkFormPanel(String id){
		super(id);
		add(new RequiredTextField<Time>("dedicatedTime"));
		add(new TextArea<String>("description").setRequired(true).add(new StringValidator.MaximumLengthValidator(255)));
		add(new FeedbackPanel("feedback"));
	}

}
