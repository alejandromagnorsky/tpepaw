package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import ar.edu.itba.it.paw.model.Issue;

@SuppressWarnings("serial")
public class IssueTypePanel extends Panel {

	public IssueTypePanel(String id, IModel<Issue.Type> issueTypeModel) {
		super(id);
		Issue.Type p = issueTypeModel.getObject();

		ResourceModel text = null;

		if (p.equals(Issue.Type.Error)) {
			add(new SimpleAttributeModifier("class", "type-img type-error"));
			text = new ResourceModel("type.error");

		} else if (p.equals(Issue.Type.Improvement)) {
			add(new SimpleAttributeModifier("class",
					"type-img type-improvement"));
			text = new ResourceModel("type.improvement");

		} else if (p.equals(Issue.Type.Issue)) {
			add(new SimpleAttributeModifier("class", "type-img type-issue"));
			text = new ResourceModel("type.issue");

		} else if (p.equals(Issue.Type.NewFeature)) {
			add(new SimpleAttributeModifier("class", "type-img type-new"));
			text = new ResourceModel("type.new");

		} else {
			add(new SimpleAttributeModifier("class", "type-img type-technique"));
			text = new ResourceModel("type.technique");
		}

		add(new SimpleAttributeModifier("title", text.getObject()));
	}

}
