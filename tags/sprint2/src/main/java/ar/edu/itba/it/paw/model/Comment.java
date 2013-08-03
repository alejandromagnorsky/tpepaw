package ar.edu.itba.it.paw.model;

import org.joda.time.DateTime;

public class Comment extends AbstractModel implements Comparable<Comment> {

	private User user;
	private DateTime date;
	private String description;
	private Issue issue;

	public Comment(User user, DateTime date, String description, Issue issue) {
		if (user == null || date == null 
				|| !ValidationUtils.validateLength(description, 1, 255) || issue == null)
			throw new IllegalArgumentException();
		this.user = user;
		this.date = date;
		this.description = description;
		this.issue = issue;
	}

	public User getUser() {
		return user;
	}

	public DateTime getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public Issue getIssue() {
		return issue;
	}
	
	public int compareTo(Comment other) {
		return other.date.compareTo(this.date);
	}

}
