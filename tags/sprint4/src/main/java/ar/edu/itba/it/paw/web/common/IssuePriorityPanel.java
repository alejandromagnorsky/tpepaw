package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import ar.edu.itba.it.paw.model.Issue;

@SuppressWarnings("serial")
public class IssuePriorityPanel extends Panel {

	public IssuePriorityPanel(String id,
			IModel<Issue.Priority> issuePriorityModel) {
		super(id);
		Issue.Priority p = issuePriorityModel.getObject();

		ResourceModel text = null;

		if (p.equals(Issue.Priority.Critical)) {
			add(new SimpleAttributeModifier("class",
					"priority-img priority-critical"));
			text = new ResourceModel("priority.critical");
		} else if (p.equals(Issue.Priority.High)) {
			add(new SimpleAttributeModifier("class",
					"priority-img priority-high"));
			text = new ResourceModel("priority.high");
		} else if (p.equals(Issue.Priority.Low)) {
			add(new SimpleAttributeModifier("class",
					"priority-img priority-low"));
			text = new ResourceModel("priority.low");
		} else if (p.equals(Issue.Priority.Normal)) {
			add(new SimpleAttributeModifier("class",
					"priority-img priority-normal"));
			text = new ResourceModel("priority.normal");
		} else {
			add(new SimpleAttributeModifier("class",
					"priority-img priority-trivial"));
			text = new ResourceModel("priority.trivial");
		}

		add(new SimpleAttributeModifier("title", text.getObject()));
	}

}
