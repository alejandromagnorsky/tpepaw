package ar.edu.itba.it.paw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "comment")
public class Comment extends AbstractModel implements Comparable<Comment> {

	@ManyToOne
	@JoinColumn(name = "userid", nullable = false)
	private User user;

	@Column(name = "date", nullable = false)
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime date;

	@Column(nullable = false)
	private String description;

	@ManyToOne
	@JoinColumn(name = "issueid", nullable = false)
	private Issue issue;

	Comment() {

	}

	public Comment(User user, DateTime date, String description, Issue issue) {
		if (user == null || date == null 
				|| !ValidationUtils.validateLength(description, 1, 255) || issue == null)
			throw new IllegalArgumentException();
		this.user = user;
		this.date = date;
		this.description = description;
		this.issue = issue;
		issue.addComment(user, this);
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
