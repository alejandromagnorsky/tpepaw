package ar.edu.itba.it.paw.web.command;

import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.Comment;
import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.User;


public class CommentForm {
	private Issue issue;
	private String description;

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Comment build(User user){
		return new Comment(user, new DateTime(), this.description, this.issue);
	}
}
