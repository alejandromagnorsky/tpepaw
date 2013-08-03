package ar.edu.itba.it.paw.web.page.issue;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.validator.StringValidator;

@SuppressWarnings("serial")
public class CommentFormPanel extends Panel {
	
	public CommentFormPanel(String id){
		super(id);
		add(new TextArea<String>("description").setRequired(true).add(new StringValidator.MaximumLengthValidator(255)));
		add(new FeedbackPanel("feedback"));
	}

}
