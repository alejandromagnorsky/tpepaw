package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import ar.edu.itba.it.paw.model.Issue;

@SuppressWarnings("serial")
public class IssueStateLabel extends Label {
	
	public IssueStateLabel(String id, IModel<Issue.State> issueStateModel) {
		super(id);
		setDefaultModel(new PropertyModel<String>(issueStateModel, "caption"));
		Issue.State state = issueStateModel.getObject();
		if(state.equals(Issue.State.Open))
			add(new SimpleAttributeModifier("class", "lightblue-text"));
		else if(state.equals(Issue.State.Ongoing))
			add(new SimpleAttributeModifier("class", "yellow-text"));
		else if(state.equals(Issue.State.Completed))
			add(new SimpleAttributeModifier("class", "green-text"));
		else
			add(new SimpleAttributeModifier("class", "grey-text"));
	}

}
