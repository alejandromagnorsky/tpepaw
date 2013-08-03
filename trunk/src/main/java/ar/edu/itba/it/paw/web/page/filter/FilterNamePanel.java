package ar.edu.itba.it.paw.web.page.filter;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.web.WicketSession;

@SuppressWarnings("serial")
public class FilterNamePanel extends Panel {

	public FilterNamePanel(String id, final IModel<Project> project){
		super(id);
		TextField<String> textField;
		if (WicketSession.get().logged())
			textField = new RequiredTextField<String>("name");
		else{
			textField = new TextField<String>("name");
			setVisible(false);
		}
		add(textField);
	}
	
}
