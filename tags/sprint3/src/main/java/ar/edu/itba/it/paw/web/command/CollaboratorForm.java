package ar.edu.itba.it.paw.web.command;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.User;

public class CollaboratorForm {

	private Issue issue;
	private User target;
	
	public CollaboratorForm() {
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public User getTarget() {
		return target;
	}

	public void setTarget(User target) {
		this.target = target;
	}	
	
}
