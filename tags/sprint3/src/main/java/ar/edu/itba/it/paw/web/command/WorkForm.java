package ar.edu.itba.it.paw.web.command;

import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Work;

public class WorkForm {

	private Time dedicatedTime;
	private String description;
	private Issue issue;
	private Work originalWork; // Only for editing circumstances

	public Time getDedicatedTime() {
		return dedicatedTime;
	}

	public void setDedicatedTime(Time dedicatedTime) {
		this.dedicatedTime = dedicatedTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	
	public Work getOriginalWork() {
		return originalWork;
	}

	public void setOriginalWork(Work originalWork) {
		this.originalWork = originalWork;
	}

	public Work build(User user) {
		return new Work(user, new DateTime(), description, dedicatedTime, issue);
	}

	public void update(){
		originalWork.setDedicatedTime(dedicatedTime);
		originalWork.setDescription(description);		
	}
}
