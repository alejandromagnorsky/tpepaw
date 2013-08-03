package ar.edu.itba.it.paw.model;

import org.joda.time.DateTime;

public class Work extends AbstractModel implements Comparable<Work> {

	private User user;
	private DateTime date;
	private String description;
	private Issue issue;
	private Time dedicatedTime;

	public Work(User user, DateTime date, String description,
			Time dedicatedTime, Issue issue) {
		if (user == null || issue == null)
			throw new IllegalArgumentException();
		this.user = user;
		setDate(date);
		setDescription(description);
		this.issue = issue;
		setDedicatedTime(dedicatedTime);
	}

	public Time getDedicatedTime() {
		return dedicatedTime;
	}

	public void setDedicatedTime(Time dedicatedTime) {
		if (dedicatedTime == null)
			throw new IllegalArgumentException();
		this.dedicatedTime = dedicatedTime;
		setDate(new DateTime());
	}

	public User getUser() {
		return user;
	}

	public DateTime getDate() {
		return date;
	}

	private void setDate(DateTime date) {
		if (date == null)
			throw new IllegalArgumentException();
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if(!ValidationUtils.validateLength(description, 1, 255))
			throw new IllegalArgumentException();
		this.description = description;
		setDate(new DateTime());
	}

	public Issue getIssue() {
		return issue;
	}
	
	public int compareTo(Work other) {
		return other.date.compareTo(this.date);
	}
}
