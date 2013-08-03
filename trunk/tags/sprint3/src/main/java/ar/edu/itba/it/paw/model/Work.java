package ar.edu.itba.it.paw.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "work")
public class Work extends AbstractModel implements Comparable<Work> {
	
	@ManyToOne
	@JoinColumn(name = "userid", nullable = false)
	private User user;
	
	@Column(name = "date", nullable = false)
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime date;
	
	@Column(nullable = false)
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "issueid", nullable = false)
	private Issue issue;	
	
	@Embedded
	@AttributeOverride(name="minutes", column = @Column(name="dedicatedtime") )
	@Column(nullable = false)
	Time dedicatedTime;

	public Work(){
		
	}
	
	public Work(User user, DateTime date, String description,
			Time dedicatedTime, Issue issue) {
		if (user == null || date == null || description == null
				|| !ValidationUtils.validateLength(description, 1, 255) || dedicatedTime == null || issue == null)
			throw new IllegalArgumentException();
		this.user = user;
		this.date = date;
		this.description = description;
		this.issue = issue;
		issue.addWork(user, this);
		this.dedicatedTime = dedicatedTime;
	}

	public Time getDedicatedTime() {
		return dedicatedTime;
	}

	public void setDedicatedTime(Time dedicatedTime) {
		if(dedicatedTime == null)
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
		if(date == null)
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
